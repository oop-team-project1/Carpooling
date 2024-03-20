package com.company.carpooling.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_profile_pics")
@Getter
@Setter
public class UserProfilePic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pic_id")
    private int picId;

    @Column(name = "pic")
    private String pic;
}

