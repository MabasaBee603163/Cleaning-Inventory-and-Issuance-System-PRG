package za.ac.belgiumcampus.cleaninginventory.model;

public class Cleaner {
    private long cleanerId;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String department;

    public Cleaner() {}

    public Cleaner(long cleanerId, String firstName, String lastName, String phone, String email, String department) {
        this.cleanerId = cleanerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.department = department;
    }

    //Getters

    public long getCleanerId() {
        return cleanerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    //Setters
    public void setCleanerId(long cleanerId) {
        this.cleanerId = cleanerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    //tostring
    @Override
    public String toString() {
        return "Cleaner{" +
            "cleanerId=" + cleanerId +
            ", firstName='" + firstName + '\'' +    
            ", lastName='" + lastName + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", department='" + department + '\'' +
            '}';    
    }
}       
