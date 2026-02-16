package com.example.webapp.controller.menu;

import com.example.webapp.model.Comment;
import com.example.webapp.model.MenuItem;
import com.example.webapp.model.User;
import com.example.webapp.repository.CommentRepository;
import com.example.webapp.repository.MenuItemRepository;
import com.example.webapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    public CommentController(CommentRepository commentRepository,
                             UserRepository userRepository,
                             MenuItemRepository menuItemRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
    }

    /* =========================
       ПОЛУЧЕНИЕ КОММЕНТАРИЕВ
       ========================= */
    @GetMapping("/{menuItemId}")
    public List<Comment> getComments(@PathVariable Long menuItemId) {

        return commentRepository
                .findByMenuItemIdOrderByCreatedAtDesc(menuItemId);
    }


    /* =========================
       ДОБАВЛЕНИЕ КОММЕНТАРИЯ
       ========================= */
    @PostMapping("/{menuItemId}")
    public Comment addComment(@PathVariable Long menuItemId,
                              @RequestBody Map<String, String> body,
                              Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MenuItem item = menuItemRepository
                .findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        String text = body.get("text");
        String ratingStr = body.get("rating");

        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Comment text cannot be empty");
        }

        if (text.length() > 200) {
            throw new RuntimeException("Comment too long (max 200 chars)");
        }

        int rating = 0;

        if (ratingStr != null && !ratingStr.isEmpty()) {
            rating = Integer.parseInt(ratingStr);

            if (rating < 1 || rating > 5) {
                throw new RuntimeException("Rating must be between 1 and 5");
            }
        }

        Comment comment = new Comment();
        comment.setMenuItem(item);
        comment.setUser(user);
        comment.setText(text.trim());
        comment.setRating(rating);

        return commentRepository.save(comment);
    }


    /* =========================
       УДАЛЕНИЕ КОММЕНТАРИЯ
       ========================= */
    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId,
                              Authentication authentication) {

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String currentUsername = authentication.getName();

        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You can delete only your own comments");
        }

        commentRepository.delete(comment);
    }
}
