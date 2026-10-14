package org.madrid.saii.lib.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogoDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Service("pdfCamposService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class PdfCamposServiceImpl implements PdfCamposService {

    /** The representante jur service. */
    @Autowired
    @Qualifier("representanteJurService")
    private RepresentanteJurService representanteJurService;

    /** The expedienteService service. */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /** The suca municipio service. */
    @Autowired
    @Qualifier("sucaMunicipioService")
    private SucaMunicipioService sucaMunicipioService;

    /** The suca provincia service. */
    @Autowired
    @Qualifier("sucaProvinciaService")
    private SucaProvinciaService sucaProvinciaService;

    /** The solicitud dao. */
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    /** The solicitud dao. */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;

    /**
     * Calcula el importe total del presupuesto presentado de un expediente
     * (bloque de formato ID_PRESUPUESTO_PRESENTADO), replicando el mismo
     * cálculo que se hace en rellenarCampos para obtener el valor que se
     * guardaba en decimalValorStringMontoTotalTotales, pero de forma aislada
     * y reutilizable, sin efectos secundarios sobre hamCampos.
     *
     * @param dto expediente sobre el que calcular el importe
     * @return importe total del presupuesto presentado
     * @throws ServiceException the service exception
     */
    @Override
    public BigDecimal calcularImporteTotalPresupuesto(ExpedienteDTO dto) throws ServiceException {

        BigDecimal montoTotalTotales = BigDecimal.ZERO;

        Optional<BloqueDTO> bloquePresupuesto = dto.getImpresoDTO().getLstBloqueDTO().stream()
                .filter(b -> Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO
                        .equals(b.getIdFormato().getNmConstante()))
                .findFirst();

        if (!bloquePresupuesto.isPresent()) {
            return montoTotalTotales;
        }

        BloqueDTO bloque = bloquePresupuesto.get();

        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(dto.getConvocatoriaActDTO().getIdConvocatoria());

        List<BloqueComponenteDTO> listaBloqueComponente = bloque.getLstBloqueComponenteDTO().stream()
                .filter(x -> x.getFcBajaFila() == null)
                .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                .collect(Collectors.toList());

        if (convocatoriaDTO.getItConceptoPresup() != null
                && convocatoriaDTO.getItConceptoPresup().equalsIgnoreCase("S")) {

            List<BloqueComponenteDTO> lstFilasAux = listaBloqueComponente.stream()
                    .filter(f -> f.getItFila()).collect(Collectors.toList());

            for (BloqueComponenteDTO bloqCom : lstFilasAux) {
                bloqCom.setDatos(bloqCom.getDatos().stream()
                        .filter(p -> p.getExpedienteDTO() != null
                                && p.getExpedienteDTO().getIdExpediente() != null
                                && p.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente()))
                        .collect(Collectors.toList()));
            }

            List<BloqueComponenteDTO> lstFilas = new ArrayList<>();

            for (BloqueComponenteDTO bloqueComp : lstFilasAux) {
                if (bloqueComp.getDatos().size() == convocatoriaDTO.getNmDurMaxAyuda() + 1) {
                    bloqueComp.setDatos(bloqueComp.getDatos().stream()
                            .sorted(Comparator.comparing(DatoDTO::getNmColumna))
                            .collect(Collectors.toList()));
                    lstFilas.add(bloqueComp);
                } else if (bloqueComp.getDatos().size() > convocatoriaDTO.getNmDurMaxAyuda() + 2) {
                    int posCelda = 0;
                    bloqueComp.setDatos(bloqueComp.getDatos().stream()
                            .sorted(Comparator.comparing(DatoDTO::getNmFila))
                            .collect(Collectors.toList()));
                    for (int j = posCelda; j < bloqueComp.getDatos().size(); j++) {
                        List<DatoDTO> datosFilaConcepto = new ArrayList<>();
                        BloqueComponenteDTO bloqueComConcepto = new BloqueComponenteDTO();
                        for (int index = 0; index < convocatoriaDTO.getNmDurMaxAyuda() + 1; index++) {
                            datosFilaConcepto.add(bloqueComp.getDatos().get(posCelda));
                            posCelda++;
                            j = posCelda;
                        }
                        datosFilaConcepto = datosFilaConcepto.stream()
                                .sorted(Comparator.comparing(DatoDTO::getNmColumna))
                                .collect(Collectors.toList());
                        bloqueComConcepto.setIdBloque(bloqueComp.getIdBloque());
                        bloqueComConcepto.setDatos(datosFilaConcepto);
                        bloqueComConcepto.setIdBloqueComponente(bloqueComp.getIdBloqueComponente());
                        bloqueComConcepto.setCdUsuAlta(bloqueComp.getCdUsuAlta());
                        bloqueComConcepto.setComponenteDTO(bloqueComp.getComponenteDTO());
                        bloqueComConcepto.setNmOrden(bloqueComp.getNmOrden());
                        bloqueComConcepto.setItFila(bloqueComp.getItFila());
                        bloqueComConcepto.setItColumna(bloqueComp.getItColumna());
                        bloqueComConcepto.setItObligatorio(bloqueComp.getItObligatorio());
                        bloqueComConcepto.setItRelacion(bloqueComp.getItRelacion());
                        bloqueComConcepto.setTipoFinanciacionDTO(bloqueComp.getTipoFinanciacionDTO());
                        bloqueComConcepto.setItSeleccionado(bloqueComp.getItSeleccionado());
                        lstFilas.add(bloqueComConcepto);
                    }
                }
            }

            lstFilas = lstFilas.stream().filter(f -> f.getItFila()).collect(Collectors.toList());

            for (int i = 1; i < convocatoriaDTO.getNmDurMaxAyuda() + 1; i++) {
                BigDecimal montoTotalColumna = BigDecimal.ZERO;
                for (BloqueComponenteDTO lstFila : lstFilas) {
                    if (Objects.nonNull(lstFila) && lstFila.getDatos().size() > i
                            && Objects.nonNull(lstFila.getDatos().get(i))
                            && lstFila.getDatos().get(i).getNmColumna() != null
                            && lstFila.getDatos().get(i).getNmColumna().intValue() != 0) {
                        String valorTcolum = lstFila.getDatos().get(i).getDsValor();
                        BigDecimal valor = BigDecimal.ZERO;
                        if (valorTcolum != null && !valorTcolum.trim().equals("")) {
                            valor = BigDecimal.valueOf(Double.valueOf(valorTcolum));
                        }
                        montoTotalColumna = montoTotalColumna.add(valor);
                    }
                }
                montoTotalTotales = montoTotalTotales.add(montoTotalColumna);
            }

        } else {

            List<BloqueComponenteDTO> lstFilas = listaBloqueComponente.stream()
                    .filter(f -> f.getItFila()).collect(Collectors.toList());

            for (BloqueComponenteDTO bloqCom : lstFilas) {
                bloqCom.setDatos(bloqCom.getDatos().stream()
                        .filter(p -> p.getExpedienteDTO() != null
                                && p.getExpedienteDTO().getIdExpediente() != null
                                && p.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente()))
                        .collect(Collectors.toList()));
            }

            for (int i = 0; i < convocatoriaDTO.getNmDurMaxAyuda(); i++) {
                BigDecimal montoTotalColumna = BigDecimal.ZERO;
                for (BloqueComponenteDTO lstFila : lstFilas) {
                    if (Objects.nonNull(lstFila) && lstFila.getDatos().size() > i
                            && Objects.nonNull(lstFila.getDatos().get(i))) {
                        String dsVal = lstFila.getDatos().get(i).getDsValor();
                        if (dsVal != null && !dsVal.trim().isEmpty()) {
                            montoTotalColumna = montoTotalColumna
                                    .add(BigDecimal.valueOf(Double.valueOf(dsVal.trim())));
                        }
                    }
                }
                montoTotalTotales = montoTotalTotales.add(montoTotalColumna);
            }
        }

        return montoTotalTotales;
    }

    /**
     *
     */
    /**
     * VMM 03/09/26 MD-3011: la colección getDatos() de un BloqueComponenteDTO puede traer datos ya dados
     * de baja (fcBajaFila != null) y, si por un guardado duplicado hay más de un dato vigente
     * para la misma fila/columna, también duplicados. Filtramos los vigentes de este expediente
     * y nos quedamos con el más reciente por fila/columna (mayor fcAltaFila, desempatando por
     * idDato), para no sumar/mezclar importes duplicados ni obsoletos en los totales del
     * presupuesto del PDF de la solicitud.
     */
    private List<DatoDTO> filtrarYDeduplicarDatosVigentes(List<DatoDTO> datos, Integer idExpediente) {
        List<DatoDTO> vigentes = datos.stream()
                .filter(d -> d.getFcBajaFila() == null)
                .filter(d -> d.getExpedienteDTO() != null
                        && d.getExpedienteDTO().getIdExpediente() != null
                        && d.getExpedienteDTO().getIdExpediente().equals(idExpediente))
                .collect(Collectors.toList());

        return new ArrayList<>(vigentes.stream()
                .collect(Collectors.toMap(
                        d -> d.getNmFila() + "-" + d.getNmColumna(),
                        d -> d,
                        (d1, d2) -> {
                            if (d1.getFcAltaFila() == null) {
                                return d2;
                            }
                            if (d2.getFcAltaFila() == null) {
                                return d1;
                            }
                            int cmp = d1.getFcAltaFila().compareTo(d2.getFcAltaFila());
                            if (cmp != 0) {
                                return cmp > 0 ? d1 : d2;
                            }
                            return (d1.getIdDato() != null && d2.getIdDato() != null
                                    && d1.getIdDato() > d2.getIdDato()) ? d1 : d2;
                        }))
                .values());
    }

    @Override
    public HashMap<String, String> rellenarCampos(ExpedienteDTO dto) throws ServiceException {

        HashMap<String, String> hamCampos = new HashMap<>();

        try {

            // Rellenamos los campos relacionables
            hamCampos = this.rellenarCamposRelacionables(hamCampos, dto);

            HashMap<Integer, Integer> hamFilaBloque = new HashMap<>();

            // Iteramos los bloques del formulario
            for (BloqueDTO bloque : dto.getImpresoDTO().getLstBloqueDTO()) {
                Integer nmFilas = 1;

                if (Constantes.Formatos.ID_LISTADO.equals(bloque.getIdFormato().getNmConstante()) || Constantes.Formatos.ID_CUENTA_BANCARIA.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = bloque.getNmFilas();
                } else if (Constantes.Formatos.ID_TABLA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = 0;

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloque
                            .getLstBloqueComponenteDTO()) {
                        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())) {
                            nmFilas++;
                        }
                    }

                }

                hamFilaBloque.put(bloque.getIdBloque(), nmFilas);

                // Rellenamos los datos de los participantes
                if (Boolean.TRUE.equals(Constantes.Formatos.ID_PARTICIPANTES.equals(bloque.getIdFormato().getNmConstante()))) {

                    List<ExpedienteDTO> lstExpedientesDTO = this.expedienteService
                            .buscarParticipantesPorSolicitud(dto.getIdSolicitud(),
                                    bloque.getActor().getIdActor());

                    for (Integer i = 0; i < lstExpedientesDTO.size(); i++) {
                        String nombreCampo = "_" + bloque.getIdBloque() + "_" + i;
                        ExpedienteDTO expedienteDTO = lstExpedientesDTO.get(i);
                        Boolean esPF = Constantes.Identificacion.FISICA.equals(bloque.getActor().getIdentificacionDTO().getNmConstante());

                        if (Boolean.TRUE.equals(esPF)) {
                            hamCampos.put("dni" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getCdNumIden());
                            hamCampos.put("nombre" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsNombre());
                            hamCampos.put("apellido1" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsApellido1());
                            hamCampos.put("apellido2" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsApellido2());
                        } else {
                            hamCampos.put("nif" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getCdNumIden());
                            hamCampos.put("razonSocial" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsNombre());
                        }
                    }

                }

                // Preparamos los datos del bloque presupuesto
                if (Boolean.TRUE.equals(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(bloque.getIdFormato().getNmConstante()))) {
                    
                    ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                            .buscarId(dto.getConvocatoriaActDTO().getIdConvocatoria());
                    
                    if(convocatoriaDTO.getItConceptoPresup() != null && convocatoriaDTO.getItConceptoPresup().equalsIgnoreCase("S")) {
                        
                        List<BloqueComponenteDTO> listaBloqueComponente = bloque
                                .getLstBloqueComponenteDTO().stream()
                                .filter(x -> x.getFcBajaFila() == null)
                                .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                                .collect(Collectors.toList());

                       

                        List<BloqueComponenteDTO> lstFilasAux = listaBloqueComponente.stream()
                                .filter(f -> f.getItFila()).collect(Collectors.toList());
                        
                        for (BloqueComponenteDTO bloqCom : lstFilasAux) {
                            bloqCom.setDatos(this.filtrarYDeduplicarDatosVigentes(bloqCom.getDatos(), dto.getIdExpediente()));
                        }

                        List<BloqueComponenteDTO> lstFilas = new ArrayList<>();
                        
                        for(BloqueComponenteDTO bloqueComp:lstFilasAux) {
                            if(bloqueComp.getDatos().size()== convocatoriaDTO.getNmDurMaxAyuda()+1) {
                                bloqueComp.setDatos(bloqueComp.getDatos().stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList()));
                                lstFilas.add(bloqueComp);
                            }else if(bloqueComp.getDatos().size()> convocatoriaDTO.getNmDurMaxAyuda()+2) {
                                int posCelda= 0;
                                bloqueComp.setDatos(bloqueComp.getDatos().stream().sorted(Comparator.comparing(DatoDTO::getNmFila)).collect(Collectors.toList()));
                                for (int j=posCelda; j< bloqueComp.getDatos().size();j++){
                                    List<DatoDTO> datosFilaConcepto= new ArrayList<>();
                                    BloqueComponenteDTO bloqueComConcepto= new BloqueComponenteDTO();
                                    for (int index = 0; index < convocatoriaDTO.getNmDurMaxAyuda()+1; index++) {
                                        datosFilaConcepto.add(bloqueComp.getDatos().get(posCelda));
                                        posCelda++;
                                        j=posCelda;
                                    }
                                    
                                    datosFilaConcepto= datosFilaConcepto.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                                    bloqueComConcepto.setIdBloque(bloqueComp.getIdBloque());
                                    bloqueComConcepto.setDatos(datosFilaConcepto);
                                    bloqueComConcepto.setIdBloqueComponente(bloqueComp.getIdBloqueComponente());
                                    bloqueComConcepto.setCdUsuAlta(bloqueComp.getCdUsuAlta());
                                    bloqueComConcepto.setComponenteDTO(bloqueComp.getComponenteDTO());
                                    bloqueComConcepto.setNmOrden(bloqueComp.getNmOrden());
                                    bloqueComConcepto.setItFila(bloqueComp.getItFila());
                                    bloqueComConcepto.setItColumna(bloqueComp.getItColumna());
                                    bloqueComConcepto.setItObligatorio(bloqueComp.getItObligatorio());
                                    bloqueComConcepto.setItRelacion(bloqueComp.getItRelacion());
                                    bloqueComConcepto.setTipoFinanciacionDTO(bloqueComp.getTipoFinanciacionDTO());
                                    bloqueComConcepto.setItSeleccionado(bloqueComp.getItSeleccionado());
                                    lstFilas.add(bloqueComConcepto);
                                    
                                }
                                   
                                    
                            }
                        }
                        
                        lstFilas = lstFilas.stream()
                                .filter(f -> f.getItFila()).collect(Collectors.toList());
                        
                        for (int i = 0; i < lstFilas.size(); i++) {
                            
                            List<DatoDTO> listaDatos= lstFilas.get(i).getDatos();
                            BigDecimal montoTotalFila =BigDecimal.ZERO;
                           // listaDatos= listaDatos.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                            for (DatoDTO datoDTO : listaDatos) {

                                if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                                    String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                            + lstFilas.get(i).getIdBloqueComponente() + "_"
                                            + (datoDTO.getNmFila() + 1) + "_"
                                            + (datoDTO.getNmColumna() + 1);

                                    String valorCampo ="";
                                    if(datoDTO.getDsValor() != null) {
                                        valorCampo =datoDTO.getDsValor().trim();
                                    }
                                    
                                    
                                    if(datoDTO.getNmColumna().intValue() != 0) {
                                        if(valorCampo.equals("")) {
                                            valorCampo="0.00";
                                        }
                                        BigDecimal decimalValorCampo = BigDecimal.valueOf(Double.valueOf(valorCampo));
                                        
                                        String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                                        hamCampos.put(nombreCampo, decimalValorString);

                                        montoTotalFila = montoTotalFila.add(decimalValorCampo);
                                    }else {
                                        hamCampos.put(nombreCampo,valorCampo);
                                    }

                                    

                                }

                            }

                            BigDecimal decimalMontoTotalFila = montoTotalFila;
                            String nombreCampo = "TF_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);

                            String decimalValorStringMontoTotalFila = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                            hamCampos.put(nombreCampo, decimalValorStringMontoTotalFila);
                            
                           

                        }
                       
                      
                        BigDecimal montoTotalTotales = BigDecimal.ZERO;

                        for (int i = 1; i < convocatoriaDTO.getNmDurMaxAyuda()+1; i++) {

                            BigDecimal montoTotalColumna = BigDecimal.ZERO;

                            for (BloqueComponenteDTO lstFila : lstFilas) {
                                if (Objects.nonNull(lstFila) && lstFila.getDatos().size() > i
                                        && Objects.nonNull(lstFila.getDatos().get(i))
                                        && lstFila.getDatos().get(i).getNmColumna() != null
                                        && lstFila.getDatos().get(i).getNmColumna().intValue() != 0) {
                                    String valorTcolum=lstFila.getDatos().get(i).getDsValor();
                                    BigDecimal valor= BigDecimal.ZERO;
                                    if(valorTcolum != null && !valorTcolum.trim().equals("") ) {
                                        valor=BigDecimal.valueOf(Double.valueOf(valorTcolum));
                                    }
                                    
                                    montoTotalColumna = montoTotalColumna.add(valor);
                                    
                                           
                                }
                            }
                            
                            BigDecimal decimalMontoTotalColumna = montoTotalColumna;
                            String nombreCampo = "TC_" + bloque.getIdBloque() + "_"
                                    + (lstFilas.size() + 1) + "_" + (i + 1);
                            
                            String decimalValorStringMontoTotal = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                            hamCampos.put(nombreCampo, decimalValorStringMontoTotal);

                            montoTotalTotales =montoTotalTotales.add(montoTotalColumna);

                        }

                        BigDecimal decimalMontoTotalTotales = montoTotalTotales;
                        String nombreCampo = "TT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                                + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                        String decimalValorStringMontoTotalTotales = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);
                        hamCampos.put(nombreCampo, decimalValorStringMontoTotalTotales);

                        // parametros de ayuda solicitada calculada en funcion de intensidad de ayuda
                        BigDecimal intensidadAyuda = BigDecimal.valueOf(convocatoriaDTO.getPorcentajeIntensidadAyuda() != null ? convocatoriaDTO.getPorcentajeIntensidadAyuda() : 0.0);
                        
                        for (int i = 0; i < lstFilas.size(); i++) {

                            BigDecimal montoTotalFila = BigDecimal.ZERO;

                            for (DatoDTO datoDTO : lstFilas.get(i).getDatos()) {

                                if (StringUtils.isNotBlank(datoDTO.getDsValor())) {
                                 
                                     nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                            + lstFilas.get(i).getIdBloqueComponente() + "_"
                                            + (datoDTO.getNmFila() + 1) + "_"
                                            + (datoDTO.getNmColumna() + 1);

                                    String valorCampo ="";
                                    if(datoDTO.getDsValor() != null) {
                                        valorCampo =datoDTO.getDsValor().trim();
                                    }
                                    
                                    if(datoDTO.getNmColumna().intValue() != 0) {
                                        if(valorCampo.equals("")) {
                                            valorCampo="0.00";
                                        }
                                        BigDecimal decimalValorCampo = (BigDecimal.valueOf(Double.valueOf(valorCampo)).multiply(intensidadAyuda))
                                                .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                                        
                                        String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                                        hamCampos.put(nombreCampo, decimalValorString);

                                        montoTotalFila = montoTotalFila.add(BigDecimal.valueOf(Double.valueOf(valorCampo)));
                                    }else {
                                        hamCampos.put(nombreCampo,valorCampo);
                                    }
                                    

                                }

                            }

                            BigDecimal decimalMontoTotalFila = (montoTotalFila.multiply(intensidadAyuda))
                                    .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                            
                            nombreCampo = "ATF_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);
                            
                            String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                            hamCampos.put(nombreCampo, decimalValorString);

                        }

                         montoTotalTotales = BigDecimal.ZERO;

                        for (int i = 1; i < convocatoriaDTO.getNmDurMaxAyuda()+1; i++) {

                            BigDecimal montoTotalColumna = BigDecimal.ZERO;

                            for (BloqueComponenteDTO lstFila : lstFilas) {
                                if (Objects.nonNull(lstFila) && !lstFila.getDatos().isEmpty()
                                        && Objects.nonNull(lstFila.getDatos().get(i))) {
                                    String valorTotalColumn=lstFila.getDatos().get(i).getDsValor();
                                    if(valorTotalColumn== null) {
                                        valorTotalColumn="0.00";
                                    }
                                    montoTotalColumna = montoTotalColumna.add(BigDecimal.valueOf(Double.valueOf(valorTotalColumn)));
                                }
                            }
                            BigDecimal decimalMontoTotalColumna = (montoTotalColumna.multiply(intensidadAyuda))
                            .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                            nombreCampo = "ATC_" + bloque.getIdBloque() + "_"
                                    + (lstFilas.size() + 1) + "_" + (i + 1);
                            String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                            hamCampos.put(nombreCampo, decimalValorString);

                            montoTotalTotales = montoTotalColumna;

                        }

                         decimalMontoTotalTotales = (montoTotalTotales.multiply(intensidadAyuda))
                                 .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                         nombreCampo = "ATT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                                + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                         String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);
                         hamCampos.put(nombreCampo, decimalValorString);
                        
                    }else {
                        
                        List<BloqueComponenteDTO> listaBloqueComponente = bloque
                                .getLstBloqueComponenteDTO().stream()
                                .filter(x -> x.getFcBajaFila() == null)
                                .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                                .collect(Collectors.toList());

                       

                        List<BloqueComponenteDTO> lstFilas = listaBloqueComponente.stream()
                                .filter(f -> f.getItFila()).collect(Collectors.toList());
                        
                        for (BloqueComponenteDTO bloqCom : lstFilas) {
                            bloqCom.setDatos(this.filtrarYDeduplicarDatosVigentes(bloqCom.getDatos(), dto.getIdExpediente()));
                        }

                        for (int i = 0; i < lstFilas.size(); i++) {

                            BigDecimal montoTotalFila = BigDecimal.ZERO;

                            for (DatoDTO datoDTO : lstFilas.get(i).getDatos()) {

                                if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                                    String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                            + lstFilas.get(i).getIdBloqueComponente() + "_"
                                            + (datoDTO.getNmFila() + 1) + "_"
                                            + (datoDTO.getNmColumna() + 1);

                                    String valorCampo = datoDTO.getDsValor().trim();

                                    BigDecimal decimalValorCampo = BigDecimal.valueOf(Double.valueOf(valorCampo));
                                    String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                                    hamCampos.put(nombreCampo, decimalValorString);

                                    montoTotalFila = montoTotalFila.add(decimalValorCampo);

                                }

                            }

                            BigDecimal decimalMontoTotalFila = montoTotalFila;
                            String nombreCampo = "TF_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);

                            String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                            hamCampos.put(nombreCampo, decimalValorString);

                        }

                        BigDecimal montoTotalTotales = BigDecimal.ZERO;

                        for (int i = 0; i < convocatoriaDTO.getNmDurMaxAyuda(); i++) {

                            BigDecimal montoTotalColumna = BigDecimal.ZERO;

                            for (BloqueComponenteDTO lstFila : lstFilas) {
                                if (Objects.nonNull(lstFila) && lstFila.getDatos().size() > i
                                        && Objects.nonNull(lstFila.getDatos().get(i))) {
                                    String dsVal = lstFila.getDatos().get(i).getDsValor();
                                    if (dsVal != null && !dsVal.trim().isEmpty()) {
                                        montoTotalColumna = montoTotalColumna.add(BigDecimal.valueOf(Double.valueOf(dsVal.trim())));
                                    }
                                }
                            }
                            BigDecimal decimalMontoTotalColumna = montoTotalColumna;
                            String nombreCampo = "TC_" + bloque.getIdBloque() + "_"
                                    + (lstFilas.size() + 1) + "_" + (i + 1);
                            String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                            hamCampos.put(nombreCampo, decimalValorString);
                            
                            montoTotalTotales =montoTotalTotales.add(montoTotalColumna);

                        }

                        BigDecimal decimalMontoTotalTotales = montoTotalTotales;
                        String nombreCampo = "TT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                                + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                        String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);
                        hamCampos.put(nombreCampo, decimalValorString);
                        // parametros de ayuda solicitada calculada en funcion de intensidad de ayuda
                        BigDecimal intensidadAyuda = BigDecimal.valueOf(convocatoriaDTO.getPorcentajeIntensidadAyuda() != null ? convocatoriaDTO.getPorcentajeIntensidadAyuda() : 0.0);
                        
                        for (int i = 0; i < lstFilas.size(); i++) {

                            BigDecimal montoTotalFila = BigDecimal.ZERO;

                            for (DatoDTO datoDTO : lstFilas.get(i).getDatos()) {

                                if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                                     nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                            + lstFilas.get(i).getIdBloqueComponente() + "_"
                                            + (datoDTO.getNmFila() + 1) + "_"
                                            + (datoDTO.getNmColumna() + 1);

                                    String valorCampo = datoDTO.getDsValor().trim();

                                    BigDecimal decimalValorCampo = (BigDecimal.valueOf(Double.valueOf(valorCampo)).multiply(intensidadAyuda))
                                            .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);

                                    String decimalValorStringCampo = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                                    hamCampos.put(nombreCampo, decimalValorStringCampo);

                                    montoTotalFila =montoTotalFila.add(BigDecimal.valueOf(Double.valueOf(valorCampo)));

                                }

                            }

                            BigDecimal decimalMontoTotalFila = (montoTotalFila.multiply(intensidadAyuda))
                                    .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                            
                             nombreCampo = "ATF_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);
                            String decimalValorStringTotal = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);

                            hamCampos.put(nombreCampo, decimalValorStringTotal);

                        }

                         montoTotalTotales = BigDecimal.ZERO;

                        for (int i = 0; i < convocatoriaDTO.getNmDurMaxAyuda(); i++) {

                            BigDecimal montoTotalColumna = BigDecimal.ZERO;

                            for (BloqueComponenteDTO lstFila : lstFilas) {
                                if (Objects.nonNull(lstFila) && lstFila.getDatos().size() > i
                                        && Objects.nonNull(lstFila.getDatos().get(i))) {
                                    String dsVal = lstFila.getDatos().get(i).getDsValor();
                                    if (dsVal != null && !dsVal.trim().isEmpty()) {
                                        montoTotalColumna = montoTotalColumna.add(BigDecimal.valueOf(Double.valueOf(dsVal.trim())));
                                    }
                                }
                            }
                            BigDecimal decimalMontoTotalColumna = (montoTotalColumna.multiply(intensidadAyuda))
                            .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                             nombreCampo = "ATC_" + bloque.getIdBloque() + "_"
                                    + (lstFilas.size() + 1) + "_" + (i + 1);
                             String decimalValorStringMontoTotal = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                             hamCampos.put(nombreCampo, decimalValorStringMontoTotal);

                            montoTotalTotales =montoTotalTotales.add(montoTotalColumna);

                        }

                         decimalMontoTotalTotales = (montoTotalTotales.multiply(intensidadAyuda))
                                 .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                         nombreCampo = "ATT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                                + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                         String decimalValorStringMontoTotalTotales = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);

                         hamCampos.put(nombreCampo, decimalValorStringMontoTotalTotales);
                    }

                   

                }

                if (Boolean.TRUE.equals(Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(bloque.getIdFormato().getNmConstante()))) {

                    List<BloqueComponenteDTO> listaBloqueComponente = dto.getImpresoDTO()
                            .getLstBloqueDTO().stream()
                            .filter(f -> f.getFcBajaFila() == null && f.getEsTipoAyudaSolicitada())
                            .flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                            .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

                    List<BloqueComponenteDTO> lstColumnas = listaBloqueComponente.stream()
                            .filter(f -> f.getItColumna()).collect(Collectors.toList());

                    for (int i = 0; i < lstColumnas.size(); i++) {

                        for (DatoDTO datoDTO : lstColumnas.get(i).getDatos()) {

                            if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                                String nombreCampo = lstColumnas.get(i).getIdBloqueComponente()
                                        + "_" + (i + 1);
                                BigDecimal decimalMontoTotalTotales = this.parsearValorFlexible(datoDTO.getDsValor());
                                String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);
                                hamCampos.put(nombreCampo, decimalValorString);

                            }

                        }

                    }

                }

                if (Boolean.TRUE.equals(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(bloque.getIdFormato().getNmConstante()))) {
                    
                    Integer idComFinTolImpInv = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_INVERSION);
                    Integer idComFinTolImpAyu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_AYUDA);
                    Integer idComFinActu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.ACTUACION);
                    Integer idComFinCodActu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.CODIGO_ACTUACION);
                    Integer idComFinOfeSelProv = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_PROVEEDOR);
                    Integer idComFinOfeSelImp = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE);
                    
                    List<BloqueComponenteDTO> listaBloqueComponente = dto.getImpresoDTO()
                            .getLstBloqueDTO().stream()
                            .filter(f -> f.getFcBajaFila() == null && f.getEsTipoActuaciones())
                            .flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                            .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

                    // tabla seleccion de actuaciones
                    List<BloqueComponenteDTO> lstFilas = listaBloqueComponente.stream()
                            .filter(f -> f.getItFila() && Objects.isNull(f.getFcBajaFila()))
                            .collect(Collectors.toList());

                    List<BloqueComponenteDTO> lstColumnas = listaBloqueComponente.stream()
                            .filter(f -> f.getItColumna() && Objects.isNull(f.getFcBajaFila()))
                            .collect(Collectors.toList());

                    // actualizamos el indicador de actuacion seleccionada
                    for (BloqueComponenteDTO fila : lstFilas) {

                        if (lstColumnas.stream()
                                .anyMatch(f -> Objects.nonNull(f.getDatos())
                                        && f.getDatos().stream()
                                                .anyMatch(p -> Objects
                                                        .nonNull(p.getNmFila())
                                                        && p.getNmFila() + 1 == fila.getNmOrden())
                                        && !idComFinTolImpInv.equals(f.getComponenteDTO().getIdComponente())
                                        && !idComFinTolImpAyu.equals(f.getComponenteDTO().getIdComponente()))) {
                            fila.setItSeleccionado(Boolean.TRUE);
                        } else {
                            fila.setItSeleccionado(Boolean.FALSE);
                        }

                    }

                    // preparamos los check marcados
                    for (BloqueComponenteDTO fila : lstFilas) {
                        if (Boolean.TRUE.equals(fila.getItSeleccionado())) {
                            String nombreFila = "chk_" + fila.getTipoFinanciacionDTO().getCdCodigo()
                                    + "_" + fila.getNmOrden();
                            hamCampos.put(nombreFila, "true");
                        }
                    }

                    // tabla detalle de actuaciones
                    List<BloqueComponenteDTO> filasSeleccionadas = lstFilas.stream()
                            .filter(f -> Boolean.TRUE.equals(f.getItSeleccionado()))
                            .collect(Collectors.toList());

                    int indexColumna = 1;
                    for (BloqueComponenteDTO lstColumna : lstColumnas) {

                        if (!idComFinActu.equals(lstColumna.getComponenteDTO().getIdComponente())
                                && !idComFinOfeSelProv.equals(lstColumna.getComponenteDTO().getIdComponente())
                                && !idComFinOfeSelImp.equals(lstColumna.getComponenteDTO().getIdComponente())
                                && !idComFinTolImpInv.equals(lstColumna.getComponenteDTO().getIdComponente())
                                && !idComFinTolImpAyu.equals(lstColumna.getComponenteDTO().getIdComponente())) {

                            for (int indexFila = 0; indexFila < filasSeleccionadas
                                    .size(); indexFila++) {

                                // código de actuación
                                if (idComFinCodActu.equals(lstColumna.getComponenteDTO().getIdComponente())) {

                                    String nombreCampo = "DT_COD_" + (indexFila + 1) + "_"
                                            + indexColumna;

                                    String valorCampo = filasSeleccionadas.get(indexFila)
                                            .getTipoFinanciacionDTO().getCdCodigo().toUpperCase()
                                            .trim();

                                    hamCampos.put(nombreCampo, valorCampo);

                                } else {

                                    if (Constantes.TipoComponente.ID_CAMPO.equals(lstColumna.getComponenteDTO().getTipoDTO().getNmConstante())
                                            && StringUtils.isNotBlank(lstColumna.getDatos()
                                                    .get(indexFila).getDsValor())) {

                                        String nombreCampo = "DT_IMP_" + (indexFila + 1) + "_"
                                                + indexColumna;

                                        String valorCampo = lstColumna.getDatos().get(indexFila)
                                                .getDsValor().toUpperCase().trim();

                                        log.info(String.valueOf(valorCampo.length()));
                                        if (valorCampo.length() > 9) {
                                            String cadFinal = "";
                                            int cont = 0;
                                            for (char c : valorCampo.toCharArray()) {

                                                if (cont == 9) {
                                                    cadFinal += "\n";
                                                    cont = 0;
                                                }

                                                cadFinal += String.valueOf(c);
                                                cont++;
                                            }

                                            valorCampo = cadFinal;
                                        }

                                        if (Constantes.SubTipoComponente.ID_NUMERO.equals(lstColumna.getComponenteDTO().getSubTipoDTO().getNmConstante())) {
                                            BigDecimal decimalMontoTotalFila = new BigDecimal(
                                                    valorCampo);
                                            String decimalValorStringMontoTotalFila = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                                            hamCampos.put(nombreCampo, decimalValorStringMontoTotalFila);
                                            
                                        } else {
                                            hamCampos.put(nombreCampo, valorCampo);
                                        }

                                    } else if (Constantes.TipoComponente.ID_CHECK.equals(lstColumna.getComponenteDTO().getTipoDTO().getNmConstante())
                                            && StringUtils.isNotBlank(lstColumna.getDatos()
                                                    .get(indexFila).getDsValor())) {

                                        String nombreCampo = "chk_" + (indexFila + 1) + "_"
                                                + indexColumna + "_"
                                                + lstColumna.getDatos().get(indexFila).getDsValor();

                                        hamCampos.put(nombreCampo, "true");

                                    }

                                }

                            }

                            indexColumna++;
                        }

                    }

                    // tabla datos seleccionados
                    indexColumna = 1;
                    for (BloqueComponenteDTO lstColumna : lstColumnas) {

                        if (idComFinCodActu.equals(lstColumna.getComponenteDTO().getIdComponente())
                                || idComFinOfeSelProv.equals(lstColumna.getComponenteDTO().getIdComponente())
                                || idComFinOfeSelImp.equals(lstColumna.getComponenteDTO().getIdComponente())) {

                            for (int indexFila = 0; indexFila < filasSeleccionadas
                                    .size(); indexFila++) {

                                if (idComFinCodActu.equals(lstColumna.getComponenteDTO().getIdComponente())) {

                                    String nombreCampo = "DT_IMP_SEL_" + (indexFila + 1) + "_"
                                            + indexColumna;

                                    String valorCampo = filasSeleccionadas.get(indexFila)
                                            .getTipoFinanciacionDTO().getCdCodigo().toUpperCase()
                                            .trim();

                                    hamCampos.put(nombreCampo, valorCampo);

                                } else {
                                    String nombreCampo = "DT_IMP_SEL_" + (indexFila + 1) + "_"
                                            + indexColumna;

                                    String valorCampo = lstColumna.getDatos().get(indexFila)
                                            .getDsValor().toUpperCase().trim();

                                    if (valorCampo.length() > 27) {
                                        String cadFinal = "";
                                        int cont = 0;
                                        for (char c : valorCampo.toCharArray()) {

                                            if (cont == 27) {
                                                cadFinal += "\n";
                                                cont = 0;
                                            }

                                            cadFinal += String.valueOf(c);
                                            cont++;
                                        }

                                        valorCampo = cadFinal;
                                    }

                                    hamCampos.put(nombreCampo, valorCampo);

                                    if (Constantes.SubTipoComponente.ID_NUMERO.equals(lstColumna.getComponenteDTO().getSubTipoDTO().getNmConstante())) {

                                        BigDecimal decimalMontoTotalFila = new BigDecimal(
                                                valorCampo);

                                        String decimalValorStringMontoTotalFila = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                                        hamCampos.put(nombreCampo, decimalValorStringMontoTotalFila);

                                    }else if(Constantes.TipoComponente.ID_RADIO.equals(lstColumna.getComponenteDTO().getTipoDTO().getNmConstante())) {
                                        nombreCampo= "radio_" +nombreCampo;
                                        hamCampos.put(nombreCampo, valorCampo);
                                    } else {
                                        hamCampos.put(nombreCampo, valorCampo);
                                    }

                                }

                            }
                            indexColumna++;

                        }

                    }

                    // tabla ayuda solicitada
                    indexColumna = 1;
                    for (BloqueComponenteDTO lstColumna : lstColumnas) {

                        if (idComFinTolImpInv.equals(lstColumna.getComponenteDTO().getIdComponente())
                                || idComFinTolImpAyu.equals(lstColumna.getComponenteDTO().getIdComponente())) {

                            String nombreCampo = "DT_IMP_AS_1_" + indexColumna;

                            if (lstColumna.getDatos().isEmpty() || lstColumna.getDatos().get(0) == null
                                    || lstColumna.getDatos().get(0).getDsValor() == null) {
                                indexColumna++;
                                continue;
                            }
                            String valorCampo = lstColumna.getDatos().get(0).getDsValor()
                                    .toUpperCase().trim();

                           /* BigDecimal decimalMontoTotalFila = new BigDecimal(valorCampo);
                            
                            String decimalValorStringMontoTotalFila = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                            hamCampos.put(nombreCampo, decimalValorStringMontoTotalFila);*/
                            
                            hamCampos.put(nombreCampo, valorCampo);

                            indexColumna++;

                        }

                    }

                }
                
                if ( (Boolean.TRUE.equals(Constantes.Formatos.ID_FORMULARIO.equals(bloque.getIdFormato().getNmConstante()))
                        ||Boolean.TRUE.equals(Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD.equals(bloque.getIdFormato().getNmConstante()))
                        )
                        &&
                        bloque.getLstBloqueComponenteDTO() != null) {
                    List<BloqueComponenteDTO> listaBloqueComponente = bloque
                            .getLstBloqueComponenteDTO().stream()
                            .filter(x -> x.getFcBajaFila() == null)
                            .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                            .collect(Collectors.toList());
                    for(BloqueComponenteDTO bc : listaBloqueComponente) {
                        if (bc.getComponenteDTO() != null && bc.getComponenteDTO().getSubTipoDTO() != null
                                && Constantes.SubTipoComponente.ID_FECHA.equals(bc.getComponenteDTO().getSubTipoDTO().getNmConstante())
                                && bc.getDatos() != null && !bc.getDatos().isEmpty()
                                && StringUtils.isNotBlank(bc.getDatos().get(0).getDsValor())){
                            // Obtener el valor de fecha actual (formato ISO)
                            String fechaStr = bc.getDatos().get(0).getDsValor();
                            
                            try {
                                // Convertir de String a Date
                                Instant instant = Instant.parse(fechaStr);
                                Date fecha = Date.from(instant);

                                // Formatear a dd/MM/yyyy en la zona horaria de Madrid,
                                // para no restar un día si la zona por defecto de la JVM no es Europe/Madrid
                                SimpleDateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
                                String fechaFormateada = sdf.format(fecha);

                                // Guardar el nuevo valor (en formato dd/MM/yyyy)
                                bc.getDatos().get(0).setDsValor(fechaFormateada);

                            } catch (Exception e) {
                                log.error("Error al convertir fecha: " + fechaStr);
                            }
                        }
                    }
                }
            }

            // Extraemos la lista de bloques componentes del expediente que no
            // estén
            // dados de baja
            List<BloqueComponenteDTO> listaBloqueComponente = dto.getImpresoDTO().getLstBloqueDTO()
                    .stream()
                    .filter(f -> !f.getEsTipoPresupuestoPresentado() && !f.getEsTipoPresupuestoFinanciable()
                            && !f.getEsTipoAyudaSolicitada() && !f.getEsTipoActuaciones())
                    .flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                    .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

            // Rellenamos el hashmap de datos
            
            Integer contador=1;
            for (BloqueComponenteDTO bloqueComponente : listaBloqueComponente) {

                Integer nmFila = hamFilaBloque.get(bloqueComponente.getIdBloque());
                SimpleDateFormat formatter = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);

                if (Objects.isNull(bloqueComponente.getTipoFinanciacionDTO())
                        || Objects.isNull(bloqueComponente.getTipoFinanciacionDTO().getIdValor())) {

                    for (Integer i = 0; i < nmFila; i++) {
                        String nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (i + 1);
                        String valorCampo = StringUtils.EMPTY;

                        if (bloqueComponente.getDatos() != null
                                && bloqueComponente.getDatos().size() > i) {
                            if (Objects.nonNull(bloqueComponente.getDatos().get(i))) {
                                valorCampo = bloqueComponente.getDatos().get(i).getDsValor();
                            }
                        }

                        if (bloqueComponente.getComponenteDTO().getTipoDTO().getIdValor() != null) {
                            if (Constantes.TipoComponente.ID_CHECK.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {

                                if (bloqueComponente.getDatos() != null && StringUtils.isNotBlank(
                                        bloqueComponente.getDatos().get(i).getDsValor())) {
                                    String[] valoresCheck = bloqueComponente.getDatos().get(i)
                                            .getDsValor().split(";");
                                    for (Integer j = 0; j < valoresCheck.length; j++) {
                                        hamCampos.put("chk_" + nombreCampo + "_" + (j + 1),
                                                valoresCheck[j]);
                                    }
                                }

                                // Saltamos a otro componente
                                continue;
                            } else if (bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante()
                                    .equals(Constantes.TipoComponente.ID_COMBO)) {
                                // El tipo combo necesita el formato:
                                // idBloque_nmfila.idBloque_nmFila
                                nombreCampo = nombreCampo + "." + nombreCampo;
                               
                            }else if(Constantes.TipoComponente.ID_RADIO.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {
                                nombreCampo= "radio_" +nombreCampo;
                                hamCampos.put(nombreCampo, valorCampo);
                            }
                        } else if (bloqueComponente.getComponenteDTO().getTipoDocumentoDTO() != null
                                && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
                            // Es un bloque de documentación
                            nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                    + (contador);
                            nombreCampo = "chk_" + nombreCampo + "_doc_1";
                            contador++;
                            // Buscamos el expedienteDoc
                            Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                                    .getLstExpedienteDocDTO().stream()
                                    .filter(x -> x.getBloqueComponenteDTO() != null && x
                                            .getBloqueComponenteDTO().getIdBloqueComponente()
                                            .equals(bloqueComponente.getIdBloqueComponente()))
                                    .findFirst();

                            if (expedienteDocDTO.isPresent()) {
                                // Si hay código de FICA, marcamos el checkbox
                                // del
                                // PDF
                                if (expedienteDocDTO.get().getCdFica() != null) {
                                    valorCampo = "true";
                                }
                            }

                        } else if (bloqueComponente.getComponenteDTO().getSubTipoDTO() != null
                                && Constantes.SubTipoComponente.ID_FECHA.equals(bloqueComponente.getComponenteDTO().getSubTipoDTO().getNmConstante())) {
                            // Es un campo de fecha, lo formateamos
                            formatter.format(valorCampo);
                        }

                        hamCampos.put(nombreCampo, valorCampo);

                    }

                }
            }
            
            
            
            // docuemntos tipo C
            
            List<BloqueDTO> listaBloquesDocumento= dto.getImpresoDTO().getLstBloqueDTO().stream()
                    .filter(f ->  f.getFcBajaFila() == null && f.getEsTipoDocumento() && !f.getItAutorizaConsulta() 
                            ).collect(Collectors.toList());
            for (BloqueDTO bloque :listaBloquesDocumento) {
                contador=1;
                List<BloqueComponenteDTO> componentes= bloque.getLstBloqueComponenteDTO();
                Comparator<BloqueComponenteDTO> compareByNumOrdens = (BloqueComponenteDTO c1,
                        BloqueComponenteDTO c2) -> c1.getNmOrden().compareTo(c2.getNmOrden());

                if (!componentes.isEmpty()) {
                    Collections.sort(componentes, compareByNumOrdens);
                }
                // Rellenamos el hashmap de datos
                for (BloqueComponenteDTO bloqueComponente :componentes ) {
                    String nombreCampo="";
                    String valorCampo="";
                    
                 // Buscamos el expedienteDoc
                    Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                            .getLstExpedienteDocDTO().stream()
                            .filter(x -> x.getBloqueComponenteDTO() != null && x
                                    .getBloqueComponenteDTO().getIdBloqueComponente()
                                    .equals(bloqueComponente.getIdBloqueComponente()))
                            .findFirst();
                    
                   if( !Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc()) && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO()
                            .getIdValor() != null) {
                        
                        // Es un bloque de documentación
                        nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (contador);
                        nombreCampo = "chk_" + nombreCampo + "_doc_1";
                        contador++;
                       
                        if (expedienteDocDTO.isPresent()) {
                            // Si hay código de FICA, marcamos el checkbox
                            // del
                            // PDF
                            if (expedienteDocDTO.get().getCdFica() != null) {
                                valorCampo = "true";
                            }
                        }
                        
                    }
                    hamCampos.put(nombreCampo, valorCampo);
                   
                    
                }
            }
            
            //FIN DOC
            
           
            List<BloqueDTO> listaBloquesDocumentos= dto.getImpresoDTO().getLstBloqueDTO().stream()
                    .filter(f ->  f.getFcBajaFila() == null && f.getEsTipoDocumento() && f.getItAutorizaConsulta() 
                            ).collect(Collectors.toList());
            for (BloqueDTO bloque :listaBloquesDocumentos) {
                contador=1;
                // Rellenamos el hashmap de datos
                for (BloqueComponenteDTO bloqueComponente : bloque.getLstBloqueComponenteDTO()) {
                    String nombreCampo="";
                    String valorCampo="";
                    
                 // Buscamos el expedienteDoc
                    Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                            .getLstExpedienteDocDTO().stream()
                            .filter(x -> x.getBloqueComponenteDTO() != null && x
                                    .getBloqueComponenteDTO().getIdBloqueComponente()
                                    .equals(bloqueComponente.getIdBloqueComponente()))
                            .findFirst();
                    
                    if (Constantes.TipoSeleccionDocumento.R.equals(bloque.getItTipoSelDoc())
                            && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO()
                            .getIdValor() != null) {
                        
                       nombreCampo = "radio_" +bloqueComponente.getIdBloqueComponente() + "_"+contador+"_doc";
                       contador++;
                        if (expedienteDocDTO.isPresent()) {
                            // Si hay código de FICA, marcamos el checkbox
                            // del
                            // PDF
                            if (expedienteDocDTO.get().getCdFica() != null && expedienteDocDTO.get().getItAutoriza() != null && expedienteDocDTO.get().getItAutoriza().equalsIgnoreCase("N") ) {
                                valorCampo = "No autorizo la consulta y aporto el documento";
                            }else if(expedienteDocDTO.get().getItAutoriza() != null && expedienteDocDTO.get().getItAutoriza().equalsIgnoreCase("S")) {
                                valorCampo = "Autorizo la consulta";
                            }
                        }
                        
                        
                    }else if( !Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc()) && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO()
                            .getIdValor() != null) {
                        
                        // Es un bloque de documentación
                        nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (contador);
                        nombreCampo = "chk_" + nombreCampo + "_doc_1";
                        contador++;
                       
                        if (expedienteDocDTO.isPresent()) {
                            // Si hay código de FICA, marcamos el checkbox
                            // del
                            // PDF
                            if (expedienteDocDTO.get().getCdFica() != null) {
                                valorCampo = "true";
                            }
                        }
                        
                    }
                    hamCampos.put(nombreCampo, valorCampo);
                   
                    
                }
            }

            // Rellenamos los datos del lugar y fecha
            LocalDate localDate = LocalDate.now();
            //SimpleDateFormat sdfrmt = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);

            String lugar = "Madrid";

           // String dia = String.valueOf(localDate.getDayOfMonth());

            String fechaTexto = localDate
                    .format(DateTimeFormatter.ofPattern(Constantes.Utiles.FORMATO_FECHA, new Locale("es", "ES")));

           // String ano = String.valueOf(localDate.getYear());

            hamCampos.put("lugar", lugar);
            hamCampos.put("fechaTexto", fechaTexto);
           // hamCampos.put("mes", mes);
           // hamCampos.put("ano", ano);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return hamCampos;
    }

    /**
     * Método que rellena los campos relacionables
     *
     * @param hashCampos HashMap<String, String> con el campo a rellenar
     * @param dto Expediente
     * @return HashMap<String, String>
     * @throws ServiceException
     */
    private HashMap<String, String> rellenarCamposRelacionables(HashMap<String, String> hashCampos,
            ExpedienteDTO dto) throws ServiceException {
        HashMap<String, String> hamCampos = new HashMap<>(hashCampos);
        InteresadoDTO interesado = dto.getInteresadoDTO();

        // Recuperamos la solicitud de este expediente
        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        // Comprobamos si la solicitud es de PF o PJ
        Boolean esPJ = Constantes.Identificacion.JURIDICA.equals(solicitud.getInteresado().getTipoActor().getNmConstante());

        // Variables en caso que sea expediente de persona jurídica
        String razonSocial = StringUtils.EMPTY;
        String cargo = StringUtils.EMPTY;

        // En caso que sea persona jurídica, recuperamos primero algunos datos
        if (Boolean.TRUE.equals(esPJ)) {
            // Obtenemos el representante jurídico del expediente
            RepresentanteJurDTO representanteJurDTO = this.representanteJurService
                    .obtenerPorInteresado(dto.getInteresadoDTO().getCdNumIden(),
                            solicitud.getInteresado().getCdNumIden());

            razonSocial = representanteJurDTO.getEntidadDTO().getInteresadoDTO().getDsNombre();
            cargo = representanteJurDTO.getDsCargo();
        }

        // Recorremos los bloques del expediente
        for (BloqueDTO bloqueDTO : dto.getImpresoDTO().getLstBloqueDTO()) {

            // Si el bloque tiene campos relacionables, preparamos el HashMap
            if (Boolean.TRUE.equals(bloqueDTO.getTieneCamposRelacionables())) {

                // Obtenemos los bloqueComponentes relacionables del bloque
                List<BloqueComponenteDTO> lstBloqueComponenteRel = bloqueDTO
                        .getLstBloqueComponenteDTO().stream()
                        .filter(x -> Boolean.TRUE.equals(x.getItRelacion()))
                        .filter(x -> Boolean.TRUE.equals(x.getComponenteDTO().getPuedeRelacionar()))
                        .collect(Collectors.toList());

                for (BloqueComponenteDTO bloqueComponenteDTO : lstBloqueComponenteRel) {

                    // Variables comunes
                    Integer nmConstante = componenteService.buscarId(bloqueComponenteDTO.getComponenteDTO().getIdComponente()).getNmConstante();
                    String label = bloqueComponenteDTO.getIdBloqueComponente() + "_";

                    if (Constantes.ComponentesRelacionables.TIPO_VIA.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_TIPO_VIA,
                                interesado.getCdTipoVia());
                    } else if (Constantes.ComponentesRelacionables.NOMBRE_VIA.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_NOMBRE_VIA,
                                interesado.getDsDireccion());
                    } else if (Constantes.ComponentesRelacionables.NUMERO.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_NUMERO,
                                interesado.getDsNumero());
                    } else if (Constantes.ComponentesRelacionables.PISO.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_PISO,
                                interesado.getDsPiso());
                    } else if (Constantes.ComponentesRelacionables.PUERTA.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_PUERTA,
                                interesado.getDsPuerta());
                    } else if (Constantes.ComponentesRelacionables.CP.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_CP,
                                interesado.getCdCodigoPostal());
                    } else if (Constantes.ComponentesRelacionables.MUNICIPIO.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_MUNICIPIO,
                                this.obtenerDescripcionMunicipio(interesado.getCdProvincia(),
                                        interesado.getCdLocalidad()));
                    } else if (Constantes.ComponentesRelacionables.PROVINCIA.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_PROVINCIA,
                                this.obtenerDescripcionProvincia(interesado.getCdProvincia()));
                    } else if (Constantes.ComponentesRelacionables.NIF_NIE.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_NIF_NIE,
                                interesado.getCdNumIden());
                    } else if (Constantes.ComponentesRelacionables.RAZON_SOCIAL.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_RAZON_SOCIAL,
                                razonSocial);
                    } else if (Constantes.ComponentesRelacionables.NOMBRE.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_NOMBRE,
                                interesado.getDsNombre());
                    } else if (Constantes.ComponentesRelacionables.APELLIDO_1.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_APELLIDO_1,
                                interesado.getDsApellido1());
                    } else if (Constantes.ComponentesRelacionables.APELLIDO_2.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_APELLIDO_2,
                                interesado.getDsApellido2());
                    } else if (Constantes.ComponentesRelacionables.TELEFONO_FIJO.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_TELEFONO_FIJO,
                                interesado.getDsTelefono());
                    } else if (Constantes.ComponentesRelacionables.TELEFONO_MOVIL.equals(nmConstante)) {
                        hamCampos.put(
                                label + Constantes.ComponentesRelacionables.RELACION_TELEFONO_MOVIL,
                                interesado.getDsMovil());
                    } else if (Constantes.ComponentesRelacionables.CORREO_ELECTRONICO.equals(nmConstante)) {
                        hamCampos.put(label
                                + Constantes.ComponentesRelacionables.RELACION_CORREO_ELECTRONICO,
                                interesado.getDsEmail());
                    } else if (Constantes.ComponentesRelacionables.CARGO.equals(nmConstante)) {
                        hamCampos.put(label + Constantes.ComponentesRelacionables.RELACION_CARGO,
                                cargo);
                    }
                }
            }
        }

        return hamCampos;
    }

    /**
     * Método que devuelve el nombre del municipio o vacío
     *
     * @param cdProvincia provincia
     * @param cdLocalidad municipio
     * @return nombre de la localidad o empty
     * @throws ServiceException
     */
    private String obtenerDescripcionMunicipio(String cdProvincia, String cdLocalidad)
            throws ServiceException {
        String dsDescripcion = StringUtils.EMPTY;

        if (StringUtils.isNotBlank(cdProvincia)) {
            SucaMunicipioDTO sucaMunicipioDTO = this.sucaMunicipioService
                    .obtenerMunicipio(cdProvincia, cdLocalidad);
            dsDescripcion = sucaMunicipioDTO.getDsMunicipio();
        }

        return dsDescripcion;
    }

    /**
     * Método que devuelve el nombre de la provincia o vacío
     *
     * @param cdProvincia a obtener
     * @return nombre de la provincia o empty
     * @throws ServiceException excepción
     */
    private String obtenerDescripcionProvincia(String cdProvincia) throws ServiceException {
        String dsDescripcion = StringUtils.EMPTY;

        if (StringUtils.isNotBlank(cdProvincia)) {
            CatalogoDTO provinciaDTO = this.sucaProvinciaService.obtenerPorProvincia(cdProvincia);
            dsDescripcion = provinciaDTO.getDescripcion();
        }

        return dsDescripcion;
    }
    
    @Override
    public HashMap<String, String> rellenarCamposJustec(ExpedienteDTO dto,ImpresoDTO impresoFormulario,List<ExpedienteDocDTO> listaExpedienteDocDTO) throws ServiceException {

        HashMap<String, String> hamCampos = new HashMap<>();
        try {

            HashMap<Integer, Integer> hamFilaBloque = new HashMap<>();

            // Iteramos los bloques del formulario
            for (BloqueDTO bloque : impresoFormulario.getLstBloqueDTO()) {
                Integer nmFilas = 1;

                if (Constantes.Formatos.ID_LISTADO.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = bloque.getNmFilas();
                } else if (Constantes.Formatos.ID_TABLA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = 0;

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloque
                            .getLstBloqueComponenteDTO()) {
                        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())) {
                            nmFilas++;
                        }
                    }
                }

                hamFilaBloque.put(bloque.getIdBloque(), nmFilas);
            }

            // Extraemos la lista de bloques componentes del expediente que no
            // estén
            // dados de baja
            List<BloqueComponenteDTO> listaBloqueComponente = impresoFormulario.getLstBloqueDTO()
                    .stream().flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                    .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

            // Rellenamos el hashmap de datos
            for (BloqueComponenteDTO bloqueComponente : listaBloqueComponente) {

                Integer nmFila = hamFilaBloque.get(bloqueComponente.getIdBloque());
                SimpleDateFormat formatter = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);

                if (Objects.isNull(bloqueComponente.getTipoFinanciacionDTO())
                        || Objects.isNull(bloqueComponente.getTipoFinanciacionDTO().getIdValor())) {

                    for (Integer i = 0; i < nmFila; i++) {
                        String nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (i + 1);
                        String valorCampo = StringUtils.EMPTY;

                        if (bloqueComponente.getDatos() != null
                             && !bloqueComponente.getDatos().isEmpty() && 
                             Objects.nonNull(bloqueComponente.getDatos().get(i))) {
                                valorCampo = bloqueComponente.getDatos().get(i).getDsValor();
                        }

                        if (bloqueComponente.getComponenteDTO().getTipoDTO().getIdValor() != null) {
                            if (Constantes.TipoComponente.ID_CHECK.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {

                                if (bloqueComponente.getDatos() != null && StringUtils.isNotBlank(
                                        bloqueComponente.getDatos().get(i).getDsValor())) {
                                    String[] valoresCheck = bloqueComponente.getDatos().get(i)
                                            .getDsValor().split(";");
                                    for (Integer j = 0; j < valoresCheck.length; j++) {
                                        hamCampos.put("chk_" + nombreCampo + "_" + (j + 1),
                                                valoresCheck[j]);
                                    }
                                }

                                // Saltamos a otro componente
                                continue;
                            } else if (bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante().equals(Constantes.TipoComponente.ID_COMBO)) {
                                // El tipo combo necesita el formato:
                                // idBloque_nmfila.idBloque_nmFila
                                nombreCampo = nombreCampo + "." + nombreCampo;
                            }
                        } else if (bloqueComponente.getComponenteDTO().getTipoDocumentoDTO() != null
                                && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
                            // Es un bloque de documentación
                            nombreCampo = "chk_" + nombreCampo + "_doc_1";

                            // Buscamos el expedienteDoc
                            Optional<ExpedienteDocDTO> expedienteDocDTO = listaExpedienteDocDTO.stream()
                                    .filter(x -> x.getBloqueComponenteDTO() != null && x
                                            .getBloqueComponenteDTO().getIdBloqueComponente()
                                            .equals(bloqueComponente.getIdBloqueComponente()))
                                    .findFirst();

                            if (expedienteDocDTO.isPresent() && expedienteDocDTO.get().getCdFica() != null) {
                                // Si hay código de FICA, marcamos el checkbox
                                // del
                                // PDF
                                valorCampo = "true";
                            }

                        } else if (bloqueComponente.getComponenteDTO().getSubTipoDTO() != null
                                && Constantes.SubTipoComponente.ID_FECHA.equals(bloqueComponente.getComponenteDTO().getSubTipoDTO().getNmConstante())) {
                            // Es un campo de fecha, lo formateamos
                            formatter.format(valorCampo);
                        }

                        hamCampos.put(nombreCampo, valorCampo);

                    }

                }
            }
            
            
            
 // docuemntos tipo C
           
            List<BloqueDTO> listaBloquesDocumento=impresoFormulario.getLstBloqueDTO().stream()
                    .filter(f ->  f.getFcBajaFila() == null && f.getEsTipoDocumento() && !f.getItAutorizaConsulta() 
                            ).collect(Collectors.toList());
            for (BloqueDTO bloque :listaBloquesDocumento) {
                Integer contador=1;
                List<BloqueComponenteDTO> componentes= bloque.getLstBloqueComponenteDTO();
                Comparator<BloqueComponenteDTO> compareByNumOrdens = (BloqueComponenteDTO c1,
                        BloqueComponenteDTO c2) -> c1.getNmOrden().compareTo(c2.getNmOrden());

                if (!componentes.isEmpty()) {
                    Collections.sort(componentes, compareByNumOrdens);
                }
                // Rellenamos el hashmap de datos
                for (BloqueComponenteDTO bloqueComponente :componentes ) {
                    String nombreCampo="";
                    String valorCampo="";
                    
                 // Buscamos el expedienteDoc
                    Optional<ExpedienteDocDTO> expedienteDocDTO = listaExpedienteDocDTO.stream()
                            .filter(x -> x.getBloqueComponenteDTO() != null && x
                                    .getBloqueComponenteDTO().getIdBloqueComponente()
                                    .equals(bloqueComponente.getIdBloqueComponente()))
                            .findFirst();
                    
                   if( !Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc()) && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO()
                            .getIdValor() != null) {
                        
                        // Es un bloque de documentación
                        nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (contador);
                        nombreCampo = "chk_" + nombreCampo + "_doc_1";
                        contador++;
                       
                        if (expedienteDocDTO.isPresent()) {
                            // Si hay código de FICA, marcamos el checkbox
                            // del
                            // PDF
                            if (expedienteDocDTO.get().getCdFica() != null) {
                                valorCampo = "true";
                            }
                        }
                        
                    }
                    hamCampos.put(nombreCampo, valorCampo);
                   
                    
                }
            }
            
            //FIN DOC
            
           
            List<BloqueDTO> listaBloquesDocumentos= impresoFormulario.getLstBloqueDTO().stream()
                    .filter(f ->  f.getFcBajaFila() == null && f.getEsTipoDocumento() && f.getItAutorizaConsulta() 
                            ).collect(Collectors.toList());
            for (BloqueDTO bloque :listaBloquesDocumentos) {
               Integer contador=1;
                // Rellenamos el hashmap de datos
                for (BloqueComponenteDTO bloqueComponente : bloque.getLstBloqueComponenteDTO()) {
                    String nombreCampo="";
                    String valorCampo="";
                    
                 // Buscamos el expedienteDoc
                    Optional<ExpedienteDocDTO> expedienteDocDTO = listaExpedienteDocDTO.stream()
                            .filter(x -> x.getBloqueComponenteDTO() != null && x
                                    .getBloqueComponenteDTO().getIdBloqueComponente()
                                    .equals(bloqueComponente.getIdBloqueComponente()))
                            .findFirst();
                    
                    if (Constantes.TipoSeleccionDocumento.R.equals(bloque.getItTipoSelDoc())
                            && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO()
                            .getIdValor() != null) {
                        
                       nombreCampo = "radio_" +bloqueComponente.getIdBloqueComponente() + "_"+contador+"_doc";
                       contador++;
                        if (expedienteDocDTO.isPresent()) {
                            // Si hay código de FICA, marcamos el checkbox
                            // del
                            // PDF
                            if (expedienteDocDTO.get().getCdFica() != null && expedienteDocDTO.get().getItAutoriza() != null && expedienteDocDTO.get().getItAutoriza().equalsIgnoreCase("N") ) {
                                valorCampo = "No autorizo la consulta y aporto el documento";
                            }else if(expedienteDocDTO.get().getItAutoriza() != null && expedienteDocDTO.get().getItAutoriza().equalsIgnoreCase("S")) {
                                valorCampo = "Autorizo la consulta";
                            }
                        }
                        
                        
                    }else if( !Constantes.TipoSeleccionDocumento.N.equals(bloque.getItTipoSelDoc()) && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO()
                            .getIdValor() != null) {
                        
                        // Es un bloque de documentación
                        nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (contador);
                        nombreCampo = "chk_" + nombreCampo + "_doc_1";
                        contador++;
                       
                        if (expedienteDocDTO.isPresent()) {
                            // Si hay código de FICA, marcamos el checkbox
                            // del
                            // PDF
                            if (expedienteDocDTO.get().getCdFica() != null) {
                                valorCampo = "true";
                            }
                        }
                        
                    }
                    hamCampos.put(nombreCampo, valorCampo);
                   
                    
                }
            }


            // Rellenamos los datos del lugar y fecha
            LocalDate localDate = LocalDate.now();
            String lugar = "Madrid";
            String dia = String.valueOf(localDate.getDayOfMonth());
            String mes = localDate
                    .format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")));
            String ano = String.valueOf(localDate.getYear());

            hamCampos.put("lugar", lugar);
            hamCampos.put("dia", dia);
            hamCampos.put("mes", mes);
            hamCampos.put("ano", ano);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return hamCampos;
    }
    
    @Override
    public HashMap<String, String> rellenarCamposContrato(ExpedienteDTO dto,ImpresoDTO impresoFormulario, 
            List<ExpedienteDTO> listExpedientesParticipantes) throws ServiceException {
        
   
        HashMap<String, String> hamCampos = new HashMap<>();
        try {

            HashMap<Integer, Integer> hamFilaBloque = new HashMap<>();

            // Iteramos los bloques del formulario
            for (BloqueDTO bloque : impresoFormulario.getLstBloqueDTO()) {
                Integer nmFilas = 1;

                if (Constantes.Formatos.ID_LISTADO.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = bloque.getNmFilas();
                } else if (Constantes.Formatos.ID_TABLA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = 0;

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloque
                            .getLstBloqueComponenteDTO()) {
                        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())) {
                            nmFilas++;
                        }
                    }

                }

                hamFilaBloque.put(bloque.getIdBloque(), nmFilas);
            }
            
            // Rellenamos los datos de los participantes
           
            SimpleDateFormat formatter = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                
            if(listExpedientesParticipantes != null && !listExpedientesParticipantes.isEmpty()) {
                for (ExpedienteDTO expedienteDTO :listExpedientesParticipantes) {
                    ExpedienteContratoDTO contrato = expedienteDTO.getExpedienteContrato();
                    String nombreCampo = "_" +expedienteDTO.getIdExpediente();
                    String nombreInteresado="";
                    hamCampos.put("expediente" + nombreCampo,
                            expedienteDTO.getCdExpediente());
                    
                    hamCampos.put("tipoContrato" + nombreCampo,
                             contrato.getIdTipoContratoDTO().getDsValor());
                   
                    hamCampos.put("dni" + nombreCampo,
                            expedienteDTO.getInteresadoDTO().getCdNumIden());
                    
                    nombreInteresado =expedienteDTO.getInteresadoDTO().getDsNombre()+ " "+expedienteDTO.getInteresadoDTO().getDsApellido1();
                    if(expedienteDTO.getInteresadoDTO().getDsApellido2() != null) {
                        nombreInteresado +=" "+ expedienteDTO.getInteresadoDTO().getDsApellido2();
                    }
                    hamCampos.put("nombreParticipante" + nombreCampo,
                            nombreInteresado   );
                    
                    hamCampos.put("EstadoContrato" + nombreCampo,
                             contrato.getIdEstadoDTO().getDsValor());
                    
                   
                }
            }
               

            // Extraemos la lista de bloques componentes del expediente que no
            // estén
            // dados de baja
            List<BloqueComponenteDTO> listaBloqueComponente = impresoFormulario.getLstBloqueDTO()
                    .stream().flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                    .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

            // Rellenamos el hashmap de datos
            for (BloqueComponenteDTO bloqueComponente : listaBloqueComponente) {

                Integer nmFila = hamFilaBloque.get(bloqueComponente.getIdBloque());
               
                if (Objects.isNull(bloqueComponente.getTipoFinanciacionDTO())
                        || Objects.isNull(bloqueComponente.getTipoFinanciacionDTO().getIdValor())) {

                    for (Integer i = 0; i < nmFila; i++) {
                        String nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (i + 1);
                        String valorCampo = StringUtils.EMPTY;

                        if (bloqueComponente.getDatos() != null
                                && !bloqueComponente.getDatos().isEmpty()
                                && Objects.nonNull(bloqueComponente.getDatos().get(i))) {
                            
                            valorCampo = bloqueComponente.getDatos().get(i).getDsValor();
                        }

                        if (bloqueComponente.getComponenteDTO().getTipoDTO().getIdValor() != null) {
                            if (Constantes.TipoComponente.ID_CHECK.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {

                                if (bloqueComponente.getDatos() != null && StringUtils.isNotBlank(
                                        bloqueComponente.getDatos().get(i).getDsValor())) {
                                    String[] valoresCheck = bloqueComponente.getDatos().get(i)
                                            .getDsValor().split(";");
                                    for (Integer j = 0; j < valoresCheck.length; j++) {
                                        hamCampos.put("chk_" + nombreCampo + "_" + (j + 1),
                                                valoresCheck[j]);
                                    }
                                }

                                // Saltamos a otro componente
                                continue;
                            } else if (bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante().equals(Constantes.TipoComponente.ID_COMBO)) {
                                // El tipo combo necesita el formato:
                                // idBloque_nmfila.idBloque_nmFila
                                nombreCampo = nombreCampo + "." + nombreCampo;
                            }
                        } else if (bloqueComponente.getComponenteDTO().getTipoDocumentoDTO() != null
                                && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
                            // Es un bloque de documentación
                            nombreCampo = "chk_" + nombreCampo + "_doc_1";

                            // Buscamos el expedienteDoc
                            Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                                    .getLstExpedienteDocDTO().stream()
                                    .filter(x -> x.getBloqueComponenteDTO() != null && x
                                            .getBloqueComponenteDTO().getIdBloqueComponente()
                                            .equals(bloqueComponente.getIdBloqueComponente()))
                                    .findFirst();

                            if (expedienteDocDTO.isPresent()) {
                                // Si hay código de FICA, marcamos el checkbox
                                // del
                                // PDF
                                if (expedienteDocDTO.get().getCdFica() != null) {
                                    valorCampo = "true";
                                }
                            }

                        } else if (bloqueComponente.getComponenteDTO().getSubTipoDTO() != null
                                && Constantes.SubTipoComponente.ID_FECHA.equals(bloqueComponente.getComponenteDTO().getSubTipoDTO().getNmConstante())) {
                            // Es un campo de fecha, lo formateamos
                            formatter.format(valorCampo);
                        }

                        hamCampos.put(nombreCampo, valorCampo);

                    }

                }
            }

            // Rellenamos los datos del lugar y fecha
            LocalDate localDate = LocalDate.now();

            String lugar = "Madrid";

            String dia = String.valueOf(localDate.getDayOfMonth());

            String mes = localDate
                    .format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")));

            String ano = String.valueOf(localDate.getYear());

            hamCampos.put("lugar", lugar);
            hamCampos.put("dia", dia);
            hamCampos.put("mes", mes);
            hamCampos.put("ano", ano);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return hamCampos;
    }
    
    
    private HashMap<String, String> crearTablaPresupesto(HashMap<String, String> hamCampos,BloqueDTO bloque, ExpedienteDTO dto) throws ServiceException {
        // Preparamos los datos del bloque presupuesto
        if (Boolean.TRUE.equals(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE.equals(bloque.getIdFormato().getNmConstante()))) {
            
            ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                    .buscarId(dto.getConvocatoriaActDTO().getIdConvocatoria());
            
            if(convocatoriaDTO.getItConceptoPresup() != null && convocatoriaDTO.getItConceptoPresup().equalsIgnoreCase("S")) {
                
                List<BloqueComponenteDTO> listaBloqueComponente = bloque
                        .getLstBloqueComponenteDTO().stream()
                        .filter(x -> x.getFcBajaFila() == null)
                        .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                        .collect(Collectors.toList());

               

                List<BloqueComponenteDTO> lstFilasAux = listaBloqueComponente.stream()
                        .filter(f -> f.getItFila()).collect(Collectors.toList());
                
                for (BloqueComponenteDTO bloqCom : lstFilasAux) {
                    bloqCom.setDatos(bloqCom.getDatos().stream().filter(p -> p.getExpedienteDTO() != null 
                            && p.getExpedienteDTO().getIdExpediente() != null
                            && p.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente()))
                            .collect(Collectors.toList()));
                }
                
                List<BloqueComponenteDTO> lstFilas = new ArrayList<>();
                
                for(BloqueComponenteDTO bloqueComp:lstFilasAux) {
                    if(bloqueComp.getDatos().size()== convocatoriaDTO.getNmDurMaxAyuda()+1) {
                        bloqueComp.setDatos(bloqueComp.getDatos().stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList()));
                        lstFilas.add(bloqueComp);
                    }else if(bloqueComp.getDatos().size()> convocatoriaDTO.getNmDurMaxAyuda()+2) {
                        int posCelda= 0;
                        bloqueComp.setDatos(bloqueComp.getDatos().stream().sorted(Comparator.comparing(DatoDTO::getNmFila)).collect(Collectors.toList()));
                        for (int j=posCelda; j< bloqueComp.getDatos().size();j++){
                            List<DatoDTO> datosFilaConcepto= new ArrayList<>();
                            BloqueComponenteDTO bloqueComConcepto= new BloqueComponenteDTO();
                            for (int index = 0; index < convocatoriaDTO.getNmDurMaxAyuda()+1; index++) {
                                datosFilaConcepto.add(bloqueComp.getDatos().get(posCelda));
                                posCelda++;
                                j=posCelda;
                            }
                            
                            datosFilaConcepto= datosFilaConcepto.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                            bloqueComConcepto.setIdBloque(bloqueComp.getIdBloque());
                            bloqueComConcepto.setDatos(datosFilaConcepto);
                            bloqueComConcepto.setIdBloqueComponente(bloqueComp.getIdBloqueComponente());
                            bloqueComConcepto.setCdUsuAlta(bloqueComp.getCdUsuAlta());
                            bloqueComConcepto.setComponenteDTO(bloqueComp.getComponenteDTO());
                            bloqueComConcepto.setNmOrden(bloqueComp.getNmOrden());
                            bloqueComConcepto.setItFila(bloqueComp.getItFila());
                            bloqueComConcepto.setItColumna(bloqueComp.getItColumna());
                            bloqueComConcepto.setItObligatorio(bloqueComp.getItObligatorio());
                            bloqueComConcepto.setItRelacion(bloqueComp.getItRelacion());
                            bloqueComConcepto.setTipoFinanciacionDTO(bloqueComp.getTipoFinanciacionDTO());
                            bloqueComConcepto.setItSeleccionado(bloqueComp.getItSeleccionado());
                            lstFilas.add(bloqueComConcepto);
                            
                        }
                           
                            
                    }
                }
                
                lstFilas = lstFilas.stream()
                        .filter(f -> f.getItFila()).collect(Collectors.toList());
                
                for (int i = 0; i < lstFilas.size(); i++) {
                    
                    List<DatoDTO> listaDatos= lstFilas.get(i).getDatos();
                    BigDecimal montoTotalFila =BigDecimal.ZERO;
                   // listaDatos= listaDatos.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                    for (DatoDTO datoDTO : listaDatos) {

                        if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                            String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_"
                                    + (datoDTO.getNmFila() + 1) + "_"
                                    + (datoDTO.getNmColumna() + 1);

                            String valorCampo ="";
                            if(datoDTO.getDsValor() != null) {
                                valorCampo =datoDTO.getDsValor().trim();
                            }
                            
                            
                            if(datoDTO.getNmColumna().intValue() != 0) {
                                if(valorCampo.equals("")) {
                                    valorCampo="0.00";
                                }
                                BigDecimal decimalValorCampo = BigDecimal.valueOf(Double.valueOf(valorCampo));
                                
                                String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                                hamCampos.put(nombreCampo, decimalValorString);

                                montoTotalFila = montoTotalFila.add(decimalValorCampo);
                            }else {
                                hamCampos.put(nombreCampo,valorCampo);
                            }

                            

                        }

                    }

                    BigDecimal decimalMontoTotalFila = montoTotalFila;
                    String nombreCampo = "TF_" + bloque.getIdBloque() + "_"
                            + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);

                    String decimalValorStringMontoTotalFila = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                    hamCampos.put(nombreCampo, decimalValorStringMontoTotalFila);
                    
                   

                }
               
              
                BigDecimal montoTotalTotales = BigDecimal.ZERO;

                for (int i = 1; i < convocatoriaDTO.getNmDurMaxAyuda()+1; i++) {

                    BigDecimal montoTotalColumna = BigDecimal.ZERO;

                    for (BloqueComponenteDTO lstFila : lstFilas) {
                        if (Objects.nonNull(lstFila) && !lstFila.getDatos().isEmpty()
                                && Objects.nonNull(lstFila.getDatos().get(i) )
                                && lstFila.getDatos().get(i).getNmColumna().intValue() != 0) {  
                            String valorTcolum=lstFila.getDatos().get(i).getDsValor();
                            BigDecimal valor= BigDecimal.ZERO;
                            if(valorTcolum != null && !valorTcolum.trim().equals("") ) {
                                valor=BigDecimal.valueOf(Double.valueOf(valorTcolum));
                            }
                            
                            montoTotalColumna = montoTotalColumna.add(valor);
                            
                                   
                        }
                    }
                    
                    BigDecimal decimalMontoTotalColumna = montoTotalColumna;
                    String nombreCampo = "TC_" + bloque.getIdBloque() + "_"
                            + (lstFilas.size() + 1) + "_" + (i + 1);
                    
                    String decimalValorStringMontoTotal = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                    hamCampos.put(nombreCampo, decimalValorStringMontoTotal);

                    montoTotalTotales =montoTotalTotales.add(montoTotalColumna);

                }

                BigDecimal decimalMontoTotalTotales = montoTotalTotales;
                String nombreCampo = "TT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                        + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                String decimalValorStringMontoTotalTotales = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);
                hamCampos.put(nombreCampo, decimalValorStringMontoTotalTotales);
                
                // parametros de ayuda solicitada calculada en funcion de intensidad de ayuda
                BigDecimal intensidadAyuda = BigDecimal.valueOf(convocatoriaDTO.getPorcentajeIntensidadAyuda());
                
                for (int i = 0; i < lstFilas.size(); i++) {

                    BigDecimal montoTotalFila = BigDecimal.ZERO;

                    for (DatoDTO datoDTO : lstFilas.get(i).getDatos()) {

                        if (StringUtils.isNotBlank(datoDTO.getDsValor())) {
                         
                             nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_"
                                    + (datoDTO.getNmFila() + 1) + "_"
                                    + (datoDTO.getNmColumna() + 1);

                            String valorCampo ="";
                            if(datoDTO.getDsValor() != null) {
                                valorCampo =datoDTO.getDsValor().trim();
                            }
                            
                            if(datoDTO.getNmColumna().intValue() != 0) {
                                if(valorCampo.equals("")) {
                                    valorCampo="0.00";
                                }
                                BigDecimal decimalValorCampo = (BigDecimal.valueOf(Double.valueOf(valorCampo)).multiply(intensidadAyuda))
                                        .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                                
                                String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                                hamCampos.put(nombreCampo, decimalValorString);

                                montoTotalFila = montoTotalFila.add(BigDecimal.valueOf(Double.valueOf(valorCampo)));
                            }else {
                                hamCampos.put(nombreCampo,valorCampo);
                            }
                            

                        }

                    }

                    BigDecimal decimalMontoTotalFila = (montoTotalFila.multiply(intensidadAyuda))
                            .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                    
                    nombreCampo = "ATF_" + bloque.getIdBloque() + "_"
                            + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);
                    
                    String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                    hamCampos.put(nombreCampo, decimalValorString);

                }

                 montoTotalTotales = BigDecimal.ZERO;

                for (int i = 1; i < convocatoriaDTO.getNmDurMaxAyuda()+1; i++) {

                    BigDecimal montoTotalColumna = BigDecimal.ZERO;

                    for (BloqueComponenteDTO lstFila : lstFilas) {
                        if (Objects.nonNull(lstFila) && !lstFila.getDatos().isEmpty()
                                && Objects.nonNull(lstFila.getDatos().get(i))) {
                            String valorTotalColumn=lstFila.getDatos().get(i).getDsValor();
                            if(valorTotalColumn== null) {
                                valorTotalColumn="0.00";
                            }
                            montoTotalColumna = montoTotalColumna.add(BigDecimal.valueOf(Double.valueOf(valorTotalColumn)));
                        }
                    }
                    BigDecimal decimalMontoTotalColumna = (montoTotalColumna.multiply(intensidadAyuda))
                    .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                    nombreCampo = "ATC_" + bloque.getIdBloque() + "_"
                            + (lstFilas.size() + 1) + "_" + (i + 1);
                    String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                    hamCampos.put(nombreCampo, decimalValorString);

                    montoTotalTotales = montoTotalColumna;

                }

                 decimalMontoTotalTotales = (montoTotalTotales.multiply(intensidadAyuda))
                         .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                 nombreCampo = "ATT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                        + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                 String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);
                 hamCampos.put(nombreCampo, decimalValorString);
                
            }else {
                
                List<BloqueComponenteDTO> listaBloqueComponente = bloque
                        .getLstBloqueComponenteDTO().stream()
                        .filter(x -> x.getFcBajaFila() == null)
                        .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                        .collect(Collectors.toList());

               

                List<BloqueComponenteDTO> lstFilas = listaBloqueComponente.stream()
                        .filter(f -> f.getItFila()).collect(Collectors.toList());
                
                for (BloqueComponenteDTO bloqCom : lstFilas) {
                    bloqCom.setDatos(bloqCom.getDatos().stream().filter(p -> p.getExpedienteDTO() != null 
                            && p.getExpedienteDTO().getIdExpediente() != null
                            && p.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente()))
                            .collect(Collectors.toList()));
                }

                for (int i = 0; i < lstFilas.size(); i++) {

                    BigDecimal montoTotalFila = BigDecimal.ZERO;

                    for (DatoDTO datoDTO : lstFilas.get(i).getDatos()) {

                        if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                            String nombreCampo = "IC_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_"
                                    + (datoDTO.getNmFila() + 1) + "_"
                                    + (datoDTO.getNmColumna() + 1);

                            String valorCampo = datoDTO.getDsValor().trim();

                            BigDecimal decimalValorCampo = BigDecimal.valueOf(Double.valueOf(valorCampo));
                            String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                            hamCampos.put(nombreCampo, decimalValorString);
                            
                            montoTotalFila = montoTotalFila.add(decimalValorCampo);

                        }

                    }

                    BigDecimal decimalMontoTotalFila = montoTotalFila;
                    String nombreCampo = "TF_" + bloque.getIdBloque() + "_"
                            + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);

                    String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);
                    hamCampos.put(nombreCampo, decimalValorString);

                }

                BigDecimal montoTotalTotales = BigDecimal.ZERO;

                for (int i = 0; i < convocatoriaDTO.getNmDurMaxAyuda(); i++) {

                    BigDecimal montoTotalColumna = BigDecimal.ZERO;

                    for (BloqueComponenteDTO lstFila : lstFilas) {
                        if (Objects.nonNull(lstFila) && !lstFila.getDatos().isEmpty()
                                && Objects.nonNull(lstFila.getDatos().get(i))) {
                            montoTotalColumna = montoTotalColumna.add(BigDecimal.valueOf(Double.valueOf(lstFila.getDatos().get(i).getDsValor())));
                        }
                    }
                    BigDecimal decimalMontoTotalColumna = montoTotalColumna;
                    String nombreCampo = "TC_" + bloque.getIdBloque() + "_"
                            + (lstFilas.size() + 1) + "_" + (i + 1);
                    String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                    hamCampos.put(nombreCampo, decimalValorString);
                    
                    montoTotalTotales =montoTotalTotales.add(montoTotalColumna);

                }

                BigDecimal decimalMontoTotalTotales = montoTotalTotales;
                String nombreCampo = "TT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                        + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                String decimalValorString = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);
                hamCampos.put(nombreCampo, decimalValorString);
                // parametros de ayuda solicitada calculada en funcion de intensidad de ayuda
                BigDecimal intensidadAyuda = BigDecimal.valueOf(convocatoriaDTO.getPorcentajeIntensidadAyuda());
                
                for (int i = 0; i < lstFilas.size(); i++) {

                    BigDecimal montoTotalFila = BigDecimal.ZERO;

                    for (DatoDTO datoDTO : lstFilas.get(i).getDatos()) {

                        if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                             nombreCampo = "AIC_" + bloque.getIdBloque() + "_"
                                    + lstFilas.get(i).getIdBloqueComponente() + "_"
                                    + (datoDTO.getNmFila() + 1) + "_"
                                    + (datoDTO.getNmColumna() + 1);

                            String valorCampo = datoDTO.getDsValor().trim();

                            BigDecimal decimalValorCampo = (BigDecimal.valueOf(Double.valueOf(valorCampo)).multiply(intensidadAyuda))
                                    .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);

                            String decimalValorStringCampo = Utils.formatearBigDecimalEuropeo(decimalValorCampo);
                            hamCampos.put(nombreCampo, decimalValorStringCampo);

                            montoTotalFila =montoTotalFila.add(BigDecimal.valueOf(Double.valueOf(valorCampo)));

                        }

                    }

                    BigDecimal decimalMontoTotalFila = (montoTotalFila.multiply(intensidadAyuda))
                            .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                    
                     nombreCampo = "ATF_" + bloque.getIdBloque() + "_"
                            + lstFilas.get(i).getIdBloqueComponente() + "_" + (i + 1);
                    String decimalValorStringTotal = Utils.formatearBigDecimalEuropeo(decimalMontoTotalFila);

                    hamCampos.put(nombreCampo, decimalValorStringTotal);

                }

                 montoTotalTotales = BigDecimal.ZERO;

                for (int i = 0; i < convocatoriaDTO.getNmDurMaxAyuda(); i++) {

                    BigDecimal montoTotalColumna = BigDecimal.ZERO;

                    for (BloqueComponenteDTO lstFila : lstFilas) {
                        if (Objects.nonNull(lstFila) && !lstFila.getDatos().isEmpty()
                                && Objects.nonNull(lstFila.getDatos().get(i))) {
                            montoTotalColumna = montoTotalColumna.add(BigDecimal.valueOf(Double.valueOf(lstFila.getDatos().get(i).getDsValor())));
                        }
                    }
                    BigDecimal decimalMontoTotalColumna = (montoTotalColumna.multiply(intensidadAyuda))
                    .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                     nombreCampo = "ATC_" + bloque.getIdBloque() + "_"
                            + (lstFilas.size() + 1) + "_" + (i + 1);
                     String decimalValorStringMontoTotal = Utils.formatearBigDecimalEuropeo(decimalMontoTotalColumna);
                     hamCampos.put(nombreCampo, decimalValorStringMontoTotal);

                    montoTotalTotales =montoTotalTotales.add(montoTotalColumna);

                }

                 decimalMontoTotalTotales = (montoTotalTotales.multiply(intensidadAyuda))
                         .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_HALF_UP);
                 nombreCampo = "ATT_" + bloque.getIdBloque() + "_" + (lstFilas.size() + 1)
                        + "_" + (convocatoriaDTO.getNmDurMaxAyuda() + 1);

                 String decimalValorStringMontoTotalTotales = Utils.formatearBigDecimalEuropeo(decimalMontoTotalTotales);

                 hamCampos.put(nombreCampo, decimalValorStringMontoTotalTotales);
            }

           

        }
        
        return hamCampos;
    }
    
    @Override
    public HashMap<String, String> rellenarCamposModPresupPlazosOtra(ExpedienteDTO dto) throws ServiceException {

        HashMap<String, String> hamCampos = new HashMap<>();
        Integer idModificacion=dto.getExpedienteModifOtraDTO()
                .getExpedienteModifPresPlazoOtraDTO()
                .getIdExpedienteModifPresPlazoOtra();
        try {

            HashMap<Integer, Integer> hamFilaBloque = new HashMap<>();

            // Iteramos los bloques del formulario
            for (BloqueDTO bloque : dto.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()) {
                Integer nmFilas = 1;

                if (Constantes.Formatos.ID_LISTADO.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = bloque.getNmFilas();
                } else if (Constantes.Formatos.ID_TABLA.equals(bloque.getIdFormato().getNmConstante())
                        ) {
                    nmFilas = 0;

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloque
                            .getLstBloqueComponenteDTO()) {
                        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())) {
                            nmFilas++;
                        }
                    }

                }

                hamFilaBloque.put(bloque.getIdBloque(), nmFilas);
             // Preparamos los datos del bloque presupuesto
                this.crearTablaPresupesto( hamCampos,bloque, dto);
                
                if (Boolean.TRUE.equals(Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL.equals(bloque.getIdFormato().getNmConstante()))) {

                    SimpleDateFormat formatter = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA); 
                    
                    List<BloqueComponenteDTO> listaBloqueComponente = bloque
                            .getLstBloqueComponenteDTO().stream()
                            .filter(x -> x.getFcBajaFila() == null)
                            .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                            .collect(Collectors.toList());

                
                    List<BloqueComponenteDTO> lstFilas = listaBloqueComponente.stream()
                            .filter(f -> f.getItFila()).collect(Collectors.toList());
                    
                    
                    
                            for(BloqueComponenteDTO bloqCom:lstFilas) {
                                List<DatoDTO> datosBloq= bloqCom.getDatos().stream().filter(x -> 
                                x.getExpedienteModifPresPlazoOtraDTO() != null 
                                && dto.getExpedienteModifOtraDTO()!= null
                                && dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO() != null
                                
                                && x.getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra()
                                .equals(idModificacion)).collect(Collectors.toList()); 
                                bloqCom.setDatos(datosBloq); 
                            }
                    
                    for (int i = 0; i < lstFilas.size(); i++) {

                        String nombreCampo = 
                                 "_TAN"+ bloque.getIdBloque() + "_"
                                        + lstFilas.get(i).getIdBloqueComponente() + "_"+i;
                        String valorCampo = "Anualidad " +i+1;
                        hamCampos.put(nombreCampo,
                                valorCampo );
                        if (lstFilas.get(i).getDatos() != null && lstFilas.get(i).getDatos().size() >0) {
                           /* DatoDTO datoDTO =lstFilas.get(i).getDatos().get(0);
                            if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                                 nombreCampo = "fecha_ini_ejecucion_anualidad_" + bloque.getIdBloque() + "_"
                                        + lstFilas.get(i).getIdBloqueComponente() + "_"
                                        + (datoDTO.getNmFila() + 1) + "_"
                                        + (datoDTO.getNmColumna() + 1);

                                valorCampo =datoDTO.getDsValor();
                                 
                                 Instant instant = Instant.parse(valorCampo);
                                 Date fecha = Date.from(instant);
                                 
                                 // Formatear a dd/MM/yyyy
                                 SimpleDateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                                 String fechaFormateada = sdf.format(fecha);

                                hamCampos.put(nombreCampo,fechaFormateada);

                             }*/
                            
                            DatoDTO datoDTO =lstFilas.get(i).getDatos().get(0);
                            if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                                 nombreCampo = "fecha_fin_ejecucion_anualidad_" + bloque.getIdBloque() + "_"
                                        + lstFilas.get(i).getIdBloqueComponente() + "_"
                                        + (datoDTO.getNmFila() + 1) + "_"
                                        + (datoDTO.getNmColumna() + 1);

                                 valorCampo =datoDTO.getDsValor();

                                 Instant instant = Instant.parse(valorCampo);
                                 Date fecha = Date.from(instant);

                                 // Formatear a dd/MM/yyyy en la zona horaria de Madrid,
                                 // para no restar un día si la zona por defecto de la JVM no es Europe/Madrid
                                 SimpleDateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                                 sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
                                 String fechaFormateada = sdf.format(fecha);

                                hamCampos.put(nombreCampo,fechaFormateada);

                             }

                        }


                    }
                  
                }
                
                /*if (Boolean.TRUE.equals(Constantes.Formatos.
                        .equals(bloque.getIdFormato().getIdValor()))) {
                    
                    //otras modificaciones presupuesto
                    List<BloqueComponenteDTO> listaBloqueComponente = dto.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()
                            .stream()
                            .filter(f ->  f.getEsTipoOtrasModificaciones())
                            .flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                            .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
                    
                    if(listaBloqueComponente != null && !listaBloqueComponente.isEmpty() 
                            && listaBloqueComponente.get(0).getDatos()!=null && !listaBloqueComponente.get(0).getDatos().isEmpty()
                            && listaBloqueComponente.get(0).getDatos().get(0).getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra()
                            .equals(idModificacion)){
                        String nombreCampo = listaBloqueComponente.get(0).getIdBloqueComponente() + "_1";
                        String valorCampo = listaBloqueComponente.get(0).getDatos().get(0).getDsValor();
                        hamCampos.put(nombreCampo,valorCampo );
                    }
                    
                }*/
                
              
              
            }

            // Extraemos la lista de bloques componentes del expediente que no
            // estén
            // dados de baja
            List<BloqueComponenteDTO> listaBloqueComponente = dto.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()
                    .stream()
                    .filter(f ->  !f.getEsTipoPresupuestoFinanciable()
                            && !f.getEsTipoPlazosEjecAnual()
                           )
                    .flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                    .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

            // Nos quedamos solo con los datos de la modificacion actual (p.ej. los
            // bloques de texto pueden tener datos de modificaciones anteriores del
            // mismo expediente) para que no se pierda/confunda el valor a mostrar
            for (BloqueComponenteDTO bloqueComponente : listaBloqueComponente) {
                if (bloqueComponente.getDatos() != null) {
                    List<DatoDTO> datosBloqueComp = bloqueComponente.getDatos().stream()
                            .filter(x -> x.getExpedienteModifPresPlazoOtraDTO() != null
                                    && x.getExpedienteModifPresPlazoOtraDTO()
                                    .getIdExpedienteModifPresPlazoOtra() != null
                                    && x.getExpedienteModifPresPlazoOtraDTO()
                                            .getIdExpedienteModifPresPlazoOtra().equals(idModificacion))
                            .collect(Collectors.toList());
                    bloqueComponente.setDatos(datosBloqueComp);
                }
            }

            // Rellenamos el hashmap de datos
            for (BloqueComponenteDTO bloqueComponente : listaBloqueComponente) {

                Integer nmFila = hamFilaBloque.get(bloqueComponente.getIdBloque());
                SimpleDateFormat formatter = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);

                if (Objects.isNull(bloqueComponente.getTipoFinanciacionDTO())
                        || Objects.isNull(bloqueComponente.getTipoFinanciacionDTO().getIdValor())) {

                    for (Integer i = 0; i < nmFila; i++) {
                        String nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (i + 1);
                        String valorCampo = StringUtils.EMPTY;

                        if (bloqueComponente.getDatos() != null
                                && bloqueComponente.getDatos().size() > i) {
                            if (Objects.nonNull(bloqueComponente.getDatos().get(i))) {
                                valorCampo = bloqueComponente.getDatos().get(i).getDsValor();
                            }
                        }

                        if (bloqueComponente.getComponenteDTO().getTipoDTO().getIdValor() != null) {
                            if (Constantes.TipoComponente.ID_CHECK.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {

                                if (bloqueComponente.getDatos() != null && StringUtils.isNotBlank(
                                        bloqueComponente.getDatos().get(i).getDsValor())) {
                                    String[] valoresCheck = bloqueComponente.getDatos().get(i)
                                            .getDsValor().split(";");
                                    for (Integer j = 0; j < valoresCheck.length; j++) {
                                        hamCampos.put("chk_" + nombreCampo + "_" + (j + 1),
                                                valoresCheck[j]);
                                    }
                                }

                                // Saltamos a otro componente
                                continue;
                            } else if (bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante()
                                    .equals(Constantes.TipoComponente.ID_COMBO)) {
                                // El tipo combo necesita el formato:
                                // idBloque_nmfila.idBloque_nmFila
                                nombreCampo = nombreCampo + "." + nombreCampo;
                            }
                          
                        } else if (bloqueComponente.getComponenteDTO().getTipoDocumentoDTO() != null
                                && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
                            // Es un bloque de documentación
                            nombreCampo = "chk_" + nombreCampo + "_doc_1";

                            // Buscamos el expedienteDoc
                            Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                                    .getLstExpedienteDocDTO().stream()
                                    .filter(x -> x.getBloqueComponenteDTO() != null && x
                                            .getBloqueComponenteDTO().getIdBloqueComponente()
                                            .equals(bloqueComponente.getIdBloqueComponente()))
                                    .findFirst();

                            if (expedienteDocDTO.isPresent()) {
                                // Si hay código de FICA, marcamos el checkbox
                                // del
                                // PDF
                                if (expedienteDocDTO.get().getCdFica() != null) {
                                    valorCampo = "true";
                                }
                            }

                        } else if (Constantes.SubTipoComponente.ID_FECHA.equals(
                                bloqueComponente.getComponenteDTO().getSubTipoDTO().getNmConstante())) {
                            // Es un campo de fecha, lo formateamos
                            formatter.format(valorCampo);
                        }

                        hamCampos.put(nombreCampo, valorCampo);

                    }

                }
            }

            // Rellenamos los datos del lugar y fecha
            LocalDate localDate = LocalDate.now();
           
            String lugar = "Madrid";
            String fechaTexto = localDate.format(DateTimeFormatter.ofPattern(Constantes.Utiles.FORMATO_FECHA, new Locale("es", "ES")));
            hamCampos.put("lugar", lugar);
            hamCampos.put("fechaTexto", fechaTexto);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return hamCampos;
    }

    @Override
    public HashMap<String, String> rellenarCamposSubsanacionReq(ExpedienteDTO dto,ImpresoDTO impresoFormulario, 
            List<ExpedienteDTO> listExpedientesParticipantes) throws ServiceException {
        
        HashMap<String, String> hamCampos = new HashMap<>();
        try {

            HashMap<Integer, Integer> hamFilaBloque = new HashMap<>();

            // Iteramos los bloques del formulario
            for (BloqueDTO bloque : impresoFormulario.getLstBloqueDTO()) {
                Integer nmFilas = 1;

                if (Constantes.Formatos.ID_LISTADO.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = bloque.getNmFilas();
                } else if (Constantes.Formatos.ID_TABLA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_AYUDA_SOLICITADA.equals(bloque.getIdFormato().getNmConstante())
                        || Constantes.Formatos.ID_FINANCIACION_ACTUACIONES.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = 0;

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloque
                            .getLstBloqueComponenteDTO()) {
                        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())) {
                            nmFilas++;
                        }
                    }

                }

                hamFilaBloque.put(bloque.getIdBloque(), nmFilas);
            }
            
            // Rellenamos los datos de los participantes
           
            SimpleDateFormat formatter = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                
            if(listExpedientesParticipantes != null && !listExpedientesParticipantes.isEmpty()) {
                for (ExpedienteDTO expedienteDTO :listExpedientesParticipantes) {
                    String nombreCampo = "_" +expedienteDTO.getIdExpediente();
                    String nombreInteresado="";
                    hamCampos.put("expediente" + nombreCampo,
                            expedienteDTO.getCdExpediente());
                    

                    hamCampos.put("dni" + nombreCampo,
                            expedienteDTO.getInteresadoDTO().getCdNumIden());
                    
                    nombreInteresado =expedienteDTO.getInteresadoDTO().getDsNombre()+ " "+expedienteDTO.getInteresadoDTO().getDsApellido1();
                    if(expedienteDTO.getInteresadoDTO().getDsApellido2() != null) {
                        nombreInteresado +=" "+ expedienteDTO.getInteresadoDTO().getDsApellido2();
                    }
                    hamCampos.put("nombreParticipante" + nombreCampo,
                            nombreInteresado   );
                    
                    hamCampos.put("estadoBeneficiario" + nombreCampo,
                            expedienteDTO.getEstadoBeneficiarioDTO().getDsValor());
                   
                }
            }

            // Extraemos la lista de bloques componentes del expediente que no
            // estén
            // dados de baja
            List<BloqueComponenteDTO> listaBloqueComponente = impresoFormulario.getLstBloqueDTO()
                    .stream().flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                    .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

            // Rellenamos el hashmap de datos
            for (BloqueComponenteDTO bloqueComponente : listaBloqueComponente) {

                Integer nmFila = hamFilaBloque.get(bloqueComponente.getIdBloque());
               
                if (Objects.isNull(bloqueComponente.getTipoFinanciacionDTO())
                        || Objects.isNull(bloqueComponente.getTipoFinanciacionDTO().getIdValor())) {

                    for (Integer i = 0; i < nmFila; i++) {
                        String nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (i + 1);
                        String valorCampo = StringUtils.EMPTY;

                        if (bloqueComponente.getDatos() != null
                                && !bloqueComponente.getDatos().isEmpty()
                                && Objects.nonNull(bloqueComponente.getDatos().get(i))) {
                            
                            valorCampo = bloqueComponente.getDatos().get(i).getDsValor();
                        }

                        if (bloqueComponente.getComponenteDTO().getTipoDTO().getIdValor() != null) {
                            if (Constantes.TipoComponente.ID_CHECK.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {

                                if (bloqueComponente.getDatos() != null && StringUtils.isNotBlank(
                                        bloqueComponente.getDatos().get(i).getDsValor())) {
                                    String[] valoresCheck = bloqueComponente.getDatos().get(i)
                                            .getDsValor().split(";");
                                    for (Integer j = 0; j < valoresCheck.length; j++) {
                                        hamCampos.put("chk_" + nombreCampo + "_" + (j + 1),
                                                valoresCheck[j]);
                                    }
                                }

                                // Saltamos a otro componente
                                continue;
                            } else if (bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante().equals(Constantes.TipoComponente.ID_COMBO)) {
                                // El tipo combo necesita el formato:
                                // idBloque_nmfila.idBloque_nmFila
                                nombreCampo = nombreCampo + "." + nombreCampo;
                            }
                        } else if (bloqueComponente.getComponenteDTO().getTipoDocumentoDTO() != null
                                && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
                            // Es un bloque de documentación
                            nombreCampo = "chk_" + nombreCampo + "_doc_1";

                            // Buscamos el expedienteDoc
                            Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                                    .getLstExpedienteDocDTO().stream()
                                    .filter(x -> x.getBloqueComponenteDTO() != null && x
                                            .getBloqueComponenteDTO().getIdBloqueComponente()
                                            .equals(bloqueComponente.getIdBloqueComponente()))
                                    .findFirst();

                            if (expedienteDocDTO.isPresent()) {
                                // Si hay código de FICA, marcamos el checkbox
                                // del
                                // PDF
                                if (expedienteDocDTO.get().getCdFica() != null) {
                                    valorCampo = "true";
                                }
                            }

                        } else if (bloqueComponente.getComponenteDTO().getSubTipoDTO() != null
                                && Constantes.SubTipoComponente.ID_FECHA.equals(bloqueComponente.getComponenteDTO().getSubTipoDTO().getNmConstante())) {
                            // Es un campo de fecha, lo formateamos
                            formatter.format(valorCampo);
                        }

                        hamCampos.put(nombreCampo, valorCampo);

                    }

                }
            }

            // Rellenamos los datos del lugar y fecha
            LocalDate localDate = LocalDate.now();

            String lugar = "Madrid";

            String dia = String.valueOf(localDate.getDayOfMonth());

            String mes = localDate
                    .format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")));

            String ano = String.valueOf(localDate.getYear());

            hamCampos.put("lugar", lugar);
            hamCampos.put("dia", dia);
            hamCampos.put("mes", mes);
            hamCampos.put("ano", ano);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return hamCampos;
    }

    @Override
    public HashMap<String, String> rellenarCamposSubsanacionAdm(ExpedienteDTO dto,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes)
            throws ServiceException {
        return rellenarCamposSubsanacionReq(dto, impresoFormulario, listExpedientesParticipantes);
    }
    
    @Override
    public HashMap<String, String> rellenarCamposAceptacion(ExpedienteDTO dto) throws ServiceException {

        HashMap<String, String> hamCampos = new HashMap<>();

        try {

            // Rellenamos los campos relacionables
            hamCampos = this.rellenarCamposRelacionables(hamCampos, dto);

            HashMap<Integer, Integer> hamFilaBloque = new HashMap<>();

            // Iteramos los bloques del formulario
            for (BloqueDTO bloque : dto.getImpresoAceptacionDTO().getLstBloqueDTO()) {
                Integer nmFilas = 1;

                if (Constantes.Formatos.ID_LISTADO.equals(bloque.getIdFormato().getNmConstante()) || Constantes.Formatos.ID_CUENTA_BANCARIA.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = bloque.getNmFilas();
                } else if (Constantes.Formatos.ID_TABLA.equals(bloque.getIdFormato().getNmConstante())) {
                    nmFilas = 0;

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloque
                            .getLstBloqueComponenteDTO()) {
                        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItColumna())) {
                            nmFilas++;
                        }
                    }

                }

                hamFilaBloque.put(bloque.getIdBloque(), nmFilas);

                // Rellenamos los datos de los participantes
                if (Boolean.TRUE.equals(Constantes.Formatos.ID_PARTICIPANTES.equals(bloque.getIdFormato().getNmConstante()))) {

                    List<ExpedienteDTO> lstExpedientesDTO = this.expedienteService
                            .buscarParticipantesPorSolicitud(dto.getIdSolicitud(),
                                    bloque.getActor().getIdActor());

                    for (Integer i = 0; i < lstExpedientesDTO.size(); i++) {
                        String nombreCampo = "_" + bloque.getIdBloque() + "_" + i;
                        ExpedienteDTO expedienteDTO = lstExpedientesDTO.get(i);
                        Boolean esPF = Constantes.Identificacion.FISICA.equals(bloque.getActor().getIdentificacionDTO().getNmConstante());

                        if (Boolean.TRUE.equals(esPF)) {
                            hamCampos.put("dni" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getCdNumIden());
                            hamCampos.put("nombre" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsNombre());
                            hamCampos.put("apellido1" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsApellido1());
                            hamCampos.put("apellido2" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsApellido2());
                        } else {
                            hamCampos.put("nif" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getCdNumIden());
                            hamCampos.put("razonSocial" + nombreCampo,
                                    expedienteDTO.getInteresadoDTO().getDsNombre());
                        }
                    }

                }

                if (Boolean.TRUE.equals(Constantes.Formatos.ID_FORMULARIO.equals(bloque.getIdFormato().getNmConstante())) &&
                        bloque.getLstBloqueComponenteDTO() != null) {
                    List<BloqueComponenteDTO> listaBloqueComponente = bloque
                            .getLstBloqueComponenteDTO().stream()
                            .filter(x -> x.getFcBajaFila() == null)
                            .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                            .collect(Collectors.toList());
                    for(BloqueComponenteDTO bc : listaBloqueComponente) {
                        if (bc.getComponenteDTO() != null && bc.getComponenteDTO().getSubTipoDTO() != null
                                && Constantes.SubTipoComponente.ID_FECHA.equals(bc.getComponenteDTO().getSubTipoDTO().getNmConstante())
                                && bc.getDatos() != null && !bc.getDatos().isEmpty()
                                && StringUtils.isNotBlank(bc.getDatos().get(0).getDsValor())){
                            // Obtener el valor de fecha actual (formato ISO)
                            String fechaStr = bc.getDatos().get(0).getDsValor();
                            
                            try {
                                // Convertir de String a Date
                                Instant instant = Instant.parse(fechaStr);
                                Date fecha = Date.from(instant);

                                // Formatear a dd/MM/yyyy en la zona horaria de Madrid,
                                // para no restar un día si la zona por defecto de la JVM no es Europe/Madrid
                                SimpleDateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
                                String fechaFormateada = sdf.format(fecha);

                                // Guardar el nuevo valor (en formato dd/MM/yyyy)
                                bc.getDatos().get(0).setDsValor(fechaFormateada);

                            } catch (Exception e) {
                                log.error("Error al convertir fecha: " + fechaStr);
                            }
                        }
                    }
                }
            }

            // Extraemos la lista de bloques componentes del expediente que no
            // estén
            // dados de baja
            List<BloqueComponenteDTO> listaBloqueComponente = dto.getImpresoAceptacionDTO().getLstBloqueDTO()
                    .stream()
                    .filter(f -> !f.getEsTipoPresupuestoPresentado() && !f.getEsTipoPresupuestoFinanciable()
                            && !f.getEsTipoAyudaSolicitada() && !f.getEsTipoActuaciones())
                    .flatMap(x -> x.getLstBloqueComponenteDTO().stream())
                    .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

            // Rellenamos el hashmap de datos
            for (BloqueComponenteDTO bloqueComponente : listaBloqueComponente) {

                Integer nmFila = hamFilaBloque.get(bloqueComponente.getIdBloque());
                SimpleDateFormat formatter = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);

                if (Objects.isNull(bloqueComponente.getTipoFinanciacionDTO())
                        || Objects.isNull(bloqueComponente.getTipoFinanciacionDTO().getIdValor())) {

                    for (Integer i = 0; i < nmFila; i++) {
                        String nombreCampo = bloqueComponente.getIdBloqueComponente() + "_"
                                + (i + 1);
                        String valorCampo = StringUtils.EMPTY;

                        if (bloqueComponente.getDatos() != null
                                && bloqueComponente.getDatos().size() > i) {
                            if (Objects.nonNull(bloqueComponente.getDatos().get(i))) {
                                valorCampo = bloqueComponente.getDatos().get(i).getDsValor();
                            }
                        }

                        if (bloqueComponente.getComponenteDTO().getTipoDTO().getIdValor() != null) {
                            if (Constantes.TipoComponente.ID_CHECK.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {

                                if (bloqueComponente.getDatos() != null && StringUtils.isNotBlank(
                                        bloqueComponente.getDatos().get(i).getDsValor())) {
                                    String[] valoresCheck = bloqueComponente.getDatos().get(i)
                                            .getDsValor().split(";");
                                    for (Integer j = 0; j < valoresCheck.length; j++) {
                                        hamCampos.put("chk_" + nombreCampo + "_" + (j + 1),
                                                valoresCheck[j]);
                                    }
                                }

                                // Saltamos a otro componente
                                continue;
                            } else if (bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante()
                                    .equals(Constantes.TipoComponente.ID_COMBO)) {
                                // El tipo combo necesita el formato:
                                // idBloque_nmfila.idBloque_nmFila
                                nombreCampo = nombreCampo + "." + nombreCampo;
                               
                            }else if(Constantes.TipoComponente.ID_RADIO.equals(bloqueComponente.getComponenteDTO().getTipoDTO().getNmConstante())) {
                                nombreCampo= "radio_" +nombreCampo;
                                hamCampos.put(nombreCampo, valorCampo);
                            }
                        } else if (bloqueComponente.getComponenteDTO().getTipoDocumentoDTO() != null
                                && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
                            // Es un bloque de documentación
                            
                            nombreCampo = "chk_" + nombreCampo + "_doc_1";

                            // Buscamos el expedienteDoc
                            Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                                    .getListaDocAceptacionAyuda().stream()
                                    .filter(x -> x.getBloqueComponenteDTO() != null && x
                                            .getBloqueComponenteDTO().getIdBloqueComponente()
                                            .equals(bloqueComponente.getIdBloqueComponente()))
                                    .findFirst();

                            if (expedienteDocDTO.isPresent()) {
                                // Si hay código de FICA, marcamos el checkbox
                                // del
                                // PDF
                                if (expedienteDocDTO.get().getCdFica() != null) {
                                    valorCampo = "true";
                                }
                            }

                        } else if (bloqueComponente.getComponenteDTO().getSubTipoDTO() != null
                                && Constantes.SubTipoComponente.ID_FECHA.equals(bloqueComponente.getComponenteDTO().getSubTipoDTO().getNmConstante())) {
                            // Es un campo de fecha, lo formateamos
                            formatter.format(valorCampo);
                        }

                        hamCampos.put(nombreCampo, valorCampo);

                    }

                }
            }
            
           
            List<BloqueDTO> listaBloquesDocumentos= dto.getImpresoAceptacionDTO().getLstBloqueDTO().stream()
                    .filter(f ->  f.getFcBajaFila() == null && f.getEsTipoDocumento() && f.getItAutorizaConsulta() 
                            ).collect(Collectors.toList());
            for (BloqueDTO bloque :listaBloquesDocumentos) {
                // Rellenamos el hashmap de datos
                for (BloqueComponenteDTO bloqueComponente : bloque.getLstBloqueComponenteDTO()) {
                    String nombreCampo="";
                    String valorCampo="";
                    
                 // Buscamos el expedienteDoc
                    Optional<ExpedienteDocDTO> expedienteDocDTO = dto
                            .getListaDocAceptacionAyuda().stream()
                            .filter(x -> x.getBloqueComponenteDTO() != null && x
                                    .getBloqueComponenteDTO().getIdBloqueComponente()
                                    .equals(bloqueComponente.getIdBloqueComponente()))
                            .findFirst();
                    
                    if (Constantes.TipoSeleccionDocumento.R.equals(bloque.getItTipoSelDoc())
                            && bloqueComponente.getComponenteDTO().getTipoDocumentoDTO()
                            .getIdValor() != null) {
                        
                       nombreCampo = "radio_" +bloqueComponente.getIdBloqueComponente() + "_1_doc";
                       
                        if (expedienteDocDTO.isPresent()) {
                            // Si hay código de FICA, marcamos el checkbox
                            // del
                            // PDF
                            if (expedienteDocDTO.get().getCdFica() != null && expedienteDocDTO.get().getItAutoriza() != null && expedienteDocDTO.get().getItAutoriza().equalsIgnoreCase("N") ) {
                                valorCampo = "No autorizo la consulta y aporto el documento";
                            }else if(expedienteDocDTO.get().getItAutoriza() != null && expedienteDocDTO.get().getItAutoriza().equalsIgnoreCase("S")) {
                                valorCampo = "Autorizo la consulta";
                            }
                        }
                        
                        
                    }
                    hamCampos.put(nombreCampo, valorCampo);
                    
                }
            }

            // Rellenamos los datos del lugar y fecha
            LocalDate localDate = LocalDate.now();
            //SimpleDateFormat sdfrmt = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);

            String lugar = "Madrid";

           // String dia = String.valueOf(localDate.getDayOfMonth());

            String fechaTexto = localDate
                    .format(DateTimeFormatter.ofPattern(Constantes.Utiles.FORMATO_FECHA, new Locale("es", "ES")));

           // String ano = String.valueOf(localDate.getYear());

            hamCampos.put("lugar", lugar);
            hamCampos.put("fechaTexto", fechaTexto);
           // hamCampos.put("mes", mes);
           // hamCampos.put("ano", ano);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return hamCampos;
    }

    @Override
    public HashMap<String, String> rellenarCamposJusEco(ExpedienteDTO expedienteDTO,
            List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO) throws ServiceException {
       
        HashMap<String, String> hamCampos = new HashMap<>();
        
     // Rellenamos los datos del lugar y fecha
        LocalDate localDate = LocalDate.now();
        //SimpleDateFormat sdfrmt = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);

        String lugar = "Madrid";

       // String dia = String.valueOf(localDate.getDayOfMonth());

        String fechaTexto = localDate
                .format(DateTimeFormatter.ofPattern(Constantes.Utiles.FORMATO_FECHA, new Locale("es", "ES")));

       // String ano = String.valueOf(localDate.getYear());

        hamCampos.put("lugar", lugar);
        hamCampos.put("fechaTexto", fechaTexto);
        
        
        return hamCampos;
    }

    private BigDecimal parsearValorFlexible(String valor) {
        String v = valor.trim();
        if (v.contains(",") && v.contains(".")) {
            if (v.lastIndexOf(',') > v.lastIndexOf('.')) {
                // Formato europeo: "1.234,50" → punto=miles, coma=decimal
                v = v.replace(".", "").replace(",", ".");
            } else {
                // Formato anglosajón: "1,234.50" → coma=miles, punto=decimal
                v = v.replace(",", "");
            }
        } else if (v.contains(",")) {
            // Solo coma: "1,50" → coma=decimal
            v = v.replace(",", ".");
        }
        
        
        return BigDecimal.valueOf(Double.valueOf(v));
    }

}
