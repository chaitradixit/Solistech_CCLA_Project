package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.LinkedHashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.data.Company;

/** Wrapper object for a set of field group comparison checks against two Aurora entities
 *  of the same type <A>
 * 
 * @author tm
 *
 */
public class AuroraEntityComparisonOutcome<A> {
	
	private final Vector<FieldGroupAttributesComparisonOutcome<A>> comparisons;
	
	private final ComparisonOutcome comparisonOutcome;
	
	/** Explains why a comparison check can't be executed. */
	private final String errorMsg;
	
	/** True if all field groups match. */
	private boolean matching;
	/** True if one or more critical field groups are mismatched. */
	private boolean criticalMismatch;
	
	/** Unique outcome for an Aurora entity comparison check.
	 * 
	 * @author tm
	 *
	 */
	public enum ComparisonOutcome {
		/** Required data is missing from the DB entity to run a comparison */
		INVALID_DATA ("Invalid data"),
		/** Corresponding Aurora entity missing - nothing to compare against! */
		NO_AURORA_ENTITY ("No corresponding Aurora entity"),
		/** All DB/Aurora Field Group values match */
		MATCH ("Full match"),
		/** One or more non-critical DB/Aurora Field Group values are mismatched */
		MISMATCH ("Non-critical fields mismatched"),
		/** One or more critical DB/Aurora Field Group values are mismatched */
		CRITICAL_MISMATCH ("Critical fields mismatched");
		
		private String label;
		
		private ComparisonOutcome(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
	
	/** Wrapper class for Company/ComparisonOutcome maps.
	 *  
	 *  Used for building summary reports.
	 *  
	 * @author tm
	 *
	 */
	public static class CompanyComparisonOutcome<A> {
		private Company company;
		private AuroraEntityComparisonOutcome<A> outcome;
		
		public Company getCompany() {
			return company;
		}
		public AuroraEntityComparisonOutcome<A> getOutcome() {
			return outcome;
		}
		public CompanyComparisonOutcome(Company company,
				AuroraEntityComparisonOutcome<A> outcome) {
			super();
			this.company = company;
			this.outcome = outcome;
		}
		
		public static <A> DataResultSet getResultSet
		 (Vector<CompanyComparisonOutcome<A>> outcomes) {
			DataResultSet rs = new DataResultSet(new String[] {
				"COMPANY_ID",
				"COMPANY_CODE",
				"OUTCOME",
				"OUTCOME_LABEL",
				"ERROR_MESSAGE"
			});
			
			for (CompanyComparisonOutcome<?> outcome : outcomes) {
				Vector<String> v = new Vector<String>();
				
				v.add(Integer.toString(outcome.getCompany().getCompanyId()));
				v.add(outcome.getCompany().getCode());
				v.add(outcome.getOutcome().getComparisonOutcome().toString());
				v.add(outcome.getOutcome().getComparisonOutcome().getLabel());
				v.add(outcome.getOutcome().getErrorMsg());
				
				rs.addRow(v);
			}
			
			return rs;
		}
	}
	
	public ComparisonOutcome getComparisonOutcome() {
		return this.comparisonOutcome;
	}
	
	public String getErrorMsg() {
		return this.errorMsg;
	}
	
	public AuroraEntityComparisonOutcome
	 (Vector<FieldGroupAttributesComparisonOutcome<A>> comparisons) {
		
		this.comparisons = comparisons;
		this.matching = true;
		
		// Determine overall comparison outcome. If a single Field Group comparison
		// is mismatched then the overall outcome is 'false'
		for (FieldGroupAttributesComparisonOutcome<A> fieldGroupComparison 
			: this.comparisons) {
			if (!fieldGroupComparison.getOutcome()) {
				// This field group failed the comparison check
				this.matching = false;
				
				if (fieldGroupComparison.getFieldGroupConfig().isCritical())
					this.criticalMismatch = true;
			}
		}
		
		// Now determine the outcome.
		if (this.isMatching()) {
			this.comparisonOutcome = ComparisonOutcome.MATCH;
			this.errorMsg = "All key fields match";
		} else if (this.isCriticalMismatch()) {
			this.comparisonOutcome = ComparisonOutcome.CRITICAL_MISMATCH;
			this.errorMsg = "One or more critical field groups are mismatched";
		} else {
			this.comparisonOutcome = ComparisonOutcome.MISMATCH;
			this.errorMsg = "One or more non-critical field groups are mismatched";
		}
	}
	
	/** Use this constructor to store invalid comparison checks.
	 *  
	 *  This will be due to 2 reasons:
	 *  -Central DB entity data is not valid for running a comparison, due to missing
	 *   or invalid data
	 *  -The corresponding Aurora entity doesn't exist, so nothing to compare against.
	 *  
	 * @param comparisonOutcome
	 * @param errorMsg
	 * @throws DataException 
	 */
	public AuroraEntityComparisonOutcome
	 (ComparisonOutcome comparisonOutcome, String errorMsg) throws DataException {
		
		this.comparisons = null;
		
		if (comparisonOutcome != ComparisonOutcome.INVALID_DATA
			&&
			comparisonOutcome != ComparisonOutcome.NO_AURORA_ENTITY)
			throw new DataException("Use the other constructor. This one is for error" +
			 "or failed states only");
		
		this.comparisonOutcome = comparisonOutcome;
		this.errorMsg = errorMsg;
	}

	public Vector<FieldGroupAttributesComparisonOutcome<A>> getComparisons() {
		return comparisons;
	}
	
	public Vector<String> getMismatchedFieldGroupNames() {
		Vector<String> groupNames = new Vector<String>();
		
		if (this.comparisons == null)
			return groupNames;
		
		for (FieldGroupAttributesComparisonOutcome<A> fieldGroupComparison 
			: this.comparisons) {
			if (!fieldGroupComparison.getOutcome()) {
				// This field group failed the comparison check
				groupNames.add(fieldGroupComparison.getFieldGroupConfig().getName());
			}
		}
		
		return groupNames;
	}

	public boolean isMatching() {
		return matching;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("AuroraEntityComparisonOutcome: matching=" + isMatching() + ", " +
		 "no. of field group comparisons=" + this.getComparisons().size() + ", " +
		 "no. of mismatched groups=" + this.getMismatchedFieldGroupNames().size());
		sb.append("\n");
		for (FieldGroupAttributesComparisonOutcome<A> fgComparison : this.getComparisons()) {
			sb.append(fgComparison.toString());
		}
		
		return sb.toString();
	}
	
	public DataResultSet getSummaryResultSet() {
		DataResultSet rsComparisonSummary = new DataResultSet(
			new String[] {
				"MATCH_OUTCOME", 
				"ERROR_MESSAGE",
				"HAS_COMPARISON_DATA",
				"IS_MATCHING",
				"CRITICAL_MISMATCH",
				"NUM_GROUP_COMPARISONS", 
				"NUM_MISMATCHED_GROUPS",
				"MISMATCHED_GROUP_NAMES"
			}
		);
		
		Vector<String> v = new Vector<String>();
		
		v.add(this.getComparisonOutcome().toString());
		v.add(this.getErrorMsg());
		v.add(this.comparisons != null ? "1" : "0");
		v.add(this.isMatching() ? "1" : "0");
		v.add(this.isCriticalMismatch() ? "1" : "0");
		v.add(this.getComparisons() != null ? 
		 Integer.toString(this.getComparisons().size()) : null);
		v.add(Integer.toString(this.getMismatchedFieldGroupNames().size()));
		
		// Build comma-separated list of mismatched group names
		String groupNames = "";
		for (String groupName : this.getMismatchedFieldGroupNames()) {
			if (groupNames.length() > 0)
				groupNames += ", ";
			
			groupNames += groupName;
		}
		
		v.add(groupNames);
		
		rsComparisonSummary.addRow(v);
		return rsComparisonSummary;
	}
	
	public DataResultSet getMismatchedGroupsResultSet() {
		DataResultSet rs = new DataResultSet(
			new String[] {
				"MISMATCHED_GROUP_NAME"
			}
		);
		
		for (String groupName : this.getMismatchedFieldGroupNames()) {
			Vector<String> v = new Vector<String>();
			
			v.add(groupName);
			rs.addRow(v);
		}
		
		return rs;
	}
	
	public LinkedHashMap<FieldGroupConfig, DataResultSet> getDetailResultSets() 
	 throws DataException {
		
		if (this.comparisons == null)
			throw new DataException("Unable to build detail ResultSets -" +
			 " no comparison data available");
		
		LinkedHashMap<FieldGroupConfig, DataResultSet> rsMap =
		 new LinkedHashMap<FieldGroupConfig, DataResultSet>();

		for (FieldGroupAttributesComparisonOutcome<A> groupComparison 
			: this.getComparisons()) {
			DataResultSet rs = groupComparison.getComparisonResultSet();
			
			rsMap.put(groupComparison.getFieldGroupConfig(), rs);
		}

		return rsMap;
	}

	public boolean isCriticalMismatch() {
		return criticalMismatch;
	}
}
