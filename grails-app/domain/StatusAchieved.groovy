class StatusAchieved
 {
     static belongsTo = [ client:Client ]

     enum Type { Citizenship, LPR, DACA, TPS }
     
     Type type
     Date date

    static constraints =
    {
    }
}
