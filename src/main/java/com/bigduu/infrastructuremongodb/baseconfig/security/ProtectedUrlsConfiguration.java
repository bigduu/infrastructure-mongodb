package com.bigduu.infrastructuremongodb.baseconfig.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bigduu
 * @title: ProtectedUrlsConfiguration
 * @projectName infrastructure-mongodb
 * @description: TODO
 * @date 2020/7/1522:27
 */
@Configuration
@ConfigurationProperties(prefix = "qszs.security")
@Getter
@Setter
public class ProtectedUrlsConfiguration {
    private List<String> protectedUrls;
}
