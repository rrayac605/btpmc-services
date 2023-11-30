package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.EstadoRegistroDTO;

public interface EstadoRegistroRepository {

	Optional<EstadoRegistroDTO> findOneByCve(String cveCasoRegistro);

	Optional<List<EstadoRegistroDTO>> findAll();

}
