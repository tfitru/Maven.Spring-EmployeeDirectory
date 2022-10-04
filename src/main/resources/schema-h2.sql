CREATE TABLE employee (
  id INT NOT NULL,
   first_name VARCHAR(255),
   last_name VARCHAR(255),
   title VARCHAR(255),
   phone_numb VARCHAR(255),
   email VARCHAR(255),
   hire_date VARCHAR(255),
   manager_id INT,
   department_id INT,
   CONSTRAINT pk_employee PRIMARY KEY (id)
);

ALTER TABLE employee ADD CONSTRAINT FK_EMPLOYEE_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES department (id);

ALTER TABLE employee ADD CONSTRAINT FK_EMPLOYEE_ON_MANAGER FOREIGN KEY (manager_id) REFERENCES manager (id);

CREATE SEQUENCE id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE department (
  id INT NOT NULL,
   name VARCHAR(255),
   manager_id INT,
   CONSTRAINT pk_department PRIMARY KEY (id)
);

ALTER TABLE department ADD CONSTRAINT FK_DEPARTMENT_ON_MANAGER FOREIGN KEY (manager_id) REFERENCES manager (id);

CREATE SEQUENCE id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE manager (
  id INT NOT NULL,
   first_name VARCHAR(255),
   last_name VARCHAR(255),
   CONSTRAINT pk_manager PRIMARY KEY (id)
);

CREATE TABLE department_employees (
  department_id INT NOT NULL,
   employees_id INT NOT NULL,
   CONSTRAINT pk_department_employees PRIMARY KEY (department_id, employees_id)
);

ALTER TABLE department_employees ADD CONSTRAINT uc_department_employees_employees UNIQUE (employees_id);

ALTER TABLE department_employees ADD CONSTRAINT fk_depemp_on_department FOREIGN KEY (department_id) REFERENCES department (id);

ALTER TABLE department_employees ADD CONSTRAINT fk_depemp_on_employee FOREIGN KEY (employees_id) REFERENCES employee (id);

CREATE TABLE manager_employee (
  manager_id INT NOT NULL,
   employee_id INT NOT NULL,
   CONSTRAINT pk_manager_employee PRIMARY KEY (manager_id, employee_id)
);

ALTER TABLE manager_employee ADD CONSTRAINT uc_manager_employee_employee UNIQUE (employee_id);

ALTER TABLE manager_employee ADD CONSTRAINT fk_manemp_on_employee FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE manager_employee ADD CONSTRAINT fk_manemp_on_manager FOREIGN KEY (manager_id) REFERENCES manager (id);


DROP SEQUENCE hibernate_sequence;

CREATE SEQUENCE hibernate_sequence;



--DROP TABLE PERSON;
--
--CREATE TABLE PERSON (
--  ID NUMBER(10,0) NOT NULL AUTO_INCREMENT,
--  FIRST_NAME VARCHAR2(255) NOT NULL DEFAULT '',
--  LAST_NAME VARCHAR2(255) NOT NULL DEFAULT '',
--  MOBILE VARCHAR2(20) NOT NULL DEFAULT '',
--  BIRTHDAY DATE DEFAULT NULL,
--  PRIMARY KEY (ID));
--
--DROP TABLE HOME;
--
--CREATE TABLE HOME (
--  ID NUMBER(10,0) NOT NULL AUTO_INCREMENT,
--  ADDRESS VARCHAR2(255) not null default '',
--  HOMENUMBER varchar2(255) NOT NULL DEFAULT '',
--  PRIMARY KEY (ID)
--);
--
--
--DROP TABLE CAR;
--
--CREATE TABLE CAR (
--  ID NUMBER(10,0) NOT NULL AUTO_INCREMENT,
--  MAKE VARCHAR2(255) not null default '',
--  MODEL varchar2(255) NOT NULL DEFAULT '',
--  YEAR VARCHAR2(5) NOT NULL DEFAULT '01907',
--  PRIMARY KEY (ID)
--);

