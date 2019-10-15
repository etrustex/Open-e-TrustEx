/**
 *
 */
package eu.europa.ec.etrustex.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IMessageRoutingDAO;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.etrustex.services.IMessageRoutingService;
import eu.europa.ec.etrustex.services.util.EncryptionService;

/**
 * @author chiricr
 *
 */
@Service
public class MessageRoutingService implements IMessageRoutingService {

	private Logger logger = LoggerFactory.getLogger(MessageRoutingService.class);

	@Autowired
	private IMessageRoutingDAO messageRoutingDAO;

	@Autowired
	private EncryptionService encryptionService;

	@Override
	@Transactional
	public MessageRouting save(MessageRouting messageRouting) {
		return messageRoutingDAO.create(messageRouting);
	}

	@Override
	@Transactional
	public MessageRouting markAsProcessed(Long id) {
		MessageRouting messageRouting = messageRoutingDAO.read(id);
		messageRouting.setProcessed(Boolean.TRUE);
		messageRouting.getAccessInfo().setModificationDate(new Date());
		messageRouting.getAccessInfo().setModificationId("TRUSTEX");
		return messageRoutingDAO.update(messageRouting);
	}

	/**
	 * Decrypts the password upon retrieval
	 */
	@Override
	@Transactional(readOnly = true)
	public MessageRouting findById(Long id) {
		MessageRouting mr = messageRoutingDAO.read(id);
		if(mr != null && mr.getMessage() != null){
			Hibernate.initialize(mr.getMessage().getParentMessages());
		}

		if(mr.getEndpoint() != null){
			if(mr.getEndpoint().getCredentials() != null){
				String pwd = resolvePassword(mr.getEndpoint().getCredentials());
				mr.getEndpoint().getCredentials().setDecryptedPassword(pwd);
			}
			if(mr.getEndpoint().getProxyCredential() != null){
				String pwd = resolvePassword(mr.getEndpoint().getProxyCredential());
				mr.getEndpoint().getProxyCredential().setDecryptedPassword(pwd);
			}
		}

		return mr;
	}

	/**
	 *
	 * @param cred
	 * @return password in clear if IV is null or the decrypted password
	 */
	public String resolvePassword(Credentials cred){
		String password = cred.getPassword();
		if (cred.getIv() != null){
			try {
				password = encryptionService.decryptMessageInAES(password, cred.getIv());
			} catch (UnrecoverableKeyException | InvalidKeyException | KeyStoreException | NoSuchAlgorithmException
					| IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidAlgorithmParameterException | IOException e) {
				logger.error(e.getMessage());
			}
		}
		return password;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageRouting> findByMessageId(Long messageId) {
		return messageRoutingDAO.findByMessageId(messageId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageRouting> findByEndpointId(Long endpointId) {
		return messageRoutingDAO.findByEndpointId(endpointId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageRouting> findByEndpointIdAndNotDispatched(Long endpointId) {
		return messageRoutingDAO.findByEndpointIdAndNotDispatched(endpointId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageRouting> findByEndpointPartyId(Long partyId) {
		return messageRoutingDAO.findByEndpointPartyId(partyId);
	}

	@Override
	@Transactional
	public void deleteByMessageId (Long messageId) {
		for (MessageRouting messageRouting : messageRoutingDAO.findByMessageId(messageId)) {
			messageRoutingDAO.delete(messageRouting);
		}
	}
}
