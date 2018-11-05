/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  MNicaretta
 * Created: Aug 4, 2018
 */

-------------------------------- Tables --------------------------------
-- users
create table users
(
    id       serial not null,
    name     varchar(200) not null,
    login    varchar(100) not null,
    email    varchar(200) not null,
    password varchar(100) not null,
    phone    varchar(20) not null,
    type     int not null,
    state    int not null,

    constraint pk_users primary key (id)
);

-- groups
create table "groups"
(
    id        serial not null,
    ref_group int not null,
    ref_user  int not null,

    constraint pk_groups primary key (id),
    constraint fk_groups_user foreign key (ref_user) references users(id),
    constraint uq_groups unique (ref_group, ref_user)
);

-- logs
create table logs
(
    id           serial not null,
    event        int not null,
    object_name  varchar(200) not null,
    object_class varchar(200) not null,
    command      text not null,
    timestamp    timestamp not null,
    ref_user     int not null,

    constraint pk_logs      primary key (id),
    constraint fk_logs_user foreign key (ref_user) references users(id)
);

-- permissions
create table permissions
(
    id          serial not null,
    name        varchar(100) not null,
    ref_viewers int null,

    constraint pk_permissions         primary key (id),
    constraint fk_permissions_viewers foreign key (ref_viewers) references "groups"(id)
);

-- forms
create table forms
(
    id          serial not null,
    name        varchar(200) not null,
    info        text not null,
    ref_viewers int null,
    state       int not null,

    constraint pk_forms         primary key (id),
    constraint fk_forms_viewers foreign key (ref_viewers) references "groups"(id)
);

-- options
create table options
(
    id          serial not null,
    name        varchar(200) not null,
    type        int not null,
    ref_viewers int null,
    state       int not null,

    constraint pk_options         primary key (id),
    constraint fk_options_viewers foreign key (ref_viewers) references "groups"(id)
);

-- categories
create table categories
(
    id          serial not null,
    name        varchar(200) not null,
    info        text not null,
    ref_parent  int null,
    ref_viewers int null,
    ref_user    int not null,
    state       int not null,

    constraint pk_categories         primary key (id),
    constraint fk_categories_parent  foreign key (ref_parent) references categories(id),
    constraint fk_categories_viewers foreign key (ref_viewers) references "groups"(id),
    constraint fk_categories_user    foreign key (ref_user) references users(id)
);

-- option_itens
create table option_itens
(
    id         serial not null,
    name       varchar(200) not null,
    value      varchar(200) not null,
    ref_option int not null,

    constraint pk_option_itens        primary key (id),
    constraint fk_option_itens_option foreign key (ref_option) references options(id)
);

-- questions
create table questions
(
    id          serial not null,
    name        varchar(200) not null,
    type        int not null,
    constraints varchar(200) not null,
    ref_option  int null,
    ref_form    int not null,
    ref_parent  int null,
    position    int not null,
    state       int not null,

    constraint pk_questions         primary key (id),
    constraint fk_questions_options foreign key (ref_option) references options(id),
    constraint fk_questions_form    foreign key (ref_form) references forms(id),
    constraint fk_questions_parent  foreign key (ref_parent) references questions(id)
);

-- surveys
create table surveys
(
    id               serial not null,
    title            varchar(200) not null,
    info             text not null,
    dt_created       timestamp not null,
    type             int not null,
    ref_user         int not null,
    ref_category     int not null,
    ref_participants int null,
    ref_viewers      int null,
    ref_form         int not null,
    state            int not null,

    constraint pk_surveys             primary key (id),
    constraint fk_survey_user         foreign key (ref_user) references users(id),
    constraint fk_survey_category     foreign key (ref_category) references categories(id),
    constraint fk_survey_viewers      foreign key (ref_viewers) references "groups"(id),
    constraint fk_survey_participants foreign key (ref_participants) references "groups"(id),
    constraint fk_survey_form         foreign key (ref_form) references forms(id)
);

-- answers
create table answers
(
    id          serial not null,
    feedback    text not null,
    dt_occurred timestamp not null,
    ref_survey  int not null,
    ref_user    int not null,
    state       int not null,

    constraint pk_answers        primary key (id),
    constraint fk_answers_survey foreign key (ref_survey) references surveys(id),
    constraint fk_answers_user   foreign key (ref_user) references users(id)
);


-- question_answers
create table question_answers
(
    ref_question int not null,
    ref_answer   int not null,
    value        varchar(200) not null,

    constraint pk_question_answers          primary key (ref_question, ref_answer),
    constraint fk_question_answers_question foreign key (ref_question) references questions(id),
    constraint fk_question_answers_answer   foreign key (ref_answer) references answers(id)
);

create table attachments
(
    id           serial not null,
    ref_question int not null,
    name         varchar(200) not null,
    type         varchar(5) not null,
    state        int not null,

    constraint pk_files primary key (id),
    constraint fk_files_question foreign key (ref_question) references questions(id)
);

-------------------------------- Stored Procedures --------------------------------

--Function que retorna a quantidade de cada tipo das pesquisas
CREATE OR REPLACE FUNCTION survey_type_count
(
    OUT count_public INT,
    OUT count_private INT,
    OUT count_annonymus INT,
    OUT count_totem INT
)
AS $$
BEGIN
    -- Privada
    SELECT count(*) INTO count_public
    FROM surveys
    WHERE type = 0;
    -- Pública
    SELECT count(*) INTO count_private
    FROM surveys
    WHERE type = 1;
    -- Anônima
    SELECT count(*) INTO count_annonymus
    FROM surveys
    WHERE type = 2;
    -- Anônima
    SELECT count(*) INTO count_totem
    FROM surveys
    WHERE type = 3;
END; $$
LANGUAGE plpgsql;

--Function retorna o percentual de conclusão de uma pesquisa
--Parâmetros: survey_id
CREATE OR REPLACE FUNCTION answer_percentage_concluded
(	
    IN answer_id INT,
    OUT percentage_executed  DECIMAL(10,9)
)
AS $$
    DECLARE count_questions DECIMAL(10,9);
    DECLARE count_answers DECIMAL(10,9);
BEGIN
    SELECT COUNT(*) INTO count_questions
    FROM questions q, forms f, surveys s, answers a
    WHERE q.ref_form = f.id AND f.id = s.ref_form AND s.id = a.ref_survey AND a.id = answer_id;

    SELECT COUNT(*) INTO count_answers
    FROM question_answers qa
    WHERE qa.ref_answer = answer_id;
	
    IF count_questions > 0 THEN
        SELECT (count_answers/count_questions) INTO percentage_executed;
    ELSE
        percentage_executed = 0;
    END IF;
END; $$
LANGUAGE plpgsql;

--Function realiza o insert da tabela de logs para uma tabela secundária
--Parâmetros: table_name
--parametros de data e registro de transferência das auditorias
CREATE OR REPLACE FUNCTION transfer_logs_to_aux_table
(	
    IN table_name_param VARCHAR(50),
    IN timestamp_init TIMESTAMP,
    IN timestamp_fin TIMESTAMP
)
RETURNS INTEGER	AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = table_name_param) THEN
        EXECUTE format(
            'CREATE TABLE IF NOT EXISTS %I
            (
                id           INT NOT NULL ,
                event        INT NOT NULL ,
                object_name  VARCHAR(200) NOT NULL ,
                object_class VARCHAR(200) NOT NULL ,
                command      TEXT NOT NULL ,
                timestamp    TIMESTAMP NOT NULL ,
                ref_user     INT NOT NULL ,

                CONSTRAINT PK_%I PRIMARY KEY (id),
                CONSTRAINT FK_%I_User FOREIGN KEY (ref_user)
                REFERENCES users(id)
            );',
            table_name_param,
            table_name_param,
            table_name_param
        );
    END IF;

    EXECUTE format(
        'INSERT INTO %I (id, event, object_name, object_class, command, timestamp, ref_user)
        SELECT id, event, object_name, object_class, command, timestamp, ref_user
        FROM logs
        WHERE to_char(timestamp, ''YYYY-MM-DD'') between to_char($1, ''YYYY-MM-DD'') and to_char($2, ''YYYY-MM-DD'');',
        table_name_param
    ) USING timestamp_init, timestamp_fin;

    DELETE from logs where to_char(timestamp, 'YYYY-MM-DD') between to_char($2, 'YYYY-MM-DD') and to_char($3, 'YYYY-MM-DD');

    RETURN 1;
END; $$
LANGUAGE plpgsql;

--Function busca as pesquisas pendentes de um usuário
--Parâmetros: user_id
CREATE OR REPLACE FUNCTION user_pending_surveys
(
    IN user_id INT
)
RETURNS TABLE(id integer, survey_name character varying) AS $$
BEGIN
    RETURN QUERY
            SELECT s.id, s.title
            FROM answers a, surveys s
            WHERE a.survey = s.id and a.ref_user = user_id;
END; $$
LANGUAGE plpgsql;

--Function busca as pesquisas pendentes de um usuário
--Parâmetros: user_id
CREATE OR REPLACE FUNCTION obsolete_surveys()
RETURNS TABLE(id integer, survey_name character varying, survey_dt_created date) AS $$
BEGIN
    RETURN QUERY
            SELECT s.id, s.title, s.dt_created
            FROM answers a, surveys s
            WHERE a.survey = s.id and s.dt_created <= DATEADD(mm, -6, NOW())
            HAVING COUNT(a.id) = 0;
END;
$$ LANGUAGE plpgsql;

-------------------------------- Views --------------------------------

--View que elenca as informações mais importantes das tabelas de pesquisa, pergunta e resposta
CREATE VIEW 
    survey_field_answers AS
    SELECT s.id as "survey_id", s.title as "survey_title", q.id as "question_id", q.name as "question_name", q.type as "question_type", qa.value as "answer_value"
    FROM surveys s, forms f, questions q, question_answers qa
    WHERE s.ref_form = f.id AND q.ref_form = f.id AND qa.ref_question = q.id;

--View que elenca as permissões de cada usuário
CREATE VIEW 
    user_permissions AS
    SELECT u.id as "user_id", u.name as "user_name", p.id as "permission_id", p.name as "permission_name"
    FROM users u, groups g, permissions p
    WHERE u.id = g.ref_user AND g.ref_group = p.ref_viewers;

--View que elenca o ranking de cada usuário
-- CREATE VIEW
--     user_rankings AS
--     SELECT u.id as "user_id", u.name as "user_name", SUM(rs.score)
--     FROM users u, rank_statements rs
--     WHERE u.id = rs.ref_user
--     GROUP BY u.id, u.name;

--View que elenca os percentual de conclusão de cada pesquisa
CREATE VIEW 
    survey_percentages AS
    SELECT s.id as "survey_id", s.title as "survey_name", (SELECT answer_percentage_concluded(s.id))
    FROM surveys s;

--View que elenca os itens por pergunta
CREATE VIEW 
    questions_details AS
    SELECT q.id as "question_id", q.type as "question_type", q.constraints as "question_constraint", o.type as "item_id"
    FROM questions q, options o
    WHERE q.ref_option = o.id;
