package fi.haagahelia.course.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Message {
	
	// attributes
	@NotNull
	private Long id;
	
    @Size(min=5, max=30)
	private String msg;
	
	// constructors
	public Message(Long id, String msg) {
		this.id = id;
		this.msg = msg;
	}
	public Message() {
		this.id = null;
		this.msg = null;
	}

	// setterit
	public void setId(Long id) {
		this.id = id;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	// getterit
	public Long getId() {
		return id;
	}
	public String getMsg() {
		return msg;
	}
	
	
	@Override
	public String toString() {
		return "Message [msg=" + msg + "]";
	}
}
