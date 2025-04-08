package org.example.pzris1.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private Integer student_group;
    private String paid_form;

    public void setStudentGroup(Integer student_group) {
        this.student_group = student_group;
    }

    public void setPaidForm(String paid_form) {
        this.paid_form = paid_form;
    }


}
