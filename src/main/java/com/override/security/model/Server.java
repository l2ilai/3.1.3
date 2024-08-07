package com.override.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_server")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    @NotEmpty(message = "Имя сервера не должно быть пустым!")
    @Size(min = 3, max = 20, message = "Имя сервера должно быть от 3 до 20 символов!")
    private String name;

    @Column
    @NotEmpty(message = "IP не должно быть пустым!")
    @Size(min = 7, max = 15, message = "IP сервера должно быть от 7 до 20 символов!")
    private String ip;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="t_user_server",
            joinColumns=  @JoinColumn(name="server_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="user_id", referencedColumnName="id") )
    private Set<User> users;

}
