package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.TeacherTitleMapper;
import com.mikhail.tarasevich.university.service.TeacherTitleService;
import com.mikhail.tarasevich.university.validator.TeacherTitleValidator;
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
public class TeacherTitleServiceImpl extends AbstractPageableService implements TeacherTitleService {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherTitleServiceImpl.class);

    private final TeacherTitleDao teacherTitleDao;
    private final TeacherDao teacherDao;
    private final TeacherTitleMapper mapper;
    private final TeacherTitleValidator validator;

    @Autowired
    public TeacherTitleServiceImpl(TeacherTitleDao teacherTitleDao, TeacherDao teacherDao, TeacherTitleMapper mapper,
                                   TeacherTitleValidator validator) {
        this.teacherTitleDao = teacherTitleDao;
        this.teacherDao = teacherDao;
        this.mapper = mapper;
        this.validator = validator;
    }

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
            } catch (IncorrectRequestData e) {
                LOG.info("The teacher title were deleted from the save list. The teacher title: {} .", r);
            }
        });

        teacherTitleDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        LOG.info("The teacher titles were saved in the database. Saved teacher titles: {} .", acceptableRequests);
    }

    @Override
    public Optional<TeacherTitleResponse> findById(int id) {
        return teacherTitleDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<TeacherTitleResponse> findAll(String page) {
        final long itemsCount = teacherTitleDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return teacherTitleDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
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
            } catch (IncorrectRequestData e) {
                LOG.info("The teacher title was deleted from the update list. The teacher title: {} .", r);
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
            LOG.info("Delete was rejected. There is no teacher title with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = teacherTitleDao.deleteByIds(ids);

        if (result) LOG.info("The teacher titles have been deleted. Deleted teacher titles: {}", ids);

        return result;
    }

    private void unbindDependenciesBeforeDelete(int id) {
        teacherDao.unbindTeachersFromTeacherTitle(id);
    }

}
