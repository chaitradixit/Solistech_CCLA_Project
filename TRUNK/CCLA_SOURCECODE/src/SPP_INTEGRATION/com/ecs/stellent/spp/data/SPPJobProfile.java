package com.ecs.stellent.spp.data;

import java.util.Date;
import java.util.Vector;

import com.ecs.stellent.spp.Variable;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;

import com.ecs.utils.Log;

import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

/** Wrapper class for managing SPP job variable mappings.
 * 
 *  Each profile corresponds to a different 'Map' in SPP, i.e. a particular variable
 *  and Map Name configuration which will trigger a specific type of job in SPP.
 * 
 * @author Tom
 *
 */
public class SPPJobProfile {

	// Standard document/instruction-based job profile.
	public static SPPJobProfile JOB_PROFILE;
	
	// Query/Complaint/Error/Breach job profile. Same as standard profile, with extra
	// fields for handling QCEB jobs.
	public static SPPJobProfile QUERY_PROFILE;
	
	private String profileName;
	private String mapName;
	
	private Vector<JobVariable> workflowVariables;
	
	private String[] sppVarNames; 
	private String[] ucmVarNames; 
	private short[] sppVarTypes;
	
	// Builds the static WorkflowProfile instances. Must be called during UCM startup.
	public static void init() throws DataException {
		JOB_PROFILE = new SPPJobProfile("JOB_PROFILE");
		QUERY_PROFILE = new SPPJobProfile("QUERY_PROFILE");
	}	
	
	/** Numeric encodings for SPP variable data types.
	 * 
	 * @author Tom
	 *
	 */
	public static class VariableDataTypes {
		public static final short SHORT 	= 2;
		public static final short LONG 		= 3;
		public static final short FLOAT 	= 4;
		public static final short DOUBLE	= 5;
		public static final short CURRENCY	= 6;
		public static final short DATE		= 7;
		public static final short STRING 	= 8;
		public static final short BOOLEAN	= 11;
		public static final short DECIMAL	= 14;
		public static final short BYTE		= 17;
	}
	
	/** Wrapper for a single SPP variable. Includes its SPP name, type, and UCM name.
	 * 
	 * @author Tom
	 *
	 */
	public static class JobVariable {
		private String ucmVarName;
		private String sppVarName;
		private short sppVarDataType;
		
		public JobVariable(String ucmVarName, String sppVarName,
				short sppVarDataType) {
			this.ucmVarName = ucmVarName;
			this.sppVarName = sppVarName;
			this.sppVarDataType = sppVarDataType;
		}
		
		public String getUcmVarName() {
			return ucmVarName;
		}

		public String getSppVarName() {
			return sppVarName;
		}

		public short getSppVarDataType() {
			return sppVarDataType;
		}
		
		/** Builds an empty SPP Variable instance that corresponds to this 
		 *  WorkflowVariable instance. Useful when building arrays of Variable instances
		 *  when triggering SPP jobs.
		 *
		 * @return
		 */
		public Variable getEmptyVariableInstance() {
			Variable variable = new Variable();
			variable.setName(this.getSppVarName());
			variable.setType(this.getSppVarDataType());
			
			return variable;
		}
		
		public Variable getVariableInstance(String value) throws DataException {
			Variable variable = new Variable();
			variable.setName(this.getSppVarName());
			variable.setType(this.getSppVarDataType());
			
			if (StringUtils.stringIsBlank(value)) {
				variable.setValue(null);
				return variable;
			}
			
			try {
			
				// Cast the passed String to the appropriate object type for this Variable.
				if (this.getSppVarDataType() == VariableDataTypes.STRING) {
					variable.setValue(value);
				} else if (this.getSppVarDataType() == VariableDataTypes.SHORT) {
					variable.setValue(new Short(value));
				} else if (this.getSppVarDataType() == VariableDataTypes.LONG) {
					variable.setValue(new Long(value));
				} else if (this.getSppVarDataType() == VariableDataTypes.FLOAT
							||
							this.getSppVarDataType() == VariableDataTypes.DECIMAL) {
					variable.setValue(new Float(value));	
				} else if (this.getSppVarDataType() == VariableDataTypes.DOUBLE
							||
							this.getSppVarDataType() == VariableDataTypes.CURRENCY) {
					variable.setValue(new Double(value));	
				} else if (this.getSppVarDataType() == VariableDataTypes.DATE) {
					Date date = null;
					
					date = DateFormatter.getSystemSimpleDateFormat().parse(value);
					date = SppIntegrationUtils.getSppDate(date);
					
					variable.setValue(date);
	
				} else if (this.getSppVarDataType() == VariableDataTypes.BOOLEAN) {
					Boolean paramBoolean = !StringUtils.stringIsBlank(value) &&
					 !value.equals("N") && !value.equals("No")
					 && !value.equals("0") 
					 && !value.equals("False");
					
					variable.setValue(paramBoolean);
				} else if (this.getSppVarDataType() == VariableDataTypes.BYTE) {	
					variable.setValue(new Byte(value));
				}
			} catch (Exception e) {
				String msg = "Failed to parse value for SPP Job Variable: " + 
				 this.getSppVarName() + ": " + e.getMessage();
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
			
			return variable;
		}
	}
	
	public SPPJobProfile(String profileName) throws DataException{
		this.profileName = profileName;
		
		this.mapName = SharedObjects.getEnvironmentValue("SPP_INT_SPP_WORKFLOW_NAME_" 
				+ this.profileName);
		
		this.sppVarNames = SharedObjects.getEnvironmentValue(
				"SPP_INT_FIELD_MAP_SPPNAME_" + this.profileName).split(",");
		
		this.ucmVarNames = SharedObjects.getEnvironmentValue(
				"SPP_INT_FIELD_MAP_UCMNAME_" + this.profileName).split(",");
		
		String[] varTypesStr = SharedObjects.getEnvironmentValue(
				"SPP_INT_FIELD_MAP_SPPTYPE_" + this.profileName).split(",");
		
		this.sppVarTypes = new short[varTypesStr.length];
		
		for (int i=0; i<varTypesStr.length; i++) {
			this.sppVarTypes[i] = Short.parseShort(varTypesStr[i]);
		}
		
		if (sppVarNames.length != ucmVarNames.length 
			||
			sppVarNames.length != sppVarTypes.length
			||
			ucmVarNames.length != sppVarTypes.length) {
				throw new DataException("Unable to create Workflow profile '" +
				 profileName + ": field/data mapping entries had different " +
				 "lengths (" + sppVarNames.length + ", " + ucmVarNames.length +
				 ", " + sppVarTypes.length + ")");
		}
		
		workflowVariables = new Vector<JobVariable>();
		
		for (int i=0; i<ucmVarNames.length; i++) {
			JobVariable workflowVariable = new JobVariable(
				ucmVarNames[i],
				sppVarNames[i],
				sppVarTypes[i]
			);
			
			workflowVariables.add(workflowVariable);
		}
	}

	public String getProfileName() {
		return profileName;
	}

	public String getMapName() {
		return mapName;
	}

	public String[] getSppVarNames() {
		return sppVarNames;
	}

	public String[] getUcmVarNames() {
		return ucmVarNames;
	}

	public short[] getSppVarTypes() {
		return sppVarTypes;
	}

	public int getNumVariables() {
		return workflowVariables.size();
	}
	
	public Vector<JobVariable> getJobVariables() {
		return workflowVariables;
	}
}
