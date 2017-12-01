package eu.europa.ec.cipa.etrustex.services;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.services.IMessageService;

@ContextConfiguration(locations={"/etrustex-services-test-context.xml"})
public class MessageServiceTest extends AbstractJUnit4SpringContextTests{
	
	
	@Autowired
	private IMessageService messageService;
	
	public IMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	@Test
	public void testRetrieveMessages() throws Exception{
		long start = System.currentTimeMillis();
		List<Message> queryResult = messageService.retrieveMessages(null,new Long(4),null,null,null,null,null,null,true,false,true,true);
//		for (Message message : queryResult) {
//			System.out.println(message.getDocumentId());
//			Set<MessageBinary> bin=  message.getBinaries();
//			for (MessageBinary messageBinary : bin) {
//				System.out.println(messageBinary.getBinaryType());
//			}
//		}
		System.out.println(System.currentTimeMillis()-start);
		start = System.currentTimeMillis();
		queryResult = messageService.retrieveMessages(null,new Long(4),null,null,null,null,null,null,true,false,true,true);	
		System.out.println(System.currentTimeMillis()-start);	
	}

}
