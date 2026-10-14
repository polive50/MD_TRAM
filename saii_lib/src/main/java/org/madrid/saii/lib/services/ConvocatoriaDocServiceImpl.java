package org.madrid.saii.lib.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaDoc
 *
 * @author everis
 */
@Service("convocatoriaDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaDocServiceImpl
        extends BaseServiceImpl<ConvocatoriaDoc, Integer, ConvocatoriaDocDAO>
        implements ConvocatoriaDocService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDocDAO
     */
    @Autowired
    public ConvocatoriaDocServiceImpl(@Qualifier("convocatoriaDocDAO")
    ConvocatoriaDocDAO convocatoriaDocDAO) {
        super();
        this.setDao(convocatoriaDocDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaDocServiceImpl() {
        super();
    }

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /** Repositorio ConvocatoriaService */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /** DAO **/
    /** Repositorio ConvocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /** Repositorio ExpedienteDAO */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaDocToDTO.INSTANCE));

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDocDTO guardar(ConvocatoriaDocDTO convocatoriaDocDTO)
            throws ServiceException {

        if (Objects.isNull(convocatoriaDocDTO.getIdConvocatoria())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Boolean permiteGrabar = Boolean.TRUE;

        Convocatoria convocatoria = this.convocatoriaDAO
                .find(convocatoriaDocDTO.getIdConvocatoria());

        // TODO: ESTAS VALIDACIONES HAY QUE VALIDARLAS PORQUE NO SABEMOS SI SON
        // CORRECTAS AL 100%
        // y encima se han metido nuevas fechas de tramites de propResolProv y
        // ResolProv...
        // valida el documento de acta de comisión evaluacion
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_COMISION_EVALUACION)
                .equals(convocatoriaDocDTO.getIdTipoDocumento().getIdValor())) {
            this.validarTipoDocumentoActaComisionEvaluacion(convocatoria);

            // valida el documento de propuesta de resolucion definitiva
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA)
                .equals(convocatoriaDocDTO.getIdTipoDocumento().getIdValor())) {
            this.validarTipoDocumentoPropuestaResolucionDefinitiva(convocatoria);

            // valida el documento de propuesta de resolucion provisional
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL)
                .equals(convocatoriaDocDTO.getIdTipoDocumento().getIdValor())) {
            permiteGrabar = this.validarTipoDocumentoPropuestaResolucionProvisional(convocatoria);
        }
        // VMM 18/03/25 MD-1338: añadimos metodo de validacion tipo doc de
        // resoluc provisional
        // valida el documento de resolucion provisional
        else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PROVISIONAL)
                .equals(convocatoriaDocDTO.getIdTipoDocumento().getIdValor())) {
            this.validarTipoDocumentoResolucionProvisional(convocatoria);
        }
        // valida el documento de resolución definitiva
        else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_DEFINITIVA)
                .equals(convocatoriaDocDTO.getIdTipoDocumento().getIdValor())) {
            this.validarTipoDocumentoResolucionConcesion(convocatoria);
        }

        if (Boolean.TRUE.equals(permiteGrabar)) {
            ConvocatoriaDoc convocatoriaDoc = this
                    .insertOrUpdate(ConvocatoriaDocToEntity.INSTANCE.apply(convocatoriaDocDTO));
            convocatoriaDocDTO = ConvocatoriaDocToDTO.INSTANCE.apply(convocatoriaDoc);
        }

        return convocatoriaDocDTO;

    }

    /**
     * Metodo para validar el tipo de documento de acta de comision de
     * evaluacion
     *
     * @param convocatoria
     * @throws ServiceException
     */
    private void validarTipoDocumentoActaComisionEvaluacion(Convocatoria convocatoria)
            throws ServiceException {
        // si es la primera vez que se sube este doc y no tiene su fecha de
        // tramite informada le seteamos el dia actual
        if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_COMISION_EVALUACION)))
                && Objects.isNull(convocatoria.getFcTraComisionEval())) {

            convocatoria.setFcTraComisionEval(new Date());
            this.convocatoriaDAO.update(convocatoria);
        }

        // aqui no lanza excepcion si ya existe este doc en la convocatoria con
        // su fecha de tramite correspondiente
        // y lo queremos volver a subir porque parece ser que se puede subir
        // varias veces, no solo una
    }

    /**
     * Metodo para validar el tipo de documento propuesta de resolucion
     * definitiva
     *
     * @param convocatoria
     * @throws ServiceException
     */
    private void validarTipoDocumentoPropuestaResolucionDefinitiva(Convocatoria convocatoria)
            throws ServiceException {

        if (Constantes.AfirmacionNegacion.S.equals(convocatoria.getItAlegacion())) {

            // valida que la fecha de fin de alegación sea menor al dia actual
            LocalDateTime fechaHoy = LocalDateTime.now();
            LocalDateTime fechaAlegacionesFin = Utils
                    .convertDateToLocalDateTime(convocatoria.getFcFinAlegaciones());

            if (Objects.isNull(fechaAlegacionesFin)) {
                throw new ServiceException(
                        Constantes.Error.ERROR_FECHA_FIN_ALEGACIONES_NULL_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA,
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            if (fechaHoy.isAfter(fechaAlegacionesFin)) {
                throw new ServiceException(
                        Constantes.Error.ERROR_FECHA_FIN_ALEGACIONES_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA
                                + fechaAlegacionesFin
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            // valida si existe algún expediente en estado “Propuesta de
            // resolución”. Si se cumple, lanzaremos una ServiceException
            List<Expediente> lstExpediente = this.expedienteDAO
                    .listaExpedientePorConvocatoria(convocatoria.getIdConvocatoria());
            
            Integer idPropRes = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION);
            
            if (lstExpediente.stream()
                    .filter(filtro -> filtro.getEstado().getIdValor()
                            .equals(idPropRes))
                    .count() > 0) {
                throw new ServiceException(
                        Constantes.Error.ERROR_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA_ESTADO_PROPUESTA_RESOLUCION,
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

            }
            
            Integer idTipDocProResDef = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
            
            // si es la primera vez que se sube este doc y no tiene su fecha de
            // tramite informada le seteamos el dia actual
            if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(), idTipDocProResDef)
                    && Objects.isNull(convocatoria.getFcTraPropResDef()))) {
                convocatoria.setFcTraPropResDef(new Date());
                this.convocatoriaDAO.update(convocatoria);
            }

            // si esta subiendo de nuevo este doc que ya existe y ademas tiene
            // informada su fecha de tramite, lanzamos error
            if (Boolean.TRUE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(), idTipDocProResDef)
                    && Objects.nonNull(convocatoria.getFcTraPropResDef()))) {
                throw new ServiceException(
                        Constantes.Error.ERROR_SUBIR_DOCUMENTO_PROPUESTA_RESOLUCION_DEFINITIVA,
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

        }

    }

    /**
     * Metodo para validar el tipo de documento propuesta de resolucion
     * provisional
     *
     * @param convocatoria
     * @throws ServiceException
     */
    private void validarTipoDocumentoResolucionProvisional(Convocatoria convocatoria)
            throws ServiceException {

        // VMM 18/03/25 MD-1338: añadimos metodo de validacion tipo doc de
        // resoluc provisional (REVISAR ESTA FUNCIONALIDAD)
        if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PROVISIONAL))
                && Objects.isNull(convocatoria.getFcTraResolucionProv()))) {
            convocatoria.setFcTraResolucionProv(new Date());
            this.convocatoriaDAO.update(convocatoria);
        }

        if (Boolean.TRUE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PROVISIONAL))
                && Objects.nonNull(convocatoria.getFcTraResolucionProv()))) {
            throw new ServiceException(
                    Constantes.Error.ERROR_SUBIR_DOCUMENTO_PROPUESTA_RESOLUCION_PROVISIONAL, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        // valida que no exista el tipo de documento propuesta de resolución
        // provisional y lanza un error,
        if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL)))) {

            throw new ServiceException(
                    Constantes.Error.ERROR_SUBIR_DOCUMENTO_RESOLUCION_PROVISIONAL, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        // valida si existe el tipo de documento resolución provisional y lanza
        // un error (aqui no tiene en cuenta si la fecha de este tramite esta
        // informada o nula)
        if (Boolean.TRUE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PROVISIONAL)))) {
            throw new ServiceException(
                    Constantes.Error.ERROR_SUBIR_DOCUMENTO_RESOLUCION_PROVISIONAL_EXISTE, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        // si existe el tipo de documento propuesta de resolución provisional y
        // el documento de concesión se sube por
        // primera vez, entonces realiza la acción siguiente
        if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PROVISIONAL))
                && Objects.isNull(convocatoria.getFcTraResolucionProv()))) {
            convocatoria.setFcTraResolucionProv(new Date());
            this.convocatoriaDAO.update(convocatoria);
        }

    }

    /**
     * Metodo para validar el tipo de documento resolucion provisional
     *
     * @param convocatoria
     * @return
     * @throws ServiceException
     */
    private Boolean validarTipoDocumentoPropuestaResolucionProvisional(Convocatoria convocatoria)
            throws ServiceException {
        // TODO: aqui deberia comprobar como en la validacion de Prop Resol Def
        // si hay alegaciones etc???

        // si es una convocatoria de Evaluaciones y no tiene el doc de acta de
        // comision de evaluacion subido, error
        if (convocatoria.getItEvaluacion() != null
                && convocatoria.getItEvaluacion().equals(Constantes.AfirmacionNegacion.S)
                && Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_COMISION_EVALUACION)))) {

            throw new ServiceException(Constantes.Error.ERROR_SUBIR_DOCUMENTO_COMISION_EVALUACION,
                    null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        Boolean permiteGrabar = this.convocatoriaService
                .esPuedeSubirPropuestaResProvisional(convocatoria.getIdConvocatoria());

        // si no estan todos en estados tramitados, lanzamos error
        if (Boolean.FALSE.equals(permiteGrabar)) {
            throw new ServiceException(
                    Constantes.Error.ERROR_SUBIR_DOCUMENTO_EXPEDIENTES_TRAMITES_ACTIVOS, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        
        Integer idTipDocProResProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);
        
        // VMM 18/03/25 MD-1338: añadimos esta comprobacion para propuesta de
        // resoluc provisional
        // si existe el tipo de documento propuesta de resolución provisional y
        // el documento de concesión se sube por
        // primera vez, entonces realiza la acción siguiente
        if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(), idTipDocProResProv)
                && Objects.isNull(convocatoria.getFcTraPropResProv()))) {
            convocatoria.setFcTraPropResProv(new Date());
            this.convocatoriaDAO.update(convocatoria);
        }
        // TODO: (VER SI ES CORRECTO) no sabemos cuantas veces se puede subir
        // este documento
        if (Boolean.TRUE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(), idTipDocProResProv)
                && Objects.nonNull(convocatoria.getFcTraPropResProv()))) {
            throw new ServiceException(
                    Constantes.Error.ERROR_SUBIR_DOCUMENTO_PROPUESTA_RESOLUCION_PROVISIONAL, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        return permiteGrabar;
    }

    /**
     * Metodo para validar el tipo de documento de acta de comision de
     * resolucion
     *
     *
     * @param convocatoria
     * @throws ServiceException
     */
    private void validarTipoDocumentoResolucionConcesion(Convocatoria convocatoria)
            throws ServiceException {
        // valida que no exista el tipo de documento propuesta de resolución
        // definitiva y lanza un error,
        if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA)))) {

            throw new ServiceException(Constantes.Error.ERROR_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA,
                    null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        // valida si existe el tipo de documento resolución definitiva y lanza
        // un error (aqui no tiene en cuenta si la fecha de este tramite esta
        // informada o nula)
        if (Boolean.TRUE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_DEFINITIVA)))) {
            throw new ServiceException(
                    Constantes.Error.ERROR_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA_EXISTE, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        // si existe el tipo de documento propuesta de resolución definitiva y
        // el documento de concesión se sube por
        // primera vez, entonces realiza la acción siguiente
        if (Boolean.FALSE.equals(this.existeTipoDocumento(convocatoria.getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_DEFINITIVA))
                && Objects.isNull(convocatoria.getFcTraResolucionDef()))) {
            convocatoria.setFcTraResolucionDef(new Date());
            this.convocatoriaDAO.update(convocatoria);
        }
    }

    /**
     *
     */
    @Override
    public ConvocatoriaDocDTO buscarId(final Integer idDocumentacion) throws ServiceException {

        // Validacion de entrada
        if (idDocumentacion == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return ConvocatoriaDocToDTO.INSTANCE.apply(this.find(idDocumentacion));

    }

    /**
     *
     */
    @Override
    public ConvocatoriaDocDTO descargar(Integer cdFica) throws ServiceException {

        if (Objects.isNull(cdFica)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ConvocatoriaDocDTO convocatoriaDocDTO = new ConvocatoriaDocDTO();

        FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
        convocatoriaDocDTO.setFicaFichero(ficaFichero);
        convocatoriaDocDTO.setContenidoFichero(ficaFichero.getDatosFichero());
        convocatoriaDocDTO.setCdFica(cdFica);
        return convocatoriaDocDTO;
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocDTO> obtenerDocumentosConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        List<ConvocatoriaDocDTO> lstConvocatoriaDocDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        if (idConvocatoria != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        }

        lstConvocatoriaDocDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaDocToDTO.INSTANCE));

        return lstConvocatoriaDocDTO;
    }

    /**
     *
     */
    @Override
    public ConvocatoriaDocDTO subirDocumentoFica(ConvocatoriaDocDTO convocatoriaDocDTO)
            throws ServiceException {

        FicaFichero ficheroRecuperar = null;
        FicaFichero fichero;

        // Validacion
        if (convocatoriaDocDTO.getContenidoFichero() == null
                || convocatoriaDocDTO.getFicaMetadatos() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        if (StringUtils.isBlank(convocatoriaDocDTO.getFicaMetadatos().getExtension())) {
            log.info("El Fichero no tiene extensión.");
            throw new ServiceException("El Fichero no tiene extensión.");
        }

        convocatoriaDocDTO.getFicaMetadatos()
                .setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        if (convocatoriaDocDTO.getCdFica() != null) {
            ficheroRecuperar = this.ficaService.recuperar(convocatoriaDocDTO.getCdFica());
        }
        if (ficheroRecuperar == null) {
            fichero = this.ficaService.guardar(convocatoriaDocDTO.getContenidoFichero(),
                    convocatoriaDocDTO.getFicaMetadatos());
        } else {
            fichero = this.ficaService.actualizar(convocatoriaDocDTO.getCdFica(),
                    convocatoriaDocDTO.getContenidoFichero(),
                    convocatoriaDocDTO.getFicaMetadatos());
        }
        convocatoriaDocDTO.setFicaFichero(fichero);
        convocatoriaDocDTO.setCdFica(fichero.getIdInterno());
        convocatoriaDocDTO.setDsNombreFichero(fichero.getNombreFichero());
        return convocatoriaDocDTO;
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaDocToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public Boolean existeTipoDocumento(Integer idConvocatoria, Integer idTipoDocumento)
            throws ServiceException {

        if (Objects.isNull(idConvocatoria) || Objects.isNull(idTipoDocumento)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        Integer total = this.getDao().count(filtros);

        return total > 0;
    }

}
