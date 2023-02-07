package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.LessonMapper;
import com.mikhail.tarasevich.university.service.LessonService;
import com.mikhail.tarasevich.university.validator.LessonValidator;
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
public class LessonServiceImpl extends AbstractPageableService implements LessonService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonDao lessonDao;
    private final LessonMapper mapper;
    private final LessonValidator validator;

    @Autowired
    public LessonServiceImpl(LessonDao lessonDao, LessonMapper mapper, LessonValidator validator) {
        this.lessonDao = lessonDao;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public LessonResponse register(LessonRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(lessonDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<LessonRequest> requests) {
        List<LessonRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The lesson were deleted from the save list. The lesson: {} .", r);
            }
        });

        lessonDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        LOG.info("The lessons were saved in the database. Saved lessons: {} .", acceptableRequests);
    }

    @Override
    public Optional<LessonResponse> findById(int id) {
        return lessonDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<LessonResponse> findAll(String page) {
        final long itemsCount = lessonDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        lessonDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<LessonRequest> requests) {
        List<LessonRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The lesson was deleted from the update list. The lesson: {} .", r);
            }
        });

        lessonDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Lesson> optionalCourseEntities = lessonDao.findById(id);

        if (optionalCourseEntities.isPresent()) {
            return lessonDao.deleteById(id);
        } else {
            LOG.info("Delete was rejected. There is no lesson with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        boolean result = lessonDao.deleteByIds(ids);

        if (result) LOG.info("The lesson have been deleted. Deleted lessons: {}", ids);

        return result;
    }

    @Override
    public List<LessonResponse> findLessonsRelateToGroup(int groupId) {
        return lessonDao.findLessonsRelateToGroup(groupId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}
