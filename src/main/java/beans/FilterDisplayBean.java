package beans;

import java.util.ArrayList;

public class FilterDisplayBean
{
	private String symbol;
	private String price;
	private String volume;
	private String targetConsensus;
	private String upside;
	private String rating;
	private String altmanZScore;
	private String piotroskiScore;
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
	
	public String getPrice()
	{
		return price;
	}
	
	public void setPrice(String price)
	{
		this.price = price;
	}
	
	public String getVolume()
	{
		return volume;
	}

	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	public String getTargetConsensus()
	{
		return targetConsensus;
	}
	
	public void setTargetConsensus(String targetConsensus)
	{
		this.targetConsensus = targetConsensus;
	}
	
	public String getUpside()
	{
		return upside;
	}

	public void setUpside(String upside)
	{
		this.upside = upside;
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
