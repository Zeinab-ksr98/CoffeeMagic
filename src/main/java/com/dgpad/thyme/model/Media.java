package com.dgpad.thyme.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String fileName; // Name of the file stored in MinIO


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner; // User who uploaded the video

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // Link to Post
    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;
}
