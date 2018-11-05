/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Survey;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Surveys;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class SurveyFetcher
        implements Fetcher<Survey>
{
    @Override
    public Survey fetch(ResultSet resultSet) throws Exception
    {
        Survey result = new Survey();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setTitle(resultSet.getString(count++));
        result.setInfo(resultSet.getString(count++));
        result.setCreatedDate(resultSet.getTimestamp(count++));
        result.setType(resultSet.getInt(count++));
        result.setOwnerId(resultSet.getInt(count++));
        result.setCategoryId(resultSet.getInt(count++));
        result.setParticipantsId(resultSet.getInt(count++));
        result.setViewersId(resultSet.getInt(count++));
        result.setFormId(resultSet.getInt(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Survey value) throws Exception
    {
        Surveys S = (Surveys) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    S.columns.TITLE            + ", " +
                    S.columns.INFO             + ", " +
                    S.columns.DT_CREATED       + ", " +
                    S.columns.TYPE             + ", " +
                    S.columns.REF_USER         + ", " +
                    S.columns.REF_CATEGORY     + ", " +
                    S.columns.REF_PARTICIPANTS + ", " +
                    S.columns.REF_VIEWERS      + ", " +
                    S.columns.REF_FORM         + ", " +
                    S.columns.STATE            +
               " ) values( " +
                    db.quote(value.getTitle())       + ", " +
                    db.quote(value.getInfo())        + ", " +
                    db.quote(value.getCreatedDate()) + ", " +
                    value.getType()                  + ", " +
                    value.getOwnerId()               + ", " +
                    value.getCategoryId()            + ", " +
                    value.getParticipantsId()        + ", " +
                    value.getViewersId()             + ", " +
                    value.getFormId()                + ", " +
                    value.getState()                 +
               " )"+
               " returning " + S.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Survey value) throws Exception
    {
        Surveys S = (Surveys) schema.alias("");
        
        return "update " + S.name + " set " +
                    S.columns.TITLE            + " = " + db.quote(value.getTitle())       + ", " +
                    S.columns.INFO             + " = " + db.quote(value.getInfo())        + ", " +
                    S.columns.DT_CREATED       + " = " + db.quote(value.getCreatedDate()) + ", " +
                    S.columns.TYPE             + " = " + value.getType()                  + ", " +
                    S.columns.REF_USER         + " = " + value.getOwnerId()               + ", " +
                    S.columns.REF_CATEGORY     + " = " + value.getCategoryId()            + ", " +
                    S.columns.REF_PARTICIPANTS + " = " + value.getParticipantsId()        + ", " +
                    S.columns.REF_VIEWERS      + " = " + value.getFormId()                + ", " +
                    S.columns.REF_FORM         + " = " + value.getViewersId()             + ", " +
                    S.columns.STATE            + " = " + value.getState()                 +
               " where " +
                    S.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Survey value) throws Exception
    {
        Surveys S = (Surveys) schema.alias("");
        
        return "update " + S.name + " set " +
                    S.columns.STATE + " = " + Survey.STATE_INACTIVE +
               " where " +
                    S.columns.ID + " = " + value.getId();
    }

}
