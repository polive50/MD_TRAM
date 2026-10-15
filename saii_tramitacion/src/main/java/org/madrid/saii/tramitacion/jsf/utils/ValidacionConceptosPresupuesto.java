package org.madrid.saii.tramitacion.jsf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;

/**
 * VMM 06/10/26 MD-3113: validaciones compartidas de los conceptos de las tablas de presupuesto
 * (solapa Financiacion y solapa Modificaciones P/P/O) en convocatorias CON conceptos:
 * <ul>
 * <li>un concepto no puede tener la descripcion vacia o en blanco</li>
 * <li>un concepto no puede repetirse dentro de la misma partida (se compara sin distinguir
 * mayusculas/minusculas ni espacios sobrantes)</li>
 * </ul>
 * Las filas TOTAL y las filas sin partida se ignoran.
 */
public final class ValidacionConceptosPresupuesto {

    private ValidacionConceptosPresupuesto() {
    }

    /**
     * @param lista filas de la tabla de presupuesto
     * @return una entrada (nombre de la partida) por cada fila con el concepto vacio o en blanco
     */
    public static List<String> partidasConConceptoVacio(List<ExpedientePresupDTO> lista) {
        List<String> resultado = new ArrayList<>();
        if (lista == null) {
            return resultado;
        }
        for (ExpedientePresupDTO fila : lista) {
            if (esFilaDeConcepto(fila) && StringUtils.isBlank(fila.getTlConcepto())) {
                resultado.add(nombrePartida(fila));
            }
        }
        return resultado;
    }

    /**
     * @param lista filas de la tabla de presupuesto
     * @return "Partida - Concepto" de cada concepto repetido dentro de su partida (una vez cada uno)
     */
    public static List<String> conceptosRepetidos(List<ExpedientePresupDTO> lista) {
        List<String> resultado = new ArrayList<>();
        if (lista == null) {
            return resultado;
        }

        Map<String, Integer> contador = new HashMap<>();
        Map<String, String> etiqueta = new HashMap<>();
        for (ExpedientePresupDTO fila : lista) {
            if (esFilaDeConcepto(fila) && StringUtils.isNotBlank(fila.getTlConcepto())) {
                String clave = fila.getTipoFinanciacionDTO().getIdValor() + "|"
                        + normalizar(fila.getTlConcepto());
                contador.merge(clave, 1, Integer::sum);
                etiqueta.putIfAbsent(clave,
                        nombrePartida(fila) + " - " + fila.getTlConcepto().trim());
            }
        }
        for (Map.Entry<String, Integer> entrada : contador.entrySet()) {
            if (entrada.getValue() > 1) {
                resultado.add(etiqueta.get(entrada.getKey()));
            }
        }
        return resultado;
    }

    /**
     * @param fila fila cuyo concepto se acaba de editar
     * @param lista tabla a la que pertenece
     * @return true si otra fila de la misma partida tiene ya ese mismo concepto
     */
    public static boolean esConceptoRepetido(ExpedientePresupDTO fila,
            List<ExpedientePresupDTO> lista) {
        if (lista == null || !esFilaDeConcepto(fila) || StringUtils.isBlank(fila.getTlConcepto())) {
            return false;
        }
        String concepto = normalizar(fila.getTlConcepto());
        for (ExpedientePresupDTO otra : lista) {
            if (otra == fila || !esFilaDeConcepto(otra)) {
                continue;
            }
            boolean mismaFila = fila.getIdExpedientePresup() != null
                    && Objects.equals(fila.getIdExpedientePresup(), otra.getIdExpedientePresup());
            if (!mismaFila
                    && Objects.equals(fila.getTipoFinanciacionDTO().getIdValor(),
                            otra.getTipoFinanciacionDTO().getIdValor())
                    && concepto.equals(normalizar(otra.getTlConcepto()))) {
                return true;
            }
        }
        return false;
    }

    private static boolean esFilaDeConcepto(ExpedientePresupDTO fila) {
        return fila != null && fila.getTipoFinanciacionDTO() != null
                && fila.getTipoFinanciacionDTO().getIdValor() != null
                && !"TOTAL".equals(fila.getTipoFinanciacionDTO().getDsValor());
    }

    private static String nombrePartida(ExpedientePresupDTO fila) {
        String ds = fila.getTipoFinanciacionDTO().getDsValor();
        return ds != null ? ds : "";
    }

    private static String normalizar(String concepto) {
        return concepto == null ? ""
                : concepto.trim().replaceAll("\\s+", " ").toLowerCase();
    }
}
