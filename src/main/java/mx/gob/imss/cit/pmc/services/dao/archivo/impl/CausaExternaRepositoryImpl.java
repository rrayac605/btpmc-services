package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.CausaExternaDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.CausaExternaRepository;

@Repository
public class CausaExternaRepositoryImpl implements CausaExternaRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<CausaExternaDTO> findOneByCve(String cveIdCausaExterna) {
		Query query = new Query(Criteria.where("cveIdCausaExterna").is(Integer.valueOf(cveIdCausaExterna)));
		CausaExternaDTO d = this.mongoOperations.findOne(query, CausaExternaDTO.class);

		Optional<CausaExternaDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<CausaExternaDTO>> findAll() {
		List<CausaExternaDTO> d = this.mongoOperations.findAll(CausaExternaDTO.class);

		Optional<List<CausaExternaDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
