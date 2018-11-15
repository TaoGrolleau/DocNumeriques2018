create table personne (
    numero_id INT PRIMARY KEY,
    date_autorisation VARCHAR(10),
    prenom VARCHAR(20),
    email VARCHAR(100)
);
/* Contient les destinataires de nos messages */

create table message (
    id_message INT PRIMARY KEY,
    id_message_parent INT FOREIGN KEY REFERENCES message(id_message), /* Optionnel */
    statut VARCHAR(20)
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
    id_objet_ref INT FOREIGN KEY REFERENCES objet(id_objet),
    id_description_ref INT FOREIGN KEY REFERENCES description(id_description),
    PRIMARY KEY (id_objet_ref, id_description_ref)
);
/* Il faudra aller chercher les descriptions de
l'objet en se servant de son ID
(toutes les description de cet objet auront cet ID dans la 1ere colonne) */

create table messages_a_personne (
    id_personne_ref INT FOREIGN KEY personne(numero_id),
    id_message_ref INT FOREIGN KEY message(id_message),
    PRIMARY KEY (id_personne_ref, id_message_ref)
);
/* Il faudra aller chercher les messages d'une
d'une personne en se servant de son ID
(tous les messages de cette personne auront cet ID dans la 1ere colonne) */

create table liste_don_objet (
    id_don_ref INT FOREIGN KEY message(id_message),
    id_objet_don_ref INT FOREIGN KEY objet(id_objet),
    PRIMARY KEY (id_don_ref, id_objet_don_ref)
);
/* Il faudra aller chercher les objets donnés d'un
message en se servant de son ID
(tous les objets de ce message auront cet ID dans la 1ere colonne) */

create table liste_demande_objet (
    id_demande_ref INT FOREIGN KEY message(id_message),
    id_objet_demand_ref INT FOREIGN KEY objet(id_objet),
    PRIMARY KEY (id_demande_ref, id_objet_demand_ref),
);
/* Il faudra aller chercher les objets demandés d'un message
en se servant de son ID
(tous les objets de ce message auront cet ID dans la 1ere colonne) */
