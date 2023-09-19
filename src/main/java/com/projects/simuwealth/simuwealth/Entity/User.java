package com.projects.simuwealth.simuwealth.Entity;

import com.projects.simuwealth.simuwealth.CustomAnnotations.EmailExists;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Arrays;

@Entity
@Table(name= "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId; // New primary key

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

    @Column(name = "profilePicture")
    private String profilePicture;

    @Column(name = "capitol")
    private double capitol;

    public User() {
        // blank constructor as required by JPA/Hibernate
    }

    public User(int userId, String email, String password, String firstName, String lastName, String profilePicture, double capitol) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.capitol = capitol;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public double getCapitol() {
        return capitol;
    }

    public void setCapitol(double capitol) {
        this.capitol = capitol;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", capitol=" + capitol +
                '}';
    }
}
