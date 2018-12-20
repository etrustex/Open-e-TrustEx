package eu.europa.ec.etrustex.dao.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.Security;

import eu.europa.ec.etrustex.dao.dto.SlaValidationDTO;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import eu.europa.ec.etrustex.dao.dto.EncryptBinaryDTO;
import eu.europa.ec.etrustex.dao.exception.BinaryStreamLimitExceededException;
import eu.europa.ec.etrustex.dao.impl.MessageBinaryDAO;

public class Cipher {
	public static final String SECURITY_PROVIDER = "BC";
	
	private static final transient SecureRandom secureRandom = new SecureRandom(); 

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static void main(String []p)throws Exception{
		
//		for (File f : new File("C:/test/bin").listFiles()){
//			
//			FileOutputStream out = new FileOutputStream("c:/test/dec/"+f.getName());	
//			FileInputStream in = new FileInputStream(f);
//			System.out.println(f.getName());
//			
//			InputStream indec = decryptAes(in, decodeFromBase64("XbwXQaT+LcFUXYX3Cnt6tQ8n8QKxcNRjpfZYQ9BGUdI="));
//			
//			byte[] temp = new byte[100*1024];
//			int count=0;
//			
//			while ((count=indec.read(temp))>0){
//				out.write(temp, 0, count);
//			}
//			
//			in.close();
//			indec.close();
//			out.close();
//		}		
	}

	
	public static String encodeToBase64(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	public static byte[] decodeFromBase64(String data) throws IOException {
		return Base64.decodeBase64(data);
	}

	public static InputStream decryptAes(InputStream in, byte[] key, byte[] iv){
        BufferedBlockCipher bufferedBlockCipher = buildAesBufferedBlockCipher(key, false, iv).getBufferedBlockCipher();
        // Bytes read from in will be decrypted
        return new CipherInputStream(in, bufferedBlockCipher);
    }
	
	public static EncryptBinaryDTO encryptWithAES(InputStream in, OutputStream out, byte[] key, int bufLength,
			  SlaValidationDTO slaValidationDTO) throws BinaryStreamLimitExceededException, MessageCreationException {
		
		EncryptBinaryDTO encryptBinaryDTO = buildAesBufferedBlockCipher(key, true, null);
        BufferedBlockCipher bufferedBlockCipher = encryptBinaryDTO.getBufferedBlockCipher();
      
        try {
            // Bytes written to out will be encrypted
            out = new CipherOutputStream(out, bufferedBlockCipher);

            copyFromSourceToOutput(in, out, bufLength, slaValidationDTO);
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
        return encryptBinaryDTO;
    }
	/**
	 * This metod streams to the database in "clear"
	 * 
	 * @param in
	 * @param out
	 * @param bufLength
	 * @param slaValidationDTO
	 * @throws IOException
	 * @throws BinaryStreamLimitExceededException
	 */
	public static void copyFromSourceToOutput(InputStream in, OutputStream out, int bufLength,
			  SlaValidationDTO slaValidationDTO) throws IOException, BinaryStreamLimitExceededException, MessageCreationException {
        // Read in the cleartext bytes and write to out to encryptAes
        byte[] buf = new byte[bufLength];

        int numRead = 0;
        long numberOfBytes = 0; 
        while ((numRead = in.read(buf)) >= 0){
			if ((slaValidationDTO.getSlaMaxBinarySize() != null && numberOfBytes > slaValidationDTO.getSlaMaxBinarySize())
        			|| numberOfBytes > MessageBinaryDAO.STREAM_MAX_NUMBER_OF_BYTES) {
        		out.flush();
        		out.close();
				throw new BinaryStreamLimitExceededException(
						"Read " + numberOfBytes + " bytes. Max " + MessageBinaryDAO.STREAM_MAX_NUMBER_OF_BYTES + " bytes can be submitted.");
			}
			if (slaValidationDTO.isSlaVolumeExceeded()) {
				throw new MessageCreationException(ErrorResponseCode.SLA_VOLUME_EXCEEDED.getDescription(), ErrorResponseCode.SLA_VOLUME_EXCEEDED);
			}
        	out.write(buf, 0, numRead);
        	numberOfBytes = numberOfBytes + numRead;
			slaValidationDTO.setExpendedVolume(slaValidationDTO.getExpendedVolume() + numRead);
        	if (numRead == 1) {
        		break;
        	}
        }
        out.flush();
        out.close();
    }

	public static EncryptBinaryDTO buildAesBufferedBlockCipher(byte[] key, boolean forEncryption, byte[] initVector) {
		/*
		 * A full list of BlockCiphers can be found at
		 * http://www.bouncycastle.org
		 * /docs/docs1.6/org/bouncycastle/crypto/BlockCipher.html
		 */
		BlockCipher blockCipher = new AESEngine();
		CBCBlockCipher cbcBlockCipher = new CBCBlockCipher(blockCipher);
		
		/*
		 * Paddings available
		 * (http://www.bouncycastle.org/docs/docs1.6/org/bouncycastle
		 * /crypto/paddings/BlockCipherPadding.html): - ISO10126d2Padding -
		 * ISO7816d4Padding - PKCS7Padding - TBCPadding - X923Padding -
		 * ZeroBytePadding
		 */
		BlockCipherPadding blockCipherPadding = new PKCS7Padding();

		BufferedBlockCipher bufferedBlockCipher = new PaddedBufferedBlockCipher(
				cbcBlockCipher, blockCipherPadding);
		CipherParameters cipherParameters = null;
		if (forEncryption || initVector != null) {			
			if (initVector == null) {
				//generate IV for encryption
				initVector = new byte[bufferedBlockCipher.getBlockSize()];
				secureRandom.nextBytes(initVector);
			} else {
				//nothing, IV was read from the DB for decryption
			}
			cipherParameters = new ParametersWithIV(new KeyParameter(key), initVector);			
		} else {
			//this is for decrypting files that were encrypted w/o IV
			cipherParameters = new KeyParameter(key);
		} 
		bufferedBlockCipher.init(forEncryption, cipherParameters);
		return new EncryptBinaryDTO(bufferedBlockCipher, initVector);
	}
	
//	public static byte[] genAes256Key() throws NoSuchProviderException, NoSuchAlgorithmException {
//        KeyGenerator kgen = KeyGenerator.getInstance("AES", SECURITY_PROVIDER);
//        kgen.init(256); // 192 and 256 bits may not be available
//
//        // Generate the secret key specs.
//        SecretKey skey = kgen.generateKey();
//        return skey.getEncoded();
//    }	
}