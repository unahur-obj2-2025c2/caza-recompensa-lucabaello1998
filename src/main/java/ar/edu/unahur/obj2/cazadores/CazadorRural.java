package ar.edu.unahur.obj2.cazadores;

import ar.edu.unahur.obj2.Cazador;
import ar.edu.unahur.obj2.profugos.IProfugo;

public class CazadorRural extends Cazador {

    public CazadorRural(Integer experiencia) {
        super(experiencia);
    }

    @Override
    protected boolean condicionEspecifica(IProfugo profugo) {
        return profugo.esNervioso();
    }

    @Override
    protected void efectoIntimidacion(IProfugo profugo) {
        profugo.volverseNervioso();
    }
}
