package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.CodigoDiagnosticoDTO;

public interface CodigoDiagnosticoRepository {

	Optional<CodigoDiagnosticoDTO> findOneByCve(String cveIdCodigoDiagnostico);

	Optional<List<CodigoDiagnosticoDTO>> findAll();

}
