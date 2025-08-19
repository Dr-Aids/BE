package com.example.dr_aids.security.common;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "ai.api")
@Data
public class AIProperties {
    private String key;
    private List<String> whitelistPaths;
}
