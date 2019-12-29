package carzanodev.genuniv.microservices.college.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "intra.service")
public class IntraServiceProperties {

    private final GeneralInfo generalInfo;
    private final PersonalRecords personalRecords;

    @AllArgsConstructor
    @Getter
    @ConstructorBinding
    @ToString
    public static final class GeneralInfo {
        private final String protocol;
        private final String serviceName;
        private final SchoolYearAPI schoolYearAPI;
        private final SchoolPeriodAPI schoolPeriodAPI;
        private final ScheduleAPI scheduleAPI;

        public String getSchoolYearApiUrl() {
            return protocol + "://" + serviceName + schoolYearAPI.getApiPrefix();
        }

        public String getSchoolPeriodApiUrl() {
            return protocol + "://" + serviceName + schoolPeriodAPI.getApiPrefix();
        }

        public String getScheduleApiUrl() {
            return protocol + "://" + serviceName + scheduleAPI.getApiPrefix();
        }

        @ConstructorBinding
        public static final class SchoolYearAPI extends Api {
            public SchoolYearAPI(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class SchoolPeriodAPI extends Api {
            public SchoolPeriodAPI(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class ScheduleAPI extends Api {
            public ScheduleAPI(String apiPrefix) {
                super(apiPrefix);
            }
        }
    }

    @AllArgsConstructor
    @Getter
    @ConstructorBinding
    @ToString
    public static final class PersonalRecords {
        private final String protocol;
        private final String serviceName;
        private final FacultyAPI facultyAPI;

        public String getFacultyApiUrl() {
            return protocol + "://" + serviceName + facultyAPI.getApiPrefix();
        }

        @ConstructorBinding
        public static final class FacultyAPI extends Api {
            public FacultyAPI(String apiPrefix) {
                super(apiPrefix);
            }
        }
    }

    @AllArgsConstructor
    @Getter
    public static abstract class Api {
        private final String apiPrefix;
    }

}
