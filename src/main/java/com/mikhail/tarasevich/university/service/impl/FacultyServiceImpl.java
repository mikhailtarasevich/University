package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.*;
import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.dto.FacultyResponse;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.FacultyMapper;
import com.mikhail.tarasevich.university.service.FacultyService;
import com.mikhail.tarasevich.university.validator.FacultyValidator;
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
public class FacultyServiceImpl extends AbstractPageableService implements FacultyService {

    private static final Logger LOG = LoggerFactory.getLogger(FacultyServiceImpl.class);

    private final FacultyDao facultyDao;
    private final GroupDao groupDao;
    private final FacultyMapper mapper;
    private final FacultyValidator validator;

    @Autowired
    public FacultyServiceImpl(FacultyDao facultyDao, GroupDao groupDao,
                              FacultyMapper mapper,
                              FacultyValidator validator) {
        this.facultyDao = facultyDao;
        this.groupDao = groupDao;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public FacultyResponse register(FacultyRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(facultyDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<FacultyRequest> requests) {
        List<FacultyRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The faculty were deleted from the save list. Request: {} .", r);
            }
        });

        facultyDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        LOG.info("Faculties were saved in the database. Saved faculties: {} .", acceptableRequests);
    }

    @Override
    public Optional<FacultyResponse> findById(int id) {
        return facultyDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<FacultyResponse> findAll(String page) {
        final long itemsCount = facultyDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return facultyDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FacultyRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        facultyDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<FacultyRequest> requests) {
        List<FacultyRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The faculty was deleted from the update list. Request: {} .", r);
            }
        });

        facultyDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Faculty> optionalCourseEntities = facultyDao.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            return facultyDao.deleteById(id);
        } else {
            LOG.info("Delete was rejected. There is no faculty with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = facultyDao.deleteByIds(ids);

        if (result) LOG.info("The faculties have been deleted. Deleted faculties: {}", ids);

        return result;
    }

    private void unbindDependenciesBeforeDelete(int id) {
        groupDao.unbindGroupsFromFaculty(id);
    }

}
