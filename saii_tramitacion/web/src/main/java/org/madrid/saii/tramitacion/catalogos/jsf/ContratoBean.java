package org.madrid.saii.tramitacion.catalogos.jsf;

import java.time.LocalDate;
import java.util.Objects;

import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.core.exceptions.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nttdata
 *
 */
@Slf4j
@Getter
@Setter
@Component("contratoBean")
@Scope("request")
public class ContratoBean {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////

    private ExpedienteBean expedienteBean;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////
    
    /**
     *
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /**
     *
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     *
     */
    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;
    
    
    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public ContratoBean() {
        super();
    }

    /**
     * Método para guardar los plazos de contratos
     */
    public void guardarPlazo() {

        // Verificamos que las fechas no sean vacías
        if (Objects.nonNull(this.expedienteBean.getExpedienteDTO()) && Boolean.TRUE
                .equals(this.fechasExisten(this.expedienteBean.getExpedienteDTO()))) {

            // recuperamos las fechas de formulario
            LocalDate fcIniContrato = Utils.convertDateToLocalDate(
                    this.expedienteBean.getExpedienteDTO().getFcIniContrato());

            LocalDate fcFinContrato = Utils.convertDateToLocalDate(
                    this.expedienteBean.getExpedienteDTO().getFcFinContrato());

            try {
                // Recuperamos los datos de la convocatoria
                ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                        .buscarId(this.expedienteBean.getIdConvocatoria());

                // Verificamos si las fechas ingresadas son válidas
                if (Boolean.TRUE.equals(
                        this.sonFechasValidas(fcIniContrato, fcFinContrato, convocatoriaDTO))) {

                    // Actualizamos las fechas de expedientes
                    ExpedienteDTO expedienteDTO = this.expedienteService.actualizarFechaContrato(
                            this.expedienteBean.getExpedienteDTO().getIdExpediente(),
                            this.expedienteBean.getExpedienteDTO().getFcIniContrato(),
                            this.expedienteBean.getExpedienteDTO().getFcFinContrato());

                    // Actualizamos datos a mostrar
                    this.expedienteBean.setExpedienteDTO(expedienteDTO);

                    // Enviamos mensaje de conformidad
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                }

            } catch (ServiceException e) {
                log.error(e.getMessage());
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
            }
        }
    }

    /**
     * Permite validar si las fechas de contratos estan vacias
     *
     * @param expedienteDTO
     * @return
     */
    private Boolean fechasExisten(ExpedienteDTO expedienteDTO) {
        Boolean existenFechas = Boolean.TRUE;
        if (Objects.isNull(expedienteDTO.getFcIniContrato())) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_FECHA_INICIO_CONTRATO_VACIO);
            existenFechas = Boolean.FALSE;
        }

        if (Objects.isNull(expedienteDTO.getFcFinContrato())) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_FECHA_FIN_CONTRATO_VACIO);
            existenFechas = Boolean.FALSE;
        }

        return existenFechas;
    }

    /**
     * Permite validar si las fechas ingresadas son válidas
     *
     * @param fcIniContrato
     * @param fcFinContrato
     * @param convocatoriaDTO
     * @return
     */
    private Boolean sonFechasValidas(LocalDate fcIniContrato, LocalDate fcFinContrato,
            ConvocatoriaDTO convocatoriaDTO) {
        Boolean fechasValidas = Boolean.TRUE;

        LocalDate fcIniContratoConv = Utils
                .convertDateToLocalDate(convocatoriaDTO.getFcIniContrato());

        LocalDate fcFinContratoConv = Utils
                .convertDateToLocalDate(convocatoriaDTO.getFcFinContrato());

        if (fcFinContrato.isBefore(fcIniContrato)) {

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Err.ERROR_FECHA_CONTRATO_INICIO_MAYOR_FIN);
            fechasValidas = Boolean.FALSE;

        } else {

            Boolean fechasFueraMargen = Boolean.FALSE;
            if (fcIniContrato.isBefore(fcIniContratoConv)
                    || fcIniContrato.isAfter(fcFinContratoConv)) {
                fechasFueraMargen = Boolean.TRUE;
                fechasValidas = Boolean.FALSE;
            }

            if (fcFinContrato.isBefore(fcIniContratoConv)
                    || fcFinContrato.isAfter(fcFinContratoConv)) {
                fechasFueraMargen = Boolean.TRUE;
                fechasValidas = Boolean.FALSE;
            }

            if (Boolean.TRUE.equals(fechasFueraMargen)) {
                Object[] o = new Object[] {
                        Utils.dateToString(convocatoriaDTO.getFcIniContrato(),
                                Constantes.Utiles.FORMATO_FECHA),
                        Utils.dateToString(convocatoriaDTO.getFcFinContrato(),
                                Constantes.Utiles.FORMATO_FECHA) };
                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Err.ERROR_FECHA_CONTRATO_EXPE_FUERA_RANGO, o);
            }

        }

        return fechasValidas;
    }

}
