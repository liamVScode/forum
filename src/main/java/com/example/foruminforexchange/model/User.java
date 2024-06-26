    package com.example.foruminforexchange.model;

    import jakarta.persistence.*;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.Date;
    import java.util.List;


    @Entity
    @Table(name = "Users")
    public class User implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long userId;

        @Column(unique = true, nullable = true)
        private String facebookId;

        @Column(nullable = false, length = 255)
        private String password;

        @Column(nullable = false, length = 255)
        private String email;

        @Column(length = 255)
        private String avatar;

        @Column(name = "first_name", length = 255)
        private String firstName;

        @Column(name = "last_name", length = 255)
        private String lastName;

        @Column(name = "date_of_birth")
        private LocalDate dateOfBirth;

        @Column(length = 255)
        private String location;

        @Column(length = 255)
        private String website;

        private Status status;

        @Column(name = "is_locked")
        private Boolean isLocked = false;

        @ManyToOne
        @JoinColumn(name = "locked_by")
        private User lockedBy;

        @Column(columnDefinition = "NVARCHAR(255)")
        private String about;

        @Column(length = 255)
        private String skype;

        @Column(length = 255)
        private String facebook;

        @Column(length = 255)
        private String twitter;

        @Column(name = "create_at", nullable = false)
        private LocalDateTime createAt = LocalDateTime.now();

        private Role role;

        // Constructors, Getters and Setters

        public User() {
        }

        public User(String password, String email, String avatar, String firstName, String lastName, LocalDate dateOfBirth, String location, String website, String about, String skype, String facebook, String twitter) {
            this.password = password;
            this.email = email;
            this.avatar = avatar;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.location = location;
            this.website = website;
            this.about = about;
            this.skype = skype;
            this.facebook = facebook;
            this.twitter = twitter;
        }




        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role.name()));
        }

        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getSkype() {
            return skype;
        }

        public void setSkype(String skype) {
            this.skype = skype;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }


        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getFacebookId() {
            return facebookId;
        }

        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }

        public Boolean getLocked() {
            return isLocked;
        }

        public void setLocked(Boolean locked) {
            isLocked = locked;
        }

        public User getLockedBy() {
            return lockedBy;
        }

        public void setLockedBy(User lockedBy) {
            this.lockedBy = lockedBy;
        }

        public LocalDateTime getCreateAt() {
            return createAt;
        }

        public void setCreateAt(LocalDateTime createAt) {
            this.createAt = createAt;
        }
    }
