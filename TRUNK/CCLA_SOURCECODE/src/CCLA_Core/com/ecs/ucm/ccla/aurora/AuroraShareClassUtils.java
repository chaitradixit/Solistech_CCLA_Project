package com.ecs.ucm.ccla.aurora;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import com.aurora.webservice.ArrayOfShareClassMovement;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AuroraShareClassUtils {

	/**
	 * Create an aurora pending share class movement for today
	 * @param account
	 * @param shareClassCode
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static boolean createAuroraPendingShareClassMovement(Account account, int shareClassCode, FWFacade facade) 
	throws DataException, ServiceException 
	{	
		if (StringUtils.stringIsBlank(account.getFundCode())) 
			throw new DataException("Account with id:"+account.getAccountId()+" has no fund code");

		Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
		
		if (fund==null) 
			throw new DataException("Account with id:"+account.getAccountId()+" has no fund code");		
		
		Company company = fund.getCompany();
		if (company==null)
			throw new DataException("Fund :"+fund.getFundCode()+ ", has no company");					
		
		AuroraAccountHandler handler = new AuroraAccountHandler();
		
		com.aurora.webservice.Account auroraAcc = 
		 handler.getExistingAuroraEntity(account, company, facade);
		
		if (auroraAcc!=null)
			throw new DataException("Cannot find AccountAccount for AccountId "+account.getAccountId());
		
		boolean success = false;
		
		try {
			success = 
				AuroraWebServiceHandler.getAuroraWS().createAccountPendingShareClassMovement(
						company.getCode(), 
						auroraAcc.getAccountNumberExternal(), 
						shareClassCode);
		} catch (RemoteException re) {			
			throw new ServiceException("Cannot CreateAuroraPendingShareClassMovement for Account:"
					+account.getAccountId()+", error:"+re.getMessage());
		}
		return success;
	}		
	
	/**
	 * Gets the pending shareClass movement for an account in Aurora.
	 * @param account
	 * @param facade
	 * @return ArrayOfShareClassMovement
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static ArrayOfShareClassMovement getAuroraPendingShareClassMovement(Account account, Date requestDate, FWFacade facade) 
	throws DataException, ServiceException 
	{	
		if (StringUtils.stringIsBlank(account.getFundCode())) 
			throw new DataException("Account with id:"+account.getAccountId()+" has no fund code");

		Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
		
		if (fund==null) 
			throw new DataException("Account with id:"+account.getAccountId()+" has no fund code");		
		
		Company company = fund.getCompany();
		if (company==null)
			throw new DataException("Fund :"+fund.getFundCode()+ ", has no company");					
		
		
		AuroraAccountHandler handler = new AuroraAccountHandler();
		
		com.aurora.webservice.Account auroraAcc = 
		 handler.getExistingAuroraEntity(account, company, facade);
		
		if (auroraAcc!=null)
			throw new DataException("Cannot find AccountAccount for AccountId "+account.getAccountId());
		
		return getAuroraPendingShareClassMovement(auroraAcc, company, DateUtils.getNow(), facade);
	}
	
	
	/**
	 * Gets all pending movements for an aurora account.
	 * @param aurAccount
	 * @param company
	 * @param requestDate
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static ArrayOfShareClassMovement getAuroraPendingShareClassMovement(
			com.aurora.webservice.Account aurAccount, 
			Company company, 
			Date requestDate, 
			FWFacade facade) throws DataException, ServiceException 
	{
		ArrayOfShareClassMovement movements = null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(requestDate);
		
		try {
			movements = 
				AuroraWebServiceHandler.getAuroraWS().getPendingShareClassMovementByAccountNumber(
						company.getCode(), 
						aurAccount.getAccountNumberExternal(), 
						cal);
		} catch (RemoteException re) {			
			throw new ServiceException("Cannot getAuroraPendingShareClassMovement for aurora Account:"
					+aurAccount.getAccountNumberExternal()+", error:"+re.getMessage());
		}
		return movements;	
	}
}
