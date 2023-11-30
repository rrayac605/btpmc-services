package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.FechaInhabilDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.DiasInhabilesRepository;

@Repository
public class DiasInhabilesRepositoryImpl implements DiasInhabilesRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public List<FechaInhabilDTO> findAll() {
		
		List<FechaInhabilDTO> d = this.mongoOperations.findAll(FechaInhabilDTO.class);
		return d;
	}

}
