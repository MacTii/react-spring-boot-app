package com.ZAIORO.SpringBootApplication.service;

import com.ZAIORO.SpringBootApplication.domain.Assignment;
import com.ZAIORO.SpringBootApplication.domain.Comment;
import com.ZAIORO.SpringBootApplication.domain.User;
import com.ZAIORO.SpringBootApplication.dto.CommentDto;
import com.ZAIORO.SpringBootApplication.repository.AssignmentRepository;
import com.ZAIORO.SpringBootApplication.repository.CommentRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Comment save(CommentDto commentDto, @AuthenticationPrincipal User user) {
        Comment comment = new Comment();
        Assignment assignment = assignmentRepository.getById(commentDto.getAssignmentId());

        comment.setId(commentDto.getId());
        comment.setAssignment(assignment);
        comment.setText(commentDto.getText());
        comment.setCreatedBy(user);
        if(comment.getId() == null)
            comment.setCreatedDate(ZonedDateTime.now());
        else
            comment.setCreatedDate(commentDto.getCreatedDate());

        return commentRepository.save(comment);
    }

    public Set<Comment> getCommentsByAssignmentId(Long assignmentId) {
        return commentRepository.findByAssignmentId(assignmentId);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
