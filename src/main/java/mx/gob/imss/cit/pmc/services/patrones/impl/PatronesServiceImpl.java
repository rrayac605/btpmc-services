package mx.gob.imss.cit.pmc.services.patrones.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import mx.gob.imss.cit.pmc.commons.dto.PatronDTO;
import mx.gob.imss.cit.pmc.commons.dto.RegistroDTO;
import mx.gob.imss.cit.pmc.commons.exception.PatronNoEncontradoException;
import mx.gob.imss.cit.pmc.commons.utils.DigitoVerificadorUtils;
import mx.gob.imss.cit.pmc.services.catalogos.CatalogosBDTUService;
import mx.gob.imss.cit.pmc.services.catalogos.CatalogosLocales;
import mx.gob.imss.cit.pmc.services.dao.archivo.PatronRepository;
import mx.gob.imss.cit.pmc.services.dto.PatronJsonDTO;
import mx.gob.imss.cit.pmc.services.patrones.PatronesService;

@Component
public class PatronesServiceImpl implements PatronesService {

	private final static Logger logger = LoggerFactory.getLogger(PatronesServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CatalogosLocales catalogosLocales;

	@Autowired
	private CatalogosBDTUService catalogosBDTUService;

	@Autowired
	private PatronRepository patronRepository;

	@Autowired
	@Qualifier("urlPatron")
	private String urlPatron;

	@Override
	public PatronDTO obtenerPatron(String regPatronal) throws PatronNoEncontradoException {
		ResponseEntity<PatronJsonDTO> response = null;
		PatronDTO patronDTO = new PatronDTO();
		@SuppressWarnings("resource")
		Formatter fmt = new Formatter();
		Instant start = Instant.now();
		try {
			if ((regPatronal != null && !regPatronal.trim().equals("")) && regPatronal.length() == 10) {
				regPatronal = regPatronal.concat(DigitoVerificadorUtils.generaDigitoVerificadorRP(regPatronal));
			}
			response = restTemplate.getForEntity(urlPatron.concat(regPatronal), PatronJsonDTO.class);
		} catch (RestClientException e) {
			logger.error("No existe información: {}", regPatronal, e.getMessage());
		}
		if (response != null) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append(
						response.getBody().getClasificacion().getFraccion().getGrupo().getDivision().getNumDivision());
				sb.append(response.getBody().getClasificacion().getFraccion().getGrupo().getNumGrupo());
				if (response.getBody().getClasificacion().getFraccion().getNumFraccion() < 10) {
					sb.append(0);
				}
				sb.append(response.getBody().getClasificacion().getFraccion().getNumFraccion());
				patronDTO.setCveClase(response.getBody().getClasificacion().getFraccion().getClase().getClave());
				patronDTO.setDesClase(response.getBody().getClasificacion().getFraccion().getClase().getDescripcion());
				patronDTO.setCveFraccion((response.getBody().getClasificacion().getFraccion().getId()));
				patronDTO.setDesFraccion((response.getBody().getClasificacion().getFraccion().getDescripcion()));
				patronDTO.setCveDelRegPatronal(response.getBody().getSubdelegacion().getDelegacion().getClave());
				patronDTO.setDesDelRegPatronal(response.getBody().getSubdelegacion().getDelegacion().getDescripcion());
				patronDTO.setCveSubDelRegPatronal(response.getBody().getSubdelegacion().getClave());
				patronDTO.setDesSubDelRegPatronal(response.getBody().getSubdelegacion().getDescripcion());
				patronDTO.setNumPrima(response.getBody().getClasificacion().getPrimaSRTActual());
				if (response.getBody().getMoral() != null) {
					patronDTO.setDesRazonSocial(response.getBody().getMoral().getRazonSocial());
					patronDTO.setDesRfc(response.getBody().getMoral().getRfc());
				} else if (response.getBody().getFisica() != null) {
					patronDTO.setDesRazonSocial(response.getBody().getFisica().getNombre());
					patronDTO.setDesRfc(response.getBody().getFisica().getRfc());
				}
				patronDTO.setRefRegistroPatronal(regPatronal);
				patronDTO.setCveDelegacionAux(String
						.valueOf(fmt.format("%02d", response.getBody().getSubdelegacion().getDelegacion().getId())));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				patronDTO.setCveDelegacionAux(String.valueOf(fmt.format("%02d", 0)));
			}
		} else {
			patronDTO.setCveDelegacionAux(String.valueOf(fmt.format("%02d", 0)));
		}
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		logger.debug("Tiempo para encontrar patron: {} milliseconds", timeElapsed.toMillis());
		return patronDTO;
	}

	@Override
	public PatronDTO obtenerPatronOracle(String regPatronal) throws PatronNoEncontradoException {
		PatronDTO patronDTO = new PatronDTO();
		@SuppressWarnings("resource")
		Formatter fmt = new Formatter();
		Instant start = Instant.now();
		try {
			if ((regPatronal != null && !regPatronal.trim().equals("")) && regPatronal.length() == 10) {
				regPatronal = regPatronal.concat(DigitoVerificadorUtils.generaDigitoVerificadorRP(regPatronal));
			}
			patronDTO = patronRepository.obtenerPatron(regPatronal.substring(0, 10));
			patronDTO.setRefRegistroPatronal(regPatronal);
			patronDTO.setCveDelegacionAux(String.valueOf(fmt.format("%02d", patronDTO.getCveDelRegPatronal())));
		} catch (Exception e) {
			logger.error("No existe información: {}", regPatronal, e.getMessage());
			patronDTO.setCveDelegacionAux(String.valueOf(fmt.format("%02d", 0)));
		}
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		logger.debug("Tiempo para encontrar patron: {} milliseconds", timeElapsed.toMillis());
		return patronDTO;
	}

	@Override
	public PatronDTO obtenerPatronInicial(RegistroDTO item) throws PatronNoEncontradoException {
		PatronDTO patronDTO = new PatronDTO();
		@SuppressWarnings("resource")
		Formatter fmt = new Formatter();
		try {
			patronDTO.setCveClase(item.getCveClase());
			patronDTO.setDesClase(item.getCveClase() > 0
					? catalogosLocales.obtenerClase(String.valueOf(item.getCveClase())).getDesCatalogo()
					: null);
			patronDTO.setCveFraccion(item.getCveFraccion());
			patronDTO.setDesFraccion(item.getDesFraccion());
			patronDTO.setCveDelRegPatronal(item.getCveDelRegPatronal());
			patronDTO.setDesDelRegPatronal(catalogosBDTUService
					.obtenerDelegacion(String.valueOf(item.getCveDelRegPatronal())).getDescripcion());
			patronDTO.setCveSubDelRegPatronal(item.getCveSubDelRegPatronal());
			patronDTO.setDesSubDelRegPatronal(
					catalogosBDTUService.obtenerSubDelegacion(String.valueOf(item.getCveDelRegPatronal()),
							String.valueOf(item.getCveSubDelRegPatronal())).getDescripcion());
			patronDTO.setNumPrima(item.getNumPrima());
			patronDTO.setDesRazonSocial(item.getDesRazonSocial());
			patronDTO.setDesRfc(item.getDesRfc());
			patronDTO.setRefRegistroPatronal(item.getRefRegistroPatronal());
			patronDTO.setCveDelegacionAux(String.valueOf(
					catalogosBDTUService.obtenerDelegacion(String.valueOf(item.getCveDelRegPatronal())).getId()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			patronDTO.setCveDelegacionAux(String.valueOf(fmt.format("%02d", 0)));
		}
		return patronDTO;
	}

}
