package com.github.alanaafsc.ifood.cadastro.panache;

import com.github.alanaafsc.ifood.cadastro.Restaurante;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

public class PanacheQueries {

    public void exemplosSelects() {
        //METODOS DA CLASSE
        //findAll
        PanacheQuery<Restaurante> findAll = Restaurante.findAll();
        Restaurante.findAll(Sort.by("nome").and("id", Sort.Direction.Ascending));

        PanacheQuery<Restaurante> page = findAll.page(Page.of(3, 10));

        //find sem sort
        Map<String, Object> params = new HashMap<>();
        params.put("nome", "");
        Restaurante.find("select r from Restaurante where nome = :nome", params);

        String nome = "";
        Restaurante.find("select r from Restaurante where nome = $1", nome);

        Restaurante.find("select r from Restaurante where nome = :nome and id = :id",
                Parameters.with("nome", "").and("id", 1L));

        //find com stream, mas precisa de transacao se nao o cursor pode fechar antes
        Restaurante.stream("select r from Restaurante where nome = :nome", params);

        Restaurante.stream("select r from Restaurante where nome = $1", nome);

        Restaurante.stream("select r from Restaurante where nome = :nome and id = :id",
                Parameters.with("nome", "").and("id", 1L));

        //find by id
        Restaurante restaurante = Restaurante.findById(1L);

        //Persist
        Restaurante.persist(null, null);

        //delete
        Restaurante.delete("delete Restaurante where nome = :nome", params);
        //pode ser da seguinte forma tambem:
        Restaurante.delete("nome = :nome", params);

        //update
        Restaurante.update("update Restaurante where nome = :nome", params);
        //pode ser da seguinte forma tambem:
        Restaurante.update("nome = :nome", params);

        //count
        Restaurante.count(); //qntd de elementos na tabela

        //METODOS DA INSTANCIA
        Restaurante restaurante1 = new Restaurante();
        restaurante1.persist();
        restaurante1.isPersistent(); //ver se persist ocorreu
        restaurante1.delete(); //apaga essa instancia restaurante1

    }

    @Entity
    class MinhaEntidade1 extends PanacheEntity {
        public String nome;
    }

    @Entity
    class MinhaEntidade2 extends PanacheEntityBase {
        public String nome;
    }

    @Entity
    class MinhaEntidade3 {
        public String nome;
    }

    @ApplicationScoped
    class MeuRepositorio implements PanacheRepository<MinhaEntidade3> {

    }

}
