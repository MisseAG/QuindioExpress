package app;

import model.AlgoritmosPaquetes;
import model.CentroDistribucion;
import model.ComparadoresPaquete;
import model.Paquete;
import model.Repartidor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final List<Paquete> PAQUETES = List.of(
            new Paquete("PQ731", "Armenia", 2.5, (byte) 3, 45),
            new Paquete("PQ105", "Calarcá", 8.0, (byte) 5, 30),
            new Paquete("PQ942", "Quimbaya", 1.2, (byte) 2, 60),
            new Paquete("PQ418", "Salento", 12.5, (byte) 4, 25),
            new Paquete("PQ867", "Circasia", 4.7, (byte) 1, 80),
            new Paquete("PQ236", "Armenia", 6.3, (byte) 5, 20),
            new Paquete("PQ594", "Quimbaya", 3.4, (byte) 3, 55),
            new Paquete("PQ319", "Calarcá", 9.1, (byte) 2, 35),
            new Paquete("PQ776", "Salento", 0.8, (byte) 5, 40),
            new Paquete("PQ154", "Circasia", 5.6, (byte) 4, 50),
            new Paquete("PQ683", "Armenia", 7.4, (byte) 1, 70),
            new Paquete("PQ527", "Quimbaya", 2.1, (byte) 4, 15),
            new Paquete("PQ899", "Salento", 10.0, (byte) 2, 65),
            new Paquete("PQ341", "Calarcá", 1.7, (byte) 3, 75),
            new Paquete("PQ612", "Circasia", 6.8, (byte) 5, 18),
            new Paquete("PQ473", "Armenia", 4.0, (byte) 2, 42),
            new Paquete("PQ728", "Quimbaya", 11.3, (byte) 1, 90),
            new Paquete("PQ290", "Salento", 3.9, (byte) 4, 33),
            new Paquete("PQ861", "Calarcá", 7.9, (byte) 5, 28),
            new Paquete("PQ406", "Circasia", 2.8, (byte) 2, 52),
            new Paquete("PQ953", "Armenia", 13.2, (byte) 4, 22),
            new Paquete("PQ177", "Quimbaya", 5.1, (byte) 3, 48),
            new Paquete("PQ644", "Salento", 1.5, (byte) 1, 85),
            new Paquete("PQ382", "Calarcá", 4.4, (byte) 4, 38),
            new Paquete("PQ815", "Circasia", 9.7, (byte) 3, 32),
            new Paquete("PQ269", "Armenia", 0.6, (byte) 5, 12),
            new Paquete("PQ537", "Quimbaya", 8.6, (byte) 2, 58),
            new Paquete("PQ704", "Salento", 6.2, (byte) 3, 44),
            new Paquete("PQ128", "Calarcá", 2.9, (byte) 1, 95),
            new Paquete("PQ986", "Circasia", 14.0, (byte) 5, 10)
    );

    private static final List<Repartidor> REPARTIDORES = List.of(
            new Repartidor("R-204", "Eduardo Mejía", "Armenia", false),
            new Repartidor("R-817", "Hector Salamanca", "Armenia", true),
            new Repartidor("R-356", "Nicolás Alaguna", "Calarcá", true),
            new Repartidor("R-941", "Elma Chacón", "Circasia", true),
            new Repartidor("R-628", "Nora Navas", "Quimbaya", true),
            new Repartidor("R-573", "Hermenegildo Angulo", "Salento", true)
    );

    public static void main(String[] args) {
        System.out.println("=== QUINDÍOEXPRESS ===");
        CentroDistribucion centro = crearCentro();
        probarRegistroYConsultas(centro);
        probarMunicipios(centro);
        probarDespachoPorLlegadaYEntregas(centro);

        System.out.println("\n=== ALGORITMOS PAQUETES ===");
        probarDespachoPrioritario();
        probarAlgoritmos(centro.paquetesEnOrdenDeRegistro());

        System.out.println("\nTodas las comprobaciones terminaron correctamente.");
    }

    private static CentroDistribucion crearCentro() {
        CentroDistribucion centro = new CentroDistribucion();
        for (Repartidor plantilla : REPARTIDORES) {
            Repartidor repartidor = new Repartidor(plantilla.getIdentificacion(), plantilla.getNombre(),
                    plantilla.getZona(), plantilla.isDisponible());
            comprobar(centro.registrarRepartidor(repartidor),
                    "Se registró el repartidor " + repartidor.getIdentificacion());
        }
        for (Paquete paquete : PAQUETES) {
            comprobar(centro.registrarPaquete(paquete), "Se registró el paquete " + paquete.getCodigo());
        }
        return centro;
    }

    private static void probarRegistroYConsultas(CentroDistribucion centro) {
        System.out.println("\n-- Registro, unicidad y consulta por código --");
        comprobar(centro.totalRegistrados() == 30, "Hay exactamente 30 paquetes registrados");
        comprobar(centro.consultarPaquete("PQ731") == PAQUETES.get(0),
                "La consulta por código retorna el objeto correcto");
        comprobar(centro.consultarPaquete("PQ000") == null, "Un código inexistente no aparece");
        comprobar(!centro.registrarPaquete(new Paquete("PQ731", "Armenia", 2, (byte) 1, 10)),
                "Se rechaza un código duplicado");
        comprobar(!centro.registrarPaquete(new Paquete("PQ-invalid", "Armenia", 0, (byte) 1, 10)),
                "Se rechaza un peso inválido");
        comprobar(centro.paquetesEnOrdenDeRegistro().equals(PAQUETES),
                "El orden de registro se conserva exactamente");
        System.out.println("Paquetes registrados: " + centro.totalRegistrados());
    }

    private static void probarMunicipios(CentroDistribucion centro) {
        System.out.println("\n-- Municipios sin repetidos y ordenados --");
        Set<String> municipios = centro.municipiosConPaquetes();
        comprobar(municipios.size() == 5, "Se conservan cinco municipios únicos");
        comprobar(centro.municipiosAlfabeticos().equals(
                        Set.of("Armenia", "Calarcá", "Circasia", "Quimbaya", "Salento")),
                "Los municipios usados están completos");
        comprobar(new ArrayList<>(centro.municipiosAlfabeticos()).equals(
                        List.of("Armenia", "Calarcá", "Circasia", "Quimbaya", "Salento")),
                "Los municipios están en orden alfabético");
        Map<String, Integer> carga = centro.cargaPorZona();
        comprobar(carga.values().stream().mapToInt(Integer::intValue).sum() == 30,
                "La carga por zona incluye todos los pendientes");
        System.out.println("Municipios: " + centro.municipiosAlfabeticos());
        System.out.println("Carga: " + carga);
    }

    private static void probarDespachoPorLlegadaYEntregas(CentroDistribucion centro) {
        System.out.println("\n-- Despacho FIFO, asignación por zona e historial --");
        comprobar(!centro.consultarRepartidor("R-204").isDisponible(),
                "El repartidor no disponible no se habilita automáticamente");

        for (Paquete esperado : PAQUETES) {
            Paquete despachado = centro.despacharPorLlegada();
            comprobar(despachado == esperado, "FIFO despacha " + esperado.getCodigo());
            Repartidor repartidor = centro.asignarPaquete(despachado);
            comprobar(repartidor != null && repartidor.getZona().equals(esperado.getDestino()),
                    "La asignación respeta la zona de " + esperado.getCodigo());
            comprobar(centro.registrarEntrega(esperado.getCodigo()),
                    "Se registra la entrega de " + esperado.getCodigo());
        }

        List<Paquete> historial = new ArrayList<>();
        centro.getHistorial().forEach(historial::add);
        comprobar(historial.equals(PAQUETES), "El historial conserva el orden real de entrega");
        comprobar(centro.totalEntregados() == 30 && centro.totalPendientes() == 0,
                "La jornada finaliza sin paquetes pendientes");
        comprobar(centro.despacharPorLlegada() == null, "La cola FIFO queda vacía");
        System.out.println("Entregas registradas: " + centro.totalEntregados());
    }

    private static void probarDespachoPrioritario() {
        CentroDistribucion centro = crearCentro();
        List<Paquete> esperados = new ArrayList<>(PAQUETES);
        esperados.sort(ComparadoresPaquete.DESPACHO);
        List<Paquete> despachados = new ArrayList<>();
        for (int i = 0; i < PAQUETES.size(); i++) {
            despachados.add(centro.despacharPorPrioridad());
        }
        comprobar(despachados.equals(esperados),
                "El despacho usa prioridad, luego tiempo y finalmente código");
        System.out.println("Primeros tres despachos prioritarios: "
                + despachados.subList(0, 3).stream().map(Paquete::getCodigo).toList());
    }

    private static void probarAlgoritmos(List<Paquete> registrados) {
        List<Paquete> ordenados = new ArrayList<>(registrados);
        ordenados.sort(Comparator.naturalOrder());
        int posicion = AlgoritmosPaquetes.busquedaBinaria(ordenados, "PQ731");
        comprobar(posicion >= 0 && ordenados.get(posicion).getCodigo().equals("PQ731"),
                "La búsqueda binaria encuentra PQ731");
        comprobar(AlgoritmosPaquetes.busquedaBinaria(ordenados, "PQ000") == -1,
                "La búsqueda binaria rechaza un código inexistente");

        double pesoArmenia = AlgoritmosPaquetes.pesoTotalMunicipio(registrados, "Armenia");
        comprobar(Math.abs(pesoArmenia - 34.0) < 0.0001,
                "La suma recursiva de peso para Armenia es correcta");
        comprobar(AlgoritmosPaquetes.contarPrioridadMinima(registrados, (byte) 5) == 7,
                "El conteo recursivo de prioridad mínima 5 es correcto");
        comprobar(AlgoritmosPaquetes.mayorPeso(registrados).getCodigo().equals("PQ986"),
                "Divide y vencerás encuentra el paquete de mayor peso");
        System.out.println("Peso total en Armenia: " + pesoArmenia + " kg");
        System.out.println("Paquetes con prioridad 5 o superior: "
                + AlgoritmosPaquetes.contarPrioridadMinima(registrados, (byte) 5));
        System.out.println("Mayor peso: " + AlgoritmosPaquetes.mayorPeso(registrados));
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError("FALLÓ: " + mensaje);
        }
        System.out.println("[OK] " + mensaje);
    }
}
