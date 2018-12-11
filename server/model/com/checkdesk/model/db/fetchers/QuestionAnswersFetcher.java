/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.QuestionAnswer;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.QuestionAnswers;
import com.checkdesk.model.db.Schemas.Schema;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class QuestionAnswersFetcher
        implements Fetcher<QuestionAnswer>
{
    @Override
    public QuestionAnswer fetch(ResultSet resultSet) throws Exception
    {
        QuestionAnswer result = new QuestionAnswer();

        int count = 1;

        result.setAnswerId(resultSet.getInt(count++));
        result.setQuestionId(resultSet.getInt(count++));
        result.setValue(resultSet.getString(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, QuestionAnswer value) throws Exception
    {
        QuestionAnswers QA = (QuestionAnswers) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    QA.columns.REF_ANSWER   + ", " +
                    QA.columns.REF_QUESTION + ", " +
                    QA.columns.VALUE        +
               " ) values( " +
                    value.getAnswerId()        + ", " +
                    value.getQuestionId()      + ", " +
                    db.quote(value.getValue()) +
               " ) returning 0";
    }

    @Override
    public String update(Database db, Schema schema, QuestionAnswer value) throws Exception
    {
        QuestionAnswers QA = (QuestionAnswers) schema.alias("");
        
        return "update " + QA.name + " set " +
                    QA.columns.VALUE        + " = " + db.quote(value.getValue()) +
               " where " +
                    QA.columns.REF_ANSWER   + " = " + value.getAnswerId() +
                    " and " +
                    QA.columns.REF_QUESTION + " = " + value.getQuestionId();
    }

    @Override
    public String delete(Database db, Schema schema, QuestionAnswer value) throws Exception
    {
        QuestionAnswers QA = (QuestionAnswers) schema.alias("");
        
        return "delete from " + QA.name +
               " where " +
                    QA.columns.REF_ANSWER   + " = " + value.getAnswerId() +
                    " and " +
                    QA.columns.REF_QUESTION + " = " + value.getQuestionId();
    }
}
