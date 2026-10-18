package actions;

import beans.ProfileBean;
import core.StockManUtilities;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProfileAction extends Action
{
	public void execute( HttpServletRequest request,
            			 HttpServletResponse response )
	{
		String destinationPage = "/JSP/profile.jsp";
		String symbol =  request.getParameter( "ProfileAction" );
		// Load the JSON profile data into the data bean.
		ProfileBean profile =  StockManUtilities.loadProfile( symbol );
		
		request.setAttribute( "PROFILE", profile );

	    RequestDispatcher dispatcher = request.getRequestDispatcher( destinationPage );
	    try
	    {
	    	dispatcher.forward( request, response );
	    }
	    catch( Exception e )
	    {
	    	e.printStackTrace();
	    }
	}
}
