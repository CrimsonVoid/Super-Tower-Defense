// TowerObjects and EnemyObjects basically have the same properties so we can use this as a template to set the attributes of the respective object

public class TowerAttributes {
	private int hp, power, speed, attackRate, cost;
	private String type;
	
	public TowerAttributes(String typ, int health, int pow, int atkRate, int spd) {
		this(typ, health, pow, atkRate, spd, 0);
	}
	
	public TowerAttributes(String typ, int health, int pow, int atkRate, int spd, int value) {
		// Tower - health, power (of each bullet), fireRate, speed (how fast the bullet travels), cost
		// Enemy - health, power (of each attack), attackRate, speed (how fast it moves)
		setHP(health);
		setPower(pow);
		setAttackRate(atkRate);
		setSpeed(spd);
		setCost(value);
		setType(typ);
	}
	
	//* Setters and Getters *//
	
	public void setHP(int health) {
		hp = health;
	}

	public void setPower(int pow) {
		power = pow;
	}

	public void setSpeed(int spd) {
		speed = spd;
	}

	public void setAttackRate(int attack) {
		attackRate = attack;
	}
	
	public void setCost(int value) {
		cost = value;
	}
		
	public void setType(String typ) {
		type = typ;
	}

	public int getHP() {
		return hp;
	}

	public int getPower() {
		return power;
	}

	public int getSpeed() {
		return speed;
	}
	
	public int getAttackRate() {
		return attackRate;
	}

	public int getCost() {
		return cost;
	}
	
	public String getType() {
		return type;
	}
}
