/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Log;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Logs;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class LogFetcher
        implements Fetcher<Log>
{
    @Override
    public Log fetch(ResultSet resultSet) throws Exception
    {
        Log result = new Log();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setEvent(resultSet.getInt(count++));
        result.setObjectName(resultSet.getString(count++));
        result.setObjectClass(resultSet.getString(count++));
        result.setCommand(resultSet.getString(count++));
        result.setTimestamp(resultSet.getTimestamp(count++));
        result.setUserId(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Log value) throws Exception
    {
        Logs L = (Logs) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    L.columns.EVENT        + ", " +
                    L.columns.OBJECT_NAME  + ", " +
                    L.columns.OBJECT_CLASS + ", " +
                    L.columns.COMMAND      + ", " +
                    L.columns.TIMESTAMP    + ", " +
                    L.columns.REF_USER     +
               " ) values( " +
                    value.getEvent()                 + ", " +
                    db.quote(value.getObjectName())  + ", " +
                    db.quote(value.getObjectClass()) + ", " +
                    db.quote(value.getCommand())     + ", " +
                    db.quote(value.getTimestamp())   + ", " +
                    value.getUserId()                +
               " )"+
               " returning " + L.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Log value) throws Exception
    {
        Logs L = (Logs) schema.alias("");
        
        return "update " + L.name + " set " +
                    L.columns.EVENT        + " = " + value.getEvent()                 + ", " +
                    L.columns.OBJECT_NAME  + " = " + db.quote(value.getObjectName())  + ", " +
                    L.columns.OBJECT_CLASS + " = " + db.quote(value.getObjectClass()) + ", " +
                    L.columns.COMMAND      + " = " + db.quote(value.getCommand())     + ", " +
                    L.columns.TIMESTAMP    + " = " + db.quote(value.getTimestamp())   + ", " +
                    L.columns.REF_USER     + " = " + value.getUserId()                +
               " where " +
                    L.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Log value) throws Exception
    {
        Logs L = (Logs) schema.alias("");
        
        return "delete from " + L.name +
               " where " + L.columns.ID + " = " + value.getId();
    }
}
