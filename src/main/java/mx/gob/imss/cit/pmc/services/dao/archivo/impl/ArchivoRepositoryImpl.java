package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.ArchivoDTO;
import mx.gob.imss.cit.pmc.commons.dto.DetalleRegistroDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.ArchivoRepository;

@Repository
public class ArchivoRepositoryImpl implements ArchivoRepository {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public ArchivoDTO saveUser(ArchivoDTO archivoDTO) {
		mongoOperations.save(archivoDTO);
		return findOneById(archivoDTO.getObjectIdArchivo()).get();
	}

	public void actualizaCifras(ArchivoDTO archivoDTO) {
		mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(archivoDTO.getObjectIdArchivo())),
				Update.update("cifrasControlDTO", archivoDTO.getCifrasControlDTO()), ArchivoDTO.class);
	}

	@Override
	public void insertaDetalle(ArchivoDTO archivoDTO) {
		for (DetalleRegistroDTO detalleRegistroDTO : archivoDTO.getDetalleRegistroDTO()) {
			mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(archivoDTO.getObjectIdArchivo())),
					new Update().push("detalleRegistroDTO", detalleRegistroDTO), ArchivoDTO.class);
		}
	}

	@Override
	public Optional<ArchivoDTO> findOneById(String archivoId) {

		ArchivoDTO d = this.mongoOperations.findOne(new Query(Criteria.where("objectIdArchivo").is(archivoId)),
				ArchivoDTO.class);

		Optional<ArchivoDTO> user = Optional.ofNullable(d);

		return user;

	}

	@Override
	public Optional<ArchivoDTO> findOneByName(String nombre) {

		ArchivoDTO d = this.mongoOperations.findOne(new Query(
				Criteria.where("nomArchivo").is(nombre).andOperator(Criteria.where("cveEstadoArchivo").is("2"))),
				ArchivoDTO.class);

		Optional<ArchivoDTO> user = Optional.ofNullable(d);

		return user;

	}

	@Override
	public boolean existeArchivo(String nombre) {

		return this.mongoOperations.exists(new Query(
				Criteria.where("nomArchivo").is(nombre).andOperator(Criteria.where("cveEstadoArchivo").is("2"))),
				ArchivoDTO.class);

	}

}
