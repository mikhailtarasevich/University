package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.UserRequest;
import com.mikhail.tarasevich.university.dto.UserResponse;
import com.mikhail.tarasevich.university.entity.User;
import com.mikhail.tarasevich.university.repository.RoleRepository;
import com.mikhail.tarasevich.university.repository.UserRepository;
import com.mikhail.tarasevich.university.service.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.UserMapper;
import com.mikhail.tarasevich.university.service.UserService;
import com.mikhail.tarasevich.university.service.validator.UserValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Log4j2
public abstract class AbstractUserPageableService<D extends UserRepository<U>, REQUEST extends UserRequest,
        RESPONSE extends UserResponse, U extends User>
        extends AbstractPageableService implements UserService<REQUEST, RESPONSE> {

    protected final D userRepository;
    protected final RoleRepository roleRepository;
    protected final PasswordEncoder passwordEncoder;
    protected final UserMapper<REQUEST, RESPONSE, U> userMapper;
    protected final UserValidator<REQUEST> validator;

    protected AbstractUserPageableService(D userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                                          UserMapper<REQUEST, RESPONSE, U> userMapper,
                                          UserValidator<REQUEST> validator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
            U userEntity = userRepository.save(userMapper.toEntity(request));
            if (request instanceof StudentRequest) roleRepository.addRoleForUser(userEntity.getId(), 3);
            if (request instanceof TeacherRequest) roleRepository.addRoleForUser(userEntity.getId(), 2);
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

        userRepository.saveAll(acceptableRequests.stream()
                .map(userMapper::toEntity)
                .collect(Collectors.toList()));

        log.info("The users were saved in the database. Saved courses: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public RESPONSE findById(int id) {
        Optional<RESPONSE> foundUser = userRepository.findById(id).map(userMapper::toResponse);

        if (foundUser.isPresent()) {
            return foundUser.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The user with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RESPONSE> findAll(String page) {
        final long itemsCount = userRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return userRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RESPONSE> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(REQUEST request) {
        validator.validateUserNameNotNullOrEmpty(request);
        validator.validateEmail(request);
        validator.validatePassword(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(userMapper.toEntity(request));
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

        userRepository.saveAll(
                acceptableRequests.stream()
                        .map(userMapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void editGeneralUserInfo(REQUEST request) {
        validator.validateUserNameNotNullOrEmpty(request);
        validator.validateEmail(request);
        User e = userMapper.toEntity(request);
        userRepository.updateGeneralInfo(e.getId(), e.getFirstName(), e.getLastName(), e.getGender(), e.getEmail());
    }

    @Override
    public void editPassword(REQUEST request) {
        validator.validatePassword(request);

        userRepository.updateUserPassword(request.getId(), passwordEncoder.encode(request.getPassword()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean login(String email, String password) {
        Optional<U> user = userRepository.findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    protected boolean checkEmail(REQUEST request) {
        Optional<U> user = userRepository.findByEmail(request.getEmail());
        return !user.isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) userRepository.count() / ITEMS_PER_PAGE);
    }

}
