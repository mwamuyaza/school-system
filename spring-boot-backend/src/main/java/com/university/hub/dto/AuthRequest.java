package com.university.hub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    /**
     * 1. Login Request DTO
     */
    public static class Login {
        @NotBlank(message = "Username or E-mail cannot be blank")
        private String username;

        @NotBlank(message = "Password cannot be blank")
        private String password;

        public Login() {}

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 2. Student Sign Up Request DTO
     */
    public static class StudentSignUp {
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;

        @NotBlank(message = "E-mail cannot be blank")
        @Email(message = "Invalid e-mail format")
        @Size(max = 100, message = "E-mail cannot exceed 100 characters")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
        private String password;

        @NotBlank(message = "First name cannot be blank")
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        private String firstName;

        @NotBlank(message = "Last name cannot be blank")
        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        private String lastName;

        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        private String phone;

        private String address;

        private Long classCohortId;

        public StudentSignUp() {}

        public StudentSignUp(String username, String email, String password, String firstName, 
                             String lastName, String phone, String address, Long classCohortId) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.address = address;
            this.classCohortId = classCohortId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Long getClassCohortId() {
            return classCohortId;
        }

        public void setClassCohortId(Long classCohortId) {
            this.classCohortId = classCohortId;
        }
    }

    /**
     * 3. Staff Sign Up Request DTO
     */
    public static class StaffSignUp {
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;

        @NotBlank(message = "E-mail cannot be blank")
        @Email(message = "Invalid e-mail format")
        @Size(max = 100, message = "E-mail cannot exceed 100 characters")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
        private String password;

        @NotBlank(message = "First name cannot be blank")
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        private String firstName;

        @NotBlank(message = "Last name cannot be blank")
        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        private String lastName;

        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        private String phone;

        private String address;

        @NotBlank(message = "Department cannot be blank")
        private String department;

        @NotBlank(message = "Qualification cannot be blank")
        private String qualification;

        @NotBlank(message = "Position cannot be blank")
        private String position;

        public StaffSignUp() {}

        public StaffSignUp(String username, String email, String password, String firstName, 
                           String lastName, String phone, String address, String department, 
                           String qualification, String position) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.address = address;
            this.department = department;
            this.qualification = qualification;
            this.position = position;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }

    /**
     * 4. Verification Code DTO
     */
    public static class Verify {
        @NotBlank(message = "Username cannot be blank")
        private String username;

        @NotBlank(message = "Verification code cannot be blank")
        @Size(min = 6, max = 6, message = "Verification code must be exactly 6 digits")
        private String code;

        public Verify() {}

        public Verify(String username, String code) {
            this.username = username;
            this.code = code;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * 5. Resend Code DTO
     */
    public static class ResendCode {
        @NotBlank(message = "Username cannot be blank")
        private String username;

        public ResendCode() {}

        public ResendCode(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    /**
     * 6. Forgot Password Request DTO
     */
    public static class ForgotPasswordRequest {
        @NotBlank(message = "Username cannot be blank")
        private String username;

        @NotBlank(message = "E-mail cannot be blank")
        @Email(message = "Invalid e-mail format")
        private String email;

        public ForgotPasswordRequest() {}

        public ForgotPasswordRequest(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    /**
     * 7. Password Recovery Reset DTO
     */
    public static class ForgotPasswordReset {
        @NotBlank(message = "Username cannot be blank")
        private String username;

        @NotBlank(message = "E-mail cannot be blank")
        @Email(message = "Invalid e-mail format")
        private String email;

        @NotBlank(message = "Recovery code cannot be blank")
        @Size(min = 6, max = 6, message = "Recovery code must be exactly 6 digits")
        private String code;

        @NotBlank(message = "New password cannot be blank")
        @Size(min = 6, max = 100, message = "New password must be at least 6 characters")
        private String newPassword;

        public ForgotPasswordReset() {}

        public ForgotPasswordReset(String username, String email, String code, String newPassword) {
            this.username = username;
            this.email = email;
            this.code = code;
            this.newPassword = newPassword;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
