package com.example.hibernate.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "Student")
@DiscriminatorValue("student")
public class Student extends Person {

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	School school;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	List<Course> courses;

	public Student() {
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void addCourse(Course course) {
		if ( this.courses == null ) {
			this.courses = new ArrayList<>();
		}
		courses.add( course );
	}


	public void removeCourse(Course course) {
		if ( this.courses == null ) {
			return;
		}
		courses.remove( course );
	}
}
