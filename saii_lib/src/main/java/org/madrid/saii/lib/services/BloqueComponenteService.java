package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.BloqueComponenteDAO;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio BloqueComponente
 *
 * @author everis
 */
public interface BloqueComponenteService
        extends BaseService<BloqueComponente, Integer, BloqueComponenteDAO> {

    /**
     * Buscar por id idBloqueComponente.
     *
     * @param idValor the id Componente
     * @return ComponenteDTO
     * @throws ServiceException the service exception
     */
    BloqueComponenteDTO buscarId(final Integer idBloqueComponente) throws ServiceException;

    /**
     * Busca por id Bloque
     *
     * @param idBloque
     * @return List<BloqueComponenteDTO>
     * @throws ServiceException the service exception
     */
    List<BloqueComponenteDTO> buscarIdBloque(final Integer idBloque) throws ServiceException;

    /**
     * Busca por id Bloque e id Componente
     *
     * @param idBloque
     * @return List<BloqueComponenteDTO>
     * @throws ServiceException the service exception
     */
    BloqueComponenteDTO buscarPorBloqueComponente(final Integer idBloque,
            final Integer idComponente, final Integer idTramite) throws ServiceException;

    /**
     * Guarda un/a Componentes.
     *
     * @param ComponenteDTO the Componente DTO
     * @return ComponenteDTO
     * @throws ServiceException the service exception
     */
    BloqueComponenteDTO guardar(final BloqueComponenteDTO bloqueComponenteDTO)
            throws ServiceException;

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
    List<BloqueComponenteDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Método para buscar componente asociado a algún bloque
     *
     * @param idComponente Identificador de componente
     * @return true: si no se está utilizando el componente en el bloque.
     * @throws ServiceException the service exception
     */
    public Boolean existeComponenteEnBloque(final Integer idComponente) throws ServiceException;

    /**
     * Método que Verifica si existe un tipo de documento en alta asociado a por
     * lo menos un bloque componente
     *
     * @param idTipoDocumento: Identificador de Tipo de Documento
     * @return true: true: si existe en algún bloque componente
     * @throws ServiceException the service exception
     */
    public Boolean existePorTipoDocumento(Integer idTipoDocumento) throws ServiceException;

    /**
     * Metodo para obtener bloques componente por expediente y/o bloque
     *
     * @param idExpediente
     * @param idFormatoBloque
     * @return
     * @throws ServiceException
     */
    public List<BloqueComponenteDTO> obtenerPorExpediente(Integer idExpediente,
            Integer idFormatoBloque) throws ServiceException;

    /**
     *
     * @param financiacionDTO
     * @throws ServiceException
     */
    public void guardarFilaColumnaFinanciacion(FinanciacionDTO financiacionDTO, BloqueDTO bloqueDTO)
            throws ServiceException;
    
    /**
     * 
     * @param lstConvocatoriaAnualidadDTO
     * @param bloqueDTO
     * @throws ServiceException
     */
    public void guardarFilaColumnaPlazosEjecucionAnualidades(
            List<ConvocatoriaAnualidadDTO> lstConvocatoriaAnualidadDTO, BloqueDTO bloqueDTO)
            throws ServiceException;
    
    
    /**
     * Busca por id Bloque y fecha de baja a null
     *
     * @param idBloque
     * @return List<BloqueComponenteDTO>
     * @throws ServiceException the service exception
     */
    List<BloqueComponenteDTO> buscarIdBloqueFechBaja(final Integer idBloque) throws ServiceException;


}
