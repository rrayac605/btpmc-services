package mx.gob.imss.cit.pmc.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.commons.dto.ParametroDTO;
import mx.gob.imss.cit.pmc.commons.exception.ParametroNoExisteException;
import mx.gob.imss.cit.pmc.services.ParametrosService;
import mx.gob.imss.cit.pmc.services.dao.archivo.ParametroRepository;

@Component
public class ParametrosServiceImpl implements ParametrosService {

	@Autowired
	private ParametroRepository parametroRepository;

	@Override
	public ParametroDTO obtenerParametro(String idParametro) throws ParametroNoExisteException {

		Optional<ParametroDTO> optional = null;
		try {
			optional = parametroRepository.findOneByCve(idParametro);
		} catch (Exception e) {
			throw new ParametroNoExisteException(e);
		}

		return optional.get();
	}

}
