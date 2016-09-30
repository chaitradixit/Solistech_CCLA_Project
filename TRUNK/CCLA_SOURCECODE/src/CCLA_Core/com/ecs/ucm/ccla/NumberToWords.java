package com.ecs.ucm.ccla;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/** Converts numeric vars into a word-based representation.
 *  
 *  E.g. convert(101) -> "one hundred one"
 *  
 *  Found here: http://www.rgagnon.com/javadetails/java-0426.html
 * 
 * @author Real's How-To
 *
 */
public class NumberToWords {
	private static final String[] tensNames = {
		"",
		" ten",
		" twenty",
		" thirty",
		" forty",
		" fifty",
		" sixty",
		" seventy",
		" eighty",
		" ninety"
	};

	private static final String[] numNames = {
		"",
		" one",
		" two",
		" three",
		" four",
		" five",
		" six",
		" seven",
		" eight",
		" nine",
		" ten",
		" eleven",
		" twelve",
		" thirteen",
		" fourteen",
		" fifteen",
		" sixteen",
		" seventeen",
		" eighteen",
		" nineteen"
	};

	private static String convertLessThanOneThousand(int number) {
		String soFar;
		if (number % 100 < 20){
			soFar =  numNames[number % 100];
			number /= 100;
		}
		else {
			soFar = numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0) return soFar;
		if (soFar.equals("")) return numNames[number] + " hundred";
		return numNames[number] + " hundred and " + soFar;
	}
		
	public static String convertToGBP(BigDecimal number) {

		NumberFormat PLAIN_NUMBER_FORMAT = NumberFormat.getInstance();
		PLAIN_NUMBER_FORMAT.setGroupingUsed(false);
		PLAIN_NUMBER_FORMAT.setMaximumFractionDigits(2);
		PLAIN_NUMBER_FORMAT.setMinimumFractionDigits(2);
		
		String floatString = PLAIN_NUMBER_FORMAT.format(number);
		
		String pounds = null, pence = null;
		
		int spotPos = floatString.indexOf(".");

		if (spotPos > -1) {
			String penceStr = floatString.substring(spotPos+1, floatString.length());
			String poundsStr = floatString.substring(0, spotPos);
			
			pounds = convert(Integer.parseInt(poundsStr)).trim();
			
			if (Integer.parseInt(penceStr) != 0) {
				pence = convert(Integer.parseInt(penceStr)).trim();
				return pounds + " pounds and " + pence + " pence";
			} else {
				return pounds + " pounds only";
			}
			
		} else {
			pounds = convert(Integer.parseInt(floatString)).trim();
			
			return pounds + " pounds only";
		}
	}	
		
	public static String convert(long number) {
		// 0 to 999 999 999 999
		if (number == 0) { return "zero"; }

		String snumber = Long.toString(number);

		// pad with "0"
		String mask = "000000000000";
		DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);

		// XXXnnnnnnnnn 
		int billions = Integer.parseInt(snumber.substring(0,3));
		// nnnXXXnnnnnn
		int millions  = Integer.parseInt(snumber.substring(3,6)); 
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6,9)); 
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9,12));    

		String tradBillions;
		switch (billions) {
		case 0:
			tradBillions = "";
			break;
		case 1 :
			tradBillions = convertLessThanOneThousand(billions) 
			+ " billion ";
			break;
		default :
			tradBillions = convertLessThanOneThousand(billions) 
			+ " billion ";
		}
		String result =  tradBillions;

		String tradMillions;
		switch (millions) {
		case 0:
			tradMillions = "";
			break;
		case 1 :
			tradMillions = convertLessThanOneThousand(millions) 
			+ " million ";
			break;
		default :
			tradMillions = convertLessThanOneThousand(millions) 
			+ " million ";
		}
		result =  result + tradMillions;

		String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
			tradHundredThousands = "";
			break;
		case 1 :
			tradHundredThousands = "one thousand ";
			break;
		default :
			tradHundredThousands =  convertLessThanOneThousand(hundredThousands); 
			tradHundredThousands += " thousand ";
		}
		result =  result + tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		if (result.length()!=0 && tradThousand.length()<0){
			result += " and ";
			}

		result =  result + tradThousand;

		// remove extra spaces!
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	/**
	 * testing
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("*** " + convert(1));
		System.out.println("*** " + convert(10));
		System.out.println("*** " + convert(1000));
		System.out.println("*** " + convert(20000));
		System.out.println("*** " + convert(300000));
		System.out.println("*** " + convert(55300));
		System.out.println("*** " + convert(6000000));
		System.out.println("*** " + convert(71231100));
		System.out.println("*** " + convert(800000000));
		System.out.println("*** " + convert(10123411));
		System.out.println("*** " + convert(1992211));
		System.out.println("*** " + convert(9000000));
		
		System.out.println("*** " + convert(123456789));
		
		//System.out.println("*** " + convert(3000000010L));
		System.out.println(convertToGBP(new BigDecimal("1.00")));
		System.out.println(convertToGBP(new BigDecimal("1.99")));
		System.out.println(convertToGBP(new BigDecimal("12.99")));
		System.out.println(convertToGBP(new BigDecimal("123.99")));
		System.out.println(convertToGBP(new BigDecimal("1234.99")));

		System.out.println(convertToGBP(new BigDecimal("12.00")));
		System.out.println(convertToGBP(new BigDecimal("123.00")));
		System.out.println(convertToGBP(new BigDecimal("1234.00")));
		
		System.out.println("*** " + convert((long)(8159.30)));
		
		System.out.println("*** " + convertToGBP(new BigDecimal("8159.35")));
		System.out.println("*** " + convertToGBP(new BigDecimal("8159.30")));
		System.out.println("*** " + convertToGBP(new BigDecimal("8159.32")));
		System.out.println("*** " + convertToGBP(new BigDecimal("8159.03")));
		System.out.println("*** " + convertToGBP(new BigDecimal("8159.00")));
		System.out.println("*** " + convertToGBP(new BigDecimal("0.02")));
		System.out.println("*** " + convertToGBP(new BigDecimal("0.33")));
	}
}
