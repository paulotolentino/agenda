package com.techboxsys.agendacontato.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techboxsys.agendacontato.Lista;
import com.techboxsys.agendacontato.R;
import com.techboxsys.agendacontato.back4app.B4AContato;
import com.techboxsys.agendacontato.modelo.Contatos;
import com.techboxsys.agendacontato.banco.Alocados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;
import com.parse.Parse;
import android.app.Application;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context Contexto;
    private ListView Contatos;
    private Alocados ContatosAlocados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ContatosAlocados = new Alocados(this);

        Contexto = this;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(Contexto, Contato.class));
            }
        });

        FloatingActionButton sync = findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BuscarContatos4App();
                Log.i("Sync", "onClick: Cheguei");
                ContatosAlocados.restore();
            }
        });

//        FloatingActionButton fabDel = findViewById(R.id.fabDel);
//        fabDel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                ContatosAlocados.deleteTodos();
//                B4AContato.deleteContact();
//                Toast.makeText(Contexto, "Todos os contatos foram apagados", Toast.LENGTH_SHORT).show();
//            }
//        });


        this.Contatos = findViewById(R.id.listView);
        BuscarContatos();

        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("Y8l6BtCuSnEt0xNRjPkSiOiXVsGDtQz01Na9yvZK")
            .clientKey("jvvUBlVx5xpOVu44NKgSL2Jf80lG2ethDMBaAMrL")
            .server("https://parseapi.back4app.com")
            .build()
        );
    }

    private void BuscarContatos(){
        List<Contatos> contatosNaAgenda = this.ObterContact();
        if(contatosNaAgenda == null || contatosNaAgenda.size() <= 0) {
            this.Contatos.setAdapter(null);
            return;
        }

        String[] nomes = new String[contatosNaAgenda.size()];

        for(int i=0; i<contatosNaAgenda.size(); i++)
            nomes[i] = contatosNaAgenda.get(i).getApelido();
        Lista lista =  new Lista(this, nomes, contatosNaAgenda);
        this.Contatos.setAdapter(lista);
    }

    private void BuscarContatos4App(){
        List<Contatos> newContacts = this.ObterContact(1);
        List<Contatos> updateContacts = this.ObterContact(2);
        List<Contatos> deleteContacts = this.ObterContact(3);
        if(newContacts != null && newContacts.size() > 0) {
            for(int i=0; i<newContacts.size(); i++)
                B4AContato.createContact(newContacts.get(i));
        }
        if(updateContacts != null && updateContacts.size() > 0) {
            for(int i=0; i<updateContacts.size(); i++)
                B4AContato.updateContact(updateContacts.get(i));
        }

        if(deleteContacts != null && deleteContacts.size() > 0) {
            for(int i=0; i<deleteContacts.size(); i++)
                B4AContato.deleteContact(deleteContacts.get(i));
        }

        if((newContacts != null && newContacts.size() > 0) ||
                (updateContacts != null && updateContacts.size() > 0) ||
                (deleteContacts != null && deleteContacts.size() > 0)){
            Toast.makeText(Contexto, "Contatos sincronizadoso com sucesso", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Contexto, "Contatos já estão sincronizadoso", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Contatos> ObterContact(){

        List<Contatos> contatosNaAgenda = ContatosAlocados.obterTodosContatos();
        if(contatosNaAgenda == null || contatosNaAgenda.size() <= 0)
            return null;
        return contatosNaAgenda;
    }

    private List<Contatos> ObterContact(int i){
        List<Contatos> contatosNaAgenda;
        switch (i){
            case 1:
                contatosNaAgenda = ContatosAlocados.obterContatosTipo("N");
                break;
            case 2:
                contatosNaAgenda = ContatosAlocados.obterContatosTipo("U");
                break;
            case 3:
            default:
                contatosNaAgenda = ContatosAlocados.obterContatosTipo("D");
                break;
        }


        if(contatosNaAgenda == null || contatosNaAgenda.size() <= 0)
            return null;
        return contatosNaAgenda;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BuscarContatos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
