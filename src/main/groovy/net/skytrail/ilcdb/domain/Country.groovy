package net.skytrail.ilcdb.domain;

import gorm.transform.Entity

@Entity
class Country {
    String name;

    public String toString()
    {
        return name;
    }
}
