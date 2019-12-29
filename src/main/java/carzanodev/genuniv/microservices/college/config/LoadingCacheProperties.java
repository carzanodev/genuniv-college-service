package carzanodev.genuniv.microservices.college.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "loading-cache")
public class LoadingCacheProperties {

    private int slowInterval;
    private int fastInterval;

}
