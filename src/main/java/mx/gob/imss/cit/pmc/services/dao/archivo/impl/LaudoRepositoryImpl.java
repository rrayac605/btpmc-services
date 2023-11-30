package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.LaudoDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.LaudoRepository;

@Repository
public class LaudoRepositoryImpl implements LaudoRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<LaudoDTO> findOneByCve(String cveLaudo) {
		Query query = new Query(Criteria.where("cveIdLaudo").is(Integer.valueOf(cveLaudo)));
		LaudoDTO d = this.mongoOperations.findOne(query, LaudoDTO.class);

		Optional<LaudoDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<LaudoDTO>> findAll() {
		List<LaudoDTO> d = this.mongoOperations.findAll(LaudoDTO.class);

		Optional<List<LaudoDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
