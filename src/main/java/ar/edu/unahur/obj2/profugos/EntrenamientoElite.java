package ar.edu.unahur.obj2.profugos;

public class EntrenamientoElite extends EntrenamientoDecorator {

    public EntrenamientoElite(IProfugo delegado) {
        super(delegado);
    }

    @Override
    public Boolean esNervioso() {
        return false;
    }

    @Override
    public void volverseNervioso() {
        // El entrenamiento de elite suprime el nerviosismo
    }
}
