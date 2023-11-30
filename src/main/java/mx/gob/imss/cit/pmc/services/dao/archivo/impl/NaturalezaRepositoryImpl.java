package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.NaturalezaDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.NaturalezaRepository;

@Repository
public class NaturalezaRepositoryImpl implements NaturalezaRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<NaturalezaDTO> findOneByCve(String cveTipoRiesgo) {
		Query query = new Query(Criteria.where("cveIdTipoRegistro").is(Integer.valueOf(cveTipoRiesgo)));
		NaturalezaDTO d = this.mongoOperations.findOne(query, NaturalezaDTO.class);

		Optional<NaturalezaDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<NaturalezaDTO>> findAll() {
		List<NaturalezaDTO> d = this.mongoOperations.findAll(NaturalezaDTO.class);

		Optional<List<NaturalezaDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
