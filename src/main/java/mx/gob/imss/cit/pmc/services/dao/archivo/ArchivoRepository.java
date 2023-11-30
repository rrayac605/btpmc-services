package mx.gob.imss.cit.pmc.services.dao.archivo;

import java.util.Optional;

import mx.gob.imss.cit.pmc.commons.dto.ArchivoDTO;

public interface ArchivoRepository {

	Optional<ArchivoDTO> findOneById(String userId);

	Optional<ArchivoDTO> findOneByName(String nombre);

	boolean existeArchivo(String nombre);

	ArchivoDTO saveUser(ArchivoDTO archivoDTO);

	void insertaDetalle(ArchivoDTO archivoDTO);

	void actualizaCifras(ArchivoDTO archivoDTO);

}
