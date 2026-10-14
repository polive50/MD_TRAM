package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ComponenteDAO;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Componente
 *
 * @author everis
 */
public interface ComponenteService extends BaseService<Componente, Integer, ComponenteDAO> {

    /**
     * Buscar por id.
     *
     * @param idValor the id Componente
     * @return ComponenteDTO
     * @throws ServiceException the service exception
     */
    ComponenteDTO buscarId(final Integer idComponente) throws ServiceException;

    /**
     * Guarda un/a Componentes.
     *
     * @param ComponenteDTO the Componente DTO
     * @return ComponenteDTO
     * @throws ServiceException the service exception
     */
    ComponenteDTO guardar(final ComponenteDTO componenteDTO) throws ServiceException;

    /**
     * Buscar paginadamente los Impresos.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ComponenteDTO>
     * @throws ServiceException the service exception
     */
    List<ComponenteDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un Componente y BloqueComponente para un tipo documento.
     *
     * @param Id Bloque
     * @param Id Tipo Documento
     * @return ComponenteDTO
     * @throws ServiceException the service exception
     */
    ComponenteDTO guardarDocumento(final Integer idBloque,
            final ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO,
            final List<BloqueComponenteDTO> listaBloqueComponente) throws ServiceException;

    /**
     * Buscar Componentes por tipo de documento
     *
     * @param ComponenteDTO the Componente DTO
     * @return ComponenteDTO
     * @throws ServiceException the service exception
     */
    Componente buscarPorTipoDocumento(final Integer idTipoDocumento) throws ServiceException;
    
//    /**
//     * Devuelve un listado de los registros con nmConstante
//     * 
//     * @return
//     * @throws ServiceException
//     */
//    List<Componente> cargaNmConstante() throws ServiceException;

}
