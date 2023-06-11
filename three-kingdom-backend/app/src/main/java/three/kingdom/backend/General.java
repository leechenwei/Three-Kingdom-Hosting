package three.kingdom.backend;
public class General{
    String name;
    String armyType;
    String image;
    int strength;
    int leadership;
    int intelligence;
    int politic;
    int hitPoint;

    public General(String name, String image, String armyType, int strength, int leadership, int intelligence, int politic, int hitPoint) {
        this.name = name;
        this.image = image;
        this.armyType = armyType;
        this.strength = strength;
        this.leadership = leadership;
        this.intelligence = intelligence;
        this.politic = politic;
        this.hitPoint = hitPoint;
    }

    public String getName() {
        return name;
    }

    public String getImage(){
        return image;
    }

    public String getArmyType() {
        return armyType;
    }

    public int getStrength() {
        return strength;
    }

    public int getLeadership() {
        return leadership;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getPolitic() {
        return politic;
    }

    public int getHitPoint() {
        return hitPoint;
    }
    
    @Override
    public String toString(){
        return "Name: " + name +
                " Army Type: " + armyType +
                " Strength: " + strength +
                " Leadership: " + leadership +
                " Intelligence: " + intelligence +
                " Politic: " + politic +
                " Hit Point: " + hitPoint;
    }
    
    public int getAbility(String attribute) {
        switch (attribute) {
            case "Strength":
                return getStrength();
            case "Leadership":
                return getLeadership();
            case "Intelligence":
                return getIntelligence();
            case "Political Skill":
                return getPolitic();
            case "Hit Point":
                return getHitPoint();
            default:
                throw new IllegalArgumentException("Invalid attribute.");
        }
    }

    //another method for naming purpose for backend
    public int getAttributeValue(String attribute) {
        switch (attribute) {
            case "politic":
                return politic;
            case "leadership":
                return leadership;
            case "strength":
                return strength;
            case "intelligence":
                return intelligence;
            default:
                return 0;
        }
    }
}
