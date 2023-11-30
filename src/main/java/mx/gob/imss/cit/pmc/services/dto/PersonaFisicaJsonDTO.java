package mx.gob.imss.cit.pmc.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties
public class PersonaFisicaJsonDTO {

	@Getter
	@Setter
	private String nombre;

	@Getter
	@Setter
	private String rfc;

}
