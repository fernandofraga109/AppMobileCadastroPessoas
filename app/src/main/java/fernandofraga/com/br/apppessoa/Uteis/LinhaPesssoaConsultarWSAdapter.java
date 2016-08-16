package fernandofraga.com.br.apppessoa.Uteis;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fernandofraga.com.br.apppessoa.ConsultarActivity;
import fernandofraga.com.br.apppessoa.EditarActivity;
import fernandofraga.com.br.apppessoa.R;
import fernandofraga.com.br.apppessoa.model.Pessoa;
import fernandofraga.com.br.apppessoa.model.PessoaModel;
import fernandofraga.com.br.apppessoa.repository.PessoaRepository;

/**
 * Created by fernandofraga on 05/07/16.
 */
public class LinhaPesssoaConsultarWSAdapter extends BaseAdapter {

    //CRIANDO UM OBJETO LayoutInflater PARA FAZER LINK A NOSSA VIEW(activity_linha_consultar.xml)
    private static LayoutInflater layoutInflater = null;

    //CRIANDO UMA LISTA DE PESSOAS
    List<Pessoa> pessoas =  new ArrayList<Pessoa>();

    //CRIRANDO UM OBJETO DA NOSSA CLASSE QUE FAZ ACESSO AO BANCO DE DADOS
    PessoaRepository  pessoaRepository;

    //CRIANDO UM OBJETO DA NOSSA ATIVIDADE QUE CONTEM A LISTA
    private ConsultarActivity consultarActivity;

    //CONSTRUTOR QUE VAI RECEBER A NOSSA ATIVIDADE COMO PARAMETRO E A LISTA DE PESSOAS QUE VAI RETORNAR
    //DA NOSSA BASE DE DADOS
    public LinhaPesssoaConsultarWSAdapter(ConsultarActivity consultarActivity, List<Pessoa> pessoas ) {

        this.pessoas       =  pessoas;
        this.consultarActivity  =  consultarActivity;
        this.layoutInflater     = (LayoutInflater) this.consultarActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pessoaRepository   = new PessoaRepository(consultarActivity);
    }

    //RETORNA A QUANTIDADE DE REGISTROS DA LISTA
    @Override
    public int getCount(){

        return pessoas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    //ESSE MÉTODO SETA OS VALORES DE UM ITEM DA NOSSA LISTA DE PESSOAS PARA UMA LINHA DO NOSSO LISVIEW
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        //CRIANDO UM OBJETO DO TIPO View PARA ACESSAR O NOSSO ARQUIVO DE LAYOUT activity_linha_consultar.xml
        final View viewLinhaLista = layoutInflater.inflate(R.layout.activity_linha_consultar,null);

        //VINCULANDO OS CAMPOS DO ARQUIVO DE LAYOUT(activity_linha_consultar.xml) AOS OBJETOS DECLARADOS.

        //CAMPO QUE VAI MOSTRAR O CÓDIGO DA PESSOA
        TextView textViewCodigo          = (TextView) viewLinhaLista.findViewById(R.id.textViewCodigo);

        //CAMPO QUE VAI MOSTRAR O NOME DA PESSOA
        TextView textViewNome            = (TextView) viewLinhaLista.findViewById(R.id.textViewNome);

        //CAMPO QUE VAI MOSTRAR O ENDEREÇO DA PESSOA
        TextView textViewEndereco        = (TextView) viewLinhaLista.findViewById(R.id.textViewEndereco);

        //CAMPOS QUE VAI MOSTRAR O SEXO DA PESSOA
        TextView textViewSexo            = (TextView) viewLinhaLista.findViewById(R.id.textViewSexo);

        //CAMPO QUE VAI MOSTRAR O ESTADO CIVIL
        TextView textViewEstadoCivil     = (TextView) viewLinhaLista.findViewById(R.id.textViewEstadoCivil);

        //CAMPO QUE VAI MOSTRAR A DATA DE NASCIMENTO
        TextView textViewNascimento      = (TextView) viewLinhaLista.findViewById(R.id.textViewNascimento);

        //CAMPOS QUE VAI MOSTRAR SE O REGISTRO ESTÁ ATIVO OU NÃO
        TextView textViewRegsitroAtivo   = (TextView) viewLinhaLista.findViewById(R.id.textViewRegistroAtivo);

        //CRIANDO O BOTÃO  EXCLUIR PARA DELETARMOS UM REGISTRO DO BANCO DE DADOS
        Button buttonExcluir             = (Button)   viewLinhaLista.findViewById(R.id.buttonExcluir);

        //CRIANDO O BOTÃO PARA EDITAR UM REGISTRO CADASTRADO
        Button   buttonEditar            = (Button)   viewLinhaLista.findViewById(R.id.buttonEditar);

        //SETANDO O CÓDIGO NO CAMPO DA NOSSA VIEW
        textViewCodigo.setText(String.valueOf(pessoas.get(position).getCodigo()));

        //SETANDO O NOME NO CAMPO DA NOSSA VIEW
        textViewNome.setText(pessoas.get(position).getNome());


        //SETANDO O SEXO NO CAMPO DA NOSSA VIEW
        if(pessoas.get(position).getSexo().toUpperCase().equals("M"))
            textViewSexo.setText("Masculino");
        else
            textViewSexo.setText("Feminino");


        //CRIANDO EVENTO CLICK PARA O BOTÃO DE EXCLUIR REGISTRO
        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //EXCLUINDO UM REGISTRO
                pessoaRepository.Excluir(pessoas.get(position).getCodigo());

                //MOSTRA A MENSAGEM APÓS EXCLUIR UM REGISTRO
                Toast.makeText(consultarActivity, "Registro excluido com sucesso!", Toast.LENGTH_LONG).show();

                //CHAMA O MÉTODO QUE ATUALIZA A LISTA COM OS REGISTROS QUE AINDA ESTÃO NA BASE
                AtualizarLista();

            }
        });

        //CRIANDO EVENTO CLICK PARA O BOTÃO QUE VAI REDIRECIONAR PARA A TELA DE EDIÇÃO
        // DO REGISTRO.
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intentRedirecionar = new Intent(consultarActivity, EditarActivity.class);

                intentRedirecionar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intentRedirecionar.putExtra("id_pessoa",pessoas.get(position).getCodigo());

                consultarActivity.startActivity(intentRedirecionar);

                consultarActivity.finish();


            }
        });


        return viewLinhaLista;
    }

    //MÉTODO QUE RETORNA A DESCRIÇÃO DO ESTADO CIVIL POR CÓDIGO
    public String GetEstadoCivil(String codigoEstadoCivil){


        if(codigoEstadoCivil.equals("S"))
            return "Solteiro(a)";
        else if(codigoEstadoCivil.equals("C"))
            return "Casado(a)";
        else if(codigoEstadoCivil.equals("V"))
            return "Viuvo(a)";
        else
            return "Divorciado(a)";

    }

    //ATUALIZA A LISTTA DEPOIS DE EXCLUIR UM REGISTRO
    public void AtualizarLista(){

        this.pessoas.clear();
        //this.pessoas= pessoaRepository.SelecionarTodos();
        this.notifyDataSetChanged();
    }

}