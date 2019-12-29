package carzanodev.genuniv.microservices.college.api.course;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.college.persistence.entity.College;
import carzanodev.genuniv.microservices.college.persistence.entity.Course;
import carzanodev.genuniv.microservices.college.persistence.repository.CollegeRepository;
import carzanodev.genuniv.microservices.college.persistence.repository.CourseRepository;
import carzanodev.genuniv.microservices.common.api.service.InformationService;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.ResponseMeta;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

import static carzanodev.genuniv.microservices.common.util.MetaMessage.CREATE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.DELETE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.LIST_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.RETRIEVE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.UPDATE_MSG;

@Service
class CourseService extends InformationService {

    private final CourseRepository courseRepo;
    private final CollegeRepository collegeRepo;

    @Autowired
    CourseService(CourseRepository courseRepo, CollegeRepository collegeRepo) {
        this.courseRepo = courseRepo;
        this.collegeRepo = collegeRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return courseRepo;
    }

    StandardResponse<CourseDTO.List> getAllCourseBySpecification(Specification<Course> courseSpec, boolean isActiveOnly) {
        List<CourseDTO> courseDtos = (isActiveOnly ? courseRepo.findAllActive(courseSpec) : courseRepo.findAll(courseSpec))
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        CourseDTO.List response = new CourseDTO.List(courseDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(courseDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CourseDTO> getCourseById(long id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Course> course = isActiveOnly ? courseRepo.findActiveById(id) : courseRepo.findById(id);

        if (course.isEmpty()) {
            throw new InvalidTargetEntityException("course", String.valueOf(id));
        }

        CourseDTO response = entityToDto(course.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CourseDTO> addCourse(CourseDTO courseDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Course saved = courseRepo.save(dtoToNewEntity(courseDto));

        CourseDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CourseDTO> updateCourse(long id, CourseDTO courseDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Course saved = courseRepo.save(dtoToUpdatedEntity(id, courseDto, isActiveOnly));

        CourseDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<CourseDTO> deleteCourse(long id) throws InvalidTargetEntityException {
        Optional<Course> courseFromDb = courseRepo.findById(id);

        if (courseFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("course", String.valueOf(id));
        }

        Course course = courseFromDb.get();
        courseRepo.delete(course);

        CourseDTO response = entityToDto(course);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private CourseDTO entityToDto(Course entity) {
        return new CourseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getUnit(),
                entity.getLectureHours(),
                entity.getLabHours(),
                entity.getCollege().getId());
    }

    Course dtoToNewEntity(CourseDTO courseDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return dtoToUpdatedEntity(0L, courseDto, false);
    }

    Course dtoToUpdatedEntity(long id, CourseDTO courseDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Optional<Course> c;
        if (id == 0) {
            c = Optional.of(new Course());
        } else {
            c = isActiveOnly ? courseRepo.findActiveById(id) : courseRepo.findById(id);
        }

        if (c.isEmpty()) {
            throw new InvalidTargetEntityException("college", String.valueOf(id));
        }

        Course course = c.get();

        String code = courseDto.getCode();
        if (!StringUtils.isEmpty(code)) {
            course.setCode(code);
        } else {
            throw new NonEmptyException("code");
        }

        String name = courseDto.getName();
        if (!StringUtils.isEmpty(name)) {
            course.setName(name);
        } else {
            throw new NonEmptyException("name");
        }

        String description = courseDto.getDescription();
        course.setDescription(description);

        double unit = courseDto.getUnit();
        course.setUnit(unit);

        int lectureHours = courseDto.getLectureHours();
        course.setLectureHours(lectureHours);

        int labHours = courseDto.getLabHours();
        course.setLabHours(labHours);

        int collegeId = courseDto.getCollegeId();
        if (collegeId > 0) {
            Optional<College> college = collegeRepo.findById(collegeId);

            if (college.isEmpty()) {
                throw new InvalidReferenceValueException("college_id", String.valueOf(collegeId));
            }

            course.setCollege(college.get());
        } else {
            throw new NonEmptyException("college_id");
        }

        return course;
    }

}
