package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.madrid.saii.lib.dto.ExpedienteAnualidadHistDTO;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.ExpedienteAnualidadHistService;
import org.madrid.saii.lib.services.ExpedienteAnualidadService;
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
@Component("expedienteAnualidadBean")
@Scope("request")
public class ExpedienteAnualidadBean {

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
    private List<ExpedienteAnualidadDTO> listaExpedienteAnualidad;

    private ExpedienteAnualidadDTO selectExpedienteAnualidadDTO;

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
     * Servicio asociado a la entidad ExpedienteAnualidadHist
     */
    @Autowired
    @Qualifier("expedienteAnualidadHistService")
    private ExpedienteAnualidadHistService expedienteAnualidadHistService;

    /**
     *
     */
    @Autowired
    @Qualifier("expedienteAnualidadService")
    private ExpedienteAnualidadService expedienteAnualidadService;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     *
     */
    public void cargarInformacion() {

        try {

            this.listaExpedienteAnualidad = this.expedienteAnualidadService
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
     * ExpedienteAnualidad
     *
     * @param event
     */
    public void guardarCambiosRegistro(RowEditEvent event) {

        try {

            ExpedienteAnualidadDTO registroEditado = (ExpedienteAnualidadDTO) event.getObject();

            if (Boolean.TRUE.equals(this.verificarFechas(registroEditado))) {

                this.expedienteAnualidadService.guardar(registroEditado);

                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            }
            
            //recargamos la datatable si validó o no el proceso
            this.listaExpedienteAnualidad = this.expedienteAnualidadService
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
     */
    private Boolean verificarFechas(ExpedienteAnualidadDTO dto) {
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
    private Boolean verificarCamposVacias(ExpedienteAnualidadDTO dto) {
        Boolean esValido = Boolean.TRUE;

        if (Objects.isNull(dto.getFcIniEjecucion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_EJECUCION_VACIO);
            esValido = Boolean.FALSE;
        }

        if (Objects.isNull(dto.getFcFinEjecucion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_EJECUCION_VACIO);
            esValido = Boolean.FALSE;
        }

        if (StringUtils.isBlank(dto.getTlObservacion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_JUSECO_VACIO);
            esValido = Boolean.FALSE;
        }

        return esValido;
    }

    /**
     *
     * @param dto
     * @return
     */
    private Boolean verificarRangoFechas(ExpedienteAnualidadDTO dto) {
        Boolean esValido = Boolean.TRUE;

        // si la fecha de fin de ejecucion es mayor o igual a la de inicio
        if (dto.getFcFinEjecucion().before(dto.getFcIniEjecucion())
                || dto.getFcIniEjecucion().equals(dto.getFcFinEjecucion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_EJECUCION_ERROR);
            esValido = Boolean.FALSE;
        }

        // si la fecha de ejecucion es mayor a la fecha de inicio de
        // justificacion cientifico tecnico
        /*if (dto.getFcIniEjecucion().after(dto.getFcIniJustiCienTec())
                || dto.getFcFinEjecucion().after(dto.getFcIniJustiCienTec())
                || dto.getFcIniEjecucion().equals(dto.getFcIniJustiCienTec())
                || dto.getFcFinEjecucion().equals(dto.getFcIniJustiCienTec())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_ANUALIDAD_FEC_EJECUCION_MAYOR_ANUALIDAD_ERROR);
            esValido = Boolean.FALSE;
        }

        // si la fecha de ejecucion es mayor a la fecha de inicio de
        // justificacion economica
        if (dto.getFcIniEjecucion().after(dto.getFcIniJustiEconomica())
                || dto.getFcFinEjecucion().after(dto.getFcIniJustiEconomica())
                || dto.getFcIniEjecucion().equals(dto.getFcIniJustiEconomica())
                || dto.getFcFinEjecucion().equals(dto.getFcIniJustiEconomica())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_ANUALIDAD_FEC_EJECUCION_MAYOR_JUSECO_ERROR);
            esValido = Boolean.FALSE;
        }*/

       return esValido;
    }

    /**
     *
     * @param dto
     * @return
     */
    private Boolean verificarFechasRepetidas(ExpedienteAnualidadDTO dto) {
        Boolean esRepetido = Boolean.FALSE;

        for (ExpedienteAnualidadDTO item : this.listaExpedienteAnualidad) {

            if (!item.getNmAnualidad().equals(dto.getNmAnualidad())) {

                if (Objects.nonNull(item.getFcIniEjecucion())
                        && item.getFcIniEjecucion().equals(dto.getFcIniEjecucion())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_EJECUCION_REPETIDO);
                    esRepetido = Boolean.TRUE;
                }

                if (Objects.nonNull(item.getFcFinEjecucion())
                        && item.getFcFinEjecucion().equals(dto.getFcFinEjecucion())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_EJECUCION_REPETIDO);
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
    private Boolean verificarFilaFechaSecuencial(ExpedienteAnualidadDTO dto) {
        Boolean esSecuencial = Boolean.TRUE;

        if (this.listaExpedienteAnualidad.size() > 1 && dto.getNmAnualidad() != 1
                && dto.getNmAnualidad() != this.listaExpedienteAnualidad.size()) {

//            int indiceAnterior = dto.getNmAnualidad() - 2;
//            int indiceActual = dto.getNmAnualidad() - 1;
//            int indiceSiguiente = dto.getNmAnualidad();

//            ExpedienteAnualidadDTO filaAnterior = this.listaExpedienteAnualidad.get(indiceAnterior);
//            ExpedienteAnualidadDTO filaActual = this.listaExpedienteAnualidad.get(indiceActual);
//            ExpedienteAnualidadDTO filaSiguiente = this.listaExpedienteAnualidad.get(indiceSiguiente);

           /* if (Objects.isNull(filaAnterior.getFcFinJustiCienTec())) {

                String anualidad = String.valueOf(dto.getNmAnualidad() - 2);
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.EXPEDIENTE_ANUALIDAD_FEC_NO_SECUENCIAL, anualidad);
                esSecuencial = Boolean.FALSE;

            } else {

                if (filaAnterior.getFcFinJustiCienTec().after(filaActual.getFcIniEjecucion())
                        || filaAnterior.getFcFinJustiCienTec()
                                .equals(filaActual.getFcIniEjecucion())) {
                    String anualidad = String.valueOf(dto.getNmAnualidad() - 1);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_ANUALIDAD_FEC_NO_SECUENCIAL, anualidad);
                    esSecuencial = Boolean.FALSE;
                }

                if (Objects.nonNull(filaSiguiente.getFcIniEjecucion())
                        && filaActual.getFcFinJustiCienTec()
                                .after(filaSiguiente.getFcIniEjecucion())
                        || filaActual.getFcFinJustiCienTec()
                                .equals(filaSiguiente.getFcIniEjecucion())) {
                    String anualidad = String.valueOf(dto.getNmAnualidad() + 1);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_ANUALIDAD_FEC_NO_SECUENCIAL, anualidad);
                    esSecuencial = Boolean.FALSE;
                }

            }*/

        }

        return esSecuencial;
    }

    /**
     * Método para obtener la lista ExpedienteAnualidadHist
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<InteresadoDTO> Lista de Interesado
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteAnualidadHistDTO> obtenerListaExpedienteAnualHist(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<ExpedienteAnualidadHistDTO> lista = new AtlasListAndCount<>();

        try {

            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, this.idExpediente,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_ANUALIDAD,
                    this.selectExpedienteAnualidadDTO.getIdExpedienteAnualidad(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<ExpedienteAnualidadHistDTO> listaExpedienteAnualidadHistDTO = this.expedienteAnualidadHistService
                    .buscar(index, pageSize, filtros, orden);
            lista.setResultados(listaExpedienteAnualidadHistDTO);
            lista.setTotal(listaExpedienteAnualidadHistDTO.size());

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return lista;

    }

    /**
     * Metodo para ver el histórico
     */
    public void verHistorico(ExpedienteAnualidadDTO selectExpedienteAnualidadDTO) {
        this.selectExpedienteAnualidadDTO = selectExpedienteAnualidadDTO;
        PrimefacesUtils.mostrarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO_ANUAL);
    }

    /**
     * Metodo para cerrar el histórico
     */
    public void cerrarHistorico() {
        this.selectExpedienteAnualidadDTO = new ExpedienteAnualidadDTO();
        PrimefacesUtils.ocultarPopupPF(ConstantesWeb.Referencia.DLG_VER_HISTORICO_ANUAL);
    }

}
