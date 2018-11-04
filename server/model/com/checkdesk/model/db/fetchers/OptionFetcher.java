/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Option;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Options;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class OptionFetcher
        implements Fetcher<Option>
{
    @Override
    public Option fetch(ResultSet resultSet) throws Exception
    {
        Option result = new Option();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setType(resultSet.getInt(count++));
        result.setViewersId(resultSet.getInt(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Option value) throws Exception
    {
        Options O = (Options) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    O.columns.NAME        + ", " +
                    O.columns.TYPE        + ", " +
                    O.columns.REF_VIEWERS + ", " +
                    O.columns.STATE       +
               " ) values( " +
                    db.quote(value.getName()) + ", " +
                    value.getType()           + ", " +
                    value.getViewersId()      + ", " +
                    value.getState()          +
               " )"+
               " returning " + O.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Option value) throws Exception
    {
        Options O = (Options) schema.alias("");
        
        return "update " + O.name + " set " +
                    O.columns.NAME        + " = " + db.quote(value.getName()) + ", " +
                    O.columns.TYPE        + " = " + value.getType()           + ", " +
                    O.columns.REF_VIEWERS + " = " + value.getViewersId()      + ", " +
                    O.columns.STATE       + " = " + value.getState()          +
               " where " +
                    O.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Option value) throws Exception
    {
        Options O = (Options) schema.alias("");
        
        return "update " + O.name + " set " +
                    O.columns.STATE + " = " + Option.STATE_INACTIVE +
               " where " +
                    O.columns.ID + " = " + value.getId();
    }

}
