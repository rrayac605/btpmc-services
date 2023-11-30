package mx.gob.imss.cit.pmc.services.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties
public class SubDelegacionJsonDTO {

	@Setter
	@Getter
	private int clave;

	@Setter
	@Getter
	private DelegacionJsonDTO delegacion;

	@Setter
	@Getter
	private String descripcion;

	@Setter
	@Getter
	private int id;

	@Setter
	@Getter
	private Map<Integer, UMFJsonDTO> umfsJsonDTO;

}
