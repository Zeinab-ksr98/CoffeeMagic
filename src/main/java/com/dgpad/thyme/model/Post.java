package com.dgpad.thyme.model;

import com.dgpad.thyme.model.Media;
import com.dgpad.thyme.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
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

    @NotBlank(message = "Description cannot be blank")
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String ingredients;
    @Column(nullable = false)
    private String Instructions;
    @Column(nullable = false)
    private String Servings;
    @Column(nullable = false)
    private LocalDate createdDate;
    public Boolean Public;
    @OneToOne
    @JoinColumn(name = "main_image_id")
    private Media PostImage; // One main image
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> postMedias;


    @Column(nullable = false)
    private int viewsCount = 0;

    public void incrementViews() {
        this.viewsCount++;
    }

}
