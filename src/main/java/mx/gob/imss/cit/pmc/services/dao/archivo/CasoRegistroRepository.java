package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.CasoRegistroDTO;

public interface CasoRegistroRepository {

	Optional<CasoRegistroDTO> findOneByCve(String cveCasoRegistro);

	Optional<List<CasoRegistroDTO>> findAll();

}
