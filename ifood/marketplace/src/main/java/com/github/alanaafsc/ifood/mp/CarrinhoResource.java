package com.github.alanaafsc.ifood.mp;

import com.github.alanaafsc.ifood.mp.dto.PedidoRealizadoDTO;
import com.github.alanaafsc.ifood.mp.dto.PratoDTO;
import com.github.alanaafsc.ifood.mp.dto.PratoPedidoDTO;
import com.github.alanaafsc.ifood.mp.dto.RestauranteDTO;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Path("/carrinho")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class CarrinhoResource {

    private static final String CLIENTE = "a";

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    @Inject
    @Channel("pedidos")
    Emitter<PedidoRealizadoDTO> emitterPedido;

    @GET
    public Uni<List<PratoCarrinho>> buscarCarrinho() {
        return PratoCarrinho.findCarrinho(client, CLIENTE);
    }

    @POST
    @Path("/prato/{idPrato}")
    public Uni<Long> adicionarPrato(@PathParam("idPrato") Long idPrato) {
        //poderia retornar pedido atual
        PratoCarrinho pc = new PratoCarrinho();
        pc.cliente = CLIENTE;
        pc.prato = idPrato;
        return PratoCarrinho.save(client, CLIENTE, idPrato);
    }

    @POST
    @Path("/realizar-pedido")
    public Uni<Boolean> finalizar(){
        PedidoRealizadoDTO pedido = new PedidoRealizadoDTO();
        String cliente = CLIENTE;
        pedido.cliente = cliente;
        List<PratoCarrinho> pratoCarrinho = PratoCarrinho.findCarrinho(client, CLIENTE)
                .await().indefinitely();
        //Utilizar mapstruts
        List<PratoPedidoDTO> pratos = pratoCarrinho.stream().map(pc -> from(pc)).collect(Collectors.toList());
        pedido.pratos = pratos;

        RestauranteDTO restaurante = new RestauranteDTO();
        restaurante.nome = "nome restaurante";
        pedido.restaurante = restaurante;
        emitterPedido.send(pedido);
        return PratoCarrinho.delete(client, cliente);
    }
    private PratoPedidoDTO from(PratoCarrinho pratoCarrinho) {
        PratoDTO dto = Prato.findById(client, pratoCarrinho.prato).await().indefinitely();
        return new PratoPedidoDTO(dto.nome, dto.descricao, dto.preco);
    }
}
