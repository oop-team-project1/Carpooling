package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", updatable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_enabled")
    private boolean isVerified;


    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "profile_pic")
    private UserProfilePic profilePic;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Application> applications;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private Set<Feedback> feedbacks;

    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Trip> tripsAsDriver;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
