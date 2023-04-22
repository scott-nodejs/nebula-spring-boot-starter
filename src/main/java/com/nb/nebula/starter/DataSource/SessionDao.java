package com.nb.nebula.starter.DataSource;

import com.nb.nebula.starter.nebula.NebulaConfig;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author lucong
 * @date 2021/7/20 15:13
 */
public class SessionDao implements ApplicationContextAware{
    private static final Logger log = LoggerFactory.getLogger(SessionDao.class);

    private ISession session;

    private ApplicationContext context;

    public ISession getSession(){
        try {
            if(session == null){
                synchronized (Object.class) {
                    if(session == null) {
                        NebulaConfig nebulaConfig = (NebulaConfig) context.getBean("nebulaConfig");
                        NebulaPool nebulaPool = (NebulaPool) context.getBean("nebulaPool");
                        session = new SessionTemplate(nebulaPool, nebulaConfig.getUser(), nebulaConfig.getPassword(), nebulaConfig.getSpace());
                    }
                }
            }
        }catch (Exception e){
            log.error("获取", e);
        }
        log.info("获取的session：{}", session);
        return session;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
