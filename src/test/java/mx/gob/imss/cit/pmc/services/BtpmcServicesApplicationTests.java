package mx.gob.imss.cit.pmc.services;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import mx.gob.imss.cit.pmc.commons.dto.ParametroDTO;
import mx.gob.imss.cit.pmc.commons.to.EmailTO;
import mx.gob.imss.cit.pmc.commons.to.MailTO;
import mx.gob.imss.cit.pmc.commons.utils.DateUtils;
import mx.gob.imss.cit.pmc.services.configuration.ApplicationConfig;
import mx.gob.imss.cit.pmc.services.dao.archivo.ParametroRepository;
import mx.gob.imss.cit.pmc.services.enums.PlantillaEmailEnum;
import mx.gob.imss.cit.pmc.services.exception.EmailException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BtpmcServicesApplicationTests {
	
	private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	@Autowired
	EmailService emailService;

	@Autowired
	private ParametroRepository parametroRepository;

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	@Ignore
	void sendEmailx() {
		MailTO mail = new MailTO();
		mail.setMailFrom("pmc@imss.gob.mx");
		mail.setMailTo(new String[] { "alberto.espvaz@gmail.com", "alberto.espinozav@softtek.com" });
		mail.setMailSubject("PMC - Error en el archivo");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("firstName", "Jose");
		model.put("lastName", "Chavan");
		mail.setModel(model);

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//        MailService mailService = (MailService) context.getBean("mailService");
		try {
			emailService.sendEmail(mail, PlantillaEmailEnum.ARCHIVO_CON_ERROR);
		} catch (EmailException e) {
			logger.error(e.getMessage(), e);
		}
		context.close();
	}

	@Ignore
	public void consultaParametro() {
		Optional<ParametroDTO> parametro = parametroRepository.findOneByCve("nssa.mailTo");
		if (parametro.isPresent()) {
			EmailTO emailTo;
			Gson gson = new Gson();
			emailTo = gson.fromJson(parametro.get().getDesParametro(),EmailTO.class);
			System.out.println(parametro.get().getDesParametro().toString());
			System.out.println(emailTo.nombre);
		}
		if (parametro != null) {
			assertTrue(parametro.isPresent());
		}
	}
	
	@Test
	public void consultaParametroSimple() {
		Optional<ParametroDTO> parametro = parametroRepository.findOneByCve("sisat.mailNoExistSubject");
		if (parametro.isPresent()) {
			parametro.get().getDesParametro();
			System.out.println(parametro.get().getDesParametro());
		}
		if (parametro != null) {
			assertTrue(parametro.isPresent());
		}
	}
	
	

}
