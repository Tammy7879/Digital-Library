package org.gfg.minor1.request;

import lombok.*;
import org.gfg.minor1.models.Student;
import org.gfg.minor1.models.StudentType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateStudentRequest {
    private String name;

    private String address;

    private String contact;

    private String email;

    public Student to() {
        return Student.builder().
                name(this.name).
                email(this.email).
                contact(this.contact).
                studentType(StudentType.ACTIVE).
                address(this.address).
                build();
    }
}
