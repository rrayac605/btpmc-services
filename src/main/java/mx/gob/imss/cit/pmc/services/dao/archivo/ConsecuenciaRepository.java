package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.ConsecuenciaDTO;

public interface ConsecuenciaRepository {

	Optional<ConsecuenciaDTO> findOneByCve(String cveConsecuencia);
	
	Optional<List<ConsecuenciaDTO>> findAll();

}
