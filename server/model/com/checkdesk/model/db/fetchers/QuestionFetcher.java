/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Question;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Questions;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class QuestionFetcher
        implements Fetcher<Question>
{
    @Override
    public Question fetch(ResultSet resultSet) throws Exception
    {
        Question result = new Question();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setType(resultSet.getInt(count++));
        result.setConstraints(resultSet.getString(count++));
        result.setOptionId(resultSet.getInt(count++));
        result.setFormId(resultSet.getInt(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Question value) throws Exception
    {
        Questions Q = (Questions) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    Q.columns.NAME        + ", " +
                    Q.columns.TYPE        + ", " +
                    Q.columns.CONSTRAINTS + ", " +
                    Q.columns.REF_OPTION  + ", " +
                    Q.columns.REF_FORM    + ", " +
                    Q.columns.STATE       +
               " ) values( " +
                    db.quote(value.getName())        + ", " +
                    value.getType()                  + ", " +
                    db.quote(value.getConstraints()) + ", " +
                    value.getOptionId()              + ", " +
                    value.getFormId()                + ", " +
                    value.getState()                 +
               " )"+
               " returning " + Q.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Question value) throws Exception
    {
        Questions Q = (Questions) schema.alias("");
        
        return "update " + Q.name + " set " +
                    Q.columns.NAME        + " = " + db.quote(value.getName())        + ", " +
                    Q.columns.TYPE        + " = " + value.getType()                  + ", " +
                    Q.columns.CONSTRAINTS + " = " + db.quote(value.getConstraints()) + ", " +
                    Q.columns.REF_OPTION  + " = " + value.getOptionId()              + ", " +
                    Q.columns.REF_FORM    + " = " + value.getFormId()                + ", " +
                    Q.columns.STATE       + " = " + value.getState()                 +
               " where " +
                    Q.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Question value) throws Exception
    {
        Questions Q = (Questions) schema.alias("");
        
        return "update " + Q.name + " set " +
                    Q.columns.STATE + " = " + Question.STATE_INACTIVE +
               " where " +
                    Q.columns.ID + " = " + value.getId();
    }

}
