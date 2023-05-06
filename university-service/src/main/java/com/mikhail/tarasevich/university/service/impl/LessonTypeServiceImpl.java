package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.dto.LessonTypeResponse;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.repository.LessonTypeRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.LessonTypeMapper;
import com.mikhail.tarasevich.university.service.LessonTypeService;
import com.mikhail.tarasevich.university.service.validator.LessonTypeValidator;
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
public class LessonTypeServiceImpl extends AbstractPageableService implements LessonTypeService {

    private final LessonTypeRepository lessonTypeRepository;
    private final LessonRepository lessonRepository;
    private final LessonTypeMapper mapper;
    private final LessonTypeValidator validator;

    @Override
    public LessonTypeResponse register(LessonTypeRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(lessonTypeRepository.save(mapper.toEntity(r)));
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

        lessonTypeRepository.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The lesson types were saved in the database. Saved lesson types: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonTypeResponse findById(int id) {
        Optional<LessonTypeResponse> foundLessonType = lessonTypeRepository.findById(id).map(mapper::toResponse);

        if (foundLessonType.isPresent()) {
            return foundLessonType.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The lesson type with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonTypeResponse> findAll(String page) {
        final long itemsCount = lessonTypeRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonTypeRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonTypeResponse> findAll() {
        return lessonTypeRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonTypeRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        lessonTypeRepository.save(mapper.toEntity(r));
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

        lessonTypeRepository.saveAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<LessonType> optionalCourseEntities = lessonTypeRepository.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            lessonTypeRepository.deleteById(id);

            return true;
        } else {
            log.info("Delete was rejected. There is no lesson type with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        lessonTypeRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) lessonTypeRepository.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        lessonRepository.unbindLessonsFromLessonType(id);
    }

}
