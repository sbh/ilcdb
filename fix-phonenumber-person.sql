-- select phone_number from person where phone_number REGEXP '^\\([[:digit:]]{3}\\).[[:digit:]]{3}.[[:digit:]]{4}$';

-- select REPLACE(REPLACE(phone_number, '(', ''), ') ', '-') AS phone_number from person;

-- Run this preview first to see what will change:

   SELECT
       id,
       phone_number AS original,
       REGEXP_REPLACE(phone_number, '[^0-9]', '') AS digits,
       CASE
           WHEN CHAR_LENGTH(REGEXP_REPLACE(phone_number, '[^0-9]', '')) = 10
           THEN CONCAT(
               LEFT(REGEXP_REPLACE(phone_number, '[^0-9]', ''), 3), '-',
               MID(REGEXP_REPLACE(phone_number, '[^0-9]', ''), 4, 3), '-',
               RIGHT(REGEXP_REPLACE(phone_number, '[^0-9]', ''), 4)
           )
           ELSE ''
       END AS formatted
   FROM person
   WHERE phone_number IS NOT NULL AND phone_number != ''
   ORDER BY id;

-- If that looks good, run the update:

   UPDATE person p
   JOIN (
       SELECT id, REGEXP_REPLACE(phone_number, '[^0-9]', '') AS digits
       FROM person
   ) t ON p.id = t.id
   SET p.phone_number =
       CASE
           WHEN CHAR_LENGTH(t.digits) = 10
           THEN CONCAT(LEFT(t.digits, 3), '-', MID(t.digits, 4, 3), '-', RIGHT(t.digits, 4))
           ELSE ''
       END
   WHERE p.phone_number IS NOT NULL AND p.phone_number != '';

 -- This strips all non-digits, formats 10-digit numbers as XXX-XXX-XXXX, and blanks anything that isn't exactly 10 digits.
