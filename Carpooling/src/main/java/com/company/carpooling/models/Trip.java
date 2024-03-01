package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "trips")
@Getter
@Setter
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonManagedReference
    @JsonIgnoreProperties({"id", "firstName", "lastName", "email", "phoneNumber", "profilePic", "admin", "blocked"})
    private User driver;

    @ManyToOne
    @JoinColumn(name = "start_point")
    @JsonManagedReference
    @JsonIgnoreProperties("id")
    private Street startPoint;

    @ManyToOne
    @JoinColumn(name = "end_point")
    @JsonManagedReference
    @JsonIgnoreProperties("id")
    private Street endPoint;

    @Column(name = "departure_time")
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime departureTime;

    @Column(name = "distance")
    private float distance;

    @Column(name = "duration")
    private float duration;

    @Column(name = "passengers_count")
    private int passengersCount;

    /*@JoinColumn(name = "status_id")
    private TripStatus status;*/

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfCreation;

    //TODO set of users (accepted, pending, ...) or Map<User, UserStatus>
    /*@ManyToMany
    @JoinTable(name = "passengers",
            joinColumns = {@JoinColumn(name = "trip_id")},
            inverseJoinColumns = {@JoinColumn(name = "trip_id")})
    @MapKeyJoinColumn(@JoinTable(name = "passengers_statuses",
            joinColumns = @JoinColumn(name = "status_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id")))
    private Map<User, TripStatus> acceptedUsers;*/

}
