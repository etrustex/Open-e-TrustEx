package eu.europa.ec.etrustex.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.ICIALevelDAO;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.services.ICIALevelService;

@Service("ciaLevelService")
public class CIALevelService implements ICIALevelService {
	
	@Autowired
	private ICIALevelDAO ciaLevelDao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public CIALevel getCIALevel(Long id){
		return ciaLevelDao.read(id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public CIALevel retrieveCIALevel(Integer confidentiality, Integer integrity, Integer availability) {
		List<CIALevel> levels = ciaLevelDao.retrieveCIALevel(confidentiality, integrity, availability);
		if (levels != null && !levels.isEmpty()) {
			return levels.get(0);
		}
		return null;
	}
	

}
