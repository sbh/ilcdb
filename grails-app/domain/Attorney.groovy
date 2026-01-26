class Attorney {
    String firstName
    String lastName
    String email

    static mapping = {
        cache true
    }

    static constraints = {
        firstName(nullable: false, blank: false)
        lastName(nullable: false, blank: false)
        email(nullable: false, blank: false, email: true)
    }

    String toString() {
        return firstName
    }
}
