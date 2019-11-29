package com.techboxsys.agendacontato.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techboxsys.agendacontato.Lista;
import com.techboxsys.agendacontato.R;
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

        this.Contatos = findViewById(R.id.listView);
        BuscarContatos();
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
        Log.i("nome1===>", "nome1: " + nomes[6]);
        Lista lista =  new Lista(this, nomes, contatosNaAgenda);
        this.Contatos.setAdapter(lista);
        Log.i("Contatos===>", "BuscarContatos: " + this.Contatos);
        Log.i("lista===>", "BuscarContatos: " + lista);
    }

    private List<Contatos> ObterContact(){

        List<Contatos> contatosNaAgenda = ContatosAlocados.obterTodosContatos();
        Log.i("contatos ===>", "ObterContact: "+contatosNaAgenda);
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
