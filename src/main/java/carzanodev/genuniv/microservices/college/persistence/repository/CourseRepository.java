package carzanodev.genuniv.microservices.college.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.college.persistence.entity.Course;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course>, InformationRepository {

    @Query("SELECT c FROM Course c WHERE c.isActive = TRUE")
    Set<Course> findAllActive(Specification<Course> specification);

    @Query("SELECT c FROM Course c WHERE c.id = :id AND c.isActive = TRUE")
    Optional<Course> findActiveById(long id);

}
