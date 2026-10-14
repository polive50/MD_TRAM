package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.madrid.saii.lib.dao.ApartadoDAO;
import org.madrid.saii.lib.dao.EvaluacionNotaDAO;
import org.madrid.saii.lib.dao.InformeDAO;
import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.domain.Informe;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionToEntity;
import org.madrid.saii.lib.dto.converters.InformeToDTO;
import org.madrid.saii.lib.dto.converters.InformeToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

@Service("informeService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class InformeServiceImpl extends BaseServiceImpl<Informe, Integer, InformeDAO>
        implements InformeService {
    
    @Autowired
    @Qualifier("evaluacionNotaDAO")
    private EvaluacionNotaDAO evaluacionNotaDAO;
    
    @Autowired
    @Qualifier("apartadoDAO")
    private ApartadoDAO apartadoDAO;
    
    @Autowired
    @Qualifier("evaluacionService")
    private EvaluacionService evaluacionService;
    
    @Autowired
    @Lazy
    private InformeService self;

    /**
     * 
     */
    public InformeServiceImpl(@Qualifier("informeDAO") InformeDAO informeDAO) {
        super();
        this.setDao(informeDAO);
    }

    /**
     * 
     */
    @Override
    public List<InformeDTO> findByIdConvocatoriaIdTipoInforme(Integer idConvocatoria,
            Integer idTipoInforme) throws ServiceException {
        
        List<AtlasFilter> lstFiltro = new ArrayList<>();
        lstFiltro.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        if (Objects.nonNull(idTipoInforme)) {
            lstFiltro.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_INFORME_ID_VALOR, idTipoInforme,
                    Operador.EQUALS));
        }
        lstFiltro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<Informe> informe = this.getDao().find(lstFiltro, null);
        return informe.stream().map(InformeToDTO.INSTANCE::apply).collect(Collectors.toList());
    }

    /**
     * 
     */
    @Override
    public List<InformeDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), InformeToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public InformeDTO guardar(InformeDTO informeDTO) throws ServiceException {
        return InformeToDTO.INSTANCE
                .apply(this.self.insertOrUpdate(InformeToEntity.INSTANCE.apply(informeDTO)));
    }

    @Override
    public List<InformeDTO> obtenerPorConvocatoria(Integer idConvocatoria) throws ServiceException {
        List<InformeDTO> lstInformes = new ArrayList<>();
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<AtlasSort> lisOrden = new ArrayList<>();
        lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_INFORME, true));
        List<Informe> lstInforme = this.getDao().find(filtros, lisOrden);
        if (Objects.nonNull(lstInforme)) {
            lstInformes = new ArrayList<>(Lists.transform(lstInforme, InformeToDTO.INSTANCE));
        }
        return lstInformes;
    }

    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(InformeDTO informeDTO) throws ServiceException {
        
        Informe informe = InformeToEntity.INSTANCE.apply(informeDTO);
        informe.setCdUsuBaja(UserUtils.getUserName());
        informe.setFcBajaFila(new Date());
        this.self.update(informe);
        
        List<Evaluacion> lstEvaluacion = this.evaluacionService
                .buscarPorInforme(informeDTO.getIdInforme());
        
        for (Evaluacion evaluacion : lstEvaluacion) {
            
            evaluacion.setCdUsuBaja(UserUtils.getUserName());
            evaluacion.setFcBajaFila(new Date());
            this.evaluacionService.update(evaluacion);

            // consultar todas las notas asociadas a la evaluacion
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION,
                    evaluacion.getIdEvaluacion(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<EvaluacionNota> listaNotas = this.evaluacionNotaDAO.find(filtros, null);

            for (EvaluacionNota nota : listaNotas) {
                nota.setCdUsuBaja(UserUtils.getUserName());
                nota.setFcBajaFila(new Date());
                this.evaluacionNotaDAO.update(nota);
            }

            List<Apartado> listaApartados = this.apartadoDAO.find(filtros, null);
            for (Apartado apartado : listaApartados) {
                apartado.setCdUsuBaja(UserUtils.getUserName());
                apartado.setFcBajaFila(new Date());
                this.apartadoDAO.update(apartado);
            }
            
        }
        
    }

    @Override
    public List<InformeDTO> findByIdConvocatoriaIdInforme(Integer idConvocatoria, Integer idInforme)
            throws ServiceException {
        List<AtlasFilter> lstFiltro = new ArrayList<>();
        lstFiltro.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        if (Objects.nonNull(idInforme)) {
            lstFiltro.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_INFORME, idInforme,
                    Operador.EQUALS));
        }
        lstFiltro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<Informe> informe = this.getDao().find(lstFiltro, null);
        return informe.stream().map(InformeToDTO.INSTANCE::apply).collect(Collectors.toList());
    }
   
}
