package mx.gob.imss.cit.pmc.services.dto;

import lombok.Getter;
import lombok.Setter;

public class AseguradoJsonDTO {

	@Setter
	@Getter
	private String curp;

	@Setter
	@Getter
	private int cveIdPersona;

	@Setter
	@Getter
	private String nombre;

	@Setter
	@Getter
	private String primerApellido;

	@Setter
	@Getter
	private String segundoApellido;
		
	@Setter
	@Getter
	private SubDelegacionJsonDTO subdelegacion;
	
	@Setter
	@Getter
	private UMFJsonDTO umf;

}
