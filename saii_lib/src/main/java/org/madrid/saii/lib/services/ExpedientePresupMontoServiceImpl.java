package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ExpedientePresMontHistDAO;
import org.madrid.saii.lib.dao.ExpedientePresupMontoDAO;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.converters.ExpedientePresupMontoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedientePresupMontoToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio ExpedienteFin
 *
 * @author nttdata
 */
@Service("expedientePresupMontoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedientePresupMontoServiceImpl
        extends BaseServiceImpl<ExpedientePresupMonto, Integer, ExpedientePresupMontoDAO>
        implements ExpedientePresupMontoService {

    @Autowired
    @Qualifier("expedientePresMontHistService")
    private ExpedientePresMontHistService expedientePresMontHistService;
    
    @Autowired
    @Qualifier("expedientePresupMontoDAO")
    private ExpedientePresupMontoDAO expedientePresupMontoDAO;
    
    @Autowired
    @Qualifier("expedientePresMontHistDAO")
    private ExpedientePresMontHistDAO expedientePresMontHistDAO;
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedientePresupDAO
     */
    @Autowired
    public ExpedientePresupMontoServiceImpl(
            @Qualifier("expedientePresupMontoDAO") ExpedientePresupMontoDAO expedientePresupMontoDAO) {
        super();
        this.setDao(expedientePresupMontoDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedientePresupMontoServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ExpedientePresupMontoDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ExpedientePresupMontoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedientePresupMontoDTO buscarId(Integer idExpedienteFin) throws ServiceException {
        return ExpedientePresupMontoToDTO.INSTANCE.apply(this.getDao().find(idExpedienteFin));
    }

    /**
     *
     */
    @Override
    public List<ExpedientePresupMontoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedientePresupMontoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedientePresupMontoDTO guardar(ExpedientePresupMontoDTO expedientePresupMontoDTO)
            throws ServiceException {
        return ExpedientePresupMontoToDTO.INSTANCE.apply(this.insertOrUpdate(
                ExpedientePresupMontoToEntity.INSTANCE.apply(expedientePresupMontoDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ExpedientePresupMontoDTO expedientePresupMontoDTO)
            throws ServiceException {
        expedientePresupMontoDTO.setCdUsuBaja(null);
        expedientePresupMontoDTO.setFcBajaFila(null);
        this.getDao()
                .update(ExpedientePresupMontoToEntity.INSTANCE.apply(expedientePresupMontoDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ExpedientePresupMontoDTO expedientePresupMontoDTO)
            throws ServiceException {
        expedientePresupMontoDTO.setCdUsuBaja(UserUtils.getUserName());
        expedientePresupMontoDTO.setFcBajaFila(new Date());
        this.getDao()
                .update(ExpedientePresupMontoToEntity.INSTANCE.apply(expedientePresupMontoDTO));
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public ExpedientePresupMontoDTO guardarConHistorico(ExpedientePresupMontoDTO expedientePresupMontoDTO)
            throws ServiceException {

        // VMM 24/09/26 MD-2725: se cambia insertOrUpdate() (saveOrUpdate) por merge(). Si el
        // llamante ya cargo esta misma entidad en la sesion actual (p.ej. via buscarId()/find()
        // para leer el importe actual antes de modificarlo), ExpedientePresupMontoToEntity.apply(dto)
        // crea una instancia Java NUEVA con el mismo id, y saveOrUpdate() la rechaza con
        // NonUniqueObjectException por tener ya otra instancia asociada a esa misma fila. merge()
        // esta pensado precisamente para este caso: concilia el estado sobre la instancia ya
        // gestionada (o la carga si no habia ninguna) en vez de exigir que sea la unica.
        ExpedientePresupMonto entidadAGuardar = ExpedientePresupMontoToEntity.INSTANCE.apply(expedientePresupMontoDTO);

        // VMM 20/10/26 MD-3156: el DTO no lleva los campos de auditoria y desde el batch (volcado de las
        // modificaciones resueltas, ResolucionModificacionServiceImpl) no hay sesion web, asi que
        // UserUtils.getUserName() da vacio y CD_USU_MODIF se quedaba sin informar. Mismo fallback que ya usa
        // crearNuevoConceptoHijo para CD_USU_ALTA.
        if (entidadAGuardar != null && StringUtils.isBlank(UserUtils.getUserName())) {
            entidadAGuardar.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
            entidadAGuardar.setFcModifFila(new Date());
        }

        ExpedientePresupMonto entidadFusionada = this.getDao().merge(entidadAGuardar);
        ExpedientePresupMontoDTO expPresupMontoDTO = ExpedientePresupMontoToDTO.INSTANCE.apply(entidadFusionada);

        expedientePresMontHistService.getDao().crearHistorico(ExpedientePresupMontoToEntity.INSTANCE.apply(expPresupMontoDTO));

        return expPresupMontoDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public void eliminarMonto(ExpedientePresupMontoDTO montoDTO) {
        //buscamos la entidad a eliminar para dar la baja logica
        ExpedientePresupMonto expPresupMonto = this.expedientePresupMontoDAO.find(montoDTO.getIdExpedientePresupMonto());
        
        if (expPresupMonto != null) {
            expPresupMonto.setCdUsuBaja(UserUtils.getUserName());
            expPresupMonto.setFcBajaFila(new Date());
            
            this.getDao().merge(expPresupMonto);
        }
        
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public ExpedientePresupMontoDTO crearNuevoConceptoHijo(ExpedientePresupMontoDTO expPresupMontoDTO) throws ServiceException {
        
        // Convertir el DTO del Hijo a Entidad
        ExpedientePresupMonto expPresupMontoEntity = ExpedientePresupMontoToEntity.INSTANCE.apply(expPresupMontoDTO);
        
        if (expPresupMontoEntity != null) {
            // VMM 24/09/26 MD-2725: UserUtils.getUserName() da vacio cuando este metodo se llama desde
            // el batch (ResolucionModificacionServiceImpl), que no tiene sesion web - caia en
            // ORA-01400 al ser CD_USU_ALTA NOT NULL.
            String cdUsuAlta = UserUtils.getUserName();
            expPresupMontoEntity.setCdUsuAlta(
                    StringUtils.isNotBlank(cdUsuAlta) ? cdUsuAlta : Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
            expPresupMontoEntity.setFcAltaFila(new Date());
            
            expPresupMontoEntity = this.getDao().insert(expPresupMontoEntity);
            
            //insertar los montos en el historico
            this.expedientePresMontHistDAO.crearHistorico(expPresupMontoEntity);
        }
        
        return ExpedientePresupMontoToDTO.INSTANCE.apply(expPresupMontoEntity);
    }

}
