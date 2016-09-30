package com.ecs.stellent.ccla.clientservices.campaign;

import intradoc.data.DataBinder;
import intradoc.data.DataException;

import java.util.Hashtable;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.SubscriptionUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionType;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CommunityFirstPaymentHandler {
	
	public Hashtable<String, String> getSubscriptionPaymentDocMeta
	 (Subscription subscription, String docClass, FWFacade cdbFacade) 
	 throws DataException {
		
		Hashtable<String, String> paymentRefMeta = new Hashtable<String, String>();
		
		Vector<Contribution> contribs = Contribution.getAllBySubscriptionId
		 (subscription.getId(), cdbFacade);
		
		// Always add the SUBSCRIPTION_ID
		paymentRefMeta.put
		 (Subscription.Cols.ID, Integer.toString(subscription.getId()));
		
		Integer contribTypeId = null;
		
		if (!contribs.isEmpty()) {
			contribTypeId = contribs.get(0).getContributionTypeId();
			
			// Determine the Community First deposit account we are after from the 
			// Contribution Type ID
			String accountTypeName = 
			 CommunityFirstClientEnrolmentHandler.getAccountType(contribTypeId);
			
			Account depositAccount = Account.getCFAccount
			 (SubscriptionUtils.CF_DEPOSIT_FUND, accountTypeName, cdbFacade);
			
			if (depositAccount != null) {
				Integer ownerOrgId = depositAccount.getOwnerOrganisationId(cdbFacade);
				
				Entity ownerOrg = Entity.get(ownerOrgId, cdbFacade);
				
				Company accountCompany = Fund.getCache().getCachedInstance
				 (SubscriptionUtils.CF_DEPOSIT_FUND).getCompany();
				
				AuroraClient ownerAuroraClient = Entity.getAuroraClientCompanyMapping
				 (ownerOrgId, accountCompany, cdbFacade);
				
				Fund fund = Fund.getCache().getCachedInstance
				 (depositAccount.getFundCode());
				
				paymentRefMeta.put
				 (Globals.UCMFieldNames.ClientName, ownerOrg.getOrganisationName());
				
				paymentRefMeta.put
				 (Globals.UCMFieldNames.OrgAccountCode, ownerOrg.getOrgAccountCode());
				
				paymentRefMeta.put
				 (Globals.UCMFieldNames.Company, fund.getCompany().getCode());
				
				paymentRefMeta.put
				 (Globals.UCMFieldNames.Fund, depositAccount.getFundCode());
				
				paymentRefMeta.put
				 (Globals.UCMFieldNames.ClientNumber, 
				 ownerAuroraClient.getPaddedClientNumber());

				paymentRefMeta.put
				 (Globals.UCMFieldNames.AccountNumber, 
				 depositAccount.getAccountNumberString());
				
				if (!StringUtils.stringIsBlank(docClass) &&
					docClass.equals(DocumentClass.Classes.DEPCHQ)) {
					// This has been explicitly marked as a 'cheque' class - don't
					// touch the doc class in this case.
				} else {
					paymentRefMeta.put
					 (Globals.UCMFieldNames.DocClass, DocumentClass.Classes.DEPBNK);
				}
			}
		}
		
		
		return paymentRefMeta;
	}
	
}
