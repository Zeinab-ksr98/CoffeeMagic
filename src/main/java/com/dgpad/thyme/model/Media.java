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
    private String title; // Title of the video
    private String description; // Description of the video

    @Column(nullable = false)
    private String fileName; // Name of the file stored in MinIO

    @Column(nullable = false)
    private String fileType; // Type of the file (e.g., video/mp4)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner; // User who uploaded the video

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // Link to Post

    @Column(nullable = false)
    private LocalDate createdDate; // Date when the video was created

    @Column(nullable = false)
    private int viewsCount = 0; // Number of views

    // Method to increment views count
    public void incrementViews() {
        this.viewsCount++;
    }
}
