package carzanodev.genuniv.microservices.college.api.course;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import carzanodev.genuniv.microservices.college.persistence.entity.Course;
import carzanodev.genuniv.microservices.college.persistence.util.CourseSpecs;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.SourceInfo;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@RestController
@RequestMapping("/api/v1/course")
class CourseController {

    private final CourseService courseService;

    @Autowired
    CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<CourseDTO.List> getCourse(@RequestParam(name = "code", defaultValue = "") String code,
                                               @RequestParam(name = "unit", defaultValue = "") String unit,
                                               @RequestParam(name = "lecturehours", defaultValue = "") String lectureHours,
                                               @RequestParam(name = "labhours", defaultValue = "") String labHours,
                                               @RequestParam(name = "collegeid", defaultValue = "") String collegeId,
                                               @RequestParam(name = "contain_name", defaultValue = "") String containName,
                                               @RequestParam(name = "contain_description", defaultValue = "") String containDescription) {
        Specification<Course> spec = CourseSpecs.createFullSpecification(code, unit, lectureHours, labHours, collegeId, containName, containDescription);
        return courseService.getAllCourseBySpecification(spec, true);
    }

    @GetMapping(path = "{id}")
    StandardResponse<CourseDTO> getCourse(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return courseService.getCourseById(id, true);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getCourseInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return courseService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<CourseDTO> postCourse(@Valid @NonNull @RequestBody CourseDTO courseDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return courseService.addCourse(courseDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<CourseDTO> putCourse(@PathVariable("id") long id,
                                          @Valid @NonNull @RequestBody CourseDTO courseDto) throws InvalidReferenceValueException, InvalidTargetEntityException, NonEmptyException {
        return courseService.updateCourse(id, courseDto, true);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<CourseDTO> deleteCourse(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return courseService.deleteCourse(id);
    }

}
