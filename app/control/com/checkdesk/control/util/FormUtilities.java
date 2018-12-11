/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.AnalysisController;
import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Option;
import com.checkdesk.model.data.OptionItem;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.FormWrapper;
import com.checkdesk.model.util.OptionWrapper;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.model.util.QuestionWrapper;
import com.checkdesk.views.editors.FormEditor;
import com.checkdesk.views.editors.OptionEditor;
import com.checkdesk.views.util.Callback;
import com.checkdesk.views.parts.Prompts;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.chart.Chart;
import javafx.stage.FileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author MNicaretta
 */
public class FormUtilities
{
    public static final Item TYPE_CATEGORY = new Item("Categoria", Question.TYPE_CATEGORY);
    public static final Item TYPE_SMALL_TEXT = new Item("Texto", Question.TYPE_SMALL_TEXT);
    public static final Item TYPE_LARGE_TEXT = new Item("Texto Grande", Question.TYPE_LARGE_TEXT);
    public static final Item TYPE_SINGLE_CHOICE = new Item("Escolha Simples", Question.TYPE_SINGLE_CHOICE);
    public static final Item TYPE_MULTI_CHOICE = new Item("Escolha Multipla", Question.TYPE_MULTI_CHOICE);
    public static final Item TYPE_DATE = new Item("Data", Question.TYPE_DATE);
    public static final Item TYPE_NUMBER = new Item("Número", Question.TYPE_NUMBER);

    public static final FileChooser.ExtensionFilter XML_FILTER = new FileChooser.ExtensionFilter("eXtensible Markup Language file", "*.xml");

    public static void addForm()
    {
        Form form = new Form();

        new FormEditor(new Callback<FormWrapper>(new FormWrapper(form))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    saveForm(getSource());
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
        new FormEditor(new Callback<FormWrapper>(new FormWrapper(form))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    saveForm(getSource());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }

    public static void deleteForm(int formId)
    {
        deleteForm(getForm(formId));
    }

    public static void deleteForm(Form form)
    {
        if (Prompts.confirm("Exlusão de Formulário", "Deseja realmente excluir o formulário?"))
        {
            try
            {
                for (Question question : getQuestions(form))
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

    public static void saveForm(FormWrapper formWrapper) throws Exception
    {
        if (formWrapper.getForm().getId() == 0)
        {
            formWrapper.setForm((Form) EntityService.getInstance().insert(formWrapper.getForm()));

            save(null, formWrapper.getForm(), formWrapper.getQuestions());
        }

        else
        {
            List<Question> oldQuestions = EntityService.getInstance().getValues(Question.class,
                                                                                new Parameter(Question.class.getDeclaredField("formId"),
                                                                                        formWrapper.getForm().getId(),
                                                                                        Parameter.COMPARATOR_EQUALS));
            
            Set<QuestionWrapper> currentQuestions = new HashSet<>();
            
            formWrapper.getQuestions().values().forEach((list) ->
            {
                currentQuestions.addAll(list);
            });
            
            for (Question deletable : oldQuestions)
            {
                boolean delete = true;

                for (QuestionWrapper questionWrapper : currentQuestions)
                {
                    if (deletable.equals(questionWrapper.getQuestion()))
                    {
                        delete = false;
                        break;
                    }
                }

                if (delete)
                {
                    AttachmentUtilities.deleteAttachments(deletable);
                    EntityService.getInstance().delete(deletable);
                }
            }

            formWrapper.setForm((Form) EntityService.getInstance().update(formWrapper.getForm()));

            save(null, formWrapper.getForm(), formWrapper.getQuestions());
        }
    }
    
    private static void save(QuestionWrapper wrapper, Form form, Map<QuestionWrapper, List<QuestionWrapper>> map) throws Exception
    {
        Integer id = null;
        
        if (wrapper != null)
        {
            wrapper.getQuestion().setFormId(form.getId());

            if (wrapper.getQuestion().getId() == 0)
            {
                wrapper.setQuestion((Question) EntityService.getInstance().insert(wrapper.getQuestion()));
            }

            else
            {
                wrapper.setQuestion((Question) EntityService.getInstance().update(wrapper.getQuestion()));
            }
            
            id = wrapper.getQuestion().getId();
            
            AttachmentUtilities.saveAttachments(wrapper);
        }
        
        int count = 0;
        
        for (QuestionWrapper questionWrapper : map.get(wrapper))
        {
            questionWrapper.getQuestion().setPosition(count++);
            questionWrapper.getQuestion().setParentId(id);
            
            save(questionWrapper, form, map);
        }
    }

    public static void exportForm(Form form)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(form.getName());
        fileChooser.getExtensionFilters().add(XML_FILTER);
        File file = fileChooser.showSaveDialog(ApplicationController.getInstance().getRootWindow());

        if (file != null)
        {
            try
            {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElement("form");
                doc.appendChild(rootElement);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(form.getName()));
                rootElement.appendChild(name);

                Element info = doc.createElement("info");
                info.appendChild(doc.createTextNode(form.getInfo()));
                rootElement.appendChild(info);

                Element viewersId = doc.createElement("viewersId");
                viewersId.appendChild(doc.createTextNode(String.valueOf(form.getViewersId())));
                rootElement.appendChild(viewersId);

                Element questions = doc.createElement("questions");
                rootElement.appendChild(questions);

                for (Question question : getQuestions(form))
                {
                    Element element = doc.createElement("question");
                    questions.appendChild(element);

                    Attr attrName = doc.createAttribute("name");
                    attrName.setValue(question.getName());
                    element.setAttributeNode(attrName);

                    Attr attrType = doc.createAttribute("type");
                    attrType.setValue(String.valueOf(question.getType()));
                    element.setAttributeNode(attrType);

                    Attr attrConstraints = doc.createAttribute("constraints");
                    attrConstraints.setValue(question.getConstraints());
                    element.setAttributeNode(attrConstraints);

                    Attr attrOptionId = doc.createAttribute("optionId");
                    attrOptionId.setValue(String.valueOf(question.getOptionId()));
                    element.setAttributeNode(attrOptionId);

                    Attr attrParentId = doc.createAttribute("parentId");
                    attrParentId.setValue(String.valueOf(question.getParentId()));
                    element.setAttributeNode(attrParentId);

                    Attr attrPosition = doc.createAttribute("position");
                    attrPosition.setValue(String.valueOf(question.getPosition()));
                    element.setAttributeNode(attrPosition);
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);

                transformer.transform(source, result);
            }
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
    }

    public static FormWrapper importForm(File file)
    {
        Form form = null;
        List<Question> questionList = new ArrayList<>();

        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nListForms = doc.getElementsByTagName("form");

            Node formNode = nListForms.item(0);

            String formName = null;
            String formInfo = null;
            Integer formViewersId = null;

            if (formNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element formElement = (Element) formNode;

                formName = formElement.getElementsByTagName("name").item(0).getTextContent();
                formInfo = formElement.getElementsByTagName("info").item(0).getTextContent();
                formViewersId = parseIntIgnoreException(formElement.getElementsByTagName("viewersId").item(0).getTextContent());

                if (formName != null && formInfo != null)
                {
                    form = new Form();

                    form.setName(formName);
                    form.setInfo(formInfo);
                    form.setViewersId(formViewersId);

                    NodeList nListQuestions = doc.getElementsByTagName("question");

                    for (int i = 0; i < nListQuestions.getLength(); i++)
                    {
                        try
                        {
                            Node questions = nListQuestions.item(i);
                            String questionName = null;
                            Integer questionType = null;
                            String questionsConstraints = null;
                            Integer questionOptionId = null;
                            Integer questionParentId = null;
                            Integer questionPosition = null;

                            if (questions.getNodeType() == Node.ELEMENT_NODE)
                            {
                                Element question = (Element) questions;

                                questionName = question.getAttribute("name");
                                questionType = Integer.parseInt(question.getAttribute("type"));
                                questionsConstraints = question.getAttribute("constraints");
                                questionOptionId = parseIntIgnoreException(question.getAttribute("optionId"));
                                questionParentId = parseIntIgnoreException(question.getAttribute("parentId"));
                                questionPosition = Integer.parseInt(question.getAttribute("position"));
                            }

                            if (questionName != null && questionType != null && questionsConstraints != null)
                            {
                                Question question = new Question();

                                question.setName(questionName);
                                question.setType(questionType);
                                question.setConstraints(questionsConstraints);
                                question.setOptionId(questionOptionId);
                                question.setParentId(questionParentId);
                                question.setPosition(questionPosition);

                                questionList.add(question);
                            }
                        }
                        catch (Exception e)
                        {
                            ApplicationController.logException(e);
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return form != null ? new FormWrapper(form, questionList) : null;
    }

    private static Integer parseIntIgnoreException(String string)
    {
        Integer result = null;
        try
        {
            result = Integer.parseInt(string);
        }
        catch (Exception e)
        {
            //Silent
        }
        return result;
    }

    public static Form getForm(int formId)
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

    public static List<Form> getForms()
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

    public static List<Item> getQuestionTypes()
    {
        return getQuestionTypes(false);
    }
    
    public static List<Item> getQuestionTypes(boolean onlyOptions)
    {
        if (onlyOptions)
        {
            return Arrays.asList(TYPE_CATEGORY, TYPE_SINGLE_CHOICE, TYPE_MULTI_CHOICE);
        }
        
        return Arrays.asList(TYPE_CATEGORY,
                             TYPE_SMALL_TEXT,
                             TYPE_LARGE_TEXT,
                             TYPE_SINGLE_CHOICE,
                             TYPE_MULTI_CHOICE,
                             TYPE_DATE,
                             TYPE_NUMBER);
    }
    
    public static void addOption()
    {
        new OptionEditor(new Callback<OptionWrapper>(new OptionWrapper(new Option()))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    OptionWrapper wrapper = getSource();
                    
                    wrapper.setOption((Option) EntityService.getInstance().insert(wrapper.getOption()));
                    
                    for (OptionItem item : wrapper.getItems())
                    {
                        item.setOptionId(wrapper.getOption().getId());
                        
                        EntityService.getInstance().insert(item);
                    }
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }
    
    public static void editOption(Option option)
    {
        new OptionEditor(new Callback<OptionWrapper>(new OptionWrapper(option))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    OptionWrapper wrapper = getSource();
                    
                    List<OptionItem> oldItems = EntityService.getInstance().getValues(OptionItem.class,
                                                                                      new Parameter(OptionItem.class.getDeclaredField("optionId"),
                                                                                                    wrapper.getOption().getId(),
                                                                                                    Parameter.COMPARATOR_EQUALS));
                    for (OptionItem deletable : oldItems)
                    {
                        boolean delete = true;

                        for (OptionItem item : wrapper.getItems())
                        {
                            if (deletable.equals(item))
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

                    for (OptionItem item : wrapper.getItems())
                    {
                        if (item.getId() == 0)
                        {
                            EntityService.getInstance().insert(item);
                        }

                        else
                        {
                            EntityService.getInstance().update(item);
                        }
                    }

                    EntityService.getInstance().update(wrapper.getOption());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }
    
    public static void deleteOption(final Option option)
    {
        if (Prompts.confirm("Exlusão de Opção", "Deseja realmente excluir a opção?"))
        {
            try
            {
                for (OptionItem item : getOptionItems(option))
                {
                    EntityService.getInstance().delete(item);
                }

                EntityService.getInstance().delete(option);
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
    }

    public static Option getOption(Integer optionId)
    {
        Option result = null;

        if (optionId != null)
        {
            try
            {
                result = (Option) EntityService.getInstance().getValue(Option.class, (int) optionId);
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }

        return result;
    }

    public static List<Option> getOptions()
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
    
    public static ObservableList<Item> getOptionTypes()
    {
        return FXCollections.observableArrayList(new Item("Não tem tipo", 0));
    }
    
    public static List<OptionItem> getOptionItems(Option option)
    {
        List<OptionItem> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(OptionItem.class, true, new Parameter(OptionItem.class.getDeclaredField("optionId"),
                                                                                                 option.getId(),
                                                                                                 Parameter.COMPARATOR_EQUALS));
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static Map<QuestionWrapper, List<QuestionWrapper>> questionListToMap(List<Question> questions)
    {
        Map<QuestionWrapper, List<QuestionWrapper>> result = new HashMap<>();
        Map<Question, QuestionWrapper> questionMap = new HashMap<>();
        
        for (Question question : questions)
        {
            QuestionWrapper parent = questionMap.get(findQuestion(question.getParentId(), questions));
            
            List<QuestionWrapper> list = result.get(parent);
            
            if (list == null)
            {
                result.put(parent, list = new ArrayList<>());
            }
            
            QuestionWrapper wrapper = new QuestionWrapper(question);
            
            questionMap.put(question, wrapper);
            list.add(wrapper);
        }
        
        return result;
    }

    private static Question findQuestion(Integer id, List<Question> questions)
    {
        Question result = null;
        
        if (id != null)
        {
            for (Question question : questions)
            {
                if (question.getId() == id)
                {
                    result = question;
                    break;
                }
            }
        }
        
        return result;
    }

    public static List<Question> getQuestions(Form form)
    {
        return getQuestions(form != null ? form.getId() : 0);
    }

    public static List<Question> getQuestions(int formId)
    {
        List<Question> result = new ArrayList();

        if (formId != 0)
        {
            try
            {
                result = EntityService.getInstance().getValues(Question.class, new Parameter(Question.class.getDeclaredField("formId"),
                                                                               formId,
                                                                               Parameter.COMPARATOR_EQUALS));
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }

        return result;
    }
    
    public static Chart getQuestionChart(Form form)
    {
        List<Item> items = new ArrayList<>();
        
        for (Item item : getQuestionTypes())
        {
            try
            {
                int count = EntityService.getInstance().countValues(Question.class, new Parameter(Question.class.getDeclaredField("type"),
                                                                                                  item.getValue(),
                                                                                                  Parameter.COMPARATOR_EQUALS),
                                                                                    new Parameter(Question.class.getDeclaredField("formId"),
                                                                                                  form.getId(),
                                                                                                  Parameter.COMPARATOR_EQUALS));

                if (count > 0)
                {
                    items.add(new Item(item.getLabel(), count));
                }
            }
            
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
        
        return AnalysisController.createChart(AnalysisController.PIE_CHART, form.toString(), items);
    }
}
