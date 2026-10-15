package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecHistDTO;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.ExpedienteJusTecHistService;
import org.madrid.saii.lib.services.ExpedienteJusTecService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.primefaces.event.RowEditEvent;
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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@Component("expedienteJusTecBean")
@Scope("request")
public class ExpedienteJusTecBean {

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
    private List<ExpedienteJusTecDTO> listaExpedienteJustec;

    private ExpedienteJusTecDTO selectExpedienteJusTecDTO;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio asociado a la entidad Expediente
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /**
     * Servicio asociado a la entidad ExpedienteJusTecHist
     */
    @Autowired
    @Qualifier("expedienteJusTecHistService")
    private ExpedienteJusTecHistService expedienteJusTecHistService;

    /**
     *
     */
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     *
     */
    public void cargarInformacion() {

        try {

            this.listaExpedienteJustec = this.expedienteJusTecService
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
     * Metodo para guardar los datos editados del registro de la
     * ExpedienteJusTec
     *
     * @param event
     */
    public void guardarCambiosRegistro(RowEditEvent event) {

        try {

            ExpedienteJusTecDTO registroEditado = (ExpedienteJusTecDTO) event.getObject();

            if (Boolean.TRUE.equals(this.verificarFechas(registroEditado))) {

                this.expedienteJusTecService.guardar(registroEditado);

                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            }
            
            //VMM 10/12/24 MD-1058: recargamos la datatable de JCT si validó o no el proceso
            this.listaExpedienteJustec = this.expedienteJusTecService
                    .buscarPorExpediente(this.idExpediente);

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);

        }

    }

    /**
     *
     * @param dto
     * @return
     * @throws ServiceException 
     */
    private Boolean verificarFechas(ExpedienteJusTecDTO dto) throws ServiceException {
        Boolean esValido = Boolean.TRUE;

        if (Boolean.FALSE.equals(this.verificarCamposVacias(dto))) {
            esValido = Boolean.FALSE;
        } else {

            if (Boolean.FALSE.equals(this.verificarRangoFechas(dto))) {
                esValido = Boolean.FALSE;
            } else {

                if (Boolean.TRUE.equals(this.verificarFechasRepetidas(dto))) {
                    esValido = Boolean.FALSE;
                } else {

                    if (Boolean.FALSE.equals(this.verificarFilaFechaSecuencial(dto))) {
                        esValido = Boolean.FALSE;
                    }

                }

            }

        }

        return esValido;
    }

    /**
     *
     * @param dto
     * @return
     */
    private Boolean verificarCamposVacias(ExpedienteJusTecDTO dto) {
        Boolean esValido = Boolean.TRUE;

        if (Objects.isNull(dto.getFcIniJustiCienTec())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSTEC_VACIO);
            esValido = Boolean.FALSE;
        }

        if (Objects.isNull(dto.getFcFinJustiCienTec())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_JUSTEC_VACIO);
            esValido = Boolean.FALSE;
        }

        if (StringUtils.isBlank(dto.getTlObservacion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_OBSERVACION_VACIO);
            esValido = Boolean.FALSE;
        }

        return esValido;
    }

    /**
     *
     * @param dto
     * @return
     * @throws ServiceException 
     */
    private Boolean verificarRangoFechas(ExpedienteJusTecDTO dto) throws ServiceException {
        Boolean esValido = Boolean.TRUE;
        Date currentDate = new Date();
        
        // si la fecha de fin de justec es menor o igual a la de inicio, error
        if (dto.getFcFinJustiCienTec().before(dto.getFcIniJustiCienTec())
                || dto.getFcIniJustiCienTec().equals(dto.getFcFinJustiCienTec())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_JUSTEC_ERROR);
            esValido = Boolean.FALSE;
        }
        
        //VMM 10/12/24 MD-1058: comprobamos que no se ha iniciado el periodo de JCT para las modificaciones
        ExpedienteJusTecDTO item = this.expedienteJusTecService.buscarPorIdExpedienteJustec(dto.getIdExpedienteJusTec());

        // si la fecha de ini se ha modificado 
        if (item.getFcIniJustiCienTec().getTime() != dto.getFcIniJustiCienTec().getTime()) {
            
            //si la que ya habia es menor o igual a la de hoy (periodo ya iniciado), error
            if (item.getFcIniJustiCienTec().before(currentDate) || item.getFcIniJustiCienTec().equals(currentDate)) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSTEC_PASADA);
                esValido = Boolean.FALSE;
            }else {
                //si la que ya habia es mayor a la de hoy (periodo no iniciado), vemos si la modifcada es inferior o no
                if (dto.getFcIniJustiCienTec().before(currentDate) || dto.getFcIniJustiCienTec().equals(currentDate)) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSTEC_PASADA);
                    esValido = Boolean.FALSE;
                }
            } 
        }
        
        // si la fecha de ejecucion es mayor a la fecha de inicio de
        // justificacion cientifico tecnico
       /* if (dto.getFcIniEjecucion().after(dto.getFcIniJustiCienTec())
                || dto.getFcFinEjecucion().after(dto.getFcIniJustiCienTec())
                || dto.getFcIniEjecucion().equals(dto.getFcIniJustiCienTec())
                || dto.getFcFinEjecucion().equals(dto.getFcIniJustiCienTec())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_EJECUCION_MAYOR_JUSTEC_ERROR);
            esValido = Boolean.FALSE;
        }*/

        // si la fecha de justificacion cientifico tecnico es mayor a la fecha
        // de inicio de justificacion economica
        /*
         * if (dto.getFcIniJustiCienTec().after(dto.getFcIniJustiEconomica()) ||
         * dto.getFcFinJustiCienTec().after(dto.getFcIniJustiEconomica()) ||
         * dto.getFcIniJustiCienTec().equals(dto.getFcIniJustiEconomica()) ||
         * dto.getFcFinJustiCienTec().equals(dto.getFcIniJustiEconomica())) {
         * AtlasMessageUtils.addErrorMessageFromBundle( ConstantesWeb.Mensajes.
         * EXPEDIENTE_JUSTEC_FEC_JUSTEC_MAYOR_JUSECO_ERROR); esValido =
         * Boolean.FALSE; }
         */

        return esValido;
    }

    /**
     *
     * @param dto
     * @return
     */
    private Boolean verificarFechasRepetidas(ExpedienteJusTecDTO dto) {
        Boolean esRepetido = Boolean.FALSE;

        for (ExpedienteJusTecDTO item : this.listaExpedienteJustec) {

            if (!item.getNmPeriodo().equals(dto.getNmPeriodo())) {

                if (Objects.nonNull(item.getFcIniJustiCienTec())
                        && item.getFcIniJustiCienTec().equals(dto.getFcIniJustiCienTec())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSTEC_REPETIDO);
                    esRepetido = Boolean.TRUE;
                }

                if (Objects.nonNull(item.getFcFinJustiCienTec())
                        && item.getFcFinJustiCienTec().equals(dto.getFcFinJustiCienTec())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_JUSTEC_REPETIDO);
                    esRepetido = Boolean.TRUE;
                }
            }

        }

        return esRepetido;
    }

    /**
     *
     * @param dto
     * @return
     */
    private Boolean verificarFilaFechaSecuencial(ExpedienteJusTecDTO dto) {
        Boolean esSecuencial = Boolean.TRUE;

        if (this.listaExpedienteJustec.size() > 1 && dto.getNmPeriodo() != 1
                && dto.getNmPeriodo() != this.listaExpedienteJustec.size()) {

            int indiceAnterior = dto.getNmPeriodo() - 2;
//            int indiceActual = dto.getNmPeriodo() - 1;
//            int indiceSiguiente = dto.getNmPeriodo();

            ExpedienteJusTecDTO filaAnterior = this.listaExpedienteJustec.get(indiceAnterior);
//            ExpedienteJusTecDTO filaActual = this.listaExpedienteJustec.get(indiceActual);
//            ExpedienteJusTecDTO filaSiguiente = this.listaExpedienteJustec.get(indiceSiguiente);

            if (Objects.isNull(filaAnterior.getFcFinJustiCienTec())) {

                String periodo = String.valueOf(dto.getNmPeriodo() - 2);
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_NO_SECUENCIAL, periodo);
                esSecuencial = Boolean.FALSE;

            } else {

                /*if (filaAnterior.getFcFinJustiCienTec().after(filaActual.getFcIniEjecucion())
                        || filaAnterior.getFcFinJustiCienTec()
                                .equals(filaActual.getFcIniEjecucion())) {
                    String anualidad = String.valueOf(dto.getNmPeriodo() - 1);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_NO_SECUENCIAL, anualidad);
                    esSecuencial = Boolean.FALSE;
                }

                if (Objects.nonNull(filaSiguiente.getFcIniEjecucion())
                        && filaActual.getFcFinJustiCienTec()
                                .after(filaSiguiente.getFcIniEjecucion())
                        || filaActual.getFcFinJustiCienTec()
                                .equals(filaSiguiente.getFcIniEjecucion())) {
                    String periodo = String.valueOf(dto.getNmPeriodo() + 1);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_NO_SECUENCIAL, periodo);
                    esSecuencial = Boolean.FALSE;
                }*/

            }

        }

        return esSecuencial;
    }

    /**
     * Método para obtener la lista ExpedienteJustecHist
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<InteresadoDTO> Lista de Interesado
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteJusTecHistDTO> obtenerListaExpedienteJustecHist(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<ExpedienteJusTecHistDTO> lista = new AtlasListAndCount<>();

        try {

            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, this.idExpediente,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                    this.selectExpedienteJusTecDTO.getIdExpedienteJusTec(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<ExpedienteJusTecHistDTO> listaExpedienteJusTecHistDTO = this.expedienteJusTecHistService
                    .buscar(index, pageSize, filtros, orden);
            lista.setResultados(listaExpedienteJusTecHistDTO);
            lista.setTotal(listaExpedienteJusTecHistDTO.size());

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return lista;

    }

    /**
     * Metodo para ver el histórico
     */
    public void verHistorico(ExpedienteJusTecDTO selectExpedienteJusTecDTO) {
        this.selectExpedienteJusTecDTO = selectExpedienteJusTecDTO;
        PrimefacesUtils.mostrarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO);
    }

    /**
     * Metodo para cerrar el histórico
     */
    public void cerrarHistorico() {
        this.selectExpedienteJusTecDTO = new ExpedienteJusTecDTO();
        PrimefacesUtils.ocultarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO);
    }

}
