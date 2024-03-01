package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="streets")
public class Street {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "street_id")
    int id;

    @Column(name = "street_name")
    String streetName;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    City city;



}
