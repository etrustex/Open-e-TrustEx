package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.domain.util.EmailAttachment;

public interface EMailSender {
	
	/**
	 * Simple email with only 1 recipient, body, subject and no attachments.
	 * 
	 * @param messageRecepient The email address.
	 * @param subject Subject of the email.
	 * @param body Text of the email.
	 */
	public void send(String messageRecepient, String subject, String body);
	
	/**
	 * Email with only 1 recipient, body, subject and only 1 attachment.
	 * 
	 * @param messageRecepient The email address.
	 * @param subject Subject of the email.
	 * @param body Text of the email.
	 * @param attachment The attachment of the email as EmailAttachment domain object. Null for no attachment.
	 */
	public void send(String messageRecepient, String subject, String body, EmailAttachment attachment);
	
	/**
	 * Email with multiple recipients, body, subject but only 1 attachment.
	 * 
	 * @param messageRecepients List of email addresses.
	 * @param subject Subject of the email.
	 * @param body Text of the email.
	 * @param attachment The attachment of the email as EmailAttachment domain object. Null for no attachment.
	 */
	public void send(List<String> messageRecepients, String subject, String body, EmailAttachment attachment);

	/**
	 * Email with multiple recipients, body, subject and multiple attachments.
	 * 
	 * @param messageRecepients List of email addresses.
	 * @param subject Subject of the email.
	 * @param body Text of the email.
	 * @param attachments List of EmailAttachment domain objects. Null for no attachments.
	 */
	public void send(List<String> messageRecepients, String subject, String body, List<EmailAttachment> attachments);
	
}