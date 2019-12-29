package carzanodev.genuniv.microservices.college.api.offering;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carzanodev.genuniv.microservices.college.cache.model.FacultyDTO;
import carzanodev.genuniv.microservices.college.cache.model.ScheduleDTO;
import carzanodev.genuniv.microservices.college.cache.model.SchoolPeriodDTO;
import carzanodev.genuniv.microservices.college.cache.model.SchoolYearDTO;
import carzanodev.genuniv.microservices.college.persistence.entity.Classroom;
import carzanodev.genuniv.microservices.college.persistence.entity.Course;
import carzanodev.genuniv.microservices.college.persistence.entity.Offering;
import carzanodev.genuniv.microservices.college.persistence.repository.ClassroomRepository;
import carzanodev.genuniv.microservices.college.persistence.repository.CourseRepository;
import carzanodev.genuniv.microservices.college.persistence.repository.OfferingRepository;
import carzanodev.genuniv.microservices.common.api.service.InformationService;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.ResponseMeta;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

import static carzanodev.genuniv.microservices.college.cache.GeneralCacheContext.FACULTY;
import static carzanodev.genuniv.microservices.college.cache.GeneralCacheContext.SCHEDULE;
import static carzanodev.genuniv.microservices.college.cache.GeneralCacheContext.SCHOOL_PERIOD;
import static carzanodev.genuniv.microservices.college.cache.GeneralCacheContext.SCHOOL_YEAR;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.CREATE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.DELETE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.LIST_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.RETRIEVE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.UPDATE_MSG;

@Service
class OfferingService extends InformationService {

    private final OfferingRepository offeringRepo;
    private final CourseRepository courseRepo;
    private final ClassroomRepository classroomRepo;

    @Autowired
    OfferingService(OfferingRepository offeringRepo, CourseRepository courseRepo, ClassroomRepository classroomRepo) {
        this.offeringRepo = offeringRepo;
        this.courseRepo = courseRepo;
        this.classroomRepo = classroomRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return offeringRepo;
    }

    StandardResponse<OfferingDTO.List> getAllOfferings(boolean isActiveOnly) {
        List<OfferingDTO> offeringDtos = (isActiveOnly ? offeringRepo.findAllActive() : offeringRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        OfferingDTO.List response = new OfferingDTO.List(offeringDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(offeringDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<OfferingDTO> getOfferingById(long id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Offering> offering = isActiveOnly ? offeringRepo.findActiveById(id) : offeringRepo.findById(id);

        if (offering.isEmpty()) {
            throw new InvalidTargetEntityException("offering", String.valueOf(id));
        }

        OfferingDTO response = entityToDto(offering.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<OfferingDTO> addOffering(OfferingDTO offeringDto) throws InvalidTargetEntityException, NonEmptyException, InvalidReferenceValueException {
        Offering saved = offeringRepo.save(dtoToNewEntity(offeringDto));

        OfferingDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<OfferingDTO> updateOffering(long id, OfferingDTO offeringDto, boolean isActiveOnly) throws InvalidTargetEntityException, NonEmptyException, InvalidReferenceValueException {
        Offering saved = offeringRepo.save(dtoToUpdatedEntity(id, offeringDto, isActiveOnly));

        OfferingDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<OfferingDTO> deleteOffering(long id) throws InvalidTargetEntityException {
        Optional<Offering> offeringFromDb = offeringRepo.findById(id);

        if (offeringFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("offering", String.valueOf(id));
        }

        Offering offering = offeringFromDb.get();
        offeringRepo.delete(offering);

        OfferingDTO response = entityToDto(offering);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private OfferingDTO entityToDto(Offering entity) {
        return new OfferingDTO(
                entity.getId(),
                entity.getCourse() == null ? 0 : entity.getCourse().getId(),
                entity.getScheduleId(),
                entity.getFacultyId(),
                entity.getClassroom() == null ? 0 : entity.getClassroom().getId(),
                entity.getCapacity(),
                entity.getSchoolPeriodId(),
                entity.getSchoolYearId());
    }

    private Offering dtoToNewEntity(OfferingDTO offeringDto) throws InvalidTargetEntityException, NonEmptyException, InvalidReferenceValueException {
        return dtoToUpdatedEntity(0, offeringDto, false);
    }

    private Offering dtoToUpdatedEntity(long id, OfferingDTO offeringDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Optional<Offering> c;
        if (id == 0) {
            c = Optional.of(new Offering());
        } else {
            c = isActiveOnly ? offeringRepo.findActiveById(id) : offeringRepo.findById(id);
        }

        if (c.isEmpty()) {
            throw new InvalidTargetEntityException("offering", String.valueOf(id));
        }

        Offering offering = c.get();

        long courseId = offeringDto.getCourseId();
        if (courseId > 0) {
            Optional<Course> course = courseRepo.findActiveById(courseId);

            if (course.isEmpty()) {
                throw new InvalidReferenceValueException("course_id", String.valueOf(courseId));
            }

            offering.setCourse(course.get());
        } else {
            throw new NonEmptyException("course_id");
        }

        int scheduleId = offeringDto.getScheduleId();
        if (scheduleId > 0) {
            Optional<ScheduleDTO> schedule = Optional.ofNullable(SCHEDULE.get(scheduleId));

            if (schedule.isEmpty()) {
                throw new InvalidReferenceValueException("schedule_id", String.valueOf(scheduleId));
            }

            offering.setScheduleId(scheduleId);
        } else {
            throw new NonEmptyException("schedule_id");
        }

        long facultyId = offeringDto.getFacultyId();
        if (facultyId > 0) {
            Optional<FacultyDTO> faculty = Optional.ofNullable(FACULTY.get(facultyId));

            if (faculty.isEmpty()) {
                throw new InvalidReferenceValueException("faculty_id", String.valueOf(facultyId));
            }

            offering.setFacultyId(facultyId);
        }

        int classroomId = offeringDto.getClassroomId();
        if (classroomId > 0) {
            Optional<Classroom> classroom = classroomRepo.findActiveById(classroomId);

            if (classroom.isEmpty()) {
                throw new InvalidReferenceValueException("classroom_id", String.valueOf(classroomId));
            }

            offering.setClassroom(classroom.get());
        }

        int capacity = offeringDto.getCapacity();
        offering.setCapacity(capacity);

        int schoolPeriodId = offeringDto.getSchoolPeriodId();
        if (schoolPeriodId > 0) {
            Optional<SchoolPeriodDTO> schoolPeriod = Optional.ofNullable(SCHOOL_PERIOD.get(schoolPeriodId));

            if (schoolPeriod.isEmpty()) {
                throw new InvalidReferenceValueException("school_period_id", String.valueOf(schoolPeriodId));
            }

            offering.setSchoolPeriodId(schoolPeriodId);
        } else {
            throw new NonEmptyException("school_period_id");
        }

        int schoolYearId = offeringDto.getSchoolYearId();
        if (schoolYearId > 0) {
            Optional<SchoolYearDTO> schoolYear = Optional.ofNullable(SCHOOL_YEAR.get(schoolYearId));

            if (schoolYear.isEmpty()) {
                throw new InvalidReferenceValueException("school_year_id", String.valueOf(schoolYearId));
            }

            offering.setSchoolYearId(schoolYearId);
        } else {
            throw new NonEmptyException("school_year_id");
        }

        return offering;
    }

}