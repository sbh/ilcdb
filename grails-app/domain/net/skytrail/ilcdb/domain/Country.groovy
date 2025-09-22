package net.skytrail.ilcdb.domain;
import gorm.transform.Entity


@Entity
class Country @Entity {
    String name;

    public String toString()
    {
        return name;
    }
}
