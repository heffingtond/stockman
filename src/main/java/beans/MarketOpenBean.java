package beans;

public class MarketOpenBean
{
	private String exchange;
	private String name;
	private boolean isMarketOpen;
	
	public String getExchange()
	{
		return exchange;
	}
	
	public void setExchange(String exchange)
	{
		this.exchange = exchange;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isMarketOpen()
	{
		return isMarketOpen;
	}
	
	public void setMarketOpen(boolean isMarketOpen)
	{
		this.isMarketOpen = isMarketOpen;
	}
}
