package beans;

public class QuoteShortBean
{
	private String symbol;
	private String price;
	private String change;
	private String volume;
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
	
	public String getChange()
	{
		return change;
	}
	
	public void setChange(String change)
	{
		this.change = change;
	}
	
	public String getVolume()
	{
		return volume;
	}
	
	public void setVolume(String volume)
	{
		this.volume = volume;
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
