public class TowerAttributes {
	private int hp, power, speed, attackRate;
	private String type;

	public TowerAttributes(int health, int pow, int spd, String typ) {
		hp = health;
		power = pow;
		speed = spd;
		type = typ;
	}
	
	public TowerAttributes(int health, int pow, int spd, int attack, String typ) {
		hp = health;
		power = pow;
		speed = spd;
		attackRate = attack;
		type = typ;
	}

	public void setHP(int health) {
		hp = health;
	}

	public void setPow(int pow) {
		power = pow;
	}

	public void setSpeed(int spd) {
		speed = spd;
	}

	public void setAttackRate(int attack) {
		attackRate = attack;
	}
	
	public void setType(String typ) {
		type = typ;
	}

	public int getHP() {
		return hp;
	}

	public int getPow() {
		return power;
	}

	public int getSpeed() {
		return speed;
	}
	
	public int getAttackRate() {
		return attackRate;
	}

	public String getType() {
		return type;
	}
}
