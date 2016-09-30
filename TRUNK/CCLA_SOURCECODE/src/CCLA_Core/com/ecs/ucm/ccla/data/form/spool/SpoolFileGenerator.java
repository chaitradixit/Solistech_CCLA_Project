package com.ecs.ucm.ccla.data.form.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Address;

/** Abstract superclass for all spool file generator classes.
 *  Contains various shared variables.
 *  
 * @author Tom Marchant
 *
 */
public abstract class SpoolFileGenerator {
		
	/** Wrapper class for a single spool file line data.
	 * 
	 * @author Tom
	 *
	 */
	public static class SpoolFileLine {
		public String name;
		public String value;
		
		public SpoolFileLine(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
	
	/** Verbose date format used for printing dates on covering
	 *  letters
	 */
	protected static final SimpleDateFormat COVER_LETTER_DATE_FORMAT = 
	 new SimpleDateFormat("dd MMMMM yyyy");
	 
	/** Format used for 8-box date fields on forms
	 * 
	 */
	protected static final SimpleDateFormat EIGHT_CHAR_DATE_FORMAT = 
	 new SimpleDateFormat("ddMMyyyy");	
	
	/** Used for unit counts, to ensure they are returned with up 
	 *  to 4 decimal places
	 */
	protected static final NumberFormat UNIT_COUNT_FORMAT = 
	 new DecimalFormat("0.####");
	
	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 20;
	
	/** Form ID values will always be padding to this length using preceding 
	 *  zeroes */
	public static final int BARCODE_PADDING = 9;
	
	/** Stores the spool file contents while it is being generated */
	protected BufferedWriter 	writer;
	/** Wrapper class for header values */
	protected SpoolHeader		header;
	/** Corresponding UCM dDocName for this spool file */
	protected String			docName;
	
	/** Corresponding Client Services process ID */
	protected Integer			processId;
	
	public SpoolFileGenerator(SpoolHeader header) {
		this.header = header;
	}
	
	public SpoolFileGenerator(SpoolHeader spoolHeader,
	 String docName, Integer processId) {
		this(spoolHeader);
		
		this.docName 	= docName;
		this.processId 	= processId;
	}
	
	public abstract ByteArrayOutputStream createSpoolFile()
	 throws IOException, DataException, ServiceException;
	
	/** Adds a report header to the BufferedWriter. */
	protected void addReportHeader(String reportId) 
	 throws IOException, DataException {
		
		SpoolFileUtils.addReportHeader(writer, reportId);
		writer.newLine();
	}
	
	/** Adds a name-value line to the BufferedWriter. */
	public void addNameValueString(String name, String value) 
	 throws DataException, IOException {
		
		String nameValue = getSpoolNameValueString(name, value);
		writer.write(nameValue);
		writer.newLine();
	}
	
	/** Adds a name-value line to the BufferedWriter, with the given padding. */
	protected void addNameValueString(String name, String value, int nameValuePadding) 
	 throws DataException, IOException {
		
		String nameValue = getSpoolNameValueString(name, value, nameValuePadding);
		writer.write(nameValue);
		writer.newLine();
	}
	
	/** Adds a set of SpoolFileLines to the BufferedWriter, with the given padding. */
	public void addLines(Vector<SpoolFileLine> lines, int nameValuePadding) 
	 throws IOException, DataException {
		
		for (SpoolFileLine line : lines) {
			String nameValue = getSpoolNameValueString
			 (line.name, line.value, nameValuePadding);
			
			writer.write(nameValue);
			writer.newLine();
		}
	}
	
	/** Returns the given name/value pair formatted for use in a
	 *  spool file, with the default NAME_VALUE_PADDING padding from the 
	 *  SpoolFileGenerator class definition.
	 *  
	 * @param name
	 * @param value
	 * @return
	 */
	public static String getSpoolNameValueString(String name, String value)
	 throws DataException {
		
		String nameValue 	= SpoolFileUtils.getSpoolValueLabel
		 (name, NAME_VALUE_PADDING);
		
		if (value != null)
			nameValue 			+= value;
		
		return nameValue;
	}
	
	/** Returns the given name/value pair formatted for use in a spool file, with
	 *  the given padding size.
	 *  
	 * @param name
	 * @param value
	 * @return
	 */
	public static String getSpoolNameValueString
	 (String name, String value, int nameValuePadding) throws DataException {
		
		String nameValue 	= SpoolFileUtils.getSpoolValueLabel(name, nameValuePadding);
		
		if (value != null)
			nameValue 			+= value;
		
		return nameValue;
	}
	
	/** Appends a comma-separated rendition of the given address, designed to fit
	 *  into a form character grid, with the grid's dimensions specified by with and
	 *  height parameters.
	 * 
	 *  The passed address can be null, this will output a set of blank lines instead.
	 *  
	 * @param address
	 * @param namePrefix
	 * @param width
	 * @param height
	 * @return
	 */
	public static Vector<SpoolFileLine> getGridFormatAddress
	 (Address address, String namePrefix, 
	 boolean includePostCode, boolean includeCountry,
	 int width, int height) {
		
		Vector<SpoolFileLine> v = new Vector<SpoolFileLine>();
		
		if (address == null) {
			// Return a set of lines with a null value
			for (int i=0; i<height; i++)
				v.add(new SpoolFileLine(namePrefix + (i+1), null));
		} else {
			// Fetch printable rendition of address first.
			Vector<String> addressPieces = address.getPrintableAddress
			 (includePostCode, includeCountry);
			
			int totalAvailableChars = width * height;
			
			// Generate a comma-separated string containg as many complete address
			// pieces as possible with the available chars.
			StringBuffer fullAddress = new StringBuffer();
			
			for (String addressPiece : addressPieces) {
				if (fullAddress.length() + addressPiece.length() > totalAvailableChars) {
					// Ran out of space for any more address pieces!
					break;
				}
				
				if (fullAddress.length() > 0)
					fullAddress.append(",");
				
				fullAddress.append(addressPiece);
			}
			
			// Spread the concatenated address across SpoolFileLine instaces.
			int currentAddressCharPosition = 0;
			
			for (int i=0; i<height; i++) {
				String lineChars = null;
				
				if (currentAddressCharPosition < fullAddress.length()) {
					int endIndex = ((i*width) + width) > fullAddress.length() ?
									fullAddress.length() : ((i*width) + width);
					
					lineChars = fullAddress.substring((i*width), endIndex);
					currentAddressCharPosition = ((i*width) + width);
				}
				
				v.add(new SpoolFileLine(namePrefix + (i+1), lineChars));
			}
		}
		
		return v;
	}
	
	/** Writes the SpoolHeader contents to the BufferedWriter instance.
	 * 
	 * @throws SpoolFileException
	 * @throws DataException
	 * @throws IOException
	 */
	protected void writeHeader() throws SpoolFileException, DataException, 
	 IOException {
		
		if (writer == null)
			throw new SpoolFileException("BufferedWriter not initialized");
		
		if (header == null)
			throw new SpoolFileException("SpoolHeader not initialized");
		
		addNameValueString("Company", header.company);
		addNameValueString("WorkingDate", header.workingDate);
		addNameValueString("User Name", header.userName);
		addNameValueString("Workstation", header.workstation);
		addNameValueString("PrintDate", header.printDate);

		addNameValueString("Records", Integer.toString(header.records));
		addNameValueString("TemplatePath", header.templatePath);

		addNameValueString("AnacompDocType", header.anacompDocType);
		addNameValueString("AnacompRetGrpNo", header.anacompRetGrpNo);
		
		addNameValueString("ReleaseVersion", header.releaseVersion);
	}
	
	/** Writes the NEW SpoolHeader contents to the BufferedWriter instance. 
	 *  This one doesn't bother with the Anacomp fields and adds a FormID instead.
	 * 
	 * @throws SpoolFileException
	 * @throws DataException
	 * @throws IOException
	 */
	protected void writeNewHeader() throws SpoolFileException, DataException, 
	 IOException {
		
		if (writer == null)
			throw new SpoolFileException("BufferedWriter not initialized");
		
		if (header == null)
			throw new SpoolFileException("SpoolHeader not initialized");
		
		addNameValueString("Company", header.company);
		addNameValueString("ClientNumber", header.clientNumber);
		addNameValueString("WorkingDate", header.workingDate);
		addNameValueString("User Name", header.userName);
		addNameValueString("Workstation", header.workstation);
		addNameValueString("PrintDate", header.printDate);

		addNameValueString("Records", Integer.toString(header.records));
		addNameValueString("TemplatePath", header.templatePath);
		
		addNameValueString("FormID", CCLAUtils.padString(
		 Integer.toString(header.formId), '0', BARCODE_PADDING));
		
		addNameValueString("OrganisationID", Integer.toString(header.orgId));
		
		addNameValueString("ReleaseVersion", header.releaseVersion);
	}
}
