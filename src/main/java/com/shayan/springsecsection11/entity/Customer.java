package com.shayan.springsecsection11.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="customer") // this is optional
@Getter  @Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "pwd")
    private String pwd;

    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Authority> authorities;



}
