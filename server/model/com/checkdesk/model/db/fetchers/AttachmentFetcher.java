/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Attachment;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Attachments;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class AttachmentFetcher
        implements Fetcher<Attachment>
{
    @Override
    public Attachment fetch(ResultSet resultSet) throws Exception
    {
        Attachment result = new Attachment();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setQuestionId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setType(resultSet.getString(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Attachment value) throws Exception
    {
        Attachments A = (Attachments) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    A.columns.REF_QUESTION + ", " +
                    A.columns.NAME         + ", " +
                    A.columns.TYPE         + ", " +
                    A.columns.STATE        +
               " ) values( " +
                    value.getQuestionId()     + ", " +
                    db.quote(value.getName()) + ", " +
                    db.quote(value.getType()) + ", " +
                    value.getState()          +
               " )"+
               " returning " + A.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Attachment value) throws Exception
    {
        Attachments A = (Attachments) schema.alias("");
        
        return "update " + A.name + " set " +
                    A.columns.REF_QUESTION + " = " + value.getQuestionId()     + ", " +
                    A.columns.NAME         + " = " + db.quote(value.getName()) + ", " +
                    A.columns.TYPE         + " = " + db.quote(value.getType()) + ", " +
                    A.columns.STATE        + " = " + value.getState()          +
               " where " +
                    A.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Attachment value) throws Exception
    {
        Attachments A = (Attachments) schema.alias("");
        
        return "update " + A.name + " set " +
                    A.columns.STATE + " = " + Attachment.STATE_INACTIVE +
               " where " +
                    A.columns.ID + " = " + value.getId();
    }
}
