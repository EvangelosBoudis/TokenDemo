package com.example.token.domain.data;

import com.example.token.security.utils.AuthenticationToken;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Double Bcrypt with salthash

@Entity
@Table(name = "users")
public class UserData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "normalized_username")
    private String normalizedUsername;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "email_confirmed", nullable = false)
    private boolean emailConfirmed;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "phone_number_confirmed", nullable = false)
    private boolean phoneNumberConfirmed;

    @Column(name = "mfa_enabled", nullable = false)
    private boolean mfaEnabled;

    @Column(name = "lockout_end")
    private Timestamp lockoutEnd;

    @Column(name = "lockout_enabled", nullable = false)
    private boolean lockoutEnabled;

    @Column(name = "access_failed_count", nullable = false)
    private int accessFailedCount;

    @Column(name = "disabled_access", nullable = false)
    private boolean disabledAccess;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<RoleData> roles;

    public UserData() {
    }

    public UserData(String username, String email, String passwordHash, Collection<RoleData> roles) {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.username = username;
        this.email = email;
        this.emailConfirmed = false;
        this.passwordHash = passwordHash;
        this.phoneNumberConfirmed = false;
        this.mfaEnabled = false;
        this.lockoutEnabled = false;
        this.accessFailedCount = 0;
        this.disabledAccess = false;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNormalizedUsername() {
        return normalizedUsername;
    }

    public void setNormalizedUsername(String normalizedUsername) {
        this.normalizedUsername = normalizedUsername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberConfirmed() {
        return phoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(boolean phoneNumberConfirmed) {
        this.phoneNumberConfirmed = phoneNumberConfirmed;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public Timestamp getLockoutEnd() {
        return lockoutEnd;
    }

    public void setLockoutEnd(Timestamp lockoutEnd) {
        this.lockoutEnd = lockoutEnd;
    }

    public boolean isLockoutEnabled() {
        return lockoutEnabled;
    }

    public void setLockoutEnabled(boolean lockoutEnabled) {
        this.lockoutEnabled = lockoutEnabled;
    }

    public int getAccessFailedCount() {
        return accessFailedCount;
    }

    public void setAccessFailedCount(int accessFailedCount) {
        this.accessFailedCount = accessFailedCount;
    }

    public boolean isDisabledAccess() {
        return disabledAccess;
    }

    public void setDisabledAccess(boolean disabledAccess) {
        this.disabledAccess = disabledAccess;
    }

    public Collection<RoleData> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleData> roles) {
        this.roles = roles;
    }

    public AuthenticationToken asAuthenticationToken() {

        Stream<GrantedAuthority> roles = getRoles()
                .stream()
                .map(RoleData::getName)
                .map(SimpleGrantedAuthority::new);

        Stream<GrantedAuthority> claims = getRoles()
                .stream()
                .map(RoleData::getClaims)
                .flatMap(Collection::stream)
                .map(ClaimData::getName)
                .map(SimpleGrantedAuthority::new);

        return new AuthenticationToken(
                id.toString(),
                passwordHash,
                Stream.concat(roles, claims).collect(Collectors.toList())
        );
    }

    /**
     * Implement equals() and hashCode() according to:
     * a) This guide by Thorben Janssen: https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/
     * b) This guide by Vlad Mihalcea: https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData)) return false;
        UserData other = (UserData) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UsersData{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", username='" + username + '\'' +
                ", normalizedUsername='" + normalizedUsername + '\'' +
                ", email='" + email + '\'' +
                ", emailConfirmed=" + emailConfirmed +
                ", passwordHash='" + passwordHash + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneNumberConfirmed=" + phoneNumberConfirmed +
                ", mfaEnabled=" + mfaEnabled +
                ", lockoutEnd=" + lockoutEnd +
                ", lockoutEnabled=" + lockoutEnabled +
                ", accessFailedCount=" + accessFailedCount +
                ", disabledAccess=" + disabledAccess +
                ", roles=" + roles +
                '}';
    }
}
