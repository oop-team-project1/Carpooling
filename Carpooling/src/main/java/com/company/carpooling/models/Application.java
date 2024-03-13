package com.company.carpooling.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private PassengerStatus status;

    public Application(Trip trip, User user) {
        this.trip = trip;
        this.user = user;
        this.status = new PassengerStatus(1,"Pending");

    }


}
