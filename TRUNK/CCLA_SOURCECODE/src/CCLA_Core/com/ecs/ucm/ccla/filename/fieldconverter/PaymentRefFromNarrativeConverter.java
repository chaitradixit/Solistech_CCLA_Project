package com.ecs.ucm.ccla.filename.fieldconverter;

import java.util.HashMap;
import java.util.regex.Matcher;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.filename.MetadataFieldConverter;
import com.ecs.ucm.ccla.filename.MetadataFieldMapping;
import com.ecs.utils.StringUtils;

/** Attempts to extract a Payment Ref value from a narrative line of a bank transaction.
 * 
 * @author Tom
 *
 */
public class PaymentRefFromNarrativeConverter implements MetadataFieldConverter {

	public void convert(MetadataFieldMapping fieldMapping,
	 String fileNameValue, HashMap<String, String> mapping) {
		
		String possiblePaymentRef = fileNameValue;
		
		if (possiblePaymentRef.length() > 0) {
			Matcher paymentRefMatcher = 
			 Subscription.CONTRIBUTION_PAYMENT_REF_PATTERN.matcher(possiblePaymentRef);
			
			if (paymentRefMatcher.find()) {
				String paymentRef = paymentRefMatcher.group();
				
				// Found what looks to be a Payment Ref!
				mapping.put(UCMFieldNames.PaymentRef, paymentRef);
			}
		}
	}

}
