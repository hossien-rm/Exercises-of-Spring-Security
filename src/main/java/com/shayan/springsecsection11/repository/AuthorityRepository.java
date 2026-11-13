package com.shayan.springsecsection11.repository;

import com.shayan.springsecsection11.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
