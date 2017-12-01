package eu.europa.ec.cipa.etrustex.services;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.cipa.etrustex.domain.MonitoringQuery;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.services.dao.IMetadataDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IMonitoringDAO;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class MonitoringService implements IMonitoringService {

	@Autowired
	private IMetadataDAO metadataDAO;
	
	@Autowired
	private IMonitoringDAO monitoringDAO;
	
	@Override
	public void monitor() {
		
		try{
			
			System.out.println(System.getProperty("weblogic.Name"));			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<MetaDataItem> mdList = metadataDAO.getDefaultMetadataByType(MetaDataItemType.FILE_STORE_PATH.toString());
		if (mdList == null)
			throw new RuntimeException("No default metadata");
		boolean foundFileStorePathMD = false;
		for(MetaDataItem md : mdList){
			if (MetaDataItemType.FILE_STORE_PATH.equals(md.getItemType())){
				foundFileStorePathMD = true;				
				if (md.getValue()!=null){
					String[] paths = md.getValue().split(",");
					for(String path : paths){					
						if (!new File(path).exists()){
							throw new RuntimeException("Path "+path+" does not exist");
						}						
					}				
				}else{
					throw new RuntimeException("No FILE_STORE_PATH value");
				}
				break;
				
			}			
		}
		
		if(!foundFileStorePathMD){
			throw new RuntimeException("No FILE_STORE_PATH metadata");
		}
		
		
		

	}
	
	public List<MonitoringQuery> runMonitoringQueries() {
		return monitoringDAO.runMonitoringQueries();
	}

}
