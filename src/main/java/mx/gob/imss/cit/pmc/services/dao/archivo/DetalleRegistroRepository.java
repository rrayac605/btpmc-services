package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.CifrasControlDTO;
import mx.gob.imss.cit.pmc.commons.dto.DetalleRegistroDTO;
import mx.gob.imss.cit.pmc.commons.dto.MovementsDupDTO;
import mx.gob.imss.cit.pmc.commons.dto.MovementsSusDTO;
import mx.gob.imss.cit.pmc.commons.dto.RegistroDTO;

public interface DetalleRegistroRepository {

	boolean existeRegistro(RegistroDTO registroDTO, String tipoArchivo);

	boolean existeRegistroRn78(RegistroDTO registroDTO, String tipoArchivo);

	List<DetalleRegistroDTO> existeSusceptible(RegistroDTO registroDTO, String tipoArchivo);

	List<DetalleRegistroDTO> existeSusceptibleNss(RegistroDTO registroDTO, String tipoArchivo);

	void guardarDetalle(DetalleRegistroDTO detalleRegistroDTO);

	int guardarDetalles(List<DetalleRegistroDTO> detalles);
	
	void actualizaDetalle(DetalleRegistroDTO archivoDTO);
	
	void actualizaDetalleDuplicado(DetalleRegistroDTO archivoDTO);

	Optional<DetalleRegistroDTO> findOneById(String userId);

	List<MovementsSusDTO> validateDuplicatedNss(String nombreArchivo);

	List<MovementsDupDTO> validateDuplicated(String nombreArchivo);
	
	CifrasControlDTO obtenerCifrasControl(String nombreArchivo);

}
