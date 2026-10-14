package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.AlegacionDatoDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.domain.Alegacion;
import org.madrid.saii.lib.domain.AlegacionDato;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.AlegacionDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionDatoDTO;
import org.madrid.saii.lib.dto.converters.AlegacionDatoToDTO;
import org.madrid.saii.lib.dto.converters.AlegacionDatoToEntity;
import org.madrid.saii.lib.dto.converters.AlegacionToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.Setter;

/**
 * Implementación del servicio para la clase de dominio AlegacionDato
 *
 * @author nttdata
 */
@Service("alegacionDatoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class AlegacionDatoServiceImpl extends
        BaseServiceImpl<AlegacionDato, Integer, AlegacionDatoDAO> implements AlegacionDatoService {

    /**
     * Servicio de Fica
     */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /**
     * Servicio PdfService
     */
    @Lazy
    @Autowired
    @Qualifier("pdfService")
    @Setter
    private PdfService pdfService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * DAO de Expediente
     */
    @Autowired
    @Qualifier("expedienteDAO")
    @Setter
    private ExpedienteDAO expedienteDAO;

    /**
     * DAO de Expediente
     */
    @Autowired
    @Qualifier("expedienteHistDAO")
    @Setter
    private ExpedienteHistDAO expedienteHistDAO;

    /**
     * DAO de Alegacion
     */
    @Autowired
    @Qualifier("alegacionService")
    @Setter
    private AlegacionService alegacionService;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param alegacionDatoDAO
     */
    @Autowired
    public AlegacionDatoServiceImpl(@Qualifier("alegacionDatoDAO")
    AlegacionDatoDAO alegacionDatoDAO) {
        super();
        this.setDao(alegacionDatoDAO);
    }

    /**
     * Constructor Vacio
     */
    public AlegacionDatoServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<AlegacionDatoDTO> obtenerPorExpediente(Integer idExpediente)
            throws ServiceException {
        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));

        List<AlegacionDato> listaAlegacionDato = this.getDao().find(filtros, null);
        return listaAlegacionDato.stream().map(AlegacionDatoToDTO.INSTANCE::apply)
                .collect(Collectors.toList());
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public List<AlegacionDatoDTO> guardarDatosAlegacion(EvaluacionDatoDTO evaluacionDatoDTO,
            String cdNumIden) throws ServiceException {

        List<AlegacionDatoDTO> lstAlegacionesDTO = new ArrayList<>();
        for (AlegacionDatoDTO alegacionDatoDTO : evaluacionDatoDTO.getLstAlegacionDatoDTO()) {

            if (alegacionDatoDTO.getDsValor().length() > 2600) {
                throw new ServiceException(
                        Constantes.Error.ERROR_CANTIDAD_MAXIMA_PERMITIDA_ALEGACIONES);
            }

            AlegacionDato alegacionDato = AlegacionDatoToEntity.INSTANCE.apply(alegacionDatoDTO);
            Alegacion alegacion = AlegacionToEntity.INSTANCE
                    .apply(this.alegacionService.buscarId(alegacionDatoDTO.getIdAlegacion()));
            alegacionDato.setIdExpediente(evaluacionDatoDTO.getIdExpediente());
            alegacionDato.setAlegacion(alegacion);
            alegacionDato.setDsValor(alegacionDatoDTO.getDsValor());
            alegacionDato = this.insertOrUpdate(alegacionDato);
            lstAlegacionesDTO.add(AlegacionDatoToDTO.INSTANCE.apply(alegacionDato));
        }

        // generar el pdf
        byte[] documento = this.pdfService
                .crearFormularioAlegacionDato(evaluacionDatoDTO.getIdExpediente(), cdNumIden);

        Expediente expediente = this.expedienteDAO.find(evaluacionDatoDTO.getIdExpediente());
        String cdFica = this.guardarFicheroFica(expediente.getCdFicaValoracionAleg(),
                evaluacionDatoDTO.getIdentificador(), documento);

        expediente
                .setCdFicaValoracionAleg(Objects.nonNull(cdFica) ? Integer.parseInt(cdFica) : null);
        expediente.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION)));
        expediente.setEstadoBenef(ExpedienteToEntity.INSTANCE
                .calcularEstadoBenef(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION)));

        expediente.setFcModifFila(new Date());
        expediente.setCdUsuModif(cdNumIden);

        expediente = this.expedienteDAO.merge(expediente);

        // Historico
        this.expedienteHistDAO.crearHistorico(expediente, cdNumIden);

        return lstAlegacionesDTO;
    }

    /**
     *
     * @param cdFica
     * @param identificador
     * @param documento
     * @return
     * @throws ServiceException
     */
    private String guardarFicheroFica(Integer cdFica, String identificador, byte[] documento)
            throws ServiceException {
        // Validacion
        if (Objects.isNull(documento)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        FicaMetadatos ficaMetadatos = new FicaMetadatos();
        ficaMetadatos.setNombreFichero(Constantes.TipoCoorEval.INFORME_ALEGACION);
        ficaMetadatos.setFechaFichero(new Date());
        ficaMetadatos.setExtension(Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION);
        ficaMetadatos.setUsuario(identificador);
        ficaMetadatos.setEncriptar(false);
        ficaMetadatos.setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);

        FicaFichero fichero;
        if (Objects.nonNull(cdFica)) {
            fichero = this.ficaService.actualizar(cdFica, documento, ficaMetadatos);
            return String.valueOf(fichero.getIdInterno());
        }

        fichero = this.ficaService.guardar(documento, ficaMetadatos);
        return Objects.nonNull(fichero) ? String.valueOf(fichero.getIdInterno()) : null;
    }

}
