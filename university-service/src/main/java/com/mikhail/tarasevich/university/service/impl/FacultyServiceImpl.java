package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.dto.FacultyResponse;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.repository.FacultyRepository;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.FacultyMapper;
import com.mikhail.tarasevich.university.service.FacultyService;
import com.mikhail.tarasevich.university.service.validator.FacultyValidator;
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
public class FacultyServiceImpl extends AbstractPageableService implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final GroupRepository groupRepository;
    private final FacultyMapper mapper;
    private final FacultyValidator validator;

    @Override
    public FacultyResponse register(FacultyRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(facultyRepository.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<FacultyRequest> requests) {
        List<FacultyRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The faculty were deleted from the save list. Request: {} .", r);
            }
        });

        facultyRepository.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("Faculties were saved in the database. Saved faculties: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyResponse findById(int id) {
        Optional<FacultyResponse> foundFaculty = facultyRepository.findById(id).map(mapper::toResponse);

        if (foundFaculty.isPresent()) {
            return foundFaculty.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The faculty with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponse> findAll(String page) {
        final long itemsCount = facultyRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return facultyRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponse> findAll() {
        return facultyRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(FacultyRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        Faculty f = mapper.toEntity(r);
        facultyRepository.update(f.getId(), f.getName(), f.getDescription());
    }

    @Override
    public void editAll(List<FacultyRequest> requests) {
        List<FacultyRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The faculty was deleted from the update list. Request: {} .", r);
            }
        });

        facultyRepository.saveAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Faculty> optionalCourseEntities = facultyRepository.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            facultyRepository.deleteById(id);

            return true;
        } else {
            log.info("Delete was rejected. There is no faculty with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        facultyRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) facultyRepository.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        groupRepository.unbindGroupsFromFaculty(id);
    }

}
