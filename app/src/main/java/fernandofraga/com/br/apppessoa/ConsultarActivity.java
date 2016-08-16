package fernandofraga.com.br.apppessoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import fernandofraga.com.br.apppessoa.Uteis.LinhaConsultarAdapter;
import fernandofraga.com.br.apppessoa.Uteis.LinhaPesssoaConsultarWSAdapter;
import fernandofraga.com.br.apppessoa.Uteis.Uteis;
import fernandofraga.com.br.apppessoa.model.PessoaModel;
import fernandofraga.com.br.apppessoa.model.Pessoas;
import fernandofraga.com.br.apppessoa.repository.PessoaRepository;

public class ConsultarActivity extends AppCompatActivity {

    //CRIANDO UM OBJETO DO TIPO ListView PARA RECEBER OS REGISTROS DE UM ADAPTER
    ListView listViewPessoas;

    //CRIANDO O BOTÃO VOLTAR PARA RETORNAR PARA A TELA COM AS OPÇÕES
    Button buttonVoltar;

    ProgressDialog prgDialog;

    Pessoas pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        prgDialog = new ProgressDialog(this);
        // SETA MENSAGEM
        prgDialog.setMessage("Aguarde...");
        // SETA PARA QUE NAO SEJA POSSIVEL CANCELAR
        prgDialog.setCancelable(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);

        //VINCULANDO O LISTVIEW DA TELA AO OBJETO CRIADO
        listViewPessoas = (ListView)this.findViewById(R.id.listViewPessoas);

        //VINCULANDO O BOTÃO VOLTAR DA TELA AO OBJETO CRIADO
        buttonVoltar    = (Button)this.findViewById(R.id.buttonVoltar);


        //CHAMA O MÉTODO QUE CARREGA AS PESSOAS CADASTRADAS NA BASE DE DADOS
        this.CarregarPessoasCadastradas();

        //CHAMA O MÉTODO QUE CRIA O EVENTO PARA O BOTÃO VOLTAR
        this.CriarEvento();





    }

    //MÉTODO QUE CRIA EVENTO PARA O BOTÃO VOLTAR
    protected  void CriarEvento(){

        buttonVoltar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //REDIRECIONA PARA A TELA PRINCIPAL
                Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMainActivity);

                //FINALIZA A ATIVIDADE ATUAL
                finish();
            }
        });
    }

    //MÉTODO QUE CONSULTA AS PESSOAS CADASTRADAS
    protected  void CarregarPessoasCadastradas(){

        PessoaRepository pessoaRepository =  new PessoaRepository(this);

        //BUSCA AS PESSOAS CADASTRADAS
        List<PessoaModel> pessoas = pessoaRepository.SelecionarTodos();

        //SETA O ADAPTER DA LISTA COM OS REGISTROS RETORNADOS DA BASE
       // listViewPessoas.setAdapter(new LinhaConsultarAdapter(this, pessoas));

        prgDialog.show();
        ConsultarTodasPessoasTask consultarTodasPessoasTask = new ConsultarTodasPessoasTask();
        consultarTodasPessoasTask.execute();
    }


    public class ConsultarTodasPessoasTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            RestTemplate clientRest = new RestTemplate();
            clientRest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            setPessoas(clientRest.getForObject(Uteis.UrlWebServices() + "todasPessoas", Pessoas.class));
            getPessoas().get(0).getNome();
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            //SETA O ADAPTER DA LISTA COM OS REGISTROS RETORNADOS DA BASE
            listViewPessoas.setAdapter(new LinhaPesssoaConsultarWSAdapter(getContext(), getPessoas()));
            prgDialog.hide();
            super.onPostExecute(strings);
        }
    }

    public ProgressDialog getPrgDialog() {
        return this.prgDialog;
    }
    public ConsultarActivity getContext() {
        return this;
    }

    public Pessoas getPessoas() {
        return pessoas;
    }

    public void setPessoas(Pessoas pessoas) {
        this.pessoas = pessoas;
    }

}