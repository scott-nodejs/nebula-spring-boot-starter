package com.nb.nebula.starter.DataSource;

import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.exception.IOErrorException;

import java.io.UnsupportedEncodingException;

/**
 * @author lucong
 * @date 2021/7/26 15:16
 */
public class Session implements ISession {

    private com.vesoft.nebula.client.graph.net.Session session;

    private int referenceCount = 0;

    public com.vesoft.nebula.client.graph.net.Session getSession(){
        return session;
    }

    public void setSession(com.vesoft.nebula.client.graph.net.Session session){
        this.session = session;
    }

    public Session(com.vesoft.nebula.client.graph.net.Session session){
        this.session = session;
    }


    @Override
    public ResultSet execute(String stmt) throws IOErrorException, UnsupportedEncodingException {
        return session.execute(stmt);
    }

    @Override
    public void release() {
        session.release();
    }

    public void requested() {
        this.referenceCount++;
    }

    public void released() {
        this.referenceCount--;
    }

    public int getCount(){
        return referenceCount;
    }
}
