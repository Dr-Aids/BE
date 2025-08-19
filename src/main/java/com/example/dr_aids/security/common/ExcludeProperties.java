package com.example.dr_aids.security.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "exclude")
@Component
@Data
public class ExcludeProperties {
    private List<String> paths;
}