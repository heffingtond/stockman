package actions;

import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import beans.AuthenticationProfileBean;
import beans.OrganizationBean;
import beans.UserBean;
import core.StockManUtilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginAction extends Action
{
	
	public void execute( HttpServletRequest request,
            			 HttpServletResponse response )
	{
		String destinationPage = "/JSP/login.jsp";
		String buttonPressed =  request.getParameter( "LoginAction" );
		if ( "Login".equalsIgnoreCase( buttonPressed ) )
		{
			HttpSession session = request.getSession( false );
			if ( session == null ) // start over
			{
				System.out.println( "Session was dead.  Getting a new one." );
				session = request.getSession();
			}
	//		session.setMaxInactiveInterval( 30 ); // 30 seconds (used when testing) 
			session.setMaxInactiveInterval( 3600 ); // 1 hour
			String username = request.getParameter( "username" );
			String password = request.getParameter( "password" );
			UserBean user = new UserBean();
			user.setUserName( username );
			request.getSession().setAttribute( "UserBean", user );
			user.getErrors().addAll( StockManUtilities.validateLogin(  username, password ) );
	
			if ( user.getErrors().size() == 0 )
			{
				// Call a REST service to authenticate.
				// Authenticate with user name and password, 
				// return the profile if successful.  Otherwise, 
				// return [AUTHENTICATION_FAILED].
				String loginResultJson = StockManUtilities.getSecureLogin( username, password );
				user.setLoginAuthenticationProfile( StockManUtilities.loadAuthenticationProfileBean( loginResultJson ) );
				
				if ( "WAIT FOR 2 FACTOR AUTHENTICATION".equals( user.getLoginAuthenticationProfile().getClientResult() ) )
				{
					System.out.println( "Wait for 2FA" );
					
					String randomCode = null;
					// Get the randomCode that was generated from loginResultJson
					JSONParser parser = new JSONParser();
					try
					{
						JSONArray jsonArray = ( JSONArray ) parser.parse( loginResultJson );
						for ( Object obj : jsonArray ) 
						{
							JSONObject jsonObject = ( JSONObject ) obj; // Each element is a JSONObject
							randomCode = ( String ) jsonObject.get( "randomCode" );
						}
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}

					if ( "EMAIL".equals( user.getLoginAuthenticationProfile().getVerificationCodeMethod() ) )
					{
						request.removeAttribute( "userMessage" );
						String otpMessage = "Hello " + user.getLoginAuthenticationProfile().getFirstName() + "!<br><br>";
							   otpMessage += "When prompted, please enter this one-time code to complete your secure login.<br><br>";
							   otpMessage += "Your Code: "+ randomCode + "<br><br>";
							   otpMessage += "Thank you,<br>";
							   otpMessage += "StockMan Support";
						// Send email with the random code.
						try
						{
							StockManUtilities.sendEmail( "java:jboss/mail/Default", "support@watchtowerusa.net", user.getLoginAuthenticationProfile().getEmail(), "Watchtowerusa Security", otpMessage );
							request.setAttribute( "userMessage", "Please check you email to complete login.  If email does not arrive soon, check your spam folder." );
						}
						catch( Exception e )
						{
							request.setAttribute( "userMessage", "Error sending email." );
							e.printStackTrace();
						}
					}
					else
					if ( "TEXT".equals( user.getLoginAuthenticationProfile().getVerificationCodeMethod() ) )
					{
						// TEXT NOT WORKING PENDING LOCATION OF SUITABLE VENDOR.
						request.setAttribute( "userMessage", "NOTE: This user set up with 'TEXT' verification method.  Only 'EMAIL' method is working until a suitable text vendor is located." );
					}
					destinationPage = "/JSP/otpVerification.jsp";
				}
				else
				if ( "INCORRECT PASSWORD".equals( user.getLoginAuthenticationProfile().getClientResult() )
				  || "USER NOT FOUND".equals( user.getLoginAuthenticationProfile().getClientResult() )	)
				{
					user.getErrors().add( "Bad username or password" );
					destinationPage = "/JSP/login.jsp";
				}
				else
				{
					// Get user roles here.
					StockManUtilities.loadUserRoles( user );
					if ( user.getErrors().size() == 0 )
						destinationPage = "/JSP/filter.jsp";
					else
						destinationPage = "/JSP/login.jsp";
				}
			}
		}
		else
		if ( "Validate".equalsIgnoreCase( buttonPressed ) )
		{
			UserBean user = ( UserBean ) request.getSession().getAttribute( "UserBean" );
			String passcode = request.getParameter( "passcode" );
			if ( passcode != null )
				passcode = passcode.trim();
			String validationResultJson = StockManUtilities.validateCode( user.getLoginAuthenticationProfile().getAuthenticationProfileId(), passcode );
			String validationResult = null;
			JSONParser parser = new JSONParser();
			try
			{
				JSONArray jsonArray = ( JSONArray ) parser.parse( validationResultJson );
				for ( Object obj : jsonArray ) 
				{
					JSONObject jsonObject = ( JSONObject ) obj; // Each element is a JSONObject
					validationResult = ( String ) jsonObject.get( "validationResult" );
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			// Possible Results:
			// VALIDATION SUCCESSFUL or VALIDATION FAILED
			if ( validationResult != null )
			{
				if ( "VALIDATION SUCCESSFUL".equals( validationResult ) )
				{
					StockManUtilities.loadUserRoles( user );
					if ( user.getErrors().size() == 0 )
						destinationPage = "/JSP/filter.jsp";
					else
						destinationPage = "/JSP/login.jsp";
				}
				else
				{
					user.getErrors().add( "Passcode validation failed.  Please try again." );
					destinationPage = "/JSP/otpVerification.jsp";
				}
			}
			else
			{
				user.getErrors().add( "Passcode validation failed.  Please try again.  Contact administrator if problem persists." );
				destinationPage = "/JSP/otpVerification.jsp";
			}
		}
		else
		if ( "Request New Code".equalsIgnoreCase( buttonPressed ) )
		{
			UserBean user = ( UserBean ) request.getSession().getAttribute( "UserBean" );
			String verificationCodeMethod =  user.getLoginAuthenticationProfile().getVerificationCodeMethod();
			String mobilePhone =  user.getLoginAuthenticationProfile().getMobilePhone();
			String email =  user.getLoginAuthenticationProfile().getEmail();
			String newCodeResult = "NEW CODE FAILED";
			int authenticationProfileId = user.getLoginAuthenticationProfile().getAuthenticationProfileId();
			if ( verificationCodeMethod != null )
			{
				String newVarificationCodeResultJson = StockManUtilities.getNewVerificationCode( authenticationProfileId, 
						                                                                         verificationCodeMethod,
						                                                                         mobilePhone,
						                                                                         email );
				
				JSONParser parser = new JSONParser();
				try
				{
					JSONArray jsonArray = ( JSONArray ) parser.parse( newVarificationCodeResultJson );
					for ( Object obj : jsonArray ) 
					{
						JSONObject jsonObject = ( JSONObject ) obj; // Each element is a JSONObject
						newCodeResult = ( String ) jsonObject.get( "newCodeResult" );
					}
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
			else
				user.getErrors().add( "Verification code send method not defined on profile." );
			if ( ! "WAIT FOR 2 FACTOR AUTHENTICATION".equals( newCodeResult ) )
				user.getErrors().add( "New code request failed: " + newCodeResult + ".  If problem persists, contact administrator." );
			else
				request.setAttribute( "userMessage", "New code sent successfully." );
			destinationPage = "/JSP/otpVerification.jsp";
		}
		else
		if ( "forgotUserId".equalsIgnoreCase( buttonPressed ) )
		{
			System.out.println( "FORGOT USERNAME" );
			request.setAttribute( "RECOVERY_TYPE", "username" );
			destinationPage = "/JSP/recover.jsp";
		}
		else
		if ( "Send Username".equalsIgnoreCase( buttonPressed ) )
		{
			String email = request.getParameter( "email" );
			// First, look up the organization details.
			String organizationJson = StockManUtilities.getOrganization( "Stockman Customers" );
			OrganizationBean organization = StockManUtilities.loadOrganizationBean( organizationJson );
			// Then look for the authentication profile using the supplied 'email' + 'organizationId'
			String authenticationProfileJson = StockManUtilities.retrieveAuthenticationProfileForEmail( organization.getOrganizationId(), email );
			AuthenticationProfileBean profile = StockManUtilities.loadAuthenticationProfileBean( authenticationProfileJson );
			request.removeAttribute( "userMessage" );
			String usernameRecoveryMessage = "Hello " + profile.getFirstName() + "!<br><br>";
		       	   usernameRecoveryMessage += "Your Stock Man username we have on file is " + profile.getUserId() + ".<br><br>";
				   usernameRecoveryMessage += "Thank you,<br>";
				   usernameRecoveryMessage += "StockMan Support";
			if ( profile != null && "USER PROFILE FOUND".equals( profile.getClientResult() ) )
			{
				// Send email with the username.
				try
				{
					StockManUtilities.sendEmail( "java:jboss/mail/Default", "support@watchtowerusa.net", email, "recovery", usernameRecoveryMessage );
					request.setAttribute( "userMessage", "Username emailed to you.  If email does not arrive soon, check your spam folder." );
				}
				catch( Exception e )
				{
					request.setAttribute( "userMessage", "Error sending email." );
					e.printStackTrace();
				}
				
			}
			else
			{
				request.setAttribute( "userMessage", "No profile found for " + email );
			}

			request.setAttribute( "RECOVERY_TYPE", "response" );
			
			destinationPage = "/JSP/recover.jsp";
		}
		else
		if ( "Return to Login".equalsIgnoreCase( buttonPressed ) )
		{
			destinationPage = "/JSP/login.jsp";
		}
		else
		if ( "forgotPassword".equalsIgnoreCase( buttonPressed ) )
		{
			System.out.println( "FORGOT PASSWORD" );
			request.setAttribute( "RECOVERY_TYPE", "password" );
			destinationPage = "/JSP/recover.jsp";
		}
		else
		if ( "Reset Password".equalsIgnoreCase( buttonPressed ) )
		{
			String userId = request.getParameter( "userId" );
			// First, look up the organization details.
			String organizationJson = StockManUtilities.getOrganization( "Stockman Customers" );
			OrganizationBean organization = StockManUtilities.loadOrganizationBean( organizationJson );
			// Then look for the authentication profile using the supplied 'userId' + 'organizationId'
			String authenticationProfileJson = StockManUtilities.retrieveAuthenticationProfileForUserId( organization.getOrganizationId(), userId );
			AuthenticationProfileBean profile = StockManUtilities.loadAuthenticationProfileBean( authenticationProfileJson );
			if ( profile.getAuthenticationProfileId() <= 0 )
			{
				request.setAttribute( "userMessage", "No profile found for user " + userId );
				request.setAttribute( "RECOVERY_TYPE", "password" );
				destinationPage = "/JSP/login.jsp";
			}
			else
			{	
				// Set verification code to be included in the reset password link in email.
				String newVerificationCodeResultJson = StockManUtilities.getNewVerificationCode( profile.getAuthenticationProfileId(), 
	                    																		 "RECOVER",
	                    																		 profile.getMobilePhone(),
	                    																		 profile.getEmail() );
				String newCodeResult = null;
				String randomCode = null;
				JSONParser parser = new JSONParser();
				try
				{
					JSONArray jsonArray = ( JSONArray ) parser.parse( newVerificationCodeResultJson );
					for ( Object obj : jsonArray ) 
					{
						JSONObject jsonObject = ( JSONObject ) obj; // Each element is a JSONObject
						newCodeResult = ( String ) jsonObject.get( "newCodeResult" );
						randomCode = ( String ) jsonObject.get( "randomCode" );
					}
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
	
				if ( "WAIT FOR 2 FACTOR AUTHENTICATION".equals( newCodeResult ) )
				{
					request.removeAttribute( "userMessage" );
					String domainName = null;
					Context ctx = null;
					try
					{
						ctx = new InitialContext();
			        	domainName = ( String ) ctx.lookup("java:global/restServiceDomain");
					}	
					catch( Exception e )
					{
						e.printStackTrace();
					}
					
					String passworResetMessage = "Hello " + profile.getFirstName() + "!<br><br>";
						   passworResetMessage += "Please use the one-time link below to reset your password for " + profile.getUserId() + ".<br><br>";
						   passworResetMessage += "<a href=" + domainName + "/stockman/StockManServlet?LoginAction=newPassword&userId=" + userId + "&randomCode="+ randomCode + ">Reset Password</a><br><br>";
						   passworResetMessage += "Thank you,<br>";
						   passworResetMessage += "StockMan Support";
					if ( profile != null && "USER PROFILE FOUND".equals( profile.getClientResult() ) )
					{
						// Send email with the username.
						try
						{
							StockManUtilities.sendEmail( "java:jboss/mail/Default", "support@watchtowerusa.net", profile.getEmail(), "recovery", passworResetMessage );
							request.setAttribute( "userMessage", "Password reset link emailed.  If email does not arrive soon, check your spam folder." );
						}
						catch( Exception e )
						{
							request.setAttribute( "userMessage", "Error sending email." );
							e.printStackTrace();
						}
						
					}
					else
					{
						request.setAttribute( "userMessage", "No profile found for " + userId );
					}
				}
	
				request.setAttribute( "RECOVERY_TYPE", "response" );
				destinationPage = "/JSP/recover.jsp";
			}
		}
		else
		if ( "newPassword".equalsIgnoreCase( buttonPressed ) )
		{
			System.out.println( "TYPE NEW PASSWORD" );
			String userId = request.getParameter( "userId" );
			String randomCode = request.getParameter( "randomCode" );
			request.setAttribute( "RECOVERY_TYPE", "newPassword" );
			destinationPage = "/JSP/recover.jsp";
		}
		else
		if ( "Submit New Password".equalsIgnoreCase( buttonPressed ) )
		{
			System.out.println( "NEW PASSWORD SUBMIT" );
			request.setAttribute( "RECOVERY_TYPE", "response" );
			String textPassword = request.getParameter( "textPassword" );
			String userId = request.getParameter( "userId" );
			String randomCode = request.getParameter( "randomCode" );
			ArrayList<String> errors = new ArrayList<String>();
			StockManUtilities.validatePassword( textPassword, errors );
			if ( errors.size() == 0 )
			{
				String organizationJson = StockManUtilities.getOrganization( "Stockman Customers" );
				OrganizationBean organization = StockManUtilities.loadOrganizationBean( organizationJson );
				// Then look for the authentication profile using the supplied 'userId' + 'organizationId'
				String authenticationProfileJson = StockManUtilities.retrieveAuthenticationProfileForUserId( organization.getOrganizationId(), userId );
				AuthenticationProfileBean profile = StockManUtilities.loadAuthenticationProfileBean( authenticationProfileJson );
				// Now validate the random code.
				String resultJson = StockManUtilities.validateCode( profile.getAuthenticationProfileId(), randomCode );
				// Check the result of the code validation.
				String codeValidationResult = null;
				JSONParser parser = new JSONParser();
				try
				{
					JSONArray jsonArray = ( JSONArray ) parser.parse( resultJson );
					for ( Object obj : jsonArray ) 
					{
						JSONObject jsonObject = ( JSONObject ) obj; // Each element is a JSONObject
						codeValidationResult = ( String ) jsonObject.get( "validationResult" );
					}
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				if ( "VALIDATION SUCCESSFUL".equals( codeValidationResult ) )
				{
					// set the new password here.
					StockManUtilities.setPassword( profile.getAuthenticationProfileId(), textPassword );
					request.setAttribute( "userMessage", "Password has been reset." );
				}
				else
				{
					errors.add( "Random Code Validation Failed: " +  codeValidationResult );
					request.setAttribute( "userMessage", "This link has been used already or has expired.  Password reset failed." );

				}
			}
			else
			{
				request.setAttribute( "userMessage", errors.get( 0 ) );
			}
			
			destinationPage = "/JSP/recover.jsp";
		}

		this.view( request, response, destinationPage );
	}
}
