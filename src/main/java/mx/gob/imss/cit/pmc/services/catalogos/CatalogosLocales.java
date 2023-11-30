package mx.gob.imss.cit.pmc.services.catalogos;

import mx.gob.imss.cit.pmc.commons.dto.CatalogoDTO;
import mx.gob.imss.cit.pmc.commons.enums.IdentificadorArchivoEnum;

public interface CatalogosLocales {

	CatalogoDTO obtenerCasoRegistro(String cveCasoRegistro);

	CatalogoDTO obtenerEstadoRegistro(String cveEstadoRegistro);

	CatalogoDTO obtenerOcupacion(String cveOcupacion, IdentificadorArchivoEnum tipo);

	CatalogoDTO obtenerConsecuencia(String cveConsencuencia);

	CatalogoDTO obtenerLaudo(String cveLaudo);

	CatalogoDTO obtenerTipoRiesgo(String cveTipoRiesgo);

	CatalogoDTO obtenerNaturaleza(String cveNaturaleza);

	CatalogoDTO obtenerTipoIncapacidad(String cveTipoIncapacidad);
	
	CatalogoDTO obtenerCausaExterna(String cveCausaExterna);
	
	CatalogoDTO obtenerRiesgoFisico(String cveIdRiesgoFisico);
	
	CatalogoDTO obtenerDiagnostico(String cveIdCodigoDiagnostico);
	
	CatalogoDTO obtenerActoInseguro(String cveIdActoInseguro);
	
	CatalogoDTO obtenerClase(String cveCasoRegistro);


}