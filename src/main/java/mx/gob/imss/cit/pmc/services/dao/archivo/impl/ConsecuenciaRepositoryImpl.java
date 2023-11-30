package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.ConsecuenciaDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.ConsecuenciaRepository;

@Repository
public class ConsecuenciaRepositoryImpl implements ConsecuenciaRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<ConsecuenciaDTO> findOneByCve(String cveConsecuencia) {
		Query query = new Query(Criteria.where("cveIdConsecuencia").is(Integer.valueOf(cveConsecuencia)));
		ConsecuenciaDTO d = this.mongoOperations.findOne(query, ConsecuenciaDTO.class);

		Optional<ConsecuenciaDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<ConsecuenciaDTO>> findAll() {
		List<ConsecuenciaDTO> d = this.mongoOperations.findAll(ConsecuenciaDTO.class);

		Optional<List<ConsecuenciaDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
