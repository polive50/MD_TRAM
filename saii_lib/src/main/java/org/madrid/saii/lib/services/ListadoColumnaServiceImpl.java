package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.ListadoColumnaDAO;
import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ListadoColumnaDTO;
import org.madrid.saii.lib.dto.ListadoConfiguracionDTO;
import org.madrid.saii.lib.dto.converters.ListadoColumnaToDTO;
import org.madrid.saii.lib.dto.converters.ListadoColumnaToEntity;
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
 * Implementación del servicio para la clase de dominio ListadoColumna
 *
 * @author nttdata
 */
@Service("listadoColumnaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ListadoColumnaServiceImpl
        extends BaseServiceImpl<ListadoColumna, Integer, ListadoColumnaDAO>
        implements ListadoColumnaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param listadoColumnaDAO
     */
    @Autowired
    public ListadoColumnaServiceImpl(
            @Qualifier("listadoColumnaDAO") ListadoColumnaDAO listadoColumnaDAO) {
        super();
        this.setDao(listadoColumnaDAO);
    }

    /**
     * Constructor Vacio
     */
    public ListadoColumnaServiceImpl() {
        super();
    }

    /**
    *
    */
    @Override
    public List<ListadoColumnaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ListadoColumnaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ListadoColumnaDTO guardar(ListadoColumnaDTO listadoColumnaDTO) throws ServiceException {
        return ListadoColumnaToDTO.INSTANCE.apply(
                this.insertOrUpdate(ListadoColumnaToEntity.INSTANCE.apply(listadoColumnaDTO)));
    }

    @Override
    @Transactional(readOnly = false)
    public void guardarConfiguracion(List<ListadoConfiguracionDTO> lista) throws ServiceException {

        int ordenColumna = 1;
        for (ListadoConfiguracionDTO configuracionDTO : lista) {

            if (Objects.nonNull(configuracionDTO.getIdListadoColumna())) {
                ListadoColumna columna = this.find(configuracionDTO.getIdListadoColumna());
                columna.setNmOrden(ordenColumna);

                this.getDao().insertOrUpdate(columna);
                ordenColumna++;
            }

        }
    }

    /**
     *
     */
    @Override
    public List<ListadoConfiguracionDTO> obtenerListadoColumnas(Integer idConvocatoria,
            Integer idListado) throws ServiceException {
        return this.getDao().obtenerListadoColumnas(idConvocatoria, idListado);
    }

    @Override
    public List<ListadoConfiguracionDTO> obtenerListaColumnaDisponible(Integer idConvocatoria)
            throws ServiceException {
        return this.getDao().obtenerListaColumnaDisponible(idConvocatoria);
    }

    @Override
    public List<ListadoConfiguracionDTO> obtenerListaColumnaConfigurada(Integer idListado)
            throws ServiceException {
        return this.getDao().obtenerListaColumnaConfigurada(idListado);
    }

}
