package com.example.Trade.controller;

import com.example.Trade.model.Employee;
import com.example.Trade.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// In this class, we have left the caching approach for tutorial simplicity.
// If users which they can enabling caching in this application.
@RestController
@RequestMapping(value = "/api/redis/employee")
public class EmployeeController {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeService service;

	// Save a new employee.
	// Url - http://localhost:10091/api/redis/employee
	@PostMapping
	public String save(@RequestBody final Employee employee) {
		LOG.info("Saving the new employee to the redis.");
		System.out.println(employee);

		service.save(employee);
		System.out.println(employee);
		return "Successfully added. Employee with id= " + employee.getId();
	}

	// Get all employees.
	// Url - http://localhost:8080/api/redis/employee/getall
	@GetMapping("/getall")
	public Map<String, Employee> findAll() {
		LOG.info("Fetching all employees from the redis.");
		final Map<String, Employee> employeeMap = service.findAll();
		// Todo - If developers like they can sort the map (optional).
		return employeeMap;
	}

	// Get employee by id.
	// Url - http://localhost:8080/api/redis/employee/get/<employee_id>
	@GetMapping("/get/{id}")
	public Employee findById(@PathVariable("id") final String id) {
		LOG.info("Fetching employee with id= " + id);
		return service.findById(id);
	}

	// Delete employee by id.
	// Url - http://localhost:8080/api/redis/employee/delete/<employee_id>
	@DeleteMapping("/delete/{id}")
	public Map<String, Employee> delete(@PathVariable("id") final String id) {
		LOG.info("Deleting employee with id= " + id);
		// Deleting the employee.
		service.delete(id);
		// Returning the all employees (post the deleted one).
		return findAll();
	}
}
