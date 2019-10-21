package eu.europa.ec.etrustex.services;

public interface ITrustExService {
	
	public <T> T findEntityByPK(Long entityID);

}
