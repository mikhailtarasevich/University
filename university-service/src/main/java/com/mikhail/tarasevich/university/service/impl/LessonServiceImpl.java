package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.LessonMapper;
import com.mikhail.tarasevich.university.service.LessonService;
import com.mikhail.tarasevich.university.service.validator.LessonValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final LessonRepository lessonRepository;
    private final LessonMapper mapper;
    private final LessonValidator validator;

    @Override
    public LessonResponse register(LessonRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);
        validator.validateStartTimeNotNull(r);

        return mapper.toResponse(lessonRepository.save(mapper.toEntity(r)));
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

        lessonRepository.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The lessons were saved in the database. Saved lessons: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse findById(int id) {
        Optional<LessonResponse> foundLesson = lessonRepository.findById(id).map(mapper::toResponse);

        if (foundLesson.isPresent()) {
            return foundLesson.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The lesson with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findAll(String page) {
        final long itemsCount = lessonRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE, Sort.by("startTime"))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        return lessonRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        validator.validateStartTimeNotNull(r);
        lessonRepository.save(mapper.toEntity(r));
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

        lessonRepository.saveAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Lesson> optionalCourseEntities = lessonRepository.findById(id);

        if (optionalCourseEntities.isPresent()) {
            lessonRepository.deleteById(id);
            return true;
        } else {
            log.info("Delete was rejected. There is no lesson with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        lessonRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findLessonsRelateToGroup(int groupId) {
        return lessonRepository.findLessonByGroupId(groupId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) lessonRepository.count() / ITEMS_PER_PAGE);
    }

}
