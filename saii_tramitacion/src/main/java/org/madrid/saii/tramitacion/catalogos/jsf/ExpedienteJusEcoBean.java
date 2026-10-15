package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoHistDTO;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.ExpedienteJusEcoHistService;
import org.madrid.saii.lib.services.ExpedienteJusEcoService;
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
@Component("expedienteJusEcoBean")
@Scope("request")
public class ExpedienteJusEcoBean {

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
    private List<ExpedienteJusEcoDTO> listaExpedienteJuseco;

    private ExpedienteJusEcoDTO selectExpedienteJusEcoDTO;

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
     * Servicio asociado a la entidad ExpedienteJusEcoHist
     */
    @Autowired
    @Qualifier("expedienteJusEcoHistService")
    private ExpedienteJusEcoHistService expedienteJusEcoHistService;

    /**
     *
     */
    @Autowired
    @Qualifier("expedienteJusEcoService")
    private ExpedienteJusEcoService expedienteJusEcoService;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     *
     */
    public void cargarInformacion() {

        try {

            this.listaExpedienteJuseco = this.expedienteJusEcoService
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
     * ExpedienteJusEco
     *
     * @param event
     */
    public void guardarCambiosRegistro(RowEditEvent event) {

        try {

            ExpedienteJusEcoDTO registroEditado = (ExpedienteJusEcoDTO) event.getObject();

            if (Boolean.TRUE.equals(this.verificarFechas(registroEditado))) {

                this.expedienteJusEcoService.guardar(registroEditado);

                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);

            }
            
            this.listaExpedienteJuseco = this.expedienteJusEcoService
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
    private Boolean verificarFechas(ExpedienteJusEcoDTO dto) throws ServiceException {
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
    private Boolean verificarCamposVacias(ExpedienteJusEcoDTO dto) {
        Boolean esValido = Boolean.TRUE;

        if (Objects.isNull(dto.getFcIniJustiEconomica())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSECO_VACIO);
            esValido = Boolean.FALSE;
        }

        if (Objects.isNull(dto.getFcFinJustiEconomica())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_JUSECO_VACIO);
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
    private Boolean verificarRangoFechas(ExpedienteJusEcoDTO dto) throws ServiceException {
        Boolean esValido = Boolean.TRUE;
        Date currentDate = new Date();

        // si la fecha de fin de justificacion economica es mayor o igual a la
        // de inicio
        if (dto.getFcFinJustiEconomica().before(dto.getFcIniJustiEconomica())
                || dto.getFcIniJustiEconomica().equals(dto.getFcFinJustiEconomica())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_JUSECO_ERROR);
            esValido = Boolean.FALSE;
        }
        
        //VMM 10/12/24 MD-1058: comprobamos que no se ha iniciado el periodo de JCT para las modificaciones
        ExpedienteJusEcoDTO item = this.expedienteJusEcoService.buscarPorIdExpedienteJuseco(dto.getIdExpedienteJusEco());
        
        // si la fecha de ini se ha modificado 
        if (item.getFcIniJustiEconomica().getTime() != dto.getFcIniJustiEconomica().getTime()) {
            
            //si la que ya habia es menor o igual a la de hoy (periodo ya iniciado), error
            if (item.getFcIniJustiEconomica().before(currentDate) || item.getFcIniJustiEconomica().equals(currentDate)) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSTECO_PASADA);
                esValido = Boolean.FALSE;
            }else {
                //si la que ya habia es mayor a la de hoy (periodo no iniciado), vemos si la modifcada es inferior o no
                if (dto.getFcIniJustiEconomica().before(currentDate) || dto.getFcIniJustiEconomica().equals(currentDate)) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSTECO_PASADA);
                    esValido = Boolean.FALSE;
                }
            } 
        }
        
        // si la fecha de ejecucion es mayor a la fecha de inicio de
        // justificacion economica
        /*if (dto.getFcIniEjecucion().after(dto.getFcIniJustiEconomica())
                || dto.getFcFinEjecucion().after(dto.getFcIniJustiEconomica())
                || dto.getFcIniEjecucion().equals(dto.getFcIniJustiEconomica())
                || dto.getFcFinEjecucion().equals(dto.getFcIniJustiEconomica())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSECO_FEC_EJECUCION_MAYOR_JUSECO_ERROR);
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
         * EXPEDIENTE_JUSECO_FEC_JUSECO_MAYOR_JUSECO_ERROR); esValido =
         * Boolean.FALSE; }
         */

        return esValido;
    }

    /**
     *
     * @param dto
     * @return
     */
    private Boolean verificarFechasRepetidas(ExpedienteJusEcoDTO dto) {
        Boolean esRepetido = Boolean.FALSE;

        for (ExpedienteJusEcoDTO item : this.listaExpedienteJuseco) {

            if (!item.getNmPeriodo().equals(dto.getNmPeriodo())) {

                if (Objects.nonNull(item.getFcIniJustiEconomica())
                        && item.getFcIniJustiEconomica().equals(dto.getFcIniJustiEconomica())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_JUSECO_REPETIDO);
                    esRepetido = Boolean.TRUE;
                }

                if (Objects.nonNull(item.getFcFinJustiEconomica())
                        && item.getFcFinJustiEconomica().equals(dto.getFcFinJustiEconomica())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_JUSECO_REPETIDO);
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
    private Boolean verificarFilaFechaSecuencial(ExpedienteJusEcoDTO dto) {
        Boolean esSecuencial = Boolean.TRUE;
        
        if (this.listaExpedienteJuseco.size() > 1 && dto.getNmPeriodo() != 1
                && dto.getNmPeriodo() != this.listaExpedienteJuseco.size()) {
            
            int indiceAnterior = dto.getNmPeriodo() - 2;
//            int indiceActual = dto.getNmPeriodo() - 1;
//            int indiceSiguiente = dto.getNmPeriodo();
            
            ExpedienteJusEcoDTO filaAnterior = this.listaExpedienteJuseco.get(indiceAnterior);
//            ExpedienteJusEcoDTO filaActual = this.listaExpedienteJuseco.get(indiceActual);
//            ExpedienteJusEcoDTO filaSiguiente = this.listaExpedienteJuseco.get(indiceSiguiente);
            
            if (Objects.isNull(filaAnterior.getFcFinJustiEconomica())) {

                String periodo = String.valueOf(dto.getNmPeriodo() - 2);
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.EXPEDIENTE_JUSECO_FEC_NO_SECUENCIAL, periodo);
                esSecuencial = Boolean.FALSE;

            } else {

                /*
                if (filaAnterior.getFcFinJustiEconomica().after(filaActual.getFcIniEjecucion())
                        || filaAnterior.getFcFinJustiEconomica()
                                .equals(filaActual.getFcIniEjecucion())) {
                    String anualidad = String.valueOf(dto.getNmPeriodo() - 1);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_NO_SECUENCIAL, anualidad);
                    esSecuencial = Boolean.FALSE;
                }
        
                if (Objects.nonNull(filaSiguiente.getFcIniEjecucion())
                        && filaActual.getFcFinJustiEconomica()
                                .after(filaSiguiente.getFcIniEjecucion())
                        || filaActual.getFcFinJustiEconomica()
                                .equals(filaSiguiente.getFcIniEjecucion())) {
                    String periodo = String.valueOf(dto.getNmPeriodo() + 1);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_NO_SECUENCIAL, periodo);
                    esSecuencial = Boolean.FALSE;
                }
                */

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
    public AtlasListAndCount<ExpedienteJusEcoHistDTO> obtenerListaExpedienteJusecoHist(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<ExpedienteJusEcoHistDTO> lista = new AtlasListAndCount<>();

        try {

            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, this.idExpediente,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSECO,
                    this.selectExpedienteJusEcoDTO.getIdExpedienteJusEco(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<ExpedienteJusEcoHistDTO> listaExpedienteJusEcoHistDTO = this.expedienteJusEcoHistService
                    .buscar(index, pageSize, filtros, orden);
            lista.setResultados(listaExpedienteJusEcoHistDTO);
            lista.setTotal(listaExpedienteJusEcoHistDTO.size());

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return lista;

    }

    /**
     * Metodo para ver el histórico
     */
    public void verHistorico(ExpedienteJusEcoDTO selectExpedienteJusEcoDTO) {
        this.selectExpedienteJusEcoDTO = selectExpedienteJusEcoDTO;
        PrimefacesUtils.mostrarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO_ECO);
    }

    /**
     * Metodo para cerrar el histórico
     */
    public void cerrarHistorico() {
        this.selectExpedienteJusEcoDTO = new ExpedienteJusEcoDTO();
        PrimefacesUtils.ocultarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO_ECO);
    }

}
