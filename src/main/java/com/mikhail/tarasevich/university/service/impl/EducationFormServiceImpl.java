package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.dto.EducationFormResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.EducationFormMapper;
import com.mikhail.tarasevich.university.service.EducationFormService;
import com.mikhail.tarasevich.university.validator.EducationFormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EducationFormServiceImpl extends AbstractPageableService implements EducationFormService {

    private static final Logger LOG = LoggerFactory.getLogger(EducationFormServiceImpl.class);

    private final EducationFormDao educationFormDao;
    private final GroupDao groupDao;
    private final EducationFormMapper mapper;
    private final EducationFormValidator validator;

    @Autowired
    public EducationFormServiceImpl(EducationFormDao educationFormDao, GroupDao groupDao,
                                    EducationFormMapper mapper, EducationFormValidator validator) {
        this.educationFormDao = educationFormDao;
        this.groupDao = groupDao;
        this.mapper = mapper;
        this.validator = validator;
    }

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
            } catch (IncorrectRequestData e) {
                LOG.info("The education forms were deleted from the save list. Request: {} .", r);
            }
        });

        educationFormDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        LOG.info("The eduction forms were saved in the database. Saved education forms: {} .", acceptableRequests);
    }

    @Override
    public Optional<EducationFormResponse> findById(int id) {
        return educationFormDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<EducationFormResponse> findAll(String page) {
        final long itemsCount = educationFormDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return educationFormDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
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
            } catch (IncorrectRequestData e) {
                LOG.info("The education form was deleted from the update list. Education form: {} .", r);
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
            LOG.info("Delete was rejected. There is no education form with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = educationFormDao.deleteByIds(ids);

        if (result) LOG.info("Education forms have been deleted. Deleted courses: {}", ids);

        return result;
    }

    private void unbindDependenciesBeforeDelete(int id) {
        groupDao.unbindGroupsFromEducationForm(id);
    }

}
