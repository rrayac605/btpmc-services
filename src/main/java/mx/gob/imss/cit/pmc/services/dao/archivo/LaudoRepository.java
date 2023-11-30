package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.LaudoDTO;

public interface LaudoRepository {

	Optional<LaudoDTO> findOneByCve(String cveLaudo);

	Optional<List<LaudoDTO>> findAll();

}
