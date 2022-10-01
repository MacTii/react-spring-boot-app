package com.ZAIORO.SpringBootApplication.repository;

import com.ZAIORO.SpringBootApplication.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{

}
