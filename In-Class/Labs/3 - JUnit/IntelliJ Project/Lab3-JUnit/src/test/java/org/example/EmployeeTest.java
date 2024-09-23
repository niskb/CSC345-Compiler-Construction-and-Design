package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void getName() {
        // Arrange
        Employee e = new Employee();
        String testName = "Employee";

        // Act
        e.setName(testName);

        // Assert
        assertTrue(e.getName().equals(testName));
    }

    @Test
    void setName() {
        // Arrange
        Employee e = new Employee();
        String testName = "Employee";

        // Act
        e.setName(testName);

        // Assert
        assertEquals(testName, e.getName());
    }

    @Test
    void getId() {
        // Arrange
        Employee e = new Employee();
        int testId = 1;

        // Act
        e.setId(testId);

        // Assert
        assertEquals(testId, e.getId());
    }

    @Test
    void setId() {
        // Arrange
        Employee e = new Employee();
        int testId = 1;

        // Act
        e.setId(testId);

        // Assert
        assertTrue(e.getId() == testId);
    }

    @Test
    void getSalary() {
        // Arrange
        Employee e = new Employee();
        double testSalary = 99;

        // Act
        e.setSalary(testSalary);

        // Assert
        assertEquals(testSalary, e.getSalary());
    }

    @Test
    void setSalary() {
        // Arrange
        Employee e = new Employee();
        double testSalary = 1;

        // Act
        e.setSalary(testSalary);

        // Assert
        assertTrue(Math.abs(testSalary-e.getSalary()) <= 0.000001);
    }
}