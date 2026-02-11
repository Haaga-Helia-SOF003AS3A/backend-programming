package fi.haagahelia.demo.domain;

public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    
    public Student () {
    }


    public Student (String firstName, String lastName) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
    }
      
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
    public String toString() {
        return String.format(
                "Person[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

}