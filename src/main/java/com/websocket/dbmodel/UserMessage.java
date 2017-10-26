package com.websocket.dbmodel;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserMessage {

	 @Id
	 private String id;
	 private String messageContent;
	 private String messageFrom;
	 private String messageTo;
	 private String messageType;
	 private String messageReadStatus;
	 private Date messageDate;
	 
	 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getMessageFrom() {
		return messageFrom;
	}
	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}
	public String getMessageTo() {
		return messageTo;
	}
	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public Date getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}
	public String getMessageReadStatus() {
		return messageReadStatus;
	}
	public void setMessageReadStatus(String messageReadStatus) {
		this.messageReadStatus = messageReadStatus;
	}
}
