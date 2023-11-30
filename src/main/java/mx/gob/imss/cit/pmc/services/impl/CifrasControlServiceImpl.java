package mx.gob.imss.cit.pmc.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.commons.dto.CifrasControlDTO;
import mx.gob.imss.cit.pmc.services.CifrasControlService;
import mx.gob.imss.cit.pmc.services.dao.archivo.CifrasControlRepository;

@Component
public class CifrasControlServiceImpl implements CifrasControlService {

	@Autowired
	private CifrasControlRepository cifrasControlRepository;

	@Override
	public CifrasControlDTO obtenerCifrasControl(String archivoId) {
		return cifrasControlRepository.obtenerCifrasControl(archivoId).get();
	}

}
