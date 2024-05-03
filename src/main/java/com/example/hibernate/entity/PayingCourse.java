package com.example.hibernate.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Entity(name = "PayingCourse")
@DiscriminatorValue("paying")
public class PayingCourse extends Course {

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Teacher moneyReceiver;

	public PayingCourse() {
	}

	public Teacher getMoneyReceiver() {
		return moneyReceiver;
	}

	public void setMoneyReceiver(Teacher moneyReceiver) {
		this.moneyReceiver = moneyReceiver;
	}


	@Override
	public String toString() {
		return "PayingCourse{" +
				"moneyReceiver=" + moneyReceiver +
				"} " + super.toString();
	}
}
