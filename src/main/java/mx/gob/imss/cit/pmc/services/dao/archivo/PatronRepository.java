package mx.gob.imss.cit.pmc.services.dao.archivo;

import mx.gob.imss.cit.pmc.commons.dto.PatronDTO;

public interface PatronRepository {
	
	PatronDTO obtenerPatron(String regPatronal);

}
