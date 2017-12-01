package eu.europa.ec.cipa.etrustex.services.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.services.dto.CreateMessageBinaryDTO;
import eu.europa.ec.cipa.etrustex.services.dto.EncryptBinaryDTO;
import eu.europa.ec.cipa.etrustex.services.exception.BinaryStreamLimitExceededException;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;

public interface IMessageBinaryDAO extends ITrustExDAO<MessageBinary,Long>{
	
	public MessageBinary createMessageBinary(CreateMessageBinaryDTO createMessageBinaryDTO) throws SQLException,BinaryStreamLimitExceededException, IOException;
	public void storeBinaryStreamToDataBase(Long binaryId, InputStream binaryStream, Boolean encrypt) throws SQLException,BinaryStreamLimitExceededException, IOException;
	public EncryptBinaryDTO storeBinaryStreamToFile(InputStream binaryStream,String fullFilePath,  Boolean encrypt, boolean enforceMaxNumberOfBytes) throws SQLException,BinaryStreamLimitExceededException, IOException;
	//public void linkBinaryToMessage(Long messageId,Long binaryId);
	public MessageBinary createMessageBinary(Long messageId,MessageBinary bin)  throws SQLException;
	public String getMessageBinaryAsString(Long messageId, MessageBinaryType type);
	public InputStream getMessageBinaryAsStream(Long messageBinaryId);
	public InputStream getDecryptedStream(InputStream encryptedStream, byte[] iv)  throws IOException;
	public void deleteMessageBinary(Long binaryId) ;
	public Long getMessageBinaryCountByFilenames(List<String> filenames);
	public List<MessageBinary> getMessageBinaryByFilenames(List<String> filenames);
	public List<MessageBinary> getMessageBinary(int startIndex, int pageSize);
}
