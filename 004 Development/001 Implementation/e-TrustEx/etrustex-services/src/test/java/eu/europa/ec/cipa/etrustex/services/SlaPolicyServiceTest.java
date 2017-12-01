package eu.europa.ec.cipa.etrustex.services;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/etrustex-services-test-context.xml"})
@TransactionConfiguration(defaultRollback=true)
@Transactional*/
public class SlaPolicyServiceTest{
	
	/*@Autowired
	private ISlaPolicyService slaPolicyService;

	@Autowired
	private IMetadataService metadataService;

	private String localFileStorePath = "E:\\etrFileStore";
	
	@Autowired
	private IMessageBinaryDAO messageBinaryDao;*/
	
	//private static int MAX_PAGING = 500;
	private static int MAX_FILES_BUFFER = 4;
	
//	@Test
//	public void testAll(){
//		long start = System.currentTimeMillis();
//		
//		slaPolicyService.run();
//
//		System.out.println("ISlaPolicyService testAll took " + (System.currentTimeMillis()-start) + "ms.");
//	}
	
//	@Test
//	public void deleteBinariesNotInFileStore(){
//		int startIndex = 0;
//		String pathname = null;		
//		boolean iterate = true;		
//		List<MessageBinary> binaries = null;
//		int filesDeleted = 0;
//		while(iterate){
//			binaries = messageBinaryDao.getMessageBinary(startIndex, MAX_PAGING);
//			for (MessageBinary messageBinary : binaries) {
//				pathname = messageBinary.getFileId(); 
//				if(!(new File(pathname)).exists()){
//					if(messageBinary.getMessage() != null){
//						System.err.println("**To be Deleted:--DocId:"+messageBinary.getMessage().getDocumentId()+"--Path::"+pathname);
//					}else{
//						System.err.println("**--------------Not associated to a Message:--BinaryId:"+messageBinary.getId()+"--Path::"+pathname);
//					}
//					
//					filesDeleted++;
//				}
//			}			
//			if(binaries.size() < MAX_PAGING){
//				iterate = false;
//			}else{
//				startIndex += MAX_PAGING;
//			}			
//		}
//		System.err.println("-->FilesDeleted:"+filesDeleted);
//	}
	
//	@Ignore
//	@Test
//	public void deleteFilesNotInDB(){
//		
//		slaPolicyService.applyRetentionPolicies();
//	}
//	
//	/**
//	 * Traverses the directories tree
//	 * @param path
//	 * @param map
//	 * @throws Exception
//	 */
//	private void traversePath(String path, Map<String,File> map) throws Exception{
//		File mainFile = (new File(path));
//		File[] content = mainFile.listFiles();
//		for (File file : content) {			
//			if(file.isDirectory()){
//				traversePath(file.getAbsolutePath(), map);
//			}else{
//				if(map.size() >= MAX_FILES_BUFFER){
//					if(!checkFiles(map)){
//						throw new Exception("Flow");
//					}
//				}
//				map.put(file.getAbsolutePath(), file);
//			}
//		}				
//	}
//	
//	/**
//	 * Checks if all the files in the current map have an equivalent entry in the Message Binaries table
//	 * @param map
//	 * @return
//	 */
//	private boolean checkFiles(Map<String,File> map){
//		Long count = 0L;
//		List<String> list = new ArrayList<String>(map.keySet());
//		if(list != null && list.size() > 0){
//			count = messageBinaryDao.getMessageBinaryCountByFilenames(list);
//		}
//		if(count == map.size()){
//			map.clear();
//			return true;
//		}
//		return false;
//	}
//	
//	/**
//	 * Deletes the files that have no reference in the Message Binary table and clears the files Map filled in during traversal
//	 * @param map
//	 */
//	private void deleteOrphanFiles(Map<String,File> map){
//		List<MessageBinary> binaries = messageBinaryDao.getMessageBinaryByFilenames(new ArrayList<String>(map.keySet()));
//		for (MessageBinary messageBinary : binaries) {
//			map.remove(messageBinary.getFileId());
//		}
//		for (String key : map.keySet()) {
//			map.get(key).delete();
//			System.err.println("!!!!FILE TO REMOVE!!!! ==>"+key);
//		}
//		map.clear();
//	}
//	
//	public void setSlaPolicyService(ISlaPolicyService slaPolicyService) {
//		this.slaPolicyService = slaPolicyService;
//	}
//
//	public ISlaPolicyService getSlaPolicyService() {
//		return slaPolicyService;
//	}
//
//
//	public IMetadataService getMetadataService() {
//		return metadataService;
//	}
//
//	public void setMetadataService(IMetadataService metadataService) {
//		this.metadataService = metadataService;
//	}
//
//	public IMessageBinaryDAO getMessageBinaryDao() {
//		return messageBinaryDao;
//	}
//
//	public void setMessageBinaryDao(IMessageBinaryDAO messageBinaryDao) {
//		this.messageBinaryDao = messageBinaryDao;
//	}
}
