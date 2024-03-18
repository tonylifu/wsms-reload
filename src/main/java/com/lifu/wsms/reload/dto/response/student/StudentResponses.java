package com.lifu.wsms.reload.dto.response.student;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class StudentResponses {
    private List<StudentResponse> students;
    private Long totalCount;
}
