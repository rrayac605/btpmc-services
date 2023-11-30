package mx.gob.imss.cit.pmc.services.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties
public class UMFJsonDTO implements Serializable {

	private static final long serialVersionUID = 5853761112037187264L;

	@Setter
	@Getter
	private int idUMF;
	
	@Setter
	@Getter
	private int numUMF;

	@Setter
	@Getter
	private String descripcion;
	
	@Setter
	@Getter
	private String descUMF;

	@Setter
	@Getter
	private String nombreCorto;

	@Setter
	@Getter
	private int noEconomico;

	@Setter
	@Getter
	private SubDelegacionJsonDTO subdelegacion;

}
