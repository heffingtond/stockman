package beans;

public class QuoteAfterHoursBean
{
	private String symbol;
	private String price;
	private String tradeSize;
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
	
	public String getTradeSize()
	{
		return tradeSize;
	}

	public void setTradeSize(String tradeSize)
	{
		this.tradeSize = tradeSize;
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
