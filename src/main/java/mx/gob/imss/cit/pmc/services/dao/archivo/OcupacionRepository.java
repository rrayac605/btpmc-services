package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.OcupacionDTO;
import mx.gob.imss.cit.pmc.commons.dto.OcupacionNssaDTO;
import mx.gob.imss.cit.pmc.commons.dto.OcupacionSisatDTO;

public interface OcupacionRepository {

	Optional<OcupacionDTO> findOneByCveNssa(String cveOcupacion);
	
	Optional<OcupacionDTO> findOneByCveSisat(String cveOcupacion);

	Optional<List<OcupacionNssaDTO>> findAllNssa();

	Optional<List<OcupacionSisatDTO>> findAllSisat();

}
