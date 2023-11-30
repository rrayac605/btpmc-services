package mx.gob.imss.cit.pmc.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties
public class FraccionJsonDTO {
	
	@Setter
	@Getter
	private int id;
	
	@Setter
	@Getter
	private String descripcion;
	
	@Setter
	@Getter
	private String descripcionDetallada;
	
	@Setter
	@Getter
	private ClaseJsonDTO clase;
	
	@Setter
	@Getter
	private GrupoJsonDTO grupo;
	
	@Setter
	@Getter
	private int numFraccion;

}
