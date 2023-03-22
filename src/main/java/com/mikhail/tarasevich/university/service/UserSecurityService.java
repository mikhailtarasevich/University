package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.UserRequest;

public interface UserSecurityService<REQUEST extends UserRequest>{

    void register(REQUEST requestDTO);

}
