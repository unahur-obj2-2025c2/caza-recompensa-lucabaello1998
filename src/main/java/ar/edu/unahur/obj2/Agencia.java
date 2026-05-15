package ar.edu.unahur.obj2;

import ar.edu.unahur.obj2.profugos.IProfugo;

import java.util.Comparator;
import java.util.List;

public class Agencia {
    private final List<Cazador> cazadores;

    public Agencia(List<Cazador> cazadores) {
        this.cazadores = cazadores;
    }

    public List<IProfugo> todosProfugosCapturados() {
        return cazadores.stream()
                .flatMap(c -> c.getCapturados().stream())
                .toList();
    }

    public IProfugo profugoMasHabil() {
        return todosProfugosCapturados().stream()
                .max(Comparator.comparingInt(IProfugo::getHabilidad))
                .orElseThrow(() -> new RuntimeException("No hay prófugos capturados"));
    }

    public Cazador cazadorConMasCapturas() {
        return cazadores.stream()
                .max(Comparator.comparingInt(Cazador::cantidadCapturados))
                .orElseThrow(() -> new RuntimeException("No hay cazadores registrados"));
    }
}
