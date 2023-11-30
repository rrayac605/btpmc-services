package mx.gob.imss.cit.pmc.services;

import mx.gob.imss.cit.pmc.commons.to.PlantillaEmailTO;
import mx.gob.imss.cit.pmc.services.enums.CasoValidadoEnum;
import mx.gob.imss.cit.pmc.services.enums.SistemaOrigenEnum;

public interface EmailParametrosService {

	PlantillaEmailTO obtenerParametrosEmail(SistemaOrigenEnum sistemaOrigen, CasoValidadoEnum caso);

}
