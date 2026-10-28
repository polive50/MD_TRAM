package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPaginacionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.ResponseDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Convocatoria
 *
 * @author everis
 */
public interface ConvocatoriaService extends BaseService<Convocatoria, Integer, ConvocatoriaDAO> {

    /**
     * Buscar por id idr.
     *
     * @param id the id Convocatoria
     * @return ConvocatoriaDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDTO buscarId(final Integer id) throws ServiceException;

    /**
     * Buscar por acronimo.
     *
     * @param cdAcronimo the cdAcronimo
     * @return List<ConvocatoriaDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDTO> buscarPorAcronimo(final String cdAcronimo) throws ServiceException;

    /**
     * Buscar paginadamente Convocatorias.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @param cdUsuarioGestor
     * @return List<ConvocatoriaDTO>
     * @throws ServiceException the service exception
     */
    ConvocatoriaPaginacionDTO buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden,
            final String cdUsuarioGestor) throws ServiceException;

    /**
     * Buscar convocatorias activas.
     *
     * @throws ServiceException
     */

    List<ConvocatoriaDTO> buscarConvocatoriasActivas() throws ServiceException;

    /**
     * Guarda un/a Convocatorias.
     *
     * @param ConvocatoriaDTO the Convocatorias DTO
     * @return ConvocatoriaDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDTO guardar(final ConvocatoriaDTO convocatoriaDTO) throws ServiceException;

    /**
     * Método para guardar los datos de la convocatoria en base a otra existente
     *
     * @param convocatoriaDTO Datos de la convocatoria
     * @param impresoDTO Datos del impreso
     * @param idConvocatoriaExistente Identificador de convocatoria existente
     * @return ConvocatoriaDTO Datos de la convocatoria
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    ConvocatoriaDTO guardarBaseOtraConvocatoria(final ConvocatoriaDTO convocatoriaDTO,
            final Integer idConvocatoriaExistente) throws ServiceException;

    /**
     * Comprueba si una convocatoria es accesible para el tipo de actor y
     * persona
     *
     * @param idConvocatoria
     * @param idTipoIdentificacion
     * @param idTipoActor
     * @return ResponseDTO (Resultado / Codigo / descripcion error) Resultado =
     *         true -> OK
     * @throws ServiceException
     */
    ResponseDTO comprobarTipoActor(final Integer idConvocatoria, final Integer idTipoIdentificacion,
            final Integer idTipoActor) throws ServiceException;

    /**
     * Actualiza la fecha de modificacion El usuario logado.
     *
     * @param idConvocatoria
     * @throws ServiceException
     */
    void actualizarAuditoria(final Integer idConvocatoria) throws ServiceException;

    /**
     * Método para validar las fechas de presentación configuradas por
     * convocatorias
     *
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    void procesarConvocatoriasBatch() throws ServiceException;

    /**
     * Método para validar si los documentos fueron subsanados dentro del plazo
     * configurado por convocatoria
     *
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    void consultarDocumentoPlazoSubsanacionBatch() throws ServiceException;

    /**
     * Método para validar si los documentos de JCT fueron subsanados dentro del plazo
     * configurado por convocatoria
     *
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    void consultarDocumentoPlazoSubsanacionJCTBatch() throws ServiceException;
    
    /**
     * Método para validar si los documentos de CONTRATOS fueron subsanados dentro del plazo
     * configurado por convocatoria
     *
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    void consultarDocumentoPlazoSubsanacionContratosBatch() throws ServiceException;

    /**
     * VMM 28/10/26 MD-3159: si el plazo de subsanación del documento ya ha vencido, lo pasa a "Plazo de
     * subsanación vencido" y actualiza la revisión del expediente. Se ejecuta en su propia transacción
     * (REQUIRES_NEW), para que un fallo en un documento no afecte al resto del batch.
     *
     * @param idExpedienteDoc Identificador del documento del expediente
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    void procesarPlazoSubsanacionDocumentoBatch(Integer idExpedienteDoc) throws ServiceException;
    
    
    /**
     * Buscar convocatorias tramitacion.
     *
     * @param index
     * @param pageSize
     * @param filtros
     * @param orden
     * @param cdUsuarioGestor
     * @return
     * @throws ServiceException
     */
    ConvocatoriaPaginacionDTO buscarConvocatoriasTramitacion(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden, String cdUsuarioGestor)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param conRequisitos
     */
    ConvocatoriaDTO actualizarIndicadorRequisito(Integer idConvocatoria, Boolean conRequisitos)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     *
     */
    Boolean validarDescargaListados(Integer idConvocatoria) throws ServiceException;

    /**
     * Comprueba que todos los expedientes que no están dados de baja tengan su
     * estado tramite evaluación (ID_ESTADO_REV_EVA) en “Evaluado KO o Evaluado
     * OK” y su estado interno no sea “Desistido” (ID_ESTADO_REV_ADMI),
     * “Excluido” (ID_ESTADO_REV_REQUI) o “Renuncia expresa” (ID_ESTADO)
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    Boolean esPuedeSubirPropuestaResProvisional(Integer idConvocatoria) throws ServiceException;

    /**
     * Este método actualiza el estado de la convocatoria a “Resuelta”. Pero
     * antes de realizar el cambio de estado comprueba que todos los expedientes
     * de la convocatoria estén en un estado final
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    //ConvocatoriaDTO resolver(Integer idConvocatoria) throws ServiceException;
    
    /**
     * Método que genera el documento de alegaciones según sea configurado en la convocatoriia
     * 
     * @param ConvocatoriaDTO the Convocatorias DTO
     * @Param void
     * @throws ServiceException
     */
    List<ConvocatoriaTipoDocDTO> alegaciones(ConvocatoriaDTO convocatoriaDTO) throws ServiceException;

    /**
     * Guarda NmDiasAmpliContrato en un/a Convocatorias.
     *
     * @param ConvocatoriaDTO the Convocatorias DTO
     * @return ConvocatoriaDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDTO guardarNmDiasAmpliContrato(final ConvocatoriaDTO convocatoriaDTO) throws ServiceException;

    /**
     * Guardar checks de financiacion en la convocaotoria
     *
     * @param ConvocatoriaDTO the Convocatorias DTO
     * @return the Convocatorias DTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDTO guardarChecksFinanConvocatoria(ConvocatoriaDTO convocatoriaDTO)
            throws ServiceException;
    
    /*
     * Guardar checks de ayuda solicitada
     */
    ConvocatoriaDTO guardarChecksAyudaSolicitada(ConvocatoriaDTO convocatoriaDTO)
            throws ServiceException;

    List<ConvocatoriaDTO> buscarConvocatoriasTramitacion(
            List<ConvocatoriaDTO> listaConvocatoriaPaginada);

 }
