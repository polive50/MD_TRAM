package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEvalJustecDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEvalJustec;
import org.madrid.saii.lib.dto.ConvocatoriaEvalJustecDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaEvalJustec
 *
 * @author Quenatal
 */
public interface ConvocatoriaEvalJustecService
        extends BaseService<ConvocatoriaEvalJustec, Integer, ConvocatoriaEvalJustecDAO> {

    /**
     * Buscar todos los registros de ConvocatoriaCoorEval.
     *
     * @return List<ConvocatoriaCoorEvalDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaEvalJustecDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ConvocatoriaEvalJustec.
     *
     * @param idConvocatoriaEvalJustec the idConvocatoriaEvalJustec
     * @return ConvocatoriaEvalJustecDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaEvalJustecDTO buscarId(Integer idConvocatoriaEvalJustec) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaEvalJustec.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaEvalJustecDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaEvalJustecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaEvalJustec.
     *
     * @param convocatoriaEvalJustecDTO the convocatoriaEvalJustec DTO
     * @return convocatoriaEvalJustecDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaEvalJustecDTO guardar(ConvocatoriaEvalJustecDTO convocatoriaEvalJustecDTO)
            throws ServiceException;

    /**
     * Borrado logico ConvocatoriaCoorEval
     *
     * @param ConvocatoriaEvalJustecDTO
     * @return ConvocatoriaEvalJustecDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaEvalJustecDTO convocatoriaevaljustecDTO)
            throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriaevaljustecDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaEvalJustecDTO convocatoriaevaljustecDTO) throws ServiceException;

    /**
     * Buscar por id Evaluador.
     *
     * @param idEvaluador the idEvaluador
     * @return ConvocatoriaEvalJustecDTO
     * @throws ServiceException the service exception
     */
     Boolean buscarByIdEvaluadorExist(Integer idEvaluador, Integer idConvocatoria) throws ServiceException;
     
     
     List<ConvocatoriaEvalJustecDTO> buscarEvaluadoresJCT(final int index, final int pageSize,
             final List<AtlasFilter> filtros, final List<AtlasSort> orden, Integer idArea, List<ConvocatoriaEvalJustecDTO> listaEvalJustecDTO)
             throws ServiceException;
     
     List<ConvocatoriaEvalJustecDTO> retornarPaginado(List<ConvocatoriaEvalJustecDTO> listaEvaluadoresJCTFiltrada, int index,
             int pageSize);
     
     List<ConvocatoriaEvalJustecDTO> buscarPersona(final int index, final int pageSize,final List<AtlasFilter> filtros, final List<AtlasSort> orden, Integer idArea)
             throws ServiceException;
     
}
