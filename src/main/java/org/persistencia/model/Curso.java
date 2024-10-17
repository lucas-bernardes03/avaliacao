package org.persistencia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    private Integer iden;
    private Integer ano;
    private String nome;
    private Disciplina disciplina;


    @Override
    public String toString(){
        String toString = "Identificador: " + iden + "\n";
        toString += "Nome: " + nome + "\n";
        toString += "Ano: " + ano + "\n";
        toString += "Disciplina: " + disciplina.getNome() + " | CH: " + disciplina.getCh() + "\n";
        toString += "------------------------------------------------";

        return toString;
    }
}
