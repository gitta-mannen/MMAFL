package database;

public class Rounds extends Record {
	int id,	fight_id, fighter_id, round, knockdowns, sig_str_landed, sig_str_attem,
		tot_str_attem, tot_str_landed, td_attem, td_comp, sub_attem,
		guard_passes, reversals;

	public Rounds() {
		super();
	}

	public Rounds(int id, int fight_id, int fighter_id, int round,
			int knockdowns, int sig_str_landed, int sig_str_attem,
			int tot_str_attem, int tot_str_landed, int td_attem, int td_comp,
			int sub_attem, int guard_passes, int reversals) {
		super();
		this.id = id;
		this.fight_id = fight_id;
		this.fighter_id = fighter_id;
		this.round = round;
		this.knockdowns = knockdowns;
		this.sig_str_landed = sig_str_landed;
		this.sig_str_attem = sig_str_attem;
		this.tot_str_attem = tot_str_attem;
		this.tot_str_landed = tot_str_landed;
		this.td_attem = td_attem;
		this.td_comp = td_comp;
		this.sub_attem = sub_attem;
		this.guard_passes = guard_passes;
		this.reversals = reversals;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFight_id() {
		return fight_id;
	}

	public void setFight_id(int fight_id) {
		this.fight_id = fight_id;
	}

	public int getFighter_id() {
		return fighter_id;
	}

	public void setFighter_id(int fighter_id) {
		this.fighter_id = fighter_id;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getKnockdowns() {
		return knockdowns;
	}

	public void setKnockdowns(int knockdowns) {
		this.knockdowns = knockdowns;
	}

	public int getSig_str_landed() {
		return sig_str_landed;
	}

	public void setSig_str_landed(int sig_str_landed) {
		this.sig_str_landed = sig_str_landed;
	}

	public int getSig_str_attem() {
		return sig_str_attem;
	}

	public void setSig_str_attem(int sig_str_attem) {
		this.sig_str_attem = sig_str_attem;
	}

	public int getTot_str_attem() {
		return tot_str_attem;
	}

	public void setTot_str_attem(int tot_str_attem) {
		this.tot_str_attem = tot_str_attem;
	}

	public int getTot_str_landed() {
		return tot_str_landed;
	}

	public void setTot_str_landed(int tot_str_landed) {
		this.tot_str_landed = tot_str_landed;
	}

	public int getTd_attem() {
		return td_attem;
	}

	public void setTd_attem(int td_attem) {
		this.td_attem = td_attem;
	}

	public int getTd_comp() {
		return td_comp;
	}

	public void setTd_comp(int td_comp) {
		this.td_comp = td_comp;
	}

	public int getSub_attem() {
		return sub_attem;
	}

	public void setSub_attem(int sub_attem) {
		this.sub_attem = sub_attem;
	}

	public int getGuard_passes() {
		return guard_passes;
	}

	public void setGuard_passes(int guard_passes) {
		this.guard_passes = guard_passes;
	}

	public int getReversals() {
		return reversals;
	}

	public void setReversals(int reversals) {
		this.reversals = reversals;
	}

	
}
