package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModfPresupPlazosOtrosDocDAO;
import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;
import org.madrid.saii.lib.dto.ExpedienteModfPresupPlazosOtrosDocDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModfPresupPlazosOtrosDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModfPresupPlazosOtrosDocToEntity;
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


@Service("expedienteModfPresupPlazosOtrosDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteModfPresupPlazosOtrosDocServiceImpl extends BaseServiceImpl<ExpedienteModfPresupPlazosOtrosDoc, Integer, ExpedienteModfPresupPlazosOtrosDocDAO>
    implements ExpedienteModfPresupPlazosOtrosDocService {
    
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param 
     */
    @Autowired
    public ExpedienteModfPresupPlazosOtrosDocServiceImpl(
            @Qualifier("expedienteModfPresupPlazosOtrosDocDAO") ExpedienteModfPresupPlazosOtrosDocDAO expedienteModfPresupPlazosOtrosDocDAO) {
        super();
        this.setDao(expedienteModfPresupPlazosOtrosDocDAO);
    }
    
    @Override
    @Transactional(readOnly = false)
    public  ExpedienteModfPresupPlazosOtrosDocDTO guardarExpedienteModfPresupPlazosOtrosDoc(ExpedienteModfPresupPlazosOtrosDocDTO expedienteModfPresupPlazosOtrosDocDTO) throws ServiceException {

        // VMM 26/09/26 MD-3065: antes de insertar, comprobamos si ya existe un enlace para este mismo
        // (documento, modificacion) - este metodo siempre construia una entidad NUEVA a partir del DTO
        // (sin id), asi que si se llamaba mas de una vez para el mismo documento (p.ej. reenvio del
        // formulario de "Subir resolución") se duplicaba la fila de enlace en
        // SAII_EXPE_MOD_PRE_PLA_OTRA_DOC. Si ya existe, se reutiliza esa fila en vez de crear otra.
        if (expedienteModfPresupPlazosOtrosDocDTO.getExpedienteDocDTO() != null
                && expedienteModfPresupPlazosOtrosDocDTO.getExpedienteDocDTO().getIdExpedienteDoc() != null
                && expedienteModfPresupPlazosOtrosDocDTO.getExpedienteModifPresPlazoOtraDTO() != null
                && expedienteModfPresupPlazosOtrosDocDTO.getExpedienteModifPresPlazoOtraDTO()
                        .getIdExpedienteModifPresPlazoOtra() != null) {

            List<AtlasFilter> filtrosExistente = new ArrayList<>();
            filtrosExistente.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                    expedienteModfPresupPlazosOtrosDocDTO.getExpedienteDocDTO().getIdExpedienteDoc(),
                    Operador.EQUALS));
            filtrosExistente.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF,
                    expedienteModfPresupPlazosOtrosDocDTO.getExpedienteModifPresPlazoOtraDTO()
                            .getIdExpedienteModifPresPlazoOtra(),
                    Operador.EQUALS));
            filtrosExistente.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<ExpedienteModfPresupPlazosOtrosDoc> existentes = this.find(filtrosExistente, null);
            if (!existentes.isEmpty()) {
                return ExpedienteModfPresupPlazosOtrosDocToDTO.INSTANCE.apply(existentes.get(0));
            }
        }

        ExpedienteModfPresupPlazosOtrosDoc expedienteModfPresupPlazosOtrosDoc = ExpedienteModfPresupPlazosOtrosDocToEntity.INSTANCE.apply(expedienteModfPresupPlazosOtrosDocDTO);
        this.getDao().insertOrUpdate(expedienteModfPresupPlazosOtrosDoc);
        return ExpedienteModfPresupPlazosOtrosDocToDTO.INSTANCE.apply(expedienteModfPresupPlazosOtrosDoc);

     }
        
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteModfPresupPlazosOtrosDocDTO actualizarExpedienteModfPresupPlazosOtrosDoc(
            ExpedienteModfPresupPlazosOtrosDocDTO expedienteModfPresupPlazosOtrosDocDTO) throws ServiceException {

        ExpedienteModfPresupPlazosOtrosDoc expedienteModfPresupPlazosOtrosDoc = ExpedienteModfPresupPlazosOtrosDocToEntity.INSTANCE.apply(expedienteModfPresupPlazosOtrosDocDTO);
       this.getDao().update(expedienteModfPresupPlazosOtrosDoc);
       
       return ExpedienteModfPresupPlazosOtrosDocToDTO.INSTANCE.apply(expedienteModfPresupPlazosOtrosDoc);

   }
    
    @Override
    public List<ExpedienteModfPresupPlazosOtrosDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteModfPresupPlazosOtrosDocToDTO.INSTANCE));
    }
    
    @Override
    public List<ExpedienteModfPresupPlazosOtrosDocDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTEDOC_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteModfPresupPlazosOtrosDocToDTO.INSTANCE));
    }
    
    @Override
    public List<ExpedienteModfPresupPlazosOtrosDocDTO> buscarPorExpedienteModfPresupPlazosOtros(Integer idExpedienteModifPresPlazoOtra)
            throws ServiceException {
        
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF, idExpedienteModifPresPlazoOtra,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));
        
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteModfPresupPlazosOtrosDocToDTO.INSTANCE));
    }
    
}
