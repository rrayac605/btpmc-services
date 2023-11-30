package mx.gob.imss.cit.pmc.services;

import mx.gob.imss.cit.pmc.commons.to.MailTO;
import mx.gob.imss.cit.pmc.services.enums.PlantillaEmailEnum;
import mx.gob.imss.cit.pmc.services.exception.EmailException;

public interface EmailService {
	void sendEmail(MailTO mail, PlantillaEmailEnum plantillaEmail) throws EmailException;
}
