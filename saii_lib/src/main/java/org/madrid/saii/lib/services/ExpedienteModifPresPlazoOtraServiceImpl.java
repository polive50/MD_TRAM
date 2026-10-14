package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.ExpedienteModifPresPlazoOtraDAO;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPresPlazoOtraToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPresPlazoOtraToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio
 * ExpedienteModifPresPlazoOtra
 *
 * @author quental
 */
@Slf4j
@Service("expedienteModifPresPlazoOtraService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteModifPresPlazoOtraServiceImpl extends
        BaseServiceImpl<ExpedienteModifPresPlazoOtra, Integer, ExpedienteModifPresPlazoOtraDAO>
        implements ExpedienteModifPresPlazoOtraService {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// DAO
    // ////////////////////////////////////////////////////////

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param ExpedienteModifPresPlazoOtraDAO
     */
    @Autowired
    public ExpedienteModifPresPlazoOtraServiceImpl(@Qualifier("expedienteModifPresPlazoOtraDAO")
    ExpedienteModifPresPlazoOtraDAO expedienteModifPresPlazoOtraDAO) {
        super();
        this.setDao(expedienteModifPresPlazoOtraDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteModifPresPlazoOtraServiceImpl() {
        super();
    }

    /**
     * Buscar por idExpedienteModifPresPlazoOtra
     *
     * @param idExpedienteModifPresPlazoOtra
     * @return ExpedienteModifPresPlazoOtraDTO
     * @throws ServiceException the service exception
     */
    @Override
    public ExpedienteModifPresPlazoOtraDTO buscarId(Integer idExpedienteModifPresPlazoOtra)
            throws ServiceException {

        return ExpedienteModifPresPlazoOtraToDTO.INSTANCE
                .apply(this.find(idExpedienteModifPresPlazoOtra));
    }

    /**
     * Buscar paginadamente ExpedienteModifPresPlazoOtra.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteModifPresPlazoOtraDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteModifPresPlazoOtraDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {

        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteModifPresPlazoOtraToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteModifPresPlazoOtraDTO guardar(
            ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO)
            throws ServiceException {

        ExpedienteModifPresPlazoOtra expedienteMod = ExpedienteModifPresPlazoOtraToEntity.INSTANCE
                .apply(expedienteModifPresPlazoOtraDTO);

        return ExpedienteModifPresPlazoOtraToDTO.INSTANCE.apply(this.insertOrUpdate(expedienteMod));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final Integer idExpedienteModifPresPlazoOtra)
            throws ServiceException {
        this.deleteLogico(idExpedienteModifPresPlazoOtra);
    }

    /**
     *
     * @param ExpedienteModifPresPlazoOtraDTO
     * @throws ServiceException
     */
    private void validarCrearExpedienteModif(
            ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO)
            throws ServiceException {
        if (Objects.isNull(expedienteModifPresPlazoOtraDTO)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteModifPresPlazoOtraDTO crear(
            ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO)
            throws ServiceException {

        // Validaciones entrada
        this.validarCrearExpedienteModif(expedienteModifPresPlazoOtraDTO);

        // falta meter la logica aqui....

        // Auditoria
        // expedienteModifPresPlazoOtraDTO.setCdUsuAlta(expedienteModifPresPlazoOtraDTO.getInteresadoDTO().getCdNumIden());
        expedienteModifPresPlazoOtraDTO.setFcAltaFila(new Date());

        return expedienteModifPresPlazoOtraDTO;
    }

    /**
     * Método para buscar ExpedienteModifPresPlazoOtraDTO por el id de
     * expediente
     * 
     * @param idExpediente id del expediente
     */
    @Override
    public List<ExpedienteModifPresPlazoOtraDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_IDMODIFICACION, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteModifPresPlazoOtraToDTO.INSTANCE));
    }

    @Override
    public List<ExpedienteModifPresPlazoOtraDTO> buscarPorExpedienteEstado(Integer idExpediente,
            Integer idEstado) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_MODIF_PRESPLAZO_OTRA_ID_ESTADO,
                utilConstantes.getGlobalCatalogosValores().get(idEstado), Operador.EQUALS));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_IDMODIFICACION, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteModifPresPlazoOtraToDTO.INSTANCE));
    }

    @Override
    public List<ExpedienteModifPresPlazoOtraDTO> buscarPorExpedienteEstadoAbiertas(
            Integer idExpediente) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_REVISION_SUBSANACION));
        // VMM 05/10/26 MD-3066: se quitan PENDIENTE_PORTAFIRMAS y ENVIADA_PORTAFIRMAS - una vez enviada a la
        // cola de portafirmas el gestor no tiene nada mas que hacer en la solapa, que no debe salir en
        // rojo (esta lista solo la usa ExpedienteModifPresPlazoOtraBean.validarEstilo para el color).
        // VMM 06/10/26 MD-3066: la solapa debe seguir en rojo hasta que el gestor envia la resolucion a
        // la cola de portafirmas (boton amarillo): tras "Finalizar revision" (PEDIENTE_MODIFICACIONES) y
        // tras subir la resolucion (PENDIENTE_ENVIO_COLA). Ojo: PENDIENTE_ENVIO_COLA es tambien el estado
        // al que vuelve el batch tras la firma, cuando ya NO hay nada que hacer - ahi la distincion (si
        // ya existe el envio en SAII_DOCUMENTO_PTFR) la hace el llamante, ver validarEstilo.
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE_MODIFICACIONES));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_ENVIO_COLA));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_MODIF_PRESPLAZO_OTRA_ID_ESTADO,
                lstEstados, Operador.IN));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_IDMODIFICACION, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteModifPresPlazoOtraToDTO.INSTANCE));
    }

}
