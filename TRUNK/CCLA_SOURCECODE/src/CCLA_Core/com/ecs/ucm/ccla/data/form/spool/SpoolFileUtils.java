package com.ecs.ucm.ccla.data.form.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import com.aurora.webservice.BankDetails;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

/** Utility methods for generating spool files.
 * 
 * @author Tom Marchant
 *
 */
public class SpoolFileUtils {
	
	/** Character used as report section delimiter. */
	public static final String REPORT_DELIMITER = "~";
	
	/** Values are always printed after this number of columns on each line. */
	public static final int	DEFAULT_NAME_VALUE_PADDING = 20;

	/** Returns the given name/value pair formatted for use in a
	 *  spool file.
	 *  
	 * @param name
	 * @param value
	 * @return
	 */
	public static String getSpoolNameValueString(String name, String value)
	 throws DataException {
		
		String nameValue 	= SpoolFileUtils.getSpoolValueLabel
		 (name, DEFAULT_NAME_VALUE_PADDING);
		
		if (value != null)
			nameValue 			+= value;
		
		return nameValue;
	}

	/** For a given value name, returns the formatted name
	 *  string required by the Aurora Create!Form function.
	 *  
	 *  Currently formats as:
	 *  
	 *  '<name>            '
	 *  
	 *  The padded string length should correspond to the value of the
	 *  NAME_VALUE_PADDING static value in SpoolFileGenerator classes.
	 *  An error is thrown if the name exceeds this length.
	 *  
	 * @param name
	 * @return
	 */
	public static String getSpoolValueLabel(String name, int padding)
	 throws DataException {
		
		StringBuffer paddedName = new StringBuffer();
		paddedName.append("<").append(name).append(">");
		
		if (paddedName.length() > (padding-1))
			throw new DataException("Passed value name '" + name + "' " +
			 "exceeds maximum size.");
		
		// Pad with spaces
		while (paddedName.length() < (padding-1)) {
			paddedName.append(" ");
		} 
		
		return paddedName.toString();
	}

	public static void addReportHeader(BufferedWriter writer, String reportName)
	 throws IOException, DataException {
		writer.write(SpoolFileUtils.REPORT_DELIMITER);
		writer.newLine();
		writer.write(getSpoolNameValueString("ReportID", reportName));
		writer.newLine();
	}
	
	
	/** Takes an Aurora web service Address instance and converts
	 *  it to an array of Strings. If any address fields are null/blank
	 *  they are ignored and not added to the Vector.
	 *  
	 *  This helps to 'compact' the address before its displayed on the
	 *  form.
	 *  
	 * @return a Vector of address fields, maximum size of 8.
	 */
	public static Vector getNormalizedAuroraAddress(com.aurora.webservice.Address address) {
		
		Vector<String> v = new Vector<String>();
		
		if (!StringUtils.stringIsBlank(address.getOrganisation()))
			v.add(address.getOrganisation());
		if (!StringUtils.stringIsBlank(address.getBuilding()))
			v.add(address.getBuilding());
		if (!StringUtils.stringIsBlank(address.getStreet()))
			v.add(address.getStreet());
		if (!StringUtils.stringIsBlank(address.getLocality()))
			v.add(address.getLocality());
		if (!StringUtils.stringIsBlank(address.getTown()))
			v.add(address.getTown());
		if (!StringUtils.stringIsBlank(address.getCounty()))
			v.add(address.getCounty());
		if (!StringUtils.stringIsBlank(address.getPostCode()))
			v.add(address.getPostCode());
		if (!StringUtils.stringIsBlank(address.getCountry()))
			v.add(address.getCountry());
		
		return v;
	}
	
	/** Takes an Address instance and converts
	 *  it to a Vector of Strings. If any address fields are null/blank
	 *  they are ignored and not added to the Vector.
	 *  
	 *  This helps to 'compact' the address before its displayed on the
	 *  form, removing any blank lines.
	 * 
	 * @param address
	 * @param includePostcode 	whether to include the postcode in the Vector
	 * @param includeCountry 	whether to include the country in the Vector.
	 * 							Should be false when displaying Client addresses,
	 * 							the core data column has been used to store phone
	 * 							numbers!
	 * @return a Vector of address fields, maximum size of 8.
	 */
	public static Vector<String> getNormalizedAddress(Address address, 
	 boolean includePostcode, boolean includeCountry) {
		
		Vector<String> v = new Vector<String>();
		
		if (!StringUtils.stringIsBlank(address.getFlat()))
			v.add(address.getFlat());
		if (!StringUtils.stringIsBlank(address.getHouseName()))
			v.add(address.getHouseName());
		if (!StringUtils.stringIsBlank(address.getHouseNumber()))
			v.add(address.getHouseNumber());
		if (!StringUtils.stringIsBlank(address.getStreet()))
			v.add(address.getStreet());;
		if (!StringUtils.stringIsBlank(address.getCity()))
			v.add(address.getCity());
		if (!StringUtils.stringIsBlank(address.getCounty()))
			v.add(address.getCounty());
		
		if (includePostcode) {
			if (!StringUtils.stringIsBlank(address.getPostCode()))
				v.add(address.getPostCode());
		}
		
		if (includeCountry) {
			if (!StringUtils.stringIsBlank(address.getCountry()))
				v.add(address.getCountry());
		}
		
		return v;
	}

	
	/** Takes a list of strings and wraps them over the number
	 *  of lines specified in the lineLengths array.
	 *  
	 *  The value of each entry in lineLengths determines how many
	 *  characters can fit across each line.
	 *  
	 *  Each separate, non-empty value in the strings list is delimited
	 *  by a space. If the includeComma flag is set, each item is also
	 *  suffixed with a comma.
	 *  
	 *  The method will first attempt a 'nice' fit, i.e. one string
	 *  to each line. If this does not work it will wrap the strings
	 *  over the given line set.
	 *  
	 *  TODO: sort out this shit code
	 *  
	 * @return
	 */
	public static Vector<String> wrapToLines
	 (Vector<String> strings, int[] lineLengths, boolean includeComma) {
		
		Vector<String> v = new Vector<String>();
		
		int delimSize = 1; 	// specifies how many characters are used to
							// delimit each string.
		if (includeComma)
			delimSize = 2;
		
		Log.debug("Wrapping " + strings.size() + " strings over " 
		 + lineLengths.length + " lines");
		
		if (strings.size() == 0)
			return strings;
			
		if (strings.size() <= lineLengths.length) {
			Log.debug("Attempting nice fit first.");
			
			boolean failedNiceFit = false;
			
			for (int i=0; (i<strings.size() && !failedNiceFit); i++) {
				if (strings.get(i).length() <= lineLengths[i])
					v.add(strings.get(i));
				else
					failedNiceFit = true;
			}
			
			if (failedNiceFit) {
				Log.debug("Failed nice fit. Performing wrap instead.");
				v = new Vector<String>();
			} else {
				Log.debug("Nice fit achieved.");
				return v;
			}
		}
		
		int curLine = 0;
		int curString = 0;
		
		char[] stringChars = null;
		int curStringChar  = 0;
		
		char[] lineChars = null;
		int curLineChar = 0;
		
		while (true) {
			int remainingStringChars = 0;
			
			boolean getNewLine = false;
			
			if (lineChars == null)
				getNewLine = true;
			
			if (stringChars != null) {
				// No. of remaining characters in current string
				remainingStringChars = (stringChars.length - curStringChar); 
				
				if (curLineChar >= (lineChars.length-delimSize)) {
					// Line has been used. Store to array.
					v.add(new String(lineChars));
					
					if (curLine == (lineLengths.length - 1)) {
						// No more lines left - finish.
						
						// Check if any chars/strings remain unused
						if (remainingStringChars > 0 || curString < strings.size()) {
							Log.warn(remainingStringChars + 
							 " chars and " + (strings.size()-curString) + 
							 " strings were not allocated to lines.");
						}
							
						break;
					}
					
					curLine ++;
					getNewLine = true;
				}
			}
			
			if (getNewLine) {
				// Destination char array for current line
				lineChars	= getSpacedCharArray(lineLengths[curLine]);
				curLineChar = 0;
			}
			
			if (remainingStringChars == 0) {
				// No chars remain in current string. Attempt to fetch next one.
	
				if (curString == strings.size()) {
					// Output the final line char array
					v.add(new String(lineChars));
					
					break; // No more strings to process - finish.
				}
				
				String string = strings.get(curString);
				curString++;

				stringChars 	= string.toCharArray();
				curStringChar 	= 0;
				
				remainingStringChars = stringChars.length;
				
				// Add space/comma if this isn't the first string, and
				// there is more than 2 character spaces left on the current
				// line.
				if (curString > 1 && (curLineChar > 0) 
					&& (curLineChar < (lineChars.length-delimSize))) {
					
					if (includeComma)
						lineChars[curLineChar] = ',';
					
					lineChars[curLineChar+1] = ' ';
					
					curLineChar += delimSize;
				}
			}
			
			if ((lineChars.length - curLineChar) >= remainingStringChars) {
				// Current string can fit on current line
				
				for (int j=curLineChar; j<(curLineChar + remainingStringChars); j++) {
					lineChars[j] = stringChars[curStringChar];
					curStringChar++;
				}
				
				// Update current char position for this line
				curLineChar += remainingStringChars;
				
			} else {
				// Current string can't be fit on the current line. Fit as
				// many chars as possible.
				
				for (int j=curLineChar; j<lineChars.length; j++) {
					lineChars[j] = stringChars[curStringChar];
					curStringChar++;
				}
				
				curLineChar = lineChars.length;
			}
		}
		
		return v;
	}
	
	private static char[] getSpacedCharArray(int size) {
		
		char[] chars = new char[size];
		
		for (int i=0; i<chars.length; i++) {
			chars[i] = ' ';
		}
		
		return chars;
	}
	
	public static void main(String[] args) {
		Vector<String> strings = new Vector<String>();
		
		strings.add("Hello");
		strings.add("My name is tom");
		strings.add("Cheamyyyy");
		
		Vector v = wrapToLines(strings, new int[] {8,8,8}, false);
		
		for (int i=0; i<v.size(); i++) {
			System.out.println("Line " + i + ": '" + v.get(i) + "'");
		}
	}
	
	public static String getPaddedBarcode(int barcode) {
		
		String barcodeStr = Integer.toString(barcode);
		
		barcodeStr = CCLAUtils.padString
		 (barcodeStr, '0', SpoolFileGenerator.BARCODE_PADDING);

		return barcodeStr;
	}
	
	/** Takes a sort code string and pads it with two dash characters,
	 *  i.e:
	 *  
	 *  '123456' -> '12-34-56'
	 *  
	 *  The string is initially padded with zeroes if it is less than
	 *  6 characters in length.
	 *  
	 * @param sortCode
	 * @return
	 */
	public static String formatSortCode(String sortCode) {
		
		if (StringUtils.stringIsBlank(sortCode))
			return sortCode;
		
		String paddedSortCode	= 
		 CCLAUtils.padSortCode(sortCode);
		
		// support for 6 digit sort codes
		paddedSortCode = 
		 paddedSortCode.substring(0, 2) + "-" +
		 paddedSortCode.substring(2, 4) + "-" + 
		 paddedSortCode.substring(4, paddedSortCode.length());
		
		return paddedSortCode;
	}
	
	/** Removes all spaces from the passed String.
	 * 
	 * @param string
	 * @return
	 */
	public static String stripAllSpaces(String string) {
		
		if (string == null)
			return null;
		
		string = string.trim();
		
		while (string.indexOf(" ") > -1) {
			string = StringUtils.replaceInString(string, " ", "");
		}
		
		return string;
	}
	
	/** Returns either null, "Y" or "N" which reflects the value of the passed Boolean.
	 * 
	 * @param val
	 * @return
	 */
	public static String getTickboxValue(Boolean val) {
		if (val == null) return null;
		return (val) ? "Y" : "N";
	}
	
	/** Adds Person name fields, DOB and Position to form output.
	 *  
	 *  The passed Person can be null.
	 *  
	 * @param person
	 * @param prefix
	 * @throws IOException 
	 * @throws DataException 
	 */
	public static void addPersonDetails
	 (SpoolFileGenerator generator, Person person, String prefix) 
	 throws DataException, IOException {
		addPersonDetails(generator, person, prefix, true, true);
	}
	
	/** Adds Person name fields, DOB and Position to form output.
	 *  
	 *  The passed Person can be null.
	 *  
	 * @param person
	 * @param prefix
	 * @throws IOException 
	 * @throws DataException 
	 */
	public static void addPersonDetails
	 (SpoolFileGenerator generator, Person person, String prefix,
	 boolean includeDOB, boolean includePosition) 
	 throws DataException, IOException {
		
		PersonTitle title = null;
		
		if (person != null && person.getTitleId() != null)
			title = PersonTitle.getCache().getCachedInstance(person.getTitleId());
		
		generator.addNameValueString
		 (prefix + "Title", person != null && title != null ? 
		 title.getTitle() : null);
		generator.addNameValueString
		 (prefix + "Forename",
		 person != null ? person.getFirstName() : null);
		generator.addNameValueString
		 (prefix + "MiddleName",
		 person != null ? person.getMiddleName() : null);
		generator.addNameValueString
		 (prefix + "LastName",
		 person != null ? person.getLastName() : null);
		
		if (includeDOB) {
			generator.addNameValueString
			 (prefix + "DOB",
			 person != null && person.getDateOfBirth() != null ? 
			 SpoolFileGenerator.EIGHT_CHAR_DATE_FORMAT.format
			  (person.getDateOfBirth()) : null);
		}
		
		if (includePosition) {
			generator.addNameValueString(prefix + "Position", 
			 person != null ? person.getJobTitle() : null);
		}
	}
	
	/** Adds PrefHome/PrefOrg default address preference, i.e. whether the passed
	 *  default address is marked as a 'Home address' or 'Work address'.
	 * 
	 *  The defaultAddressContact parameter can be null - this will print empty lines
	 * 
	 * @param defaultAddressContact
	 * @param linePrefix
	 * @throws DataException
	 * @throws IOException
	 */
	public static void addCorrespondencePref
	 (SpoolFileGenerator generator, Contact defaultAddressContact, String linePrefix) 
	 throws DataException, IOException {
		
		if (defaultAddressContact != null) {
			boolean prefHome = false; 
			 // is default person address 'home address' type
			
			prefHome = 
			 (defaultAddressContact.getSubmethodId() 
			  == 
			 Contact.SUBMETHOD_ADDRESS_HOME);
			
			generator.addNameValueString
			 (linePrefix + "PrefHome", prefHome ? "Y" : null);
			generator.addNameValueString
			 (linePrefix + "PrefOrg", !prefHome ? "Y" : null);
		} else {
			// print empty lines.
			generator.addNameValueString(linePrefix + "PrefHome", null);
			generator.addNameValueString(linePrefix + "PrefOrg", null);
		}
	}
	
	/** Adds default telephone/email fields for the given Person.
	 * 
	 * @param person
	 * @param prefix
	 * @throws IOException 
	 * @throws DataException 
	 */
	public static void addPersonContactDetails
	 (SpoolFileGenerator generator, Person person, String prefix)
	 throws DataException, IOException {
		
		Vector<Contact> corrContacts 	= null;
		Contact telephone 				= null;
		Contact email 					= null;
		
		if (person != null) {
			corrContacts = person.getContacts();
			
			telephone = Contact.getDefaultContact(corrContacts, Contact.PHONE);
			email = Contact.getDefaultContact(corrContacts, Contact.EMAIL);
		}
			
		generator.addNameValueString(prefix + "Telephone", 
		 telephone != null ? 
		 CCLAUtils.stripSpaceCharacters(telephone.getValue()) : null);
		generator.addNameValueString(prefix  + "Email", 
		 email != null ? email.getValue() : null);	
	}
	
	/** Adds home address house name/number, postcode and date moved fields for the 
	 *  given Person.
	 * 
	 * @param person
	 * @param prefix
	 * @throws IOException 
	 * @throws DataException 
	 */
	public static void addPersonShortAddress 
	 (SpoolFileGenerator generator, Person person, String prefix) 
	 throws DataException, IOException {
		
		Contact homeAddressContact = null;
		
		if (person != null) {
			homeAddressContact = Contact.getFirstContactBySubMethod
			 (person.getContacts(), Contact.SUBMETHOD_ADDRESS_HOME);
		}
			
		Address homeAddress = homeAddressContact != null ? 
		 homeAddressContact.getAddress() : null;
			
		// Use either the house number or house name, depending on which is found
		// first.
		String houseNumberOrName = null;
		
		if (homeAddress != null) {
			if (homeAddress.getHouseNumber() != null)
				houseNumberOrName = homeAddress.getHouseNumber();
			else
				houseNumberOrName = homeAddress.getHouseName();
		}
				
		generator.addNameValueString(prefix + "HouseNumberName", houseNumberOrName);
		generator.addNameValueString(prefix + "Postcode", 
		 homeAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(homeAddress.getPostCode()) : null);
		
		// TODO
		generator.addNameValueString(prefix + "DateMovedToAddress", null);
	}
	
	/** Adds Bank Account data fields for the passed BankAccount instance.
	 *  
	 *  The passed instance can be null - this will output blank lines instead.
	 *  
	 * @param bankAccount
	 * @throws IOException 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void addBankAccountDetails
	 (SpoolFileGenerator generator, BankAccount bankAccount) 
	 throws DataException, IOException, ServiceException {
		
		BankDetails bankDetails = null;
		
		if (bankAccount != null)
			bankDetails = bankAccount.getBankDetails();
		
		generator.addNameValueString("BankName", 
		 bankDetails != null ? bankDetails.getBankName() : null);
		generator.addNameValueString("BranchName",
		 bankDetails != null ? bankDetails.getBranchTitle() : null);
		
		generator.addNameValueString("AccountName", 
		 bankAccount != null ? bankAccount.getAccountName() : null);
		generator.addNameValueString("SortCode", 
		 bankAccount != null ? bankAccount.getSortCode() : null);
		generator.addNameValueString("AccountNumber", 
		 bankAccount != null ? bankAccount.getAccountNumber() : null);
	}
}
