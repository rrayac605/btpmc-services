package mx.gob.imss.cit.pmc.services.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;

import org.springframework.jdbc.core.RowMapper;

import mx.gob.imss.cit.pmc.commons.dto.PatronDTO;

public class PatronRowMapper implements RowMapper<PatronDTO> {

	@Override
	public PatronDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		@SuppressWarnings("resource")
		Formatter fmt = new Formatter();
		PatronDTO patronDTO = new PatronDTO();
		StringBuilder sb = new StringBuilder();
		sb.append(rs.getInt("NUM_DIVISION"));
		sb.append(rs.getInt("NUM_GRUPO"));
		if (rs.getInt("NUM_FRACCION") < 10) {
			sb.append(0);
		}
		sb.append(rs.getInt("NUM_FRACCION"));
		patronDTO.setCveClase(rs.getInt("CVE_ID_CLASE"));
		patronDTO.setDesClase(rs.getString("DES_CLASE"));
		patronDTO.setCveFraccion(Integer.valueOf(sb.toString()));
		patronDTO.setDesFraccion(rs.getString("DES_FRACCION"));
		patronDTO.setCveDelRegPatronal(rs.getInt("CLAVE_DELEGACION"));
		patronDTO.setDesDelRegPatronal(rs.getString("DES_DELEG"));
		patronDTO.setCveSubDelRegPatronal(rs.getInt("CLAVE_SUBDELEGACION"));
		patronDTO.setDesSubDelRegPatronal(rs.getString("DES_SUBDELEGACION"));
		patronDTO.setNumPrima(rs.getBigDecimal("NUM_PRIMA_MEDIA"));
		patronDTO.setDesRazonSocial(rs.getString("nombre"));
		patronDTO.setDesRfc(rs.getString("rfc"));
		patronDTO.setNumPrima(rs.getBigDecimal("NUM_PRIMA_PAGO"));
		patronDTO.setCveDelegacionAux(String.valueOf(fmt.format("%02d", rs.getInt("CLAVE_DELEGACION"))));
		return patronDTO;
	}

}
