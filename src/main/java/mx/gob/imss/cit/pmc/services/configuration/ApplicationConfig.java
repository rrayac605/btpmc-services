package mx.gob.imss.cit.pmc.services.configuration;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.client.RestTemplate;

import mx.gob.imss.cit.pmc.commons.dto.ParametroDTO;
import mx.gob.imss.cit.pmc.commons.enums.ParametrosEnum;
import mx.gob.imss.cit.pmc.commons.to.MailTO;
import mx.gob.imss.cit.pmc.commons.to.PlantillaEmailTO;
import mx.gob.imss.cit.pmc.services.EmailParametrosService;
import mx.gob.imss.cit.pmc.services.dao.archivo.ParametroRepository;
import mx.gob.imss.cit.pmc.services.enums.CasoValidadoEnum;
import mx.gob.imss.cit.pmc.services.enums.PlantillaEmailEnum;
import mx.gob.imss.cit.pmc.services.enums.SistemaOrigenEnum;
import mx.gob.imss.cit.pmc.services.exception.EmailException;
import mx.gob.imss.cit.pmc.services.impl.EmailParametrosServiceImpl;
import mx.gob.imss.cit.pmc.services.impl.EmailServiceImpl;

@Configuration
@ComponentScan(basePackages = "mx.gob.imss.cit.pmc")
public class ApplicationConfig {
	
	private final static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

	@Autowired
	private ParametroRepository parametroRepository;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		Optional<ParametroDTO> host = parametroRepository.findOneByCve("config.mailHost");
		Optional<ParametroDTO> port = parametroRepository.findOneByCve("config.mailPort");
		Optional<ParametroDTO> email = parametroRepository.findOneByCve("config.email");
		Optional<ParametroDTO> emailPass = parametroRepository.findOneByCve("config.emailPass");
		Optional<ParametroDTO> ttlsEnable = parametroRepository.findOneByCve("config.mail.smtp.starttls.enable");
		Optional<ParametroDTO> ttlsRequired = parametroRepository.findOneByCve("config.mail.smtp.starttls.required");
		Optional<ParametroDTO> auth = parametroRepository.findOneByCve("config.mail.smtp.auth");
		Optional<ParametroDTO> protocol = parametroRepository.findOneByCve("config.mail.transport.protocol");
		Optional<ParametroDTO> quitwait = parametroRepository.findOneByCve("config.mail.smtp.starttls.quitwait");
		Optional<ParametroDTO> debug = parametroRepository.findOneByCve("config.mail.debug");

		mailSender.setHost(host.get().getDesParametro());
		mailSender.setPort(Integer.valueOf(port.get().getDesParametro()).intValue());

		mailSender.setUsername(email.get().getDesParametro());
		mailSender.setPassword(emailPass.get().getDesParametro());

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", ttlsEnable.get().getDesParametro());
		javaMailProperties.put("mail.smtp.starttls.required", ttlsRequired.get().getDesParametro());
		javaMailProperties.put("mail.smtp.auth", auth.get().getDesParametro());
		javaMailProperties.put("mail.transport.protocol", protocol.get().getDesParametro());
		javaMailProperties.put("mail.smtp.starttls.quitwait", quitwait.get().getDesParametro());
		javaMailProperties.put("mail.debug", debug.get().getDesParametro());

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	@Bean(name = "getFreeMarkerConfigurationPMC")
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfigurationPMC() {
		FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
		fmConfigFactoryBean.setTemplateLoaderPath("classpath:/mailTemplates/");
		return fmConfigFactoryBean;
	}

	@Bean(name = "consultarImg")
	public InputStream consultarImg() throws URISyntaxException {
		return ApplicationConfig.class.getResourceAsStream("/consultar.png");

	}

	@Bean(name = "footerImg")
	public InputStream footerImg() throws URISyntaxException {
		return ApplicationConfig.class.getResourceAsStream("/footer.png");
	}

	@Bean(name = "headerImg")
	public InputStream headerImg() throws URISyntaxException {
		return ApplicationConfig.class.getResourceAsStream("/header.png");
	}

	@Bean(name = "urlPatron")
	public String getUrlPatron() {
		Optional<ParametroDTO> urlPatron = parametroRepository
				.findOneByCve(ParametrosEnum.URL_PATRON.getIdentificador());
		return urlPatron.get().getDesParametro();
	}

	@Bean(name = "urlAsegurado")
	public String getUrlAsegurado() {
		Optional<ParametroDTO> urlPatron = parametroRepository
				.findOneByCve(ParametrosEnum.URL_ASEGURADO.getIdentificador());
		return urlPatron.get().getDesParametro();
	}

	@Bean(name = "urlDelegacion")
	public String getUrlDelegacion() {
		Optional<ParametroDTO> urlPatron = parametroRepository
				.findOneByCve(ParametrosEnum.URL_DELEGACION.getIdentificador());
		return urlPatron.get().getDesParametro();
	}

	@Bean(name = "urlSubDelegacion")
	public String getUrlSubDelegacion() {
		Optional<ParametroDTO> urlPatron = parametroRepository
				.findOneByCve(ParametrosEnum.URL_SUBDELEGACION.getIdentificador());
		return urlPatron.get().getDesParametro();
	}
	
	@Bean(name = "urlUMF")
	public String getUrlUMF() {
		Optional<ParametroDTO> urlPatron = parametroRepository.findOneByCve(ParametrosEnum.URL_UMF.getIdentificador());
		return urlPatron.get().getDesParametro();
	}

	@Bean(name = "sftpHost")
	public String getSftpHost() {
		Optional<ParametroDTO> sftpHost = parametroRepository.findOneByCve(ParametrosEnum.SFTP_HOST.getIdentificador());
		return sftpHost.get().getDesParametro();
	}

	@Bean(name = "sftpUser")
	public String getSftpUsuario() {
		Optional<ParametroDTO> sftpUser = parametroRepository.findOneByCve(ParametrosEnum.SFTP_USER.getIdentificador());
		return sftpUser.get().getDesParametro();
	}

	@Bean(name = "sftpPassword")
	public String getSftpPassword() {
		Optional<ParametroDTO> sftpPassword = parametroRepository
				.findOneByCve(ParametrosEnum.SFTP_PASSWORD.getIdentificador());
		return sftpPassword.get().getDesParametro();
	}

	@Bean(name = "sftpPort")
	public int getSftpPort() {
		Optional<ParametroDTO> sftpPort = parametroRepository.findOneByCve(ParametrosEnum.SFTP_PORT.getIdentificador());
		return Integer.valueOf(sftpPort.get().getDesParametro());
	}

	@Bean(name = "sftp")
	public boolean getSftp() {
		Optional<ParametroDTO> sftp = parametroRepository.findOneByCve(ParametrosEnum.SFTP.getIdentificador());
		return Boolean.valueOf(sftp.get().getDesParametro());
	}

	public static void main(String args[]) {

		MailTO mail = new MailTO();

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		EmailServiceImpl emailService = (EmailServiceImpl) context.getBean("emailService");
		EmailParametrosService emailParameterService = (EmailParametrosServiceImpl) context
				.getBean("emailParametrosService");
		PlantillaEmailTO plantilla = emailParameterService.obtenerParametrosEmail(SistemaOrigenEnum.SISAT,
				CasoValidadoEnum.ERROR_ARCHIVO_NO_EXISTE);

//        lidia.lopez@imss.gob.mx, albertoony2k@hotmail.com;alberto.espinozav@softtek.com;alberto.espvaz@gmail.com;vannyhz@gmail.com;vanessa.hernandez@imss.gob.mx;roberto.raya@softtek.com;davido.sanchez@softtek.com
		mail.setMailFrom(plantilla.getMailFrom());
		mail.setMailTo(plantilla.getMailTo());
		mail.setMailCc(plantilla.getMailCc());
		mail.setMailSubject(plantilla.getSubject());

		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("nombreCompleto", plantilla.getNombreCompleto());
		model.put("fechaEnvio", plantilla.getFechaEnvio());
		model.put("horaEnvio", plantilla.getHoraEnvio());
		model.put("sistemaOrigen", plantilla.getSistemaOrigen());
		model.put("diasEnvio", plantilla.getDiasEnvio());
		model.put("urlConsultar", plantilla.getUrlConsultar());

		model.put("errorArchivo", plantilla.getDescError());

		mail.setModel(model);

		try {
			emailService.sendEmail(mail, PlantillaEmailEnum.SIN_ARCHIVO);
		} catch (EmailException e) {
			logger.error(e.getMessage(), e);
		}
		context.close();
	}

}