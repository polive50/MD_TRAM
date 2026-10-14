package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.RevisionInfEvaJustecDAO;
import org.madrid.saii.lib.dao.RevisionInfEvaJustecHistDAO;
import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecDTO;
import org.madrid.saii.lib.dto.converters.RevisionInfEvaJustecToDTO;
import org.madrid.saii.lib.dto.converters.RevisionInfEvaJustecToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * @author quental
 */
@Service("revisionInfEvaJustecService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class RevisionInfEvaJustecServiceImpl
        extends BaseServiceImpl<RevisionInfEvaJustec, Integer, RevisionInfEvaJustecDAO>
        implements RevisionInfEvaJustecService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param RevisionInfEvaJustecDAO
     */
    @Autowired
    public RevisionInfEvaJustecServiceImpl(
            @Qualifier("revisionInfEvaJustecDAO") RevisionInfEvaJustecDAO revisionInfEvaJustecDAO) {
        super();
        this.setDao(revisionInfEvaJustecDAO);
    }

    @Autowired
    @Qualifier("revisionInfEvaJustecHistDAO")
    private RevisionInfEvaJustecHistDAO revisionInfEvaJustecHistDAO;

    /**
     *
     */
    @Override
    public List<RevisionInfEvaJustecDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                RevisionInfEvaJustecToDTO.INSTANCE));
    }

    /**
     * Metodo para insertar o actualizar los datos editados de los registros
     * ConvocatoriaJusTec, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public RevisionInfEvaJustecDTO guardar(RevisionInfEvaJustecDTO revisionInfEvaJustecDTO)
            throws ServiceException {

        RevisionInfEvaJustec revisionInfEvaJustec = this
                .insertOrUpdate(RevisionInfEvaJustecToEntity.INSTANCE.apply(revisionInfEvaJustecDTO));

        this.revisionInfEvaJustecHistDAO.crearHistorico(revisionInfEvaJustec);

        return RevisionInfEvaJustecToDTO.INSTANCE.apply(revisionInfEvaJustec);
    }

    /**
    *
    */
    @Override
    public List<RevisionInfEvaJustecDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                RevisionInfEvaJustecToDTO.INSTANCE));
    }

}
