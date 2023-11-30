package mx.gob.imss.cit.pmc.services.patrones;

import mx.gob.imss.cit.pmc.commons.dto.PatronDTO;
import mx.gob.imss.cit.pmc.commons.dto.RegistroDTO;
import mx.gob.imss.cit.pmc.commons.exception.PatronNoEncontradoException;

public interface PatronesService {

	PatronDTO obtenerPatron(String regPatronal) throws PatronNoEncontradoException;
	
	PatronDTO obtenerPatronOracle(String regPatronal) throws PatronNoEncontradoException;

	PatronDTO obtenerPatronInicial(RegistroDTO item) throws PatronNoEncontradoException;

}
