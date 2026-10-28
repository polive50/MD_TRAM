package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.model.SelectItem;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.madrid.saii.lib.services.NotificacionCiudadanoService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.utils.PrimefacesUtils;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/** Gestión de las comunicaciones del ciudadano en la revisión del expediente. */
@Slf4j
@Getter
@Setter
@Component("comunCiudadanoBean")
@Scope("request")
public class ComunCiudadanoBean {

    private Integer idComunicacionDesplegada;
    private List<ExpedienteDocDTO> documentosComunicacionDesplegada = new ArrayList<>();

    /** Evita repetir la consulta en una petición; la vista no ofrece filtros ni orden interactivos. */
    private AtlasListAndCount<NotificacionCiudadanoDTO> paginaComunicaciones;
    private Integer idExpedientePagina;
    private int indicePaginaComunicaciones = -1;
    private int tamanoPaginaComunicaciones = -1;
    private NotificacionCiudadanoDTO nuevaComunicacion = new NotificacionCiudadanoDTO();
    private List<SelectItem> tiposComunicacion = new ArrayList<>();
    private UploadedFile archivoSubir;
    private boolean comunicacionAbierta;

    @Autowired
    private ExpedienteBean expedienteBean;

    @Autowired
    private NotificacionCiudadanoService notificacionCiudadanoService;

    /** Recupera los tipos de notificación al abrir el formulario de alta. */
    public void agregarComunicacion() {
        if (!this.puedeModificar()) {
            return;
        }
        try {
            List<CatalogosValoresDTO> tiposNotificacion =
                    this.notificacionCiudadanoService.obtenerTiposComunicacionPermitidos(this.getIdExpediente());
            this.tiposComunicacion = new ArrayList<>();
            for (CatalogosValoresDTO tipoNotificacion : tiposNotificacion) {
                this.tiposComunicacion.add(new SelectItem(tipoNotificacion.getIdValor(), tipoNotificacion.getDsValor()));
            }
            this.nuevaComunicacion = new NotificacionCiudadanoDTO();
            this.nuevaComunicacion.getExpedienteDTO().setIdExpediente(this.getIdExpediente());
            this.comunicacionAbierta = true;
            PrimefacesUtils.mostrarPopupPF("dialogComunicacionCiudadano");
        } catch (ServiceException e) {
            log.error("Error al preparar la comunicación del ciudadano", e);
            this.mostrarErrorComunicacion(e);
        }
    }

    private void mostrarErrorComunicacion(ServiceException excepcion) {
        String mensaje = excepcion.getMessage();
        if ("comunicacionCiudadano.error.tipoNoPermitido".equals(mensaje)
                || "comunicacionCiudadano.error.estadoNoDisponible".equals(mensaje)
                || "comunicacionCiudadano.error.unidadAdministrativaLongitud".equals(mensaje)) {
            AtlasMessageUtils.addErrorMessageFromBundle(mensaje);
        } else {
            AtlasMessageUtils.addErrorMessage(mensaje);
        }
    }

    /** Aplica también en servidor el modo de consulta y el expediente seleccionado. */
    private boolean puedeModificar() {
        Integer idExpedienteSeleccionado = this.getIdExpediente();
        boolean permitido = idExpedienteSeleccionado != null
                && !this.expedienteBean.getConvocatoriasBean().isModoConsulta()
                && !Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta());
        if (!permitido) {
            AtlasMessageUtils.addErrorMessage("No se pueden modificar las comunicaciones del expediente seleccionado.");
        }
        return permitido;
    }

    public void cerrarComunicacion() {
        this.comunicacionAbierta = false;
        this.nuevaComunicacion = new NotificacionCiudadanoDTO();
        this.archivoSubir = null;
        PrimefacesUtils.ocultarPopupPF("dialogComunicacionCiudadano");
    }

    /** Conserva el archivo sin persistir; se registra al guardar la comunicación. */
    public void adjuntarDocumento() {
        if (!this.puedeModificar()) {
            return;
        }
        this.incorporarArchivo();
    }

    public boolean incorporarArchivo() {
        if (this.archivoSubir == null || this.archivoSubir.getSize() == 0
                || StringUtils.isBlank(FilenameUtils.getExtension(this.archivoSubir.getFileName()))) {
            AtlasMessageUtils.addErrorMessageFromBundle("comunicacionCiudadano.error.archivoInvalido");
            return false;
        }
        try (InputStream contenidoArchivo = this.archivoSubir.getInputstream()) {
            ExpedienteDocDTO documento = new ExpedienteDocDTO();
            documento.setTipoDocumentoDTO(null);
            documento.setContenidoFichero(IOUtils.toByteArray(contenidoArchivo));
            documento.setNombreFicheroFica(FilenameUtils.getName(this.archivoSubir.getFileName()));
            this.nuevaComunicacion.getLstExpedienteDocDTO().add(documento);
            this.archivoSubir = null;
            return true;
        } catch (IOException e) {
            log.error("Error al adjuntar el documento a la comunicación", e);
            AtlasMessageUtils.addErrorMessageFromBundle("comunicacionCiudadano.error.lecturaArchivo");
            return false;
        }
    }

    public void guardarComunicacion() {
        if (!this.puedeModificar()) {
            return;
        }
        // El archivo seleccionado también se incorpora al guardar, sin un paso adicional.
        if (this.archivoSubir != null && StringUtils.isNotBlank(this.archivoSubir.getFileName())
                && !this.incorporarArchivo()) {
            return;
        }
        try {
            this.nuevaComunicacion.getExpedienteDTO().setIdExpediente(this.getIdExpediente());
            // Los metadatos FICA son transitorios: se reconstruyen tras restaurar guardarEstado.
            List<ExpedienteDocDTO> documentosPendientes = this.nuevaComunicacion.getLstExpedienteDocDTO();
            for (ExpedienteDocDTO documentoPendiente : documentosPendientes) {
                FicaMetadatos metadatos = new FicaMetadatos();
                metadatos.setNombreFichero(documentoPendiente.getNombreFicheroFica());
                metadatos.setExtension(FilenameUtils.getExtension(documentoPendiente.getNombreFicheroFica()));
                metadatos.setFechaFichero(new Date());
                metadatos.setUsuario(UserUtils.getUserName().toUpperCase());
                metadatos.setEncriptar(false);
                documentoPendiente.setFicaMetadatos(metadatos);
            }
            this.notificacionCiudadanoService.registrarComunicacion(this.nuevaComunicacion);
        } catch (ServiceException e) {
            log.error("Error al registrar la comunicación del ciudadano", e);
            this.mostrarErrorComunicacion(e);
            return;
        }
        this.cerrarComunicacion();
        AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        this.paginaComunicaciones = null;
    }

    /** El cambio de revisión actualiza solo el estado, conservando el resto de los datos. */
    public void cambiarRevision(NotificacionCiudadanoDTO comunicacion, boolean revisada) {
        if (!this.puedeModificar()) {
            comunicacion.setRevisada(Constantes.EstadoNotifiCiud.RESUELTA.equals(
                    comunicacion.getEstadoDTO().getNmConstante()));
            return;
        }
        try {
            CatalogosValoresDTO estadoRevision = this.notificacionCiudadanoService.actualizarRevision(
                    comunicacion.getIdNotificacion(), this.getIdExpediente(), revisada);
            comunicacion.setEstadoDTO(estadoRevision);
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            comunicacion.setRevisada(Constantes.EstadoNotifiCiud.RESUELTA.equals(
                    comunicacion.getEstadoDTO().getNmConstante()));
            log.error("Error al cambiar la revisión de la comunicación", e);
            AtlasMessageUtils.addErrorMessage(e.getMessage());
        }
    }

    public String formatearCodigoEreg(String codigoEreg) {
        if (StringUtils.isBlank(codigoEreg) || codigoEreg.contains("/") || codigoEreg.length() != 11) {
            return codigoEreg;
        }
        return Utils.formatearNmEreg(codigoEreg);
    }

    public String descripcionTipoDocumento(ExpedienteDocDTO documento) {
        CatalogosValoresDTO tipoDocumento = documento.getTipoDocumentoDTO();
        if (tipoDocumento == null || tipoDocumento.getIdValor() == null) {
            return "OTROS" + (StringUtils.isBlank(documento.getTlObservaciones()) ? ""
                    : " - " + documento.getTlObservaciones());
        }
        return tipoDocumento.getDsValor();
    }

    /** Obtiene el identificador del expediente ya conservado por la pantalla de revisión. */
    public Integer getIdExpediente() {
        return this.expedienteBean == null || this.expedienteBean.getExpedienteDTO() == null
                ? null : this.expedienteBean.getExpedienteDTO().getIdExpediente();
    }
    
    /** La lista paginada solicita sus datos al mostrarse, igual que los demás listados Atlas. */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<NotificacionCiudadanoDTO> obtenerListaComunicaciones(int index,
            int pageSize, Object ordenacion, Object filter) {
        Integer idExpedienteSeleccionado = this.getIdExpediente();
        if (this.paginaComunicaciones != null
                && Objects.equals(idExpedienteSeleccionado, this.idExpedientePagina)
                && index == this.indicePaginaComunicaciones
                && pageSize == this.tamanoPaginaComunicaciones) {
            return this.paginaComunicaciones;
        }
        AtlasListAndCount<NotificacionCiudadanoDTO> listaComunicaciones = new AtlasListAndCount<>();
        if (idExpedienteSeleccionado == null) {
            return listaComunicaciones;
        }
        try {
            List<AtlasFilter> filtrosComunicaciones = filter == null ? new ArrayList<>()
                    : new ArrayList<>((List<AtlasFilter>) filter);
            filtrosComunicaciones.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    idExpedienteSeleccionado, Operador.EQUALS));
            filtrosComunicaciones.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<AtlasSort> ordenComunicaciones = ordenacion == null ? new ArrayList<>()
                    : new ArrayList<>((List<AtlasSort>) ordenacion);
            if (ordenComunicaciones.isEmpty()) {
                ordenComunicaciones.add(new AtlasSort("idNotificacion", false));
            }
            List<NotificacionCiudadanoDTO> comunicacionesPagina = this.notificacionCiudadanoService
                    .buscarComunicaciones(index, pageSize, filtrosComunicaciones, ordenComunicaciones);
            int totalComunicaciones = this.notificacionCiudadanoService.count(filtrosComunicaciones);
            listaComunicaciones.setResultados(comunicacionesPagina);
            listaComunicaciones.setTotal(totalComunicaciones);
        } catch (ServiceException e) {
            log.error("Error al recuperar el listado de comunicaciones del ciudadano", e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        this.paginaComunicaciones = listaComunicaciones;
        this.idExpedientePagina = idExpedienteSeleccionado;
        this.indicePaginaComunicaciones = index;
        this.tamanoPaginaComunicaciones = pageSize;
        return listaComunicaciones;
    }

    /** Consulta los adjuntos solo al desplegar la comunicación seleccionada. */
    public void alternarDocumentos(NotificacionCiudadanoDTO comunicacion) {
        if (Objects.equals(this.idComunicacionDesplegada, comunicacion.getIdNotificacion())) {
            this.idComunicacionDesplegada = null;
            this.documentosComunicacionDesplegada = new ArrayList<>();
            return;
        }
        try {
            List<ExpedienteDocDTO> documentosAdjuntos = this.notificacionCiudadanoService
                    .buscarDocumentosComunicacion(comunicacion.getIdNotificacion(), this.getIdExpediente());
            this.documentosComunicacionDesplegada = documentosAdjuntos;
            this.idComunicacionDesplegada = comunicacion.getIdNotificacion();
        } catch (ServiceException e) {
            log.error("Error al recuperar los documentos de la comunicación del ciudadano", e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
    }
}