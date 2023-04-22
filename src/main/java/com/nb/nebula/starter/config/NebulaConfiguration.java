package com.nb.nebula.starter.config;

import com.nb.nebula.starter.DataSource.SessionDao;
import com.nb.nebula.starter.nebula.NebulaConfig;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lucong
 * @date 2021/12/3 15:18
 */
@Configuration
public class NebulaConfiguration {

    @Bean
    public SessionDao sessionDao(){
        return new SessionDao();
    }

    @Bean
    @ConfigurationProperties(prefix = "nebula.config")
    public NebulaConfig nebulaConfig(){
        return new NebulaConfig();
    }

    @Bean
    public NebulaPool nebulaPool(final NebulaConfig nebulaConfig) throws UnknownHostException {
        NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
        nebulaPoolConfig.setMaxConnSize(nebulaConfig.getMaxConnSize());
        nebulaPoolConfig.setMinConnSize(nebulaConfig.getMinConnSize());
        nebulaPoolConfig.setIdleTime(nebulaConfig.getIdleTime());
        nebulaPoolConfig.setTimeout(nebulaConfig.getTimeout());
        NebulaPool pool = new NebulaPool();
        List<HostAddress> addresses = initAddress(nebulaConfig.getUrl());
        pool.init(addresses, nebulaPoolConfig);
        return pool;
    }

    private List<HostAddress> initAddress(String url){
        List<HostAddress> hostAddresses = new ArrayList<>();
        List<String> urls = Arrays.asList(url.split(","));
        urls.forEach(u->{
            String host = u.split(":")[0];
            int port = Integer.parseInt(u.split(":")[1]);
            hostAddresses.add(new HostAddress(host, port));
        });
        return hostAddresses;
    }
}
