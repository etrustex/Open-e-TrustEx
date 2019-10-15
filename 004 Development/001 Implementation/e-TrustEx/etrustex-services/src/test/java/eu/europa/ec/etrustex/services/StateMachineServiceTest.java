package eu.europa.ec.etrustex.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.exception.StateMachineException;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.util.StateMachine;

public class StateMachineServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IStateMachineService stateMachineService;
	
	private Document document;
	private Document document2;
	
	@BeforeClass public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("StateMachineService");
		System.out.println("--------------------------------------------------");
	}
	
	@Test public void testGetNextStateHappyFlow() {
		document  = buildDocument("myLN-01","statemachine/sm1.txt");
		try {
			Assert.assertEquals("APPROVED", stateMachineService.getNextState(document, "RECEIVED", "1", null));
			Assert.assertEquals("REJECTED", stateMachineService.getNextState(document, "RECEIVED", "2", null));			
		} catch (StateMachineException e) {
			Assert.fail();
		}
	}
	
	@Test public void testGetNextStateExceptions() {
		document  = buildDocument("myLN-01","statemachine/sm1.txt");
		try {
			stateMachineService.getNextState(document, "RECEIVED", "7");			
			Assert.fail("Should throw an SM exception: source state [RECEIVED] has no transition to any state via event [7]");
		} catch (StateMachineException e) {}
		
		try {
			stateMachineService.getNextState(document, "APPROVED", "7");			
			Assert.fail("Should throw a SM exception: source state [APPROVED] has no transition to any state via event [7]");
		} catch (StateMachineException e) {}
		
		try {
			stateMachineService.getNextState(document, "PROCESSED", "7");			
			Assert.fail("Should throw a SM exception: source state [PROCESSED] not found in state machine");
		} catch (StateMachineException e) {}
	}
	
	@Test public void testGetNextStateWrongFile() {
		document2 = buildDocument("myLN-02","statemachine/sm2.txt");
		try {
			stateMachineService.getNextState(document2, "RECEIVED", "7");			
			Assert.fail("Should throw an SM exception: Premature end of file.");
		} catch (StateMachineException e) {
			
		}
	}
	
	@Test public void testConfigExceptionNoSM() {
		try {
			stateMachineService.getNextState(new Document(), "RECEIVED", "1");
			Assert.fail("Config Exception");
		} catch (StateMachineException e) {}		
	}
	
	@Test public void testConfigExceptionNoSMDefinistion() {
		try {
			Document d = new Document();
			d.setStateMachine(new StateMachine());
			stateMachineService.getNextState(d, "RECEIVED", "1", null);
			Assert.fail("Config Exception");
		} catch (Exception e) {}		
	}
	
	@Test public void testModelConfigException() {
		try {
			document  = buildDocument("myLN-03","statemachine/sm3.txt");		
			stateMachineService.getNextState(document, "RECEIVED", "1");
			Assert.fail("Model Config Exception: No final Stage for the state");
		} catch (StateMachineException e) {}		
	}
	
	private Document buildDocument(String namespace, String filePath){
		Document document = new Document();
		document.setLocalName(namespace);
		document.setStateMachine(new StateMachine());
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
		String sm1 = null;
		try {
			sm1 = convertStreamToString(inputStream);
		} catch (IOException e1) {
			Assert.fail("No sm file found");
		}				
		document.getStateMachine().setDefinition(sm1);
		
		return document;
	}
	
	private String convertStreamToString(InputStream is) throws IOException {
	    if (is != null) {
	        Writer writer = new StringWriter();

	        char[] buffer = new char[1024];
	        try {
	            Reader reader = new BufferedReader(
	                    new InputStreamReader(is, "UTF-8"));
	            int n;
	            while ((n = reader.read(buffer)) != -1) {
	                writer.write(buffer, 0, n);
	            }
	        } finally {
	            is.close();
	        }
	        return writer.toString();
	    } else {        
	        return "";
	    }
	}
	
}
