package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.UserDao;
import com.mikhail.tarasevich.university.dto.UserRequest;
import com.mikhail.tarasevich.university.dto.UserResponse;
import com.mikhail.tarasevich.university.entity.User;
import com.mikhail.tarasevich.university.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.UserMapper;
import com.mikhail.tarasevich.university.service.UserService;
import com.mikhail.tarasevich.university.validator.UserValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public abstract class AbstractUserPageableService<D extends UserDao<U>, REQUEST extends UserRequest,
        RESPONSE extends UserResponse, U extends User>
        extends AbstractPageableService implements UserService<REQUEST, RESPONSE> {

    protected final D userDao;
    protected final PasswordEncoder passwordEncoder;
    protected final UserMapper<REQUEST, RESPONSE, U> userMapper;
    protected final UserValidator<REQUEST> validator;

    protected AbstractUserPageableService(D userDao, PasswordEncoder passwordEncoder,
                                          UserMapper<REQUEST, RESPONSE, U> userMapper,
                                          UserValidator<REQUEST> validator) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Override
    public RESPONSE register(REQUEST request) {
        if (checkEmail(request)) {
            validator.validateUserNameNotNullOrEmpty(request);
            validator.validateEmail(request);
            validator.validatePassword(request);
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            U userEntity = userDao.save(userMapper.toEntity(request));
            return userMapper.toResponse(userEntity);
        } else {
            throw new EmailAlreadyExistsException("The user with the same email already in the database.");
        }
    }

    @Override
    public void registerAll(List<REQUEST> requests) {
        List<REQUEST> acceptableRequests = new ArrayList<>();

        requests.forEach(u -> {
            if (checkEmail(u)) {
                try {
                    validator.validateUserNameNotNullOrEmpty(u);
                    validator.validateEmail(u);
                    validator.validatePassword(u);
                    u.setPassword(passwordEncoder.encode(u.getPassword()));
                    acceptableRequests.add(u);
                } catch (IncorrectRequestDataException e) {
                    log.info("The users were deleted from the save list. User: {} .", u);
                }
            }
        });

        userDao.saveAll(acceptableRequests.stream()
                .map(userMapper::toEntity)
                .collect(Collectors.toList()));

        log.info("The users were saved in the database. Saved courses: {} .", acceptableRequests);
    }

    @Override
    public RESPONSE findById(int id) {
        Optional<RESPONSE> foundUser = userDao.findById(id).map(userMapper::toResponse);

        if (foundUser.isPresent()) {
            return foundUser.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The user with specified id doesn't exist in the database.");
        }
    }

    @Override
    public List<RESPONSE> findAll(String page) {
        final long itemsCount = userDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return userDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RESPONSE> findAll() {
        return userDao.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(REQUEST request) {
        validator.validateUserNameNotNullOrEmpty(request);
        validator.validateEmail(request);
        validator.validatePassword(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        userDao.update(userMapper.toEntity(request));
    }

    @Override
    public void editAll(List<REQUEST> requests) {
        List<REQUEST> acceptableRequests = new ArrayList<>();

        requests.forEach(u -> {
            try {
                validator.validateUserNameNotNullOrEmpty(u);
                validator.validateEmail(u);
                validator.validatePassword(u);
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                acceptableRequests.add(u);
            } catch (IncorrectRequestDataException e) {
                log.info("The users were deleted from the update list. User: {} .", u);
            }
        });

        userDao.updateAll(
                acceptableRequests.stream()
                        .map(userMapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void editGeneralUserInfo(REQUEST request) {
        validator.validateUserNameNotNullOrEmpty(request);
        validator.validateEmail(request);
        userDao.updateGeneralUserInfo(userMapper.toEntity(request));
    }

    @Override
    public void editPassword(REQUEST request) {
        validator.validatePassword(request);

        userDao.updateUserPassword(request.getId(), passwordEncoder.encode(request.getPassword()));
    }

    @Override
    public boolean login(String email, String password) {
        Optional<U> user = userDao.findByName(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    @Override
    public int lastPageNumber(){
        return (int) Math.ceil((double)userDao.count() / ITEMS_PER_PAGE);
    }

    protected boolean checkEmail(REQUEST request) {
        Optional<U> user = userDao.findByName(request.getEmail());
        return !user.isPresent();
    }

}
