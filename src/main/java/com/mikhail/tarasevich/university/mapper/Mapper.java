package com.mikhail.tarasevich.university.mapper;

public interface Mapper<REQUEST, RESPONSE, E> {

    E toEntity(REQUEST request);
    RESPONSE toResponse(E user);

}
