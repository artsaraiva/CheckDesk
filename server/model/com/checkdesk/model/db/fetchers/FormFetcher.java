/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Form;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Forms;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class FormFetcher
        implements Fetcher<Form>
{
    @Override
    public Form fetch(ResultSet resultSet) throws Exception
    {
        Form result = new Form();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setInfo(resultSet.getString(count++));
        result.setViewersId(resultSet.getInt(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Form value) throws Exception
    {
        Forms F = (Forms) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    F.columns.NAME        + ", " +
                    F.columns.INFO        + ", " +
                    F.columns.REF_VIEWERS + ", " +
                    F.columns.STATE       +
               " ) values( " +
                    db.quote(value.getName()) + ", " +
                    db.quote(value.getInfo()) + ", " +
                    value.getViewersId()      + ", " +
                    value.getState()          +
               " )"+
               " returning " + F.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Form value) throws Exception
    {
        Forms F = (Forms) schema.alias("");
        
        return "update " + F.name + " set " +
                    F.columns.NAME        + " = " + db.quote(value.getName()) + ", " +
                    F.columns.INFO        + " = " + db.quote(value.getInfo()) + ", " +
                    F.columns.REF_VIEWERS + " = " + value.getViewersId()      + ", " +
                    F.columns.STATE       + " = " + value.getState()          +
               " where " +
                    F.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Form value) throws Exception
    {
        Forms F = (Forms) schema.alias("");
        
        return "update " + F.name + " set " +
                    F.columns.STATE + " = " + Form.STATE_INACTIVE +
               " where " +
                    F.columns.ID + " = " + value.getId();
    }
}
