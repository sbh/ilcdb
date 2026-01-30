-- Create the attorney table
DROP TABLE IF EXISTS `attorney`;
CREATE TABLE attorney (
    id BIGINT NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL DEFAULT 'Unassigned',
    last_name VARCHAR(255),
    email VARCHAR(255),
    is_active TINYINT(1) NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Add the attorney_id column to client_case table
  -- Alternative: Add with default value
  ALTER TABLE client_case
  ADD COLUMN attorney_id BIGINT NOT NULL DEFAULT 0;

-- Add foreign key constraint
ALTER TABLE client_case
ADD CONSTRAINT fk_client_case_attorney
FOREIGN KEY (attorney_id) REFERENCES attorney(id)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- Add index
CREATE INDEX idx_client_case_attorney_id ON client_case(attorney_id);

-- Insert the "Unassigned" record into the attorney table so that it gets id=1
INSERT INTO attorney (version, first_name, last_name, email, is_active)
VALUES (0, 'Unassigned', NULL, NULL, FALSE);

  -- Insert the actual attorneys (these will get auto-incremented ids starting at 1)
  INSERT INTO attorney (version, first_name, last_name, email, is_active)
  VALUES
      (0, 'Laurel', 'Herndon', 'laurel@boulderayuda.org', TRUE),
      (0, 'Belen', 'Pargas Solis', 'belen@boulderayuda.org', TRUE),
      (0, 'Maria', 'Gordillo Villa', 'maria@boulderayuda.org', TRUE),
      (0, 'Itzel', 'Cordova - Aguirre', 'itzel@boulderayuda.org', FALSE),
      (0, 'Mary', '', '', FALSE),
      (0, 'Laurel/Melissa', '', '', FALSE),
      (0, 'Mary/Melissa', '', '', FALSE),
      (0, 'Diego', '', '', FALSE),
      (0, 'Mairi', '', '', FALSE);


-- Step 2: Get attorney IDs
SELECT first_name, id  FROM attorney;
-- +----------------+----+
-- | first_name     | id |
-- +----------------+----+
-- | Unassigned     |  1 |
-- | Laurel         |  2 |
-- | Belen          |  3 |
-- | Maria          |  4 |
-- | Itzel          |  5 |
-- | Mary           |  6 |
-- | Laurel/Melissa |  7 |
-- | Mary/Melissa   |  8 |
-- | Diego          |  9 |
-- | Mairi          | 10 |
-- +----------------+----+

-- Change NULL and 'admin' attorney columns to Unassigend attorney_id in the client_case table
UPDATE client_case SET attorney_id = 1 WHERE attorney is NULL or attorney = 'admin';

-- Set attorney_id in client_case for the remaining attorney columns
UPDATE client_case SET attorney_id = 2 WHERE attorney = 'Laurel';
UPDATE client_case SET attorney_id = 3 WHERE attorney = 'Belen';
UPDATE client_case SET attorney_id = 4 WHERE attorney = 'Maria';
UPDATE client_case SET attorney_id = 5 WHERE attorney = 'Itzel';
UPDATE client_case SET attorney_id = 6 WHERE attorney = 'Mary';
UPDATE client_case SET attorney_id = 7 WHERE attorney = 'Laurel/Melissa';
UPDATE client_case SET attorney_id = 8 WHERE attorney = 'Mary/Melissa';
UPDATE client_case SET attorney_id = 9 WHERE attorney = 'Diego';
UPDATE client_case SET attorney_id = 10 WHERE attorney = 'Mairi';

-- Cleanup the remainder of rows where attorney_id is still 0.
-- These cases are where attorney is either "-Choose-" or "----"
UPDATE client_case SET attorney_id = 1, attorney = 'Unassigned' WHERE attorney_id = 0;

-- The following query should now return 0 rows
select count(*) from client_case WHERE attorney_id NOT IN (SELECT id FROM attorney);
