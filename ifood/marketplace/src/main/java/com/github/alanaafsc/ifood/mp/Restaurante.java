package com.github.alanaafsc.ifood.mp;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

public class Restaurante {
    public Long id;

    public String nome;

    public Localizacao localizacao;

    @Override
    public String toString() {
        return "Restaurante [id=" + id + ", nome=" + nome + ", localizacao=" + localizacao + "]";
    }
    @Blocking
    public void persist(PgPool pgPool) {
        pgPool.preparedQuery("insert into localizacao (id, latitude, longitude) values ($1, $2, $3)")
                .execute(Tuple.of(localizacao.id, localizacao.latitude, localizacao.longitude)).await().indefinitely();

        pgPool.preparedQuery("insert into restaurante (id, nome, localizacao_id) values ($1, $2, $3)")
                .execute(Tuple.of(id, nome, localizacao.id)).await().indefinitely();
    }
}
