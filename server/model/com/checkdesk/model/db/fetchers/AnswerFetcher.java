/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Answer;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Answers;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class AnswerFetcher
        implements Fetcher<Answer>
{
    @Override
    public Answer fetch(ResultSet resultSet) throws Exception
    {
        Answer result = new Answer();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setFeedback(resultSet.getString(count++));
        result.setOccurredDate(resultSet.getTimestamp(count++));
        result.setSurveyId(resultSet.getInt(count++));
        result.setOwnerId(resultSet.getInt(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Answer value) throws Exception
    {
        Answers A = (Answers) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    A.columns.FEEDBACK    + ", " +
                    A.columns.DT_OCCURRED + ", " +
                    A.columns.REF_SURVEY  + ", " +
                    A.columns.REF_USER    + ", " +
                    A.columns.STATE       +
               " ) values( " +
                    db.quote(value.getFeedback())     + ", " +
                    db.quote(value.getOccurredDate()) + ", " +
                    value.getSurveyId()               + ", " +
                    value.getOwnerId()                + ", " +
                    value.getState()                  +
               " )" +
               " returning " + A.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Answer value) throws Exception
    {
        Answers A = (Answers) schema.alias("");
        
        return "update " + A.name + " set " +
                    A.columns.FEEDBACK    + " = " + db.quote(value.getFeedback())     + ", " +
                    A.columns.DT_OCCURRED + " = " + db.quote(value.getOccurredDate()) + ", " +
                    A.columns.REF_SURVEY  + " = " + value.getSurveyId()               + ", " +
                    A.columns.REF_USER    + " = " + value.getOwnerId()                + ", " +
                    A.columns.STATE       + " = " + value.getState()                  +
               " where " +
                    A.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Answer value) throws Exception
    {
        Answers A = (Answers) schema.alias("");
        
        return "update " + A.name + " set " +
                    A.columns.STATE + " = " + Answer.STATE_INACTIVE +
               " where " +
                    A.columns.ID + " = " + value.getId();
    }

}
