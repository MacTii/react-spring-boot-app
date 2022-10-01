package com.ZAIORO.SpringBootApplication.web;

import com.ZAIORO.SpringBootApplication.domain.Assignment;
import com.ZAIORO.SpringBootApplication.domain.User;
import com.ZAIORO.SpringBootApplication.dto.AssignmentResponseDto;
import com.ZAIORO.SpringBootApplication.enums.AuthorityEnum;
import com.ZAIORO.SpringBootApplication.service.AssignmentService;
import com.ZAIORO.SpringBootApplication.service.UserService;
import com.ZAIORO.SpringBootApplication.util.AuthorityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> createAssignment(@AuthenticationPrincipal User user) {
        Assignment newAssignment = assignmentService.save(user);

        return ResponseEntity.ok(newAssignment);
    }

    @GetMapping("")
    public ResponseEntity<?> getAssignments(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(assignmentService.findByUser(user));
    }

    @GetMapping("{assignmentId}")
    public ResponseEntity<?> getAssignment(@PathVariable Long assignmentId, @AuthenticationPrincipal User user) {
        Optional<Assignment> assignmentOptional = assignmentService.findById(assignmentId);

        AssignmentResponseDto responseDto = new AssignmentResponseDto(assignmentOptional.orElse(new Assignment()));
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("{assignmentId}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long assignmentId,
                                              @RequestBody Assignment assignment,
                                              @AuthenticationPrincipal User user) {

        if (assignment.getCodeReviewer() != null) {
            User codeReviewer = assignment.getCodeReviewer();
            codeReviewer = userService.findUserByUsername(codeReviewer.getUsername()).orElse(new User());

            if(AuthorityUtil.hasRole(AuthorityEnum.ROLE_CODE_REVIEWER.name(), codeReviewer)) {
                assignment.setCodeReviewer(codeReviewer);
            }
        }
        Assignment updatedAssignment = assignmentService.save(assignment);
        return ResponseEntity.ok(updatedAssignment);
    }
}
