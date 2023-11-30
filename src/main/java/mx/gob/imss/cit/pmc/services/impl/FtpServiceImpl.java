package mx.gob.imss.cit.pmc.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mx.gob.imss.cit.pmc.commons.dto.SftpParamsDTO;
import mx.gob.imss.cit.pmc.commons.enums.ParametrosEnum;
import mx.gob.imss.cit.pmc.commons.exception.ParametroNoExisteException;
import mx.gob.imss.cit.pmc.exception.DownloadException;
import mx.gob.imss.cit.pmc.service.SftpClient;
import mx.gob.imss.cit.pmc.services.FtpService;
import mx.gob.imss.cit.pmc.services.ParametrosService;

@Service("ftpService")
public class FtpServiceImpl implements FtpService {

	private final static Logger logger = LoggerFactory.getLogger(FtpServiceImpl.class);

	@Autowired
	private ParametrosService parametrosService;

	@Autowired
	@Qualifier("sftpHost")
	private String sftpHost;

	@Autowired
	@Qualifier("sftpPort")
	private int sftpPort;

	@Autowired
	@Qualifier("sftpUser")
	private String sftpUser;

	@Autowired
	@Qualifier("sftpPassword")
	private String sftpPassword;

	@Autowired
	@Qualifier("sftp")
	private boolean sftp;

	@Override
	public boolean copyFileFromFTP(String nombre) {

		Path sourcepath = null;
		Path destinationepath = null;
		SftpClient client = null;
		boolean exito = false;
		try {
			if (!sftp) {
				sourcepath = Paths.get(parametrosService.obtenerParametro(ParametrosEnum.RUTA_ORIGEN.getIdentificador())
						.getDesParametro().concat(nombre));
				destinationepath = Paths
						.get(parametrosService.obtenerParametro(ParametrosEnum.RUTA_DESTINO.getIdentificador())
								.getDesParametro().concat(nombre));
				try {
					Files.copy(sourcepath, destinationepath, StandardCopyOption.REPLACE_EXISTING);
					exito = true;
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			} else {

				try {
					client = new SftpClient();
					SftpParamsDTO sftpParamsDTO = new SftpParamsDTO();

					sftpParamsDTO.setSftpHost(sftpHost);
					sftpParamsDTO.setSftpPort(sftpPort);
					sftpParamsDTO.setSftpPasword(sftpPassword);
					sftpParamsDTO.setSftpUser(sftpUser);

					client.connect(sftpParamsDTO);

					client.retrieveFile(
							parametrosService.obtenerParametro(ParametrosEnum.RUTA_ORIGEN_SFTP.getIdentificador())
									.getDesParametro().concat(nombre),
							parametrosService.obtenerParametro(ParametrosEnum.RUTA_DESTINO.getIdentificador())
									.getDesParametro().concat(nombre));
					exito = true;
				} catch (DownloadException e) {
					logger.error("", e);
				} finally {
					client.disconnect();
				}
			}
		} catch (ParametroNoExisteException e1) {
			logger.error(e1.getMessage(), e1);
		}
		return exito;
	}

}
