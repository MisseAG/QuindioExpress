package model;

import java.text.Normalizer;
import java.util.*;

public class MunicipiosDisponibles {

    private final Set<String> municipiosSet;

    public MunicipiosDisponibles() {
        municipiosSet = new TreeSet<>(Arrays.asList("Armenia", "Buenavista", "Calarcá",
                "Circasia", "Córdoba", "Filandia", "Génova", "Montenegro", "Pijao", "Quimbaya",
                "Salento", "La Tebaida"));
    }

    /**
     * Normaliza para comparar:
     * - trim (espacios extremos)
     * - colapsa espacios internos
     * - quita tildes
     * - minúsculas
     */
    private static String normalizar(String texto) {
        if (texto == null) return null;
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase();
    }

    public boolean verificarMunicipio(String municipio) {
        if (municipio == null) return false;
        String buscado = normalizar(municipio);
        for (String m : municipiosSet) {
            if (normalizar(m).equals(buscado)) return true;
        }
        return false;
    }

    public Set<String> getMunicipiosSet() {
        return Collections.unmodifiableSet(municipiosSet);
    }
}
