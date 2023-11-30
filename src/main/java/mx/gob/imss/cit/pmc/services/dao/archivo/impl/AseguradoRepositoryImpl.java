package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.gob.imss.cit.pmc.commons.dto.AseguradoDTO;
import mx.gob.imss.cit.pmc.services.dao.archivo.AseguradoRepository;
import mx.gob.imss.cit.pmc.services.rowmapper.AseguradoRowMapper;

@Repository
public class AseguradoRepositoryImpl implements AseguradoRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public AseguradoDTO existeAsegurado(String nssAsegurado) {
		AseguradoDTO aseguradoDTO = new AseguradoDTO();
		aseguradoDTO.setNumNss(nssAsegurado);
		StringBuilder consulta = new StringBuilder();
		consulta.append("SELECT ase.ref_busca AS NSS, ");
		consulta.append("  ASE.CVE_ID_PERSONA, ");
		consulta.append("  P.CURP, ");
		consulta.append("  P.NOM_NOMBRE, ");
		consulta.append("  P.NOM_PRIMER_APELLIDO, ");
		consulta.append("  P.NOM_SEGUNDO_APELLIDO, ");
		consulta.append("  SD.CLAVE_SUBDELEGACION, ");
		consulta.append("  SD.DES_SUBDELEGACION, ");
		consulta.append("  D.CLAVE_DELEGACION, ");
		consulta.append("  d.DES_DELEG , ");
		consulta.append("  UMF.NUM_ECONOM, ");
		consulta.append("  UMF.NOM_UNIDAD, ");
		consulta.append("  sd.CVE_ID_SUBDELEGACION ");
		consulta.append("FROM MGPBDTU9X.dit_llave_asegurado ase, ");
		consulta.append("  MGPBDTU9X.dit_grupo_familiar gf, ");
		consulta.append("  MGPBDTU9X.dit_persona p, ");
		consulta.append("  MGPBDTU9X.dic_umf umf, ");
		consulta.append("  MGPBDTU9X.dic_subdelegacion sd, ");
		consulta.append("  MGPBDTU9X.dic_delegacion d, ");
		consulta.append("  MGPBDTU9X.DIT_UMF_CONS_TURNO_MEDICO uctm, ");
		consulta.append("  MGPBDTU9X.DIT_UMF_CONSULTORIO_TURNO uct, ");
		consulta.append("  MGPBDTU9X.DIC_CONSULTORIO_UMF cu ");
		consulta.append("WHERE ase.ref_busca              = ? ");
		consulta.append("AND ase.cve_id_persona           = p.cve_Id_persona ");
		consulta.append("AND ase.cve_id_asignacion_nss    = gf.cve_id_asignacion_nss (+) ");
		consulta.append("AND ASE.CVE_ID_PERSONA           = GF.CVE_ID_PERSONA_INTEGRANTE(+) ");
		consulta.append("AND gf.CVE_ID_UMF_CONS_TURNO_MED = uctm.CVE_ID_UMF_CONS_TURNO_MED(+) ");
		consulta.append("AND uctm.CVE_ID_UMF_CONS_TURNO   = uct.CVE_ID_UMF_CONS_TURNO(+) ");
		consulta.append("AND uct.CVE_ID_UMF_CONSULTORIO   = cu.CVE_ID_UMF_CONSULTORIO(+) ");
		consulta.append("AND cu.CVE_ID_UMF                = umf.cve_id_umf(+) ");
		consulta.append("AND umf.cve_id_subdelegacion     = sd.cve_id_subdelegacion (+) ");
		consulta.append("AND sd.CVE_ID_DELEGACION         = d.cve_id_delegacion (+) ");
		return jdbcTemplate.queryForObject(consulta.toString(), new Object[] { nssAsegurado },
				new AseguradoRowMapper());
	}
	
	@Override
	public boolean existeMarcaAfiliatoria(String nssAsegurado) {
		AseguradoDTO aseguradoDTO = new AseguradoDTO();
		aseguradoDTO.setNumNss(nssAsegurado);
		StringBuilder consulta = new StringBuilder();
		consulta.append("select count(1) from mgpbdtu9x.dit_asignacion_nss_cl2 where num_nss = ?");
		BigDecimal resultado = jdbcTemplate.queryForObject(consulta.toString(), new Object[] { nssAsegurado },
				BigDecimal.class);
		return resultado.intValue() > 0;
	}

}
