package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.entity.User;

public interface UserMapper<REQUEST, RESPONSE, U extends User> extends Mapper<REQUEST, RESPONSE, U> {

}
