package ar.edu.unahur.obj2.profugos;

public abstract class EntrenamientoDecorator implements IProfugo {
    protected final IProfugo delegado;

    public EntrenamientoDecorator(IProfugo delegado) {
        this.delegado = delegado;
    }

    @Override
    public Integer getInocencia() {
        return delegado.getInocencia();
    }

    @Override
    public Integer getHabilidad() {
        return delegado.getHabilidad();
    }

    @Override
    public Boolean esNervioso() {
        return delegado.esNervioso();
    }

    @Override
    public void volverseNervioso() {
        delegado.volverseNervioso();
    }

    @Override
    public void dejarDeEstarNervioso() {
        delegado.dejarDeEstarNervioso();
    }

    @Override
    public void reducirHabilidad() {
        delegado.reducirHabilidad();
    }

    @Override
    public void disminuirInocencia() {
        delegado.disminuirInocencia();
    }
}
