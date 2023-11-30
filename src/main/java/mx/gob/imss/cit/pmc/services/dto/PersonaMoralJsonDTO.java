package mx.gob.imss.cit.pmc.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties
public class PersonaMoralJsonDTO {

	@Getter
	@Setter
	private String razonSocial;

	@Getter
	@Setter
	private String rfc;

}
