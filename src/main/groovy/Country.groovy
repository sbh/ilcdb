import grails.gorm.Entity;

class Country implements Entity {
    String name;
    
    public String toString()
    {
        return name;
    }
}
