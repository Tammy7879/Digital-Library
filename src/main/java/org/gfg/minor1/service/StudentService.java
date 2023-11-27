package org.gfg.minor1.service;

import org.gfg.minor1.models.OperationType;
import org.gfg.minor1.models.Student;
import org.gfg.minor1.models.StudentFilterType;
import org.gfg.minor1.repository.StudentRepository;
import org.gfg.minor1.request.CreateStudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findStudent(StudentFilterType studentFilterType, String value, OperationType operationType) {

        switch (operationType) {
            case EQUALS:
                switch (studentFilterType) {
                    case EMAIL:
                        return studentRepository.findByEmail(value);
                    case CONTACT:
                        return studentRepository.findByContact(value);
                }
            default:
                return new ArrayList<>();
        }
    }

    public Student create(CreateStudentRequest createStudentRequest) {
        // check if the student already exist or not
        List<Student> students = findStudent(StudentFilterType.CONTACT, createStudentRequest.getContact() , OperationType.EQUALS );
        if(students == null || students.isEmpty()){
            Student student = createStudentRequest.to();
           return studentRepository.save(student);
        }
        return students.get(0);
    }
}
