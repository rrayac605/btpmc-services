package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.CifrasControlDTO;

public interface CifrasControlRepository {

	Optional<CifrasControlDTO> obtenerCifrasControl(String archivoId);

}
