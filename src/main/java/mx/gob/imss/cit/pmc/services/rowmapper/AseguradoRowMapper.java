package mx.gob.imss.cit.pmc.services.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import mx.gob.imss.cit.pmc.commons.dto.AseguradoDTO;

public class AseguradoRowMapper implements RowMapper<AseguradoDTO> {

	@Override
	public AseguradoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		AseguradoDTO aseguradoDTO = new AseguradoDTO();

		StringBuilder sb = new StringBuilder();
		sb.append(rs.getString("NOM_PRIMER_APELLIDO"));
		sb.append("$");
		sb.append(rs.getString("NOM_SEGUNDO_APELLIDO"));
		sb.append("$");
		sb.append(rs.getString("NOM_NOMBRE"));
		aseguradoDTO.setCveIdPersona(rs.getInt("CVE_ID_PERSONA"));
		aseguradoDTO.setRefCurp(rs.getString("CURP"));
		aseguradoDTO.setNomAsegurado(sb.toString());
		aseguradoDTO.setCveSubdelNss(rs.getInt("CLAVE_SUBDELEGACION"));
		aseguradoDTO.setDesSubDelNss(rs.getString("DES_SUBDELEGACION"));
		aseguradoDTO.setCveDelegacionNss(rs.getInt("CLAVE_DELEGACION"));
		aseguradoDTO.setDesDelegacionNss(rs.getString("DES_DELEG"));
		aseguradoDTO.setCveUmfAdscripcion(rs.getInt("NUM_ECONOM"));
		aseguradoDTO.setDesUmfAdscripcion(rs.getString("NOM_UNIDAD"));
		return aseguradoDTO;
	}

}
