package database;

public class Fight extends Record {
	int id, round;
	String event, weight_class, method, details, time, time_format,	referee, title_fight, fotn,	kootn, sotn;
	
	public Fight() {
		super();
	}

	public Fight(int id, int round, String event, String weight_class,
			String method, String details, String time, String time_format,
			String referee, String title_fight, String fotn, String kootn,
			String sotn) {
		super();
		this.id = id;
		this.round = round;
		this.event = event;
		this.weight_class = weight_class;
		this.method = method;
		this.details = details;
		this.time = time;
		this.time_format = time_format;
		this.referee = referee;
		this.title_fight = title_fight;
		this.fotn = fotn;
		this.kootn = kootn;
		this.sotn = sotn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getWeight_class() {
		return weight_class;
	}

	public void setWeight_class(String weight_class) {
		this.weight_class = weight_class;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime_format() {
		return time_format;
	}

	public void setTime_format(String time_format) {
		this.time_format = time_format;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public String getTitle_fight() {
		return title_fight;
	}

	public void setTitle_fight(String title_fight) {
		this.title_fight = title_fight;
	}

	public String getFotn() {
		return fotn;
	}

	public void setFotn(String fotn) {
		this.fotn = fotn;
	}

	public String getKootn() {
		return kootn;
	}

	public void setKootn(String kootn) {
		this.kootn = kootn;
	}

	public String getSotn() {
		return sotn;
	}

	public void setSotn(String sotn) {
		this.sotn = sotn;
	}

	
}
