package mx.gob.imss.cit.pmc.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties
public class DivisionJsonDTO {

	@Setter
	@Getter
	private int id;

	@Setter
	@Getter
	private String descripcion;
	
	@Setter
	@Getter
	private int numDivision;

}
