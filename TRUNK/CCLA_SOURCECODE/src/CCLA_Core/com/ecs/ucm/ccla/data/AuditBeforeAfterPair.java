package com.ecs.ucm.ccla.data;

/**
 * Simple bean class to hold values following comparison of data
 * 
 * @author Keith
 * 
 */
public class AuditBeforeAfterPair {

		public AuditBeforeAfterPair() {
			
		}
		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key
		 *            the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the valueBefore
		 */
		public String getValueBefore() {
			return valueBefore;
		}

		/**
		 * @param valueBefore
		 *            the valueBefore to set
		 */
		public void setValueBefore(String valueBefore) {
			this.valueBefore = valueBefore;
		}

		/**
		 * @return the valueAfter
		 */
		public String getValueAfter() {
			return valueAfter;
		}

		/**
		 * @param valueAfter
		 *            the valueAfter to set
		 */
		public void setValueAfter(String valueAfter) {
		    if(valueAfter!=null && valueAfter.trim().length()==0) {
			this.valueAfter = null;
		    } else {
			this.valueAfter = valueAfter;
		    }
		}

		private String key;
		private String valueBefore;
		private String valueAfter;

	}

