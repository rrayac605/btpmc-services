package mx.gob.imss.cit.pmc.services.asegurado.impl;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import mx.gob.imss.cit.pmc.commons.dto.AseguradoDTO;
import mx.gob.imss.cit.pmc.commons.exception.AseguradonNoEncontradoException;
import mx.gob.imss.cit.pmc.commons.utils.DigitoVerificadorUtils;
import mx.gob.imss.cit.pmc.services.asegurado.AseguradoService;
import mx.gob.imss.cit.pmc.services.dao.archivo.AseguradoRepository;
import mx.gob.imss.cit.pmc.services.dto.AseguradoJsonDTO;

@Component
public class AseguradoServiceImpl implements AseguradoService {

	private final static Logger logger = LoggerFactory.getLogger(AseguradoServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AseguradoRepository aseguradoRepository;

	@Autowired
	@Qualifier("urlAsegurado")
	private String urlAsegurado;

	@Override
	public AseguradoDTO existeAsegurado(String nssAsegurado) throws AseguradonNoEncontradoException {
		ResponseEntity<AseguradoJsonDTO> response = null;
		AseguradoDTO aseguradoDTO = new AseguradoDTO();
		Instant start = Instant.now();
		try {
			if ((nssAsegurado != null && !nssAsegurado.trim().equals("")) && nssAsegurado.length() == 10) {
				nssAsegurado = nssAsegurado.concat(DigitoVerificadorUtils.generaDigitoVerificador(nssAsegurado));
			}
			response = restTemplate.getForEntity(urlAsegurado.concat(nssAsegurado), AseguradoJsonDTO.class);
			if (response != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(response.getBody().getPrimerApellido());
				sb.append("$");
				sb.append(response.getBody().getSegundoApellido());
				sb.append("$");
				sb.append(response.getBody().getNombre());
				aseguradoDTO.setCveIdPersona(response.getBody().getCveIdPersona());
				aseguradoDTO.setRefCurp(response.getBody().getCurp());
				aseguradoDTO.setNomAsegurado(sb.toString());
				aseguradoDTO.setCveSubdelNss(response.getBody().getSubdelegacion().getClave());
				aseguradoDTO.setDesSubDelNss(response.getBody().getSubdelegacion().getDescripcion());
				aseguradoDTO.setCveDelegacionNss(response.getBody().getSubdelegacion().getDelegacion().getClave());
				aseguradoDTO
						.setDesDelegacionNss(response.getBody().getSubdelegacion().getDelegacion().getDescripcion());
				aseguradoDTO.setCveUmfAdscripcion(response.getBody().getUmf().getNumUMF());
				aseguradoDTO.setDesUmfAdscripcion(response.getBody().getUmf().getDescUMF());
			}
		} catch (RestClientException e) {
			logger.error("No existe información: {}", nssAsegurado);
		}
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		logger.debug("Tiempo para encontrar asegurado: {} milliseconds", timeElapsed.toMillis());
		aseguradoDTO.setNumNss(nssAsegurado);
		return aseguradoDTO;
	}

	@Override
	public AseguradoDTO existeAseguradoOracle(String nssAsegurado) throws AseguradonNoEncontradoException {
		AseguradoDTO aseguradoDTO = new AseguradoDTO();
		Instant start = Instant.now();
		try {
			if ((nssAsegurado != null && !nssAsegurado.trim().equals("")) && nssAsegurado.length() == 10) {
				nssAsegurado = nssAsegurado.concat(DigitoVerificadorUtils.generaDigitoVerificador(nssAsegurado));
			}
			aseguradoDTO = aseguradoRepository.existeAsegurado(nssAsegurado);
		} catch (Exception e) {
			logger.error("No existe información: {}", nssAsegurado);
		}
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		logger.debug("Tiempo para encontrar asegurado: {} milliseconds", timeElapsed.toMillis());
		aseguradoDTO.setNumNss(nssAsegurado);
		return aseguradoDTO;
	}
	
	@Override
	public boolean existeMarcaAfiliatoria(String nssAsegurado) throws AseguradonNoEncontradoException {
		boolean aseguradoDTO = false;
		Instant start = Instant.now();
		try {
			if ((nssAsegurado != null && !nssAsegurado.trim().equals("")) && nssAsegurado.length() == 10) {
				nssAsegurado = nssAsegurado.concat(DigitoVerificadorUtils.generaDigitoVerificador(nssAsegurado));
			}
			aseguradoDTO = aseguradoRepository.existeMarcaAfiliatoria(nssAsegurado);
		} catch (Exception e) {
			logger.error("No existe información: {}", nssAsegurado);
		}
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		logger.debug("Tiempo para encontrar asegurado: {} milliseconds", timeElapsed.toMillis());
		return aseguradoDTO;
	}

}
