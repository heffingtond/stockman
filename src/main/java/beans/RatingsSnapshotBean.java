package beans;

public class RatingsSnapshotBean
{
	private String symbol;
	private String rating;
	private String overallScore;
	private String discountedCashFlowScore;
	private String returnOnEquityScore;
	private String returnOnAssetsScore;
	private String debtToEquityScore;
	private String priceToEarningsScore;
	private String priceToBookScore;
	private String timestamp;
	
	public String getSymbol()
	{
		return symbol;
	}
	
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getRating()
	{
		return rating;
	}

	public void setRating(String rating)
	{
		this.rating = rating;
	}

	public String getOverallScore()
	{
		return overallScore;
	}

	public void setOverallScore(String overallScore)
	{
		this.overallScore = overallScore;
	}

	public String getDiscountedCashFlowScore()
	{
		return discountedCashFlowScore;
	}

	public void setDiscountedCashFlowScore(String discountedCashFlowScore)
	{
		this.discountedCashFlowScore = discountedCashFlowScore;
	}

	public String getReturnOnEquityScore()
	{
		return returnOnEquityScore;
	}

	public void setReturnOnEquityScore(String returnOnEquityScore)
	{
		this.returnOnEquityScore = returnOnEquityScore;
	}

	public String getReturnOnAssetsScore()
	{
		return returnOnAssetsScore;
	}

	public void setReturnOnAssetsScore(String returnOnAssetsScore)
	{
		this.returnOnAssetsScore = returnOnAssetsScore;
	}

	public String getDebtToEquityScore()
	{
		return debtToEquityScore;
	}

	public void setDebtToEquityScore(String debtToEquityScore)
	{
		this.debtToEquityScore = debtToEquityScore;
	}

	public String getPriceToEarningsScore()
	{
		return priceToEarningsScore;
	}

	public void setPriceToEarningsScore(String priceToEarningsScore)
	{
		this.priceToEarningsScore = priceToEarningsScore;
	}

	public String getPriceToBookScore()
	{
		return priceToBookScore;
	}

	public void setPriceToBookScore(String priceToBookScore)
	{
		this.priceToBookScore = priceToBookScore;
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
