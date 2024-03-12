package com.lifu.wsms.reload.mapper;

import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import com.lifu.wsms.reload.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentToStudentResponseMapper {

    StudentToStudentResponseMapper INSTANCE = Mappers.getMapper(StudentToStudentResponseMapper.class);

    @Mapping(target = "studentId", source = "studentId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "middleName", source = "middleName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "dob", source = "dob")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "address", source = "address")
    StudentResponse toStudentResponse(Student student);
}
