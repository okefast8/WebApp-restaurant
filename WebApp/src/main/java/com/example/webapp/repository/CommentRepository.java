package com.example.webapp.repository;

import com.example.webapp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByMenuItemIdOrderByCreatedAtDesc(Long menuItemId);
}
