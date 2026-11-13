package com.shayan.springsecsection11.entity;


import com.shayan.springsecsection11.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUTHORITY")
@Getter
@Setter
@RequiredArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role")
    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
