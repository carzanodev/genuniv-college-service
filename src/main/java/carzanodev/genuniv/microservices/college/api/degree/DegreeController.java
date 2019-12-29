package carzanodev.genuniv.microservices.college.api.degree;

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
@RequestMapping("/api/v1/degree")
class DegreeController {

    private final DegreeService degreeService;

    @Autowired
    DegreeController(DegreeService degreeService) {
        this.degreeService = degreeService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<DegreeDTO.List> getDegree() {
        return degreeService.getAllDegrees(true);
    }

    @GetMapping(path = "{id}")
    StandardResponse<DegreeDTO> getDegreeById(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return degreeService.getDegreeById(id, true);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getDegreeInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return degreeService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<DegreeDTO> postDegree(@Valid @NonNull @RequestBody DegreeDTO degreeDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return degreeService.addDegree(degreeDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<DegreeDTO> putDegree(@PathVariable("id") int id,
                                          @Valid @NonNull @RequestBody DegreeDTO degreeDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return degreeService.updateDegree(id, degreeDto, true);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<DegreeDTO> deleteDegree(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return degreeService.deleteDegree(id);
    }

}
