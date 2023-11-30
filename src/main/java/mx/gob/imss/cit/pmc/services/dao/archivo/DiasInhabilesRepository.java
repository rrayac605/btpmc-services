package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.List;

import mx.gob.imss.cit.pmc.commons.dto.FechaInhabilDTO;

public interface DiasInhabilesRepository {

	List<FechaInhabilDTO> findAll();

}
