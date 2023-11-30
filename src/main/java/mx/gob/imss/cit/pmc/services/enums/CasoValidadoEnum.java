package mx.gob.imss.cit.pmc.services.enums;

import lombok.Getter;

public enum CasoValidadoEnum {
	ERROR_NUM_REG("Total_de_registros_no_coincide", 1), ERROR_ARCHIVO_MISMO_NOMBRE("Archivo_mismo_nombre", 2),
	ERROR_ARCHIVO_NO_EXISTE("Archivo_no_existe", 3), ERROR_ARCHIVO_YA_PROCESADO("Archivo_ya_procesado", 4);

	CasoValidadoEnum(String sistemaOrigen, Integer id) {
		this.sistemaOrigen = sistemaOrigen;
		this.id = id;
	}

	@Getter
	private final String sistemaOrigen;
	@Getter
	private final Integer id;
}
