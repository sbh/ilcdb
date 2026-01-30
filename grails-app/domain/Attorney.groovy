class Attorney {
    public static final Long UNASSIGNED_ATTORNEY_ID = 0L

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
