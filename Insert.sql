insert into personne values (1, '12/11/2018', 'Pierre', 'pierre.random@domaine.com');
insert into personne values (2, '25/11/2018', 'Paul', 'paul.random@domaine.com');
insert into personne values (3, '12/11/2018', 'Jacques', 'jacques.random@domaine.com');
insert into personne values (4, '01/03/2017', 'Xavier', 'xavier.random@domaine.com');
insert into personne values (5, '01/03/2017', 'Sylvie', 'sylvie.random@domaine.com');

insert into objet values (1, 'Gomme', 'Matériel de dessin');
insert into objet values (2, 'Crayon', 'Matériel de dessin');
insert into objet values (3, 'Table', 'Décoration');
insert into objet values (4, 'Enceintes', 'Musique');
insert into objet values (5, 'Vélo', 'Sport');

insert into description values (1, 'Poids', '10g');
insert into description values (2, 'Taille', '5cm');
insert into description values (3, 'Longueur', '2m');
insert into description values (4, 'Couleur', 'Vert');
insert into description values (5, 'Puissance', '50W');

insert into message values (1, 1, 'en attente');
insert into message values (2, 1, 'accepte');
insert into message values (3, 2, 'contre propose');
insert into message values (4, 3, 'non lu');

insert into objet_description values (1, 1);
insert into objet_description values (1, 2);
insert into objet_description values (1, 3);
insert into objet_description values (2, 4);
insert into objet_description values (3, 3);
insert into objet_description values (4, 5);
insert into objet_description values (5, 4);

insert into messages_a_personne values (1, 2);
insert into messages_a_personne values (1, 3);
insert into messages_a_personne values (2, 1);
insert into messages_a_personne values (3, 4);

insert into liste_don_objet values (2, 1);
insert into liste_don_objet values (2, 2);
insert into liste_don_objet values (3, 5);

insert into liste_demande_objet values (2, 3);
insert into liste_demande_objet values (2, 4);
insert into liste_demande_objet values (3, 4);
