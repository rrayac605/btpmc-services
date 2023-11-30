package mx.gob.imss.cit.pmc.services.dao.archivo;

import mx.gob.imss.cit.pmc.commons.dto.AseguradoDTO;

public interface AseguradoRepository {
	
	AseguradoDTO existeAsegurado(String nssAsegurado);
	
	boolean existeMarcaAfiliatoria(String nssAsegurado);

}
