package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "streets")
public class Street {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "street_id")
    private int id;

    @Column(name = "street_name")
    private String streetName;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    @JsonIgnoreProperties("id")
    private City city;

}
