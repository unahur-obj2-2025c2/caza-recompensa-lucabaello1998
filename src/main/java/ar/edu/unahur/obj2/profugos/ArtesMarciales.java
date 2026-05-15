package ar.edu.unahur.obj2.profugos;

public class ArtesMarciales extends EntrenamientoDecorator {

    public ArtesMarciales(IProfugo delegado) {
        super(delegado);
    }

    @Override
    public Integer getHabilidad() {
        return Math.min(100, delegado.getHabilidad() * 2);
    }
}
