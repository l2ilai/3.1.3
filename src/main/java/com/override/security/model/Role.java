package com.override.security.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "t_role")
public class Role implements GrantedAuthority {

    @Id
    @Column
    private Long id;

    @Column
    private String role;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Role(String role) {
        this.role = role;
    }

    public Role() {
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public String toString() {
        return  this.role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role1 = (Role) o;

        if (!id.equals(role1.id)) return false;
        return role.equals(role1.role);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }
}
