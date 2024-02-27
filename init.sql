DROP SCHEMA IF EXISTS pae CASCADE;

CREATE SCHEMA pae;

CREATE TABLE pae.roles
(
    id_role SERIAL PRIMARY KEY,
    role    VARCHAR NOT NULL
);

CREATE TABLE pae.users
(
    id_user           SERIAL PRIMARY KEY,
    last_name         VARCHAR(50)  NOT NULL,
    first_name        VARCHAR(50)  NOT NULL,
    email             VARCHAR(100) NOT NULL,
    password          CHAR(64)     NOT NULL,
    phone_number      VARCHAR(20)  NOT NULL,
    profile_picture   VARCHAR(50)  NOT NULL,
    registration_date DATE         NOT NULL,
    role              INTEGER REFERENCES pae.roles (id_role),
    version_number    INTEGER
);

CREATE TABLE pae.availabilities
(
    id_availability SERIAL PRIMARY KEY,
    availability    DATE NOT NULL
);

CREATE TABLE pae.item_types
(
    id_item_type SERIAL PRIMARY KEY,
    label        VARCHAR(50) NOT NULL
);

CREATE TABLE pae.item_states
(
    id_item_state SERIAL PRIMARY KEY,
    label         VARCHAR(50) NOT NULL
);

CREATE TABLE pae.items
(
    id_item                SERIAL PRIMARY KEY,
    time_slot              VARCHAR(20)                                             NOT NULL,
    item_type              INTEGER REFERENCES pae.item_types (id_item_type)        NOT NULL,
    description            TEXT                                                    NOT NULL,
    date_park_arrival      INTEGER REFERENCES pae.availabilities (id_availability) NOT NULL,
    state                  INTEGER REFERENCES pae.item_states (id_item_state)      NOT NULL,
    offering_user          INTEGER REFERENCES pae.users (id_user)                  NULL,
    phone_number           VARCHAR(20)                                             NULL,
    picture                VARCHAR(50)                                             NOT NULL,
    reason_for_refusal     TEXT                                                    NULL,
    selling_price          NUMERIC(10, 2)                                          NULL,
    selling_date           DATE                                                    NULL,
    store_deposit_date     DATE                                                    NULL,
    market_withdrawal_date DATE                                                    NULL,
    date_of_recipt         DATE                                                    NULL,
    version_number         INTEGER                                                 NOT NULL;
);

CREATE TABLE pae.notifications
(
    id_notification SERIAL PRIMARY KEY,
    item            INTEGER REFERENCES pae.items (id_item) NOT NULL,
    creation_date   DATE                                   NOT NULL,
    notification_text VARCHAR(201)                         NOT NULL
);

CREATE TABLE pae.notification_reads
(
    id_lecture    SERIAL PRIMARY KEY,
    is_read       BOOLEAN                                                NOT NULL,
    notification  INTEGER REFERENCES pae.notifications (id_notification) NOT NULL,
    notified_user INTEGER REFERENCES pae.users (id_user)                 NOT NULL

);

INSERT INTO pae.roles(role)
VALUES ('User');
INSERT INTO pae.roles(role)
VALUES ('Helper');
INSERT INTO pae.roles(role)
VALUES ('Manager');

-- filling of data in the users table
INSERT INTO pae.users (last_name, first_name, email, password,role,phone_number,version_number)
VALUES
    ('Riez', 'Robert', 'bert.riez@gmail.be', '$2a$12$XkQfSQOOi3p2hISulWXBOurSZCWJkBLh4j2ZQ0XiZJyRvLJ/QE4Bi', 3, '0477/96.85.47',1),
    ('Muise', 'Alfred', 'fred.muise@gmail.be', '$2a$12$Tr0PBRXi5MVDUcOrmUnzke0/C0gCgtUxQV593kGwzdNU48br/oZ5u', 2, '0476/96.36.26',1),
    ('Line', 'Charles', 'charline@proximus.be', '$2a$12$K8.tWfZ8u1sifDhOzVhuW.knAP1nJnFcTRNM7Th6z5UiqeDKH8AUS', 2, '0481 35 62 49',1),
    ('Line', 'Caroline', 'caro.line@hotmail.com', '$2a$10$xB.Om091nJxj6jRGiaRrmuJiF3BMB4JA6IlNs3ME9Ww6P/HRLe79S', 1, '0487/45.23.79',1),
    ('Ile', 'Achille', 'ach.ile@gmail.com', '$2a$10$xB.Om091nJxj6jRGiaRrmuJiF3BMB4JA6IlNs3ME9Ww6P/HRLe79S', 1, '0477/65.32.24',1),
    ('Ile', 'Basile', 'bas.ile@gmail.be', '$2a$10$xB.Om091nJxj6jRGiaRrmuJiF3BMB4JA6IlNs3ME9Ww6P/HRLe79S', 1, '0485/98.86.42',1),
    ('Ile', 'Théophile', 'theo.phile@proximus.be', '$2a$10$xB.Om091nJxj6jRGiaRrmuJiF3BMB4JA6IlNs3ME9Ww6P/HRLe79S', 1, '0488 35 33 89',1);

-- filling of data in the item_types table
INSERT INTO pae.item_types (label)
VALUES ('Meuble');
INSERT INTO pae.item_types (label)
VALUES ('Table');
INSERT INTO pae.item_types (label)
VALUES ('Chaise');
INSERT INTO pae.item_types (label)
VALUES ('Fauteuil');
INSERT INTO pae.item_types (label)
VALUES ('Lit/sommier');
INSERT INTO pae.item_types (label)
VALUES ('Matelas');
INSERT INTO pae.item_types (label)
VALUES ('Couvertures');
INSERT INTO pae.item_types (label)
VALUES ('Matériel de cuisine');
INSERT INTO pae.item_types (label)
VALUES ('Vaisselle');


-- filling of data in the item_states table
INSERT INTO pae.item_states (label)
VALUES ('proposé');
INSERT INTO pae.item_states (label)
VALUES ('refusé');
INSERT INTO pae.item_states (label)
VALUES ('accepté');
INSERT INTO pae.item_states (label)
VALUES ('à l''atelier');
INSERT INTO pae.item_states (label)
VALUES ('en magasin');
INSERT INTO pae.item_states (label)
VALUES ('en vente');
INSERT INTO pae.item_states (label)
VALUES ('vendu');
INSERT INTO pae.item_states (label)
VALUES ('retiré de la vente');

-- filling of data in the availabilities table
INSERT INTO pae.availabilities (availability)
VALUES ('2023-03-04');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-03-11');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-03-18');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-03-25');

INSERT INTO pae.availabilities (availability)
VALUES ('2023-04-01');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-04-15');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-04-22');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-04-29');

INSERT INTO pae.availabilities (availability)
VALUES ('2023-05-13');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-05-27');

INSERT INTO pae.availabilities (availability)
VALUES ('2023-06-03');
INSERT INTO pae.availabilities (availability)
VALUES ('2023-06-17');


-- filling of data in the items table

INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user,
                       phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date,
                       market_withdrawal_date, date_of_recipt, version_number) VALUES
    ('11:00 - 13:00', 3, 'Chaise en bois brut avec cousin beige', 3, 6, 6, null,
     'Chaise-wooden-gbe3bb4b3a_1280.png', null, 2.00, null, '2022-03-23', null, '2023-03-15', 1);


INSERT INTO pae.items ( time_slot, item_type, description, date_park_arrival, state, offering_user, phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('11:00 - 13:00', 4,'Canapé 3 placesblanc', 3, 7, 6, null, 'Fauteuil-sofa-g99f90fab2_1280.jpg', null, 3.00, '2023-03-23', '2023-03-23', null, '2023-03-15', 1);



INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user, phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('14:00 - 16:00', 1, 'Secretaire2', 4, 2, null, '0496 32 16 54', 'Secretaire.png', 'Ce meuble est magnifique mais fragile pour l’usage qui en sera fait', null, null, null, null, '2023-03-15', 1);





INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state,offering_user, phone_number,
                       picture, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt,version_number)
VALUES ('14:00 - 16:00', 9, '100 assiettes blanches', 4, 5,5,
        null, 'Vaisselle-playe-629970_1280.jpg', null, null,'2023-03-29', null, '2023-03-20',1);



INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user,
                       phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('14:00 - 16:00', 4, 'Grand canapé 4 places bleu usé', 4, 7, 5, null, 'Fauteuil-chair-g6374c21b8_1280.jpg', null, 3.50, '2023-03-29', '2023-03-29', null, '2023-03-20', 1);



INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user,
                       phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('14:00 - 16:00', 4, 'Fauteuil design très confortable', 3, 8, 5, null, 'Fauteuil-design-gee14e1707_1280.jpg', null, 5.20, null, '2023-03-18', '2023-04-29', '2023-03-15', 1);



INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user, phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('14:00 - 16:00', 3, 'Tabouret de bar en cuir', 5, 2, 5, null, 'bar-890375_1920.jpg', 'Ceci n est pas une chaise', null, null, null, null, '2023-03-28', 1);



INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user,
                       phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('11:00 - 13:00', 4, 'Fauteuil ancien,pieds et accoudoir en bois',7, 4, 6, null, 'Fauteuil-chair-g6374c21b8_1280.jpg', null, null, null,null, null, '2023-04-11', 1);





INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user,
                       phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('11:00 - 13:00', 9, '6 bols à soupe', 7, 5, 6, null, 'Fauteuil-chair-g6374c21b8_1280.jpg', null, null, null, '2023-04-25', null, '2023-04-11', 1);



INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state,offering_user, phone_number,
                       picture, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('14:00 - 16:00', 6, 'Lit cage blanc', 7, 5,7,
        '0643210876', 'LitEnfant-nursery-g9913b3b19_1280.jpg', null, null,
        '2023-04-25', null, '2023-04-11',1);

INSERT INTO pae.items ( time_slot, item_type, description, date_park_arrival, state, offering_user,
                        phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('11:00 - 13:00', 9, '30 pots à épices', 8, 5, 7, null, 'PotEpices-pharmacy-g01563afff_1280.jpg', null, null, null, '2023-05-05', null, '2023-04-18', 1);


INSERT INTO pae.items (time_slot, item_type, description, date_park_arrival, state, offering_user,
                       phone_number, picture, reason_for_refusal, selling_price, selling_date, store_deposit_date, market_withdrawal_date, date_of_recipt, version_number)
VALUES ('11:00 - 13:00', 9, '4 tasses à café et leurs sous-tasses', 8, 5, 4, null, 'Vaiselle-Tassecup-1320578_1280.jpg', null, null, null, '2023-05-05', null, '2023-04-18', 1);



