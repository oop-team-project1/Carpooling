package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "cities")
public class City {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "city_id")
    int id;

    @Column(name = "name")
    String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "country_id")
    @JsonIgnoreProperties("id")
    Country country;

}
