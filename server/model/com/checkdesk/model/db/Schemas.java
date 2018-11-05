package com.checkdesk.model.db;

import com.checkdesk.model.data.*;
import com.checkdesk.model.db.fetchers.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marcelo.nicaretta
 */
public class Schemas
{
    private static final Map<Class, Schema> mapping = new HashMap<Class, Schema>();
    static
    {
        mapping.put(Answer.class,     new Answers(null));
        mapping.put(Attachment.class, new Attachments(null));
        mapping.put(Category.class,   new Categories(null));
        mapping.put(Form.class,       new Forms(null));
        mapping.put(Log.class,        new Logs(null));
        mapping.put(OptionItem.class, new OptionItems(null));
        mapping.put(Option.class,     new Options(null));
        mapping.put(Permission.class, new Permissions(null));
        mapping.put(Question.class,   new Questions(null));
        mapping.put(Survey.class,     new Surveys(null));
        mapping.put(User.class,       new Users(null));
    }
    
    public static Schema getSchema(Class type)
    {
        return mapping.get(type);
    }
    
    public static abstract class Schema
    {
        public Fetcher fetcher;
        public String select;
        public String name;
        public abstract Schema alias(String alias);
        public abstract String getField(String field);
        public String orderBy()
        {
            return "";
        };
    }

    public static class Answers extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String FEEDBACK;
            public final String DT_OCCURRED;
            public final String REF_SURVEY;
            public final String REF_USER;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID          = alias + "id";
                FEEDBACK    = alias + "feedback";
                DT_OCCURRED = alias + "dt_occurred";
                REF_SURVEY  = alias + "ref_survey";
                REF_USER    = alias + "ref_user";
                STATE       = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID          + ", " +
                       FEEDBACK    + ", " +
                       DT_OCCURRED + ", " +
                       REF_SURVEY  + ", " +
                       REF_USER    + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "answers";
        public final Columns columns;
        
        private Answers(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new AnswerFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Answers(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "surveyId":
                    result = columns.REF_SURVEY;
                    break;
                    
                case "ownerId":
                    result = columns.REF_USER;
                    break;
                    
                case "occurredDate":
                    result = columns.DT_OCCURRED;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Attachments extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String REF_QUESTION;
            public final String NAME;
            public final String TYPE;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID           = alias + "id";
                REF_QUESTION = alias + "ref_question";
                NAME         = alias + "name";
                TYPE         = alias + "type";
                STATE        = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID           + ", " +
                       REF_QUESTION + ", " +
                       NAME         + ", " +
                       TYPE         + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "attachments";
        public final Columns columns;
        
        private Attachments(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new AttachmentFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Attachments(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "questionId":
                    result = columns.REF_QUESTION;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Categories extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String NAME;
            public final String INFO;
            public final String REF_PARENT;
            public final String REF_VIEWERS;
            public final String REF_USER;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID          = alias + "id";
                NAME        = alias + "name";
                INFO        = alias + "info";
                REF_PARENT  = alias + "ref_parent";
                REF_VIEWERS = alias + "ref_viewers";
                REF_USER    = alias + "ref_user";
                STATE       = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID          + ", " +
                       NAME        + ", " +
                       INFO        + ", " +
                       REF_PARENT  + ", " +
                       REF_VIEWERS + ", " +
                       REF_USER    + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "categories";
        public final Columns columns;
        
        private Categories(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new CategoryFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Categories(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "parentId":
                    result = columns.REF_PARENT;
                    break;
                    
                case "viewersId":
                    result = columns.REF_VIEWERS;
                    break;
                    
                case "ownerId":
                    result = columns.REF_USER;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Forms extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String NAME;
            public final String INFO;
            public final String REF_VIEWERS;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID          = alias + "id";
                NAME        = alias + "name";
                INFO        = alias + "info";
                REF_VIEWERS = alias + "ref_viewers";
                STATE       = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID          + ", " +
                       NAME        + ", " +
                       INFO        + ", " +
                       REF_VIEWERS + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "forms";
        public final Columns columns;
        
        private Forms(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new FormFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Forms(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "viewersId":
                    result = columns.REF_VIEWERS;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Groups extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String REF_GROUP;
            public final String REF_USER;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID          = alias + "id";
                REF_GROUP   = alias + "ref_group";
                REF_USER    = alias + "ref_user";
            }

            @Override
            public String toString()
            {
                return REF_GROUP + ", " +
                       REF_USER;
            }
        }

        private final String TABLE_NAME = "groups";
        public final Columns columns;
        
        private Groups(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new GroupFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Groups(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "id":
                case "groupId":
                    result = columns.REF_GROUP;
                    break;
                    
                case "users":
                    result = columns.REF_USER;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Logs extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String EVENT;
            public final String OBJECT_NAME;
            public final String OBJECT_CLASS;
            public final String COMMAND;
            public final String TIMESTAMP;
            public final String REF_USER;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID           = alias + "id";
                EVENT        = alias + "event";
                OBJECT_NAME  = alias + "object_name";
                OBJECT_CLASS = alias + "object_class";
                COMMAND      = alias + "command";
                TIMESTAMP    = alias + "timestamp";
                REF_USER     = alias + "ref_user";
            }

            @Override
            public String toString()
            {
                return ID           + ", " +
                       EVENT        + ", " +
                       OBJECT_NAME  + ", " +
                       OBJECT_CLASS + ", " +
                       COMMAND      + ", " +
                       TIMESTAMP    + ", " +
                       REF_USER;
            }
        }

        private final String TABLE_NAME = "logs";
        public final Columns columns;
        
        private Logs(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new LogFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Logs(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "userId":
                    result = columns.REF_USER;
                    break;
            }
            
            return result;
        }
    }
    
    public static class OptionItems extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String NAME;
            public final String VALUE;
            public final String REF_OPTION;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID         = alias + "id";
                NAME       = alias + "name";
                VALUE      = alias + "value";
                REF_OPTION = alias + "ref_option";
            }

            @Override
            public String toString()
            {
                return ID    + ", " +
                       NAME  + ", " +
                       VALUE + ", " +
                       REF_OPTION;
            }
        }

        private final String TABLE_NAME = "option_itens";
        public final Columns columns;
        
        private OptionItems(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new OptionItemFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new OptionItems(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "optionId":
                    result = columns.REF_OPTION;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Options extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String NAME;
            public final String TYPE;
            public final String REF_VIEWERS;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID          = alias + "id";
                NAME        = alias + "name";
                TYPE        = alias + "type";
                REF_VIEWERS = alias + "ref_viewers";
                STATE       = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID          + ", " +
                       NAME        + ", " +
                       TYPE        + ", " +
                       REF_VIEWERS + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "options";
        public final Columns columns;
        
        private Options(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new OptionFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Options(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "viewersId":
                    result = columns.REF_VIEWERS;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Permissions extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String NAME;
            public final String REF_VIEWERS;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID          = alias + "id";
                NAME        = alias + "name";
                REF_VIEWERS = alias + "ref_viewers";
            }

            @Override
            public String toString()
            {
                return ID    + ", " +
                       NAME  + ", " +
                       REF_VIEWERS;
            }
        }

        private final String TABLE_NAME = "permissions";
        public final Columns columns;
        
        private Permissions(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new PermissionFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Permissions(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "viewersId":
                    result = columns.REF_VIEWERS;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Questions extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String NAME;
            public final String TYPE;
            public final String CONSTRAINTS;
            public final String REF_OPTION;
            public final String REF_FORM;
            public final String REF_PARENT;
            public final String POSITION;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID          = alias + "id";
                NAME        = alias + "name";
                TYPE        = alias + "type";
                CONSTRAINTS = alias + "constraints";
                REF_OPTION  = alias + "ref_option";
                REF_FORM    = alias + "ref_form";
                REF_PARENT  = alias + "ref_parent";
                POSITION    = alias + "position";
                STATE       = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID          + ", " +
                       NAME        + ", " +
                       TYPE        + ", " +
                       CONSTRAINTS + ", " +
                       REF_OPTION  + ", " +
                       REF_FORM    + ", " +
                       REF_PARENT  + ", " +
                       POSITION    + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "questions";
        public final Columns columns;
        
        private Questions(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new QuestionFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Questions(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "formId":
                    result = columns.REF_FORM;
                    break;
                    
                case "optionId":
                    result = columns.REF_OPTION;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Surveys extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String TITLE;
            public final String INFO;
            public final String DT_CREATED;
            public final String TYPE;
            public final String REF_USER;
            public final String REF_CATEGORY;
            public final String REF_PARTICIPANTS;
            public final String REF_VIEWERS;
            public final String REF_FORM;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID               = alias + "id";
                TITLE            = alias + "title";
                INFO             = alias + "info";
                DT_CREATED       = alias + "dt_created";
                TYPE             = alias + "type";
                REF_USER         = alias + "ref_user";
                REF_CATEGORY     = alias + "ref_category";
                REF_PARTICIPANTS = alias + "ref_participants";
                REF_VIEWERS      = alias + "ref_viewers";
                REF_FORM         = alias + "ref_form";
                STATE            = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID               + ", " +
                       TITLE            + ", " +
                       INFO             + ", " +
                       DT_CREATED       + ", " +
                       TYPE             + ", " +
                       REF_USER         + ", " +
                       REF_CATEGORY     + ", " +
                       REF_PARTICIPANTS + ", " +
                       REF_VIEWERS      + ", " +
                       REF_FORM         + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "surveys";
        public final Columns columns;
        
        private Surveys(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new SurveyFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Surveys(alias);
        }

        @Override
        public String getField(String field)
        {
            String result = field;
            
            switch (result)
            {
                case "categoryId":
                    result = columns.REF_CATEGORY;
                    break;
                    
                case "formId":
                    result = columns.REF_FORM;
                    break;
                    
                case "viewersId":
                    result = columns.REF_VIEWERS;
                    break;
                    
                case "participantsId":
                    result = columns.REF_PARTICIPANTS;
                    break;
                    
                case "ownerId":
                    result = columns.REF_USER;
                    break;
            }
            
            return result;
        }
    }
    
    public static class Users extends Schema
    {
        public static class Columns
        {
            public final String ID;
            public final String NAME;
            public final String LOGIN;
            public final String EMAIL;
            public final String PASSWORD;
            public final String PHONE;
            public final String TYPE;
            public final String STATE;

            public Columns(String alias)
            {
                if (!alias.isEmpty())
                {
                    alias += ".";
                }

                ID       = alias + "id";
                NAME     = alias + "name";
                LOGIN    = alias + "login";
                EMAIL    = alias + "email";
                PASSWORD = alias + "password";
                PHONE    = alias + "phone";
                TYPE     = alias + "type";
                STATE    = alias + "state";
            }

            @Override
            public String toString()
            {
                return ID       + ", " +
                       NAME     + ", " +
                       LOGIN    + ", " +
                       EMAIL    + ", " +
                       PASSWORD + ", " +
                       PHONE    + ", " +
                       TYPE     + ", " +
                       STATE;
            }
        }

        private final String TABLE_NAME = "users";
        public final Columns columns;
        
        private Users(String alias)
        {
            name = alias != null ? TABLE_NAME + " " + alias : TABLE_NAME;

            columns = new Columns(alias != null ? alias : TABLE_NAME);

            select = "select " + columns + " from " + name;
            
            fetcher = new UserFetcher();
        }

        @Override
        public Schema alias(String alias)
        {
            return new Users(alias);
        }

        @Override
        public String getField(String field)
        {
            return field;
        }
    }
}
