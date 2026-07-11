package com.university.hub.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "staff_id", unique = true, nullable = false, length = 30)
    private String staffId; // e.g. FAC-2026-T924

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

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false, length = 100)
    private String qualification;

    @Column(nullable = false, length = 50)
    private String position; // e.g. Lecturer, Professor, Adjunct

    @Column(name = "joining_date")
    private LocalDate joiningDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Status status = Status.PENDING_APPROVAL;

    @Column(name = "profile_pic")
    private String profilePic;

    public enum Status {
        ACTIVE,
        ON_LEAVE,
        TERMINATED,
        PENDING_APPROVAL
    }

    // 1. Standard Constructors
    public Staff() {
        this.joiningDate = LocalDate.now();
        this.status = Status.PENDING_APPROVAL;
    }

    public Staff(Long id, User user, String staffId, String firstName, String lastName, 
                 String email, String phone, String address, String department, 
                 String qualification, String position, LocalDate joiningDate, 
                 Status status, String profilePic) {
        this.id = id;
        this.user = user;
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.department = department;
        this.qualification = qualification;
        this.position = position;
        this.joiningDate = joiningDate != null ? joiningDate : LocalDate.now();
        this.status = status != null ? status : Status.PENDING_APPROVAL;
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

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    // 3. Classic Static Builder Pattern
    public static StaffBuilder builder() {
        return new StaffBuilder();
    }

    public static class StaffBuilder {
        private Long id;
        private User user;
        private String staffId;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String department;
        private String qualification;
        private String position;
        private LocalDate joiningDate = LocalDate.now();
        private Status status = Status.PENDING_APPROVAL;
        private String profilePic;

        StaffBuilder() {}

        public StaffBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StaffBuilder user(User user) {
            this.user = user;
            return this;
        }

        public StaffBuilder staffId(String staffId) {
            this.staffId = staffId;
            return this;
        }

        public StaffBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StaffBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StaffBuilder email(String email) {
            this.email = email;
            return this;
        }

        public StaffBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public StaffBuilder address(String address) {
            this.address = address;
            return this;
        }

        public StaffBuilder department(String department) {
            this.department = department;
            return this;
        }

        public StaffBuilder qualification(String qualification) {
            this.qualification = qualification;
            return this;
        }

        public StaffBuilder position(String position) {
            this.position = position;
            return this;
        }

        public StaffBuilder joiningDate(LocalDate joiningDate) {
            this.joiningDate = joiningDate;
            return this;
        }

        public StaffBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public StaffBuilder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        public Staff build() {
            return new Staff(id, user, staffId, firstName, lastName, email, phone, address, 
                             department, qualification, position, joiningDate, status, profilePic);
        }
    }
}
