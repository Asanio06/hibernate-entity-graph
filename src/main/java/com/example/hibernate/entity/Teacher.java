package com.example.hibernate.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Teacher")
@DiscriminatorValue("teacher")
public class Teacher extends Person {

	public Teacher() {
	}
}