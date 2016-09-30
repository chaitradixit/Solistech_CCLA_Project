package com.ecs.ucm.ccla.filename;

import com.ecs.utils.Log;
import intradoc.data.DataException;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.filename.fieldconverter.AccountNumberFieldConverter;
import com.ecs.ucm.ccla.filename.fieldconverter.AmountUnitsFieldConverter;
import com.ecs.ucm.ccla.filename.fieldconverter.DocumentDateConverter;
import com.ecs.ucm.ccla.filename.fieldconverter.PaymentRefFromNarrativeConverter;
import com.ecs.utils.StringUtils;

/** Utility methods for breaking up a delimited string into name-value pairs.
 * 
 * @author Tom
 *
 */
public class MetadataFromString {
	
	/** List of cached mapping profiles.
	 * 
	 */
	private static Vector<MetadataMappingProfile> profiles; 
	
	static {
		try {
			buildMappingProfiles();
		} catch (Exception e) {
			System.out.println("Failed to build mapping profiles: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/** For the given set of (assumed) String properties, iterate through available
	 *  mapping profiles until one is found that matches the target value string.
	 *  
	 *  If a match is found, extract the name/value pairs and return.
	 *  
	 *  If no matching profiles are found, return null.
	 *  
	 * @param props
	 * @return
	 * @throws DataException
	 */
	public static HashMap<String, String> extractFromProperties(Properties props) 
	 throws DataException {
		
		try {
			for (MetadataMappingProfile profile : profiles) {
				String[] metadataFields = profile.getSourceFields();
				
				for (String metadataField : metadataFields) {
					String sourceString = props.getProperty(metadataField);
					
					if (!StringUtils.stringIsBlank(sourceString)) {
						if (profile.matches(sourceString)) {
							Log.debug("Found a matching source string in field: " +
							 metadataField + " for profile: " + profile.getName());
							return profile.getMapping(sourceString);
						}
					}
				}
			}
			
		} catch (DataException e) {
			Log.error("Failed to extract metadata from string: " + e.getMessage(), e);
		}
		
		return null;
	}
	
	/** Converts the passed string to a HashMap of name/value pairs, as dictated by the
	 *  passed profile. 
	 * 
	 * @param fieldMappingProfile
	 * @param string
	 * @return
	 * @throws DataException
	 */
	public static HashMap<String, String> convert
	 (MetadataMappingProfile fieldMappingProfile, String string) throws DataException {
		return fieldMappingProfile.getMapping(string);
	}
	
	public static Vector<MetadataMappingProfile> getProfiles() {
		return profiles;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws DataException, ClassNotFoundException {
		
		String sourceString = "_a__b_c_"; //"hello_tom___";
		
		extractChunksFromString(sourceString, "_");
		
		buildMappingProfiles();
		
		String fileName1 = "BUYDF_12345_0001_C_20120734_UNI_1,350,000 00.PDF";
		String fileName2 = "BUYDF_1234X_0001_C_20120717_GBP_1,000.00.pdf";
		String fileName3 = "BUYDF_12345_0001_C_20120717_GBP_1,000 00.pdf";
		String fileName4 = "1_BUYDF_12345_0001_C_20120717_GBP_1,000 00.pdf";
		
		String fileName5 = "1_DICONDIN_20121008_GBP_390.00_client name_CFLF00001234-00003.pdf";
		
		Vector<String> testFileNames = new Vector<String>();
		testFileNames.add(fileName1);
		testFileNames.add(fileName2);
		testFileNames.add(fileName3);
		testFileNames.add(fileName4);
		testFileNames.add(fileName5);
		
		for (MetadataMappingProfile testProfile : getProfiles()) {
			System.out.println("---\nTesting mapping profile: " + testProfile.getName());
			
			for (String testFileName : testFileNames) {
				boolean matches = testProfile.matches(testFileName);
				
				System.out.println("Input string: " + testFileName + ", matches? " 
				 + matches);
				
				if (matches) {
					HashMap<String, String> fieldMapping = 
					 testProfile.getMapping(testFileName);
					
					System.out.println("Generated field mapping: ");
					
					for (String key : fieldMapping.keySet()) {
						System.out.println(key + "=" + fieldMapping.get(key));
					}
				}
			}
		}
	}
	
	/** Hard-coded profile definitions. Any changes to capture configurations must
	 *  be entered here for now, until the proper DB schema is in place.
	 *  
	 * @throws DataException 
	 *  
	 */
	public static void buildMappingProfiles() throws DataException {
		profiles = new Vector<MetadataMappingProfile>();
		
		// Define Field Mapping instances.
		MetadataFieldMapping docClass = new MetadataFieldMapping
		 ("DocClass", UCMFieldNames.DocClass, "[A-Z]+", null);
		
		MetadataFieldMapping clientNumber = new MetadataFieldMapping
		 ("ClientNumber", UCMFieldNames.ClientNumber, "[\\d]{1,6}", null);
		
		MetadataFieldMapping accountNumber = new MetadataFieldMapping
		 ("AccountNumber", UCMFieldNames.AccountNumber, "[\\d]{1,6}",  
		 AccountNumberFieldConverter.class);
		
		MetadataFieldMapping fundCode = new MetadataFieldMapping
		 ("FundCode", UCMFieldNames.Fund, "[A-Z]{1,2}", null);
		
		// Stopped the date being mapped to the metadata field now, due to issues with
		// UCM expecting an ISO date format sometimes!
		MetadataFieldMapping documentDate = new MetadataFieldMapping
		 ("DocDate", UCMFieldNames.DocumentDate + "1", "[\\d]{8}", 
		 DocumentDateConverter.class);
		
		MetadataFieldMapping currency = new MetadataFieldMapping
		 ("Currency", "Currency", "[A-Z]{3}", null);
		
		MetadataFieldMapping amount = new MetadataFieldMapping
		 ("Amount", "RawAmount", "(\\d|,)+((\\.|\\s)\\d{2}){0,1}", 
		 AmountUnitsFieldConverter.class);
		
		MetadataFieldMapping attachmentNo = new MetadataFieldMapping
		 ("AttachmentNumber", "AttachmentNumber", "(\\d)+", null);
		
		MetadataFieldMapping narrativeLine1 = new MetadataFieldMapping
		 ("NarrativeLine1", "NarrativeLine1", "(\\s|\\S){0,18}", 
		 PaymentRefFromNarrativeConverter.class);
		
		MetadataFieldMapping narrativeLine2 = new MetadataFieldMapping
		 ("NarrativeLine2", "NarrativeLine2", "(\\s|\\S){0,18}", 
		 PaymentRefFromNarrativeConverter.class);
		
		// Email attachments/classified items from BankLine spreadsheet.
		// Doesn't feature a numeric prefix.
		// E.g. BUYBNK_777959_001_S_20110104_GBP_5000.00.pdf
		MetadataMappingProfile emailAttachment = new MetadataMappingProfile
		 (1, 
		 "Email Attachment", 
		 "Standard PDF email attachment file format. Excludes attachment number prefix", 
		 "primaryFile|dDocTitle|xOriginalFileName", 
		 "pdf", 
		 "_"
		);
		
		Vector<ProfileFieldMapping> profile1FieldMappings = 
		 new Vector<ProfileFieldMapping>();
			
		profile1FieldMappings.add(new ProfileFieldMapping(1, docClass, 1, null));
		profile1FieldMappings.add(new ProfileFieldMapping(1, clientNumber, 2, null));
		profile1FieldMappings.add(new ProfileFieldMapping(1, accountNumber, 3, 1));
		profile1FieldMappings.add(new ProfileFieldMapping(1, fundCode, 4, null));
		profile1FieldMappings.add(new ProfileFieldMapping(1, documentDate, 5, 2));
		profile1FieldMappings.add(new ProfileFieldMapping(1, currency, 6, null));
		profile1FieldMappings.add(new ProfileFieldMapping(1, amount, 7, 3));
		
		emailAttachment.setFieldMappings(profile1FieldMappings);
		
		// Email attachments/classified items from BankLine spreadsheet.
		// Features a numeric prefix.
		// E.g. 1_BUYBNK_777959_001_S_20110104_GBP_5000.00.pdf
		MetadataMappingProfile emailAttachmentWithNumericPrefix = 
		 new MetadataMappingProfile
		 (2, 
		 "Email Attachment  (with attachment no.)", 
		 "Standard PDF email attachment file format. Includes attachment number prefix", 
		 "primaryFile|dDocTitle|xOriginalFileName", 
		 "pdf", 
		 "_"
		);
		
		Vector<ProfileFieldMapping> profile2FieldMappings = 
		 new Vector<ProfileFieldMapping>();
		
		profile2FieldMappings.add(new ProfileFieldMapping(2, attachmentNo, 1, null));
		profile2FieldMappings.add(new ProfileFieldMapping(2, docClass, 2, null));
		profile2FieldMappings.add(new ProfileFieldMapping(2, clientNumber, 3, null));
		profile2FieldMappings.add(new ProfileFieldMapping(2, accountNumber, 4, 1));
		profile2FieldMappings.add(new ProfileFieldMapping(2, fundCode, 5, null));
		profile2FieldMappings.add(new ProfileFieldMapping(2, documentDate, 6, 2));
		profile2FieldMappings.add(new ProfileFieldMapping(2, currency, 7, null));
		profile2FieldMappings.add(new ProfileFieldMapping(2, amount, 8, 3));
		
		emailAttachmentWithNumericPrefix.setFieldMappings(profile2FieldMappings);
		
		// Unknown transactions coming from BankLine spreadsheet. Includes payment
		// narrative information
		// E.g. DICONDIN_20121008_GBP_390.00_427440101_.pdf
		MetadataMappingProfile unknownTransaction = 
		 new MetadataMappingProfile
		 (3, 
		 "Unknown Cash", 
		 "Unknown cash transaction from BankLine download spreadsheet.", 
		 "primaryFile|dDocTitle|xOriginalFileName", 
		 "pdf", 
		 "_"
		);
		
		Vector<ProfileFieldMapping> profile3FieldMappings = 
		 new Vector<ProfileFieldMapping>();

		profile3FieldMappings.add(new ProfileFieldMapping(3, docClass, 1, null));
		profile3FieldMappings.add(new ProfileFieldMapping(3, documentDate, 2, 2));
		profile3FieldMappings.add(new ProfileFieldMapping(3, currency, 3, null));
		profile3FieldMappings.add(new ProfileFieldMapping(3, amount, 4, 3));
		profile3FieldMappings.add(new ProfileFieldMapping(3, narrativeLine1, 5, 4));
		profile3FieldMappings.add(new ProfileFieldMapping(3, narrativeLine2, 6, 5));
		
		unknownTransaction.setFieldMappings(profile2FieldMappings);
		
		// Unknown transactions coming from BankLine spreadsheet. Includes payment
		// narrative information and attachment number
		// E.g. 1_DICONDIN_20121008_GBP_390.00_427440101_.pdf
		MetadataMappingProfile unknownTransactionWithNumericPrefix = 
		 new MetadataMappingProfile
		 (4, 
		 "Unknown Cash (with attachment no.)", 
		 "Unknown cash transaction from BankLine download spreadsheet. " +
		  "Includes attachment number prefix", 
		 "primaryFile|dDocTitle|xOriginalFileName", 
		 "pdf", 
		 "_"
		);
		
		Vector<ProfileFieldMapping> profile4FieldMappings = 
		 new Vector<ProfileFieldMapping>();
		
		profile4FieldMappings.add(new ProfileFieldMapping(4, attachmentNo, 1, null));
		profile4FieldMappings.add(new ProfileFieldMapping(4, docClass, 2, null));
		profile4FieldMappings.add(new ProfileFieldMapping(4, documentDate, 3, 2));
		profile4FieldMappings.add(new ProfileFieldMapping(4, currency, 4, null));
		profile4FieldMappings.add(new ProfileFieldMapping(4, amount, 5, 3));
		profile4FieldMappings.add(new ProfileFieldMapping(4, narrativeLine1, 6, 4));
		profile4FieldMappings.add(new ProfileFieldMapping(4, narrativeLine2, 7, 5));
		
		unknownTransactionWithNumericPrefix.setFieldMappings(profile4FieldMappings);
		
		// Append all generated profiles to list
		profiles.add(emailAttachment);
		profiles.add(emailAttachmentWithNumericPrefix);
		profiles.add(unknownTransaction);
		profiles.add(unknownTransactionWithNumericPrefix);
	}
	
	/** Returns a list of substrings originating from sourceString, after its been 
	 *  broken up around occurences of the separator char.
	 *  
	 *  Some of the returned substrings may be empty strings. The standard 
	 *  String.split() call would discard leading and trailing empty strings, hence the
	 *  need for this function.
	 *  
	 *  Example, for input ("_a__b_c_", "_"), it would yield a 6-string list, containing
	 *  3 empty strings:
	 *  
	 *  1: ""
	 *  2: "a"
	 *  3: ""
	 *  4: "b"
	 *  5: "c"
	 *  6: ""
	 *  
	 * @param sourceString
	 * @param separator
	 * @return
	 */
	public static Vector<String> extractChunksFromString
	 (String sourceString, String separator) {
		
		// Iterate through the string, breaking it up into chunks around the separator
		// char, until we can't find any more instances of the separator.
		System.out.println("Attempting to break up " 
		 + sourceString.length() + "-character string around separator char: " 
		 + separator);
		
		int splitPosition = 0;
		Vector<String> chunks = new Vector<String>();
		 
		while (true) {
			// Search for next separator char.
			int nextSeparatorPosition = sourceString.indexOf(separator, splitPosition);

			if (nextSeparatorPosition == -1) {
				// No more separators chars.
				// Extract the remainder of the source string as the last chunk.
				nextSeparatorPosition = sourceString.length();
			}
			
			String thisChunk = sourceString.substring
			 (splitPosition, nextSeparatorPosition);
			
			System.out.println
			 ("Extracting substring: (" + splitPosition + "," 
			 + nextSeparatorPosition + "): '" + thisChunk + "'");
				
			chunks.add(thisChunk);
				
			splitPosition = nextSeparatorPosition+1;
			
			if (nextSeparatorPosition == sourceString.length())
				break; // Hit the end of the string - we're done.
			else
				// Advance past next separator char position.
				splitPosition = nextSeparatorPosition+separator.length();
		}
		
		System.out.println("Split string into " + chunks.size() + " chunks: ");
		for (String s : chunks) {
			System.out.println(s);
		}
		
		return chunks;
	}
}
