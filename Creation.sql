create table personne (
    numero_auto INT PRIMARY KEY,
    date_autorisation VARCHAR(10),
    nom VARCHAR(20),
    prenom VARCHAR(20),
    email VARCHAR(100)
);
/* Contient les destinataires de nos messages */

create table message (
    id_message INT PRIMARY KEY,
    id_message_parent INT, /* Optionnel */
    statut VARCHAR(20),
    FOREIGN KEY (id_message_parent) REFERENCES message(id_message)
);

create table objet (
    id_objet INT PRIMARY KEY,
    nom VARCHAR (100),
    type VARCHAR(50)
);

create table description (
    id_description INT PRIMARY KEY,
    nom VARCHAR(100),
    valeur VARCHAR(100)
);

create table objet_description (
    id_objet_ref INT,
    id_description_ref INT,
    PRIMARY KEY (id_objet_ref, id_description_ref),
    FOREIGN KEY (id_objet_ref) REFERENCES objet(id_objet),
    FOREIGN KEY (id_description_ref) REFERENCES description(id_description)
);
/* Il faudra aller chercher les descriptions de
l'objet en se servant de son ID
(toutes les description de cet objet auront cet ID dans la 1ere colonne) */

create table messages_a_personne (
    id_personne_ref INT,
    id_message_ref INT,
    PRIMARY KEY (id_personne_ref, id_message_ref),
    FOREIGN KEY (id_personne_ref) REFERENCES personne(numero_auto),
    FOREIGN KEY (id_message_ref) REFERENCES message(id_message)
);
/* Il faudra aller chercher les messages d'une
d'une personne en se servant de son ID
(tous les messages de cette personne auront cet ID dans la 1ere colonne) */

create table liste_don_objet (
    id_don_ref INT,
    id_objet_don_ref INT,
    PRIMARY KEY (id_don_ref, id_objet_don_ref),
    FOREIGN KEY (id_don_ref) REFERENCES message(id_message),
    FOREIGN KEY (id_objet_don_ref) REFERENCES objet(id_objet)
);
/* Il faudra aller chercher les objets donnés d'un
message en se servant de son ID
(tous les objets de ce message auront cet ID dans la 1ere colonne) */

create table liste_demande_objet (
    id_demande_ref INT,
    id_objet_demand_ref INT,
    PRIMARY KEY (id_demande_ref, id_objet_demand_ref),
    FOREIGN KEY (id_demande_ref) REFERENCES message(id_message),
    FOREIGN KEY (id_objet_demand_ref) REFERENCES objet(id_objet)
);
/* Il faudra aller chercher les objets demandés d'un message
en se servant de son ID
(tous les objets de ce message auront cet ID dans la 1ere colonne) */
