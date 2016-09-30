package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import com.aurora.webservice.Address;
import com.aurora.webservice.Correspondent;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.utils.ucm.Facade;

public class ComparisonTests {
	
	static Correspondent c1 = new Correspondent(
		123, "1", 
		"Mr T N Marchant", "Mr Marchant",
		new Address(
			"OrgName", "BuildingName", 
			"StreetName", "LocalityName", "TownName", "PostcodeName",
			"CountryName", "CountyName", "Telephone", "FaxName",
			"EmailName", "WebsiteAddressName"
		),
		"Envelope",
		0,
		true,
		false
	);
	
	static Correspondent c2 = new Correspondent(
		123, "1", 
		"Mr N Marchant", "Mr Marchant",
		new Address(
			"OrgName", "BuildingName", 
			"StreetName", "LocalityName", "TownName", "PostcodeName",
			"CountryName", "CountyName", "Telephone", "FaxName",
			"EmailName", "WebsiteAddressName"
		),
		"Envelope",
		0,
		true,
		false
	);
	
	public static void main(String[] args) throws DataException {
		CorrespondentFieldSet cfs1 = new CorrespondentFieldSet(c1);
		CorrespondentFieldSet cfs2 = new CorrespondentFieldSet(c2);
		
		AuroraEntityComparator<Person, Correspondent> compCorr = 
		 new AuroraEntityComparator<Person, Correspondent>();
		
		AuroraEntityComparisonOutcome<Correspondent> outcome = compCorr.compare(cfs1, cfs2);
		
		System.out.println(outcome.toString());
	}
}
