package com.company.carpooling.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@JsonIgnoreProperties({"creator", "receiver"})
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
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfCreation;

    @OneToOne(mappedBy = "feedback", cascade = CascadeType.REMOVE)
    private FeedbackComment feedbackComment;
}
