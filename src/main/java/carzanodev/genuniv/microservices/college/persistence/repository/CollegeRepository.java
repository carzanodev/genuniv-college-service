package carzanodev.genuniv.microservices.college.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.college.persistence.entity.College;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

@Repository
public interface CollegeRepository extends JpaRepository<College, Integer>, InformationRepository {

    @Query("SELECT c FROM College c WHERE c.isActive = TRUE")
    Set<College> findAllActive();

    @Query("SELECT c FROM College c WHERE c.id = :id AND c.isActive = TRUE")
    Optional<College> findActiveById(int id);

}
