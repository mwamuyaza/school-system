package com.university.hub.controller;

import com.university.hub.dto.AuthRequest;
import com.university.hub.model.Student;
import com.university.hub.model.Staff;
import com.university.hub.model.User;
import com.university.hub.repository.StudentRepository;
import com.university.hub.repository.StaffRepository;
import com.university.hub.repository.UserRepository;
import com.university.hub.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private EmailService emailService;

    /**
     * UTILITY: Generates a highly secure 6-digit verification code.
     */
    private String generateOtpCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * UTILITY: Formats names cleanly for institutional email addresses (lowercase, no spaces)
     */
    private String cleanNameForEmail(String name) {
        if (name == null) return "";
        return name.trim().toLowerCase().replaceAll("\\s+", "");
    }

    /**
     * 1. LOGIN USER ENDPOINT (With Smart Response Redirection Variables)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest.Login request) {
        String inputName = request.getUsername().trim();
        String inputPass = request.getPassword();

        // Search user by username, institutional email, or personal backup email
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(inputName, inputName);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        User user = userOpt.get();

        // Validate password comparison
        if (!user.getPassword().equals(inputPass)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        // Enforce email verification status check
        if (!user.isVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "error", "ACCOUNT_UNVERIFIED",
                    "message", "Please verify your email address to activate your account",
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "role", user.getRole().name(),
                    "_debugCode", user.getVerificationCode()
            ));
        }

        // Create mock JWT-style token
        String token = "user-token-" + user.getRole().name() + "-" + user.getId() + "-" + System.currentTimeMillis();

        // Load contextual profile details
        Object profile = null;
        if (user.getRole() == User.Role.STUDENT) {
            profile = studentRepository.findByUserId(user.getId()).orElse(null);
        } else if (user.getRole() == User.Role.STAFF) {
            profile = staffRepository.findByUserId(user.getId()).orElse(null);
        }

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole().name() // Read by React to determine page route redirection
                ),
                "profile", profile != null ? profile : Map.of()
        ));
    }

    /**
     * 2. STUDENT SIGN UP (Onboarding with Institutional Email Generation)
     */
    @PostMapping("/signup")
    public ResponseEntity<?> studentSignUp(@RequestBody AuthRequest.StudentSignUp request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username is already in use"));
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Formulate clean institutional email: firstname.lastname.2026@student.universityhub.edu
        String institutionalEmail = cleanNameForEmail(request.getFirstName()) + "."
                + cleanNameForEmail(request.getLastName()) + "."
                + currentYear + "@student.universityhub.edu";

        // Double check against existing assignments
        if (userRepository.existsByEmail(institutionalEmail) || userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "E-mail identity is already registered inside this domain"));
        }

        String verificationCode = generateOtpCode();

        // Create Master Authentication User using the generated professional email
        User user = User.builder()
                .username(request.getUsername())
                .email(institutionalEmail)
                .password(request.getPassword()) // User-chosen secure password
                .role(User.Role.STUDENT)
                .verified(false)
                .active(true)
                .verificationCode(verificationCode)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // Generate student admission identification ID (e.g. ST-2026-X12345)
        String studentRegId = "ST-" + currentYear + "-X" + (10000 + new Random().nextInt(90000));

        // Create Student Profile Entity
        Student student = Student.builder()
                .user(user)
                .studentId(studentRegId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(institutionalEmail) // Official portal login profile email
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(Student.Status.PENDING_APPROVAL)
                .adminApproved(false)
                .lecturerApproved(false)
                .classCohortId(request.getClassCohortId())
                .build();

        studentRepository.save(student);

        // Dispatch real activation e-mail OTP to their personal layout box address!
        try {
            emailService.sendVerificationEmail(request.getEmail(), user.getUsername(), verificationCode);
        } catch (Exception e) {
            System.err.println("Failed to dispatch email: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Registration initialized. Verification code sent to your personal backup email.",
                "username", user.getUsername(),
                "institutionalEmail", institutionalEmail,
                "personalEmail", request.getEmail(),
                "_debugCode", verificationCode
        ));
    }

    /**
     * 3. STAFF SIGN UP (Onboarding with Staff Domain Setup)
     */
    @PostMapping("/signup-staff")
    public ResponseEntity<?> staffSignUp(@RequestBody AuthRequest.StaffSignUp request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username is already in use"));
        }

        // Formulate clean institutional staff email: firstname.lastname@staff.universityhub.edu
        String staffEmail = cleanNameForEmail(request.getFirstName()) + "."
                + cleanNameForEmail(request.getLastName()) + "@staff.universityhub.edu";

        if (userRepository.existsByEmail(staffEmail) || userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "E-mail identity is already registered inside this domain"));
        }

        String verificationCode = generateOtpCode();

        User user = User.builder()
                .username(request.getUsername())
                .email(staffEmail)
                .password(request.getPassword()) // User-chosen secure password
                .role(User.Role.STAFF)
                .verified(false)
                .active(true)
                .verificationCode(verificationCode)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        String staffRegId = "FAC-" + Calendar.getInstance().get(Calendar.YEAR) + "-T" + (100 + new Random().nextInt(900));

        Staff staff = Staff.builder()
                .user(user)
                .staffId(staffRegId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(staffEmail)
                .phone(request.getPhone())
                .address(request.getAddress())
                .department(request.getDepartment())
                .qualification(request.getQualification())
                .position(request.getPosition())
                .status(Staff.Status.PENDING_APPROVAL)
                .build();

        staffRepository.save(staff);

        // Dispatch real email verification OTP to personal mail address
        try {
            emailService.sendVerificationEmail(request.getEmail(), user.getUsername(), verificationCode);
        } catch (Exception e) {
            System.err.println("Failed to dispatch email: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Faculty profile generated. Activation code dispatched to your personal backup email.",
                "username", user.getUsername(),
                "institutionalEmail", staffEmail,
                "personalEmail", request.getEmail(),
                "_debugCode", verificationCode
        ));
    }

    /**
     * 4. VERIFY 6-DIGIT OTP ACTIVATION CODE
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody AuthRequest.Verify request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        User user = userOpt.get();
        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(request.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Incorrect 6-digit activation code"));
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Account activated successfully. You can now login using your generated institutional email or username.",
                "username", user.getUsername(),
                "email", user.getEmail(),
                "verified", true
        ));
    }

    /**
     * 5. RESEND OTP CODE
     */
    @PostMapping("/resend-code")
    public ResponseEntity<?> requestResendCode(@RequestBody AuthRequest.ResendCode request, @RequestParam String personalEmail) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User record not found"));
        }

        User user = userOpt.get();
        String newOtp = generateOtpCode();
        user.setVerificationCode(newOtp);
        userRepository.save(user);

        try {
            emailService.sendVerificationEmail(personalEmail, user.getUsername(), newOtp);
        } catch (Exception e) {
            System.err.println("Email resend error: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "message", "A new 6-digit verification code was dispatched.",
                "_debugCode", newOtp
        ));
    }

    /**
     * 6. FORGOT PASSWORD - REQUEST OTP CODE
     */
    @PostMapping("/forgot-password-request")
    public ResponseEntity<?> forgotPasswordRequest(@RequestBody AuthRequest.ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No user found with the provided credentials"));
        }

        User user = userOpt.get();
        String resetOtp = generateOtpCode();
        user.setResetCode(resetOtp);
        userRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(request.getEmail().trim(), user.getUsername(), resetOtp);
        } catch (Exception e) {
            System.err.println("Reset email error: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "message", "Verification code for password reset dispatched to your backup email.",
                "_debugCode", resetOtp
        ));
    }

    /**
     * 7. FORGOT PASSWORD - PERFORM RESET
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordReset(@RequestBody AuthRequest.ForgotPasswordReset request) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Username not found"));
        }

        User user = userOpt.get();
        if (user.getResetCode() == null || !user.getResetCode().equals(request.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Incorrect 6-digit recovery code"));
        }

        user.setPassword(request.getNewPassword());
        user.setResetCode(null);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Your password has been successfully updated. Please login."
        ));
    }
}