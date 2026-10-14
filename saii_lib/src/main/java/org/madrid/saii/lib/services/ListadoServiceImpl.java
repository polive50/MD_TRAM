package org.madrid.saii.lib.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.madrid.saii.lib.dao.ListadoDAO;
import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.dto.ConvocatoriaInformeExcelDTO;
import org.madrid.saii.lib.dto.ListadoConfiguracionDTO;
import org.madrid.saii.lib.dto.ListadoDTO;
import org.madrid.saii.lib.dto.ListadoSituacionEvaluadoresDTO;
import org.madrid.saii.lib.dto.converters.ListadoToDTO;
import org.madrid.saii.lib.dto.converters.ListadoToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Listado
 *
 * @author nttdata
 */
@Service("listadoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ListadoServiceImpl extends BaseServiceImpl<Listado, Integer, ListadoDAO>
        implements ListadoService {

    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Autowired
    @Qualifier("expedienteActuaService")
    private ExpedienteActuaService expedienteActuaService;

    @Autowired
    @Qualifier("listadoColumnaService")
    private ListadoColumnaService listadoColumnaService;

    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param listadoDAO
     */
    @Autowired
    public ListadoServiceImpl(@Qualifier("listadoDAO") ListadoDAO listadoDAO) {
        super();
        this.setDao(listadoDAO);
    }

    /**
     * Constructor Vacio
     */
    public ListadoServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public ListadoDTO buscarId(Integer idListado) throws ServiceException {
        return ListadoToDTO.INSTANCE.apply(this.getDao().find(idListado));
    }

    /**
     *
     */
    @Override
    public List<ListadoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), ListadoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ListadoDTO guardar(ListadoDTO listadoDTO) throws ServiceException {
        return ListadoToDTO.INSTANCE
                .apply(this.insertOrUpdate(ListadoToEntity.INSTANCE.apply(listadoDTO)));
    }

    /**
     *
     */
    @Override
    public byte[] obtenerListadoExcel(Integer idListado) throws ServiceException {

        List<ListadoSituacionEvaluadoresDTO> listaDatos = this.getDao()
                .obtenerListadoSituacionEvaluadores(idListado);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet sheet = wb.createSheet("Situación evaluadores");
            String[] cabeceras = { "Nº expediente", "Código expediente", "NIF solicitante",
                    "Solicitante", "NIF coordinador", "Coordinador", "Estado coordinador",
                    "Organismo coordinador", "NIF evaluador", "Evaluador", "Estado evaluador",
                    "Tipo de informe", "Nota final" };
            HSSFCellStyle estiloCabecera = wb.createCellStyle();
            estiloCabecera.setBorderBottom(BorderStyle.DOUBLE);
            estiloCabecera.setBorderLeft(BorderStyle.THIN);
            estiloCabecera.setBorderRight(BorderStyle.THIN);
            estiloCabecera.setAlignment(HorizontalAlignment.CENTER);
            estiloCabecera.setWrapText(true);
            HSSFRow filaCabecera = sheet.createRow(0);
            for (int columna = 0; columna < cabeceras.length; columna++) {
                HSSFCell celda = filaCabecera.createCell(columna);
                celda.setCellValue(cabeceras[columna]);
                celda.setCellStyle(estiloCabecera);
            }

            int numeroFila = 1;
            for (ListadoSituacionEvaluadoresDTO dto : listaDatos) {
                HSSFRow fila = sheet.createRow(numeroFila++);
                fila.createCell(0).setCellValue(dto.getIdExpediente() == null
                        ? "" : String.valueOf(dto.getIdExpediente()));
                fila.createCell(1).setCellValue(dto.getCdExpediente());
                fila.createCell(2).setCellValue(dto.getNifSolicitante());
                fila.createCell(3).setCellValue(dto.getSolicitante());
                fila.createCell(4).setCellValue(dto.getNifCoordinador());
                fila.createCell(5).setCellValue(dto.getCoordinador());
                fila.createCell(6).setCellValue(dto.getEstadoCoordinador());
                fila.createCell(7).setCellValue(dto.getOrganismoCoordinador());
                fila.createCell(8).setCellValue(dto.getNifEvaluador());
                fila.createCell(9).setCellValue(dto.getEvaluador());
                fila.createCell(10).setCellValue(dto.getEstadoEvaluador());
                fila.createCell(11).setCellValue(dto.getTipoInforme());
                fila.createCell(12).setCellValue(dto.getNotaFinal());
            }
            wb.write(bos);

            return bos.toByteArray();
        } catch (final IOException e1) {
            log.error(e1.getMessage(), e1);
            throw new ServiceException(e1);
        }

    }

    /**
    *
    */
    @Override
    public byte[] obtenerListadoExcel2(Integer idConvocatoria, Integer idListado)
            throws ServiceException {

        final String plantilla = "/excel/plantillaExcelInforme.xls";
        byte[] byteSalida = null;

        try (

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                InputStream inputStream = this.getClass().getResourceAsStream(plantilla);

                POIFSFileSystem fs = new POIFSFileSystem(inputStream);

                // asignar la plantilla al workbook
                HSSFWorkbook wb = new HSSFWorkbook(fs);

        ) {

            // asignar pestaña
            HSSFSheet sheet = wb.getSheetAt(0);

            // recuperamos los datos estaticos
            List<ConvocatoriaInformeExcelDTO> listaDatosEstaticos = this.getDao()
                    .obtenerListadoExcel2(idConvocatoria);

            // agregamos los datos estaticos al documento
            this.cargarDatosEstaticos(listaDatosEstaticos, sheet);

            // recuperamos los titulos dinamicos
            List<ListadoConfiguracionDTO> listaTitulosDinamicos = this.listadoColumnaService
                    .obtenerListaColumnaConfigurada(idListado);

            // agregamos los titulos dinamicos
            // this.cargarTituloDinamico(listaTitulosDinamicos, sheet, wb);

            // agregamos los datos dinamicos
            this.cargarDatoDinamico(listaDatosEstaticos, listaTitulosDinamicos, sheet);

            wb.write(bos);

            byteSalida = bos.toByteArray();

        } catch (final FileNotFoundException e1) {
            log.error(String.format("No existe la plantilla: %s error: %s", plantilla,
                    e1.getMessage()));
        } catch (final IOException e1) {
            log.error(e1.getMessage());
        }

        return byteSalida;
    }

    /**
     *
     * @param listaDatosEstaticos
     * @param sheet
     */
    private void cargarDatosEstaticos(List<ConvocatoriaInformeExcelDTO> listaDatosEstaticos,
            HSSFSheet sheet) {
        // inyectar datos al informe -> trabajar a partir de la fila 1
        int fila = 1;
        boolean existeExpediente = listaDatosEstaticos.stream()
                .anyMatch(l -> StringUtils.isNotBlank(l.getCdExpediente()));
        if (existeExpediente) {
            for (ConvocatoriaInformeExcelDTO dto : listaDatosEstaticos) {

                HSSFRow rowDataX = sheet.createRow(fila);
                HSSFCell cellDataX = rowDataX.createCell(0);
                cellDataX.setCellValue(dto.getCdAcronimoConvocatoria());

                cellDataX = rowDataX.createCell(1);
                cellDataX.setCellValue(dto.getCdExpediente());

                cellDataX = rowDataX.createCell(2);
                cellDataX.setCellValue(dto.getDsNombreInteresado());

                cellDataX = rowDataX.createCell(3);
                cellDataX.setCellValue(dto.getDsTipoIdentificadorInteresado());

                cellDataX = rowDataX.createCell(4);
                cellDataX.setCellValue(dto.getCdIdentificadorInteresado());

                cellDataX = rowDataX.createCell(5);
                cellDataX.setCellValue(dto.getNmNotaFinalExpediente());

                cellDataX = rowDataX.createCell(6);
                cellDataX.setCellValue(dto.getDsValor());

                cellDataX = rowDataX.createCell(7);
                cellDataX.setCellValue(dto.getItAlegaciones());

                fila++;

            }
        }
    }

    /**
     *
     * @param listaTitulosDinamicos
     * @param sheet
     * @throws ServiceException
     */
    private void cargarTituloDinamico(List<ListadoConfiguracionDTO> listaTitulosDinamicos,
            HSSFSheet sheet, HSSFWorkbook wb) {

        AtomicInteger indiceData = new AtomicInteger(7);
        HSSFRow rowDataX = sheet.getRow(0);

        for (ListadoConfiguracionDTO columna : listaTitulosDinamicos) {

            if (utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_PROVEEDOR)
                    .equals(columna.getIdComponente())
                    || utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE)
                            .equals(columna.getIdComponente())) {
                for (int i = 1; i <= 3; i++) {
                    HSSFCell cellData = rowDataX.getCell(indiceData.incrementAndGet());
                    cellData.setCellValue(columna.getDsTematica().toUpperCase() + " - "
                            + columna.getDsComponente().toUpperCase() + " - " + i);
                }
            } else {
                HSSFCell cellData = rowDataX.createCell(indiceData.incrementAndGet());
                if (Objects.isNull(columna.getDsComponente())) {
                    cellData.setCellValue(columna.getDsTematica().toUpperCase());
                } else {
                    cellData.setCellValue(columna.getDsTematica().toUpperCase() + " - "
                            + columna.getDsComponente().toUpperCase());
                }

            }

        }

    }

    /**
     *
     * @param listaDatosEstaticos
     * @param listaTitulosDinamicos
     * @param sheet
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    private void cargarDatoDinamico(List<ConvocatoriaInformeExcelDTO> listaDatosEstaticos,
            List<ListadoConfiguracionDTO> listaTitulosDinamicos, HSSFSheet sheet)
            throws ServiceException {

        int indexFila = 1;

        List<Integer> lstIdExpediente = listaDatosEstaticos.stream()
                .mapToInt(ConvocatoriaInformeExcelDTO::getIdExpediente).boxed()
                .collect(Collectors.toList());

        int reservadoActuaciones = this.expedienteService
                .obtenerCantidadColumnasReservadaActuacion(lstIdExpediente);

        // Iteramos los registro estaticos
        for (ConvocatoriaInformeExcelDTO fila : listaDatosEstaticos) {

            Map<String, Object> mapaColeccionDatos = this.expedienteService
                    .obtenerMapaDatosExcel(fila.getIdExpediente());

            // Validamos que haya datos
            if (Objects.nonNull(mapaColeccionDatos) && !mapaColeccionDatos.isEmpty()) {

                AtomicInteger indiceData = new AtomicInteger(7);
                HSSFRow rowDataX = sheet.getRow(indexFila);

                // Iteramos los titulos dinamicos para cargar su dato en la
                // posicion correspondiente
                for (ListadoConfiguracionDTO cabecera : listaTitulosDinamicos) {

                    String valor = "";

                    if (Objects.nonNull(cabecera.getIdComponente())) {
                        
                        Integer idComFinOfeSelProv = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_PROVEEDOR);
                        Integer idComFinOfeSelImp = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE);
                        Integer idComFinMemExpl = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.MEMORIAS_EXPLICATIVA);
                        
                        if (idComFinOfeSelProv.equals(cabecera.getIdComponente())
                                || idComFinOfeSelImp.equals(cabecera.getIdComponente())
                                || idComFinMemExpl.equals(cabecera.getIdComponente())) {

                            Map<String, String> mapaDatos = (Map<String, String>) mapaColeccionDatos
                                    .get("actuaciones_fin");

                            // titulos
                            for (int i = 1; i <= reservadoActuaciones; i++) {
                                HSSFRow rowDataT = sheet.getRow(0);
                                HSSFCell cellData = rowDataT
                                        .createCell(indiceData.incrementAndGet());
                                cellData.setCellValue(cabecera.getDsTematica().toUpperCase() + " - "
                                        + cabecera.getDsComponente().toUpperCase());
                            }

                            if (idComFinOfeSelProv.equals(cabecera.getIdComponente())) {

                                for (int i = 1; i <= reservadoActuaciones; i++) {
                                    if (Objects.nonNull(mapaDatos.get("PROV_" + i))) {
                                        valor = "[" + mapaDatos.get("TIT_" + i) + "] "
                                                + mapaDatos.get("PROV_" + i);
                                    }
                                    HSSFCell cellData = rowDataX
                                            .createCell(indiceData.incrementAndGet());
                                    cellData.setCellValue(valor);
                                }

                            } else if (idComFinOfeSelImp.equals(cabecera.getIdComponente())) {

                                for (int i = 1; i <= reservadoActuaciones; i++) {
                                    if (Objects.nonNull(mapaDatos.get("IMPO_" + i))) {
                                        valor = "[" + mapaDatos.get("TIT_" + i) + "] "
                                                + mapaDatos.get("IMPO_" + i);
                                    }
                                    HSSFCell cellData = rowDataX
                                            .createCell(indiceData.incrementAndGet());
                                    cellData.setCellValue(valor);
                                }

                            } else if (idComFinMemExpl.equals(cabecera.getIdComponente())) {

                                for (int i = 1; i <= reservadoActuaciones; i++) {
                                    if (Objects.nonNull(mapaDatos.get("MEMO_" + i))) {
                                        valor = "[" + mapaDatos.get("TIT_" + i) + "] "
                                                + mapaDatos.get("MEMO_" + i);
                                    }
                                    HSSFCell cellData = rowDataX
                                            .createCell(indiceData.incrementAndGet());
                                    cellData.setCellValue(valor);
                                }

                            }

                        }

                        else if (utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_INVERSION)
                                .equals(cabecera.getIdComponente())) {

                            Map<String, String> mapaDatos = (Map<String, String>) mapaColeccionDatos
                                    .get("actuaciones_fin");

                            valor = mapaDatos.get("AYUDA");
                            HSSFCell cellData = rowDataX.createCell(indiceData.incrementAndGet());
                            cellData.setCellValue(valor);

                        } else {

                            Map<String, String> mapaDatos = (Map<String, String>) mapaColeccionDatos
                                    .get("comun");
                            valor = mapaDatos.get(String.valueOf(cabecera.getIdComponente()));
                            HSSFCell cellData = rowDataX.createCell(indiceData.incrementAndGet());
                            cellData.setCellValue(valor);
                        }
                    }

                    else if (Objects.nonNull(cabecera.getIdTipoFinanciacion())) {
                        // valor =
                        // mapaDatos.get(String.valueOf(cabecera.getIdTipoFinanciacion()));
                        // HSSFCell cellData =
                        // rowDataX.createCell(indiceData.incrementAndGet());
                        // cellData.setCellValue(valor);
                    }

                    else {
                        // HSSFCell cellData =
                        // rowDataX.createCell(indiceData.incrementAndGet());
                        // cellData.setCellValue(valor);
                    }

                }

                indexFila++;

            }

        }

    }

}
