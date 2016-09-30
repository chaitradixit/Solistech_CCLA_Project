package com.ecs.ucm.ccla.data.subscription;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.product.Product;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

public class ContributionType {
	
	public static class Cols {
		public static final String ID = "CONTRIBUTION_TYPE_ID";
	}
	
	public static class Queries {
		public static final String GET_ALL = "qCore_GetAllContributionTypes";
		public static final String GET_ALL_BY_PRODUCT =
		 "qCore_GetAllContributionTypesByProduct";
	}
	
	public static class Ids {
		public static final int ELIGIBLE_FOR_GOVERNMENT_MATCH 	= 1;
		public static final int GIFT_AID_ENDOWMENT 				= 2;
		public static final int GIFT_AID_GRANTS_AND_SOCIAL_INV 	= 3;
		public static final int GOVERNMENT_MATCH				= 4;
	}
	
	public static DataResultSet getAllDataByProduct(int productId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Product.Cols.ID, productId);
		
		return facade.createResultSet(Queries.GET_ALL_BY_PRODUCT, binder);
	}
	
	
}
