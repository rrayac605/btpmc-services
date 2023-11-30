package mx.gob.imss.cit.pmc.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.commons.dto.ArchivoDTO;
import mx.gob.imss.cit.pmc.commons.dto.AuditoriaDTO;
import mx.gob.imss.cit.pmc.commons.dto.CifrasControlDTO;
import mx.gob.imss.cit.pmc.commons.dto.DetalleRegistroDTO;
import mx.gob.imss.cit.pmc.commons.dto.MovementsDupDTO;
import mx.gob.imss.cit.pmc.commons.dto.MovementsSusDTO;
import mx.gob.imss.cit.pmc.commons.dto.RegistroDTO;
import mx.gob.imss.cit.pmc.commons.enums.AccionRegistroEnum;
import mx.gob.imss.cit.pmc.commons.enums.CicloEnum;
import mx.gob.imss.cit.pmc.commons.enums.EstadoRegistroEnum;
import mx.gob.imss.cit.pmc.commons.enums.SituacionRegistroEnum;
import mx.gob.imss.cit.pmc.commons.utils.DateUtils;
import mx.gob.imss.cit.pmc.commons.utils.Utils;
import mx.gob.imss.cit.pmc.services.DetalleRegistroService;
import mx.gob.imss.cit.pmc.services.dao.archivo.ArchivoRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.DetalleRegistroRepository;

@Component
public class DetalleRegistroServiceImpl implements DetalleRegistroService {

	private final static Logger logger = LoggerFactory.getLogger(DetalleRegistroServiceImpl.class);

	@Autowired
	private DetalleRegistroRepository detalleRegistroRepository;

	@Autowired
	private ArchivoRepository archivoRepository;
	
	public RegistroDTO findOne(String objectId) {
		Optional<DetalleRegistroDTO> registro = detalleRegistroRepository.findOneById(objectId);
		RegistroDTO registroDTO = null;
		if (registro.isPresent()) {
			DetalleRegistroDTO detalleRegistroDTO = registro.get();
			registroDTO = Utils.fillChange(detalleRegistroDTO.getAseguradoDTO(), detalleRegistroDTO.getPatronDTO(),
					detalleRegistroDTO.getIncapacidadDTO());
		}
		return registroDTO;
	}

	@Override
	public List<String> existeRegistro(String nombreArchivo) {
		List<String> archivos = new ArrayList<String>();
		archivos.add(nombreArchivo);
		List<MovementsDupDTO> registrosDup = detalleRegistroRepository.validateDuplicated(nombreArchivo);
		for (MovementsDupDTO registroDup : registrosDup) {
			int i = 0;
			for (DetalleRegistroDTO detalle : registroDup.getMovementList()) {
				if (i != 0) {
					Optional<ArchivoDTO> optional = archivoRepository
							.findOneById(detalle.getIdentificadorArchivo().toString());
					if (optional.isPresent()) {
						ArchivoDTO archivoDTO = optional.get();
						if (!archivos.contains(archivoDTO.getNomArchivo())) {
							archivos.add(archivoDTO.getNomArchivo());
						}
					}
					if (Arrays.asList(1, 2, 4).contains(detalle.getAseguradoDTO().getCveEstadoRegistro())) {
						detalle.getAseguradoDTO()
								.setCveEstadoRegistro(EstadoRegistroEnum.DUPLICADO.getCveEstadoRegistro());
						detalle.getAseguradoDTO()
								.setDesEstadoRegistro(EstadoRegistroEnum.DUPLICADO.getDesDescripcion());
					} else if (Arrays.asList(5, 6, 8).contains(detalle.getAseguradoDTO().getCveEstadoRegistro())) {
						detalle.getAseguradoDTO()
								.setCveEstadoRegistro(EstadoRegistroEnum.DUPLICADO_OTRAS.getCveEstadoRegistro());
						detalle.getAseguradoDTO()
								.setDesEstadoRegistro(EstadoRegistroEnum.DUPLICADO_OTRAS.getDesDescripcion());
					}
					detalleRegistroRepository.actualizaDetalleDuplicado(detalle);
				}
				i++;
			}
		}
		logger.debug("Duplicados: {}", archivos.toString());
		return archivos;
	}

	@Override
	public List<RegistroDTO> existeSusceptible(RegistroDTO registroDTO, String tipoArchivo) {
		List<RegistroDTO> registrosCoincidentes = new ArrayList<RegistroDTO>();
		List<DetalleRegistroDTO> registroSus = detalleRegistroRepository.existeSusceptible(registroDTO, tipoArchivo);
		for (DetalleRegistroDTO detalleRegistroDTO : registroSus) {
			RegistroDTO registro = new RegistroDTO();
			registro.setRefFolioOriginal(detalleRegistroDTO.getAseguradoDTO().getRefFolioOriginal());
			registro.setFecAtencion(
					DateUtils.parserDatetoString(detalleRegistroDTO.getIncapacidadDTO().getFecAtencion()));
			registro.setCveConsecuencia(String.valueOf(detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia()));
			registro.setFecAtencion(
					DateUtils.parserDatetoString(detalleRegistroDTO.getIncapacidadDTO().getFecAtencion()));
			registro.setFecInicio(DateUtils.parserDatetoString(detalleRegistroDTO.getIncapacidadDTO().getFecInicio()));
			registro.setFecFin(DateUtils.parserDatetoString(detalleRegistroDTO.getIncapacidadDTO().getFecFin()));
			registrosCoincidentes.add(registro);
		}
		return registrosCoincidentes;
	}

	@Override
	public void existeSusceptibleNss(String nombreArchivo, List<String> archivos) {
		Integer[] ciclo = obetenerCliclo();
		List<MovementsSusDTO> registroSus = detalleRegistroRepository.validateDuplicatedNss(nombreArchivo);
		logger.info("Candidatos a susceptibles {}", registroSus.size());
		for (MovementsSusDTO agrupacion : registroSus) {
			boolean porcentajeMayorCero = false;
			List<DetalleRegistroDTO> registrosCoincidentes = null;
			if (agrupacion.getMovementList() != null && !agrupacion.getMovementList().isEmpty()) {
				registrosCoincidentes = new ArrayList<DetalleRegistroDTO>();
				registrosCoincidentes.addAll(agrupacion.getMovementList());
			}
			porcentajeMayorCero = existePorcentajeCicloActual(agrupacion, ciclo);
			logger.info("Candidatos a susceptibles nss {}", agrupacion.getId());
			logger.info("Candidatos a susceptibles nss cantidad {}", agrupacion.getMovementList().size());

			for (DetalleRegistroDTO detalleRegistroDTO : agrupacion.getMovementList()) {
				try {
					int cicloAnual = obtenerCiclo(detalleRegistroDTO);
					logger.info(
							"Datos del registro -> nss: {}, ciclo: {}, cicloActual: {}, porcentaje: {}, consecuencia: {}, subsidiados: {} ",
							detalleRegistroDTO.getAseguradoDTO().getNumNss(),
							detalleRegistroDTO.getAseguradoDTO().getNumCicloAnual(), Arrays.toString(ciclo),
							detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad(),
							detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia(),
							detalleRegistroDTO.getIncapacidadDTO().getNumDiasSubsidiados());
					if (reglaSieteParrafoUno(ciclo, cicloAnual, detalleRegistroDTO)) {
						logger.debug("cumple condicion uno");
						removerRegistro(detalleRegistroDTO, registrosCoincidentes);

					} else if (reglaSieteParrafoDos(ciclo, cicloAnual, detalleRegistroDTO, porcentajeMayorCero)) {
						logger.debug("cumple condicion dos");
						removerRegistro(detalleRegistroDTO, registrosCoincidentes);
					}
				} catch (Exception e) {
					logger.error("Error al valorar susceptible: {}", detalleRegistroDTO.getObjectId(), e.getMessage(),
							e);
				}
			}
			logger.info("Resultado de susceptibles {}", registrosCoincidentes.size());
			if (registrosCoincidentes != null && registrosCoincidentes.size() > 1) {
				actualizaSusceptibles(registrosCoincidentes, archivos);
			}
		}
		logger.debug("Susceptibles: {}", archivos.toString());
	}

	private void removerRegistro(DetalleRegistroDTO detalleRegistroDTO,
			List<DetalleRegistroDTO> registrosCoincidentes) {
		for (DetalleRegistroDTO susceptible : registrosCoincidentes) {
			if (susceptible.getObjectId().equals(detalleRegistroDTO.getObjectId())) {
				logger.debug("removiendo registro {}", susceptible.toString());
				registrosCoincidentes.remove(susceptible);
				break;
			}
		}
	}

	private boolean existePorcentajeCicloActual(MovementsSusDTO agrupacion, Integer[] ciclo) {
		boolean porcentajeMayorCero = false;
		for (DetalleRegistroDTO detalleRegistroDTO : agrupacion.getMovementList()) {
			int cicloAnual = detalleRegistroDTO.getAseguradoDTO().getNumCicloAnual() != null
					&& !detalleRegistroDTO.getAseguradoDTO().getNumCicloAnual().equals("")
							? Integer.valueOf(detalleRegistroDTO.getAseguradoDTO().getNumCicloAnual()).intValue()
							: 0;
			if (Arrays.asList(ciclo).contains(cicloAnual)
					&& ((detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad() != null
							&& detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad().intValue() > 0)
							|| (detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia() != null
									&& detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia() == 4))) {
				porcentajeMayorCero = true;
				break;
			}
		}
		return porcentajeMayorCero;
	}

	private boolean reglaSieteParrafoUno(Integer[] ciclo, int cicloAnual, DetalleRegistroDTO detalleRegistroDTO) {
		return cicloAnual < ciclo[0] && (detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad() == null ||
				((detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad() != null
				&& detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad().intValue() <= 0)
				&& (detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia() != null
						&& detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia().intValue() != 4)));
	}

	private boolean reglaSieteParrafoDos(Integer[] ciclo, int cicloAnual, DetalleRegistroDTO detalleRegistroDTO,
			boolean porcentajeMayorCero) {
		return cicloAnual < ciclo[0] && (detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad() == null ||
				((detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad() != null
						&& detalleRegistroDTO.getIncapacidadDTO().getPorPorcentajeIncapacidad().intValue() > 0)
						|| (detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia() != null
								&& detalleRegistroDTO.getIncapacidadDTO().getCveConsecuencia().intValue() == 4))
				&& !porcentajeMayorCero);
	}

	private Integer[] obetenerCliclo() {
		Integer[] ciclo = new Integer[2];
		Date fechaActual = Calendar.getInstance().getTime();
		Calendar fechaFinal = Calendar.getInstance();
		fechaFinal.set(Calendar.DATE, CicloEnum.DIA_FIN_CICLO.getClave());
		fechaFinal.set(Calendar.MONTH, CicloEnum.MES_FIN_CICLO.getClave());

		Calendar fechaInicial = Calendar.getInstance();
		fechaInicial.set(Calendar.DATE, CicloEnum.DIA_INICIO_CLICO.getClave());
		fechaInicial.set(Calendar.MONTH, CicloEnum.MES_INNICIO_CICLO.getClave());
		fechaInicial.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
		if (fechaInicial.getTime().compareTo(fechaActual) < 0 && fechaFinal.getTime().compareTo(fechaActual) > 0) {
			ciclo[0] = Calendar.getInstance().get(Calendar.YEAR) - 1;
			ciclo[1] = Calendar.getInstance().get(Calendar.YEAR);
		} else {
			ciclo[0] = Calendar.getInstance().get(Calendar.YEAR);
		}
		return ciclo;
	}

	private Integer obtenerCiclo(DetalleRegistroDTO detalleRegistroDTO) {
		return detalleRegistroDTO.getAseguradoDTO().getNumCicloAnual() != null
				&& !detalleRegistroDTO.getAseguradoDTO().getNumCicloAnual().equals("")
						? Integer.valueOf(detalleRegistroDTO.getAseguradoDTO().getNumCicloAnual()).intValue()
						: 0;
	}

	private List<AuditoriaDTO> crearBitacoras(DetalleRegistroDTO registro) {
		List<AuditoriaDTO> auditorias = new ArrayList<AuditoriaDTO>();
		AuditoriaDTO auditoria = new AuditoriaDTO();
		auditoria.setFecAlta(DateUtils.getSysDateMongoISO());
		auditoria.setFecBaja(DateUtils.getSysDateMongoISO());
		auditoria.setNomUsuario("Sistema PMC");
		auditoria.setNumFolioMovOriginal(registro.getAseguradoDTO().getRefFolioOriginal());
		auditoria.setCveIdAccionRegistro(AccionRegistroEnum.MODIFICACION.getClave());
		auditoria.setDesAccionRegistro(AccionRegistroEnum.MODIFICACION.getDescripcion());
		auditoria.setCveSituacionRegistro(SituacionRegistroEnum.PENDIENTE.getClave());
		auditoria.setDesSituacionRegistro(SituacionRegistroEnum.PENDIENTE.getDescripcion());
		auditoria.setCveEstadoRegistro(registro.getAseguradoDTO().getCveEstadoRegistro());
		auditoria.setDesEstadoRegistro(registro.getAseguradoDTO().getDesEstadoRegistro());
		auditoria.setOrdenEjecucion(1);
		AuditoriaDTO auditoria1 = new AuditoriaDTO();
		auditoria1.setFecAlta(DateUtils.getSysDateMongoISO());
		auditoria1.setNomUsuario("Sistema PMC");
		auditoria1.setNumFolioMovOriginal(registro.getAseguradoDTO().getRefFolioOriginal());
		auditoria1.setCveIdAccionRegistro(AccionRegistroEnum.MODIFICACION.getClave());
		auditoria1.setDesAccionRegistro(AccionRegistroEnum.MODIFICACION.getDescripcion());
		auditoria1.setCveSituacionRegistro(SituacionRegistroEnum.APROBADO.getClave());
		auditoria1.setDesSituacionRegistro(SituacionRegistroEnum.APROBADO.getDescripcion());
		auditoria1.setCveEstadoRegistro(registro.getAseguradoDTO().getCveEstadoRegistro());
		auditoria1.setDesEstadoRegistro(registro.getAseguradoDTO().getDesEstadoRegistro());
		auditoria1.setOrdenEjecucion(2);
		auditorias.add(auditoria);
		auditorias.add(auditoria1);
		return auditorias;
	}

	public int insertaRegistros(List<DetalleRegistroDTO> detalles) {
		return detalleRegistroRepository.guardarDetalles(detalles);
	}
	
	public void insertaRegistro(DetalleRegistroDTO detalles) {
		detalleRegistroRepository.guardarDetalle(detalles);
	}

	private int actualizaSusceptibles(List<DetalleRegistroDTO> detalles, List<String> archivos) {
		int i = 0;
		for (DetalleRegistroDTO detalle : detalles) {
			detalle.setAuditorias(crearBitacoras(detalle));
			if (!Arrays.asList(4, 8, 3, 7).contains(detalle.getAseguradoDTO().getCveEstadoRegistro())) {
				Optional<ArchivoDTO> optional = archivoRepository
						.findOneById(detalle.getIdentificadorArchivo().toString());
				if (optional.isPresent()) {
					ArchivoDTO archivoDTO = optional.get();
					if (!archivos.contains(archivoDTO.getNomArchivo())) {
						archivos.add(archivoDTO.getNomArchivo());
					}
				}
				if (Arrays.asList(1, 2).contains(detalle.getAseguradoDTO().getCveEstadoRegistro())) {
					detalle.getAseguradoDTO()
							.setCveEstadoRegistro(EstadoRegistroEnum.SUSCEPTIBLE.getCveEstadoRegistro());
					detalle.getAseguradoDTO().setDesEstadoRegistro(EstadoRegistroEnum.SUSCEPTIBLE.getDesDescripcion());
				} else {
					detalle.getAseguradoDTO()
							.setCveEstadoRegistro(EstadoRegistroEnum.SUSCEPTIBLE_OTRAS.getCveEstadoRegistro());
					detalle.getAseguradoDTO()
							.setDesEstadoRegistro(EstadoRegistroEnum.SUSCEPTIBLE_OTRAS.getDesDescripcion());
				}
				detalleRegistroRepository.actualizaDetalle(detalle);
			}
			i++;
		}
		return i;
	}

	@Override
	public void obtenerCifrasControl(List<String> nombreArchivoList) {
		nombreArchivoList.forEach(nombre -> {
			Optional<ArchivoDTO> optional = archivoRepository.findOneByName(nombre);
			if (optional.isPresent()) {
				ArchivoDTO archivoDTO = optional.get();
				CifrasControlDTO cifras = detalleRegistroRepository.obtenerCifrasControl(nombre);
				cifras.setNumTotalRegistros(cifras.sumatoria());
				archivoDTO.setCifrasControlDTO(cifras);
				archivoRepository.actualizaCifras(archivoDTO);
			}
		});
	}

}
