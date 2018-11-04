/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.model.util.SurveyWrapper;
import com.checkdesk.views.editors.SurveyEditor;
import com.checkdesk.views.parts.Prompts;
import com.checkdesk.views.util.EditorCallback;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;

/**
 *
 * @author MNicaretta
 */
public class SurveyUtilities
{

    private static final Item TYPE_PUBLIC = new Item("Pública", Survey.TYPE_PUBLIC);
    private static final Item TYPE_PRIVATE = new Item("Privada", Survey.TYPE_PRIVATE);
    private static final Item TYPE_ANONYMOUS = new Item("Anônima", Survey.TYPE_ANONYMOUS);
    private static final Item TYPE_TOTEM = new Item("Totem", Survey.TYPE_TOTEM);

    public static void addSurvey()
    {
        Survey survey = new Survey();
        survey.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        new SurveyEditor(new EditorCallback<SurveyWrapper>(new SurveyWrapper(survey))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    FormUtilities.saveForm(getSource().getForm());

                    EntityService.getInstance().save(getSource().getSurvey());

                    ApplicationController.getInstance().notify(getSource().getSurvey());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }

        }).show();
    }

    public static void editSurvey(Survey survey)
    {
        new SurveyEditor(new EditorCallback<SurveyWrapper>(new SurveyWrapper(survey))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    FormUtilities.saveForm(getSource().getForm());
                    
                    EntityService.getInstance().update(getSource().getSurvey());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }

    public static void deleteSurvey(Survey survey)
    {
        if (Prompts.confirm("Exlusão de Pesquisa", "Deseja realmente excluir a pesquisa?"))
        {
            try
            {
                EntityService.getInstance().delete(survey);

                FormUtilities.deleteForm(survey.getFormId());
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
    }

    public static Survey getValue(int surveyId)
    {
        Survey result = null;

        try
        {
            result = (Survey) EntityService.getInstance().getValue(Survey.class, surveyId);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static List<Survey> getValues()
    {
        List<Survey> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Survey.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }
    
    public static List<Survey> getPendingSurvey()
    {
        List<Survey> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Survey.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }
    
    public static List<Survey> getOwnedSurvey()
    {
        List<Survey> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Survey.class, Arrays.asList(new Parameter(Survey.class.getDeclaredField("ownerId"),
                                                                                                     ApplicationController.getInstance().getActiveUser().getId(),
                                                                                                     Parameter.COMPARATOR_EQUALS)));
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static ObservableList<Item> getItems()
    {
        return FXCollections.observableArrayList(TYPE_PUBLIC, TYPE_PRIVATE, TYPE_ANONYMOUS, TYPE_TOTEM);
    }

    public static Item getType(int type)
    {
        Item result = null;

        for (Item item : getItems())
        {
            if (item.getValue() == type)
            {
                result = item;
                break;
            }
        }

        return result;
    }
}
