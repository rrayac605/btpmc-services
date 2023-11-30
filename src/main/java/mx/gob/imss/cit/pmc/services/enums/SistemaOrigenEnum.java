package mx.gob.imss.cit.pmc.services.enums;

import lombok.Getter;

public enum SistemaOrigenEnum {
	SISAT("sisat"), NSSA("nssa"), SUI55("sui55"), SIST("sist");

	SistemaOrigenEnum(String sistemaOrigen) {
		this.sistemaOrigen = sistemaOrigen;
	}

	@Getter
	private final String sistemaOrigen;
}
