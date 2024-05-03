package com.example.hibernate.entity;


import java.util.Objects;

import jakarta.persistence.*;


@NamedEntityGraph(
		name = "graph_that_take_heritage_into_consideration",
		subgraphs = {
				@NamedSubgraph(name = "course_sub_graph", type = PayingCourse.class, attributeNodes = {
						@NamedAttributeNode("moneyReceiver")
				})
		},
		subclassSubgraphs = {
				@NamedSubgraph(
						name = "notUsed",
						type = Student.class,
						attributeNodes = {
								@NamedAttributeNode(value = "school"),
								@NamedAttributeNode(value = "courses", subgraph = "course_sub_graph")
						}
				)
		}
)
@Entity(name = "Person")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String firstname;
	String lastname;

	public Person() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Person person = (Person) o;
		return Objects.equals( getId(), person.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}
}



