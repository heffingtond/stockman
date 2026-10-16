package beans;

import java.util.ArrayList;

public class DisplayDataBean
{
	private String symbol;
	private String name;
	private String price;
	private String changePercentage;
	private String change;
	private String dayLow;
	private String dayHigh;
	private String open;
	private String volume;
	private String previousClose;
	private String altmanZScore;
	private String piotroskiScore;
	private String rating;
	private ArrayList<EarningsBean> allEarnings = new ArrayList<EarningsBean>();
	private String timestamp;


	public String getSymbol()
	{
		return symbol;
	}
	
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPrice()
	{
		return price;
	}
	
	public void setPrice(String price)
	{
		this.price = price;
	}
	
	public String getChangePercentage()
	{
		return changePercentage;
	}
	
	public void setChangePercentage(String changePercentage)
	{
		this.changePercentage = changePercentage;
	}
	
	public String getChange()
	{
		return change;
	}

	public void setChange(String change)
	{
		this.change = change;
	}

	public String getDayLow()
	{
		return dayLow;
	}

	public void setDayLow(String dayLow)
	{
		this.dayLow = dayLow;
	}

	public String getDayHigh()
	{
		return dayHigh;
	}

	public void setDayHigh(String dayHigh)
	{
		this.dayHigh = dayHigh;
	}

	public String getOpen()
	{
		return open;
	}

	public void setOpen(String open)
	{
		this.open = open;
	}

	public String getVolume()
	{
		return volume;
	}

	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	public String getPreviousClose()
	{
		return previousClose;
	}
	
	public void setPreviousClose(String previousClose)
	{
		this.previousClose = previousClose;
	}
	
	public String getAltmanZScore()
	{
		return altmanZScore;
	}

	public void setAltmanZScore(String altmanZScore)
	{
		this.altmanZScore = altmanZScore;
	}

	public String getPiotroskiScore()
	{
		return piotroskiScore;
	}

	public void setPiotroskiScore(String piotroskiScore)
	{
		this.piotroskiScore = piotroskiScore;
	}

	public String getRating()
	{
		return rating;
	}

	public void setRating(String rating)
	{
		this.rating = rating;
	}
	
	public ArrayList<EarningsBean> getAllEarnings()
	{
		return allEarnings;
	}

	public void setAllEarnings(ArrayList<EarningsBean> allEarnings)
	{
		this.allEarnings = allEarnings;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
}
