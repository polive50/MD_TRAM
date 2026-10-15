package org.madrid.saii.tramitacion.catalogos.jsf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecDTO;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecHistDTO;
import org.madrid.saii.lib.services.ExpedienteDocHistService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteJusTecService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.RevisionInfEvaJustecHistService;
import org.madrid.saii.lib.services.RevisionInfEvaJustecService;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.componentes.utils.PrimefacesUtils;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@Component("revisionInfEvaJustecBean")
@Scope("request")
public class RevisionInfEvaJustBean extends CatalogoBean<RevisionInfEvaJustec, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PROPIEDADES
    // ////////////////////////////////////////////////////////

    /* Id del expediente */
    private Integer idExpediente;

    /**
     *
     */
    private ExpedienteBean expedienteBean;

    /**
     *
     */
    private List<RevisionInfEvaJustecDTO> listaRevisionInfEvaJustec;

    private RevisionInfEvaJustecDTO selectRevisionInfEvaJustecDTO;


    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio asociado a la entidad Convocatoria
     */
    @Autowired
    @Qualifier("revisionInfEvaJustecHistService")
    private RevisionInfEvaJustecHistService revisionInfEvaJustecHistService;

    @Autowired
    @Qualifier("revisionInfEvaJustecService")
    private RevisionInfEvaJustecService revisionInfEvaJustecService;

    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;

    @Autowired
    @Qualifier("expedienteDocHistService")
    private ExpedienteDocHistService expedienteDocHistService;

    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;

    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;

    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     *
     */
    public void cargarInformacion() {
        try {
            this.listaRevisionInfEvaJustec = this.revisionInfEvaJustecService
                    .buscarPorExpediente(this.idExpediente);

        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// MÉTODOS
    // ////////////////////////////////////////////////////////

    /**
     * Crea una nueva instancia de la entidad RevisionInfEvaJustec
     *
     * @return entidad de tipo RevisionInfEvaJustec
     */
    @Override
    public RevisionInfEvaJustec crearEntidad() {
        return new RevisionInfEvaJustec();
    }
   
    /**
     * Método para obtener la lista de revisiones de Evaluacion justifico técnica
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<RevisionInfEvaJustecDTO> Lista de revisiones
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<RevisionInfEvaJustecDTO> obtenerListaRevisionInfEvaJustec(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<RevisionInfEvaJustecDTO> lista = new AtlasListAndCount<>();
        try {

            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, this.idExpediente,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                    this.selectRevisionInfEvaJustecDTO.getIdRevInfEvaJustec(), Operador.EQUALS));
            List<AtlasFilter> filtroEstados = new ArrayList<>();
            filtroEstados.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK), Operador.EQUALS));
            filtroEstados.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR,
                    filtroEstados, Operador.OR));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<RevisionInfEvaJustecDTO> listaRevisionInfEvaJustecDTO = this.revisionInfEvaJustecService
                    .buscar(index, pageSize, filtros, orden);
            lista.setResultados(listaRevisionInfEvaJustecDTO);
            lista.setTotal(listaRevisionInfEvaJustecDTO.size());

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return lista;

    }
    
    /**
     * Método para obtener la lista de revisiones de Evaluacion científico técnica históricas
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<RevisionInfEvaJustecHistDTO> Lista de revisiones de Evaluación científico técnica
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<RevisionInfEvaJustecHistDTO> obtenerListaRevisionInfEvaJustecHist(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<RevisionInfEvaJustecHistDTO> lista = new AtlasListAndCount<>();

        try {
            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, this.idExpediente,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                    this.selectRevisionInfEvaJustecDTO.getIdRevInfEvaJustec(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<RevisionInfEvaJustecHistDTO> listaRevisionInfEvaJustecHistDTO = this.revisionInfEvaJustecHistService
                    .buscar(index, pageSize, filtros, orden);
            lista.setResultados(listaRevisionInfEvaJustecHistDTO);
            lista.setTotal(listaRevisionInfEvaJustecHistDTO.size());

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return lista;

    }

    /**
     * Metodo para ver el histórico
     */
    public void verHistorico(RevisionInfEvaJustecDTO selectRevisionInfEvaJustecDTO) {
        this.selectRevisionInfEvaJustecDTO = selectRevisionInfEvaJustecDTO;
        PrimefacesUtils.mostrarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO);
    }

    /**
     * Metodo para cerrar el histórico
     */
    public void cerrarHistorico() {
        this.selectRevisionInfEvaJustecDTO = new RevisionInfEvaJustecDTO();
        PrimefacesUtils.ocultarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO);
    }


    
    public void descargarDocumentoRevisionInfEvaJustecFica(Integer codigoFica) {
        try {

            if (codigoFica != null) {

                FicaFichero ficaFichero = this.ficaService.recuperar(codigoFica);

                if (ficaFichero != null) {

                    log.info("Documento " + ficaFichero.getNombreFichero() + " recuperado");

                    // Se prepara el fichero.
                    UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                            FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));

                } else {
                    log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                }

            } else {
                log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }

    public void descargarDocumentoRevisionInfEvaJustecEreg(ExpedienteDocDTO expedienteDocDTO) {
        try {
            ExpedienteDTO expDTO = this.expedienteService.buscarIdMinimo(this.idExpediente);

            byte[] documento = this.eregAdmeService.descargarAnexoPorCodigo(
                    expDTO.getCdEreg(), expedienteDocDTO.getCdAnexoEreg());

            DateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA_DESCARGA);

            // Se prepara el fichero.
            UtilesWeb.openFile(documento, Constantes.Extensiones.PDF,
                    expedienteDocDTO.getCdAnexoEreg() + "_"
                            + sdf.format(expedienteDocDTO.getFcAltaFila()));
            
            
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO, ex.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }
    
    public void desaprobar(RevisionInfEvaJustecDTO revisionInfEvaJustecDTO ) {
        try {
            this.selectRevisionInfEvaJustecDTO.setIdEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO)));
            this.revisionInfEvaJustecService.guardar(this.selectRevisionInfEvaJustecDTO);
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        }
    }
    
    public void aprobar(RevisionInfEvaJustecDTO revisionInfEvaJustecDTO ) {
        try {
            this.selectRevisionInfEvaJustecDTO.setIdEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
            this.revisionInfEvaJustecService.guardar(this.selectRevisionInfEvaJustecDTO);
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        }
    }
    
    public void guardarObservaciones(RevisionInfEvaJustecDTO revisionInfEvaJustecDTO) {
        try {
            this.revisionInfEvaJustecService.guardar(this.selectRevisionInfEvaJustecDTO);
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        }
    }
}
