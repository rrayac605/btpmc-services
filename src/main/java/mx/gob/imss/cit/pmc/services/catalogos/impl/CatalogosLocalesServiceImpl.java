package mx.gob.imss.cit.pmc.services.catalogos.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.gob.imss.cit.pmc.commons.dto.ActoInseguroDTO;
import mx.gob.imss.cit.pmc.commons.dto.CasoRegistroDTO;
import mx.gob.imss.cit.pmc.commons.dto.CatalogoDTO;
import mx.gob.imss.cit.pmc.commons.dto.CausaExternaDTO;
import mx.gob.imss.cit.pmc.commons.dto.ClaseDTO;
import mx.gob.imss.cit.pmc.commons.dto.CodigoDiagnosticoDTO;
import mx.gob.imss.cit.pmc.commons.dto.ConsecuenciaDTO;
import mx.gob.imss.cit.pmc.commons.dto.EstadoRegistroDTO;
import mx.gob.imss.cit.pmc.commons.dto.LaudoDTO;
import mx.gob.imss.cit.pmc.commons.dto.NaturalezaDTO;
import mx.gob.imss.cit.pmc.commons.dto.OcupacionDTO;
import mx.gob.imss.cit.pmc.commons.dto.OcupacionSisatDTO;
import mx.gob.imss.cit.pmc.commons.dto.RiesgoFisicoDTO;
import mx.gob.imss.cit.pmc.commons.dto.TipoIncapacidadDTO;
import mx.gob.imss.cit.pmc.commons.dto.TipoRiesgoDTO;
import mx.gob.imss.cit.pmc.commons.enums.IdentificadorArchivoEnum;
import mx.gob.imss.cit.pmc.commons.utils.Utils;
import mx.gob.imss.cit.pmc.services.catalogos.CatalogosLocales;
import mx.gob.imss.cit.pmc.services.dao.archivo.ActoInseguroRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.CasoRegistroRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.CausaExternaRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.ClaseRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.CodigoDiagnosticoRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.ConsecuenciaRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.EstadoRegistroRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.LaudoRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.NaturalezaRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.OcupacionRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.RiesgoFisicoRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.TipoIncapacidadRepository;
import mx.gob.imss.cit.pmc.services.dao.archivo.TipoRiesgoRepository;

@Component
public class CatalogosLocalesServiceImpl implements CatalogosLocales {

	private Hashtable<String, CatalogoDTO> casosRegistro;

	private Hashtable<String, CatalogoDTO> estadoRegistro;

	private Hashtable<Integer, CatalogoDTO> ocupacionesSisat;

	private Hashtable<String, CatalogoDTO> consecuencias;

	private Hashtable<String, CatalogoDTO> laudos;

	private Hashtable<String, CatalogoDTO> tiposRiesgo;

	private Hashtable<String, CatalogoDTO> tiposIncapacidad;

	private Hashtable<String, CatalogoDTO> naturalezas;

	private Hashtable<String, CatalogoDTO> causas;

	private Hashtable<Integer, CatalogoDTO> riesgos;

	private Hashtable<String, CatalogoDTO> codigos;

	private Hashtable<Integer, CatalogoDTO> actos;

	private Hashtable<Integer, CatalogoDTO> clases;

	@Autowired
	private CasoRegistroRepository casoRegistroRepository;

	@Autowired
	private EstadoRegistroRepository estadoRegistroRepository;

	@Autowired
	private OcupacionRepository ocupacionRepository;

	@Autowired
	private ConsecuenciaRepository consecuenciaRepository;

	@Autowired
	private LaudoRepository laudoRepository;

	@Autowired
	private TipoRiesgoRepository tipoRiesgoRepository;

	@Autowired
	private NaturalezaRepository naturalezaRepository;

	@Autowired
	private TipoIncapacidadRepository tipoIncapacidadRepository;

	@Autowired
	private CausaExternaRepository causaExternaRepository;

	@Autowired
	private RiesgoFisicoRepository riesgoFisicoRepository;

	@Autowired
	private CodigoDiagnosticoRepository codigoDiagnosticoRepository;

	@Autowired
	private ActoInseguroRepository actoInseguroRepository;

	@Autowired
	private ClaseRepository claseRepository;

	@Override
	public CatalogoDTO obtenerClase(String cveCasoRegistro) {
		if (clases == null) {
			Optional<List<ClaseDTO>> casos = claseRepository.findAll();
			clases = new Hashtable<Integer, CatalogoDTO>();
			for (ClaseDTO casoRegistroDTO : casos.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(casoRegistroDTO.getCveIdClase()));
				catalogoDTO.setDesCatalogo(casoRegistroDTO.getDesClase());
				clases.put(casoRegistroDTO.getCveIdClase(), catalogoDTO);
			}
		}
		return clases.get(Integer.valueOf(cveCasoRegistro));
	}

	@Override
	public CatalogoDTO obtenerCasoRegistro(String cveCasoRegistro) {
		if (casosRegistro == null) {
			Optional<List<CasoRegistroDTO>> casos = casoRegistroRepository.findAll();
			casosRegistro = new Hashtable<String, CatalogoDTO>();
			for (CasoRegistroDTO casoRegistroDTO : casos.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(casoRegistroDTO.getIdCaso()));
				catalogoDTO.setDesCatalogo(casoRegistroDTO.getDescripcion());
				casosRegistro.put(String.valueOf(casoRegistroDTO.getIdCaso()), catalogoDTO);
			}
		}
		return casosRegistro.get(cveCasoRegistro);
	}

	@Override
	public CatalogoDTO obtenerEstadoRegistro(String cveEstadoRegistro) {
		if (estadoRegistro == null) {
			Optional<List<EstadoRegistroDTO>> estados = estadoRegistroRepository.findAll();
			estadoRegistro = new Hashtable<String, CatalogoDTO>();
			for (EstadoRegistroDTO estadoRegistroDTO : estados.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(estadoRegistroDTO.getCveIdEstadoRegistro()));
				catalogoDTO.setDesCatalogo(estadoRegistroDTO.getDesEstadoRegistro());
				estadoRegistro.put(String.valueOf(estadoRegistroDTO.getCveIdEstadoRegistro()), catalogoDTO);
			}
		}
		return estadoRegistro.get(cveEstadoRegistro);
	}

	@Override
	public CatalogoDTO obtenerOcupacion(String cveOcupacion, IdentificadorArchivoEnum tipo) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (ocupacionesSisat == null) {
			Optional<List<OcupacionSisatDTO>> ocupacionesLista = ocupacionRepository.findAllSisat();
			ocupacionesSisat = new Hashtable<Integer, CatalogoDTO>();
			for (OcupacionDTO ocupacionDTO : ocupacionesLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(ocupacionDTO.getCveOcupacion()));
				catalogoDTO.setDesCatalogo(ocupacionDTO.getDesOcupacion());
				ocupacionesSisat.put(ocupacionDTO.getCveOcupacion(), catalogoDTO);
			}
		}
		if ((cveOcupacion != null && !cveOcupacion.trim().equals(""))
				&& ocupacionesSisat.get(Utils.validaEntero(cveOcupacion)) != null) {
			catalogoDTOResultado = ocupacionesSisat.get(Utils.validaEntero(cveOcupacion));
		}

		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerConsecuencia(String cveConsencuencia) {
		CatalogoDTO catalogoDTOResultado = null;
		if (consecuencias == null) {
			Optional<List<ConsecuenciaDTO>> consecuenciasLista = consecuenciaRepository.findAll();
			consecuencias = new Hashtable<String, CatalogoDTO>();
			for (ConsecuenciaDTO consecuenciaDTO : consecuenciasLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(consecuenciaDTO.getCveIdConsecuencia()));
				catalogoDTO.setDesCatalogo(consecuenciaDTO.getDesConsecuencia());
				consecuencias.put(String.valueOf(consecuenciaDTO.getCveIdConsecuencia()), catalogoDTO);
			}
		}
		if (consecuencias.get(cveConsencuencia) != null) {
			catalogoDTOResultado = consecuencias.get(cveConsencuencia);
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerLaudo(String cveLaudo) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (laudos == null) {
			Optional<List<LaudoDTO>> laudosLista = laudoRepository.findAll();
			laudos = new Hashtable<String, CatalogoDTO>();
			for (LaudoDTO laudoDTO : laudosLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(laudoDTO.getCveIdLaudo()));
				catalogoDTO.setDesCatalogo(laudoDTO.getDesLaudo());
				laudos.put(String.valueOf(laudoDTO.getCveIdLaudo()), catalogoDTO);
			}
		}
		if (laudos.get(cveLaudo) != null) {
			catalogoDTOResultado = laudos.get(cveLaudo);
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerTipoRiesgo(String cveTipoRiesgo) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (tiposRiesgo == null) {
			Optional<List<TipoRiesgoDTO>> tiposRiesgoLista = tipoRiesgoRepository.findAll();
			tiposRiesgo = new Hashtable<String, CatalogoDTO>();
			for (TipoRiesgoDTO tipoRiesgoDTO : tiposRiesgoLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(tipoRiesgoDTO.getCveIdTipoRegistro()));
				catalogoDTO.setDesCatalogo(tipoRiesgoDTO.getDesDescripcion());
				tiposRiesgo.put(String.valueOf(tipoRiesgoDTO.getCveIdTipoRegistro()), catalogoDTO);
			}
		}
		if (tiposRiesgo.get(cveTipoRiesgo) != null) {
			catalogoDTOResultado = tiposRiesgo.get(cveTipoRiesgo);
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerTipoIncapacidad(String cveTipoIncapacidad) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (tiposIncapacidad == null) {
			Optional<List<TipoIncapacidadDTO>> tiposIncapacidadLista = tipoIncapacidadRepository.findAll();
			tiposIncapacidad = new Hashtable<String, CatalogoDTO>();
			for (TipoIncapacidadDTO tipoIncapacidad : tiposIncapacidadLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(tipoIncapacidad.getCveTipoIncapacidad()));
				catalogoDTO.setDesCatalogo(tipoIncapacidad.getDesDescripcion());
				tiposIncapacidad.put(String.valueOf(tipoIncapacidad.getCveTipoIncapacidad()), catalogoDTO);
			}
		}
		if (tiposIncapacidad.get(cveTipoIncapacidad) != null) {
			catalogoDTOResultado = tiposIncapacidad.get(cveTipoIncapacidad);
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerNaturaleza(String cveNaturaleza) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (naturalezas == null) {
			Optional<List<NaturalezaDTO>> naturalezasLista = naturalezaRepository.findAll();
			naturalezas = new Hashtable<String, CatalogoDTO>();
			for (NaturalezaDTO naturalezaDTO : naturalezasLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(naturalezaDTO.getCveIdNaturaleza()));
				catalogoDTO.setDesCatalogo(naturalezaDTO.getDesNaturaleza());
				naturalezas.put(String.valueOf(naturalezaDTO.getCveIdNaturaleza()), catalogoDTO);
			}
		}
		if (naturalezas.get(cveNaturaleza) != null) {
			catalogoDTOResultado = naturalezas.get(cveNaturaleza);
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerCausaExterna(String cveCausaExterna) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (causas == null) {
			Optional<List<CausaExternaDTO>> naturalezasLista = causaExternaRepository.findAll();
			causas = new Hashtable<String, CatalogoDTO>();
			for (CausaExternaDTO causaExternaDTO : naturalezasLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(causaExternaDTO.getCveIdCausaExterna()));
				catalogoDTO.setDesCatalogo(causaExternaDTO.getDesCausaExterna());
				causas.put(String.valueOf(causaExternaDTO.getCveIdCausaExterna()), catalogoDTO);
			}
		}
		if (causas.get(cveCausaExterna) != null) {
			catalogoDTOResultado = causas.get(cveCausaExterna);
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerRiesgoFisico(String cveIdRiesgoFisico) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (riesgos == null) {
			Optional<List<RiesgoFisicoDTO>> naturalezasLista = riesgoFisicoRepository.findAll();
			riesgos = new Hashtable<Integer, CatalogoDTO>();
			for (RiesgoFisicoDTO riesgoFisicoDTO : naturalezasLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(riesgoFisicoDTO.getCveIdRiesgoFisico()));
				catalogoDTO.setDesCatalogo(riesgoFisicoDTO.getDesRiesgoFisico());
				riesgos.put(riesgoFisicoDTO.getCveIdRiesgoFisico(), catalogoDTO);
			}
		}
		if (Utils.validaEntero(cveIdRiesgoFisico) > 0 && riesgos.get(Utils.validaEntero(cveIdRiesgoFisico)) != null) {
			catalogoDTOResultado = riesgos.get(Utils.validaEntero(cveIdRiesgoFisico));
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerDiagnostico(String cveIdCodigoDiagnostico) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (codigos == null) {
			Optional<List<CodigoDiagnosticoDTO>> naturalezasLista = codigoDiagnosticoRepository.findAll();
			codigos = new Hashtable<String, CatalogoDTO>();
			for (CodigoDiagnosticoDTO codigoDiagnosticoDTO : naturalezasLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(codigoDiagnosticoDTO.getCveCieGenerico()));
				catalogoDTO.setDesCatalogo(codigoDiagnosticoDTO.getDesCodigoDiagnostico());
				codigos.put(codigoDiagnosticoDTO.getCveCieGenerico(), catalogoDTO);
			}
		}
		if (codigos.get(cveIdCodigoDiagnostico) != null) {
			catalogoDTOResultado = codigos.get(cveIdCodigoDiagnostico);
		}
		return catalogoDTOResultado;
	}

	@Override
	public CatalogoDTO obtenerActoInseguro(String cveIdActoInseguro) {
		CatalogoDTO catalogoDTOResultado = new CatalogoDTO();
		if (actos == null) {
			Optional<List<ActoInseguroDTO>> naturalezasLista = actoInseguroRepository.findAll();
			actos = new Hashtable<Integer, CatalogoDTO>();
			for (ActoInseguroDTO actoInseguroDTO : naturalezasLista.get()) {
				CatalogoDTO catalogoDTO = new CatalogoDTO();
				catalogoDTO.setCveCatalogo(String.valueOf(actoInseguroDTO.getCveIdActoInseguro()));
				catalogoDTO.setDesCatalogo(actoInseguroDTO.getDesDescripcion());
				actos.put(actoInseguroDTO.getCveIdActoInseguro(), catalogoDTO);
			}
		}
		if (Utils.validaEntero(cveIdActoInseguro) > 0 && actos.get(Utils.validaEntero(cveIdActoInseguro)) != null) {
			catalogoDTOResultado = actos.get(Utils.validaEntero(cveIdActoInseguro));
		}
		return catalogoDTOResultado;
	}

}
