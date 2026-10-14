package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.domain.ExpedienteHist;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteHistToDTO;
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

/**
 * Implementación del servicio para la clase de dominio ExpedienteHist
 *
 * @author everis
 */
@Service("expedienteHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteHistServiceImpl
        extends BaseServiceImpl<ExpedienteHist, Integer, ExpedienteHistDAO>
        implements ExpedienteHistService {

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteHistDAO
     */
    @Autowired
    public ExpedienteHistServiceImpl(@Qualifier("expedienteHistDAO")
    ExpedienteHistDAO expedienteHistDAO) {
        super();
        this.setDao(expedienteHistDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteHistServiceImpl() {
        super();
    }

    /**
     * Buscar paginadamente Expediente.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteHistToDTO.INSTANCE));
    }

    @Override
    public ExpedienteHistDTO obtenerResolucionProvisional(Integer idExpediente)
            throws ServiceException {
        ExpedienteHistDTO expedienteHistDTO = new ExpedienteHistDTO();

        List<Integer> estadosExpediente = new ArrayList<>();
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL));
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, estadosExpediente,
                Operador.IN));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_HIST, false));

        List<ExpedienteHist> listaHistoricoProvisional = this.find(filtros, orden);
        if (!listaHistoricoProvisional.isEmpty()) {
            ExpedienteHist expedienteHist = listaHistoricoProvisional.get(0);
            expedienteHistDTO = ExpedienteHistToDTO.INSTANCE.apply(expedienteHist);
        }

        return expedienteHistDTO;
    }

    @Override
    public ExpedienteHistDTO obtenerResolucionDefinitiva(Integer idExpediente)
            throws ServiceException {
        ExpedienteHistDTO expedienteHistDTO = new ExpedienteHistDTO();

        List<Integer> estadosExpediente = new ArrayList<>();
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, estadosExpediente,
                Operador.IN));

        List<ExpedienteHist> listaHistoricoDefinitivo = this.find(filtros, null);
        if (!listaHistoricoDefinitivo.isEmpty()) {
            ExpedienteHist expedienteHist = listaHistoricoDefinitivo.get(0);
            expedienteHistDTO = ExpedienteHistToDTO.INSTANCE.apply(expedienteHist);
        }

        return expedienteHistDTO;
    }
    
//    @Override
//    public Boolean obtenerEstadoPendienteDeResolver(Integer idExpediente)
//            throws ServiceException {
//        Boolean existeEstado = Boolean.TRUE;
//
//        List<AtlasFilter> filtros = new ArrayList<>();
//
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
//                Operador.EQUALS));
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL),
//                Operador.EQUALS));
//
//        List<ExpedienteHist> listaHistoricoDefinitivo = this.find(filtros, null);
//        if (listaHistoricoDefinitivo.isEmpty()) {
//            existeEstado = Boolean.FALSE;
//        }
//
//        return existeEstado;
//    }
    
    

}
