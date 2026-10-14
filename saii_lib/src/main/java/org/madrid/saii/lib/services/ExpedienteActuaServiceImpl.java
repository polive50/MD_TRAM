package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.madrid.saii.lib.dao.ExpedienteActuaDAO;
import org.madrid.saii.lib.dao.ExpedienteActuaHistDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedienteActua;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.ExpedienteActuaDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteActuaToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteActuaToEntity;
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
 * Implementación del servicio para la clase de dominio ExpedienteActua
 *
 * @author nttdata
 */
@Service("expedienteActuaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteActuaServiceImpl
        extends BaseServiceImpl<ExpedienteActua, Integer, ExpedienteActuaDAO>
        implements ExpedienteActuaService {

    /**
     * DAO ExpedienteActuaHist
     */
    @Autowired
    @Qualifier("expedienteActuaHistDAO")
    private ExpedienteActuaHistDAO expedienteActuaHistDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteActuaDAO
     */
    @Autowired
    public ExpedienteActuaServiceImpl(
            @Qualifier("expedienteActuaDAO") ExpedienteActuaDAO expedienteActuaDAO) {
        super();
        this.setDao(expedienteActuaDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteActuaServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ExpedienteActuaDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ExpedienteActuaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteActuaDTO buscarId(Integer idExpedienteActua) throws ServiceException {
        return ExpedienteActuaToDTO.INSTANCE.apply(this.getDao().find(idExpedienteActua));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteActuaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteActuaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteActuaDTO guardar(ExpedienteActuaDTO expedienteActuaDTO)
            throws ServiceException {
        return ExpedienteActuaToDTO.INSTANCE.apply(
                this.insertOrUpdate(ExpedienteActuaToEntity.INSTANCE.apply(expedienteActuaDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ExpedienteActuaDTO expedienteActuaDTO) throws ServiceException {
        expedienteActuaDTO.setCdUsuBaja(null);
        expedienteActuaDTO.setFcBajaFila(null);
        this.getDao().update(ExpedienteActuaToEntity.INSTANCE.apply(expedienteActuaDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ExpedienteActuaDTO expedienteActuaDTO) throws ServiceException {
        expedienteActuaDTO.setCdUsuBaja(UserUtils.getUserName());
        expedienteActuaDTO.setFcBajaFila(new Date());
        this.getDao().update(ExpedienteActuaToEntity.INSTANCE.apply(expedienteActuaDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void guardarConHistorico(List<ExpedienteActuaDTO> lstExpedienteActuaDTO,
            String observacion, Double montoAyuda) throws ServiceException {

        List<ExpedienteActuaDTO> listaExpActuaDTO = lstExpedienteActuaDTO.stream()
                .filter(a -> Objects.nonNull(a.getIdExpedienteActua()))
                .collect(Collectors.toList());
        Date fechaHoy = new Date();
        for (ExpedienteActuaDTO act : listaExpActuaDTO) {

            ExpedienteActua expAct = this.getDao().find(act.getIdExpedienteActua());
            expAct.setTlObservacion(observacion);
            expAct.setNmImporte(act.getNmImporte());
            expAct.setNmAyuda(montoAyuda);

            if (Objects.isNull(act.getIdExpedienteActua())) {
                expAct = this.getDao().insert(expAct);
            } else {
                expAct = this.getDao().merge(expAct);
            }

            expAct.setFcAltaFila(fechaHoy);
            expAct.setFcModifFila(fechaHoy);
            this.expedienteActuaHistDAO.crearHistorico(expAct);

        }

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void cargarDatos(List<BloqueComponenteDTO> lstActuacionPresentado, Double montoAyuda,
            Integer idExpediente) throws ServiceException {

        for (BloqueComponenteDTO bc : lstActuacionPresentado) {

            if (Objects.nonNull(bc.getTipoFinanciacionDTO().getIdValor())) {

                ExpedienteActua expAct = new ExpedienteActua();

                expAct.setTipoFinanciacion(
                        new CatalogosValores(bc.getTipoFinanciacionDTO().getIdValor()));
                expAct.getTipoFinanciacion().setCdCodigo(bc.getTipoFinanciacionDTO().getCdCodigo());
                expAct.getTipoFinanciacion().setDsValor(bc.getTipoFinanciacionDTO().getDsValor());
                expAct.setIdExpediente(idExpediente);
                expAct.setTlObservacion("Datos de actuaciones financiables inicial.");

                expAct.setDsProveedor(bc.getDatos().get(0).getDsValor());
                expAct.setNmImporte(Double.valueOf(bc.getDatos().get(1).getDsValor()));
                expAct.setNmAyuda(montoAyuda);

                // TODO - IMPLEMENTAR LOGICA PARA RECUPERAR EL VALOR CORRECTO
                // expAct.setItMemoria("N");

                expAct = this.getDao().insert(expAct);

                this.expedienteActuaHistDAO.crearHistorico(expAct);
            }
        }

    }

}
