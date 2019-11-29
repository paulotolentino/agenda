package com.techboxsys.agendacontato.modelo;

import com.techboxsys.agendacontato.banco.InterfaceContato;

public class Contatos extends InterfaceContato implements Comparable<Contatos>{

    private String nome;
    private String numero;
    private String apelido;

    public Contatos(String nome, String apelido, String numero){
        this.nome = nome;
        this.numero = numero;
        this.apelido = apelido;
    }

    public Contatos(int id, String nome, String apelido, String numero){
        super.Id = id;
        this.nome = nome;
        this.numero = numero;
        this.apelido = apelido;
    }
    public int compareTo(Contatos contato) {
        return
            contato.getId() == Id &&
            contato.getNumero().equals(numero) &&
            contato.getApelido().equals(apelido) &&
            contato.getNome().equals(nome) ? 1 : 0;
    }

    public int getId(){
        return super.Id;
    }
    public String getNome() {
        return nome;
    }
    public String getNumero() {
        return numero;
    }
    public String getApelido() {
        return apelido;
    }

}
