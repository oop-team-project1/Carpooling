package com.company.carpooling.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "activation_codes")
@Getter
@Setter
public class ActivationCode {
    @Id
    @Column(name = "code")
    int activationCode;

    @Column(name = "username")
    String username;
}
