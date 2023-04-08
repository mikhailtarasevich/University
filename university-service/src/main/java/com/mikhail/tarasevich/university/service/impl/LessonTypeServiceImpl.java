package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.dto.LessonTypeResponse;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.LessonTypeMapper;
import com.mikhail.tarasevich.university.service.LessonTypeService;
import com.mikhail.tarasevich.university.service.validator.LessonTypeValidator;
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
public class LessonTypeServiceImpl extends AbstractPageableService implements LessonTypeService {

    private final LessonTypeDao lessonTypeDao;
    private final LessonDao lessonDao;
    private final LessonTypeMapper mapper;
    private final LessonTypeValidator validator;

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
            } catch (IncorrectRequestDataException e) {
                log.info("The lesson type was deleted from the save list. The lesson type: {} .", r);
            }
        });

        lessonTypeDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The lesson types were saved in the database. Saved lesson types: {} .", acceptableRequests);
    }

    @Override
    public LessonTypeResponse findById(int id) {
        Optional<LessonTypeResponse> foundLessonType = lessonTypeDao.findById(id).map(mapper::toResponse);

        if (foundLessonType.isPresent()) {
            return foundLessonType.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The lesson type with specified id doesn't exist in the database.");
        }
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
    public List<LessonTypeResponse> findAll() {
        return lessonTypeDao.findAll().stream()
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
            } catch (IncorrectRequestDataException e) {
                log.info("The lesson type was deleted from the update list. The lesson type: {} .", r);
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
            log.info("Delete was rejected. There is no lesson type with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = lessonTypeDao.deleteByIds(ids);

        if (result) log.info("The lesson types have been deleted. Deleted lesson types: {}", ids);

        return result;
    }

    @Override
    public int lastPageNumber() {
        return (int) Math.ceil((double) lessonTypeDao.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        lessonDao.unbindLessonsFromLessonType(id);
    }

}
