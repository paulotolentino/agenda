package com.techboxsys.agendacontato;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techboxsys.agendacontato.modelo.Contatos;
import com.techboxsys.agendacontato.views.Contato;

import java.util.List;

public class Lista  extends ArrayAdapter<String> {

    private Activity Contexto;
    private String[] Nomes;
    private List<Contatos> Contatos;

    public Lista(Activity contexto, String[] nomes, List<Contatos> contatos) {
        super(contexto, R.layout.activity_item, nomes);
        this.Contexto = contexto;
        this.Nomes = nomes;
        this.Contatos = contatos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = this.Contexto.getLayoutInflater();

        if (convertView == null)
            row = inflater.inflate(R.layout.activity_item, null, true);

        TextView nomeContatoLabel = row.findViewById(R.id.nomeContato);
        ImageView imgTelefone = row.findViewById(R.id.imgTelefone);

        nomeContatoLabel.setText(this.Nomes[position]);

        imgTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Contatos.get(position).getNumero()));

                try {
                    Contexto.startActivity(intent);
                }catch (SecurityException e){
                    Toast.makeText(Contexto, "App não autorizado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(Contexto, Contato.class);
            i.putExtra("Id", Contatos.get(position).getId());
            Contexto.startActivity(i);
            }
        });

        return  row;
    }

}
