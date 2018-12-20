package eu.europa.ec.etrustex.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import eu.europa.ec.etrustex.dao.dto.CreateMessageBinaryDTO;
import eu.europa.ec.etrustex.dao.dto.EncryptBinaryDTO;
import eu.europa.ec.etrustex.dao.dto.SlaValidationDTO;
import eu.europa.ec.etrustex.dao.exception.BinaryStreamLimitExceededException;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.dao.impl.MessageBinaryDAO;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.types.MessageBinaryType;

public interface IMessageBinaryDAO extends ITrustExDAO<MessageBinary,Long>{
	
	public MessageBinary createMessageBinary(CreateMessageBinaryDTO createMessageBinaryDTO) throws SQLException,BinaryStreamLimitExceededException, IOException;
	public void storeBinaryStreamToDataBase(Long binaryId, InputStream binaryStream, Boolean encrypt) throws SQLException,BinaryStreamLimitExceededException, IOException;
	/**
	 * stream bytes to the file system, stop if maxNumberOfBytesLimit or {@value MessageBinaryDAO#STREAM_MAX_NUMBER_OF_BYTES} is exceeded
	 * @param binaryStream the byte stream
	 * @param fullFilePath path to the file where the bytes should be store
	 * @param encrypt whether the bytes should be encrypted or not
	 * @param slaValidationDTO a DTO storing SLA policy data
	 * @return
	 * @throws BinaryStreamLimitExceededException when maxNumberOfBytesLimit or {@value MessageBinaryDAO#STREAM_MAX_NUMBER_OF_BYTES} is exceeded
	 */
	public EncryptBinaryDTO storeBinaryStreamToFile(InputStream binaryStream, String fullFilePath, Boolean encrypt,
			SlaValidationDTO slaValidationDTO) throws BinaryStreamLimitExceededException, MessageCreationException;
	
	public MessageBinary createMessageBinary(Long messageId,MessageBinary bin)  throws SQLException;
	public String getMessageBinaryAsString(Long messageId, MessageBinaryType type);
	public InputStream getMessageBinaryAsStream(Long messageBinaryId);
	public InputStream getDecryptedStream(InputStream encryptedStream, byte[] iv)  throws IOException;
	public void deleteMessageBinary(Long binaryId) ;
	public Long getMessageBinaryCountByFilenames(List<String> filenames);
	public List<MessageBinary> getMessageBinaryByFilenames(List<String> filenames);
	public List<MessageBinary> getMessageBinary(int startIndex, int pageSize);
	/**
	 * retrieve the binary content as input stream
	 * @param messageId
	 * @param binaryType
	 * @return
	 */
	public InputStream getMessageBinaryAsStream(Long messageId, MessageBinaryType binaryType);
	
	/**
	 * retrieve message binary by message id and type
	 * @param messageId
	 * @param binaryType
	 * @return
	 */
	public MessageBinary getMessageBinary(Long messageId, String binaryType);
	
	/**
	 * returns the file system path to the binary file
	 * @param messageId
	 * @return
	 */
	public String getFileSystemPath(Long messageId);	
}
