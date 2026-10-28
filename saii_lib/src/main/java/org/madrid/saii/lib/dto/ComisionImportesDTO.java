package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

/**
 * VMM 27/10/26 MD-3094: desglose de importes por partida y anualidad de los expedientes de la
 * Comisión de selección, tanto lo solicitado (SAII_DATO) como lo financiable/concedido
 * (SAII_EXPEDIENTE_PRESUP / SAII_EXPEDIENTE_PRESUP_MONTO).
 */
@Getter
@Setter
public class ComisionImportesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Partidas (tipo de financiación): identificador y descripción, ordenadas por identificador. */
    private Map<Integer, String> partidas = new LinkedHashMap<>();

    /** Anualidades presentes (1, 2, ...), ordenadas. */
    private SortedSet<Integer> anualidades = new TreeSet<>();

    /** Importe solicitado: idExpediente -> idPartida -> anualidad -> importe. */
    private Map<Integer, Map<Integer, Map<Integer, Double>>> solicitado = new HashMap<>();

    /** Importe financiable/concedido: idExpediente -> idPartida -> anualidad -> importe. */
    private Map<Integer, Map<Integer, Map<Integer, Double>>> concedido = new HashMap<>();

    /** Constructor. */
    public ComisionImportesDTO() {
        super();
    }
}
