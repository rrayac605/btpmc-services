package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.CausaExternaDTO;

public interface CausaExternaRepository {

	Optional<CausaExternaDTO> findOneByCve(String cveIdCausaExterna);

	Optional<List<CausaExternaDTO>> findAll();

}
