package com.company.carpooling.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "feedbacks_comments")
@Getter
@Setter
public class FeedbackComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
