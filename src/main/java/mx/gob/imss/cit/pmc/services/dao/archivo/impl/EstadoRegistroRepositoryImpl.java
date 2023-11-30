package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.EstadoRegistroDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.EstadoRegistroRepository;

@Repository
public class EstadoRegistroRepositoryImpl implements EstadoRegistroRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<EstadoRegistroDTO> findOneByCve(String cveLaudo) {
		Query query = new Query(Criteria.where("cveIdCasoRegistro").is(Integer.valueOf(cveLaudo)));
		EstadoRegistroDTO d = this.mongoOperations.findOne(query, EstadoRegistroDTO.class);

		Optional<EstadoRegistroDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<EstadoRegistroDTO>> findAll() {
		List<EstadoRegistroDTO> d = this.mongoOperations.findAll(EstadoRegistroDTO.class);

		Optional<List<EstadoRegistroDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
