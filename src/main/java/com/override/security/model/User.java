package com.override.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Имя не должно быть пустым!")
    @Size(min = 3, max = 20 , message = "Имя должно быть от 3 до 20 символов!")
    private String name;

    @Column
    @NotEmpty(message = "Имя не должно быть пустым!")
    @Size(min = 3, max = 20 , message = "Имя должно быть от 3 до 20 символов!")
    private String lastName;

    @Column
    @NotEmpty(message = "Поле не должно быть пустым!")
    private String email;

    @Column
    @Min(value = 1, message = "Возраст должен быть больше 0 лет!")
    private int age;

    @Column
    @NotEmpty(message = "Пароль не должнен быть пустым!")
    @Size(min = 3, max = 61 , message = "Пароль должно быть от 3 до 61 символов!")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return name;
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
}
