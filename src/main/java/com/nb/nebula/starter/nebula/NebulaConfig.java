package com.nb.nebula.starter.nebula;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lucong
 * @date 2021/7/26 16:07
 */
@Data
public class NebulaConfig {
    private String user;
    private String password;
    private String space;
    private String url;
    private int maxConnSize;
    private int minConnSize;
    private int idleTime;
    private int timeout;
}
