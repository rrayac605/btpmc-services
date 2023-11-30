package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.NaturalezaDTO;

public interface NaturalezaRepository {

	Optional<NaturalezaDTO> findOneByCve(String cveNaturaleza);

	Optional<List<NaturalezaDTO>> findAll();

}
