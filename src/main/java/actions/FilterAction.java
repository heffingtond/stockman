package actions;

import java.util.ArrayList;
import java.util.Comparator;

import beans.UserBean;
import core.StockManUtilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import beans.EarningsBean;
import beans.FilterDisplayBean;

public class FilterAction extends Action
{
	public void execute( HttpServletRequest request,
            			 HttpServletResponse response )
	{
		String destinationPage = "/JSP/filter.jsp";
		UserBean user = ( UserBean ) request.getSession().getAttribute( "UserBean" );

		String buttonPressed =  request.getParameter( "FilterAction" );
		if ( "runFilter".equalsIgnoreCase( buttonPressed ) )
		{
			String tickers = request.getParameter( "tickers" );
			if ( tickers.length() > 100 )
				user.getErrors().add( "Please limit the ticker string to 100 characters.  The ticker string entered is " + tickers.length() + " characters." );
			else
			{
				user.setTickers( tickers );
	            ArrayList<FilterDisplayBean> returnList = StockManUtilities.displayPriceToTarget( tickers );
	            System.out.println("New 2 return list size is " + returnList.size());
	            // Clear display list.
	            user.getAllFilterDisplay().clear();
	            ArrayList<String> droppedFromDisplay = new ArrayList<String>();
	            request.setAttribute( "DROPPED_FROM_DISPLAY", droppedFromDisplay );
	            for ( FilterDisplayBean display : returnList )
	            {
	            	boolean displayOnReport = true;
	    			String estimatedIncreaseEpsStr = request.getParameter( "estimatedIncreaseEps" );
	                System.out.println( "estimatedIncreaseEpsStr is " + estimatedIncreaseEpsStr );
	    			if ( "true".equalsIgnoreCase( estimatedIncreaseEpsStr ) )
	    				user.setEstimatedIncreaseEpsSelected( true );
	    			else
	    				user.setEstimatedIncreaseEpsSelected( false );
	            	// If the 'Estimated Increase in EPS' check box is checked, disregard any tickers that do not project an increase.
	            	if ( user.isEstimatedIncreaseEpsSelected() )
	            	{
	            		// Spin through the entire group and collect the latest entries for actual and estimated EPS.
	                    String lastEpsAct = null;
	                    String lastEpsEst = null;
	                    ArrayList<EarningsBean> tempAllEarnings = new ArrayList<EarningsBean>();
	                    tempAllEarnings.addAll( display.getAllEarnings() );
	                    
	                    // Sort by the 'date' String property (Descending)
	                    tempAllEarnings.sort( Comparator.comparing( EarningsBean::getDate ) );
	                    
	                    for ( EarningsBean tempEarningsBean : tempAllEarnings )
	                    {
	                    	if ( ! StockManUtilities.isEmpty( tempEarningsBean.getEpsActual() ) )
	                    		lastEpsAct = tempEarningsBean.getEpsActual();
	                    	if ( ! StockManUtilities.isEmpty( tempEarningsBean.getEpsEstimated() ) )
	                    		lastEpsEst = tempEarningsBean.getEpsEstimated();
	                    }
	                    // Now compare the last estimated EPS to the last actual EPS and see if there is a projected increase.
	                    if ( ! StockManUtilities.isEmpty( lastEpsAct ) && ! StockManUtilities.isEmpty( lastEpsEst ) )
	                    {
	                    	double lastActual = Double.parseDouble( lastEpsAct );
	                    	double lastEstimated = Double.parseDouble( lastEpsEst );
	                    	if ( lastEstimated < lastActual )
	                    	{
	                        	System.out.println( "EPS decrease anticipated.  Drop from display. " + display.getSymbol() );
	                        	droppedFromDisplay.add( "EPS decrease anticipated.  Drop from display. " + display.getSymbol() );
	                    		displayOnReport = false;
	                    	}
	                    }
	            	}
	            	
	            	String upside = StockManUtilities.getUpside( display.getPrice(), display.getTargetConsensus() );
	            	display.setUpside( upside );
	
	    			String upside20PercentStr = request.getParameter( "upside20Percent" );
	                System.out.println( "upside20PercentStr is " + upside20PercentStr );
	    			if ( "true".equalsIgnoreCase( upside20PercentStr ) )
	    				user.setUpside20Percent( true );
	    			else
	    				user.setUpside20Percent( false );
	
	                if ( user.isUpside20Percent() )
	                	if ( displayOnReport )
	                	{
	                		Double upsideDouble = 0.0;
	                		// Then check to see if the projected upside is 20 percent or more (only if the 20% upside check box is checked)
	                		try
	                		{
	                			upsideDouble = Double.parseDouble( upside );
	                		}
	                		catch( Exception ex )
	                		{
	                			ex.printStackTrace();
	                			System.out.println( "Could not parse upside to double: " + upsideDouble );
	                		}
	                		if ( upsideDouble < 20 )
	                		{
	                        	System.out.println( "Projected upside less than 20%.  Drop from display. " + display.getSymbol() );
	                        	droppedFromDisplay.add( "Projected upside less than 20%.  Drop from display. " + display.getSymbol() );
	                			displayOnReport = false;
	                		}
	                	}
	                
	    			String ratingBMinusOrBetterStr = request.getParameter( "ratingBMinusOrBetter" );
	                System.out.println( "ratingBMinusOrBetterStr is " + ratingBMinusOrBetterStr );
	    			if ( "true".equalsIgnoreCase( ratingBMinusOrBetterStr ) )
	    				user.setRatingBMinusOrBetter( true );
	    			else
	    				user.setRatingBMinusOrBetter( false );
	
	    			if ( user.isRatingBMinusOrBetter() )
	                	if ( displayOnReport )
	                	{
	                		final ArrayList<String> ACCEPTABLE_RANGE = new ArrayList<String>();
	                		ACCEPTABLE_RANGE.add( "B-" );
	                		ACCEPTABLE_RANGE.add( "B" );
	                		ACCEPTABLE_RANGE.add( "B+" );
	                		ACCEPTABLE_RANGE.add( "A-" );
	                		ACCEPTABLE_RANGE.add( "A" );
	                		ACCEPTABLE_RANGE.add( "A+" );
	                		ACCEPTABLE_RANGE.add( "S-" );
	                		ACCEPTABLE_RANGE.add( "S" );
	                		ACCEPTABLE_RANGE.add( "S+" );
	                		String overallRating = display.getRating();
	                		if ( overallRating != null )
	                			overallRating = overallRating.trim();
	                		
	                		if ( ! ACCEPTABLE_RANGE.contains( overallRating ) )
	                		{
	                        	System.out.println( "Rating lower than B-.  Drop from display. " + display.getSymbol() );
	                        	droppedFromDisplay.add( "Rating lower than B-.  Drop from display. " + display.getSymbol() );
	                			displayOnReport = false;
	                		}
	                	}
	
	                if ( displayOnReport )
	                	user.getAllFilterDisplay().add( display );
	            }
	            System.out.println("user.getAllFilterDisplay() size is " + user.getAllFilterDisplay().size());
	            if ( returnList.size() == 0 )
	            	request.setAttribute( "userMessage", "None Found" );
	            else
	            	request.setAttribute( "userMessage", "Done" );
			}
        }

		this.view( request, response, destinationPage );
	}
}
