package com.university.hub.controller;

import com.university.hub.model.Student;
import com.university.hub.model.Staff;
import com.university.hub.repository.StudentRepository;
import com.university.hub.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StaffRepository staffRepository;

    /**
     * 1. RETRIEVE ALL PENDING ADMISSIONS
     */
    @GetMapping("/admissions/pending")
    public ResponseEntity<?> getPendingAdmissions() {
        List<Student> pendingStudents = studentRepository.findByStatus(Student.Status.PENDING_APPROVAL);
        List<Staff> pendingStaff = staffRepository.findByStatus(Staff.Status.PENDING_APPROVAL);

        return ResponseEntity.ok(Map.of(
                "students", pendingStudents,
                "staff", pendingStaff
        ));
    }

    /**
     * 2. APPROVE / ADMIT STUDENT (Dual-Approval Stage)
     * Admin role or Lecturer role can invoke this with appropriate headers
     */
    @PutMapping("/students/{id}/approve")
    public ResponseEntity<?> approveStudent(@PathVariable Long id, @RequestParam String actorRole) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student profile not found"));
        }

        Student student = studentOpt.get();

        if ("ADMIN".equalsIgnoreCase(actorRole)) {
            student.setAdminApproved(true);
            System.out.println("[ADMIN ACTION] Approved student: " + student.getFirstName() + " " + student.getLastName());
        } else if ("STAFF".equalsIgnoreCase(actorRole)) {
            student.setLecturerApproved(true);
            System.out.println("[STAFF ACTION] Approved student: " + student.getFirstName() + " " + student.getLastName());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid signature actor role"));
        }

        // Dual signature verification checkpoint:
        // Student becomes fully active only when BOTH Admin and Faculty have approved!
        if (student.isAdminApproved() && student.isLecturerApproved()) {
            student.setStatus(Student.Status.ACTIVE);
            System.out.println("[SYSTEM] Student ID " + id + " has reached complete double approval status and is ACTIVE!");
        }

        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    /**
     * 3. REJECT / SUSPEND STUDENT ADMISSION
     */
    @PutMapping("/students/{id}/reject")
    public ResponseEntity<?> rejectStudent(@PathVariable Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student profile not found"));
        }

        Student student = studentOpt.get();
        student.setStatus(Student.Status.WITHDRAWN);
        student.setAdminApproved(false);
        student.setLecturerApproved(false);

        studentRepository.save(student);
        System.out.println("[ADMIN ACTION] Terminated/Withdrew student enrollment for ID: " + id);
        return ResponseEntity.ok(student);
    }

    /**
     * 4. APPROVE LECTURER / STAFF MEMBER
     */
    @PutMapping("/staff/{id}/approve")
    public ResponseEntity<?> approveStaff(@PathVariable Long id) {
        Optional<Staff> staffOpt = staffRepository.findById(id);
        if (staffOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Staff member profile not found"));
        }

        Staff staff = staffOpt.get();
        staff.setStatus(Staff.Status.ACTIVE);

        staffRepository.save(staff);
        System.out.println("[ADMIN ACTION] Approved faculty lecturer ID: " + id + " - State is now ACTIVE");
        return ResponseEntity.ok(staff);
    }

    /**
     * 5. REJECT / DEACTIVATE LECTURER
     */
    @PutMapping("/staff/{id}/reject")
    public ResponseEntity<?> rejectStaff(@PathVariable Long id) {
        Optional<Staff> staffOpt = staffRepository.findById(id);
        if (staffOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Staff member profile not found"));
        }

        Staff staff = staffOpt.get();
        staff.setStatus(Staff.Status.TERMINATED);

        staffRepository.save(staff);
        System.out.println("[ADMIN ACTION] Terminated faculty member ID: " + id);
        return ResponseEntity.ok(staff);
    }
}
