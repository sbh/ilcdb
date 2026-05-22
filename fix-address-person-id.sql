-- Fix orphaned addresses caused by saving Address before Person in
-- ClientController.save() and SponsorController.save().
-- Address has belongsTo = [person: Person], so address.person_id must
-- reference person.id, but was left NULL because Person had no ID yet
-- when Address was saved.

-- Step 1: Fix addresses where address.id matched client.id
-- (address rows created alongside client via the broken save order)
update address a
  inner join client c on a.id = c.id
set a.person_id = c.client_id
where a.person_id is null;

-- Step 2: Fix remaining addresses where address.id matched person.id
-- (address rows created outside the client relationship, or before
-- the bug was introduced)
update address a
  inner join person p on a.id = p.id
set a.person_id = p.id
where a.person_id is null;

-- Diagnostic: count remaining orphans
select count(*) from address where person_id is null;

-- Diagnostic: people without addresses (should be 0 for actual clients)
select p.id, p.first_name, p.last_name
from person p
  inner join client c on c.client_id = p.id
  left join address a on a.person_id = p.id
where a.id is null;
