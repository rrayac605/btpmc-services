package mx.gob.imss.cit.pmc.services.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class DelegacionJsonDTO {

	@Setter
	@Getter
	private int clave;

	@Setter
	@Getter
	private String descripcion;

	@Setter
	@Getter
	private int id;

	@Setter
	@Getter
	private int ciz;
	
	@Setter
	@Getter
	private Map<Integer, SubDelegacionJsonDTO> subDelegacionesJsonDTO;

}
