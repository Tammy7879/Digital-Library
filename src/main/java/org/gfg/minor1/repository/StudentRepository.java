package org.gfg.minor1.repository;

import org.gfg.minor1.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByEmail(String email);
    List<Student> findByContact(String contact);
}
