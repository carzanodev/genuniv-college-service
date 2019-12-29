package carzanodev.genuniv.microservices.college.api.college;

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
@RequestMapping("/api/v1/college")
class CollegeController {

    private final CollegeService collegeService;

    @Autowired
    CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<CollegeDTO.List> getCollege() {
        return collegeService.getAllColleges(true);
    }

    @GetMapping(path = "{id}")
    StandardResponse<CollegeDTO> getCollegeById(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return collegeService.getCollegeById(id, true);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getCollegeInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return collegeService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<CollegeDTO> postCollege(@Valid @NonNull @RequestBody CollegeDTO collegeDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return collegeService.addCollege(collegeDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<CollegeDTO> putCollege(@PathVariable("id") int id,
                                            @Valid @NonNull @RequestBody CollegeDTO collegeDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return collegeService.updateCollege(id, collegeDto, true);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<CollegeDTO> deleteCollege(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return collegeService.deleteCollege(id);
    }

}
