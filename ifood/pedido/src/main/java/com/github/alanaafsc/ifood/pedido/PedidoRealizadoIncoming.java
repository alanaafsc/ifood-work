package com.github.alanaafsc.ifood.pedido;

import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRealizadoIncoming {

    @Incoming("pedidos")
    @Blocking
    public void lerPedidos(PedidoRealizadoDTO dto){
        System.out.println("---------------------");
        System.out.println(dto);
    }
}
