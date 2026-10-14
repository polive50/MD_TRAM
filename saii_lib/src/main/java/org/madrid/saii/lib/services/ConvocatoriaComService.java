package org.madrid.saii.lib.services;

import java.io.InputStream;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaComDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCom
 *
 * @author nttdata
 */
public interface ConvocatoriaComService
        extends BaseService<ConvocatoriaCom, Integer, ConvocatoriaComDAO> {

    /**
     * Buscar por id ConvocatoriaCom.
     *
     * @param idConvocatoriaCom the id ConvocatoriaCom
     * @return ConvocatoriaComDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaComDTO buscarId(Integer idConvocatoriaCom) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaCom.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaComDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaComDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaCom.
     *
     * @param convocatoriaComDTO the convocatoriaCom DTO
     * @return convocatoriaComDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaComDTO guardar(ConvocatoriaComDTO convocatoriaComDTO) throws ServiceException;

    /**
     * Borrado logico ConvocatoriaCom
     *
     * @param ConvocatoriaComDTO
     * @return ConvocatoriaComDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaComDTO convocatoriacomDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriacomDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaComDTO convocatoriacomDTO) throws ServiceException;

    /**
     * Carga las comunicaciones de una nueva convocatoria a partir de su id
     *
     * @param idConvocatoria
     * @throws ServiceException
     */
    void nuevaConvocatoria(Convocatoria convocatoria) throws ServiceException;

    /**
     * Envia las comunicaciones para el tramite/convocatoria dado
     *
     * @param idConvocatoria
     * @param idExpediente
     * @param idTipoTramite
     * @throws ServiceException
     */
    List<String> enviarComunicaciones(Integer idConvocatoria, Integer idExpediente, Integer idTipoTramite,
            Integer idPeticion, InputStream documentoFirmado) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param idTipoTramite
     * @return
     * @throws ServiceException
     */
    ConvocatoriaPlaDTO obtenerConvocatoriaPla(Integer idConvocatoria, Integer idTipoTramite)
            throws ServiceException;
    
    /**
     * Guarda un/a convocatoriaCom.
     *
     * @param convocatoriaComDTO the convocatoriaCom DTO
     * @return convocatoriaComDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaComDTO guardarComunicacion(ConvocatoriaComDTO convocatoriaComDTO) throws ServiceException;
    
    
    List<ConvocatoriaComDTO> buscarConvocatoriaComPorIdConvocatoriaIdConvocatoriaActNote(Integer idConvocatoria, Integer idActor) throws ServiceException;

    List<ConvocatoriaComDTO> buscarConvocatoriaComPorIdConvocatoriaIdConvocatoriaActCorreo(Integer idConvocatoria, Integer idActor) throws ServiceException;

    

}
