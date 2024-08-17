package com.dgpad.thyme.repository;

import com.dgpad.thyme.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlogRepository extends JpaRepository<Blog, UUID> {

}
