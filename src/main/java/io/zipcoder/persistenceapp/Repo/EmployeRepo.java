package io.zipcoder.persistenceapp.Repo;

import io.zipcoder.persistenceapp.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepo extends JpaRepository<Employee, Integer> {
}
