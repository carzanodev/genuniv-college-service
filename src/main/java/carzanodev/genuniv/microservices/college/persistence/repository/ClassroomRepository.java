package carzanodev.genuniv.microservices.college.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.college.persistence.entity.Classroom;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer>, InformationRepository {

    @Query("SELECT c FROM Classroom c WHERE c.isActive = TRUE")
    Set<Classroom> findAllActive();

    @Query("SELECT c FROM Classroom c WHERE c.id = :id AND c.isActive = TRUE")
    Optional<Classroom> findActiveById(int id);

}
