package mx.gob.imss.cit.pmc.services.impl;

import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import mx.gob.imss.cit.pmc.commons.to.MailTO;
import mx.gob.imss.cit.pmc.services.EmailService;
import mx.gob.imss.cit.pmc.services.enums.PlantillaEmailEnum;
import mx.gob.imss.cit.pmc.services.exception.EmailException;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	private final static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private static final String FOOTER = "footer.png";
	private static final String HEADER = "header.png";

	@Autowired
	private JavaMailSender mailSender;

	@Autowired()
	@Qualifier("getFreeMarkerConfigurationPMC")
	Configuration fmConfiguration;

	@Resource
	private InputStream consultarImg;

	@Resource
	private InputStream footerImg;

	@Resource
	private InputStream headerImg;

	@Override
	public void sendEmail(MailTO mail, PlantillaEmailEnum plantillaEmail) throws EmailException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(mail.getMailFrom());
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setCc(mail.getMailCc());
			mail.setMailContent(geContentFromTemplate(mail.getModel(), plantillaEmail));
			mimeMessageHelper.setText(mail.getMailContent(), true);

			if (headerImg != null && footerImg != null) {
				try {
					mimeMessageHelper.addInline(HEADER, new ClassPathResource("/static/images/header.png"));
					mimeMessageHelper.addInline(FOOTER, new ClassPathResource("/static/images/footer.png"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage(), e);
				}
			}

			mailSender.send(mimeMessageHelper.getMimeMessage());
		} catch (MessagingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String geContentFromTemplate(Map<String, Object> model, PlantillaEmailEnum plantillaEmail) {
		StringBuffer content = new StringBuffer();

		try {
			content.append(FreeMarkerTemplateUtils
					.processTemplateIntoString(fmConfiguration.getTemplate(plantillaEmail.getTipoPlantilla()), model));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return content.toString();
	}

}
