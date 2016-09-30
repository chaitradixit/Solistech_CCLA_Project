package com.ecs.ucm.ccla.aurora.compare;

import java.util.Vector;

import intradoc.data.DataException;

/**
 * Used to run a comparison between 2 Aurora Field Sets. Matching field set types is 
 * enforced by generics.
 * 
 * @author tm
 *
 * @param <C> the type of Central DB entity the Aurora entity is based on
 * @param <A> the type of Aurora entity being compared.
 */
public class AuroraEntityComparator<C extends AuroraEntitySource, A> {
	
	public AuroraEntityComparisonOutcome<A> compare
	 (AuroraFieldSet<C, A> fs1, AuroraFieldSet<C, A> fs2) throws DataException {
	
		Vector<FieldGroupAttributesComparisonOutcome<A>> fieldGroupComparisons = 
		 new Vector<FieldGroupAttributesComparisonOutcome<A>>();

		int fieldGroupIndex = 0;
		
		for (FieldGroup<C,A> a1FieldGroup : fs1.getFieldGroups()) {
			FieldGroup<C,A> a2FieldGroup =
			 fs2.getFieldGroups().get(fieldGroupIndex);
			
			FieldGroupAttributesComparisonOutcome<A> fieldGroupComparison = 
			 new FieldGroupAttributesComparisonOutcome<A>
			 (a1FieldGroup.getFieldGroupConfig(),
			 a1FieldGroup.getFieldGroupAttributes(), 
			 a2FieldGroup.getFieldGroupAttributes());
			
			fieldGroupComparisons.add(fieldGroupComparison);
			fieldGroupIndex++;
		}
		
		return new AuroraEntityComparisonOutcome<A>(fieldGroupComparisons);
	}
	
}
