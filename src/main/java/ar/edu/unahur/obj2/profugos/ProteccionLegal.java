package ar.edu.unahur.obj2.profugos;

public class ProteccionLegal extends EntrenamientoDecorator {

    public ProteccionLegal(IProfugo delegado) {
        super(delegado);
    }

    @Override
    public Integer getInocencia() {
        return Math.max(40, delegado.getInocencia());
    }
}
