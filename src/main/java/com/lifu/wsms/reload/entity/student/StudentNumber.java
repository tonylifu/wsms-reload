package com.lifu.wsms.reload.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_numbers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_numbers_sequence")
    @SequenceGenerator(name = "student_numbers_sequence", sequenceName = "student_numbers_id_sequence",
            allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private int currentYear;

    @Column(nullable = false)
    private int studentNumber;

//    @Column(nullable = false, unique = true)
//    private String studentIdPostfix;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private String actionBy;
}