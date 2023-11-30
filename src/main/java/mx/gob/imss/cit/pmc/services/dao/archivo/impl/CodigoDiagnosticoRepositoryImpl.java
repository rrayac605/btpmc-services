package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.CodigoDiagnosticoDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.CodigoDiagnosticoRepository;

@Repository
public class CodigoDiagnosticoRepositoryImpl implements CodigoDiagnosticoRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<CodigoDiagnosticoDTO> findOneByCve(String cveIdCodigoDiagnostico) {
		Query query = new Query(Criteria.where("cveCieGenerico").is(Integer.valueOf(cveIdCodigoDiagnostico)));
		CodigoDiagnosticoDTO d = this.mongoOperations.findOne(query, CodigoDiagnosticoDTO.class);

		Optional<CodigoDiagnosticoDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<CodigoDiagnosticoDTO>> findAll() {
		List<CodigoDiagnosticoDTO> d = this.mongoOperations.findAll(CodigoDiagnosticoDTO.class);

		Optional<List<CodigoDiagnosticoDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
