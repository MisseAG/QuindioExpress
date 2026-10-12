package model;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class MunicipiosDisponibles {

    private final Set<String> municipiosSet;

    public MunicipiosDisponibles() {
        municipiosSet = new TreeSet<>(Arrays.asList("Armenia", "Buenavista", "Calarcá",
                "Circasia", "Córdoba", "Filandia", "Génova", "Montenegro", "Pijao", "Quimbaya",
                "Salento", "La Tebaida"));
    }

    public boolean verificarMunicipio(String municipio) {
        if (municipio == null) {
            return false;
        }
        for (String m : municipiosSet) {
            if (m.equalsIgnoreCase(municipio.trim())) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getMunicipiosSet() {
        return municipiosSet;
    }
}
