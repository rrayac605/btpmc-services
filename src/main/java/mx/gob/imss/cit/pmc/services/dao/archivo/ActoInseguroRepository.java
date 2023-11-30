package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;
import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.ActoInseguroDTO;

public interface ActoInseguroRepository {

	Optional<ActoInseguroDTO> findOneByCve(String cveIdActoInseguro);

	Optional<List<ActoInseguroDTO>> findAll();

}
