package com.example.hibernate;


import com.example.hibernate.entity.*;
import com.example.hibernate.repository.PersonJpaRepository;
import jakarta.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.persistence.PersistenceUnitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HibernateApplicationIT {

	@Autowired
	private PersonJpaRepository personJpaRepository;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void setup() {

		var school = new School();
		school.setName( "School" );

		var student = new Student();
		student.setFirstname( "Bob" );
		student.setLastname( "Doe" );
		student.setSchool( school );

		var teacher = new Teacher();
		teacher.setFirstname( "Alice" );
		teacher.setLastname( "Doe" );

		var payingCourse = new PayingCourse();
		payingCourse.setName( "Paying course" );
		payingCourse.setMoneyReceiver( teacher );

		var freeCourse = new FreeCourse();
		freeCourse.setName( "Free course" );

		student.addCourse( payingCourse );
		student.addCourse( freeCourse );

		personJpaRepository.saveAll( List.of( student, teacher ) );
	}

	@BeforeEach
	void clean() {
		var em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery( "DELETE FROM student_courses" ).executeUpdate();
		em.createQuery( "DELETE FROM Course" ).executeUpdate();
		em.createQuery( "DELETE FROM Person " ).executeUpdate();
		em.getTransaction().commit();
		em.close();
	}


	@Nested
	class WithEntityManager {
		@Test
		public void fetch_person_without_subclass_specifics_attributes() {
			var em = entityManagerFactory.createEntityManager();
			PersistenceUnitUtil persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();


			var students = em.createQuery( "select s from Student s left join s.school on s.school.id = 1", Student.class ).getResultList();

			var persons = em.createQuery( "select p from Person p", Person.class )
					.getResultList();

			em.close();

			var student = (Student) persons.stream().filter( p -> p instanceof Student ).findFirst().get();


			var isStudentCoursesLoaded = persistenceUnitUtil.isLoaded( student, "courses" );
			var isStudentSchoolLoaded = persistenceUnitUtil.isLoaded( student, "school" );

			assertThat( isStudentCoursesLoaded ).isFalse();
			assertThat( isStudentSchoolLoaded ).isFalse();
		}


		@Test
		public void fetch_person_with_subclass_specifics_attributes() {
			var em = entityManagerFactory.createEntityManager();
			PersistenceUnitUtil persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();


			var graph = em.createEntityGraph( Person.class );
			var studentGraph = graph.addTreatedSubgraph( Student.class );
			studentGraph.addAttributeNodes( "school", "courses" );

			var payingCourseSubgraph = studentGraph.addSubgraph( "courses", PayingCourse.class );
			payingCourseSubgraph.addAttributeNodes( "moneyReceiver" );

			var persons = em.createQuery( "select p from Person p", Person.class )
					.setHint( "jakarta.persistence.fetchgraph", graph )
					.getResultList();

			em.close();

			var student = (Student) persons.stream().filter( p -> p instanceof Student ).findFirst().get();

			var isStudentCoursesLoaded = persistenceUnitUtil.isLoaded( student, "courses" );
			var isStudentSchoolLoaded = persistenceUnitUtil.isLoaded( student, "school" );


			assertThat( isStudentCoursesLoaded ).isTrue();
			assertThat( isStudentSchoolLoaded ).isTrue();


			var payingCourse = (PayingCourse) student.getCourses()
					.stream()
					.filter( p -> p instanceof PayingCourse )
					.findFirst()
					.get();

			var isPayingCourseMoneyReceiverLoaded = persistenceUnitUtil.isLoaded( payingCourse, "moneyReceiver" );

			assertThat( isPayingCourseMoneyReceiverLoaded ).isTrue();

		}

		@Test
		public void fetch_person_with_subclass_specifics_attributes_via_named_graph_entity() {
			var em = entityManagerFactory.createEntityManager();
			PersistenceUnitUtil persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();


			var graph = em.getEntityGraph( "graph_that_take_heritage_into_consideration" );

			var persons = em.createQuery( "select p from Person p", Person.class )
					.setHint( "jakarta.persistence.fetchgraph", graph )
					.getResultList();

			em.close();

			var student = (Student) persons.stream().filter( p -> p instanceof Student ).findFirst().get();

			var isStudentCoursesLoaded = persistenceUnitUtil.isLoaded( student, "courses" );
			var isStudentSchoolLoaded = persistenceUnitUtil.isLoaded( student, "school" );


			assertThat( isStudentCoursesLoaded ).isTrue();
			assertThat( isStudentSchoolLoaded ).isTrue();

			var payingCourse = (PayingCourse) student.getCourses()
					.stream()
					.filter( p -> p instanceof PayingCourse )
					.findFirst()
					.get();

			var isPayingCourseMoneyReceiverLoaded = persistenceUnitUtil.isLoaded( payingCourse, "moneyReceiver" );

			assertThat( isPayingCourseMoneyReceiverLoaded ).isTrue();
		}
	}

	@Nested
	class WithRepository {

		@Test
		public void fetch_person_with_subclass_specifics_attributes() {
			PersistenceUnitUtil persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();

			var persons = personJpaRepository.findAll();

			var student = (Student) persons.stream().filter( p -> p instanceof Student ).findFirst().get();

			var isStudentCoursesLoaded = persistenceUnitUtil.isLoaded( student, "courses" );
			var isStudentSchoolLoaded = persistenceUnitUtil.isLoaded( student, "school" );


			assertThat( isStudentCoursesLoaded ).isTrue();
			assertThat( isStudentSchoolLoaded ).isTrue();

			var payingCourse = (PayingCourse) student.getCourses()
					.stream()
					.filter( p -> p instanceof PayingCourse )
					.findFirst()
					.get();

			var isPayingCourseMoneyReceiverLoaded = persistenceUnitUtil.isLoaded( payingCourse, "moneyReceiver" );

			assertThat( isPayingCourseMoneyReceiverLoaded ).isTrue();
		}
	}


}
