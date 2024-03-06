package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User creator;

    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User receiver;

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfCreation;
}
