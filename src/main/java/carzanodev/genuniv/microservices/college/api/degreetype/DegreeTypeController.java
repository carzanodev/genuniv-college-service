package carzanodev.genuniv.microservices.college.api.degreetype;

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

import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.SourceInfo;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@RestController
@RequestMapping("/api/v1/degree-type")
class DegreeTypeController {

    private final DegreeTypeService degreeTypeService;

    @Autowired
    DegreeTypeController(DegreeTypeService degreeTypeService) {
        this.degreeTypeService = degreeTypeService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<DegreeTypeDTO.List> getDegreeType() {
        return degreeTypeService.getAllDegreeTypes(true);
    }

    @GetMapping(path = "{id}")
    StandardResponse<DegreeTypeDTO> getCollegeById(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return degreeTypeService.getDegreeTypeById(id, true);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getDegreeTypeInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return degreeTypeService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<DegreeTypeDTO> postDegreeType(@Valid @NonNull @RequestBody DegreeTypeDTO degreeTypeDto) throws InvalidTargetEntityException, NonEmptyException {
        return degreeTypeService.addDegreeType(degreeTypeDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<DegreeTypeDTO> putDegreeType(@PathVariable("id") int id,
                                                  @Valid @NonNull @RequestBody DegreeTypeDTO degreeTypeDto) throws InvalidTargetEntityException, NonEmptyException {
        return degreeTypeService.updateDegreeType(id, degreeTypeDto, true);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<DegreeTypeDTO> deleteDegreeType(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return degreeTypeService.deleteDegreeType(id);
    }

}
