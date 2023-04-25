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
@Table(name = "privileges")
public class Privilege {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "role_privileges",
            joinColumns = @JoinColumn(name = "privilege_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles;

}
