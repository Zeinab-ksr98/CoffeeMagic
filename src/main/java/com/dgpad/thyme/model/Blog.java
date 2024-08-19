package com.dgpad.thyme.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
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
    private LocalDate createdDate;
    public Boolean Public;
    @OneToOne
    @JoinColumn(name = "main_image_id")
    private Media BlogImage; // One main image

    @Column(nullable = false)
    private int viewsCount = 0;

    public void incrementViews() {
        this.viewsCount++;
    }

}
