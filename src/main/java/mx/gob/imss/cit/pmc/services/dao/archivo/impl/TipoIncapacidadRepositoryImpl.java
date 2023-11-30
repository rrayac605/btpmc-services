package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.TipoIncapacidadDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.TipoIncapacidadRepository;

@Repository
public class TipoIncapacidadRepositoryImpl implements TipoIncapacidadRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<TipoIncapacidadDTO> findOneByCve(String cveTipoIncapacidad) {
		Query query = new Query(Criteria.where("cveTipoIncapacidad").is(Integer.valueOf(cveTipoIncapacidad)));
		TipoIncapacidadDTO d = this.mongoOperations.findOne(query, TipoIncapacidadDTO.class);

		Optional<TipoIncapacidadDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<TipoIncapacidadDTO>> findAll() {
		List<TipoIncapacidadDTO> d = this.mongoOperations.findAll(TipoIncapacidadDTO.class);

		Optional<List<TipoIncapacidadDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
