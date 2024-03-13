package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "status_passengers")
public class PassengerStatus {

    @Id
    @JsonIgnore
    @Column(name = "status_id")
    private int id;

    @Column
    private String status;

}
