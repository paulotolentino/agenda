package com.techboxsys.agendacontato.views;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techboxsys.agendacontato.R;
import com.techboxsys.agendacontato.banco.Alocados;
import com.techboxsys.agendacontato.modelo.Contatos;

public class Contato extends AppCompatActivity {

    private Button btnSalvar;
    private Button btnCancelar;
    private Button btnRemover;

    private EditText inputNome;
    private EditText inputNumero;
    private EditText inputApelido;
    private TextView labelNome;

    private final Context Contexto = this;

    private Alocados ContatosAlocados;
    private Contatos ContatoTelefonico;
    private boolean NovoContato = false;
    private ImageView imgTelefone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        Intent intent = getIntent();
        int idContato = intent.getIntExtra("Id", 0);

        this.ContatosAlocados = new Alocados(this.Contexto);

        this.btnSalvar = findViewById(R.id.btnSalvar);
        this.btnCancelar = findViewById(R.id.btnCancelar);
        this.btnRemover = findViewById(R.id.btnRemover);
        this.imgTelefone2 = findViewById(R.id.imgTelefone2);
        this.inputNome = findViewById(R.id.inputNome);
        this.inputApelido = findViewById(R.id.inputApelido);
        this.inputNumero = findViewById(R.id.inputNumero);
        this.labelNome = findViewById(R.id.labelNome);

        this.Inicializar(idContato);
        this.InicializarEventos();
    }

    private void InicializarEventos() {
        this.inputApelido.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            if(view.hasFocus())
                return;

            String apelido = inputApelido.getText().toString();
            if(apelido.isEmpty()) {
                labelNome.setText("");
                return;
            }
            labelNome.setText(apelido);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!CamposEstaoValidos()) {
                EnviarMensagemCurta("Campos invalidos");
                return;
            }

            if(NovoContato) {
                ContatoTelefonico = new Contatos(inputNome.getText().toString(), inputApelido.getText().toString(), inputNumero.getText().toString());
                SalvarContato();
                finish();
                return;
            }

            Contatos ContatoAtualizado = new Contatos(ContatoTelefonico.getId(), inputNome.getText().toString(), inputApelido.getText().toString(), inputNumero.getText().toString());

            if(ContatoAtualizado.compareTo(ContatoTelefonico) == 1){
                EnviarMensagemCurta("Sem alterações");
                return;
            }

            ContatoTelefonico = ContatoAtualizado;
            AtualizarContato();
            finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        AlertDialog.Builder removerContatoDialogBuilder = new AlertDialog.Builder(this);
        removerContatoDialogBuilder.setMessage("Deseja remover?");
        removerContatoDialogBuilder.setCancelable(true);

        removerContatoDialogBuilder.setPositiveButton(
            "Sim",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    RemoverContato();
                }
            });

        removerContatoDialogBuilder.setNegativeButton(
            "Não",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        final AlertDialog removerContatoDialog = removerContatoDialogBuilder.create();

        btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removerContatoDialog.show();
            }
        });

        imgTelefone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numero = inputNumero.getText().toString();

                if(numero.isEmpty())
                    return;

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numero));

                try {
                    Contexto.startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                    Toast.makeText(Contexto, "App sem permissão", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void RemoverContato() {
        try {
            this.ContatosAlocados.removerContato(this.ContatoTelefonico.getId());
            EnviarMensagemCurta("Sucesso!");
            finish();
        }catch (Exception e){
            EnviarMensagemCurta("Falha ao remover o contato");
            e.printStackTrace();
        }

    }

    private void AtualizarContato() {
        try {
            this.ContatosAlocados.atualizarContato(ContatoTelefonico);
            EnviarMensagemCurta("Sucesso!");
        }catch (Exception e){
            EnviarMensagemCurta("Falha ao atualizado contato");
            e.printStackTrace();
        }
    }

    public void EnviarMensagemCurta(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void SalvarContato() {
        try {
            this.ContatosAlocados.adiconar(ContatoTelefonico);
            EnviarMensagemCurta("Sucesso!");
        }catch (Exception e){
            EnviarMensagemCurta("Falha ao adicionar contato");
            e.printStackTrace();
        }
    }

    private boolean CamposEstaoValidos(){
    return
        !this.inputApelido.getText().toString().isEmpty() &&
        !this.inputNome.getText().toString().isEmpty() &&
        !this.inputNumero.getText().toString().isEmpty();
    }


    private void Inicializar(int idContato){
        if(idContato <= 0) {
            this.NovoContato = true;
            this.btnRemover.setVisibility(View.INVISIBLE);
            return;
        }

        this.ContatoTelefonico = this.ContatosAlocados.obterContato(idContato);
        this.CarregarInformacoesContato();
    }

    private void CarregarInformacoesContato(){
        this.inputNome.setText(this.ContatoTelefonico.getNome());
        this.inputApelido.setText(this.ContatoTelefonico.getApelido());
        this.inputNumero.setText(this.ContatoTelefonico.getNumero());
        this.labelNome.setText(this.ContatoTelefonico.getApelido());
    }
}
