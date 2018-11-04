/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  MNicaretta
 * Created: Aug 4, 2018
 */

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
    state       int not null,

    constraint pk_questions         primary key (id),
    constraint fk_questions_options foreign key (ref_option) references options(id),
    constraint fk_questions_form    foreign key (ref_form) references forms(id)
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
