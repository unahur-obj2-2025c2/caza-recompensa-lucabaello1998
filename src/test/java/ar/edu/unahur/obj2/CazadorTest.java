package ar.edu.unahur.obj2;

import ar.edu.unahur.obj2.cazadores.CazadorRural;
import ar.edu.unahur.obj2.cazadores.CazadorSigiloso;
import ar.edu.unahur.obj2.cazadores.CazadorUrbano;
import ar.edu.unahur.obj2.profugos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CazadorTest {

    // ─────────────────────────────────────────────
    // PARTE I – Proceso de captura
    // ─────────────────────────────────────────────

    @Test
    void cazador_urbano_captura_profugo_tranquilo_con_baja_inocencia() {
        CazadorUrbano urbano = new CazadorUrbano(60);
        Profugo profugo = new Profugo(30, 40, false); // tranquilo, inocencia < experiencia
        Zona zona = new Zona("Centro");
        zona.agregarProfugo(profugo);

        urbano.procesarCaptura(zona);

        assertEquals(1, urbano.cantidadCapturados());
        assertFalse(zona.getProfugos().contains(profugo));
    }

    @Test
    void cazador_urbano_no_captura_profugo_nervioso() {
        CazadorUrbano urbano = new CazadorUrbano(60);
        Profugo profugo = new Profugo(30, 40, true); // nervioso → condición falla
        Zona zona = new Zona("Centro");
        zona.agregarProfugo(profugo);

        urbano.procesarCaptura(zona);

        assertEquals(0, urbano.cantidadCapturados());
        assertTrue(zona.getProfugos().contains(profugo));
    }

    @Test
    void cazador_rural_captura_profugo_nervioso() {
        CazadorRural rural = new CazadorRural(60);
        Profugo profugo = new Profugo(30, 40, true); // nervioso → condición OK
        Zona zona = new Zona("Campo");
        zona.agregarProfugo(profugo);

        rural.procesarCaptura(zona);

        assertEquals(1, rural.cantidadCapturados());
    }

    @Test
    void cazador_sigiloso_captura_profugo_de_baja_habilidad() {
        CazadorSigiloso sigiloso = new CazadorSigiloso(60);
        Profugo profugo = new Profugo(30, 40, false); // habilidad < 50
        Zona zona = new Zona("Bosque");
        zona.agregarProfugo(profugo);

        sigiloso.procesarCaptura(zona);

        assertEquals(1, sigiloso.cantidadCapturados());
    }

    @Test
    void cazador_sigiloso_no_captura_profugo_de_alta_habilidad() {
        CazadorSigiloso sigiloso = new CazadorSigiloso(60);
        Profugo profugo = new Profugo(30, 80, false); // habilidad >= 50 → falla
        Zona zona = new Zona("Bosque");
        zona.agregarProfugo(profugo);

        sigiloso.procesarCaptura(zona);

        assertEquals(0, sigiloso.cantidadCapturados());
    }

    @Test
    void cazador_no_captura_si_inocencia_es_mayor_que_experiencia() {
        CazadorUrbano urbano = new CazadorUrbano(20);
        Profugo profugo = new Profugo(50, 30, false); // inocencia > experiencia
        Zona zona = new Zona("Centro");
        zona.agregarProfugo(profugo);

        urbano.procesarCaptura(zona);

        assertEquals(0, urbano.cantidadCapturados());
    }

    // Intimidación: inocencia baja en 2
    @Test
    void intimidacion_reduce_inocencia_del_profugo() {
        CazadorUrbano urbano = new CazadorUrbano(20);
        Profugo profugo = new Profugo(50, 30, false); // no puede capturar, lo intimida
        Zona zona = new Zona("Centro");
        zona.agregarProfugo(profugo);

        urbano.procesarCaptura(zona);

        assertEquals(48, profugo.getInocencia()); // 50 - 2
    }

    // Efecto específico: CazadorUrbano deja de nervioso al intimidar
    @Test
    void cazador_urbano_deja_no_nervioso_al_intimidar() {
        CazadorUrbano urbano = new CazadorUrbano(20);
        Profugo profugo = new Profugo(50, 30, true); // nervioso, no captura (condición falla y exp < inocencia)
        Zona zona = new Zona("Centro");
        zona.agregarProfugo(profugo);

        urbano.procesarCaptura(zona);

        assertFalse(profugo.esNervioso());
    }

    // Efecto específico: CazadorRural vuelve nervioso al intimidar
    @Test
    void cazador_rural_vuelve_nervioso_al_intimidar() {
        CazadorRural rural = new CazadorRural(20);
        Profugo profugo = new Profugo(50, 30, false); // tranquilo, no puede capturar
        Zona zona = new Zona("Campo");
        zona.agregarProfugo(profugo);

        rural.procesarCaptura(zona);

        assertTrue(profugo.esNervioso());
    }

    // Efecto específico: CazadorSigiloso reduce habilidad al intimidar
    @Test
    void cazador_sigiloso_reduce_habilidad_al_intimidar() {
        CazadorSigiloso sigiloso = new CazadorSigiloso(20);
        Profugo profugo = new Profugo(50, 80, false); // alta habilidad → no captura
        Zona zona = new Zona("Bosque");
        zona.agregarProfugo(profugo);

        sigiloso.procesarCaptura(zona);

        assertEquals(75, profugo.getHabilidad()); // 80 - 5
    }

    // Experiencia: min(habilidad intimidados) + 2 * capturados
    @Test
    void cazador_suma_experiencia_tras_captura_e_intimidacion() {
        // experiencia=100 → captura a inocencia=10,habilidad=40 (tranquilo, habilidad<50 ✓ sigiloso)
        // inocencia=80,habilidad=70 → no captura (habilidad>=50), intimida → habilidad 70, inocencia 78
        CazadorSigiloso sigiloso = new CazadorSigiloso(100);
        Profugo capturado = new Profugo(10, 40, false);
        Profugo intimidado = new Profugo(80, 70, false);
        Zona zona = new Zona("Bosque");
        zona.agregarProfugo(capturado);
        zona.agregarProfugo(intimidado);

        sigiloso.procesarCaptura(zona);

        // intimidado pierde 5 de habilidad (sigiloso) antes de sumar exp: 70→65
        // capturados=1, intimidados=[65] → min=65, experiencia=100 + 65 + 2*1 = 167
        assertEquals(167, sigiloso.getExperiencia());
    }

    // ─────────────────────────────────────────────
    // PARTE II – Decoradores de prófugos
    // ─────────────────────────────────────────────

    @Test
    void artes_marciales_duplica_habilidad() {
        IProfugo profugo = new ArtesMarciales(new Profugo(20, 30, false));
        assertEquals(60, profugo.getHabilidad()); // 30 * 2
    }

    @Test
    void artes_marciales_no_supera_100() {
        IProfugo profugo = new ArtesMarciales(new Profugo(20, 70, false));
        assertEquals(100, profugo.getHabilidad()); // min(100, 70*2=140)
    }

    @Test
    void entrenamiento_elite_nunca_es_nervioso() {
        Profugo base = new Profugo(20, 30, true); // nervioso
        IProfugo elite = new EntrenamientoElite(base);

        assertFalse(elite.esNervioso());
    }

    @Test
    void entrenamiento_elite_no_se_vuelve_nervioso() {
        Profugo base = new Profugo(20, 30, false);
        IProfugo elite = new EntrenamientoElite(base);
        elite.volverseNervioso();

        assertFalse(elite.esNervioso()); // el decorator lo bloquea
    }

    @Test
    void proteccion_legal_inocencia_minima_40() {
        IProfugo protegido = new ProteccionLegal(new Profugo(10, 30, false));
        assertEquals(40, protegido.getInocencia()); // max(40, 10)
    }

    @Test
    void proteccion_legal_no_altera_inocencia_mayor_a_40() {
        IProfugo protegido = new ProteccionLegal(new Profugo(80, 30, false));
        assertEquals(80, protegido.getInocencia());
    }

    @Test
    void decoradores_se_apilan_correctamente() {
        // Artes marciales + Elite: habilidad duplicada, nunca nervioso
        Profugo base = new Profugo(20, 30, true);
        IProfugo combinado = new ArtesMarciales(new EntrenamientoElite(base));

        assertEquals(60, combinado.getHabilidad());
        assertFalse(combinado.esNervioso());
    }

    // ─────────────────────────────────────────────
    // PARTE III – Agencia
    // ─────────────────────────────────────────────

    @Test
    void agencia_lista_todos_los_profugos_capturados() {
        CazadorUrbano u1 = new CazadorUrbano(100);
        CazadorUrbano u2 = new CazadorUrbano(100);

        Profugo p1 = new Profugo(10, 30, false);
        Profugo p2 = new Profugo(10, 50, false);

        Zona z1 = new Zona("Norte");
        Zona z2 = new Zona("Sur");
        z1.agregarProfugo(p1);
        z2.agregarProfugo(p2);

        u1.procesarCaptura(z1);
        u2.procesarCaptura(z2);

        Agencia agencia = new Agencia(List.of(u1, u2));
        assertEquals(2, agencia.todosProfugosCapturados().size());
    }

    @Test
    void agencia_informa_profugo_mas_habil() {
        CazadorUrbano u1 = new CazadorUrbano(100);
        CazadorUrbano u2 = new CazadorUrbano(100);

        Profugo debil = new Profugo(10, 30, false);
        Profugo habil = new Profugo(10, 70, false);

        Zona z1 = new Zona("Norte");
        Zona z2 = new Zona("Sur");
        z1.agregarProfugo(debil);
        z2.agregarProfugo(habil);

        u1.procesarCaptura(z1);
        u2.procesarCaptura(z2);

        Agencia agencia = new Agencia(List.of(u1, u2));
        assertEquals(habil, agencia.profugoMasHabil());
    }

    @Test
    void agencia_informa_cazador_con_mas_capturas() {
        CazadorUrbano u1 = new CazadorUrbano(100);
        CazadorUrbano u2 = new CazadorUrbano(100);

        Zona z1 = new Zona("Norte");
        z1.agregarProfugo(new Profugo(10, 30, false));
        z1.agregarProfugo(new Profugo(10, 40, false));

        Zona z2 = new Zona("Sur");
        z2.agregarProfugo(new Profugo(10, 50, false));

        u1.procesarCaptura(z1); // captura 2
        u2.procesarCaptura(z2); // captura 1

        Agencia agencia = new Agencia(List.of(u1, u2));
        assertEquals(u1, agencia.cazadorConMasCapturas());
    }
}

