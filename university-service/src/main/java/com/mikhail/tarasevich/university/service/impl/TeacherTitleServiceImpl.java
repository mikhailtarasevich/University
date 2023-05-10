package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.repository.TeacherTitleRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.TeacherTitleMapper;
import com.mikhail.tarasevich.university.service.TeacherTitleService;
import com.mikhail.tarasevich.university.service.validator.TeacherTitleValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
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
public class TeacherTitleServiceImpl extends AbstractPageableService implements TeacherTitleService {

    private final TeacherTitleRepository teacherTitleRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherTitleMapper mapper;
    private final TeacherTitleValidator validator;

    @Override
    public TeacherTitleResponse register(TeacherTitleRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(teacherTitleRepository.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<TeacherTitleRequest> requests) {
        List<TeacherTitleRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The teacher title were deleted from the save list. The teacher title: {} .", r);
            }
        });

        teacherTitleRepository.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The teacher titles were saved in the database. Saved teacher titles: {} .",
                acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherTitleResponse findById(int id) {
        Optional<TeacherTitleResponse> foundTeacherTitle = teacherTitleRepository.findById(id).map(mapper::toResponse);

        if (foundTeacherTitle.isPresent()) {
            return foundTeacherTitle.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The teacher title with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherTitleResponse> findAll(String page) {
        final long itemsCount = teacherTitleRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return teacherTitleRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE)).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherTitleResponse> findAll() {
        return teacherTitleRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(TeacherTitleRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        teacherTitleRepository.save(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<TeacherTitleRequest> requests) {
        List<TeacherTitleRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The teacher title was deleted from the update list. The teacher title: {} .", r);
            }
        });

        teacherTitleRepository.saveAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<TeacherTitle> optionalCourseEntities = teacherTitleRepository.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            teacherTitleRepository.deleteById(id);

            return true;
        } else {
            log.info("Delete was rejected. There is no teacher title with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        teacherTitleRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    public int lastPageNumber() {
        return (int) Math.ceil((double) teacherTitleRepository.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        teacherRepository.unbindTeachersFromTeacherTitle(id);
    }

}
