package se233.chapter1.model.character;
import se233.chapter1.model.character.DamageType ;
import se233.chapter1.model.item.*;


public class BasedCharacter {
    protected String name, imgpath;
    protected DamageType type;
    protected Integer fullHp, basedPow, basedDef, basedRes;
    protected Integer hp, power, defense, resistance;
    protected Weapon weapon;
    protected Armor armor;
    public String getName() {return name;}
    public String getImgpath() {return imgpath;}
    public Integer getHp() {return hp;}
    public Integer getFullHp() {return fullHp;}
    public Integer getPower() {return power;}
    public Integer getDefense() {return defense;}
    public Integer getResistance(){return resistance;}

    @Override
    public String toString(){return name;}
    public DamageType getType(){
        return type;
    }

    public boolean equipWeapon(Weapon weapon){
        if (weapon == null) {
            this.weapon = null;
            this.power = this.basedPow;
            return true;
        }

        if (this instanceof Battlemage) {
            this.weapon = weapon;
            this.power = this.basedPow + weapon.getPower();
            return true;
        } else {
            if (weapon.getDamageType() == this.type) {
                this.weapon = weapon;
                this.power = this.basedPow + weapon.getPower();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean equipArmor(Armor armor){
        if (this instanceof Battlemage) {

            return armor == null;
        }

        if (armor == null) {
            this.armor = null;
            this.defense = this.basedDef;
            this.resistance = this.basedRes;
            return true;
        }

        this.armor = armor;
        this.defense = this.basedDef + armor.getDefense();
        this.resistance = this.basedRes + armor.getResistance();
        return true;
    }
}