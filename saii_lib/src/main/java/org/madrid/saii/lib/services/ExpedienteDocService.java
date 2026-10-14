package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteDoc
 *
 * @author everis
 */
public interface ExpedienteDocService
        extends BaseService<ExpedienteDoc, Integer, ExpedienteDocDAO> {

    /**
     * Buscar paginadamente ExpedienteDoc.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    ExpedienteDocDTO buscarId(final Integer id) throws ServiceException;

    /**
     * Método que guarda un documento y crea su histórico. Usa el cdNumIden del
     * usuario logado (web)
     *
     * @param expedienteDocDTO a guardar
     * @return ExpedienteDocDTO guardado
     * @throws ServiceException
     */
    ExpedienteDocDTO guardarConHistorico(ExpedienteDocDTO expedienteDocDTO) throws ServiceException;

    /**
     * Método que guarda un documento y crea su histórico. Para su uso en rest
     *
     * @param expedienteDocDTO a guardar
     * @param cdUsuAlta del usuario
     * @return ExpedienteDocDTO guardado
     * @throws ServiceException
     */
    ExpedienteDocDTO guardarConHistorico(ExpedienteDocDTO expedienteDocDTO, String cdUsuAlta)
            throws ServiceException;

    /**
     * Guarda un documento de expediente externo
     *
     * @param expedienteDocDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteDocDTO guardarExterno(ExpedienteDocDTO expedienteDocDTO) throws ServiceException;

    /**
     * Guarda el expedienteDoc
     *
     * @param expedienteDocDTO
     * @return ExpedienteDocDTO
     * @throws ServiceException
     */
    ExpedienteDocDTO guardar(ExpedienteDocDTO expedienteDocDTO) throws ServiceException;

    /**
     * Buscar ExpedienteDoc de un Expediente
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    /**
     * Método para presentar una lista de expedienteDocDTO
     *
     * @param listaExpedienteDocDTO
     * @return RegistroEregDTO
     * @throws ServiceException
     */
    RegistroEregDTO presentarDocumentos(List<ExpedienteDocDTO> listaExpedienteDocDTO)
            throws ServiceException;

    /**
     * Sube un documento a Fica.
     *
     * @param documentoDTO the documento DTO
     * @return the expediente doc DTO
     * @throws ServiceException the service exception
     */
    ExpedienteDocDTO subir(final DocumentoDTO documentoDTO) throws ServiceException;

    /**
     *
     * @param idExpediente
     * @param idTipoDocumento
     * @param idTramite
     * @return
     * @throws ServiceException
     */
    ExpedienteDoc buscarPorExpedienteTipoDocumento(final Integer idExpediente,
            final Integer idTipoDocumento, final Integer idTramite) throws ServiceException;

    /**
    *
    * @param idExpediente
    * @param idTipoDocumento
    * @return
    * @throws ServiceException
    */
    List<ExpedienteDocDTO> buscarTodosPorExpedienteTipoDocumento(final Integer idExpediente,
            final Integer idTipoDocumento) throws ServiceException;

    
    /**
     * Guarda un contrato de expediente externo
     *
     * @param expedienteDocDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteDocDTO guardarContratoExterno(ExpedienteDocDTO expedienteDocDTO) throws ServiceException;

    /**
     * Actualiza un contrato de expediente externo
     *
     * @param expedienteDocDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteDocDTO actualizarContratoExterno(ExpedienteDocDTO expedienteDocDTO, String cdUsuAlta) throws ServiceException;

    /**
     * 
     * @param listaExpedienteDocDTO
     * @param idExpedienteJustec
     * @return
     * @throws ServiceException
     */
    RegistroEregDTO presentarDocumentosJusTec(Integer idExpedienteJustec, String cdNumIden, String dniLogin) throws ServiceException;
    
    /**
     * Método para presentar una lista de expedienteDocDTO
     * @param nifRepresentante 
     *
     * @param listaExpedienteDocDTO
     * @return RegistroEregDTO
     * @throws ServiceException
     */

    RegistroEregDTO presentarDocumentosContrato(ExpedienteDTO expedienteDTO)
            throws ServiceException;
    
    ExpedienteDoc buscarIdExpDoc(Integer id) throws ServiceException;

    /**
     * 
     * @param idExpedienteJustec
     * @param cdNumIden
     * @return
     */
    RegistroEregDTO presentarDocumentosJustecSubsanado(ExpedienteJusTecDTO idExpedienteJustec,
            String cdNumIden) throws ServiceException;
    
    /**
     * 
     * @param idExpediente
     * @return
     */
    List<ExpedienteDocDTO> obtenerDocumentosContrato(Integer idExpediente,Integer tramite) throws ServiceException;

    List<ExpedienteDocDTO> obtenerDocumentosSubsanarContrato(Integer idExpediente)
            throws ServiceException;

    /**
     * 
     * @param expedienteDocDTO
     * @return
     */
    ExpedienteDocDTO subirDocumentoFica(ExpedienteDocDTO expedienteDocDTO) throws ServiceException;
    
        
    /**
     * 
     * @param idExpediente
     * @param lstTipoTramite
     * @return
     * @throws ServiceException
     */
    Boolean existeTipoDocumentos(Integer idExpediente, List<Integer> lstTipoTramite)
            throws ServiceException;
    
    
    /**
     * 
     * @param idExpedienteJustec
     * @param idTramite
     * @return
     * @throws ServiceException
     */
    List<ExpedienteDocDTO> buscarPorExpedienteJustecYTramite(Integer idExpedienteJustec, List<Integer> idTramite)  throws ServiceException;

    List<ExpedienteDocDTO> obtenerDocumentosPresupPlazosOtros(Integer idExpediente,
            Integer tramite) throws ServiceException;

    RegistroEregDTO presentarModDocumentosPresupPlazosOtros(ExpedienteDTO expediente, String cdNumIden) throws ServiceException;

    RegistroEregDTO presentarSubsanacionReq(List<ExpedienteDTO> lstExpedienteDTO, String cdNumIden,
            String dniLogin, String cdNumIdenFuncional) throws ServiceException;
    
    
    /**
     * Devuelve el documento por cdFica.
     *
     * @param cdFica the cdFica
     * @return the ExpedienteDocDTO
     * @throws ServiceException the service exception
     */
    ExpedienteDocDTO descargar(Integer cdFica) throws ServiceException;
    
    
    /**
    *
    * @param id
    * @return
    * @throws ServiceException
    */
   ExpedienteDocDTO buscarPorIdExpTipoDoc(final Integer idExpediente, final Integer idTipoDocumento, final Integer idTramite) throws ServiceException;

   /**
    * Permite presentar subsanación administrativa de expedientes
    * @param lstExpedienteDTO
    * @param cdNumIden
    * @return
    * @throws ServiceException
    */
   RegistroEregDTO presentarSubsanacionAdm(List<ExpedienteDTO> lstExpedienteDTO, String cdNumIden, String dniLogin)
           throws ServiceException;

   void borradoLogico(Integer idExpedienteDoc) throws ServiceException;

   ExpedienteDocDTO guardarExternoSinDoc(ExpedienteDocDTO expedienteDocDTO) throws ServiceException;
 
   ExpedienteDocDTO guardarExternoExistente(ExpedienteDocDTO expedienteDocDTO, Integer idEstadoRevAdminExp) throws ServiceException;

   RegistroEregDTO presentarAceptacionDesistimientoAyuda(ExpedienteDTO expedienteDTO,
        String cdNumIden) throws ServiceException;

   RegistroEregDTO presentarJustificacionEconomica(
        List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO, String cdNumIden, String dniLogin) throws ServiceException;

List<ExpedienteDocDTO> buscarDocumentos(Integer index, Integer pageSize, Integer idSolicitud,
        List<Integer> lstTramites, Integer idExpediente, List<AtlasSort> orden);

List<ExpedienteModifPresPlazoOtraDTO> obtenerModifPresupPlazosOtros(Integer idExpediente) throws ServiceException;

ExpedienteDocDTO guardarDocumentoPresupuesto(ExpedienteDocDTO nuevoDocDTO, Integer idDato) throws ServiceException;

/**
 * Obtiene el nombre del fichero (FICA) del documento subido para un concepto
 * de presupuesto (dato), usado en la tabla de presupuesto del PDF de
 * modificaciones de presupuesto, plazos y otros.
 *
 * @param idDato identificador del dato (concepto) al que va ligado el documento
 * @return nombre del fichero, o null si no existe documento subido
 * @throws ServiceException the service exception
 */
String obtenerNombreDocumentoConceptoPresupuesto(Integer idDato) throws ServiceException;

/**
 * Obtiene los documentos subidos en los conceptos del presupuesto (detalle
 * concepto) para una modificación de presupuesto, plazos y otros, usado para
 * incluirlos en la descarga del zip/borrador de la modificación.
 *
 * @param idModificacion identificador de la modificación de presupuesto, plazos y otros
 * @return lista de ExpedienteDocDTO de los documentos de concepto subidos
 * @throws ServiceException the service exception
 */
List<ExpedienteDocDTO> obtenerDocumentosConceptoPresupuesto(Integer idModificacion) throws ServiceException;

}
