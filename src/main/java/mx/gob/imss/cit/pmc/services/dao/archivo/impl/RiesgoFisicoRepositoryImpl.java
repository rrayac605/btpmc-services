package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.RiesgoFisicoDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.RiesgoFisicoRepository;

@Repository
public class RiesgoFisicoRepositoryImpl implements RiesgoFisicoRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<RiesgoFisicoDTO> findOneByCve(String cveIdRiesgoFisico) {
		Query query = new Query(Criteria.where("cveIdRiesgoFisico").is(Integer.valueOf(cveIdRiesgoFisico)));
		RiesgoFisicoDTO d = this.mongoOperations.findOne(query, RiesgoFisicoDTO.class);

		Optional<RiesgoFisicoDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<RiesgoFisicoDTO>> findAll() {
		List<RiesgoFisicoDTO> d = this.mongoOperations.findAll(RiesgoFisicoDTO.class);

		Optional<List<RiesgoFisicoDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
