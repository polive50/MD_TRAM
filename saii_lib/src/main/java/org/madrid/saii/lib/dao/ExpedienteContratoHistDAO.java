package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;

public interface ExpedienteContratoHistDAO extends BaseDAO<ExpedienteContratoHist, Integer> {

    ExpedienteContratoHist crearHistorico(ExpedienteContrato expedienteContrato, String cdUsuAlta);
}
