package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.UserDao;
import com.mikhail.tarasevich.university.dto.UserRequest;
import com.mikhail.tarasevich.university.dto.UserResponse;
import com.mikhail.tarasevich.university.entity.User;
import com.mikhail.tarasevich.university.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.UserMapper;
import com.mikhail.tarasevich.university.service.UserService;
import com.mikhail.tarasevich.university.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractUserPageableService<D extends UserDao<U>, REQUEST extends UserRequest,
        RESPONSE extends UserResponse, U extends User>
        extends AbstractPageableService implements UserService<REQUEST, RESPONSE> {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractUserPageableService.class);

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
                    u.setPassword(passwordEncoder.encode(u.getPassword()));
                    acceptableRequests.add(u);
                } catch (IncorrectRequestData e) {
                    LOG.info("The users were deleted from the save list. User: {} .", u);
                }
            }
        });

        userDao.saveAll(acceptableRequests.stream()
                .map(userMapper::toEntity)
                .collect(Collectors.toList()));

        LOG.info("The users were saved in the database. Saved courses: {} .", acceptableRequests);
    }

    @Override
    public Optional<RESPONSE> findById(int id) {
        return userDao.findById(id).map(userMapper::toResponse);
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
    public void edit(REQUEST request) {
        validator.validateUserNameNotNullOrEmpty(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userDao.update(userMapper.toEntity(request));
    }

    @Override
    public void editAll(List<REQUEST> requests) {
        List<REQUEST> acceptableRequests = new ArrayList<>();

        requests.forEach(u -> {
            try {
                validator.validateUserNameNotNullOrEmpty(u);
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                acceptableRequests.add(u);
            } catch (IncorrectRequestData e) {
                LOG.info("The users were deleted from the update list. User: {} .", u);
            }
        });

        userDao.updateAll(
                acceptableRequests.stream()
                        .map(userMapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean login(String email, String password) {
        Optional<U> user = userDao.findByName(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    protected boolean checkEmail(REQUEST request) {
        Optional<U> user = userDao.findByName(request.getEmail());
        return !user.isPresent();
    }

}
