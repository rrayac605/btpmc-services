package mx.gob.imss.cit.pmc.services;

import mx.gob.imss.cit.pmc.commons.dto.ParametroDTO;
import mx.gob.imss.cit.pmc.commons.exception.ParametroNoExisteException;

public interface ParametrosService {

	ParametroDTO obtenerParametro(String idParametro) throws ParametroNoExisteException;

}
