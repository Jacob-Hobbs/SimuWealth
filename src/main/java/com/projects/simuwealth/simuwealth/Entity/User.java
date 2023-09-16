package com.projects.simuwealth.simuwealth.Entity;

import com.projects.simuwealth.simuwealth.CustomAnnotations.EmailExists;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name= "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))

public class User {

    @Id
    @Column(name = "email")
    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    @EmailExists
    private String email;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Column(name = "firstName")
    @NotBlank
    private String firstName;

    @Column(name = "lastName")
    @NotBlank
    private String lastName;

    public User() {
        // blank constructor as required by JPA/Hibernate
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
