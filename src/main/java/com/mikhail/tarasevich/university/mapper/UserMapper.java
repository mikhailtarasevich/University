package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.User;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

@MapperConfig(imports = Gender.class)
public interface UserMapper<REQUEST, RESPONSE, U extends User> {

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withFirstName", source = "firstName")
    @Mapping(target = "withLastName", source = "lastName")
    @Mapping(target = "withGender", source = "gender")
    @Mapping(target = "withEmail", source = "email")
    @Mapping(target = "withPassword", source = "password")
    U toEntity(REQUEST request);

    RESPONSE toResponse(U user);

}