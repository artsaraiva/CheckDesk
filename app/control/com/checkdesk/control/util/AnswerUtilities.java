/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ValidationController;
import com.checkdesk.model.data.Answer;
import com.checkdesk.model.data.Entity;
import com.checkdesk.model.data.Option;
import com.checkdesk.model.data.OptionItem;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.data.QuestionAnswer;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.AnswerWrapper;
import com.checkdesk.model.util.Parameter;
import static com.checkdesk.model.util.Parameter.COMPARATOR_UNLIKE;
import com.checkdesk.views.parts.DatePicker;
import com.checkdesk.views.util.Callback;
import com.checkdesk.views.util.Validation;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import org.json.JSONObject;

/**
 *
 * @author MNicaretta
 */
public class AnswerUtilities
{
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    public static Node nodeFor(Question question, String value)
    {
        Node result = null;
        
        switch (question.getType())
        {
            case Question.TYPE_CATEGORY:
            {
                result = new Label(question.getName());
                result.getStyleClass().add("details-header");
                HBox.setHgrow(result, Priority.ALWAYS);
            }
            break;
                
            case Question.TYPE_SMALL_TEXT:
            {
                result = new TextField(value);
                HBox.setHgrow(result, Priority.ALWAYS);
            }
            break;
                
            case Question.TYPE_LARGE_TEXT:
            {
                TextArea textarea = new TextArea(value);
                textarea.setPrefHeight(300);
                
                result = textarea;
                HBox.setHgrow(result, Priority.ALWAYS);
            }
            break;
                
            case Question.TYPE_SINGLE_CHOICE:
            {
                HBox hbox = new HBox();
                hbox.setSpacing(5);
                
                RadioButton radio = null;
                ToggleGroup group = new ToggleGroup();
                
                Option option = FormUtilities.getOption(question.getOptionId());
                
                for (OptionItem item : FormUtilities.getOptionItems(option))
                {
                    radio = new RadioButton(item.getName());
                    radio.setUserData(item);
                    radio.getWidth();
                    radio.setToggleGroup(group);
                    
                    hbox.getChildren().add(radio);
                    
                    if (item.getValue().equals(value))
                    {
                        radio.setSelected(true);
                    }
                }
                
                result = new Pane(hbox);
                hbox.setPadding(new Insets(5));
            }
            break;
                
            case Question.TYPE_MULTI_CHOICE:
            {
                HBox hbox = new HBox();
                hbox.setSpacing(5);
                
                Option option = FormUtilities.getOption(question.getOptionId());
                CheckBox checkbox = null;
                
                String[] values = new String[0];
                
                if (value != null)
                {
                    values = value.split(";");
                }
                
                for (OptionItem item : FormUtilities.getOptionItems(option))
                {
                    checkbox = new CheckBox(item.getName());
                    checkbox.setUserData(item);
                    
                    hbox.getChildren().add(checkbox);
                    
                    for (String v : values)
                    {
                        if (item.getValue().equals(v))
                        {
                            checkbox.setSelected(true);
                        }
                    }
                }
                
                result = new Pane(hbox);
                hbox.setPadding(new Insets(5));
            }
            break;
                
            case Question.TYPE_DATE:
            {
                result = new DatePicker();
                
                try
                {
                    ((DatePicker) result).setDate(df.parse(value));
                }
                
                catch (Exception e) {}
            }
            break;
                
            case Question.TYPE_NUMBER:
            {
                int min = Integer.MIN_VALUE;
                int max = Integer.MAX_VALUE;
                int initial = 0;
                
                try
                {
                    JSONObject json = new JSONObject(question.getConstraints());
                    
                    if (json.has("min"))
                    {
                        min = Integer.parseInt(json.getString("min"));
                    }
                    
                    if (json.has("max"))
                    {
                        min = Integer.parseInt(json.getString("max"));
                    }
                }
                
                catch (Exception e) {}
                
                try
                {
                    initial = Integer.parseInt(value);
                }
                
                catch (Exception e) {}
                
                result = new Spinner(min, max, initial);
            }
            break;
        }
        
        return result;
    }
    
    public static Validation validationFor(Question question, Node node)
    {
        Validation result = null;
        
        switch (question.getType())
        {
            case Question.TYPE_SMALL_TEXT:
            {
                result = ValidationController.addValidation((TextField) node);
            }
            break;
                
            case Question.TYPE_LARGE_TEXT:
            {
                TextArea textarea = (TextArea) node;
                
                result = new Validation(textarea)
                {
                    @Override
                    protected String validate()
                    {
                        String error = "";

                        if (textarea.getText() == null || textarea.getText().isEmpty())
                        {
                            error = "Esse campo deve ser preenchido";
                        }

                        return error;
                    }
                };
            }
            break;
                
            case Question.TYPE_SINGLE_CHOICE:
            {
                HBox hbox = (HBox) ((Pane) node).getChildren().get(0);
                List<RadioButton> radiobuttons = new ArrayList<>();
                
                BooleanProperty property = new RadioButton().selectedProperty();
                
                BooleanExpression lastValue = null;
                
                for (Node child : hbox.getChildren())
                {
                    RadioButton radiobutton = (RadioButton) child;
                    
                    radiobuttons.add(radiobutton);
                    
                    if (lastValue == null)
                    {
                        lastValue = radiobutton.selectedProperty();
                    }
                    
                    else
                    {
                        lastValue = lastValue.or(radiobutton.selectedProperty());
                    }
                }

                property.bind(lastValue);

                result = new Validation(hbox, property, new Callback(node)
                {
                    @Override
                    public void handle(Event t)
                    {
                        String error = (String) t.getSource();
                        
                        if (error == null || error.isEmpty())
                        {
                            hbox.setBorder(null);
                            
                            radiobuttons.forEach((radiobutton) ->
                            {
                                radiobutton.setTooltip(null);
                            });
                        }
                        
                        else
                        {
                            hbox.setBorder(new Border(new BorderStroke(Paint.valueOf("#FF0000"),
                                                      BorderStrokeStyle.SOLID,
                                                      new CornerRadii(5),
                                                      BorderWidths.DEFAULT,
                                                      new Insets(-1))));
                            
                            radiobuttons.forEach((radiobutton) ->
                            {
                                radiobutton.setTooltip(new Tooltip(error));
                            });
                        }
                    }

                })
                {
                    @Override
                    protected String validate()
                    {
                        String error = "";
                        
                        if (!property.get())
                        {
                            error = "É preciso escolher uma opção";
                        }
                        
                        return error;
                    }
                };
            }
            break;
                
            case Question.TYPE_MULTI_CHOICE:
            {
                HBox hbox = (HBox) ((Pane) node).getChildren().get(0);
                List<CheckBox> checkboxes = new ArrayList<>();
                
                BooleanProperty property = new CheckBox().selectedProperty();
                
                BooleanExpression lastValue = null;
                
                for (Node child : hbox.getChildren())
                {
                    CheckBox checkbox = (CheckBox) child;
                    
                    checkboxes.add(checkbox);
                    
                    if (lastValue == null)
                    {
                        lastValue = checkbox.selectedProperty();
                    }
                    
                    else
                    {
                        lastValue = lastValue.or(checkbox.selectedProperty());
                    }
                }

                property.bind(lastValue);

                result = new Validation(hbox, property, new Callback(node)
                {
                    @Override
                    public void handle(Event t)
                    {
                        String error = (String) t.getSource();
                        
                        if (error == null || error.isEmpty())
                        {
                            hbox.setBorder(null);
                            
                            checkboxes.forEach((checkbox) ->
                            {
                                checkbox.setTooltip(null);
                            });
                        }
                        
                        else
                        {
                            hbox.setBorder(new Border(new BorderStroke(Paint.valueOf("#FF0000"),
                                                      BorderStrokeStyle.SOLID,
                                                      new CornerRadii(5),
                                                      BorderWidths.DEFAULT,
                                                      new Insets(-1))));
                            
                            checkboxes.forEach((checkbox) ->
                            {
                                checkbox.setTooltip(new Tooltip(error));
                            });
                        }
                    }

                })
                {
                    @Override
                    protected String validate()
                    {
                        String error = "";
                        
                        if (!property.get())
                        {
                            error = "É preciso escolher pelo menos uma opção";
                        }
                        
                        return error;
                    }
                };
            }
            break;
                
            case Question.TYPE_DATE:
            {
                Date min = null;
                Date max = null;
                
                try
                {
                    JSONObject json = new JSONObject(question.getConstraints());
                    
                    if (json.has("min"))
                    {
                        min = df.parse(json.getString("min"));
                    }
                    
                    if (json.has("max"))
                    {
                        min = df.parse(json.getString("max"));
                    }
                }
                
                catch (Exception e) {}
                
                result = ValidationController.addValidation((DatePicker) node, min, max);
            }
            break;
        }
        
        return result;
    }
    
    public static String valueFor(Question question, Node node)
    {
        String result = null;
        
        switch (question.getType())
        {
            case Question.TYPE_CATEGORY:
            {
                result = "";
            }
            break;
            
            case Question.TYPE_SMALL_TEXT:
            {
                result = ((TextField) node).getText();
            }
            break;
                
            case Question.TYPE_LARGE_TEXT:
            {
                result = ((TextArea) node).getText();
            }
            break;
                
            case Question.TYPE_SINGLE_CHOICE:
            {
                HBox hbox = (HBox) ((Pane) node).getChildren().get(0);
                
                for (Node child : hbox.getChildren())
                {
                    RadioButton radiobutton = (RadioButton) child;
                    
                    if (radiobutton.isSelected())
                    {
                        result = ((OptionItem) radiobutton.getUserData()).getValue();
                        break;
                    }
                }
            }
            break;
                
            case Question.TYPE_MULTI_CHOICE:
            {
                HBox hbox = (HBox) ((Pane) node).getChildren().get(0);
                
                StringJoiner joiner = new StringJoiner(";");
                
                for (Node child : hbox.getChildren())
                {
                    CheckBox checkbox = (CheckBox) child;
                    
                    if (checkbox.isSelected())
                    {
                        joiner.add(((OptionItem) checkbox.getUserData()).getValue());
                    }
                }
                
                result = joiner.toString();
            }
            break;
                
            case Question.TYPE_DATE:
            {
                result = df.format(((DatePicker) node).getDate());
            }
            break;
                
            case Question.TYPE_NUMBER:
            {
                result = ((Spinner) node).getValue().toString();
            }
            break;
        }
        
        if (result != null && result.isEmpty() && question.getType() != Question.TYPE_CATEGORY)
        {
            result = null;
        }
        
        return result;
    }
    
    public static List<QuestionAnswer> getQuestionAnswers(Answer answer)
    {
        List<QuestionAnswer> result = new ArrayList<>();
        
        if (answer != null && answer.getId() > 0)
        {
            try
            {
                result = EntityService.getInstance().getValues(QuestionAnswer.class, true, new Parameter(QuestionAnswer.class.getDeclaredField("answerId"),
                                                                                                   answer.getId(),
                                                                                                   Parameter.COMPARATOR_EQUALS));
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
        
        return result;
    }
    
    public static boolean saveAnswer(AnswerWrapper wrapper)
    {
        boolean result = false;
        
        try
        {
            Answer answer = wrapper.getAnswer();
            
            if (answer.getId() == 0)
            {
                wrapper.setAnswer((Answer) EntityService.getInstance().insert(answer));
            }
            
            else
            {
                wrapper.setAnswer((Answer) EntityService.getInstance().update(answer));
            }
            
            for (QuestionAnswer questionAnswer : wrapper.getQuestionAnswers())
            {
                questionAnswer.setAnswerId(wrapper.getAnswer().getId());
                
                EntityService.getInstance().delete(questionAnswer);
                
                if (questionAnswer.getValue() != null)
                {
                    EntityService.getInstance().insert(questionAnswer);
                }
            }
            
            result = true;
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
    
    public static List<Answer> getPendingAnswers()
    {
        List<Answer> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Answer.class, new Parameter(Answer.class.getDeclaredField("ownerId"),
                                                                                       ApplicationController.getInstance().getActiveUser().getId(),
                                                                                       Parameter.COMPARATOR_EQUALS),
                                                                         new Parameter(Entity.class.getDeclaredField("state"),
                                                                                       Answer.STATE_FINISHED,
                                                                                       COMPARATOR_UNLIKE));
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }
    
    public static List<Answer> getAnswers(Survey survey)
    {
        List<Answer> result = new ArrayList<>();

        if (survey != null)
        {
            try
            {
                result = EntityService.getInstance().getValues(Answer.class, new Parameter(Answer.class.getDeclaredField("surveyId"),
                                                                                           survey.getId(),
                                                                                           Parameter.COMPARATOR_EQUALS));
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }

        return result;
    }
    
    public static double percentageFor(Answer answer)
    {
        double result = 0;

        if (answer != null)
        {
            try
            {
                result = ((BigDecimal) EntityService.getInstance().executeFunction("answer_percentage_concluded", Arrays.asList(answer.getId()))).doubleValue();
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }

        return result;
    }
}
