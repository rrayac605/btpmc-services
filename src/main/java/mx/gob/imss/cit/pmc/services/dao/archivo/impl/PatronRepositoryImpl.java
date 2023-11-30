package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.PatronDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.PatronRepository;
import mx.gob.imss.cit.pmc.services.rowmapper.PatronRowMapper;

@Repository
public class PatronRepositoryImpl implements PatronRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public PatronDTO obtenerPatron(String regPatronal) {
		StringBuilder consulta = new StringBuilder();
		consulta.append("SELECT DIV.NUM_DIVISION, ");
		consulta.append("  GPO.NUM_GRUPO, ");
		consulta.append("  CL.CVE_ID_CLASE, ");
		consulta.append("  CL.DES_CLASE, ");
		consulta.append("  FR.NUM_FRACCION, ");
		consulta.append("  FR.DES_FRACCION, ");
		consulta.append("  FR.CVE_ID_FRACCION, ");
		consulta.append("  D.CLAVE_DELEGACION, ");
		consulta.append("  D.DES_DELEG, ");
		consulta.append("  SD.CLAVE_SUBDELEGACION, ");
		consulta.append("  SD.DES_SUBDELEGACION, ");
		consulta.append("  CL.NUM_PRIMA_MEDIA, ");
		consulta.append("  pfm.rfc, ");
		consulta.append("  pfm.nombre, ");
		consulta.append("  llave.REF_BUSCA, ");
		consulta.append("  clas.NUM_PRIMA_PAGO ");
		consulta.append("FROM MGPBDTU9X.DIT_LLAVE_PATRON llave, ");
		consulta.append("  MGPBDTU9X.dit_delsub_pat_suj_oblig patSub, ");
		consulta.append("  MGPBDTU9X.dic_subdelegacion sd, ");
		consulta.append("  MGPBDTU9X.dic_delegacion d, ");
		consulta.append("  MGPBDTU9X.dit_clasificacion clas, ");
		consulta.append("  MGPBDTU9X.DIC_FRACCION_CLASE fc, ");
		consulta.append("  MGPBDTU9X.DIC_CLASE cl, ");
		consulta.append("  MGPBDTU9X.DIC_FRACCION fr, ");
		consulta.append("  MGPBDTU9X.dic_grupo gpo, ");
		consulta.append("  MGPBDTU9X.dic_division div, ");
		consulta.append("  (SELECT * ");
		consulta.append("  FROM ");
		consulta.append("    (SELECT llave.REF_BUSCA AS reg_patron, ");
		consulta.append("      pm.rfc, ");
		consulta.append("      pm.denominacion_razon_social AS nombre ");
		consulta.append("    FROM MGPBDTU9X.DIT_LLAVE_PATRON llave, ");
		consulta.append("      MGPBDTU9X.dit_persona_moral pm ");
		consulta.append("    WHERE LLAVE.REF_BUSCA        = ? ");
		consulta.append("    AND llave.cve_id_persona_moral = pm.cve_id_persona_moral ");
		consulta.append("    UNION ");
		consulta.append("    SELECT LLAVE.REF_BUSCA AS reg_patron , ");
		consulta.append("      PF.RFC, ");
		consulta.append("      P.NOM_NOMBRE ");
		consulta.append("      || ' ' ");
		consulta.append("      ||P.NOM_PRIMER_APELLIDO ");
		consulta.append("      || ' ' ");
		consulta.append("      ||P.NOM_SEGUNDO_APELLIDO AS nombre ");
		consulta.append("    FROM MGPBDTU9X.DIT_LLAVE_PATRON llave , ");
		consulta.append("      MGPBDTU9X.dit_persona_fisica pf, ");
		consulta.append("      MGPBDTU9X.dit_persona p ");
		consulta.append("    WHERE LLAVE.REF_BUSCA       = ? ");
		consulta.append("    AND LLAVE.CVE_ID_PERSONA_FISICA = PF.CVE_ID_PERSONA_FISICA ");
		consulta.append("    AND PF.CVE_ID_PERSONA           = P.CVE_ID_PERSONA ");
		consulta.append("    ) pfm ");
		consulta.append("  ) pfm ");
		consulta.append("WHERE LLAVE.REF_BUSCA                   = ? ");
		consulta.append("AND LLAVE.REF_BUSCA                     = pfm.reg_patron ");
		consulta.append("AND LLAVE.CVE_ID_PATRON_SUJETO_OBLIGADO = PATSUB.CVE_ID_PATRON_SUJETO_OBLIGADO ");
		consulta.append("AND PATSUB.CVE_ID_SUBDELEGACION         = SD.CVE_ID_SUBDELEGACION ");
		consulta.append("AND SD.CVE_ID_DELEGACION                = D.CVE_ID_DELEGACION ");
		consulta.append("AND llave.CVE_ID_PATRON_SUJETO_OBLIGADO = CLAS.CVE_ID_PATRON_SUJETO_OBLIGADO ");
		consulta.append("AND CLAS.CVE_ID_FRACCION_CLASE          = FC.CVE_ID_FRACCION_CLASE ");
		consulta.append("AND FC.CVE_ID_CLASE                     = CL.CVE_ID_CLASE ");
		consulta.append("AND FC.CVE_ID_FRACCION                  = FR.CVE_ID_FRACCION ");
		consulta.append("AND FR.CVE_ID_GRUPO                     = GPO.CVE_ID_GRUPO ");
		consulta.append("AND GPO.CVE_ID_DIVISION                 = div.CVE_ID_DIVISION ");
		return jdbcTemplate.queryForObject(consulta.toString(), new Object[] { regPatronal, regPatronal, regPatronal },
				new PatronRowMapper());
	}

}
