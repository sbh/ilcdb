class AMI {
    String label
    int level

    def toMap()
    {
        [label: label,
         level: level]
    }

    public String toString()
    {
        return label
    }
}
