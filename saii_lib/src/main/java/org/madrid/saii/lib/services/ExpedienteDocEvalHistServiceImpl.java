package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteDocEvalHistDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.domain.ExpedienteDocEvalHist;
import org.madrid.saii.lib.domain.ExpedienteHist;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalHistDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteDocEvalHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocEvalToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalToEntity;

import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio ExpedienteDocEvalHist
 *
 * @author everis
 */
@Service("expedienteDocEvalHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteDocEvalHistServiceImpl
        extends BaseServiceImpl<ExpedienteDocEvalHist, Integer, ExpedienteDocEvalHistDAO>
        implements ExpedienteDocEvalHistService {

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param ExpedienteDocEvalHistDAO
     */
    @Autowired
    public ExpedienteDocEvalHistServiceImpl(@Qualifier("ExpedienteDocEvalHistDAO")
    ExpedienteDocEvalHistDAO expedienteDocEvalHistDAO) {
        super();
        this.setDao(expedienteDocEvalHistDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteDocEvalHistServiceImpl() {
        super();
    }

    /**
     * Buscar paginadamente Expediente.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDocEvalHistDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteDocEvalHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteDocEvalHistToDTO.INSTANCE));
    }

    @Override
    public ExpedienteDocEvalHist crearHistorico(ExpedienteDocEvalDTO expedienteDocEvalDTO)
            throws ServiceException {
        
        ExpedienteDocEvalHist expedienteDocEvalHist = new ExpedienteDocEvalHist();
        expedienteDocEvalHist.setCdFica(expedienteDocEvalDTO.getCdFica());
        expedienteDocEvalHist.setDsNombreFichero(expedienteDocEvalDTO.getDsNombreFichero());
        expedienteDocEvalHist.setExpedienteDocEval(ExpedienteDocEvalToEntity.INSTANCE.apply(expedienteDocEvalDTO));
        expedienteDocEvalHist.setIdTipoDocumento(CatalogosValoresToEntity.INSTANCE.apply(expedienteDocEvalDTO.getIdTipoDocumento()));
        expedienteDocEvalHist.setIdExpedienteEval(ExpedienteEvalToEntity.INSTANCE.apply(expedienteDocEvalDTO.getIdExpedienteEval()));
        expedienteDocEvalHist.setFcAltaFila(new Date());
        expedienteDocEvalHist.setCdUsuAlta(expedienteDocEvalDTO.getCdUsuAlta());
        return  this.insert(expedienteDocEvalHist);
       
    }

    

}
