package eu.europa.ec.cipa.etrustex.services;

public interface ITrustExService {
	
	public <T> T findEntityByPK(Long entityID);

}
