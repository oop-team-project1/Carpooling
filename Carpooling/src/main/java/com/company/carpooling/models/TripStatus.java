package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "status_trips")
public class TripStatus {

    @Id
    @JsonIgnore
    @Column(name = "status_id")
    private int id;

    @Column(name = "status")
    private String statusName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TripStatus that = (TripStatus) o;

        return getStatusName() != null ? getStatusName().equals(that.getStatusName()) : that.getStatusName() == null;
    }

    @Override
    public int hashCode() {
        return getStatusName() != null ? getStatusName().hashCode() : 0;
    }
}
