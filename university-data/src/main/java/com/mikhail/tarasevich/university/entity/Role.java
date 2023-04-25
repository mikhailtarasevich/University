package com.mikhail.tarasevich.university.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "roles")
    List<Privilege> privileges;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> users;
    
}
