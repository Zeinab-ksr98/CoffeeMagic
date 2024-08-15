package com.dgpad.thyme.repository;

import com.dgpad.thyme.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByDate(LocalDate date);
    List<Post> findByDateAfter(LocalDate date);

    @Query("SELECT m FROM Post m ORDER BY m.viewsCount DESC")
    List<Post> findMostPopular();

    @Query("SELECT m FROM Post m WHERE m.date < :today AND m.postMedias IS NOT EMPTY")
    List<Post> findArchivedWithVideos(@Param("today") LocalDate today);

    @Query("SELECT m FROM Post m WHERE m.date BETWEEN :startDate AND :endDate")
    List<Post> findMajlisCreatedThisMonth(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    Optional<Post> findTopByOrderByViewsCountDesc();

    List<Post> findByUserId(UUID userId);
}

//package com.dgpad.thyme.repository;
//
//import com.dgpad.thyme.model.Post;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public interface PostRepository extends JpaRepository<Post, UUID> {
//    List<Post> findByDate(LocalDate date);
//    List<Post> findByDateAfter(LocalDate date);
//    @Query("SELECT m FROM Post m ORDER BY m.viewsCount DESC")
//    List<Post> findMostPopular();
//    @Query("SELECT m FROM Post m WHERE m.date < :today AND m.videos != null")
//    List<Post> findArchivedWithVideos(LocalDate today);
//    List<Post> findMajlisCreatedThisMonth();
//    Optional<Post> findTopByOrderByViewsCountDesc();
//    List<Post> findByUserId(UUID userId);
//
//
//}
