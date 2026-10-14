package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedienteActua;
import org.madrid.saii.lib.domain.ExpedienteActuaHist;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteActuaHist
 *
 * @author nttdata
 */
@Repository("expedienteActuaHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteActuaHistDAOImpl extends BaseDAOImpl<ExpedienteActuaHist, Integer>
		implements ExpedienteActuaHistDAO {

	@Override
	public void crearHistorico(ExpedienteActua expedienteActua) {
		ExpedienteActuaHist expedienteActuaHist = new ExpedienteActuaHist();
		expedienteActuaHist.setIdExpediente(expedienteActua.getidExpediente());
		expedienteActuaHist.setIdExpedienteActua(expedienteActua.getIdExpedienteActua());
		expedienteActuaHist.setNmImporte(expedienteActua.getNmImporte());
		expedienteActuaHist.setDsProveedor(expedienteActua.getDsProveedor());
		expedienteActuaHist.setTlObservacion(expedienteActua.getTlObservacion());
		expedienteActuaHist.setNmAyuda(expedienteActua.getNmAyuda());

		CatalogosValores catal = new CatalogosValores(expedienteActua.getTipoFinanciacion().getIdValor());
		expedienteActuaHist.setTipoFinanciacion(catal);

		if (Objects.nonNull(expedienteActua.getFcModifFila())) {
			expedienteActuaHist.setFcAltaFila(expedienteActua.getFcModifFila());
		} else {
			expedienteActuaHist.setFcAltaFila(expedienteActua.getFcAltaFila());
		}

		this.insert(expedienteActuaHist);

	}

	/**
	*
	*/
	@Override
	@SuppressWarnings("unchecked")
	public List<ExpedienteActuaHistDTO> buscarPorExpediente(Integer idExpediente) {
		StringBuilder queryHql = new StringBuilder();

		queryHql.append("SELECT distinct ");
		queryHql.append(" ea.fcAltaFila, ");
		queryHql.append(" ea.tlObservacion ");
		queryHql.append(" FROM ExpedienteActuaHist ea");
		queryHql.append(" where ea.idExpediente= :idExpediente");
		queryHql.append(" AND ea.fcBajaFila IS NULL");
		queryHql.append(" order by ea.fcAltaFila desc");

		Map<String, Object> params = new HashMap<>();
		params.put("idExpediente", idExpediente);
		List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(), params);

		Iterator<?> it = executeHQLQueryList.iterator();

		List<ExpedienteActuaHistDTO> listExpActuaHist = new ArrayList<>();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			ExpedienteActuaHistDTO expedienteActuaHist = new ExpedienteActuaHistDTO();
			expedienteActuaHist.setTlObservacion(Objects.nonNull(object[1]) ? String.valueOf(object[1]) : "");
			expedienteActuaHist.setFcAltaFila((Date) object[0]);
			listExpActuaHist.add(expedienteActuaHist);

		}
		return listExpActuaHist;
	}

}
