# DB changes to support moving columns to person
update person,client set person.date_of_birth  = client.date_of_birth  where person.id = client.client_id;
update person,client set person.phone_number = client.phone_number where person.id = client.client_id;
update person,client set person.place_of_birth_id  = client.place_of_birth_id  where person.id = client.client_id;
alter table client drop date_of_birth;
alter table client drop place_of_birth_id ;
alter table client drop phone_number ;
