package database;

public class Fighter extends Record {
	int id, age, str_acc, str_def, td_acc, td_def, w, l, d,	nc;
	String name, nickname, height, weight, reach, stance;
	double slpm, sapm, td_avg, sub_avg;

	public Fighter() {
		// TODO Auto-generated constructor stub
	}

	public Fighter(int id, int age, int str_acc, int str_def, int td_acc,
			int td_def, int w, int l, int d, int nc, String name,
			String nickname, String height, String weight, String reach,
			String stance, double slpm, double sapm, double td_avg,
			double sub_avg) {
		super();
		this.id = id;
		this.age = age;
		this.str_acc = str_acc;
		this.str_def = str_def;
		this.td_acc = td_acc;
		this.td_def = td_def;
		this.w = w;
		this.l = l;
		this.d = d;
		this.nc = nc;
		this.name = name;
		this.nickname = nickname;
		this.height = height;
		this.weight = weight;
		this.reach = reach;
		this.stance = stance;
		this.slpm = slpm;
		this.sapm = sapm;
		this.td_avg = td_avg;
		this.sub_avg = sub_avg;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getStr_acc() {
		return str_acc;
	}

	public void setStr_acc(int str_acc) {
		this.str_acc = str_acc;
	}

	public int getStr_def() {
		return str_def;
	}

	public void setStr_def(int str_def) {
		this.str_def = str_def;
	}

	public int getTd_acc() {
		return td_acc;
	}

	public void setTd_acc(int td_acc) {
		this.td_acc = td_acc;
	}

	public int getTd_def() {
		return td_def;
	}

	public void setTd_def(int td_def) {
		this.td_def = td_def;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public int getNc() {
		return nc;
	}

	public void setNc(int nc) {
		this.nc = nc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getReach() {
		return reach;
	}

	public void setReach(String reach) {
		this.reach = reach;
	}

	public String getStance() {
		return stance;
	}

	public void setStance(String stance) {
		this.stance = stance;
	}

	public double getSlpm() {
		return slpm;
	}

	public void setSlpm(double slpm) {
		this.slpm = slpm;
	}

	public double getSapm() {
		return sapm;
	}

	public void setSapm(double sapm) {
		this.sapm = sapm;
	}

	public double getTd_avg() {
		return td_avg;
	}

	public void setTd_avg(double td_avg) {
		this.td_avg = td_avg;
	}

	public double getSub_avg() {
		return sub_avg;
	}

	public void setSub_avg(double sub_avg) {
		this.sub_avg = sub_avg;
	}

	public String toSqlString () {
		return String.format("(%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, '%s', '%s', '%s', '%s', '%s', '%s', %f, %f, %f, %f)", id, age, str_acc, str_def, td_acc, td_def, w, l, d, nc, name, nickname, height, weight, reach, stance, slpm, sapm, td_avg, sub_avg);		
	}
	/*int id, int age, int str_acc, int str_def, int td_acc,
	int td_def, int w, int l, int d, int nc, String name,
	String nickname, String height, String weight, String reach,
	String stance, double slpm, double sapm, double td_avg,
	double sub_avg*/
}
