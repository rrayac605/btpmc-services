package mx.gob.imss.cit.pmc.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties
public class PatronJsonDTO {

	@Setter
	@Getter
	private SubDelegacionJsonDTO subdelegacion;

	@Setter
	@Getter
	private String nombreComercial;

	@Setter
	@Getter
	private ClasificacionJsonDTO clasificacion;
	
	@Setter
	@Getter
	private PersonaMoralJsonDTO moral;
	
	@Setter
	@Getter
	private PersonaFisicaJsonDTO fisica;

}
