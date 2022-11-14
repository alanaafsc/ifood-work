package com.github.alanaafsc.ifood.pedido;

import io.smallrye.common.annotation.Blocking;
import org.bson.types.Decimal128;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;

@ApplicationScoped
public class PedidoRealizadoIncoming {

    @Inject
    ESService elastic;

    @Incoming("pedidos")
    @Blocking
    public void lerPedidos(PedidoRealizadoDTO dto){
        System.out.println("---------------------");
        System.out.println(dto);

        Pedido p = new Pedido();
        p.cliente = dto.cliente;
        p.pratos = new ArrayList<>();
        dto.pratos.forEach(prato -> p.pratos.add(from(prato)));
        Restaurante restaurante = new Restaurante();
        restaurante.nome = dto.restaurante.nome;
        p.restaurante = restaurante;
        String json = JsonbBuilder.create().toJson(dto);
        elastic.index("pedidos", json);
        p.persist();
    }

    private Prato from(PratoPedidoDTO prato) {
        Prato p = new Prato();
        p.descricao = prato.descricao;
        p.nome = prato.nome;
        p.preco = new Decimal128(prato.preco);
        return p;
    }
}
