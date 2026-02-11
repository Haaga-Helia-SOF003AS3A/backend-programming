package fi.haagahelia.course.domain;

public class Message {
	private Long id;
	private String text;
	
	public Message(Long id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	
	public Message() {
		super();
		this.id = null;
		this.text = null;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
