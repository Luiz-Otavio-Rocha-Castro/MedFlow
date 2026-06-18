package com.mycompany.repository;

import com.mycompany.model.Leito;
import com.mycompany.model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class LeitoRepository {
    List<Leito> leitos = new ArrayList<>();
    private static Integer codigo = 1;

    public List<Leito> buscartodos(){
        return leitos;
    }


    public Leito buscarPorId(String leito){
        return leitos.stream()
                .filter(l -> l.getLeito().equals(leito))
                .findFirst()
                .orElse(null);
    }

    public Leito adicionar(Leito leito){
        leito.setId(codigo);
        codigo += 1;
        leitos.add(leito);
        return leito;
    }

    public void deletar(Integer id){leitos.removeIf(leito -> leito.getId().equals(id));
    }

    public Leito atualizar(String l, Leito leito){
        Leito dadosVelhos = buscarPorId(l);
        leito.setId(dadosVelhos.getId());
        leitos.set(leitos.indexOf(dadosVelhos), leito);
        return leito;
    }
}
