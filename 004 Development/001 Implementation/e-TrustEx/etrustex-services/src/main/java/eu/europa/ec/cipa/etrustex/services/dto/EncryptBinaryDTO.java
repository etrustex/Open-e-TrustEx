package eu.europa.ec.cipa.etrustex.services.dto;

import org.bouncycastle.crypto.BufferedBlockCipher;

public class EncryptBinaryDTO {
	
	private BufferedBlockCipher bufferedBlockCipher;
	//the initialization vector sent to the cipher
	private byte[] iv;
	private long binarySize;
	
	public EncryptBinaryDTO(BufferedBlockCipher bufferedBlockCipher, byte[] iv) {
		this.iv = iv;
		this.bufferedBlockCipher = bufferedBlockCipher;
	}
	
	public EncryptBinaryDTO() {
	}

	public long getBinarySize() {
		return binarySize;
	}

	public void setBinarySize(long binarySize) {
		this.binarySize = binarySize;
	}

	public BufferedBlockCipher getBufferedBlockCipher() {
		return bufferedBlockCipher;
	}

	public byte[] getIv() {
		return iv;
	}
	
	

}
