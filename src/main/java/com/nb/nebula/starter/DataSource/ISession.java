package com.nb.nebula.starter.DataSource;

import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.exception.IOErrorException;

import java.io.UnsupportedEncodingException;

/**
 * @author lucong
 * @date 2021/7/26 14:38
 */
public interface ISession {

    ResultSet execute(String stmt) throws IOErrorException, UnsupportedEncodingException;

    void release();
}
