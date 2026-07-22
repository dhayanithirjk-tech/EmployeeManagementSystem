package model;

/**
 * Abstract base class representing a generic person.
 * <p>
 * Demonstrates ABSTRACTION and INHERITANCE: common personal attributes
 * live here; concrete subclasses (e.g. {@link Employee}) extend this
 * with role-specific behaviour. The abstract {@link #getDetails()}
 * method demonstrates POLYMORPHISM - each subclass provides its own
 * implementation.
 */
public abstract class Person {

    // ENCAPSULATION: fields are private, exposed only via getters/setters.
    private String fullName;
    private int age;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;

    protected Person(String fullName, int age, String gender, String email,
                      String phoneNumber, String address) {
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    /** Abstract method - forces every subclass to define its own summary. Polymorphism. */
    public abstract String getDetails();

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
