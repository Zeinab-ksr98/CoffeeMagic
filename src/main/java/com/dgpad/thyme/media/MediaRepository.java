package com.dgpad.thyme.media;

import com.dgpad.thyme.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<Media, UUID> {
    List<Media> findMediaByOwnerId(UUID owner);
    @Query("SELECT v FROM Media v WHERE v.createdDate >= :startOfWeek AND v.createdDate <= :endOfWeek ")
    List<Media> findthisweekMedia(LocalDate startOfWeek, LocalDate endOfWeek);
    @Query("SELECT v FROM Media v WHERE YEAR(v.createdDate) = YEAR(CURRENT_DATE) AND MONTH(v.createdDate) = MONTH(CURRENT_DATE) ORDER BY v.viewsCount DESC")
    List<Media> findMediasCreatedThisMonth();
    Optional<Media> findTopByOrderByViewsCountDesc();


}
