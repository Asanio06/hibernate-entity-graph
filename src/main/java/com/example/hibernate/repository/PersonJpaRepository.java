package com.example.hibernate.repository;

import com.example.hibernate.entity.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonJpaRepository extends JpaRepository<Person, Long> {

	@Override
	@EntityGraph("graph_that_take_heritage_into_consideration")
	List<Person> findAll();
}
