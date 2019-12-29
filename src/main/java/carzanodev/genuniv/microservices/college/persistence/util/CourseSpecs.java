package carzanodev.genuniv.microservices.college.persistence.util;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.college.persistence.entity.Course;

public enum CourseSpecs {

    EQUAL_CODE_PREDICATE() {
        @Override
        public Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.equal(root.get("code"), s);
        }
    },

    EQUAL_UNIT_PREDICATE() {
        @Override
        public Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.equal(root.get("unit"), s);
        }
    },

    EQUAL_LECTUREHOURS_PREDICATE() {
        @Override
        public Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.equal(root.get("lecture_hours"), s);
        }
    },

    EQUAL_LABHOURS_PREDICATE() {
        @Override
        public Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.equal(root.get("lab_hours"), s);
        }
    },

    EQUAL_COLLEGE_PREDICATE() {
        @Override
        public Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.equal(root.get("college_id"), s);
        }
    },

    CONTAIN_NAME_PREDICATE() {
        @Override
        public Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(root.get("name"), s);
        }
    },

    CONTAIN_DESCRIPTION_PREDICATE() {
        @Override
        public Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(root.get("description"), s);
        }
    };

    public static Specification<Course> createEmptySpecification() {
        return createFullSpecification("", "", "", "", "", "", "");
    }

    public static Specification<Course> createFullSpecification(
            String equalCode,
            String equalUnit,
            String equalLectureHours,
            String equalLabHours,
            String equalCollegeId,
            String containName,
            String containDescription) {
        return (Specification<Course>) (root, query, cb) -> {
            Set<Predicate> predicateSet = new HashSet<>();

            if (!StringUtils.isEmpty(equalCode)) {
                predicateSet.add(EQUAL_CODE_PREDICATE.predicate(equalCode, root, query, cb));
            }

            if (!StringUtils.isEmpty(equalUnit)) {
                predicateSet.add(EQUAL_UNIT_PREDICATE.predicate(equalUnit, root, query, cb));
            }

            if (!StringUtils.isEmpty(equalLectureHours)) {
                predicateSet.add(EQUAL_LECTUREHOURS_PREDICATE.predicate(equalLectureHours, root, query, cb));
            }

            if (!StringUtils.isEmpty(equalLabHours)) {
                predicateSet.add(EQUAL_LABHOURS_PREDICATE.predicate(equalLabHours, root, query, cb));
            }

            if (!StringUtils.isEmpty(equalCollegeId)) {
                predicateSet.add(EQUAL_COLLEGE_PREDICATE.predicate(equalCollegeId, root, query, cb));
            }

            if (!StringUtils.isEmpty(containName)) {
                predicateSet.add(CONTAIN_NAME_PREDICATE.predicate(surroundWithWildcard(containName), root, query, cb));
            }

            if (!StringUtils.isEmpty(containDescription)) {
                predicateSet.add(CONTAIN_DESCRIPTION_PREDICATE.predicate(surroundWithWildcard(containDescription), root, query, cb));
            }

            if (!StringUtils.isEmpty(containDescription)) {
                predicateSet.add(CONTAIN_DESCRIPTION_PREDICATE.predicate(surroundWithWildcard(containDescription), root, query, cb));
            }

            return cb.and(predicateSet.toArray(Predicate[]::new));
        };
    }

    private static String surroundWithWildcard(String s) {
        return "%" + s + "%";
    }

    public abstract Predicate predicate(String s, Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb);

}
