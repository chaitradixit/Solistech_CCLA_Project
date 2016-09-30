package com.ecs.ucm.ccla.data.product;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Wrapper for REF_APP_PROD_ASSET_INVESTMENT table.
 *  
 *  Mapping between Products and their available investments (Funds/Assets)
 *  
 * @author Tom
 *
 */
public class ApplicableProductAssetInvestment {
	
	public static class Queries {
		public static final String GET_ALL = 
		 "qCore_GetAllApplicableProductAssetInvestments";
		public static final String GET_APPLICABLE_FUNDS_BY_PRODUCT_ID =
		 "qCore_GetApplicableFundsByProductId";
	}
	
	/** Returns a ResultSet of applicable Funds (i.e. matched entries in the FUND table)
	 *  for investment against the given Product ID
	 *  
	 * @param productId
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getApplicableFundsForInvestmentByProductData
	 (int productId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Product.Cols.ID, productId);
		
		return facade.createResultSet
		 (Queries.GET_APPLICABLE_FUNDS_BY_PRODUCT_ID, binder);
	}
}
