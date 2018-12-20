package eu.europa.ec.etrustex.domain.util;

import java.util.List;

import eu.europa.ec.etrustex.domain.Message;

public class MessagesListVO {
	
	private List<Message> messages;
	private Integer size;
	
	public MessagesListVO(List<Message> messages, Integer size){
		this.messages = messages;
		this.size = size;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	
}
