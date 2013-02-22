package database;

public class Event extends Record {
	
	int id;
	String name, date, location, organization, attendence;

	public Event() {
	}

	public Event(int id, String name, String date, String location,
			String organization, String attendence) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		this.location = location;
		this.organization = organization;
		this.attendence = attendence;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getAttendence() {
		return attendence;
	}

	public void setAttendence(String attendence) {
		this.attendence = attendence;
	}
	
	// Returns a string string formated to be used in a SQL-insert statement
	public String toSqlString () {
		return String.format("(%d, '%s', '%s', '%s', '%s', '%s')", id, name, date, location, organization, attendence);
		
	}

}
