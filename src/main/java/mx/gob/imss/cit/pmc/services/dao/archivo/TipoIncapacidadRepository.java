package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.TipoIncapacidadDTO;

public interface TipoIncapacidadRepository {

	Optional<TipoIncapacidadDTO> findOneByCve(String cveTipoIncapacidad);

	Optional<List<TipoIncapacidadDTO>> findAll();

}
