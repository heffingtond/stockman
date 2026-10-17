package actions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
					destinationPage = "/JSP/otpVerification.jsp";
				}
				else
				if ( "INCORRECT PASSWORD".equals( user.getLoginAuthenticationProfile().getClientResult() )
				  || "USER NOT FOUND".equals( user.getLoginAuthenticationProfile().getClientResult() )	)
				{
					user.getErrors().add( "Bad username or password" );
					System.out.println( "Login error: " + user.getLoginAuthenticationProfile().getClientResult() );
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

		this.view( request, response, destinationPage );
	}
}
