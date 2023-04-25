package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.TeacherTitleMapper;
import com.mikhail.tarasevich.university.service.TeacherTitleService;
import com.mikhail.tarasevich.university.service.validator.TeacherTitleValidator;
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
public class TeacherTitleServiceImpl extends AbstractPageableService implements TeacherTitleService {

    private final TeacherTitleDao teacherTitleDao;
    private final TeacherDao teacherDao;
    private final TeacherTitleMapper mapper;
    private final TeacherTitleValidator validator;

    @Override
    public TeacherTitleResponse register(TeacherTitleRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(teacherTitleDao.save(mapper.toEntity(r)));
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

        teacherTitleDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The teacher titles were saved in the database. Saved teacher titles: {} .",
                acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherTitleResponse findById(int id) {
        Optional<TeacherTitleResponse> foundTeacherTitle = teacherTitleDao.findById(id).map(mapper::toResponse);

        if (foundTeacherTitle.isPresent()) {
            return foundTeacherTitle.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The teacher title with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherTitleResponse> findAll(String page) {
        final long itemsCount = teacherTitleDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return teacherTitleDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherTitleResponse> findAll() {
        return teacherTitleDao.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(TeacherTitleRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        teacherTitleDao.update(mapper.toEntity(r));
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

        teacherTitleDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<TeacherTitle> optionalCourseEntities = teacherTitleDao.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            return teacherTitleDao.deleteById(id);
        } else {
            log.info("Delete was rejected. There is no teacher title with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = teacherTitleDao.deleteByIds(ids);

        if (result) log.info("The teacher titles have been deleted. Deleted teacher titles: {}", ids);

        return result;
    }

    @Override
    public int lastPageNumber() {
        return (int) Math.ceil((double) teacherTitleDao.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        teacherDao.unbindTeachersFromTeacherTitle(id);
    }

}
