package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.ClaseDTO;

public interface ClaseRepository {

	Optional<ClaseDTO> findOneByCve(String cveClase);

	Optional<List<ClaseDTO>> findAll();

}
