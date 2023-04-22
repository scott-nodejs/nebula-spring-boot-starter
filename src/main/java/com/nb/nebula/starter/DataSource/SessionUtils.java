package com.nb.nebula.starter.DataSource;

import com.vesoft.nebula.client.graph.exception.AuthFailedException;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lucong
 * @date 2021/7/27 10:39
 */
public class SessionUtils {
    private static final Logger log = LoggerFactory.getLogger(SessionUtils.class);

    static final ThreadLocal<Map<Object,Object>> resources = new NamedThreadLocal<>("session");

    public static Session getSession(NebulaPool pool, String user, String password,String space) throws IOErrorException, AuthFailedException, NotValidConnectionException {
        Map<Object, Object> objectObjectMap = resources.get();
        if(objectObjectMap == null){
            objectObjectMap = new HashMap<>();
            resources.set(objectObjectMap);
        }
        Session session1 = (Session) objectObjectMap.get(pool);
        if(session1 == null || session1.getSession() == null){
            com.vesoft.nebula.client.graph.net.Session session = null;
            try {
                session = pool.getSession(user, password, false);
                session.execute(String.format("use %s",space));
                log.info("获取session: " + session);
            } catch (Exception e) {
                log.error("获取session时异常: {}", e);
            }
            session1 = new Session(session);
            objectObjectMap.put(pool, session1);
        }
        session1.requested();
        log.info("次数: "+ Thread.currentThread().getName()+" "+session1.getCount());
        log.info("session: " + session1);
        return session1;
    }

    public static void closeSeesion(Session session){
        session.release();
        resources.remove();
        session.setSession(null);
    }
}
