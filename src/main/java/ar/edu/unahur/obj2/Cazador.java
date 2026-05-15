package ar.edu.unahur.obj2;

import ar.edu.unahur.obj2.profugos.IProfugo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Cazador {
    private Integer experiencia;
    private final List<IProfugo> capturados = new ArrayList<>();

    public Cazador(Integer experiencia) {
        this.experiencia = experiencia;
    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public List<IProfugo> getCapturados() {
        return capturados;
    }

    public Integer cantidadCapturados() {
        return capturados.size();
    }

    // Template Method: define el flujo general de captura en una zona
    public void procesarCaptura(Zona zona) {
        List<IProfugo> capturadosEstaVez = new ArrayList<>();
        List<IProfugo> intimidados = new ArrayList<>();

        // Snapshot para evitar ConcurrentModificationException
        Set<IProfugo> profugosActuales = Set.copyOf(zona.getProfugos());

        for (IProfugo profugo : profugosActuales) {
            if (puedeCapturar(profugo)) {
                capturadosEstaVez.add(profugo);
            } else {
                intimidar(profugo);
                intimidados.add(profugo);
            }
        }

        zona.eliminarCapturados(capturadosEstaVez);
        capturados.addAll(capturadosEstaVez);
        sumarExperiencia(intimidados, capturadosEstaVez.size());
    }

    private boolean puedeCapturar(IProfugo profugo) {
        return experiencia > profugo.getInocencia() && condicionEspecifica(profugo);
    }

    protected abstract boolean condicionEspecifica(IProfugo profugo);

    private void intimidar(IProfugo profugo) {
        profugo.disminuirInocencia();
        efectoIntimidacion(profugo);
    }

    protected abstract void efectoIntimidacion(IProfugo profugo);

    private void sumarExperiencia(List<IProfugo> intimidados, int numCapturados) {
        int minHabilidad = intimidados.stream()
                .mapToInt(IProfugo::getHabilidad)
                .min()
                .orElse(0);
        experiencia += minHabilidad + 2 * numCapturados;
    }
}
