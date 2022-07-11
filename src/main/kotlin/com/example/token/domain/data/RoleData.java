package com.example.token.domain.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class RoleData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UserData> users;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "roles_claims",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "claim_id", referencedColumnName = "id"))
    private Collection<ClaimData> claims;

    public RoleData() {
    }

    public RoleData(String name, Collection<ClaimData> claims) {
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.claims = claims;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Collection<UserData> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserData> users) {
        this.users = users;
    }

    public Collection<ClaimData> getClaims() {
        return claims;
    }

    public void setClaims(Collection<ClaimData> claims) {
        this.claims = claims;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleData)) return false;
        RoleData other = (RoleData) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RolesData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", users=" + users +
                ", claims=" + claims +
                '}';
    }
}
