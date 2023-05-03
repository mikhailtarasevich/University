package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.LessonMapper;
import com.mikhail.tarasevich.university.service.LessonService;
import com.mikhail.tarasevich.university.service.validator.LessonValidator;
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
public class LessonServiceImpl extends AbstractPageableService implements LessonService {

    private final LessonDao lessonDao;
    private final LessonMapper mapper;
    private final LessonValidator validator;

    @Override
    public LessonResponse register(LessonRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);
        validator.validateStartTimeNotNull(r);

        return mapper.toResponse(lessonDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<LessonRequest> requests) {
        List<LessonRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                validator.validateStartTimeNotNull(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The lesson were deleted from the save list. The lesson: {} .", r);
            }
        });

        lessonDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The lessons were saved in the database. Saved lessons: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse findById(int id) {
        Optional<LessonResponse> foundLesson = lessonDao.findById(id).map(mapper::toResponse);

        if (foundLesson.isPresent()) {
            return foundLesson.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The lesson with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findAll(String page) {
        final long itemsCount = lessonDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        return lessonDao.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        validator.validateStartTimeNotNull(r);
        lessonDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<LessonRequest> requests) {
        List<LessonRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                validator.validateStartTimeNotNull(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The lesson was deleted from the update list. The lesson: {} .", r);
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
            log.info("Delete was rejected. There is no lesson with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        boolean result = lessonDao.deleteByIds(ids);

        if (result) log.info("The lesson have been deleted. Deleted lessons: {}", ids);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findLessonsRelateToGroup(int groupId) {
        return lessonDao.findLessonsRelateToGroup(groupId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) lessonDao.count() / ITEMS_PER_PAGE);
    }

}
