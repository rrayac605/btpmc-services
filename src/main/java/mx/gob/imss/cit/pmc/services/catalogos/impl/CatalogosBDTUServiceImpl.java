package mx.gob.imss.cit.pmc.services.catalogos.impl;

import java.util.Hashtable;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import mx.gob.imss.cit.pmc.commons.dto.DelegacionDTO;
import mx.gob.imss.cit.pmc.commons.dto.SubDelegacionDTO;
import mx.gob.imss.cit.pmc.commons.dto.UMFDTO;
import mx.gob.imss.cit.pmc.commons.utils.Utils;
import mx.gob.imss.cit.pmc.services.catalogos.CatalogosBDTUService;
import mx.gob.imss.cit.pmc.services.dto.DelegacionJsonDTO;
import mx.gob.imss.cit.pmc.services.dto.SubDelegacionJsonDTO;
import mx.gob.imss.cit.pmc.services.dto.UMFJsonDTO;

@Component
public class CatalogosBDTUServiceImpl implements CatalogosBDTUService {
	
	private final static Logger logger = LoggerFactory.getLogger(CatalogosBDTUServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("urlDelegacion")
	private String urlDelegacion;

	@Autowired
	@Qualifier("urlSubDelegacion")
	private String urlSubDelegacion;

	@Autowired
	@Qualifier("urlUMF")
	private String urlUMF;

	private static final String DIAGONAL = "/";

	private static final String SUBDELEGACIONES = "/subDelegacion";

	private static final String UMFS = "/umf";

	private Hashtable<Integer, DelegacionJsonDTO> delegaciones;

	private Hashtable<Integer, UMFJsonDTO> umfs;

	@PostConstruct
	public void onInit() {
		obtenerDelegaciones();
		umfs = new Hashtable<Integer, UMFJsonDTO>();
	}

	private void obtenerDelegaciones() {
		ResponseEntity<DelegacionJsonDTO[]> response = null;
		ResponseEntity<SubDelegacionJsonDTO[]> responseSubdelegaciones = null;
		ResponseEntity<UMFJsonDTO[]> responseUMF = null;
		try {
			if (delegaciones == null) {
				response = restTemplate.getForEntity(urlDelegacion, DelegacionJsonDTO[].class);
				if (response.getBody() != null) {
					delegaciones = new Hashtable<Integer, DelegacionJsonDTO>();
					for (DelegacionJsonDTO key : response.getBody()) {
						responseSubdelegaciones = restTemplate.getForEntity(urlDelegacion.concat(DIAGONAL)
								.concat(String.valueOf(key.getId())).concat(SUBDELEGACIONES),
								SubDelegacionJsonDTO[].class);
						if (responseSubdelegaciones.getBody() != null) {
							Hashtable<Integer, SubDelegacionJsonDTO> subdelegaciones = new Hashtable<Integer, SubDelegacionJsonDTO>();
							for (SubDelegacionJsonDTO sub : responseSubdelegaciones.getBody()) {
								try {
								responseUMF = restTemplate.getForEntity(
										urlSubDelegacion.concat(String.valueOf(sub.getId())).concat(UMFS),
										UMFJsonDTO[].class);
								if (responseUMF != null && responseUMF.getBody() != null) {
									Hashtable<Integer, UMFJsonDTO> umfsLocal = new Hashtable<Integer, UMFJsonDTO>();
									for (UMFJsonDTO umfJsonDTO : responseUMF.getBody()) {
										umfsLocal.put(umfJsonDTO.getNoEconomico(), umfJsonDTO);
									}
									sub.setUmfsJsonDTO(umfsLocal);
								}
								} catch(Exception e) {
									logger.error("Error al consultar umf: delegacion{}, subdelegacion {}", key.getId(), sub.getId(), e);
								}
								subdelegaciones.put(sub.getClave(), sub);
							}
							key.setSubDelegacionesJsonDTO(subdelegaciones);
						}
						delegaciones.put(key.getId(), key);
					}
				}
			}
		} catch (RestClientException e) {
			logger.error("Error al consultar delegaciones", e);
		}
	}

	@Override
	public UMFDTO obtenerUMF(String delegacion, String subDelegacion, String umf) {
		UMFJsonDTO umfJsonDTO = null;
		SubDelegacionJsonDTO subDelegacionJsonDTO = null;
		DelegacionJsonDTO delegacionJsonDTO = null;
		UMFDTO umfDTO = null;
		try {
			delegacionJsonDTO = delegaciones.get(Utils.validaEntero(delegacion));
			if (delegacionJsonDTO != null) {
				subDelegacionJsonDTO = delegacionJsonDTO.getSubDelegacionesJsonDTO()
						.get(Utils.validaEntero(subDelegacion));
				if (subDelegacionJsonDTO != null && subDelegacionJsonDTO.getUmfsJsonDTO() != null) {
					umfJsonDTO = subDelegacionJsonDTO.getUmfsJsonDTO().get(Utils.validaEntero(umf));
					if (umfJsonDTO != null) {
						umfDTO = umfJsonToUmfDTO(umfJsonDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("No existe información existeUMF: {[]}", umf);
		}
		return umfDTO;
	}

	@Override
	public UMFDTO obtenerUMF(String umf) {
		UMFJsonDTO umfJsonDTO = null;
		UMFDTO umfDTO = null;
		try {
			umfJsonDTO = umfs.get(Utils.validaEntero(umf));
			if (umfJsonDTO == null && Utils.validaEntero(umf) > 0) {
				umfJsonDTO = existeNuevaUMF(umf);
			}
			umfDTO = umfJsonToUmfDTO(umfJsonDTO);
		} catch (Exception e) {
			logger.error("No existe información existeUMF:{[]} ", umf);
		}
		return umfDTO;
	}

	private UMFJsonDTO existeNuevaUMF(String umf) {
		ResponseEntity<UMFJsonDTO> response = null;
		UMFJsonDTO umfJsonDTO = null;
		response = restTemplate.getForEntity(urlUMF.concat(umf), UMFJsonDTO.class);
		if (response != null) {
			umfJsonDTO = response.getBody();
			umfs.put(umfJsonDTO.getIdUMF(), umfJsonDTO);
		}
		return umfJsonDTO;
	}

	private UMFDTO umfJsonToUmfDTO(UMFJsonDTO umfJsonDTO) {
		UMFDTO umfDTO = new UMFDTO();
		SubDelegacionDTO subDelegacionDTO = new SubDelegacionDTO();
		SubDelegacionJsonDTO subDelegacionJsonDTO = new SubDelegacionJsonDTO();
		DelegacionDTO delegacionDTO = new DelegacionDTO();
		DelegacionJsonDTO delegacionJsonDTO = new DelegacionJsonDTO();
		if (umfJsonDTO != null) {
			umfDTO.setDescripcion(umfJsonDTO.getDescripcion());
			umfDTO.setIdUMF(umfJsonDTO.getNoEconomico());
			umfDTO.setNombreCorto(umfJsonDTO.getNombreCorto());
			subDelegacionJsonDTO = umfJsonDTO.getSubdelegacion();
			if (umfJsonDTO.getSubdelegacion() != null) {
				subDelegacionDTO.setClave(subDelegacionJsonDTO.getClave());
				subDelegacionDTO.setDescripcion(subDelegacionJsonDTO.getDescripcion());
				subDelegacionDTO.setId(subDelegacionJsonDTO.getId());
				delegacionJsonDTO = subDelegacionJsonDTO.getDelegacion();
				if (delegacionJsonDTO != null) {
					delegacionDTO.setCiz(delegacionJsonDTO.getCiz());
					delegacionDTO.setClave(delegacionJsonDTO.getClave());
					delegacionDTO.setDescripcion(delegacionJsonDTO.getDescripcion());
					delegacionDTO.setId(delegacionJsonDTO.getId());
				}
			}
		}
		subDelegacionDTO.setDelegacion(delegacionDTO);
		umfDTO.setSubdelegacion(subDelegacionDTO);
		return umfDTO;
	}

	@Override
	public SubDelegacionDTO obtenerSubDelegacion(String delegacion, String subDelegacion) {
		SubDelegacionJsonDTO subDelegacionJsonDTO = null;
		SubDelegacionDTO subDelegacionDTO = null;
		DelegacionJsonDTO delegacionJsonDTO = null;
		try {
			delegacionJsonDTO = delegaciones.get(Utils.validaEntero(delegacion));
			if (delegacionJsonDTO != null) {
				subDelegacionJsonDTO = delegacionJsonDTO.getSubDelegacionesJsonDTO()
						.get(Utils.validaEntero(subDelegacion));
				if (subDelegacionJsonDTO != null) {
					subDelegacionDTO = subDelegacionJsonToSubDelegacionDTO(subDelegacionJsonDTO);
				}
			}
		} catch (Exception e) {
			logger.error("No existe información obtenerSubDelegacion:{[]} ", subDelegacion);
		}
		return subDelegacionDTO;
	}

	private SubDelegacionDTO subDelegacionJsonToSubDelegacionDTO(SubDelegacionJsonDTO subDelegacionJsonDTO) {
		SubDelegacionDTO subDelegacionDTO = new SubDelegacionDTO();
		DelegacionDTO delegacionDTO = new DelegacionDTO();
		DelegacionJsonDTO delegacionJsonDTO = new DelegacionJsonDTO();
		if (subDelegacionJsonDTO != null) {
			subDelegacionDTO.setClave(subDelegacionJsonDTO.getClave());
			subDelegacionDTO.setDescripcion(subDelegacionJsonDTO.getDescripcion());
			subDelegacionDTO.setId(subDelegacionJsonDTO.getId());
			delegacionJsonDTO = subDelegacionJsonDTO.getDelegacion();
			if (delegacionJsonDTO != null) {
				delegacionDTO.setCiz(delegacionJsonDTO.getCiz());
				delegacionDTO.setClave(delegacionJsonDTO.getClave());
				delegacionDTO.setDescripcion(delegacionJsonDTO.getDescripcion());
				delegacionDTO.setId(delegacionJsonDTO.getId());
			}
		}
		subDelegacionDTO.setDelegacion(delegacionDTO);
		return subDelegacionDTO;
	}

	@Override
	public DelegacionDTO obtenerDelegacion(String delegacion) {
		DelegacionJsonDTO delegacionJsonDTO = null;
		DelegacionDTO delegacionDTO = null;
		try {
			delegacionJsonDTO = delegaciones.get(Utils.validaEntero(delegacion));
			if (delegacionJsonDTO != null) {
				delegacionDTO = delegacionJsonToDelegacionDTO(delegacionJsonDTO);
			}
		} catch (Exception e) {
			logger.error("No existe información obtenerSubDelegacion: {[]}", delegacion);
		}
		return delegacionDTO;
	}

	private DelegacionDTO delegacionJsonToDelegacionDTO(DelegacionJsonDTO delegacionJsonDTO) {
		DelegacionDTO delegacionDTO = new DelegacionDTO();
		if (delegacionJsonDTO != null) {
			delegacionDTO.setCiz(delegacionJsonDTO.getCiz());
			delegacionDTO.setClave(delegacionJsonDTO.getClave());
			delegacionDTO.setDescripcion(delegacionJsonDTO.getDescripcion());
			delegacionDTO.setId(delegacionJsonDTO.getId());
		}

		return delegacionDTO;
	}

}
