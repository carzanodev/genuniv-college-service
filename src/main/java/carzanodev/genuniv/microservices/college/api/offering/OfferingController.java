package carzanodev.genuniv.microservices.college.api.offering;

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
@RequestMapping("/api/v1/offering")
class OfferingController {

    private final OfferingService offeringService;

    @Autowired
    OfferingController(OfferingService offeringService) {
        this.offeringService = offeringService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<OfferingDTO.List> getOffering() {
        return offeringService.getAllOfferings(true);
    }

    @GetMapping(path = "{id}")
    StandardResponse<OfferingDTO> getCollegeById(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return offeringService.getOfferingById(id, true);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getOfferingInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return offeringService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<OfferingDTO> postOffering(@Valid @NonNull @RequestBody OfferingDTO offeringDto) throws InvalidTargetEntityException, NonEmptyException, InvalidReferenceValueException {
        return offeringService.addOffering(offeringDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<OfferingDTO> putOffering(@PathVariable("id") int id,
                                              @Valid @NonNull @RequestBody OfferingDTO offeringDto) throws InvalidTargetEntityException, NonEmptyException, InvalidReferenceValueException {
        return offeringService.updateOffering(id, offeringDto, true);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<OfferingDTO> deleteOffering(@PathVariable("id") int id) throws InvalidTargetEntityException {
        return offeringService.deleteOffering(id);
    }

}
