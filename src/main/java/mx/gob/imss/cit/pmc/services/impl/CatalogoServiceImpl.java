package mx.gob.imss.cit.pmc.services.impl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.commons.dto.ConsecuenciaDTO;
import mx.gob.imss.cit.pmc.commons.dto.FechaInhabilDTO;
import mx.gob.imss.cit.pmc.commons.dto.LaudoDTO;
import mx.gob.imss.cit.pmc.commons.dto.TipoIncapacidadDTO;
import mx.gob.imss.cit.pmc.commons.dto.TipoRiesgoDTO;
import mx.gob.imss.cit.pmc.commons.utils.DateUtils;
import mx.gob.imss.cit.pmc.commons.utils.Utils;
import mx.gob.imss.cit.pmc.services.CatalogosService;
import mx.gob.imss.cit.pmc.services.dao.archivo.ConsecuenciaRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.DiasInhabilesRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.LaudoRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.TipoIncapacidadRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.TipoRiesgoRepository;

@Component
public class CatalogoServiceImpl implements CatalogosService {

	private final static Logger logger = LoggerFactory.getLogger(CatalogoServiceImpl.class);

	@Autowired
	private LaudoRepository laudoRepository;

	@Autowired
	private ConsecuenciaRepository consecuenciaRepository;

	@Autowired
	private TipoRiesgoRepository tipoRiesgoRepository;

	@Autowired
	private DiasInhabilesRepository diasInhabilesRepository;

	@Autowired
	private TipoIncapacidadRepository tipoIncapacidadRepository;

	private Hashtable<Integer, TipoRiesgoDTO> tiposRiesgo;

	private Hashtable<Integer, ConsecuenciaDTO> consecuencias;

	private Hashtable<Integer, LaudoDTO> laudos;

	private Hashtable<Integer, TipoIncapacidadDTO> tiposIncapacidad;

	@Override
	public TipoRiesgoDTO obtenerTipoRiesgo(String cveTipoRiesgo) {
		if (tiposRiesgo == null) {
			try {
				List<TipoRiesgoDTO> tipos = tipoRiesgoRepository.findAll().get();
				tiposRiesgo = new Hashtable<Integer, TipoRiesgoDTO>();
				for (TipoRiesgoDTO tipoRiesgo : tipos) {
					tiposRiesgo.put(tipoRiesgo.getCveIdTipoRegistro(), tipoRiesgo);
				}
			} catch (Exception e) {
				logger.error("No existe información para el tipo de riesgo: {[]}", cveTipoRiesgo, e);
			}
		}
		return tiposRiesgo.get(Utils.validaEntero(cveTipoRiesgo));
	}

	@Override
	public ConsecuenciaDTO obtenerConsecuencia(String cveConsecuencia) {
		ConsecuenciaDTO consecuenciaDTO = null;
		if (consecuencias == null) {
			try {
				List<ConsecuenciaDTO> tipos = consecuenciaRepository.findAll().get();
				consecuencias = new Hashtable<Integer, ConsecuenciaDTO>();
				for (ConsecuenciaDTO tipoRiesgo : tipos) {
					consecuencias.put(tipoRiesgo.getCveIdConsecuencia(), tipoRiesgo);
				}
			} catch (Exception e) {
				logger.error("No existe información para el tipo de riesgo: {[]}", cveConsecuencia, e);
			}
		}
		if (cveConsecuencia != null && !cveConsecuencia.trim().equals("")) {
			consecuenciaDTO = consecuencias.get(Utils.validaEntero(cveConsecuencia));
		}
		return consecuenciaDTO;
	}

	@Override
	public LaudoDTO obtenerLaudo(String cveLaudo) {
		if (laudos == null) {
			try {
				List<LaudoDTO> tipos = laudoRepository.findAll().get();
				laudos = new Hashtable<Integer, LaudoDTO>();
				for (LaudoDTO tipoRiesgo : tipos) {
					laudos.put(tipoRiesgo.getCveIdLaudo(), tipoRiesgo);
				}
			} catch (Exception e) {
				logger.error("No existe información para el tipo de riesgo: {[]}", cveLaudo, e);
			}
		}
		return laudos.get(Utils.validaEntero(cveLaudo));
	}

	@Override
	public Date obtenerFechasInhabiles(int numeroDias) {
		Date fechaHabil = null;
		LocalDate localDate = LocalDate.now();
		long millisLocalDate = localDate.atStartOfDay().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
		Calendar fechaProceso = Calendar.getInstance();
		fechaProceso.setTimeInMillis(millisLocalDate);
		try {
			List<FechaInhabilDTO> fechasInhabilesDTO = diasInhabilesRepository.findAll();
			fechaHabil = DateUtils.fechaHabil(fechaProceso.getTime(), fechasInhabilesDTO, numeroDias);

		} catch (Exception e) {
			logger.error("No existe información para el laudo: obtenerFechasInhabiles", e);
		}
		return fechaHabil;
	}

	@Override
	public TipoIncapacidadDTO obtenerTipoIncapacidad(String cveTipoIncapacidad) {
		if (tiposIncapacidad == null) {
			try {
				List<TipoIncapacidadDTO> tipos = tipoIncapacidadRepository.findAll().get();
				tiposIncapacidad = new Hashtable<Integer, TipoIncapacidadDTO>();
				for (TipoIncapacidadDTO tipoRiesgo : tipos) {
					tiposIncapacidad.put(tipoRiesgo.getCveTipoIncapacidad(), tipoRiesgo);
				}
			} catch (Exception e) {
				logger.error("No existe información para el tipo de riesgo: {[]}", cveTipoIncapacidad, e);
			}
		}
		return tiposIncapacidad.get(Utils.validaEntero(cveTipoIncapacidad));
	}

}
