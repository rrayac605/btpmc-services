package mx.gob.imss.cit.pmc.services.catalogos;

import mx.gob.imss.cit.pmc.commons.dto.DelegacionDTO;
import mx.gob.imss.cit.pmc.commons.dto.SubDelegacionDTO;
import mx.gob.imss.cit.pmc.commons.dto.UMFDTO;

public interface CatalogosBDTUService {

	UMFDTO obtenerUMF(String delegacion, String suvdelegacion, String umf);
	
	UMFDTO obtenerUMF(String umf);

	DelegacionDTO obtenerDelegacion(String delegacion);

	SubDelegacionDTO obtenerSubDelegacion(String delegacion, String sudDelegacion);

}
