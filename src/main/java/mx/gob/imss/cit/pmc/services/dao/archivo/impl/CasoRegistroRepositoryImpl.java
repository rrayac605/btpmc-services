package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.CasoRegistroDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.CasoRegistroRepository;

@Repository
public class CasoRegistroRepositoryImpl implements CasoRegistroRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<CasoRegistroDTO> findOneByCve(String cveLaudo) {
		Query query = new Query(Criteria.where("cveIdCasoRegistro").is(Integer.valueOf(cveLaudo)));
		CasoRegistroDTO d = this.mongoOperations.findOne(query, CasoRegistroDTO.class);

		Optional<CasoRegistroDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<CasoRegistroDTO>> findAll() {
		List<CasoRegistroDTO> d = this.mongoOperations.findAll(CasoRegistroDTO.class);

		Optional<List<CasoRegistroDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
