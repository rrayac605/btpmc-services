package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.commons.dto.CifrasControlDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.CifrasControlRepository;

@Component
public class CifrasControlRepositoryImpl implements CifrasControlRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<CifrasControlDTO> obtenerCifrasControl(String archivoId) {
		CifrasControlDTO d = this.mongoOperations.findOne(new Query(Criteria.where("objectIdArchivo").is(archivoId)),
				CifrasControlDTO.class);

		Optional<CifrasControlDTO> user = Optional.ofNullable(d);
		return user;
	}

}
