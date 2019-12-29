package carzanodev.genuniv.microservices.college.api.clasroom;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.college.persistence.entity.Classroom;
import carzanodev.genuniv.microservices.college.persistence.repository.ClassroomRepository;
import carzanodev.genuniv.microservices.common.api.service.InformationService;
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
class ClassroomService extends InformationService {

    private final ClassroomRepository classroomRepo;

    @Autowired
    ClassroomService(ClassroomRepository classroomRepo) {
        this.classroomRepo = classroomRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return classroomRepo;
    }

    StandardResponse<ClassroomDTO.List> getAllClassrooms(boolean isActiveOnly) {
        List<ClassroomDTO> classroomDtos = (isActiveOnly ? classroomRepo.findAllActive() : classroomRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        ClassroomDTO.List response = new ClassroomDTO.List(classroomDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(classroomDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<ClassroomDTO> getClassroomById(int id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Classroom> classroom = isActiveOnly ? classroomRepo.findActiveById(id) : classroomRepo.findById(id);

        if (classroom.isEmpty()) {
            throw new InvalidTargetEntityException("classroom", String.valueOf(id));
        }

        ClassroomDTO response = entityToDto(classroom.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<ClassroomDTO> addClassroom(ClassroomDTO classroomDto) throws InvalidTargetEntityException, NonEmptyException {
        Classroom saved = classroomRepo.save(dtoToNewEntity(classroomDto));

        ClassroomDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<ClassroomDTO> updateClassroom(int id, ClassroomDTO classroomDto, boolean isActiveOnly) throws InvalidTargetEntityException, NonEmptyException {
        Classroom saved = classroomRepo.save(dtoToUpdatedEntity(id, classroomDto, isActiveOnly));

        ClassroomDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<ClassroomDTO> deleteClassroom(int id) throws InvalidTargetEntityException {
        Optional<Classroom> classroomFromDb = classroomRepo.findById(id);

        if (classroomFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("classroom", String.valueOf(id));
        }

        Classroom classroom = classroomFromDb.get();
        classroomRepo.delete(classroom);

        ClassroomDTO response = entityToDto(classroom);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private ClassroomDTO entityToDto(Classroom entity) {
        return new ClassroomDTO(entity.getId(), entity.getName());
    }

    private Classroom dtoToNewEntity(ClassroomDTO classroomDto) throws InvalidTargetEntityException, NonEmptyException {
        return dtoToUpdatedEntity(0, classroomDto, false); // isActive here has no effect
    }

    private Classroom dtoToUpdatedEntity(int id, ClassroomDTO classroomDto, boolean isActiveOnly) throws InvalidTargetEntityException, NonEmptyException {
        Optional<Classroom> c;
        if (id == 0) {
            c = Optional.of(new Classroom());
        } else {
            c = isActiveOnly ? classroomRepo.findActiveById(id) : classroomRepo.findById(id);
        }

        if (c.isEmpty()) {
            throw new InvalidTargetEntityException("classroom", String.valueOf(id));
        }

        Classroom classroom = c.get();

        String name = classroomDto.getName();
        if (!StringUtils.isEmpty(name)) {
            classroom.setName(name);
        } else {
            throw new NonEmptyException("name");
        }

        return classroom;
    }

}