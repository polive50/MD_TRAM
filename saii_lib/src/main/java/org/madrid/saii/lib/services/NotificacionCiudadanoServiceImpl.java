package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.NotificacionCiudadanoDAO;
import org.madrid.saii.lib.dao.NotificacionCiudadanoDocDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToDTO;
import org.madrid.saii.lib.dto.converters.NotificacionCiudadanoToDTO;
import org.madrid.saii.lib.dto.converters.NotificacionCiudadanoToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio NotificacionCiudadano
 *
 * @author everis
 */
@Service("notificacionCiudadanoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class NotificacionCiudadanoServiceImpl extends
        BaseServiceImpl<NotificacionCiudadano, Integer, NotificacionCiudadanoDAO>
        implements NotificacionCiudadanoService {


    
    /** Estados nmConstante que impiden el alta de desistimiento. */
    private static final List<Integer> ESTADOS_SIN_DESISTIMIENTO = Collections.unmodifiableList(Arrays.asList(
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO,
            Constantes.EstadoExpediente.DENEGADO_DEFINITIVO,
            Constantes.EstadoExpediente.EXCLUIDO,
            Constantes.EstadoExpediente.DESISTIDO,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST,
            Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL,
            Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL,
            Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO,
            Constantes.EstadoExpediente.INICIADA,
            Constantes.EstadoExpediente.PROPUESTA,
            Constantes.EstadoExpediente.ACEPTADA,
            Constantes.EstadoExpediente.NO_ACEPTADA,
            Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO,
            Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO));

    /** Estados nmConstante que impiden el alta de renuncia. */
    private static final List<Integer> ESTADOS_SIN_RENUNCIA = Collections.unmodifiableList(Arrays.asList(
            Constantes.EstadoExpediente.INICIADA,
            Constantes.EstadoExpediente.PROPUESTA,
            Constantes.EstadoExpediente.ACEPTADA,
            Constantes.EstadoExpediente.NO_ACEPTADA,
            Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL,
            Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL,
            Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL,
            Constantes.EstadoExpediente.APROBADO_PROVISIONAL,
            Constantes.EstadoExpediente.DENEGADO_PROVISIONAL,
            Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL,
            Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA,
            Constantes.EstadoExpediente.EN_TRAMITACION,
            Constantes.EstadoExpediente.PENDIENTE_REVISION,
            Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO,
            Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS,
            Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS,
            Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES,
            Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION,
            Constantes.EstadoExpediente.EXCLUIDO,
            Constantes.EstadoExpediente.DESISTIDO,
            Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL,
            Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL,
            Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO,
            Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO));
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param notificacionCiudadanoDAO
     */
    @Autowired
    public NotificacionCiudadanoServiceImpl(
            @Qualifier("notificacionCiudadanoDAO") NotificacionCiudadanoDAO notificacionCiudadanoDAO) {
        super();
        this.setDao(notificacionCiudadanoDAO);
    }

    @Autowired
    @Qualifier("notificacionCiudadanoDocDAO")
    private NotificacionCiudadanoDocDAO notificacionCiudadanoDocDAO;
    
    @Autowired
    private CatalogosService catalogosService;

    @Autowired
    private CatalogosValoresService catalogosValoresService;

    // Se difiere la inyección porque el servicio de documentos utiliza este servicio.
    @Autowired
    @Lazy
    private ExpedienteDocService expedienteDocService;

    @Autowired
    @Lazy
    private ExpedienteService expedienteService;

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDTO buscarId(Integer id) throws ServiceException {
        return NotificacionCiudadanoToDTO.INSTANCE.apply(this.find(id));
    }

    /**
     *
     */
    @Override
    public List<NotificacionCiudadanoDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), NotificacionCiudadanoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public NotificacionCiudadanoDTO guardar(NotificacionCiudadanoDTO notificacionCiudadanoDTO)
            throws ServiceException {
        return NotificacionCiudadanoToDTO.INSTANCE.apply(this.insertOrUpdate(
                NotificacionCiudadanoToEntity.INSTANCE.apply(notificacionCiudadanoDTO)));
    }

    /**
     *
     */
    @Override
    public NotificacionCiudadano buscarEnCursoEntidad(Integer idExpediente, Integer idAsunto)
            throws ServiceException {

        if (Objects.isNull(idExpediente) || Objects.isNull(idAsunto)) {
            return null;
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_ID_ASUNTO,
                idAsunto, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDEREG, Operador.NULL));

        List<NotificacionCiudadano> lstNotifiEnCurso = this.find(filtros, null);

        return Objects.nonNull(lstNotifiEnCurso) && !lstNotifiEnCurso.isEmpty()
                ? lstNotifiEnCurso.get(0)
                : null;
    }

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDTO buscarEnCurso(Integer idExpediente, Integer idAsunto)
            throws ServiceException {

        NotificacionCiudadano notificacionCiudadano = this.buscarEnCursoEntidad(idExpediente,
                idAsunto);

        NotificacionCiudadanoDTO dto = new NotificacionCiudadanoDTO();
        if (Objects.isNull(notificacionCiudadano)) {
            return dto;
        }

        dto = NotificacionCiudadanoToDTO.INSTANCE.apply(notificacionCiudadano);

        List<AtlasFilter> filtrosDoc = new ArrayList<>();
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_DOC_ID_NOTIFICACION,
                notificacionCiudadano.getIdNotificacion(), Operador.EQUALS));
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));

        List<NotificacionCiudadanoDoc> lstDocEnlazados = this.notificacionCiudadanoDocDAO
                .find(filtrosDoc, null);

        dto.setLstExpedienteDocDTO(lstDocEnlazados.stream()
                .map(NotificacionCiudadanoDoc::getExpedienteDoc)
                .map(ExpedienteDocToDTO.INSTANCE)
                .collect(Collectors.toList()));

        return dto;
    }
    
    

    @Override
    public List<CatalogosValoresDTO> obtenerTiposComunicacion() throws ServiceException {
        List<CatalogosDTO> catalogosTipos = this.catalogosService.buscarPorNombre(
                "Tipos de notificaciones del ciudadano");
        if (catalogosTipos.size() != 1 || catalogosTipos.get(0).getFcBajaFila() != null) {
            throw new ServiceException("No se ha encontrado el catálogo Tipos de notificaciones del ciudadano.");
        }
        List<CatalogosValoresDTO> tiposComunicacion = catalogosTipos.get(0).getListCatalogosValores();
        return tiposComunicacion;
    }

    @Override
    @Transactional(readOnly = false)
    public NotificacionCiudadanoDTO registrarComunicacion(NotificacionCiudadanoDTO comunicacion)
            throws ServiceException {
        this.validarComunicacion(comunicacion);
        CatalogosValoresDTO estadoResuelta = this.catalogosValoresService.buscarNmConstante(
                Constantes.EstadoNotifiCiud.RESUELTA);
        CatalogosValoresDTO tramiteCiudadano = this.catalogosValoresService.buscarNmConstante(
                Constantes.Tramite.NOTIFICACION_CIUDADANO);
        CatalogosValoresDTO estadoDocumento = this.catalogosValoresService.buscarNmConstante(
                Constantes.EstadoDocumento.REVISION_OK);
        if (estadoResuelta.getIdValor() == null || tramiteCiudadano.getIdValor() == null
                || estadoDocumento.getIdValor() == null) {
            throw new ServiceException("No se han encontrado los estados o el trámite de la comunicación.");
        }
     // Se guarda una copia para mantener el formulario intacto si se revierte la transacción.
        NotificacionCiudadano entidadComunicacion = NotificacionCiudadanoToEntity.INSTANCE.apply(comunicacion);
        entidadComunicacion.setEstado(CatalogosValoresToEntity.INSTANCE.apply(estadoResuelta));
        entidadComunicacion.setCdEreg(StringUtils.isBlank(comunicacion.getCdEreg()) ? null
                : Utils.desformatearNmEreg(comunicacion.getCdEreg()));
        entidadComunicacion = this.insertOrUpdate(entidadComunicacion);
        List<ExpedienteDocDTO> documentosAdjuntos = comunicacion.getLstExpedienteDocDTO();
        for (ExpedienteDocDTO documentoAdjunto : documentosAdjuntos) {
            ExpedienteDocDTO documentoGuardar = new ExpedienteDocDTO();
            documentoGuardar.setExpedienteDTO(comunicacion.getExpedienteDTO());
            documentoGuardar.setTramiteDTO(tramiteCiudadano);
            documentoGuardar.setEstadoDTO(estadoDocumento);
            documentoGuardar.setTipoDocumentoDTO(documentoAdjunto.getTipoDocumentoDTO());
            documentoGuardar.setTlObservaciones(documentoAdjunto.getTlObservaciones());
            documentoGuardar.setTlObservacionesGestor(documentoAdjunto.getTlObservacionesGestor());
            documentoGuardar.setFcRegistro(documentoAdjunto.getFcRegistro());
            if (StringUtils.isNotBlank(documentoAdjunto.getCodigoEreg())) {
                documentoGuardar.setCodigoEreg(Utils.desformatearNmEreg(documentoAdjunto.getCodigoEreg()));
            }
            documentoGuardar.setItAdicional(Constantes.AfirmacionNegacion.S);
            documentoGuardar.setContenidoFichero(documentoAdjunto.getContenidoFichero());
            documentoGuardar.setFicaMetadatos(documentoAdjunto.getFicaMetadatos());
            documentoGuardar = this.expedienteDocService.subirDocumentoFica(documentoGuardar);
            documentoGuardar = this.expedienteDocService.guardarConHistorico(documentoGuardar);
            NotificacionCiudadanoDoc vinculo = new NotificacionCiudadanoDoc();
            vinculo.setNotificacionCiudadano(entidadComunicacion);
            ExpedienteDoc entidadDocumento = new ExpedienteDoc();
            entidadDocumento.setIdExpedienteDoc(documentoGuardar.getIdExpedienteDoc());
            vinculo.setExpedienteDoc(entidadDocumento);
            this.notificacionCiudadanoDocDAO.insertOrUpdate(vinculo);
        }
        return NotificacionCiudadanoToDTO.INSTANCE.apply(entidadComunicacion);
    }

    private void validarComunicacion(NotificacionCiudadanoDTO comunicacion) throws ServiceException {
        if (comunicacion == null || comunicacion.getIdNotificacion() != null
                || comunicacion.getExpedienteDTO() == null
                || comunicacion.getExpedienteDTO().getIdExpediente() == null) {
            throw new ServiceException("No se ha seleccionado un expediente para la nueva comunicación.");
        }
        if (StringUtils.isBlank(comunicacion.getTlExpone()) || comunicacion.getTlExpone().length() > 500
                || StringUtils.isBlank(comunicacion.getTlSolicita()) || comunicacion.getTlSolicita().length() > 500) {
            throw new ServiceException("Expone y Solicita son obligatorios y admiten un máximo de 500 caracteres.");
        }
        this.validarCodigoEreg(comunicacion.getCdEreg());
        List<CatalogosValoresDTO> tiposComunicacion = this.obtenerTiposComunicacionPermitidos(
                comunicacion.getExpedienteDTO().getIdExpediente());
        Integer idTipoComunicacion = comunicacion.getAsuntoDTO() == null ? null
                : comunicacion.getAsuntoDTO().getIdValor();
        boolean tipoValido = false;
        for (CatalogosValoresDTO tipoComunicacion : tiposComunicacion) {
            if (tipoComunicacion.getIdValor().equals(idTipoComunicacion)
                    && tipoComunicacion.getFcBajaFila() == null) {
                tipoValido = true;
            }
        }
        if (!tipoValido) {
            throw new ServiceException("comunicacionCiudadano.error.tipoNoPermitido");
        }
        List<ExpedienteDocDTO> documentosAdjuntos = comunicacion.getLstExpedienteDocDTO();
        if (documentosAdjuntos == null) {
            throw new ServiceException("No se ha inicializado la lista de documentos adjuntos.");
        }
        for (ExpedienteDocDTO documento : documentosAdjuntos) {
            if (documento == null || documento.getIdExpedienteDoc() != null
                    || documento.getContenidoFichero() == null || documento.getContenidoFichero().length == 0
                    || documento.getFicaMetadatos() == null
                    || StringUtils.isBlank(documento.getFicaMetadatos().getExtension())) {
                throw new ServiceException("Seleccione un archivo con extensión para cada documento adjunto.");
            }
            if (StringUtils.length(documento.getTlObservaciones()) > 2000
                    || StringUtils.length(documento.getTlObservacionesGestor()) > 2000) {
                throw new ServiceException("Las observaciones de los documentos admiten un máximo de 2000 caracteres.");
            }
            this.validarCodigoEreg(documento.getCodigoEreg());
        }
    }

    private void validarCodigoEreg(String codigoEreg) throws ServiceException {
        if (StringUtils.isNotBlank(codigoEreg) && !codigoEreg.matches("^[0-9]{2}/[0-9]{6}\\.[0-9]/[0-9]{2}$")) {
            throw new ServiceException("El código EREG debe tener el formato XX/XXXXXX.X/XX.");
        }
    }

    @Override
    @Transactional(readOnly = false)
    public CatalogosValoresDTO actualizarRevision(Integer idNotificacion, Integer idExpediente, boolean revisada)
            throws ServiceException {
        NotificacionCiudadano comunicacion = idNotificacion == null ? null : this.find(idNotificacion);
        if (comunicacion == null || comunicacion.getFcBajaFila() != null || idExpediente == null
                || !idExpediente.equals(comunicacion.getExpediente().getIdExpediente())) {
            throw new ServiceException("La comunicación no pertenece al expediente seleccionado.");
        }
        CatalogosValoresDTO estadoRevision = this.catalogosValoresService.buscarNmConstante(revisada
                ? Constantes.EstadoNotifiCiud.RESUELTA : Constantes.EstadoNotifiCiud.PENDIENTE_REVISION);
        if (estadoRevision.getIdValor() == null) {
            throw new ServiceException("No se ha encontrado el estado de revisión de la comunicación.");
        }
        CatalogosValores estado = CatalogosValoresToEntity.INSTANCE.apply(estadoRevision);
        comunicacion.setEstado(estado);
        this.insertOrUpdate(comunicacion);
        return estadoRevision;
    }
    @Override
    public List<ExpedienteDocDTO> buscarDocumentosComunicacion(Integer idNotificacion,
            Integer idExpediente) throws ServiceException {
        if (idNotificacion == null || idExpediente == null) {
            throw new ServiceException("No se ha seleccionado una comunicación del expediente.");
        }
        List<AtlasFilter> filtrosDocumentos = new ArrayList<>();
        filtrosDocumentos.add(new AtlasFilter(
                Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_DOC_ID_NOTIFICACION,
                idNotificacion, Operador.EQUALS));
        filtrosDocumentos.add(new AtlasFilter("notificacionCiudadano.expediente.idExpediente",
                idExpediente, Operador.EQUALS));
        filtrosDocumentos.add(new AtlasFilter("notificacionCiudadano.fcBajaFila", Operador.NULL));
        filtrosDocumentos.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosDocumentos.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA,
                Operador.NULL));
        List<NotificacionCiudadanoDoc> documentosComunicacion =
                this.notificacionCiudadanoDocDAO.find(filtrosDocumentos, null);
        List<ExpedienteDocDTO> documentosAdjuntos = new ArrayList<>();
        Map<Integer, String> nombresFicheros = new HashMap<>();
        for (NotificacionCiudadanoDoc vinculo : documentosComunicacion) {
            ExpedienteDocDTO documento = ExpedienteDocToDTO.INSTANCE.apply(vinculo.getExpedienteDoc());
            Integer codigoFica = documento.getCdFica();
            if (codigoFica != null) {
                if (Boolean.FALSE.equals(nombresFicheros.containsKey(codigoFica))) {
                    String nombreFichero = this.expedienteService.recuperarNombreFicheroFica(codigoFica);
                    nombresFicheros.put(codigoFica, nombreFichero);
                }
                documento.setNombreFicheroFica(nombresFicheros.get(codigoFica));
            }
            documentosAdjuntos.add(documento);
        }
        return documentosAdjuntos;
    }

    @Override
    public List<NotificacionCiudadanoDTO> buscarComunicaciones(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException {
        List<NotificacionCiudadano> notificacionesPagina = this.find(index, pageSize, filtros, orden);
        List<NotificacionCiudadanoDTO> comunicacionesPagina = new ArrayList<>();
        for (NotificacionCiudadano notificacion : notificacionesPagina) {
            NotificacionCiudadanoDTO comunicacion = NotificacionCiudadanoToDTO.INSTANCE.apply(notificacion);
            comunicacion.setRevisada(Constantes.EstadoNotifiCiud.RESUELTA.equals(
                    comunicacion.getEstadoDTO().getNmConstante()));
            comunicacionesPagina.add(comunicacion);
        }
        return comunicacionesPagina;
    }

    /**
     *
     */
    @Override
    public List<NotificacionCiudadanoDTO> buscarPresentadasPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDEREG, Operador.NOT_NULL));

        List<NotificacionCiudadano> lstNotificacionesPresentadas = this.find(filtros, null);

        List<NotificacionCiudadanoDTO> lstDto = new ArrayList<>();
        for (NotificacionCiudadano notificacionCiudadano : lstNotificacionesPresentadas) {
            NotificacionCiudadanoDTO dto = NotificacionCiudadanoToDTO.INSTANCE
                    .apply(notificacionCiudadano);
            dto.setCdEreg(Utils.formatearNmEreg(dto.getCdEreg()));

            List<AtlasFilter> filtrosDoc = new ArrayList<>();
            filtrosDoc.add(new AtlasFilter(
                    Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_DOC_ID_NOTIFICACION,
                    notificacionCiudadano.getIdNotificacion(), Operador.EQUALS));
            filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtrosDoc.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));

            List<NotificacionCiudadanoDoc> lstDocEnlazados = this.notificacionCiudadanoDocDAO
                    .find(filtrosDoc, null);

            List<ExpedienteDocDTO> lstDocumentosDto = new ArrayList<>();
            for (NotificacionCiudadanoDoc vinculo : lstDocEnlazados) {
                ExpedienteDocDTO documento = ExpedienteDocToDTO.INSTANCE.apply(vinculo.getExpedienteDoc());
                if (documento.getCdFica() != null) {
                    documento.setNombreFicheroFica(
                            this.expedienteService.recuperarNombreFicheroFica(documento.getCdFica()));
                }
                lstDocumentosDto.add(documento);
            }
            dto.setLstExpedienteDocDTO(lstDocumentosDto);

            lstDto.add(dto);
        }

        return lstDto;
    }
    
    /** Consulta el estado vigente y filtra únicamente los tipos disponibles para el alta. */
    @Override
    public List<CatalogosValoresDTO> obtenerTiposComunicacionPermitidos(Integer idExpediente)
            throws ServiceException {
        ExpedienteDTO expediente = idExpediente == null ? null
                : this.expedienteService.buscarIdSimple(idExpediente, Boolean.FALSE);
        if (expediente == null || expediente.getIdExpediente() == null
                || expediente.getEstadoDTO() == null
                || expediente.getEstadoDTO().getNmConstante() == null) {
            throw new ServiceException("comunicacionCiudadano.error.estadoNoDisponible");
        }
        Integer estado = expediente.getEstadoDTO().getNmConstante();
        List<CatalogosValoresDTO> tiposCatalogo = this.obtenerTiposComunicacion();
        List<CatalogosValoresDTO> tiposPermitidos = new ArrayList<>();
        for (CatalogosValoresDTO tipo : tiposCatalogo) {
            if (tipo.getFcBajaFila() == null && this.esTipoComunicacionPermitido(tipo.getNmConstante(), estado)) {
                tiposPermitidos.add(tipo);
            }
        }
        return tiposPermitidos;
    }

    /** La regla usa nmConstante; idValor se utiliza solo para identificar la selección. */
    private boolean esTipoComunicacionPermitido(Integer tipo, Integer estado) {
        return !(Constantes.TiposNotifCiudadano.DESISTIMIENTO_EXPRESO.equals(tipo)
                && ESTADOS_SIN_DESISTIMIENTO.contains(estado))
                && !(Constantes.TiposNotifCiudadano.RENUNCIA_EXPRESA.equals(tipo)
                && ESTADOS_SIN_RENUNCIA.contains(estado));
    }

    /**
     * 
     */
    @Override
    public NotificacionCiudadanoDTO buscarPresentadaPorExpedienteyAsunto(Integer idExpediente,
            Integer idAsunto, Integer idEstadoNotifiCiud) throws ServiceException {
        if (Objects.isNull(idExpediente) || Objects.isNull(idAsunto)) {
            return null;
        }
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_ID_ASUNTO,
                idAsunto, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, idEstadoNotifiCiud,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDEREG, Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<NotificacionCiudadano> lstNotificacionesPresentadas = this.find(filtros, null);
        if (lstNotificacionesPresentadas != null && !lstNotificacionesPresentadas.isEmpty()) {
            return NotificacionCiudadanoToDTO.INSTANCE.apply(lstNotificacionesPresentadas.get(0));
        }
        return null;
    }

}
