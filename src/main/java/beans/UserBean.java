package beans;

import java.util.ArrayList;

public class UserBean
{
	private String userName;
	private ArrayList<String> errors = new ArrayList<String>();
	private ArrayList<FilterDisplayBean> allFilterDisplay = new ArrayList<FilterDisplayBean>();
	private AuthenticationProfileBean loginAuthenticationProfile = new AuthenticationProfileBean();
	private String tickers = "";
	private String function;
	private boolean estimatedIncreaseEpsSelected;
	private boolean upside20Percent;
	private boolean ratingBMinusOrBetter;
		
	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		if ( userName != null )
			userName = userName.trim();
		this.userName = userName;
	}

	public ArrayList<String> getErrors()
	{
		return errors;
	}

	public ArrayList<String> getClearErrors()
	{
		errors.clear();
		return null;
	}

	public void setErrors(ArrayList<String> errors)
	{
		this.errors = errors;
	}

	public ArrayList<FilterDisplayBean> getAllFilterDisplay()
	{
		return allFilterDisplay;
	}

	public void setAllFilterDisplay(ArrayList<FilterDisplayBean> allFilterDisplay)
	{
		this.allFilterDisplay = allFilterDisplay;
	}

	public AuthenticationProfileBean getLoginAuthenticationProfile()
	{
		return loginAuthenticationProfile;
	}

	public void setLoginAuthenticationProfile(AuthenticationProfileBean loginAuthenticationProfile)
	{
		this.loginAuthenticationProfile = loginAuthenticationProfile;
	}

	public String getTickers()
	{
		return tickers;
	}

	public void setTickers(String tickers)
	{
		this.tickers = tickers;
	}

	public String getFunction()
	{
		return function;
	}

	public void setFunction(String function)
	{
		this.function = function;
	}

	public boolean isEstimatedIncreaseEpsSelected()
	{
		return estimatedIncreaseEpsSelected;
	}

	public void setEstimatedIncreaseEpsSelected(boolean estimatedIncreaseEpsSelected)
	{
		this.estimatedIncreaseEpsSelected = estimatedIncreaseEpsSelected;
	}

	public boolean isUpside20Percent()
	{
		return upside20Percent;
	}

	public void setUpside20Percent(boolean upside20Percent)
	{
		this.upside20Percent = upside20Percent;
	}

	public boolean isRatingBMinusOrBetter()
	{
		return ratingBMinusOrBetter;
	}

	public void setRatingBMinusOrBetter(boolean ratingBMinusOrBetter)
	{
		this.ratingBMinusOrBetter = ratingBMinusOrBetter;
	}

	
}
