package com.ZAIORO.SpringBootApplication.service;

import com.ZAIORO.SpringBootApplication.domain.Assignment;
import com.ZAIORO.SpringBootApplication.domain.User;
import com.ZAIORO.SpringBootApplication.enums.AssignmentStatusEnum;
import com.ZAIORO.SpringBootApplication.enums.AuthorityEnum;
import com.ZAIORO.SpringBootApplication.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Assignment save(User user) {
        Assignment assignment = new Assignment();
        assignment.setStatus(AssignmentStatusEnum.PENDING_SUBMISSION.getStatus());
        assignment.setNumber(findNextAssignmentToSubmit(user));
        assignment.setUser(user);

        return assignmentRepository.save(assignment);
    }

    private Integer findNextAssignmentToSubmit(User user) {
        Set<Assignment> assignmentsByUser = assignmentRepository.findByUser(user);
        if(assignmentsByUser == null) {
            return 1;
        }

        Optional<Integer> nextAssignmentNumOpt = assignmentsByUser.stream().sorted((a1,a2) -> {
            if(a1.getNumber() == null) return 1;
            if(a2.getNumber() == null) return 1;
            return a2.getNumber().compareTo(a1.getNumber());
        }).map(assignment -> {
            if(assignment.getNumber() == null) return 1;
            return assignment.getNumber() + 1;
        }).findFirst();

        return nextAssignmentNumOpt.orElse(1);
    }

    public Set<Assignment> findByUser(User user) {
        boolean hasCodeReviewerRole = user.getAuthorities()
                .stream()
                .filter(auth -> AuthorityEnum.ROLE_CODE_REVIEWER.name().equals(auth.getAuthority()))
                .count() > 0;
        if(hasCodeReviewerRole) {
            return assignmentRepository.findByCodeReviewer(user);
        } else {
            return assignmentRepository.findByUser(user);
        }
    }

    public Optional<Assignment> findById(Long assignmentID) {
        return assignmentRepository.findById(assignmentID);
    }

    public Assignment save(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }
}
