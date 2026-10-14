package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.RepresentanteJur;
import org.madrid.saii.lib.dto.RepresentanteJurFiltrosDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio RepresentanteJur
 *
 * @author everis
 */
@Repository("representanteJurDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class RepresentanteJurDAOImpl extends BaseDAOImpl<RepresentanteJur, Integer>
        implements RepresentanteJurDAO {

    /**
    *
    */
    @SuppressWarnings("unchecked")
    @Override
    public List<RepresentanteJur> filtrar(RepresentanteJurFiltrosDTO dto) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(
                "Select r from RepresentanteJur as r, Interesado as i1, Interesado i2, Entidad as e");
        queryHql.append(" where r.interesado.idInteresado = i1.idInteresado");
        queryHql.append(" and r.entidad.idEntidad = e.idEntidad");
        queryHql.append(" and e.interesado.idInteresado = i2.idInteresado");
        queryHql.append(" and upper(i2.cdNumIden) = upper(:cdNumIdenEntidad)");

        if (StringUtils.isNotBlank(dto.getCdNumIden())) {
            queryHql.append(" and upper(i1.cdNumIden) = upper(:cdNumIden)");
        }

        if (StringUtils.isNotBlank(dto.getDsNombre())) {
            queryHql.append(" and upper(i1.dsNombre) like upper(:dsNombre)");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido1())) {
            queryHql.append(" and upper(i1.dsApellido1) like upper(:dsApellido1)");
        }

        if (StringUtils.isNotBlank(dto.getDsApellido2())) {
            queryHql.append(" and upper(i1.dsApellido2) like upper(:dsApellido2)");
        }

        if (StringUtils.isNotBlank(dto.getDsEmail())) {
            queryHql.append(" and upper(i1.dsEmail) like upper(:dsEmail)");
        }

        if (StringUtils.isNotBlank(dto.getDsTelefono())) {
            queryHql.append(" and upper(i1.dsTelefono) = upper(:dsTelefono)");
        }

        if (dto.getFcVigenciaIni() != null) {
            queryHql.append(" and r.fcVigenciaIni >= :fcVigenciaIni");
        }

        if (dto.getFcVigenciaFin() != null) {
            queryHql.append(" and r.fcVigenciaFin <= :fcVigenciaFin");
        }

        if (dto.getEstadoDTO().getIdValor() != null) {
            queryHql.append(" and r.estado.idValor = :idEstado");
        }

        if (StringUtils.isNotBlank(dto.getDsCargo())) {
            queryHql.append(" and upper(r.dsCargo) like upper(:dsCargo)");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("cdNumIden", dto.getCdNumIden());
        params.put("dsNombre", "%" + dto.getDsNombre() + "%");
        params.put("dsApellido1", "%" + dto.getDsApellido1() + "%");
        params.put("dsApellido2", "%" + dto.getDsApellido2() + "%");
        params.put("dsEmail", "%" + dto.getDsEmail() + "%");
        params.put("dsTelefono", dto.getDsTelefono());
        params.put("idEstado", dto.getEstadoDTO().getIdValor());
        params.put("fcVigenciaIni", dto.getFcVigenciaIni());
        params.put("fcVigenciaFin", dto.getFcVigenciaFin());
        params.put("dsCargo", "%" + dto.getDsCargo() + "%");
        params.put("cdNumIdenEntidad", dto.getCdNumIdenEntidad());

        return (List<RepresentanteJur>) super.executeHQLQueryList(queryHql.toString(), params);
    }

}
