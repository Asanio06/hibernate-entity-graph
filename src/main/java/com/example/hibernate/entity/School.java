package com.example.hibernate.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "School")
public class School {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;

	public School() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		School school = (School) o;
		return Objects.equals( getId(), school.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}
//
//	@Override
//	public String toString() {
//		return "School{" +
//				"id=" + id +
//				", name='" + name + '\'' +
//				'}';
//	}
}