package com.netfliz.admin.constant.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EqualsAndHashCode
@Configuration
@ConfigurationProperties(prefix = "backblaze.b2")
public class B2Properties {
    private String endpoint = "";
    private String region = "";
    private String accessKeyId = "";
    private String secretAccessKey = " ";
    private String bucketName = "";
    private String workerSharedSecret = "";
}
