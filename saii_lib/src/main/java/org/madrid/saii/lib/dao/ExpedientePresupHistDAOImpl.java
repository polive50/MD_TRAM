package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.domain.ExpedientePresupHist;
import org.madrid.saii.lib.dto.ExpedientePresupHistDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedientePresupHist
 *
 * @author nttdata
 */
@Repository("expedientePresupHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedientePresupHistDAOImpl extends BaseDAOImpl<ExpedientePresupHist, Integer>
        implements ExpedientePresupHistDAO {

    @Override
    public void crearHistorico(ExpedientePresup expedientePresup) {
        ExpedientePresupHist expedientePresupHist = new ExpedientePresupHist();
        expedientePresupHist.setIdExpedientePresup(expedientePresup.getIdExpedientePresup());
        expedientePresupHist.setIdExpediente(expedientePresup.getIdExpediente());
        expedientePresupHist.setTipoFinanciacion(expedientePresup.getTipoFinanciacion());
        expedientePresupHist.setTematica(expedientePresup.getTematica());
        expedientePresupHist.setTlObservacion(expedientePresup.getTlObservacion());
        expedientePresupHist.setNmAyuda(expedientePresup.getNmAyuda());
        expedientePresupHist.setNmPresup(expedientePresup.getNmPresup());
        expedientePresupHist.setEstado(expedientePresup.getEstado());
        expedientePresupHist.setTlConcepto(expedientePresup.getTlConcepto());
        expedientePresupHist.setItNuevoConceptoFinan(expedientePresup.getItNuevoConceptoFinan());
        expedientePresupHist.setNmPorcentajeIntensidad(expedientePresup.getNmPorcentajeIntensidad());

        if (Objects.nonNull(expedientePresup.getFcModifFila())) {
            expedientePresupHist.setFcAltaFila(expedientePresup.getFcModifFila());
        } else {
            expedientePresupHist.setFcAltaFila(expedientePresup.getFcAltaFila());
        }

        // VMM 24/09/26 MD-2725: CD_USU_ALTA es NOT NULL en BD y aqui no se estaba informando nunca -
        // mismo problema y mismo fix que en ExpedientePresMontHistDAOImpl.crearHistorico.
        if (Objects.nonNull(expedientePresup.getCdUsuModif())) {
            expedientePresupHist.setCdUsuAlta(expedientePresup.getCdUsuModif());
        } else if (Objects.nonNull(expedientePresup.getCdUsuAlta())) {
            expedientePresupHist.setCdUsuAlta(expedientePresup.getCdUsuAlta());
        } else {
            expedientePresupHist.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
        }

        this.insert(expedientePresupHist);
    }

    /**
    *
    */
    @Override
    @SuppressWarnings("unchecked")
    public List<ExpedientePresupHistDTO> buscarPorExpediente(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT distinct ");
        queryHql.append(" ep.fcAltaFila, ");
        queryHql.append(" ep.tlObservacion ");
        queryHql.append(" FROM ExpedientePresupHist ep");
        queryHql.append(" where ep.idExpediente= :idExpediente");
        queryHql.append(" AND ep.fcBajaFila IS NULL");
        queryHql.append(" order by ep.fcAltaFila desc");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        List<ExpedientePresupHistDTO> listExpPresupHist = new ArrayList<>();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
            ExpedientePresupHistDTO expedientePresupHist = new ExpedientePresupHistDTO();
            expedientePresupHist
                    .setTlObservacion(Objects.nonNull(object[1]) ? String.valueOf(object[1]) : "");
            expedientePresupHist.setFcAltaFila((Date) object[0]);
            listExpPresupHist.add(expedientePresupHist);

        }
        return listExpPresupHist;
    }

}
