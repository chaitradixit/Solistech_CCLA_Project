package com.ecs.ucm.ccla;

import intradoc.data.DataException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.ecs.utils.Log;

/** Helper methods for dealing with BigDecimals etc.
 * 
 * @author Tom
 *
 */
public class NumberUtils {
	
	/** Yep, one hundred */
	public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
	
	/**
	 * Converts a given percentage value (between 0-100 inclusively) to an amount, using
	 * the passed totalAmount as a multiplier.
	 * 
	 * The result is rounded to 2 decimal places before being returned, using half-even
	 * rounding.
	 * 
	 * @param percentage
	 * @param totalAmount
	 * @return
	 */
	public static BigDecimal convertPercentageToCashAmount
	 (BigDecimal percentage, BigDecimal totalAmount) {
		
		// Set to scale 4 here. This assumes that the totalAmount argument will be 
		// accurate to 2 decimal places (scale 2). Adding the extra 2 decimals of
		// precision ensures that the division/100 below will not remove accuracy.
		BigDecimal totalAmountScaled = totalAmount.setScale(4);
		
		// Use half-even rounding. This should help to smooth out rounding errors
		// when dealing with cumulative percentage amounts.
		BigDecimal amount = totalAmountScaled.divide
		 (ONE_HUNDRED, RoundingMode.HALF_EVEN).multiply(percentage);

		Log.debug("Calculated amount as " + amount.toPlainString() + 
		 " from percentage of " + percentage.toPlainString()+", totalAmount:"
		 +totalAmount.toPlainString());
		
		BigDecimal amountPostRounding = amount.setScale(2, RoundingMode.HALF_EVEN);
		Log.debug("Post-rounding: " + amountPostRounding.toPlainString());

		return amountPostRounding;
	}

	/**
	 * Converts a given cash amount value to a percentage between 0 and 100, using the 
	 * passed totalAmount as a multiplier.
	 * 
	 * The result is rounded to the number of decimal places specified by the precison
	 * argument before being returned, using half-up rounding
	 * 
	 * @param amount
	 * @param totalAmount
	 * @return
	 */
	public static BigDecimal convertCashAmountToPercentage
	 (BigDecimal amount, BigDecimal totalAmount, int precision) {
		// Add 2 to the passed precision value here, to ensure we don't lose precision
		// after multiplying by 100.
		MathContext pcContext = 
		 new MathContext(precision+2, RoundingMode.HALF_UP);
	
		BigDecimal percentage = amount
		 .multiply(ONE_HUNDRED)
		 .divide(totalAmount, pcContext);
		
		Log.debug("Calculated percentage as " + percentage.toPlainString() + 
		 " from amount of " + amount.toPlainString()+
		 ", precision: " + precision + ", totalAmount: "+totalAmount.toPlainString());
		
		BigDecimal percentagePostRounding = 
		 percentage.setScale(precision, RoundingMode.HALF_UP);
		Log.debug("Post-rounding: " + percentagePostRounding.toPlainString());
		
		return percentagePostRounding;
	}
	
	/** Computes the passed percent/amount value from the other passed value in the
	 *  pair.
	 * 
	 *  If either percent/amount is equal to zero, it is replaced with null.
	 * 
	 *  If allocPercent and allocAmount are both non-null, an error is thrown due to
	 *  the calculation being indeterminate.
	 *  
	 *  If both are null, they are left unchanged.
	 *  
	 *  Otherwise, the percentage is calculated from the passed amount and vice-versa.
	 * 
	 * @param ttlaAllocPercent
	 * @param ttlaAllocAmount
	 * @param totalAmount
	 * @param percentagePrecision 	number of decimal places that percentage values
	 * 								are calculated to
	 * @throws DataException 
	 */
	public static AmountPercentPair computePercentageOrAmount
	 (AmountPercentPair amountPercent, BigDecimal totalAmount, 
	 int percentagePrecision) throws DataException {
	
		BigDecimal allocAmount = amountPercent.getAmount();
		BigDecimal allocPercent = amountPercent.getPercent();
		
		if (totalAmount == null) {
			throw new DataException("Unable to calculate allocation " +
			 "percent/amount: total amount missing");
		}
		
		// Treat a zero-valued Allocation percentage to be the same as null
		if (allocPercent != null && allocPercent.equals(BigDecimal.ZERO))
			allocPercent = null;
		
		// Treat a zero-valued Allocation amount to be the same as null
		if (allocAmount != null && allocAmount.equals(BigDecimal.ZERO))
			allocAmount = null;
		
		if (allocPercent != null && allocAmount != null) {
			// Both percentage and amount are specified - non-determinate calculation!
			String msg = "Unable to calculate allocation percent/amount: both " +
			 "values are specified!";
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		if (allocPercent != null || allocAmount != null) {
			if (allocPercent != null) {
				// Percentage present - calculate the amount
				allocAmount = NumberUtils.convertPercentageToCashAmount
				 (allocPercent, totalAmount);
			} else {
				// Amount present - calculate the percentage
				allocPercent = NumberUtils.convertCashAmountToPercentage
				 (allocAmount, totalAmount, percentagePrecision);
			}
		}
		
		// Return new pair instance to avoid referencing issues
		return new AmountPercentPair(allocAmount, allocPercent);
	}
	
	/** Used for passing around amount/percent pairs.
	 *  
	 * @author Tom
	 *
	 */
	public static class AmountPercentPair {
		private BigDecimal amount;
		private BigDecimal percent;
		
		public AmountPercentPair(BigDecimal amount, BigDecimal percent) {
			this.amount = amount;
			this.percent = percent;
		}
		
		public BigDecimal getAmount() {
			return amount;
		}
		public BigDecimal getPercent() {
			return percent;
		}
	}
	
	public static void main (String[] args) {
		BigDecimal three = new BigDecimal("3");
		BigDecimal six = new BigDecimal("6");
		
		System.out.println(
			convertCashAmountToPercentage(three, three.add(six), 12)
		);
	}
}
