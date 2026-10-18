package core;

import java.io.IOException;

import actions.Action;
import actions.FilterAction;
import actions.LoginAction;
import actions.LogoutAction;
import actions.ProfileAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StockManServlet
 */
@WebServlet("/StockManServlet")
public class StockManServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean timeout = false;
		try
		{
			timeout = StockManUtilities.isTimeout( request, response );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		if ( ! timeout )
		{
			Action action = null;
			if ( StockManUtilities.replaceSpecialCharacters( request.getParameter( "LoginAction" ) ) != null )
				action = new LoginAction();
			else
			if ( StockManUtilities.replaceSpecialCharacters( request.getParameter( "LogoutAction" ) ) != null )
				action = new LogoutAction();
			else
			if ( StockManUtilities.replaceSpecialCharacters( request.getParameter( "FilterAction" ) ) != null )
				action = new FilterAction();
			else
			if ( StockManUtilities.replaceSpecialCharacters( request.getParameter( "ProfileAction" ) ) != null )
				action = new ProfileAction();
			
			if ( action != null )
				action.execute( request, response );
			else
				System.out.println( "No action found in servlet!" );
		}
	}
}
