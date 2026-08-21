package fi.haagahelia.course.domain;


public class Message {

	// attributes
	private Long id;
	private String msg;

	// constructors
	public Message() {
		this.id = null;
		this.msg = null;
	}
	public Message(Long id, String msg) {
		this.id = id;
		this.msg = msg;
	}
	
	// setters
	public void setId(Long id) {
		this.id = id;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// getters
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
