package ar.edu.unahur.obj2;

import ar.edu.unahur.obj2.profugos.IProfugo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Zona {
    private final String nombre;
    private final Set<IProfugo> profugos = new HashSet<>();

    public Zona(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Set<IProfugo> getProfugos() {
        return profugos;
    }

    public void agregarProfugo(IProfugo profugo) {
        profugos.add(profugo);
    }

    public void eliminarCapturados(List<IProfugo> capturados) {
        profugos.removeAll(capturados);
    }
}
