package com.dgpad.thyme.model;

import com.dgpad.thyme.model.Media;
import com.dgpad.thyme.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Title cannot be blank")
    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String ingredients;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String Instructions;

    @Column(nullable = false)
    private int Servings;
    @Column(nullable = false)
    private LocalDate createdDate;
    @OneToOne
    @JoinColumn(name = "main_image_id")
    private Media PostImage; // One main image
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Media> postMedias= new ArrayList<>();;


    @Column(nullable = false)
    private int viewsCount = 0;

    public void incrementViews() {
        this.viewsCount++;
    }

}
