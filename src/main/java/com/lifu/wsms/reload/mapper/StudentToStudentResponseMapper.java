package com.lifu.wsms.reload.mapper;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import com.lifu.wsms.reload.entity.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper
public interface StudentToStudentResponseMapper {
    StudentToStudentResponseMapper INSTANCE = Mappers.getMapper(StudentToStudentResponseMapper.class);
    //@Mapping(target = "dob", source = "dob", qualifiedByName = "localDateToLong")
    StudentResponse toStudentResponse(Student student);

//    @Named("localDateToLong")
//    default long mapLocalDateToLong(LocalDate dob) {
//        return AppUtil.convertLocalDateToLong(dob);
//    }
}
