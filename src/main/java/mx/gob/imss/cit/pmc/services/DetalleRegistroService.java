package mx.gob.imss.cit.pmc.services;

import java.util.List;

import mx.gob.imss.cit.pmc.commons.dto.DetalleRegistroDTO;
import mx.gob.imss.cit.pmc.commons.dto.RegistroDTO;

public interface DetalleRegistroService {

	List<String> existeRegistro(String nombreArchivo);

	List<RegistroDTO> existeSusceptible(RegistroDTO registroDTO, String tipoArchivo);

	void existeSusceptibleNss(String nombreArchivo, List<String> nombres);

	int insertaRegistros(List<DetalleRegistroDTO> detalles);
	
	void insertaRegistro(DetalleRegistroDTO detalles);

	void obtenerCifrasControl(List<String> nombreArchivoList);
	
	RegistroDTO findOne(String objectId);

}