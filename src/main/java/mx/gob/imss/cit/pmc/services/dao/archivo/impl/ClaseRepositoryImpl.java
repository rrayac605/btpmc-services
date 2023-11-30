package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.ClaseDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.ClaseRepository;

@Repository
public class ClaseRepositoryImpl implements ClaseRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<ClaseDTO> findOneByCve(String cveLaudo) {
		Query query = new Query(Criteria.where("cveIdLaudo").is(Integer.valueOf(cveLaudo)));
		ClaseDTO d = this.mongoOperations.findOne(query, ClaseDTO.class);

		Optional<ClaseDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<ClaseDTO>> findAll() {
		List<ClaseDTO> d = this.mongoOperations.findAll(ClaseDTO.class);

		Optional<List<ClaseDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
