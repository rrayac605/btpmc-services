package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.OcupacionDTO;
import mx.gob.imss.cit.pmc.commons.dto.OcupacionNssaDTO;
import mx.gob.imss.cit.pmc.commons.dto.OcupacionSisatDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.OcupacionRepository;

@Repository
public class OcupacionRepositoryImpl implements OcupacionRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<OcupacionDTO> findOneByCveNssa(String cveOcupacion) {
		Query query = new Query(Criteria.where("mongoOperations").is(Integer.valueOf(cveOcupacion)));
		OcupacionDTO d = this.mongoOperations.findOne(query, OcupacionNssaDTO.class);

		Optional<OcupacionDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}
	
	@Override
	public Optional<OcupacionDTO> findOneByCveSisat(String cveOcupacion) {
		Query query = new Query(Criteria.where("mongoOperations").is(Integer.valueOf(cveOcupacion)));
		OcupacionDTO d = this.mongoOperations.findOne(query, OcupacionSisatDTO.class);

		Optional<OcupacionDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<OcupacionNssaDTO>> findAllNssa() {
		List<OcupacionNssaDTO> d = this.mongoOperations.findAll(OcupacionNssaDTO.class);

		Optional<List<OcupacionNssaDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<OcupacionSisatDTO>> findAllSisat() {
		List<OcupacionSisatDTO> d = this.mongoOperations.findAll(OcupacionSisatDTO.class);

		Optional<List<OcupacionSisatDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
