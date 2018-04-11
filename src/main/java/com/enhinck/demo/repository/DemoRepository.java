package com.enhinck.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enhinck.demo.entity.Demo;

@Repository
public interface DemoRepository extends JpaRepository<Demo, Integer> {
	
}
