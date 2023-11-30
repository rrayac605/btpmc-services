package mx.gob.imss.cit.pmc.services.asegurado;

import mx.gob.imss.cit.pmc.commons.dto.AseguradoDTO;
import mx.gob.imss.cit.pmc.commons.exception.AseguradonNoEncontradoException;

public interface AseguradoService {

	AseguradoDTO existeAsegurado(String nssAsegurado) throws AseguradonNoEncontradoException;
	
	AseguradoDTO existeAseguradoOracle(String nssAsegurado) throws AseguradonNoEncontradoException;

	boolean existeMarcaAfiliatoria(String nssAsegurado) throws AseguradonNoEncontradoException;

}
