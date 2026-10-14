package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteActuaHistDAO;
import org.madrid.saii.lib.domain.ExpedienteActuaHist;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteActuaHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteActuaHistToEntity;
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
 * Implementación del servicio para la clase de dominio ExpedienteActuaHist
 *
 * @author nttdata
 */
@Service("expedienteActuaHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteActuaHistServiceImpl
        extends BaseServiceImpl<ExpedienteActuaHist, Integer, ExpedienteActuaHistDAO>
        implements ExpedienteActuaHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteActuaHistDAO
     */
    @Autowired
    public ExpedienteActuaHistServiceImpl(
            @Qualifier("expedienteActuaHistDAO") ExpedienteActuaHistDAO expedienteActuaHistDAO) {
        super();
        this.setDao(expedienteActuaHistDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteActuaHistServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ExpedienteActuaHistDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ExpedienteActuaHistToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteActuaHistDTO buscarId(Integer idExpedienteActuaHist) throws ServiceException {
        return ExpedienteActuaHistToDTO.INSTANCE.apply(this.getDao().find(idExpedienteActuaHist));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteActuaHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteActuaHistToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteActuaHistDTO guardar(ExpedienteActuaHistDTO expedienteActuaHistDTO)
            throws ServiceException {
        return ExpedienteActuaHistToDTO.INSTANCE.apply(this.insertOrUpdate(
                ExpedienteActuaHistToEntity.INSTANCE.apply(expedienteActuaHistDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ExpedienteActuaHistDTO expedienteActuaHistDTO)
            throws ServiceException {
        expedienteActuaHistDTO.setCdUsuBaja(null);
        expedienteActuaHistDTO.setFcBajaFila(null);
        this.getDao().update(ExpedienteActuaHistToEntity.INSTANCE.apply(expedienteActuaHistDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ExpedienteActuaHistDTO expedienteActuaHistDTO)
            throws ServiceException {
        expedienteActuaHistDTO.setCdUsuBaja(UserUtils.getUserName());
        expedienteActuaHistDTO.setFcBajaFila(new Date());
        this.getDao().update(ExpedienteActuaHistToEntity.INSTANCE.apply(expedienteActuaHistDTO));
    }

    /**
    *
    */
    @Override
    public List<ExpedienteActuaHistDTO> obtenerListaActuacionHist(Integer idExpediente)
            throws ServiceException {
        return this.getDao().buscarPorExpediente(idExpediente);
    }

    /**
     *
     */
    @Override
    public List<ExpedienteActuaHistDTO> retornarPaginado(
            List<ExpedienteActuaHistDTO> listaHistorico, int index, int pageSize) {
        List<ExpedienteActuaHistDTO> listaPaginada = new ArrayList<>();

        if (!listaHistorico.isEmpty()) {

            if (pageSize != -1) {

                int lastIndex = 0;

                if (listaHistorico.size() <= 10) {
                    index = 0;
                    lastIndex = listaHistorico.size();
                } else {
                    lastIndex = index + pageSize;
                    lastIndex = lastIndex > listaHistorico.size() ? listaHistorico.size()
                            : lastIndex;
                }

                for (int i = index; i < lastIndex; i++) {
                    listaPaginada.add(listaHistorico.get(i));
                }
            } else {
                listaPaginada.addAll(listaHistorico);
            }
        }

        return listaPaginada;
    }

}
