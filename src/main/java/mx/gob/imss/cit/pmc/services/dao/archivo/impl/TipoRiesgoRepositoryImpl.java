package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.TipoRiesgoDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.TipoRiesgoRepository;

@Repository
public class TipoRiesgoRepositoryImpl implements TipoRiesgoRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<TipoRiesgoDTO> findOneByCve(String cveTipoRiesgo) {
		Query query = new Query(Criteria.where("cveIdTipoRegistro").is(Integer.valueOf(cveTipoRiesgo)));
		TipoRiesgoDTO d = this.mongoOperations.findOne(query, TipoRiesgoDTO.class);

		Optional<TipoRiesgoDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<TipoRiesgoDTO>> findAll() {
		List<TipoRiesgoDTO> d = this.mongoOperations.findAll(TipoRiesgoDTO.class);

		Optional<List<TipoRiesgoDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
