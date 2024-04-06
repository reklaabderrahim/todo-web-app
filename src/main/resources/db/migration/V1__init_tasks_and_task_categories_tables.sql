CREATE TABLE task_categories (
    category_id             NUMBER              NOT NULL,
    category_name           VARCHAR2(100)       NOT NULL,
    category_description    VARCHAR2(500)       NOT NULL,
    CONSTRAINT task_categories_pk PRIMARY KEY (category_id),
    constraint category_name_uk UNIQUE (category_name),
    /* H2 DB doesn't accept VARCHAR2(100 BYTE), so we can use the LENGTH function to get the length of a string in bytes.*/
    CONSTRAINT task_categories_check_name_length CHECK (LENGTH(category_name) <= 100),
    CONSTRAINT task_categories_check_description_length CHECK (LENGTH(category_description) <= 500)
);

CREATE SEQUENCE if not exists task_categories_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE tasks (
    task_id             NUMBER              NOT NULL,
    task_name           VARCHAR2(100)       NOT NULL,
    task_description    VARCHAR2(500)       NOT NULL,
    deadline            TIMESTAMP           NOT NULL,
    category_id         NUMBER              NOT NULL,
    CONSTRAINT tasks_pk PRIMARY KEY (task_id),
    CONSTRAINT task_categories_fk FOREIGN KEY (category_id) REFERENCES task_categories(category_id),
    /* H2 DB doesn't accept VARCHAR2(100 BYTE), so we can use the LENGTH function to get the length of a string in bytes.*/
    CONSTRAINT task_check_name_length CHECK (LENGTH(task_name) <= 100),
    CONSTRAINT task_check_description_length CHECK (LENGTH(task_description) <= 500)
);

CREATE SEQUENCE if not exists tasks_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;