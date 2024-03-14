package com.lifu.wsms.reload.mapper;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.entity.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "currentGrade", ignore = true)
//    @Mapping(target = "isDisabled", ignore = true)
    @Mapping(target = "disabilityDetail", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdateAt", ignore = true)
    @Mapping(target = "dob", source = "dob", dateFormat = "yyyyMMdd", qualifiedByName = "longToLocalDate")
    Student toStudent(CreateStudentRequest createStudentRequest);

    @Named("longToLocalDate")
    default LocalDate mapLongToLocalDate(long dob) {
        return AppUtil.convertLongToLocalDate(dob);
    }
}