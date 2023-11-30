package mx.gob.imss.cit.pmc.services.enums;

import lombok.Getter;

public enum PlantillaEmailEnum {
	
	ARCHIVO_CON_ERROR("archivoConError.txt"),
	SIN_ARCHIVO("sinArchivo.txt"),
	BITACORA_CONSULTA_SIST("bitacoraConsultaSist.txt"),
	BITACORA_CONSULTA_SIST_SIN_INFO("bitacoraConsultaSistSinInfo.txt"),
	BITACORA_CONSULTA_SIST_FALLA("bitacoraConsultaSistFalla.txt");
	
	PlantillaEmailEnum(String tipoPlantilla) {
		this.tipoPlantilla = tipoPlantilla;
	}

	@Getter 
	private final String tipoPlantilla;
	
	

}
