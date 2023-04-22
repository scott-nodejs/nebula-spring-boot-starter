package com.nb.nebula.starter.DataSource;

import com.nb.nebula.starter.utils.ExceptionUtil;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.exception.AuthFailedException;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * @author lucong
 * @date 2021/7/26 14:39
 */
public class SessionTemplate implements ISession {

    private static final Logger log = LoggerFactory.getLogger(SessionTemplate.class);

    private static ApplicationContext context;

    private ISession sessionProxy;

    public SessionTemplate(NebulaPool pool, String user, String password,String space) throws IOErrorException, AuthFailedException, NotValidConnectionException {
        this.sessionProxy = (ISession) Proxy.newProxyInstance(ISession.class.getClassLoader(),
                new Class[]{ISession.class},
                new SessionInvocationHandler(pool, user, password,space));
    }

    @Override
    public ResultSet execute(String stmt) throws IOErrorException, UnsupportedEncodingException {
        return this.sessionProxy.execute(stmt);
    }

    @Override
    public void release() {

    }

    private class SessionInvocationHandler implements InvocationHandler {

        private NebulaPool pool;

        private String user;

        private String password;

        private String space;

        public SessionInvocationHandler(NebulaPool pool, String user, String password,String space){
            this.pool = pool;
            this.user = user;
            this.password = password;
            this.space = space;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Session session1 = SessionUtils.getSession(pool,user,password,space);
            try{
                log.info("sessionTemplate动态代理对象处理...");
                Object result = method.invoke(session1, args);
                return result;
            } catch (Throwable t){
                Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
                if(unwrapped instanceof IOErrorException){
                    log.error("ioerror出现异常: {}", unwrapped);
                    SessionUtils.closeSeesion(session1);
                }
                throw unwrapped;
            }finally {
                if(session1 != null && session1.getCount() >= 20){
                    log.info("自动释放session");
                    SessionUtils.closeSeesion(session1);
                }
            }
        }
    }
}
