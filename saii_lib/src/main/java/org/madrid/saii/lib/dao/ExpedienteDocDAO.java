
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;

import atlas.busquedas.domain.AtlasSort;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteDoc
 *
 * @author everis
 */
public interface ExpedienteDocDAO extends BaseDAO<ExpedienteDoc, Integer> {

    public List<ExpedienteDoc> findByCdAnexoEreg(String cdAnexoEreg);

    List<ExpedienteDocDTO> listExpedienteDocContratos(Integer idExpediente) ;

    List<ExpedienteDocDTO> listExpedienteDocContratosPorIdActor(Integer idActor, Boolean subsanar,
            Integer tramite);

    List<ExpedienteDocDTO> listExpedienteDocContratosPorTramite(Integer idExpediente,
            Integer tramite);

    public List<ExpedienteDocDTO> listExpedienteDocContratosSubsanadoPorTramite(
            Integer idExpediente) ;

    public List<Expediente[]> listExpedientePaticipantesContratosPorIdActor(Integer idActor);

    List<ExpedienteDocDTO> listObtenerContratos(Integer idExpediente, Integer idTramite);

    List<ExpedienteDocDTO> listExpedienteDocContratosPorIdPadre(Integer idExpedientePadre,
            Integer tramite);

    List<ExpedienteDocDTO> buscarPorExpedienteJustecYTramite(Integer idExpedienteJusTec,
            List<Integer> idTramite);

    List<ExpedienteContrato> listExpedienteContratosPaticipantesPorIdActor(Integer idActor,
            Integer idExpediente) ;

    List<ExpedienteDoc> listExpedienteContratosDocPaticipantesPorIdActor(Integer idActor,
            Integer idExpediente) ;

    public List<ExpedienteDocDTO> consultarExpDocByDatoNmFila(Integer idExpediente);


    List<ExpedienteDocDTO> buscarDocumentos(Integer index, Integer pageSize, Integer idSolicitud,
            List<Integer> lstTramites, Integer idExpediente, List<AtlasSort> orden);

}
