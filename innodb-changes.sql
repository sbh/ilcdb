ALTER TABLE `address` ENGINE=InnoDB;
ALTER TABLE `ami` ENGINE=InnoDB;
ALTER TABLE `appointment` ENGINE=InnoDB;
ALTER TABLE `birth_place` ENGINE=InnoDB;
ALTER TABLE `case_result` ENGINE=InnoDB;
ALTER TABLE `case_type` ENGINE=InnoDB;
ALTER TABLE `client` ENGINE=InnoDB;
ALTER TABLE `client_case` ENGINE=InnoDB;
ALTER TABLE `client_sponsor_relation` ENGINE=InnoDB;
ALTER TABLE `conflict` ENGINE=InnoDB;
ALTER TABLE `country` ENGINE=InnoDB;
ALTER TABLE `database_user` ENGINE=InnoDB;
ALTER TABLE `note` ENGINE=InnoDB;
ALTER TABLE `person` ENGINE=InnoDB;
ALTER TABLE `registration_code` ENGINE=InnoDB;
ALTER TABLE `requestmap` ENGINE=InnoDB;
ALTER TABLE `role` ENGINE=InnoDB;
ALTER TABLE `service_record` ENGINE=InnoDB;
ALTER TABLE `sponsor` ENGINE=InnoDB;
ALTER TABLE `status_achieved` ENGINE=InnoDB;
ALTER TABLE `user_role` ENGINE=InnoDB;

ALTER TABLE `address`
  ADD CONSTRAINT `fk_address_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  ADD CONSTRAINT `fk_address_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`);

ALTER TABLE `appointment`
  ADD CONSTRAINT `fk_appointment_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`);

ALTER TABLE `birth_place`
  ADD CONSTRAINT `fk_birth_place_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`);

ALTER TABLE `client`
  ADD CONSTRAINT `fk_client_person` FOREIGN KEY (`client_id`) REFERENCES `person` (`id`),
  ADD CONSTRAINT `fk_client_ami` FOREIGN KEY (`ami_id`) REFERENCES `ami` (`id`);

ALTER TABLE `client_case`
  ADD CONSTRAINT `fk_client_case_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  ADD CONSTRAINT `fk_client_case_type` FOREIGN KEY (`case_type_id`) REFERENCES `case_type` (`id`),
  ADD CONSTRAINT `fk_client_case_result` FOREIGN KEY (`case_result_id`) REFERENCES `case_result` (`id`);

ALTER TABLE `client_sponsor_relation`
  ADD CONSTRAINT `fk_client_relation_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  ADD CONSTRAINT `fk_client_relation_sponsor` FOREIGN KEY (`sponsor_id`) REFERENCES `sponsor` (`id`);

ALTER TABLE `conflict`
  ADD CONSTRAINT `fk_conflict_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`);

ALTER TABLE `note`
  ADD CONSTRAINT `fk_note_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  ADD CONSTRAINT `fk_note_intake` FOREIGN KEY (`intake_id`) REFERENCES `client_case` (`id`);

ALTER TABLE `person`
  ADD CONSTRAINT `fk_person_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  ADD CONSTRAINT `fk_person_birth_place` FOREIGN KEY (`place_of_birth_id`) REFERENCES `birth_place` (`id`);

ALTER TABLE `service_record`
  ADD CONSTRAINT `fk_service_record_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`);

ALTER TABLE `sponsor`
  ADD CONSTRAINT `fk_sponsor_person` FOREIGN KEY (`sponsor_id`) REFERENCES `person` (`id`);

ALTER TABLE `status_achieved`
  ADD CONSTRAINT `fk_status_achieved_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`);

ALTER TABLE `user_role`
  ADD CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `database_user` (`id`);

/* Might have to do:
delete from note where `client_id` NOT IN (SELECT `id` FROM `client`);
delete from note where `intake_id` NOT IN (SELECT `id` FROM `client_case`);
delete from service_record where `client_id` NOT IN (SELECT `id` FROM `client`);
*/
