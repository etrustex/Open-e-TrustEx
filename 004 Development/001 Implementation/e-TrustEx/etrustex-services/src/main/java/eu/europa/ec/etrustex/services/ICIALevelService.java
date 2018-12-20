package eu.europa.ec.etrustex.services;

import eu.europa.ec.etrustex.domain.CIALevel;

public interface ICIALevelService {

	public CIALevel getCIALevel(Long id);
	
	public CIALevel retrieveCIALevel(Integer confidentiality, Integer integrity, Integer availability);
	
}
