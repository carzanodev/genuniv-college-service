package carzanodev.genuniv.microservices.college.api.classroom;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.SourceInfo;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@RestController
@RequestMapping("/api/v1/classroom")
class ClassroomController {

    private final ClassroomService classroomService;

    @Autowired
    ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<ClassroomDTO.List> getClassroom() {
        return classroomService.getAllClassrooms(true);
    }

    @GetMapping(path = "{id}")
    StandardResponse<ClassroomDTO> getClassroomById(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return classroomService.getClassroomById(id, true);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getClassroomInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return classroomService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<ClassroomDTO> postClassroom(@Valid @NonNull @RequestBody ClassroomDTO classroomDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return classroomService.addClassroom(classroomDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<ClassroomDTO> putClassroom(@PathVariable("id") int id,
                                                @Valid @NonNull @RequestBody ClassroomDTO classroomDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return classroomService.updateClassroom(id, classroomDto, true);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<ClassroomDTO> deleteClassroom(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return classroomService.deleteClassroom(id);
    }

}
