/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Option;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.views.editors.FormEditor;
import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.util.Prompts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javafx.event.Event;

/**
 *
 * @author MNicaretta
 */
public class FormUtilities
{
    public static final Item TYPE_CATEGORY      = new Item( "Categoria", Question.TYPE_CATEGORY);
    public static final Item TYPE_SMALL_TEXT    = new Item( "Texto", Question.TYPE_SMALL_TEXT);
    public static final Item TYPE_LARGE_TEXT    = new Item( "Texto Grande", Question.TYPE_LARGE_TEXT);
    public static final Item TYPE_SINGLE_CHOICE = new Item( "Escolha Simples", Question.TYPE_SINGLE_CHOICE);
    public static final Item TYPE_MULTI_CHOICE  = new Item( "Escolha Multipla", Question.TYPE_MULTI_CHOICE);
    public static final Item TYPE_DATE          = new Item( "Data", Question.TYPE_DATE);
    public static final Item TYPE_NUMBER        = new Item( "Número", Question.TYPE_NUMBER);
    
    public static void addForm()
    {
        Form form = new Form();

        new FormEditor(new EditorCallback<Form>(form)
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    EntityService.getInstance().save(getSource());
                    
                    for (Question question : (Set<Question>)getSource().getQuestions())
                    {
                        EntityService.getInstance().save(question);
                    }
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }
    
    public static void editForm(Form form)
    {
        new FormEditor(new EditorCallback<Form>(form)
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    EntityService.getInstance().update(getSource());
                    
                    List<Question> deleteQuestions = EntityService.getInstance().loadValues(Question.class,
                                                                                            Arrays.asList(new Parameter("form",
                                                                                                          Question.class.getDeclaredField("form"),
                                                                                                          getSource(),
                                                                                                          Parameter.COMPARATOR_EQUALS)));
                    for (Question deletable : deleteQuestions)
                    {
                        boolean delete = true;
                        
                        for (Question question : (Set<Question>)getSource().getQuestions())
                        {
                            if (deletable.getId() == question.getId())
                            {
                                delete = false;
                                break;
                            }
                        }
                        
                        if (delete)
                        {
                            EntityService.getInstance().delete(deletable);
                        }
                    }
                    
                    for (Question question : (Set<Question>)getSource().getQuestions())
                    {
                        if (question.getId() == 0)
                        {
                            EntityService.getInstance().save(question);
                        }
                        
                        else
                        {
                            EntityService.getInstance().update(question);
                        }
                    }
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }
    
    public static void deleteForm(Form form)
    {
        if (Prompts.confirm("Exlusão de Formulário", "Deseja realmente excluir o formulário?"))
        {
            try
            {
                for (Question question : (Set<Question>)form.getQuestions())
                {
                    EntityService.getInstance().delete(question);
                }

                EntityService.getInstance().delete(form);
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
    }

    public static Form getValue(int formId)
    {
        Form result = null;

        try
        {
            result = (Form) EntityService.getInstance().getValue(Form.class, formId);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static List<Form> getValues()
    {
        List<Form> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Form.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }
    
    public static List<Item> getQuestionTypes()
    {
        return Arrays.asList(TYPE_CATEGORY,
                             TYPE_SMALL_TEXT,
                             TYPE_LARGE_TEXT,
                             TYPE_SINGLE_CHOICE,
                             TYPE_MULTI_CHOICE,
                             TYPE_DATE,
                             TYPE_NUMBER);
    }
    
    public static Item getQuestionType(int questionType)
    {
        Item result = null;
        
        for (Item i : getQuestionTypes())
        {
            if (i.getValue() == questionType)
            {
                result = i;
                break;
            }
        }
        
        return result;
    }
    
    public static List<Option> getQuestionOptions()
    {
        List<Option> result = new ArrayList<>();
        
        try
        {
            result = EntityService.getInstance().getValues(Option.class);
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
}
