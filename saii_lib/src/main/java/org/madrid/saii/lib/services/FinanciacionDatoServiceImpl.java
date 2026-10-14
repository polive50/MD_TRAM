package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.madrid.saii.lib.dao.FinanciacionDAO;
import org.madrid.saii.lib.dao.FinanciacionDatoDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.FinanciacionDato;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.dto.converters.FinanciacionDatoToDTO;
import org.madrid.saii.lib.dto.converters.FinanciacionDatoToEntity;
import org.madrid.saii.lib.dto.converters.FinanciacionToDTO;
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
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio Financiacion
 *
 * @author nttdata
 */
@Service("financiacionDatoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class FinanciacionDatoServiceImpl
        extends BaseServiceImpl<FinanciacionDato, Integer, FinanciacionDatoDAO>
        implements FinanciacionDatoService {

    @Autowired
    @Qualifier("financiacionDAO")
    private FinanciacionDAO financiacionDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param financiacionDAO
     */
    @Autowired
    public FinanciacionDatoServiceImpl(
            @Qualifier("financiacionDatoDAO") FinanciacionDatoDAO financiacionDatoDAO) {
        super();
        this.setDao(financiacionDatoDAO);
    }

    /**
     * Constructor Vacio
     */
    public FinanciacionDatoServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<FinanciacionDatoDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), FinanciacionDatoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public FinanciacionDatoDTO buscarId(Integer idFinanciacionDato) throws ServiceException {
        return FinanciacionDatoToDTO.INSTANCE.apply(this.getDao().find(idFinanciacionDato));
    }

    /**
     *
     */
    @Override
    public List<FinanciacionDatoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                FinanciacionDatoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<FinanciacionDatoDTO> importesMaxPresupuesto(Integer idConvocatoria,
            List<BloqueComponenteDTO> bloques) throws ServiceException {
        List<FinanciacionDatoDTO> importesMaximos = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_TIPO,
                Constantes.Formatos.PARTIDAS_PRESUPUESTARIAS, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<FinanciacionDTO> financiacionPresupuestarias = new ArrayList<>(Lists
                .transform(this.financiacionDAO.find(filtros, null), FinanciacionToDTO.INSTANCE));

        if (!financiacionPresupuestarias.isEmpty()) {
            for (FinanciacionDTO financiacionDTO : financiacionPresupuestarias) {
                financiacionDTO.getListaFinanciacionDatoDTO().stream()
                        .filter(dato -> this.existeEnElBloque(dato, bloques))
                        .forEach(importesMaximos::add);
            }
        }

        return importesMaximos;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public FinanciacionDatoDTO guardar(FinanciacionDatoDTO financiacionDatoDTO)
            throws ServiceException {
        return FinanciacionDatoToDTO.INSTANCE.apply(
                this.insertOrUpdate(FinanciacionDatoToEntity.INSTANCE.apply(financiacionDatoDTO)));
    }

    /**
     *
     */
    @Override
    public void altaLogica(final FinanciacionDatoDTO financiacionDatoDTO) throws ServiceException {
        financiacionDatoDTO.setCdUsuBaja(null);
        financiacionDatoDTO.setFcBajaFila(null);
        this.getDao().update(FinanciacionDatoToEntity.INSTANCE.apply(financiacionDatoDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final FinanciacionDatoDTO financiacionDatoDTO)
            throws ServiceException {
        financiacionDatoDTO.setCdUsuBaja(UserUtils.getUserName());
        financiacionDatoDTO.setFcBajaFila(new Date());
        this.getDao().update(FinanciacionDatoToEntity.INSTANCE.apply(financiacionDatoDTO));
    }

    /**
     * Verifica la existencia del dato de financiacion en la lista de bloques,
     * para lo cual solo se utilizara de esa lista el campo tipoFinanciacionDTO
     *
     * @param financiacionDato
     * @param bloques
     * @return Boolean
     */
    private Boolean existeEnElBloque(FinanciacionDatoDTO financiacionDato,
            List<BloqueComponenteDTO> bloques) {
        List<Integer> tipoFinanciacionBloque = bloques.stream()
                .map(bloque -> bloque.getTipoFinanciacionDTO().getIdValor())
                .collect(Collectors.toList());

        return tipoFinanciacionBloque.stream()
                .anyMatch(tipo -> financiacionDato.getTipoDTO().getIdValor().equals(tipo));
    }

    /**
     * 
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public void guardarFinanciacionDato(Integer idFinanciacion,
            Integer idPresupuesto, List<Integer> listaAnualidades, Integer nmOrden)
            throws ServiceException {
        for (Integer anualidad : listaAnualidades) {
            FinanciacionDato financiacionDato = new FinanciacionDato();
            financiacionDato.setIdFinanciacion(idFinanciacion);
            financiacionDato.setTipo(new CatalogosValores(idPresupuesto));
            financiacionDato.setNmAnualidad(anualidad);
            financiacionDato.setItImporteFijo(Constantes.AfirmacionNegacion.N);
            financiacionDato.setNmOrden(nmOrden);
            financiacionDato = this.insertOrUpdate(financiacionDato);
        }
    }

    /**
     * 
     * @param idFinanciacion
     * @return
     * @throws ServiceException
     */
    @Override
    public List<FinanciacionDatoDTO> buscarByIdFinanciacion(Integer idFinanciacion, Integer idTipo,
            Integer anualidad) throws ServiceException {
        List<FinanciacionDatoDTO> lstFinanciacionDatoDTO = new ArrayList<>();
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_FINANCIACION, idFinanciacion,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        if (idTipo != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FINANCIACION_TIPO, idTipo,
                    Operador.EQUALS));
        }
        if (anualidad != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMANUALIDAD, anualidad,
                    Operador.EQUALS));
        }
        lstFinanciacionDatoDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), FinanciacionDatoToDTO.INSTANCE));
        return lstFinanciacionDatoDTO;
    }

}
