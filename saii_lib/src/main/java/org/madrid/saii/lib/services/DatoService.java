package org.madrid.saii.lib.services;

import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.dao.DatoDAO;
import org.madrid.saii.lib.domain.Dato;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Datos
 *
 * @author everis
 */
public interface DatoService extends BaseService<Dato, Integer, DatoDAO> {

    /**
     * Guarda un/a Datos.
     *
     * @param DatoDTO the Datos DTO
     * @return DatosDTO
     * @throws ServiceException the service exception
     */
    DatoDTO guardar(final DatoDTO convocatoriaDTO) throws ServiceException;

    /**
     * Buscar paginadamente Datos.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaDTO>
     * @throws ServiceException the service exception
     */
    List<DatoDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar por id idDatos.
     *
     * @param idDatos the id Datos
     * @return DatosDTO
     * @throws ServiceException the service exception
     */
    DatoDTO buscarIdDatos(final Integer idValor) throws ServiceException;

    /**
     * Obtiene todos los datos que estén dentro del expediente
     *
     * @param idExpediente the id Expediente
     * @param idBloqueComponente de id BloqueComponente
     * @return List<DatoDTO>
     * @throws ServiceException
     */
    List<DatoDTO> obtenerPorExpediente(Integer idExpediente, Integer idBloqueComponente)
            throws ServiceException;

    /**
     * Borrado logico dato
     *
     * @param idDato
     * @throws ServiceException
     */
    void borradoLogico(final Integer idDato) throws ServiceException;

    /**
     *
     * @param idExpediente
     * @param idBloque
     * @param cdNumIden
     */
    @Transactional(readOnly = false)
    void bajaDatosPorBloqueComponente(Integer idExpediente, Integer idBloque, Integer nmFila,
            String cdNumIden) throws ServiceException;

    Map<String, String> obtenerValoresDatos(Integer idExpediente, List<Integer> lstIdComponente,
            List<Integer> lstIdTipoFinanciacion) throws ServiceException;

    List<DatoDTO> consultarDatosImpresoModifPorIdExpModif(Integer idExpediente,
            Integer idExpModifPresPlazoOtra, Integer idBloqueComponente, boolean ordenAsc) throws ServiceException;

    void rellenarDatosMultiregistro(ExpedienteDTO dto, BloqueDTO bloqueDTO,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException;

    Integer obtenerNmRegistrosBloque(BloqueDTO bloqueDTO, Integer nmFilasTabla) throws ServiceException;

    Integer obtenerFilasTabla(BloqueDTO bloqueDTO) throws ServiceException;

    List<DatoDTO> buscarDatosByIdExpAndIdBloComp(Integer idExpediente, Integer idBloqueComponente, boolean ordenar) throws ServiceException;

    /**
     * Método para recuperar el Dato por idExpediente y IdComponente 
     * @param idExpediente
     * @param idComponente
     * @return
     */
    DatoDTO obtenerPorIdExpedienteYIdComponente(Integer idExpediente, Integer idComponente) throws ServiceException;

}
