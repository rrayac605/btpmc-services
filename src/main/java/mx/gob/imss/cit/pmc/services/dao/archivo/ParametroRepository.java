package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.ParametroDTO;

public interface ParametroRepository {

	Optional<ParametroDTO> findOneByCve(String cveIdParametro);

}
