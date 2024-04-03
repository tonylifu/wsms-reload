package com.lifu.wsms.reload.mapper.student;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import com.lifu.wsms.reload.entity.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentToStudentResponseMapper {
    StudentToStudentResponseMapper INSTANCE = Mappers.getMapper(StudentToStudentResponseMapper.class);
    @Mapping(target = "dob", source = "dob", qualifiedByName = "longToLocalDateString")
    StudentResponse toStudentResponse(Student student);

    @Named("longToLocalDateString")
    default String mapLongToLocalDateString(long dob) {
        return String.valueOf(AppUtil.convertLongToLocalDate(dob));
    }
}
