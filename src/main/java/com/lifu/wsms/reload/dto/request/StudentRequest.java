package com.lifu.wsms.reload.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentRequest {
    private String studentId;

    public StudentRequest(String studentId) {
        this.studentId = studentId;
    }
}
