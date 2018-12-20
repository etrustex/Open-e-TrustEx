package eu.europa.ec.etrustex.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import eu.europa.ec.etrustex.dao.dto.SlaValidationDTO;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IMessageBinaryDAO;
import eu.europa.ec.etrustex.dao.dto.CreateMessageBinaryDTO;
import eu.europa.ec.etrustex.dao.dto.EncryptBinaryDTO;
import eu.europa.ec.etrustex.dao.exception.BinaryStreamLimitExceededException;
import eu.europa.ec.etrustex.dao.util.Cipher;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.types.MessageBinaryType;

@Repository
public class MessageBinaryDAO extends TrustExDAO<MessageBinary, Long> implements
		IMessageBinaryDAO {
	

	
	@Autowired
	private DataSource eTrustExDS;
	
	private String retrieveBinaryQuery;

	private String retrieveBinaryStreamQuery;
	
	private String encryptionKey;
	
	private String keySize;	
	
	private Integer streamBufferSize = 131072;

	public static final Long STREAM_MAX_NUMBER_OF_BYTES = 105906176L;

	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public MessageBinary createMessageBinary(CreateMessageBinaryDTO createMessageBinaryDTO)
			throws SQLException, BinaryStreamLimitExceededException,
			IOException {
		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationDate(Calendar.getInstance().getTime());
		eai.setCreationId(createMessageBinaryDTO.getCreatorID());
		eai.setModificationDate(Calendar.getInstance().getTime());
		eai.setModificationId(createMessageBinaryDTO.getCreatorID());
		MessageBinary bin = new MessageBinary();
		bin.setAccessInfo(eai);
		bin.setBinaryType(createMessageBinaryDTO.getBinaryType());
		bin.setMimeCode(createMessageBinaryDTO.getMimeType());
		bin.setFileId(createMessageBinaryDTO.getFullFilePath() != null ? createMessageBinaryDTO.getFullFilePath().replace('\\', '/'):createMessageBinaryDTO.getFullFilePath());
		bin.setSize(createMessageBinaryDTO.getExpectedSize());
		bin.setIv(createMessageBinaryDTO.getIv());
		entityManager.persist(bin);
		return bin;
	}
	
	@Override
	public String getMessageBinaryAsString(Long messageId,
			MessageBinaryType type) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = eTrustExDS.getConnection();
			stmt = conn.prepareStatement(retrieveBinaryQuery);
			stmt.setLong(1, messageId);
			stmt.setString(2, type.name());
			rs = stmt.executeQuery();
			if (rs.next()) {
				Blob orig = rs.getBlob(1);
				if (orig != null) {
					Long blobLength = new Long(orig.length());
					byte[] bytes = orig.getBytes(1, blobLength.intValue());
					return new String(bytes, "UTF-8");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return null;
	}
	
	@Override
	public MessageBinary createMessageBinary(Long messageId, MessageBinary bin)
			throws SQLException {
		try {
			Message m = new Message();
			m.setId(messageId);
			MessageBinary newInstance = create(bin);
			if (bin.getBinaryIs() != null) {
				storeBinaryStreamToDataBase(newInstance.getId(), bin.getBinaryIs(), false);
			} else {
				storeBinaryStreamToDataBase(newInstance.getId(), new ByteArrayInputStream(bin.getValue().getValue()), false);
			}
			return newInstance;
		} catch (Exception e) {
				throw new RuntimeException(e);
		}
	
	}
	
	@Override
	public InputStream getDecryptedStream(InputStream encryptedStream, byte[] iv) throws IOException {
		
		return Cipher.decryptAes(encryptedStream, Cipher.decodeFromBase64(encryptionKey), iv);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void storeBinaryStreamToDataBase(Long binaryId, InputStream binaryStream, Boolean encrypt) 
			throws SQLException, BinaryStreamLimitExceededException, IOException {
		byte[] binary = null;
		if (encrypt){
			try (CipherInputStream is = new CipherInputStream(binaryStream,
					Cipher.buildAesBufferedBlockCipher(
							Cipher.decodeFromBase64(encryptionKey), true, null).getBufferedBlockCipher())) {
				binary = extract(is);
			}				
		}else{
			binary = extract(binaryStream);
		}
		
		MessageBinary toUpdate = read(binaryId);
		toUpdate.setBinary(binary);
		toUpdate.setSize((long) binary.length);
		update(toUpdate);		
	}
	
	private byte[] extract(InputStream inputStream) throws IOException, BinaryStreamLimitExceededException {	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();				
		byte[] buffer = new byte[1024];
		int read = 0;
		int count = 0; 
		long max = STREAM_MAX_NUMBER_OF_BYTES / 1024;
		while (((read = inputStream.read(buffer, 0, buffer.length)) != -1) && count++ < max) {
			baos.write(buffer, 0, read);
			if (read == 1) {
				break;
			}
		}
		baos.flush();		
		if (count>max){
			throw new BinaryStreamLimitExceededException("The file sent is too big");
		}
		return  baos.toByteArray();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteMessageBinary(Long binaryId)
			 {
		MessageBinary todelete = read(binaryId);
		if (todelete.getFileId()!= null){
			File f= new File(todelete.getFileId());
			if (f.exists()){
				f.delete();
			}
		}
		delete(todelete);

	}

	@Override
	public EncryptBinaryDTO storeBinaryStreamToFile(InputStream pBinaryStream,String fileNameWithFullPath, Boolean encrypt,
													SlaValidationDTO slaValidationDTO) throws BinaryStreamLimitExceededException, MessageCreationException {
		
		try{
			File file = new File(fileNameWithFullPath);
			File folder=file.getParentFile();
			if (!folder.exists()){
				folder.mkdir();
			}
			if (!file.exists()){
				file.createNewFile();
			}
			try (FileOutputStream fos = new FileOutputStream(file)) {
				EncryptBinaryDTO encryptBinaryDTO = null;
				try (CountingInputStream binaryStream = new CountingInputStream(pBinaryStream)) {
					if (encrypt){
						encryptBinaryDTO = Cipher.encryptWithAES(binaryStream, fos, Cipher.decodeFromBase64(encryptionKey),streamBufferSize, slaValidationDTO);
					}else{
						Cipher.copyFromSourceToOutput(binaryStream,fos,streamBufferSize, slaValidationDTO);
						encryptBinaryDTO = new EncryptBinaryDTO();
					}
					encryptBinaryDTO.setBinarySize(binaryStream.getTotalBytes());
				}			
				return encryptBinaryDTO;
			}	
		} catch(IOException e){
			throw new RuntimeException(e);
		}	
	}
	
	@Override
	public MessageBinary getMessageBinary(Long messageId, String binaryType) {
		List<MessageBinary> binaries = entityManager.createQuery("from MessageBinary bin where bin.message.id = :messageId and bin.binaryType = :binaryType", MessageBinary.class)
				.setParameter("messageId", messageId)
				.setParameter("binaryType", binaryType)
				.getResultList();		
		return CollectionUtils.isNotEmpty(binaries) ? binaries.get(0) : null;
	}
	
	@Override
	public InputStream getMessageBinaryAsStream(Long messageId, MessageBinaryType binaryType) {
		MessageBinary binary = getMessageBinary(messageId, binaryType.name());
		return getMessageBinaryAsStream(binary.getId());
	}	

	@Override
	public InputStream getMessageBinaryAsStream(Long messageBinaryId)
			{
			ResultSet rs =null;
			PreparedStatement stmt=null;
			Connection conn=null;
			try {
				conn = eTrustExDS.getConnection();
				stmt = conn.prepareStatement(retrieveBinaryStreamQuery);
				stmt.setLong(1,messageBinaryId);
				 rs = stmt.executeQuery();
				if (rs.next()){
					return rs.getBinaryStream(1);
				}
			}catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}
			return null;
	}
	
	public Long getMessageBinaryCountByFilenames(List<String> filenames){
		return entityManager.createQuery(
						"SELECT COUNT (msgB.id) FROM MessageBinary msgB WHERE msgB.fileId IN :filenames",
						Long.class).setParameter("filenames", filenames).getSingleResult();
	}
	
	public List<MessageBinary> getMessageBinaryByFilenames(List<String> filenames){
		return entityManager.createQuery(
						"FROM MessageBinary msgB WHERE msgB.fileId IN :filenames",
						MessageBinary.class).setParameter("filenames", filenames).getResultList();
	}
	
	public List<MessageBinary> getMessageBinary(int startIndex, int pageSize){
		return entityManager.createQuery(
				"FROM MessageBinary msgB WHERE msgB.fileId is not null",
				MessageBinary.class).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
	}
	
	public String getFileSystemPath(Long messageId) {
		List<MessageBinary> binaries = entityManager.createQuery(
				"FROM MessageBinary bin WHERE bin.message.id = :messageId and bin.fileId is not null",
				MessageBinary.class).setParameter("messageId", messageId).getResultList();
		return CollectionUtils.isNotEmpty(binaries) ? binaries.get(0).getFileId() : null;
	}
	
	
	public String getRetrieveBinaryStreamQuery() {
		return retrieveBinaryStreamQuery;
	}

	public void setRetrieveBinaryStreamQuery(String retrieveBinaryStreamQuery) {
		this.retrieveBinaryStreamQuery = retrieveBinaryStreamQuery;
	}

	public DataSource geteTrustExDS() {
		return eTrustExDS;
	}
	public void seteTrustExDS(DataSource eTrustExDS) {
		this.eTrustExDS = eTrustExDS;
	}
	public String getRetrieveBinaryQuery() {
		return retrieveBinaryQuery;
	}
	public void setRetrieveBinaryQuery(String retrieveBinaryQuery) {
		this.retrieveBinaryQuery = retrieveBinaryQuery;
	}
	public String getEncryptionKey() {
		return encryptionKey;
	}
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	public Integer getStreamBufferSize() {
		return streamBufferSize;
	}
	public void setStreamBufferSize(Integer streamBufferSize) {
		this.streamBufferSize = streamBufferSize;
	}

	public String getKeySize() {
		return keySize;
	}

	public void setKeySize(String keySize) {
		this.keySize = keySize;
	}
	
	private static class CountingInputStream extends InputStream{
		
		private InputStream source;
		private long totalBytes;
				
		public long getTotalBytes() {
			return totalBytes;
		}

		public CountingInputStream(final InputStream source){
			this.source=source;
			totalBytes = 0;
		}
		
		@Override
		public int	available() throws IOException{
			return source.available();
		}
		
		@Override
		public void	close() throws IOException{
			source.close();
		}
		
		@Override
		public void	mark(int readlimit){
			//do nothing
		}
		
		@Override
		public boolean	markSupported(){
			return false;
		}
		
		
		@Override
		public int	read(byte[] b) throws IOException{
			int count = source.read(b);
			if (count>0)
			totalBytes+=count;
			return count;
		}
		
		
		@Override
		public int	read(byte[] b, int off, int len) throws IOException{
			int count = source.read(b,off,len);
			if (count>0)
				totalBytes+=count;
			return count;
		}
		
		@Override
		public void	reset() throws IOException{
			//do nothing
		}
		
		@Override
		public long	skip(long n) throws IOException{
			long count = source.skip(n);
			if (count>0)
				totalBytes += count;
			return count;
		}		
		
		@Override
		public int read() throws IOException {
			int count = source.read();
			if (count>0)
				totalBytes+=count;			
			return count;			
		}
	}
}
