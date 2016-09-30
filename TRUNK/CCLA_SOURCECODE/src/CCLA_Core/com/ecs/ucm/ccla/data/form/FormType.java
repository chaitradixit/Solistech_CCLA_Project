package com.ecs.ucm.ccla.data.form;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.utils.ClassLoaderUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_FORM_TYPE table.
 * 
 * @author Tom
 *
 */
public class FormType implements Persistable {

	private int formTypeId;
	private String name;
	
	private InstructionType genInstructionType; // Instruction Type assigned to
												// generated spool file/form items
	private InstructionType retInstructionType; // Instruction Type assigned to
												// returned forms of this type
	
	private Class<? extends FormHandler> handlerClass;
	
	/** Short-hand version of the form type name, used for naming spool files etc.
	 * 
	 */
	private String shortName;
	private boolean singleton; 
	
	/** The Element Type which must be supplied to generate forms of this type. */
	private ElementType elementType;
	
	public static class Cols {
		public static final String ID 				= "FORM_TYPE_ID";
		public static final String NAME 			= "FORM_TYPE_NAME";
		
		public static final String GEN_INSTR_TYPE 	= "GEN_INSTRUCTION_TYPE_ID";
		public static final String RET_INSTR_TYPE 	= "RET_INSTRUCTION_TYPE_ID";
		
		public static final String FORM_HANDLER_CLASS = "FORM_HANDLER_CLASS";
		
		public static final String SHORT_NAME		= "FORM_TYPE_SHORTNAME";
		public static final String SINGLETON		= "IS_SINGLETON";
	}
	
	/** Entries from the REF_FORM_TYPE table.
	 * 
	 * @author Tom
	 *
	 */
	// PSDF Non-LA Form
	public static final int PSDF_NON_LA 		= 1;
	// PSDF LA Form
	public static final int PSDF_LA 			= 2;
	// PSDF Subscription Form
	public static final int PSDF_SUBSCRIPTION 	= 3;
	// PSDF Redemption Form
	public static final int PSDF_REDEMPTION		= 4;
	// PSDF Cancellation Form
	public static final int PSDF_CANCELLATION 	= 5;
	// PSDF Additional Account Form
	public static final int PSDF_ADDITIONAL_ACCOUNT = 6;
	// PSDF Payment Details Form
	//public static final int PSDF_PAYMENT_DETAILS = 7;
	
	// PSDF Blank Non-LA Form
	public static final int PSDF_BLANK_NON_LA	= 8;
	// PSDF Blank LA Form
	public static final int PSDF_BLANK_LA		= 9;
	
	// PSDF Additional Withdrawal Bank Account Form
	public static final int PSDF_ADDITIONAL_WITHDRAWAL_BANK_ACCOUNT = 10;
	// PSDF Change of Dividend Payment Form
	public static final int PSDF_CHANGE_OF_DIVIDEND_PAYMENT = 11;
	
	// Dio Loan Application form
	public static final int DIOLOAN_APP = 12;

	// Dio Loan Draw-down form
	public static final int DIOLOAN_DRAWDOWN = 13;
	// Dio Loan Redemption/Repayment form
	public static final int DIOLOAN_REDEMPTION = 14;
	
	// Standard Email Indemnity Form
	public static final int EMAIL_INDEMNITY = 15;
	
	// Segregated Client application/registration form
	public static final int SEG_CLIENT_APP = 16;
	
	// Community First Client Info form
	public static final int COMMUNITY_FIRST_CLIENT_INFO = 17;
	
	// Email Indemnity Form for Community First Campaign
	public static final int EMAIL_INDEMNITY_COMMUNITY_FIRST = 18;
	
	// Community First Donor Subscription Form
	public static final int COMMUNITY_FIRST_DONOR_SUBSCRIPTION = 19;
	// Old-style Community First Donor Subscription Form, includes TTLA information
	public static final int COMMUNITY_FIRST_DONOR_TTLA_SUBSCRIPTION = 24;
	
	// RPI Surplus Drawdown form - spool file generation handled by eDBA
	public static final int COMMUNITY_FIRST_RPI_SURPLUS_DRAWDOWN = 23;
	
	// Community First On-boarding Covering Letter
	public static final int COMMUNITY_FIRST_ONBOARDING_COVERING_LETTER = 20;
	
	// CBF Client Confirmation form
	public static final int CBF_CLIENT_CONFIRMATION = 21;
	// CBF Client Info form
	public static final int CBF_CLIENT_INFO = 22;
	
	public FormType(int formTypeId, String name,
			InstructionType genInstructionType,
			InstructionType retInstructionType,
			Class<? extends FormHandler> handlerClass,
			String shortName,
			boolean singleton, ElementType elemType) {
		this.formTypeId = formTypeId;
		this.name = name;
		this.genInstructionType = genInstructionType;
		this.retInstructionType = retInstructionType;
		this.handlerClass = handlerClass;
		this.shortName = shortName;
		this.singleton = singleton;
		this.elementType = elemType;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}
	
	public static Vector<FormType> getAll(FWFacade facade) throws DataException {
		
		Vector<FormType> v = new Vector<FormType>();
		
		DataResultSet rs = 
		 facade.createResultSet("qCore_GetAllFormTypes", new DataBinder());
		
		if (rs.first()) {
			do {
				v.add(get(rs));
			} while (rs.next());
		}
		
		return v;
	}
	
	@SuppressWarnings("unchecked")
	public static FormType get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		InstructionType genInstrType = null;
		InstructionType retInstrType = null;
		
		Integer genInstrTypeId = 
		 CCLAUtils.getResultSetIntegerValue(rs, Cols.GEN_INSTR_TYPE);
		Integer retInstrTypeId = 
		 CCLAUtils.getResultSetIntegerValue(rs, Cols.RET_INSTR_TYPE);
			
		if (genInstrTypeId != null)
			genInstrType = 
			 InstructionType.getIdCache().getCachedInstance(genInstrTypeId);
		
		if (retInstrTypeId != null)
			retInstrType =
			 InstructionType.getIdCache().getCachedInstance(retInstrTypeId);
		
		// Attempt to instantiate a Class object from the Handler Class name.
		Class<? extends FormHandler> handlerClass = null;
		String handlerClassName = rs.getStringValueByName(Cols.FORM_HANDLER_CLASS);

		try {
			ClassLoader cl = ClassLoaderUtils.getComponentClassLoader();
			handlerClass = (Class<? extends FormHandler>) cl.loadClass(handlerClassName);
		} catch (Exception e) {
			String msg = "Failed to initialize Form Handler class " + handlerClassName
			 + ": " + e.getMessage();
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}		
		
		ElementType elemType = null;
		Integer elemTypeId = CCLAUtils.getResultSetIntegerValue
		 (rs, ElementType.Cols.ID);
		
		if (elemTypeId != null)
			elemType = ElementType.getCache().getCachedInstance(elemTypeId);
		
		return new FormType(
			CCLAUtils.getResultSetIntegerValue(rs,Cols.ID),
			rs.getStringValueByName(Cols.NAME),
			genInstrType,
			retInstrType,
			handlerClass,
			CCLAUtils.getResultSetStringValue(rs, Cols.SHORT_NAME),
			CCLAUtils.getResultSetBoolValue(rs, Cols.SINGLETON),
			elemType
		);
	}
	
	/** Fetch all entries from REF_FORM_TYPE with the corresponding Element Type.
	 * 
	 * @param elementType
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataByElementType
	 (ElementType elementType, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, ElementType.Cols.ID, elementType.getElementTypeId());
		
		return facade.createResultSet
		 ("qCore_GetFormTypesByElementTypeId", binder);
	}

	public int getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(int formTypeId) {
		this.formTypeId = formTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InstructionType getGenInstructionType() {
		return genInstructionType;
	}

	public void setGenInstructionType(InstructionType genInstructionType) {
		this.genInstructionType = genInstructionType;
	}

	public InstructionType getRetInstructionType() {
		return retInstructionType;
	}

	public void setRetInstructionType(InstructionType retInstructionType) {
		this.retInstructionType = retInstructionType;
	}

	public Class<? extends FormHandler> getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(Class<? extends FormHandler> handlerClass) {
		this.handlerClass = handlerClass;
	}
	
	/** Returns a new instance of this form's handler class.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public FormHandler getHandlerInstance(Form form, String userName, FWFacade facade) 
	 throws DataException {
		try {
			Object[] args = new Object[3];
			
			args[0] = form;
			args[1] = userName;
			args[2] = facade;
			
			return this.getHandlerClass().getConstructor
			 (FormHandler.getFormConstructorParams()).newInstance(args);
			
		} catch (Exception e) {
			throw new DataException("Failed to instantiate Form Handler Class: " + 
			 this.getHandlerClass().getName(), e);
		}
	}
	
	public FormHandler getHandlerInstance(String userName, FWFacade facade) 
	 throws DataException {
		
		try {
			Object[] args = new Object[2];
			
			args[0] = userName;
			args[1] = facade;
			
			return this.getHandlerClass().getConstructor
			 (FormHandler.getConstructorParams()).newInstance(args);
			
		} catch (Exception e) {
			throw new DataException("Failed to instantiate Form Handler Class: " + 
			 this.getHandlerClass().getName(), e);
		}
	}
	
	/** Generates a new form instance for the current type, using the data linked to
	 *  the given Element.
	 *  
	 * @param element
	 * @param userName
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public Form generateForm(Element element, String userName, FWFacade facade) 
	 throws DataException, ServiceException {
		
		// Ensure that this form type has a designated Element Type, and the passed
		// Element matches this type.
		if (this.getElementType() == null
			||
			!(element.getType().equals(this.getElementType()))) {
			throw new DataException("Unable to generate new form, element ID " 
			 + element.getElementId() + " had the wrong type (expected: " + 
			 this.getElementType() == null ? null : this.getElementType().getName()
			 + ")");
		}
		
		FormHandler handler = getHandlerInstance(userName, facade);
		return handler.generateForm(this, element);
	}
	
	private static FormTypeCache CACHE = new FormTypeCache();
	
	public static Cachable<Integer, FormType> getCache() {
		return CACHE;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	public ElementType getElementType() {
		return elementType;
	}

	/** InstructionType cache implementor.
	 *  
	 *  Maps Instruction Type Names againsts Instruction Type instances.
	 *  
	 *  */
	private static class FormTypeCache extends Cachable<Integer, FormType> {

		public FormTypeCache() {
			super("Form Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<FormType> formTypes = FormType.getAll(facade);
			
			HashMap<Integer, FormType> newCache = 
			 new HashMap<Integer, FormType>();
			
			for (FormType formType : formTypes) {
				newCache.put(formType.getFormTypeId(), formType);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
