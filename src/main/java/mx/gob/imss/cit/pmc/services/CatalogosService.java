package mx.gob.imss.cit.pmc.services;

import java.util.Date;

import mx.gob.imss.cit.pmc.commons.dto.ConsecuenciaDTO;
import mx.gob.imss.cit.pmc.commons.dto.LaudoDTO;
import mx.gob.imss.cit.pmc.commons.dto.TipoIncapacidadDTO;
import mx.gob.imss.cit.pmc.commons.dto.TipoRiesgoDTO;

public interface CatalogosService {

	TipoRiesgoDTO obtenerTipoRiesgo(String cveTipoRiesgo);

	ConsecuenciaDTO obtenerConsecuencia(String cveConsecuencia);

	LaudoDTO obtenerLaudo(String cveLaudo);
	
	TipoIncapacidadDTO obtenerTipoIncapacidad(String cveTipoIncapacidad);

	Date obtenerFechasInhabiles(int numeroDias);

}
