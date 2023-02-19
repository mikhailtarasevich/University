package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.dto.LessonTypeResponse;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.LessonTypeMapper;
import com.mikhail.tarasevich.university.service.LessonTypeService;
import com.mikhail.tarasevich.university.validator.LessonTypeValidator;
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
public class LessonTypeServiceImpl extends AbstractPageableService implements LessonTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonTypeServiceImpl.class);

    private final LessonTypeDao lessonTypeDao;
    private final LessonDao lessonDao;
    private final LessonTypeMapper mapper;
    private final LessonTypeValidator validator;

    @Autowired
    public LessonTypeServiceImpl(LessonTypeDao lessonTypeDao, LessonDao lessonDao, LessonTypeMapper mapper,
                                 LessonTypeValidator validator) {
        this.lessonTypeDao = lessonTypeDao;
        this.lessonDao = lessonDao;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public LessonTypeResponse register(LessonTypeRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(lessonTypeDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<LessonTypeRequest> requests) {
        List<LessonTypeRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The lesson type was deleted from the save list. The lesson type: {} .", r);
            }
        });

        lessonTypeDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        LOG.info("The lesson types were saved in the database. Saved lesson types: {} .", acceptableRequests);
    }

    @Override
    public Optional<LessonTypeResponse> findById(int id) {
        return lessonTypeDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<LessonTypeResponse> findAll(String page) {
        final long itemsCount = lessonTypeDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonTypeDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonTypeRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        lessonTypeDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<LessonTypeRequest> requests) {
        List<LessonTypeRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The lesson type was deleted from the update list. The lesson type: {} .", r);
            }
        });

        lessonTypeDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<LessonType> optionalCourseEntities = lessonTypeDao.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            return lessonTypeDao.deleteById(id);
        } else {
            LOG.info("Delete was rejected. There is no lesson type with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = lessonTypeDao.deleteByIds(ids);

        if (result) LOG.info("The lesson types have been deleted. Deleted lesson types: {}", ids);

        return result;
    }

    private void unbindDependenciesBeforeDelete(int id) {
        lessonDao.unbindLessonsFromLessonType(id);
    }

}
