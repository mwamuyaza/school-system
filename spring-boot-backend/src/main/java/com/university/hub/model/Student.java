package com.university.hub.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "student_id", unique = true, nullable = false, length = 30)
    private String studentId; // e.g. ST-2026-X8392

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Status status = Status.PENDING_APPROVAL;

    @Column(name = "admin_approved", nullable = false)
    private boolean adminApproved = false;

    @Column(name = "lecturer_approved", nullable = false)
    private boolean lecturerApproved = false;

    @Column(name = "class_cohort_id")
    private Long classCohortId;

    @Column(name = "profile_pic")
    private String profilePic;

    public enum Status {
        ACTIVE,
        GRADUATED,
        SUSPENDED,
        WITHDRAWN,
        PENDING_APPROVAL
    }

    // 1. Standard Constructors
    public Student() {
        this.enrollmentDate = LocalDate.now();
        this.status = Status.PENDING_APPROVAL;
        this.adminApproved = false;
        this.lecturerApproved = false;
    }

    public Student(Long id, User user, String studentId, String firstName, String lastName, 
                   String email, String phone, String address, LocalDate enrollmentDate, 
                   Status status, boolean adminApproved, boolean lecturerApproved, 
                   Long classCohortId, String profilePic) {
        this.id = id;
        this.user = user;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.enrollmentDate = enrollmentDate != null ? enrollmentDate : LocalDate.now();
        this.status = status != null ? status : Status.PENDING_APPROVAL;
        this.adminApproved = adminApproved;
        this.lecturerApproved = lecturerApproved;
        this.classCohortId = classCohortId;
        this.profilePic = profilePic;
    }

    // 2. Plain Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isAdminApproved() {
        return adminApproved;
    }

    public boolean getAdminApproved() {
        return adminApproved;
    }

    public void setAdminApproved(boolean adminApproved) {
        this.adminApproved = adminApproved;
    }

    public boolean isLecturerApproved() {
        return lecturerApproved;
    }

    public boolean getLecturerApproved() {
        return lecturerApproved;
    }

    public void setLecturerApproved(boolean lecturerApproved) {
        this.lecturerApproved = lecturerApproved;
    }

    public Long getClassCohortId() {
        return classCohortId;
    }

    public void setClassCohortId(Long classCohortId) {
        this.classCohortId = classCohortId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    // 3. Classic Static Builder Pattern
    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private Long id;
        private User user;
        private String studentId;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private LocalDate enrollmentDate = LocalDate.now();
        private Status status = Status.PENDING_APPROVAL;
        private boolean adminApproved = false;
        private boolean lecturerApproved = false;
        private Long classCohortId;
        private String profilePic;

        StudentBuilder() {}

        public StudentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StudentBuilder user(User user) {
            this.user = user;
            return this;
        }

        public StudentBuilder studentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public StudentBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder email(String email) {
            this.email = email;
            return this;
        }

        public StudentBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public StudentBuilder address(String address) {
            this.address = address;
            return this;
        }

        public StudentBuilder enrollmentDate(LocalDate enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
            return this;
        }

        public StudentBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public StudentBuilder adminApproved(boolean adminApproved) {
            this.adminApproved = adminApproved;
            return this;
        }

        public StudentBuilder lecturerApproved(boolean lecturerApproved) {
            this.lecturerApproved = lecturerApproved;
            return this;
        }

        public StudentBuilder classCohortId(Long classCohortId) {
            this.classCohortId = classCohortId;
            return this;
        }

        public StudentBuilder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        public Student build() {
            return new Student(id, user, studentId, firstName, lastName, email, phone, address, 
                               enrollmentDate, status, adminApproved, lecturerApproved, 
                               classCohortId, profilePic);
        }
    }
}
