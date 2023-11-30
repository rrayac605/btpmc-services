package mx.gob.imss.cit.pmc.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.gob.imss.cit.pmc.commons.dto.ParametroDTO;
import mx.gob.imss.cit.pmc.commons.to.PlantillaEmailTO;
import mx.gob.imss.cit.pmc.commons.utils.DateUtils;
import mx.gob.imss.cit.pmc.services.EmailParametrosService;
import mx.gob.imss.cit.pmc.services.dao.archivo.ParametroRepository;
import mx.gob.imss.cit.pmc.services.enums.CasoValidadoEnum;
import mx.gob.imss.cit.pmc.services.enums.SistemaOrigenEnum;

@Service("emailParametrosService")
public class EmailParametrosServiceImpl implements EmailParametrosService {

	private static final String COMA = ",";
	@Autowired
	private ParametroRepository parametroRepository;

	@Override
	public PlantillaEmailTO obtenerParametrosEmail(SistemaOrigenEnum sistemaOrigen, CasoValidadoEnum caso) {
		PlantillaEmailTO plantilla = new PlantillaEmailTO();

		switch (sistemaOrigen) {
		case SISAT:
			extraerParametros(caso, plantilla, sistemaOrigen);
			break;
		case NSSA:
			extraerParametros(caso, plantilla, sistemaOrigen);
			break;
		case SUI55:
			extraerParametros(caso, plantilla, sistemaOrigen);
			break;
		}

		return plantilla;
	}

	private void extraerParametros(CasoValidadoEnum caso, PlantillaEmailTO plantilla, SistemaOrigenEnum sistemaOrigen) {
		Optional<ParametroDTO> mailFrom = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailFrom"));
		Optional<ParametroDTO> mailTo = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailTo"));
		Optional<ParametroDTO> mailCc = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailCc"));
		Optional<ParametroDTO> mailNombreCompleto = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailNombreCompleto"));
		Optional<ParametroDTO> mailSisOrigen = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailSisOrigen"));
		Optional<ParametroDTO> mailDiasEnvio = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailDiasEnvio"));
		Optional<ParametroDTO> mailUrlConsultar = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailUrlConsultar"));

		if (mailFrom.isPresent()) {
			plantilla.setMailFrom(mailFrom.get().getDesParametro());
		}
		if (mailCc.isPresent()) {
			plantilla.setMailCc(mailCc.get().getDesParametro().split(COMA));
		}
		if (mailTo.isPresent()) {
			plantilla.setMailTo(mailTo.get().getDesParametro().split(COMA));
		}
		if (mailNombreCompleto.isPresent()) {
			plantilla.setNombreCompleto(mailNombreCompleto.get().getDesParametro());
		}
		if (mailSisOrigen.isPresent()) {
			plantilla.setSistemaOrigen(mailSisOrigen.get().getDesParametro());
		}
		if (mailDiasEnvio.isPresent()) {
			plantilla.setDiasEnvio(mailDiasEnvio.get().getDesParametro());
		}
		if (mailUrlConsultar.isPresent()) {
			plantilla.setUrlConsultar(mailUrlConsultar.get().getDesParametro());
		}
		validarCaso(caso, sistemaOrigen, plantilla);
		plantilla.setFechaEnvio(DateUtils.getFechaActual());
		plantilla.setHoraEnvio(DateUtils.getHoraActual());
	}

	private void validarCaso(CasoValidadoEnum caso, SistemaOrigenEnum sistemaOrigen, PlantillaEmailTO plantilla) {
		Optional<ParametroDTO> mialErrorSubject = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailErrorSubject"));
		Optional<ParametroDTO> mailNoExistSubject = parametroRepository
				.findOneByCve(sistemaOrigen.getSistemaOrigen().concat(".mailNoExistSubject"));
		if (caso.equals(CasoValidadoEnum.ERROR_ARCHIVO_NO_EXISTE)) {
			if (mailNoExistSubject.isPresent()) {
				plantilla.setSubject(mailNoExistSubject.get().getDesParametro());
			}
			Optional<ParametroDTO> mailErrorArchivoNoExiste = parametroRepository
					.findOneByCve("gral.mailErrorArchivoNoExiste");
			if (mailErrorArchivoNoExiste.isPresent()) {
				plantilla.setDescError(mailErrorArchivoNoExiste.get().getDesParametro());
			}
		} else {
			if (mialErrorSubject.isPresent()) {
				plantilla.setSubject(mialErrorSubject.get().getDesParametro());
				Optional<ParametroDTO> mailErrorArchivo = null;

				if (caso.equals(CasoValidadoEnum.ERROR_NUM_REG)) {
					mailErrorArchivo = parametroRepository.findOneByCve("gral.mailErrorNumReg");
				} else if (caso.equals(CasoValidadoEnum.ERROR_ARCHIVO_YA_PROCESADO)
						|| caso.equals(CasoValidadoEnum.ERROR_ARCHIVO_MISMO_NOMBRE)) {
					mailErrorArchivo = parametroRepository.findOneByCve("gral.mailErrorYaProcesado");
				}

				if (mailErrorArchivo.isPresent()) {
					plantilla.setDescError(mailErrorArchivo.get().getDesParametro());
				}
			}
		}
	}

}
