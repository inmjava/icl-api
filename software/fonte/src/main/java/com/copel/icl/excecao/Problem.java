package com.copel.icl.excecao;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = Include.NON_NULL)
public class Problem {

	private Integer status;

	private OffsetDateTime timestamp;

	private String type;

	private String title;
	
	private String detail;

	private String userMessage;

	@JsonProperty("validations")
	private List<Object> objects;

	public Problem(Integer status, String title, String detail, String userMessage) {
		this.status = status;
		this.title = title;
		this.timestamp = OffsetDateTime.now();
		this.detail = detail;
		this.userMessage = userMessage;
	}

	public Problem(Integer status, String title, String detail, String userMessage, List<Object> problemObjects) {
		this.status = status;
		this.title = title;
		this.timestamp = OffsetDateTime.now();
		this.detail = detail;
		this.userMessage = userMessage;
		this.objects = problemObjects;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public List<Object> getObjects() {
		return objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public static class Object {
		private String field;
		private String message;

		public Object(String field, String message) {
			this.field = field;
			this.message = message;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

}
