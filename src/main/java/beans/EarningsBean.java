package beans;

public class EarningsBean
{
	private String symbol;
	private String date;
	private String epsActual;
	private String epsEstimated;
	private String benchmark;
	private String revenueActual;
	private String revenueEstimated;
	private String lastUpdated;
	private String timestamp;
	
	public String getSymbol()
	{
		return symbol;
	}
	
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getEpsActual()
	{
		return epsActual;
	}

	public void setEpsActual(String epsActual)
	{
		this.epsActual = epsActual;
	}

	public String getEpsEstimated()
	{
		return epsEstimated;
	}

	public void setEpsEstimated(String epsEstimated)
	{
		this.epsEstimated = epsEstimated;
	}

	public String getBenchmark()
	{
		return benchmark;
	}

	public void setBenchmark(String benchmark)
	{
		this.benchmark = benchmark;
	}

	public String getRevenueActual()
	{
		return revenueActual;
	}

	public void setRevenueActual(String revenueActual)
	{
		this.revenueActual = revenueActual;
	}

	public String getRevenueEstimated()
	{
		return revenueEstimated;
	}

	public void setRevenueEstimated(String revenueEstimated)
	{
		this.revenueEstimated = revenueEstimated;
	}

	public String getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated)
	{
		this.lastUpdated = lastUpdated;
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
