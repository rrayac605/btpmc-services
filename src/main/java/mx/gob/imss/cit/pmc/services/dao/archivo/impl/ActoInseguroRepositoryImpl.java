package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.ActoInseguroDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.ActoInseguroRepository;

@Repository
public class ActoInseguroRepositoryImpl implements ActoInseguroRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Optional<ActoInseguroDTO> findOneByCve(String cveIdActoInseguro) {
		Query query = new Query(Criteria.where("cveIdActoInseguro").is(Integer.valueOf(cveIdActoInseguro)));
		ActoInseguroDTO d = this.mongoOperations.findOne(query, ActoInseguroDTO.class);

		Optional<ActoInseguroDTO> parametro = Optional.ofNullable(d);

		return parametro;
	}

	@Override
	public Optional<List<ActoInseguroDTO>> findAll() {
		List<ActoInseguroDTO> d = this.mongoOperations.findAll(ActoInseguroDTO.class);

		Optional<List<ActoInseguroDTO>> parametro = Optional.ofNullable(d);

		return parametro;
	}

}
