package com.ZAIORO.SpringBootApplication.repository;

import com.ZAIORO.SpringBootApplication.domain.Assignment;
import com.ZAIORO.SpringBootApplication.domain.Comment;
import com.ZAIORO.SpringBootApplication.domain.User;
import com.ZAIORO.SpringBootApplication.enums.AssignmentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Set<Assignment> findByUser(User user);

    @Query("select a from Assignment a " +
            "where (a.status = 'submitted' and (a.codeReviewer is null or a.codeReviewer = :codeReviewer)) " +
            "or a.codeReviewer = :codeReviewer")
    Set<Assignment> findByCodeReviewer(User codeReviewer);
}
