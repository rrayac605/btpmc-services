package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.RiesgoFisicoDTO;

public interface RiesgoFisicoRepository {

	Optional<RiesgoFisicoDTO> findOneByCve(String cveIdRiesgoFisico);

	Optional<List<RiesgoFisicoDTO>> findAll();

}
