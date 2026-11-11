package actions;

import java.security.SecureRandom;
import java.util.ArrayList;

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

public class CustomerProfileAction extends Action
{
	
	public void execute( HttpServletRequest request,
            			 HttpServletResponse response )
	{
		String destinationPage = "/JSP/customerProfile.jsp";
		String buttonPressed =  request.getParameter( "CustomerProfileAction" );
		boolean isFraudulentRequest = false;
		if ( "initialHit".equalsIgnoreCase( buttonPressed ) )
		{
			isFraudulentRequest = false;
		      // 1. Check Honeypot Field
	        String honeypot = request.getParameter("website_confirm");
	        if ( honeypot != null && ! honeypot.trim().isEmpty() ) 
	        {
	            // It's a bot! Silently drop or reject the request
	            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
	            System.out.println( "CustomerProfileAction.initialHit - Suspicious activity detected" );
	            isFraudulentRequest = true;
	        }

	        // 2. Check User-Agent header (basic bot filter)
	        String userAgent = request.getHeader("User-Agent");
	        if (userAgent == null || userAgent.isEmpty() || StockManUtilities.isKnownBadBot( userAgent ) ) 
	        {
	            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            System.out.println( "CustomerProfileAction.initialHit - robot detected" );
	            isFraudulentRequest = true;
	        }
			
	        if ( ! isFraudulentRequest )
	        {
				AuthenticationProfileBean newCustomerProfile = new AuthenticationProfileBean();
				request.getSession().setAttribute( "NEW_CUSTOMER_PROFILE", newCustomerProfile );
				SecureRandom secureRand = new SecureRandom();
				int secureInt = secureRand.nextInt(1000000);
		        System.out.println("************* new key " + secureInt );
				request.getSession().setAttribute( "NEW_CUSTOMER_KEY", secureInt );
				ArrayList<String> errors = new ArrayList<String>();
				request.getSession().setAttribute( "NEW_CUSTOMER_ERRORS", errors );
				HttpSession session = request.getSession( false );
				if ( session == null ) // start over
				{
					System.out.println( "Session was dead.  Getting a new one." );
					session = request.getSession();
				}
		//		session.setMaxInactiveInterval( 30 ); // 30 seconds (used when testing) 
				session.setMaxInactiveInterval( 3600 ); // 1 hour
	        }
	        else // Fraudulent Request
	        {
	    	    HttpSession session = request.getSession( false );
	    	    // Invalidate the current session
	    	    if ( session != null ) 
	    	    {
	    			System.out.println( "LogoutAction invalidate the session" );
	    	        session.invalidate();
	    	    }
	        }
		}
		else
		if ( "addCustomerProfile".equalsIgnoreCase( buttonPressed ) )
		{
			isFraudulentRequest = false;
			if ( ! this.isTimeout( request, response ) )
			{
				int requestKey = 0;
				String requestKeyStr = request.getParameter( "newCustomerKey" );
				if ( ! StockManUtilities.isEmpty( requestKeyStr ) )
					requestKey = Integer.parseInt( requestKeyStr );
				int sessionKey = ( int ) request.getSession().getAttribute( "NEW_CUSTOMER_KEY" );
				System.out.println("requestKey is " + requestKey);
				System.out.println("sessionKey is " + sessionKey);
				if ( requestKey != sessionKey )
				{
		            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		            System.out.println( "CustomerProfileAction.addCustomerProfile - request key does not match session key.  Cross site attack." );
					isFraudulentRequest = true;
				}
				
			      // 1. Check Honeypot Field
		        String honeypot = request.getParameter("website_confirm");
		        if ( honeypot != null && ! honeypot.trim().isEmpty() ) 
		        {
		            // It's a bot! Silently drop or reject the request
		            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		            System.out.println( "CustomerProfileAction.addCustomerProfile - Suspicious activity detected" );
		            isFraudulentRequest = true;
		        }

		        // 2. Check User-Agent header (basic bot filter)
		        String userAgent = request.getHeader("User-Agent");
		        if (userAgent == null || userAgent.isEmpty() || StockManUtilities.isKnownBadBot(userAgent) ) 
		        {
		            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		            System.out.println( "CustomerProfileAction.addCustomerProfile - robot detected" );
		            isFraudulentRequest = true;
		        }
				
		        if ( ! isFraudulentRequest )
		        {
					AuthenticationProfileBean newCustomerProfile = ( AuthenticationProfileBean ) request.getSession().getAttribute( "NEW_CUSTOMER_PROFILE" );
					ArrayList<String> errors = ( ArrayList<String> ) request.getSession().getAttribute( "NEW_CUSTOMER_ERRORS" );
					errors.clear();
					
					// Look up and set the organization ID here.  We need to point to organization_name = "Stockman Customers"
					String organizationJson = StockManUtilities.getOrganization( "Stockman Customers" );			
					OrganizationBean organization = StockManUtilities.loadOrganizationBean( organizationJson );
					newCustomerProfile.setOrganizationId( organization.getOrganizationId() );
					
					newCustomerProfile.setUserId( request.getParameter( "userId" ) );
					newCustomerProfile.setTextPassword( request.getParameter( "textPassword" ) );
					newCustomerProfile.setFirstName( request.getParameter( "firstName" ) );
					newCustomerProfile.setLastName( request.getParameter( "lastName" ) );
					newCustomerProfile.setEmail( request.getParameter( "email" ) );
					newCustomerProfile.setMobilePhone( request.getParameter( "mobilePhone" ) );
					newCustomerProfile.setOfficePhone( request.getParameter( "officePhone" ) );
					newCustomerProfile.setOfficePhoneExt( request.getParameter( "officePhoneExt" ) );
					newCustomerProfile.setHomePhone( request.getParameter( "homePhone" ) );
					String failedLoginAttemptsStr = request.getParameter( "failedLoginAttempts" );
					if ( StockManUtilities.isEmpty( failedLoginAttemptsStr ) )
						failedLoginAttemptsStr = "0";
					int failedLoginAttempts = Integer.parseInt( failedLoginAttemptsStr );
					newCustomerProfile.setFailedLoginAttempts( failedLoginAttempts );
					newCustomerProfile.setVerificationCodeMethod( request.getParameter( "verificationCodeMethod" ) );
		
					long newAuthenticationProfileIdLong = 0;
		
					StockManUtilities.validateAuthenticationProfile( newCustomerProfile, errors );
			
					if ( errors.size() == 0 )
					{
						// Call a REST service to add a new customer profile.
						// Additional error checks that require database connections 
						// will be performed on the service, 
						// return SUCCESS if profile added successfully. 
						// return ERROR: [error message] otherwise.
						String newCustomerResponseJson = StockManUtilities.addNewCustomer( newCustomerProfile );
						String newCustomerResponse = null;
						JSONParser parser = new JSONParser();
						try
						{
							JSONArray jsonArray = ( JSONArray ) parser.parse( newCustomerResponseJson );
							for ( Object obj : jsonArray ) 
							{
								JSONObject jsonObject = ( JSONObject ) obj; // Each element is a JSONObject
								newCustomerResponse = ( String ) jsonObject.get( "result" );
								newAuthenticationProfileIdLong = ( long ) jsonObject.get( "authenticationProfileId" );
								newCustomerProfile.setClientResult( newCustomerResponse );
							}
						}
						catch( Exception e )
						{
							e.printStackTrace();
						}
						
						if ( "SUCCESS".equals( newCustomerProfile.getClientResult() ) )
						{
							System.out.println( "Customer " + newCustomerProfile.getUserId() + " Authentication Profile Added Successfully." );
						}
						else
						{
							errors.add( newCustomerProfile.getClientResult() );
							System.out.println( "error adding new profile: " + newCustomerProfile.getClientResult() );
							destinationPage = "/JSP/customerProfile.jsp";
						}
					}
					//  If the new profile added successfully, proceed with adding the role.
					if ( "SUCCESS".equals( newCustomerProfile.getClientResult() ) )
					{
						int newAuthenticationProfileId = ( int ) newAuthenticationProfileIdLong;
						String newCustomerRoleResponseJson = StockManUtilities.addNewCustomerRole( newCustomerProfile.getOrganizationId(), newAuthenticationProfileId );
						String newCustomerRoleResponse = null;
						JSONParser parser = new JSONParser();
						try
						{
							JSONArray jsonArray = ( JSONArray ) parser.parse( newCustomerRoleResponseJson );
							for ( Object obj : jsonArray ) 
							{
								JSONObject jsonObject = ( JSONObject ) obj; // Each element is a JSONObject
								newCustomerRoleResponse = ( String ) jsonObject.get( "result" );
							}
						}
						catch( Exception e )
						{
							e.printStackTrace();
						}
						if ( "SUCCESS".equals( newCustomerRoleResponse ) )
						{
							System.out.println( "Stock Man Role for Customer " + newCustomerProfile.getUserId() + " Added Successfully." );
							request.setAttribute( "userMessage", "Profile for " + newCustomerProfile.getUserId() + " added successfully.  You may use your established credentials to log into the StockMan application." );
							destinationPage = "/JSP/login.jsp";
						}
						else
						{
							errors.add( newCustomerProfile.getClientResult() );
							System.out.println( "error adding new profile: " + newCustomerProfile.getClientResult() );
							destinationPage = "/JSP/customerProfile.jsp";
						}
		
					}
					else
						destinationPage = "/JSP/customerProfile.jsp";
				}
		        else  // Request is fraudulent
 		        {
		        	System.out.println( "CustomerProfileAction: kill session." );
		    	    HttpSession session = request.getSession( false );
		    	    // Invalidate the current session
		    	    if ( session != null ) 
		    	    {
		    			System.out.println( "LogoutAction invalidate the session" );
		    	        session.invalidate();
		    	    }
		        }
			}
		}

		this.view( request, response, destinationPage );
	}
}
