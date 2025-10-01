package se233.chapter1.model.item;

import se233.chapter1.model.character.DamageType;

public class Weapon extends BasedEquipment {
    private int power ;
    private DamageType damageType ;
    public Weapon(String name, int power, DamageType damageType, String impath) {
        this.name = name;
        this.impath = impath;
        this.power = power;
        this.damageType = damageType;
    }
    public int getPower() { return power ; }
    public void setPower(int power) {
        this.power = power ;
    }
    public DamageType getDamageType() {
        return damageType;
    }
    public void setDamageType(DamageType damageType) {
        this.damageType = damageType ;
    }
    @Override
    public String toString() {
        return name ;
    }
}
