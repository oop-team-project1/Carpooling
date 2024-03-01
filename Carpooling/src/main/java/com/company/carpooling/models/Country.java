package com.company.carpooling.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
public class Country {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "country_id")
    int id;

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "country", orphanRemoval = true)
    Set<City> cities;
}
