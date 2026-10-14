package org.madrid.saii.lib.services;

import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.InformeEvalDocDAO;
import org.madrid.saii.lib.domain.InformeEvalDoc;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.madrid.saii.lib.dto.converters.InformeEvalDocToDTO;
import org.madrid.saii.lib.dto.converters.InformeEvalDocToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio InformeEvalDoc
 *
 * @author quental
 */
@Service("informeEvalDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class InformeEvalDocServiceImpl
        extends BaseServiceImpl<InformeEvalDoc, Integer, InformeEvalDocDAO>
        implements InformeEvalDocService {

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @Autowired
    public InformeEvalDocServiceImpl(@Qualifier("informeEvalDocDAO")
    InformeEvalDocDAO informeEvalDocDAO) {
        super();
        this.setDao(informeEvalDocDAO);
    }

    public InformeEvalDocServiceImpl() {
        super();
    }

    @Override
    public InformeEvalDocDTO obtenerPorExpedienteEvalEInforme(Integer idExpedienteEval,
            Integer idInforme) throws ServiceException {
        return this.getDao().obtenerPorExpedienteEvalEInforme(idExpedienteEval, idInforme);
    }

    @Override
    public List<InformeEvalDocDTO> obtenerListaPorExpedienteEval(Integer idExpedienteEval)
            throws ServiceException {
        return this.getDao().obtenerListaPorExpedienteEval(idExpedienteEval);
    }

    @Override
    @Transactional(readOnly = false)
    public InformeEvalDocDTO guardar(InformeEvalDocDTO informeEvalDocDTO) throws ServiceException {
        return InformeEvalDocToDTO.INSTANCE.apply(
                this.insertOrUpdate(InformeEvalDocToEntity.INSTANCE.apply(informeEvalDocDTO)));
    }

    @Override
    @Transactional(readOnly = false)
    public InformeEvalDocDTO mergeActualizar(InformeEvalDocDTO informeEvalDocDTO) throws ServiceException {
        InformeEvalDoc entity = InformeEvalDocToEntity.INSTANCE.apply(informeEvalDocDTO);
        return InformeEvalDocToDTO.INSTANCE.apply(
                (InformeEvalDoc) this.getDao().merge(entity));
    }

    @Override
    public InformeEvalDocDTO buscarId(Integer idEvalInfrmDoc) throws ServiceException {
        InformeEvalDoc entity = this.find(idEvalInfrmDoc);
        return InformeEvalDocToDTO.INSTANCE.apply(entity);
    }

    @Override
    public InformeEvalDocDTO obtenerPorExpedienteEvalInformeItFirmado(Integer idExpedienteEval,
            Integer idInforme, String itFirmado) throws ServiceException {
        return this.getDao().obtenerPorExpedienteEvalInformeItFirmado(idExpedienteEval, idInforme, itFirmado);
    }

    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(InformeEvalDocDTO informeEvalDocDTO) throws ServiceException {
        informeEvalDocDTO.setCdUsuBaja(UserUtils.getUserName().toUpperCase());
        informeEvalDocDTO.setFcBajaFila(new Date());
        this.guardar(informeEvalDocDTO);
    }

    @Override
    public List<InformeEvalDocDTO> obtenerIdExpedienteItCoorEvalItFirmado(Integer idExpediente) throws ServiceException {
        return this.getDao().obtenerIdExpedienteItCoorEvalItFirmado(idExpediente);
    }

    @Override
    public List<InformeEvalDocDTO> obtenerListaInformesEvaluadoresPorExpediente(Integer idExpedienteEvalPadre)
            throws ServiceException {
        List<InformeEvalDocDTO> lista = this.getDao().obtenerListaInformesEvaluadoresPorExpediente(idExpedienteEvalPadre);
        if (lista != null) {
            for (InformeEvalDocDTO dto : lista) {
                if (dto.getIdExpedienteEval() != null && dto.getIdInforme() != null
                        && dto.getIdInforme().getIdInforme() != null) {
                    InformeEvalDocDTO firmado = this.getDao().obtenerPorExpedienteEvalInformeItFirmado(
                            dto.getIdExpedienteEval(), dto.getIdInforme().getIdInforme(),
                            Constantes.AfirmacionNegacion.S);
                    if (firmado != null && firmado.getCdFica() != null) {
                        try {
                            dto.setCdFicaInformeFirmado(Integer.parseInt(firmado.getCdFica()));
                            Integer idValorCerradoCoor = utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR);
                            CatalogosValoresDTO estadoCerradoCoor = catalogosValoresService
                                    .buscarId(idValorCerradoCoor);
                            dto.setIdEstado(estadoCerradoCoor);
                        } catch (NumberFormatException e) {
                            log.warn("cdFica no numérico para informe firmado idExpedienteEval={} idInforme={}",
                                    dto.getIdExpedienteEval(), dto.getIdInforme().getIdInforme());
                        }
                    }
                }
            }
        }
        return lista;
    }

}
