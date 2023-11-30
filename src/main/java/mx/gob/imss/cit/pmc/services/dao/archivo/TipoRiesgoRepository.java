package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.TipoRiesgoDTO;

public interface TipoRiesgoRepository {

	Optional<TipoRiesgoDTO> findOneByCve(String cveTipoRiesgo);

	Optional<List<TipoRiesgoDTO>> findAll();

}
