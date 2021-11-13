package com.example.Trade.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;

// Employee model class has basic employee-related attributes.
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private int age;
	private Double salary;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", age=" + age +
				", salary=" + salary +
				'}';
	}
}
