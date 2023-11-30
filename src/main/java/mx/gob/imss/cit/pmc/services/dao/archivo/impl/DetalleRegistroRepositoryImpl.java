package mx.gob.imss.cit.pmc.services.dao.archivo.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.bulk.BulkWriteResult;

import mx.gob.imss.cit.pmc.commons.dto.CifrasControlDTO;
import mx.gob.imss.cit.pmc.commons.dto.DetalleRegistroDTO;
import mx.gob.imss.cit.pmc.commons.dto.MovementsDupDTO;
import mx.gob.imss.cit.pmc.commons.dto.MovementsSusDTO;
import mx.gob.imss.cit.pmc.commons.dto.RegistroDTO;
import mx.gob.imss.cit.pmc.commons.enums.CamposAseguradoEnum;
import mx.gob.imss.cit.pmc.commons.enums.CamposIncapacidadEnum;
import mx.gob.imss.cit.pmc.commons.enums.CamposPatronEnum;
import mx.gob.imss.cit.pmc.commons.utils.CustomAggregationOperation;
import mx.gob.imss.cit.pmc.commons.utils.DateUtils;
import mx.gob.imss.cit.pmc.commons.utils.Utils;
import mx.gob.imss.cit.pmc.services.dao.archivo.DetalleRegistroRepository;
import mx.gob.imss.cit.pmc.services.impl.DetalleRegistroServiceImpl;

@Repository
public class DetalleRegistroRepositoryImpl implements DetalleRegistroRepository {

	private final static Logger logger = LoggerFactory.getLogger(DetalleRegistroServiceImpl.class);

	@Autowired
	private MongoOperations mongoOperations;

	public int guardarDetalles(List<DetalleRegistroDTO> detalles) {

		BulkOperations bulkOperations = mongoOperations.bulkOps(BulkMode.UNORDERED, DetalleRegistroDTO.class);
		for (DetalleRegistroDTO detalle : detalles) {
			bulkOperations.insert(detalle);
		}
		BulkWriteResult result = bulkOperations.execute();
		return result.getInsertedCount();
	}

	@Override
	public boolean existeRegistro(RegistroDTO registroDTO, String tipoArchivo) {

		String[] nombres = Utils.separaNombres(Utils.obtenerNombre(registroDTO));
		String primerApellido = nombres[0];
		String segundoApellido = nombres.length > 1 ? nombres[1] : "";
		String nombre = nombres.length > 2 ? nombres[2] : "";

		TypedAggregation<DetalleRegistroDTO> aggregation = buildAggregationExist(registroDTO, tipoArchivo,
				primerApellido, segundoApellido, nombre, Boolean.FALSE);
		AggregationResults<DetalleRegistroDTO> aggregationResults = mongoOperations.aggregate(aggregation,
				DetalleRegistroDTO.class);
		logger.debug("Resgistro duplicado: ", aggregationResults.getMappedResults().size());
		return aggregationResults.getMappedResults().size() > 0;
	}

	@Override
	public boolean existeRegistroRn78(RegistroDTO registroDTO, String tipoArchivo) {

		String[] nombres = Utils.separaNombres(Utils.obtenerNombre(registroDTO));
		String primerApellido = nombres[0];
		String segundoApellido = nombres.length > 1 ? nombres[1] : "";
		String nombre = nombres.length > 2 ? nombres[2] : "";

		TypedAggregation<DetalleRegistroDTO> aggregation = buildAggregationExist(registroDTO, tipoArchivo,
				primerApellido, segundoApellido, nombre, Boolean.TRUE);
		AggregationResults<DetalleRegistroDTO> aggregationResults = mongoOperations.aggregate(aggregation,
				DetalleRegistroDTO.class);

		return aggregationResults.getMappedResults().size() > 0;
	}

	private TypedAggregation<DetalleRegistroDTO> buildAggregationExist(RegistroDTO registroDTO, String tipoArchivo,
			String primerApellido, String segundoApellido, String nombre, Boolean rn78) {

		LookupOperation lookup = Aggregation.lookup("MCT_ARCHIVO", "identificadorArchivo", "_id", "archivoDTO");
		UnwindOperation unwind = Aggregation.unwind("archivoDTO");
		MatchOperation match = Aggregation.match(
				Criteria.where("archivoDTO." + CamposAseguradoEnum.TIPO_ARCHIVO.getNombreCampo()).is(tipoArchivo));
		ProjectionOperation projection = Aggregation.project("objectId");
		Criteria criteria = new Criteria();
		MatchOperation criteriosExist = Aggregation
				.match(criteria = Criteria.where(CamposAseguradoEnum.FOLIO_ORIGINAL.getNombreCampo())
						.is(registroDTO.getRefFolioOriginal()).and(CamposAseguradoEnum.SALARIO_DIARIO.getNombreCampo())
						.is(Utils.validaBigDecimal(registroDTO.getNumSalarioDiario()))
						.and(CamposIncapacidadEnum.MEDICO_AUTORIZADOR.getNombreCampo())
						.is(registroDTO.getNumMatriculaAutoriza()));

		if (Utils.validaEntero(registroDTO.getCveUmfPagadora()) > 0) {
			criteria.and(CamposAseguradoEnum.UMF_PAGADORA.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveUmfPagadora()));
		}

		if (Utils.validaCadena(registroDTO.getFecAtencion()) != null) {
			criteria.and(CamposIncapacidadEnum.FECHA_ATENCION.getNombreCampo())
					.is(DateUtils.parserFromStringISOMOngo(registroDTO.getFecAtencion()));
		}
		if (Utils.validaCadena(registroDTO.getFecInicioPension()) != null) {
			criteria.and(CamposIncapacidadEnum.FECHA_INICIO_PENSION.getNombreCampo())
					.is(DateUtils.parserFromStringISOMOngo(registroDTO.getFecInicioPension()));
		}
		if (Utils.validaCadena(registroDTO.getFecExpedicionDictamen()) != null) {
			criteria.and(CamposIncapacidadEnum.FECHA_EXPEDICION.getNombreCampo())
					.is(DateUtils.parserFromStringISOMOngo(registroDTO.getFecExpedicionDictamen()));
		}

		Criteria criteria1 = Criteria.where(CamposAseguradoEnum.DELEGACION_NSS.getNombreCampo())
				.is(Utils.validaEntero(registroDTO.getCveDelegacionNss()))
				.and(CamposAseguradoEnum.SUBDELEGACION_NSS.getNombreCampo())
				.is(Utils.validaEntero(registroDTO.getCveSubdelNss()))
				.and(CamposAseguradoEnum.UMF_ADSCRIPCION.getNombreCampo())
				.is(Utils.validaEntero(registroDTO.getCveUmfAdscripcion())).and(CamposPatronEnum.RP.getNombreCampo())
				.is(registroDTO.getRefRegistroPatronal()).and(CamposAseguradoEnum.NSS.getNombreCampo())
				.is(registroDTO.getNumNss()).and(CamposAseguradoEnum.CURP.getNombreCampo())
				.is(Utils.obtenerCurp(registroDTO)).and(CamposAseguradoEnum.NOMBRE.getNombreCampo()).is(nombre)
				.and(CamposAseguradoEnum.PRIMER_APELLIDO.getNombreCampo()).is(primerApellido)
				.and(CamposAseguradoEnum.SEGUNDO_APELLIDO.getNombreCampo()).is(segundoApellido)
				.and(CamposIncapacidadEnum.TIPO_RIESGO.getNombreCampo())
				.is(Utils.validaEntero(registroDTO.getCveTipoRiesgo()))
				.and(CamposIncapacidadEnum.CONSECUENCIA.getNombreCampo())
				.is(Utils.validaEntero(registroDTO.getCveConsecuencia()))
				.and(CamposIncapacidadEnum.DIAS_SUBSIDIADOS.getNombreCampo())
				.is(Utils.validaEntero(registroDTO.getNumDiasSubsidiados()))
				.and(CamposIncapacidadEnum.PORCENTAJE_INCAPACIDAD.getNombreCampo())
				.is(Utils.validaBigDecimal(registroDTO.getPorPorcentajeIncapacidad()))
				.and(CamposIncapacidadEnum.LAUDO.getNombreCampo()).is(Utils.validaEntero(registroDTO.getNumLaudo()));		
		completeAggregateDos(criteria1, registroDTO);
		completeAggregate(criteria1, registroDTO);
		List<AggregationOperation> opperationList = Arrays.asList(Aggregation.match(criteria1),
				rn78 ? null : criteriosExist, lookup, unwind, match, projection);
		opperationList = opperationList.stream().filter(criterios -> criterios != null).collect(Collectors.toList());

		TypedAggregation<DetalleRegistroDTO> aggregation = Aggregation.newAggregation(DetalleRegistroDTO.class,
				opperationList);

		return aggregation;

	}

	@Override
	public List<DetalleRegistroDTO> existeSusceptible(RegistroDTO registroDTO, String tipoArchivo) {

		Query query = new Query(Criteria.where(CamposPatronEnum.RP.getNombreCampo())
				.is(registroDTO.getRefRegistroPatronal()).and(CamposAseguradoEnum.NSS.getNombreCampo())
				.is(registroDTO.getNumNss()).and(CamposAseguradoEnum.CURP.getNombreCampo())
				.is(Utils.obtenerCurp(registroDTO)).and(CamposIncapacidadEnum.TIPO_RIESGO.getNombreCampo())
				.is(Utils.validaEntero(registroDTO.getCveTipoRiesgo())));

		query.fields().include(CamposAseguradoEnum.FOLIO_ORIGINAL.getNombreCampo())
				.include(CamposIncapacidadEnum.FECHA_ATENCION.getNombreCampo())
				.include(CamposIncapacidadEnum.CONSECUENCIA.getNombreCampo())
				.include(CamposIncapacidadEnum.FECHA_INICIO.getNombreCampo())
				.include(CamposIncapacidadEnum.FECHA_FIN.getNombreCampo());

		return mongoOperations.find(query, DetalleRegistroDTO.class);
	}

	private void completeAggregateDos(Criteria criteria1, RegistroDTO registroDTO) {
		if (Utils.validaEntero(registroDTO.getCveOcupacion()) > 0) {
			criteria1.and(CamposAseguradoEnum.OCUPACION.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveOcupacion()));
		}
		if (Utils.validaEntero(registroDTO.getCveActoInseguro()) > 0) {
			criteria1.and(CamposIncapacidadEnum.ACTO_INSEGURO.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveActoInseguro()));
		}
		if (Utils.validaEntero(registroDTO.getCveRiesgoFisico()) > 0) {
			criteria1.and(CamposIncapacidadEnum.RIESGO_FISICO.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveRiesgoFisico()));
		}
		if (Utils.validaEntero(registroDTO.getCveNaturaleza()) > 0) {
			criteria1.and(CamposIncapacidadEnum.NATURALEZA.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveNaturaleza()));
		}
		if (Utils.validaEntero(registroDTO.getNumMatriculaTratante()) > 0) {
			criteria1.and(CamposIncapacidadEnum.MEDICO_TRATANTE.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getNumMatriculaTratante()));
		}
	}

	private void completeAggregate(Criteria criteria1, RegistroDTO registroDTO) {
		if (Utils.validaEntero(registroDTO.getNumCodigoDiagnostico()) > 0) {
			criteria1.and(CamposIncapacidadEnum.CODIGO_DIAGNOSTICO.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getNumCodigoDiagnostico()));
		}
		if (Utils.validaEntero(registroDTO.getCveDelegacionAtencion()) > 0) {
			criteria1.and(CamposAseguradoEnum.DELEGACION_ATENCION.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveDelegacionAtencion()));
		}
		if (Utils.validaEntero(registroDTO.getCveSubDelAtencion()) > 0) {
			criteria1.and(CamposAseguradoEnum.SUBDELEGACION_ATENCION.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveSubDelAtencion()));
		}
		if (Utils.validaCadena(registroDTO.getFecInicio()) != null) {
			criteria1.and(CamposIncapacidadEnum.FECHA_INICIO.getNombreCampo())
					.is(DateUtils.parserFromStringISOMOngo(registroDTO.getFecInicio()));
		}
		if (Utils.validaCadena(registroDTO.getFecAccidente()) != null) {
			criteria1.and(CamposIncapacidadEnum.FECHA_ACCIDENTE.getNombreCampo())
					.is(DateUtils.parserFromStringISOMOngo(registroDTO.getFecAccidente()));
		}
		if (Utils.validaCadena(registroDTO.getFecFin()) != null) {
			criteria1.and(CamposIncapacidadEnum.FECHA_FIN.getNombreCampo())
					.is(DateUtils.parserFromStringISOMOngo(registroDTO.getFecFin()));
		}
		if (Utils.validaCadena(registroDTO.getFecAltaRegistro()) != null) {
			criteria1.and(CamposIncapacidadEnum.FECHA_ALTA.getNombreCampo())
					.is(DateUtils.parserFromStringISOMOngo(registroDTO.getFecAltaRegistro()));
		}
		if (Utils.validaEntero(registroDTO.getCveUmfExp()) > 0) {
			criteria1.and(CamposAseguradoEnum.UMF_EXPEDIDORA.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveUmfExp()));
		}
		if (Utils.validaEntero(registroDTO.getCveCausaExterna()) > 0) {
			criteria1.and(CamposIncapacidadEnum.CAUSA_EXTERNA.getNombreCampo())
					.is(Utils.validaEntero(registroDTO.getCveCausaExterna()));
		}
	}

	@Override
	public List<MovementsDupDTO> validateDuplicated(String nombreArchivo) {
		TypedAggregation<MovementsDupDTO> aggregation = buildAggregationDup(nombreArchivo);
		AggregationResults<MovementsDupDTO> aggregationResults = mongoOperations.aggregate(aggregation,
				DetalleRegistroDTO.class, MovementsDupDTO.class);
		return aggregationResults.getMappedResults();
	}

	private TypedAggregation<MovementsDupDTO> buildAggregationDup(String nombreArchivo) {
		CustomAggregationOperation group = new CustomAggregationOperation(buildGroupDupString());
		CustomAggregationOperation addFields = new CustomAggregationOperation(buildAddFields());
		MatchOperation match = Aggregation.match(Criteria.where("numberOfIds").gt(1));
		CustomAggregationOperation unwindMovements = new CustomAggregationOperation(buildUnwind());
		LookupOperation lookup = Aggregation.lookup("MCT_ARCHIVO", "movementList.identificadorArchivo", "_id",
				"archivoDTO");
		UnwindOperation unwindArchivo = Aggregation.unwind("archivoDTO");
		CustomAggregationOperation group2 = new CustomAggregationOperation(buildGroupString2());
		MatchOperation nomArchivo = Aggregation.match(Criteria.where("archivoDTO.nomArchivo").is(nombreArchivo));
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(Boolean.TRUE).build();
		return Aggregation.newAggregation(MovementsDupDTO.class, group, addFields, match, unwindMovements, lookup,
				unwindArchivo, group2, nomArchivo).withOptions(options);
	}

	private String buildGroupDupString() {
		return "{ $group: { _id: { 'cveDelegacionNss': '$aseguradoDTO.cveDelegacionNss',"
				+ "'cveSubdelNss': '$aseguradoDTO.cveSubdelNss', 'cveUmfAdscripcion': '$aseguradoDTO.cveUmfAdscripcion',"
				+ "'refRegistroPatronal': '$patronDTO.refRegistroPatronal', 'numNss': '$aseguradoDTO.numNss',"
				+ "'refCurp': '$aseguradoDTO.refCurp', 'nomAsegurado': '$aseguradoDTO.nomAsegurado',"
				+ "'refPrimerApellido': '$aseguradoDTO.refPrimerApellido', 'refSegundoApellido': '$aseguradoDTO.refSegundoApellido',"
				+ "'cveTipoRiesgo': '$incapacidadDTO.cveTipoRiesgo', 'cveConsecuencia': '$incapacidadDTO.cveConsecuencia',"
				+ "'numDiasSubsidiados': '$incapacidadDTO.numDiasSubsidiados', 'porPorcentajeIncapacidad': '$incapacidadDTO.porPorcentajeIncapacidad',"
				+ "'cveLaudo': '$incapacidadDTO.cveLaudo', 'cveOcupacion': '$aseguradoDTO.cveOcupacion',"
				+ "'numActoInseguro'   : '$incapacidadDTO.numActoInseguro', 'numRiesgoFisico': '$incapacidadDTO.numRiesgoFisico',"
				+ "'cveNaturaleza': '$incapacidadDTO.cveNaturaleza', 'numCausaExterna': '$incapacidadDTO.numCausaExterna',"
				+ "'numMatMedTratante': '$incapacidadDTO.numMatMedTratante', 'numCodigoDiagnostico': '$incapacidadDTO.numCodigoDiagnostico',"
				+ "'cveDelegacionAtencion': '$aseguradoDTO.cveDelegacionAtencion', 'cveSubDelAtencion': '$aseguradoDTO.cveSubDelAtencion',"
				+ "'fecInicio': '$incapacidadDTO.fecInicio', 'fecAccidente': '$incapacidadDTO.fecAccidente',"
				+ "'fecFin': '$incapacidadDTO.fecFin',"
				+ "'cveUmfExp': '$aseguradoDTO.cveUmfExp', 'cveOrigenArchivo': '$cveOrigenArchivo',"
				+ "'cveUmfPagadora': '$aseguradoDTO.cveUmfPagadora', 'fecAtencion':'$incapacidadDTO.fecAtencion',  'fecIniPension':'$incapacidadDTO.fecIniPension',"
				+"'fecAlta': '$incapacidadDTO.fecAltaIncapacidad', 'fecExpDictamen':'$incapacidadDTO.fecExpDictamen', 'numSalarioDiario':'$aseguradoDTO.numSalarioDiario',"
				+ "'numMatMedAutCdst': '$incapacidadDTO.numMatMedAutCdst', 'refFolioOriginal': '$aseguradoDTO.refFolioOriginal' },"
				+ "movementList: { $push: { _id: '$_id', identificadorArchivo: '$identificadorArchivo',"
				+ "cveOrigenArchivo: '$cveOrigenArchivo', aseguradoDTO: '$aseguradoDTO', patronDTO: '$patronDTO',"
				+ "incapacidadDTO: '$incapacidadDTO', _class: '$_class' } }" + "} }";
	}

	@Override
	public List<MovementsSusDTO> validateDuplicatedNss(String nombreArchivo) {
		TypedAggregation<MovementsSusDTO> aggregation = buildAggregation(nombreArchivo);
		AggregationResults<MovementsSusDTO> aggregationResults = mongoOperations.aggregate(aggregation,
				DetalleRegistroDTO.class, MovementsSusDTO.class);
		return aggregationResults.getMappedResults();
	}

	private TypedAggregation<MovementsSusDTO> buildAggregation(String nombreArchivo) {
		MatchOperation fecBaja = Aggregation.match(Criteria.where("aseguradoDTO.fecBaja").is(null));
		CustomAggregationOperation group = new CustomAggregationOperation(buildGroupString());
		CustomAggregationOperation addFields = new CustomAggregationOperation(buildAddFields());
		MatchOperation numberOfIds = Aggregation.match(Criteria.where("numberOfIds").gt(1));
		CustomAggregationOperation unwindOperation = new CustomAggregationOperation(buildUnwind());
		LookupOperation lookup = Aggregation.lookup("MCT_ARCHIVO", "movementList.identificadorArchivo", "_id",
				"archivoDTO");
		UnwindOperation unwindArchivo = Aggregation.unwind("archivoDTO");
		CustomAggregationOperation group2 = new CustomAggregationOperation(buildGroupString2());
		MatchOperation nomArchivo = Aggregation.match(Criteria.where("archivoDTO.nomArchivo").is(nombreArchivo));
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(Boolean.TRUE).build();
		return Aggregation.newAggregation(MovementsSusDTO.class, fecBaja, group, addFields, numberOfIds,
				unwindOperation, lookup, unwindArchivo, group2, nomArchivo).withOptions(options);
	}

	private String buildGroupString() {
		return "{ $group: { _id: '$aseguradoDTO.numNss'," + "    movementList: { " + "        $push: {"
				+ "            _id: '$_id'," + "            identificadorArchivo: '$identificadorArchivo',"
				+ "            cveOrigenArchivo: '$cveOrigenArchivo'," + "            aseguradoDTO: '$aseguradoDTO',"
				+ "            patronDTO: '$patronDTO'," + "            incapacidadDTO: '$incapacidadDTO',"
				+ "            _class: '$_class'" + "        }" + "    }" + "} }";
	}

	private String buildAddFields() {
		return "{ $addFields: { numberOfIds: { $size: '$movementList' } } }";
	}

	private String buildUnwind() {
		return "{ $unwind: { path: '$movementList' } }";
	}

	private String buildGroupString2() {
		return "{ $group: { _id: '$_id'" + ", movementList: { $push: '$movementList' },"
				+ "archivoDTO: { $push: '$archivoDTO' }, numberOfIds: { $first: '$numberOfIds' } } }";
	}

	@Override
	public List<DetalleRegistroDTO> existeSusceptibleNss(RegistroDTO registroDTO, String tipoArchivo) {
		LookupOperation lookup = Aggregation.lookup("MCT_ARCHIVO", "identificadorArchivo", "_id", "archivoDTO");
		String jsonOpperation = "{ $project: {"
				+ " '_id':1,  'identificadorArchivo':1,     'aseguradoDTO.refFolioOriginal': 1, 'aseguradoDTO.numCicloAnual': 1,'aseguradoDTO.cveEstadoRegistro': 1,"
				+ "        'incapacidadDTO.fecAtencion': 1," + "        'incapacidadDTO.cveConsecuencia': 1,"
				+ "        'incapacidadDTO.fecInicio': 1," + "        'incapacidadDTO.fecFin': 1,"
				+ "        'incapacidadDTO.desTipoRiesgo': 1," + "        'incapacidadDTO.numDiasSubsidiados': 1,"
				+ "        'incapacidadDTO.porPorcentajeIncapacidad': 1," + "        'patronDTO.refRegistroPatronal': 1"
				+ "    }}";
		CustomAggregationOperation projection = new CustomAggregationOperation(jsonOpperation);
		TypedAggregation<DetalleRegistroDTO> aggregation = Aggregation.newAggregation(DetalleRegistroDTO.class,
				Aggregation.match(Criteria.where(CamposAseguradoEnum.NSS.getNombreCampo()).is(registroDTO.getNumNss())
						.and(CamposAseguradoEnum.ESTADO_REGISTRO.getNombreCampo()).nin(3, 7)),
				lookup, projection);

		AggregationResults<DetalleRegistroDTO> aggregationResults = mongoOperations.aggregate(aggregation,
				DetalleRegistroDTO.class);

		return aggregationResults.getMappedResults();
	}

	@Override
	public void guardarDetalle(DetalleRegistroDTO archivoDTO) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(archivoDTO.getObjectId()));
		Update update = new Update();
		update.set("aseguradoDTO.cveEstadoRegistro", archivoDTO.getAseguradoDTO().getCveEstadoRegistro());
		update.set("aseguradoDTO.desEstadoRegistro", archivoDTO.getAseguradoDTO().getDesEstadoRegistro());
		update.set("aseguradoDTO.fecActualizacion", DateUtils.getSysDateMongoISO());
		update.set("aseguradoDTO.cveCodigoError", archivoDTO.getBitacoraErroresDTO() != null ? archivoDTO.getAseguradoDTO().getCveCodigoError() : "0");
		update.set("bitacoraErroresDTO", archivoDTO.getBitacoraErroresDTO());
		mongoOperations.updateFirst(query, update, DetalleRegistroDTO.class);
	}

	@Override
	public void actualizaDetalle(DetalleRegistroDTO archivoDTO) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(archivoDTO.getObjectId()));
		Update update = new Update();
		update.set("aseguradoDTO.cveEstadoRegistro", archivoDTO.getAseguradoDTO().getCveEstadoRegistro());
		update.set("aseguradoDTO.desEstadoRegistro", archivoDTO.getAseguradoDTO().getDesEstadoRegistro());
		update.set("aseguradoDTO.fecActualizacion", DateUtils.getSysDateMongoISO());
		update.set("auditorias", archivoDTO.getAuditorias());
		mongoOperations.updateFirst(query, update, DetalleRegistroDTO.class);
	}

	@Override
	public void actualizaDetalleDuplicado(DetalleRegistroDTO archivoDTO) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(archivoDTO.getObjectId()));
		Update update = new Update();
		update.set("aseguradoDTO.cveEstadoRegistro", archivoDTO.getAseguradoDTO().getCveEstadoRegistro());
		update.set("aseguradoDTO.desEstadoRegistro", archivoDTO.getAseguradoDTO().getDesEstadoRegistro());
		update.set("aseguradoDTO.fecActualizacion", DateUtils.getSysDateMongoISO());
		update.set("aseguradoDTO.fecBaja", DateUtils.getSysDateMongoISO());
		update.set("incapacidadDTO.fecBaja", DateUtils.getSysDateMongoISO());
		update.set("patronDTO.fecBaja", DateUtils.getSysDateMongoISO());
		mongoOperations.updateFirst(query, update, DetalleRegistroDTO.class);
	}

	@Override
	public Optional<DetalleRegistroDTO> findOneById(String id) {
		DetalleRegistroDTO d = this.mongoOperations.findOne(new Query(Criteria.where("_id").is(id)),
				DetalleRegistroDTO.class);

		Optional<DetalleRegistroDTO> user = Optional.ofNullable(d);

		return user;
	}

	private String buildGroupCount() {
		Map<String, String> estadoRegistro = new HashMap<>();
		estadoRegistro.put("numRegistrosCorrectos", "1");
		estadoRegistro.put("numRegistrosError", "2");
		estadoRegistro.put("numRegistrosDup", "3");
		estadoRegistro.put("numRegistrosSus", "4");
		estadoRegistro.put("numRegistrosCorrectosOtras", "5");
		estadoRegistro.put("numRegistrosErrorOtras", "6");
		estadoRegistro.put("numRegistrosDupOtras", "7");
		estadoRegistro.put("numRegistrosSusOtras", "8");
		estadoRegistro.put("numRegistrosBaja", "10");
		estadoRegistro.put("numRegistrosBajaOtras", "11");
		String group = "{ $group : { _id : null,";
		for (String estadoReg : estadoRegistro.keySet()) {
			group = group.concat(estadoReg)
					.concat(": { $sum  : { $cond : [{ $eq : ['$aseguradoDTO.cveEstadoRegistro', ")
					.concat(estadoRegistro.get(estadoReg)).concat("] }, { $sum  : 1 }, { $sum  : 0 }] } },");
		}
		group = group.concat(
				"'numPasoAl' : { $sum  : { $cond : [{ $gt: ['$aseguradoPasoAl.numNss', ''] }, { $sum  : 1 }, { $sum  : 0 }] } } } }");
		return group;
	}

	@Override
	public CifrasControlDTO obtenerCifrasControl(String nombreArchivo) {
		LookupOperation lookup = Aggregation.lookup("MCT_ARCHIVO", "identificadorArchivo", "_id", "archivoDTO");
		UnwindOperation unwindArchivo = Aggregation.unwind("archivoDTO");
		MatchOperation nomArchivo = Aggregation.match(Criteria.where("archivoDTO.nomArchivo").is(nombreArchivo));
		CustomAggregationOperation group = new CustomAggregationOperation(buildGroupCount());
		Aggregation aggregate = Aggregation.newAggregation(CifrasControlDTO.class, lookup, unwindArchivo, nomArchivo,
				group);
		AggregationResults<CifrasControlDTO> conteoCifras = mongoOperations.aggregate(aggregate,
				DetalleRegistroDTO.class, CifrasControlDTO.class);
		return conteoCifras.getUniqueMappedResult();
	}

}