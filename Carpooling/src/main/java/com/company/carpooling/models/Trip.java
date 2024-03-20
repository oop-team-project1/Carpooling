package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


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
    private int seatsAvailable;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TripStatus status;

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfCreation;

    // TODO make separate return entity
    @JsonIgnore
    @OneToMany(mappedBy = "trip", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications;


    @Formula(value = "(select count(*) from applications where applications.trip_id = trip_id and applications.status_id=2)")
    private Long countApproved;

    @Transient
    private LocalDateTime arrivalTime;

    public LocalDateTime getArrivalTime() {
        return departureTime.plusMinutes(Math.round(duration));
    }

    public boolean isFull() {
        return countApproved == seatsAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        return getId() == trip.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    //TODO set of users (accepted, pending, ...) or Map<User, UserStatus>
    /*@ManyToMany
    @JoinTable(name = "passengers",
            joinColumns = {@JoinColumn(name = "trip_id")},
            inverseJoinColumns = {@JoinColumn(name = "trip_id")})
    @MapKeyJoinColumn(@JoinTable(name = "passengers_statuses",
            joinColumns = @JoinColumn(name = "status_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id")))
    private Map<User, TripStatus> acceptedUsers;*/

    //TODO set of users (accepted, pending, ...) or Map<User, UserStatus>
    /*@ElementCollection
    @CollectionTable(name="passengers_statuses",
            joinColumns=@JoinColumn(name="status_id"))
    @MapKeyJoinColumn(name="trip_id", referencedColumnName="trip_id")
    private Map<User, Integer> users;*/

}
