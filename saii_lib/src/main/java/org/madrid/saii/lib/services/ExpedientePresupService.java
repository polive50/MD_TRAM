package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedientePresupDAO;
import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteFin
 *
 * @author nttdata
 */
public interface ExpedientePresupService
        extends BaseService<ExpedientePresup, Integer, ExpedientePresupDAO> {

    /**
     * Buscar todos los registros de ExpedienteFin.
     *
     * @return List<ExpedienteFinDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedientePresupDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ExpedienteFin.
     *
     * @param idExpedienteFin the id ExpedienteFin
     * @return ExpedienteFinDTO
     * @throws ServiceException the service exception
     */
    ExpedientePresupDTO buscarId(Integer idExpedienteFin) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteFin.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteFinDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedientePresupDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a expedienteFin.
     * @param expedienteBean 
     *
     * @param expedientePresupDTO the expedienteFin DTO
     * @return expedienteFinDTO
     * @throws ServiceException the service exception
     */
    void guardar(List<ExpedientePresupDTO> presupuestos, Double montoAyuda, ExpedienteDTO expedienteBean)
            throws ServiceException;

    /**
     * Borrado logico ExpedienteFin
     *
     * @param ExpedientePresupDTO
     * @return ExpedienteFinDTO
     * @throws ServiceException
     */
    void borradoLogico(final ExpedientePresupDTO expedientefinDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param expedientefinDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ExpedientePresupDTO expedientefinDTO) throws ServiceException;

    void guardarFinanciacion(List<BloqueComponenteDTO> bloquesSinTotal, Double montoAyuda,
            Integer nmPresup, Integer idExpediente, Integer idTematica, Double intensidadSol) throws ServiceException;
    
    void guardarFinanciacionConcepto(List<BloqueComponenteDTO> bloquesSinTotal, Double montoAyuda,
            Integer nmPresup, Integer idExpediente, Integer idTematica, Double intensidadSol) throws ServiceException;

    List<ExpedientePresupDTO> buscarPresupIncialExpediente(Integer idExpediente)throws ServiceException;

    List<ExpedientePresupDTO> consultarExpPresupFinanPorIdExp(Integer idExpediente,
            Integer nmPresup) throws ServiceException;

    List<ExpedientePresupDTO> bucarPresupByIdExpAndNmPresup(Integer idExpediente, Integer nmPresup)
            throws ServiceException;

    void actualizarConceptoObservac(ExpedientePresupDTO expedientePresupDTO) throws ServiceException;

    void eliminarExpPresup(ExpedientePresupDTO expPresupDTO);

    ExpedientePresupDTO crearNuevoConceptoPadre(ExpedientePresupDTO expPresupDTO)
            throws ServiceException;

    void actualizarIntensidadAyuda(Integer idExpediente, Integer nmPresup, Double nmPorcentajeIntensidad,
            Double montoAyuda) throws ServiceException;

    /**
     * Método poara validar que existen cambios por guardar
     * 
     * @param lstBloque
     * @param lstPresupuestoFinanciable1
     * @param lstPresupuestoFinanciable2
     * @param montoAyuda
     * @return
     */
    Boolean existeModificacionesParaGuardar(List<BloqueDTO> lstBloque,
            List<ExpedientePresupDTO> lstPresupuestoFinanciable1,
            List<ExpedientePresupDTO> lstPresupuestoFinanciable2, Double montoAyuda);

}
