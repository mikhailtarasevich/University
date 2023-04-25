package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.dto.EducationFormResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.EducationFormMapper;
import com.mikhail.tarasevich.university.service.EducationFormService;
import com.mikhail.tarasevich.university.service.validator.EducationFormValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class EducationFormServiceImpl extends AbstractPageableService implements EducationFormService {

    private final EducationFormDao educationFormDao;
    private final GroupDao groupDao;
    private final EducationFormMapper mapper;
    private final EducationFormValidator validator;

    @Override
    public EducationFormResponse register(EducationFormRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(educationFormDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<EducationFormRequest> requests) {
        List<EducationFormRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The education forms were deleted from the save list. Request: {} .", r);
            }
        });

        educationFormDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The eduction forms were saved in the database. Saved education forms: {} .",
                acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public EducationFormResponse findById(int id) {
        Optional<EducationFormResponse> foundEducationForm = educationFormDao.findById(id).map(mapper::toResponse);

        if (foundEducationForm.isPresent()) {
            return foundEducationForm.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The education form with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationFormResponse> findAll(String page) {
        final long itemsCount = educationFormDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return educationFormDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationFormResponse> findAll() {
        return educationFormDao.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(EducationFormRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        educationFormDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<EducationFormRequest> requests) {
        List<EducationFormRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The education form was deleted from the update list. Education form: {} .", r);
            }
        });

        educationFormDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<EducationForm> optionalCourseEntities = educationFormDao.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            return educationFormDao.deleteById(id);
        } else {
            log.info("Delete was rejected. There is no education form with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = educationFormDao.deleteByIds(ids);

        if (result) log.info("Education forms have been deleted. Deleted courses: {}", ids);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) educationFormDao.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        groupDao.unbindGroupsFromEducationForm(id);
    }

}
