class Attorney {
    public static final String UNASSIGNED = "Unassigend"

    String firstName
    String lastName
    String email
    Boolean isActive = false

    static mapping = {
        cache true
    }

    static constraints = {
        firstName(nullable: false, blank: false)
        lastName(nullable: true)
        email(nullable: true, email: true)
        isActive(nullable: false)
    }

    String toString() {
        return firstName
    }
}
