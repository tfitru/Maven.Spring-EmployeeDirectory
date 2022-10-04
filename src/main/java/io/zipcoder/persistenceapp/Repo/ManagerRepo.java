package io.zipcoder.persistenceapp.Repo;

import io.zipcoder.persistenceapp.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Integer> {
}
