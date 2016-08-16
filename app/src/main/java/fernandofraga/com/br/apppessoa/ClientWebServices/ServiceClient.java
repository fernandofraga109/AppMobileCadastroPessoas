package fernandofraga.com.br.apppessoa.ClientWebServices;

import android.content.Context;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import fernandofraga.com.br.apppessoa.CadastrarActivity;
import fernandofraga.com.br.apppessoa.R;
import fernandofraga.com.br.apppessoa.Uteis.Uteis;
import fernandofraga.com.br.apppessoa.model.Pessoa;
import fernandofraga.com.br.apppessoa.model.Pessoas;


public class ServiceClient implements Runnable{

    /**
     * GERENCIA A INFRAESTRUTURA DE COMUNIÇÃO DO LADO
     * CLIENTE PARA EXECUTAR AS SOLICITAÇÕES REALIZADAS
     */
    private final RestTemplate clientRest;
    private Pessoas pessoas;
    private Pessoa pessoa;
    private Thread thread;
    private String acao = "";
    private String retornoString;
    private CadastrarActivity context;

    /**
     * ACESSA UM RECURSO IDENTIFICADO PELO URI(Uniform Resource Identifier/Identificador Uniforme de Recursos)
     */
    private WebTarget webTarget;

    /**
     * URL DO SERVIÇO REST QUE VAMOS ACESSAR
     */
    private final String URL_SERVICE = "http://192.168.1.126:8081/WebServiceRest/rest/service/";


    /**
     * CONSTRUTOR DA NOSSA CLASSE
     */
    public ServiceClient(CadastrarActivity context) {
        this.clientRest = new RestTemplate();
        this.context = context;
    }

    @Override
    public void run() {
        RestTemplate clientRest = new RestTemplate();
        clientRest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        switch (this.acao) {
            case "cadastrar" :
                clientRest.postForLocation(URL_SERVICE + this.acao, this.pessoa);
                //this.retornoString = "Pessoa cadastrada com sucesso!";
                context.getPrgDialog().hide();
                break;
            case "alterar" :
                clientRest.put(URL_SERVICE + this.acao, this.pessoa);
                this.retornoString = "Pessoa alterada com sucesso!";
                break;
        }

    }

    /**
     * CADASTRA UMA NOVA PESSOA ATRAVÉS DA OPERAÇÃO cadastrar(MÉTODO HTTP: POST)
     */
    public String CadastrarPessoa(Pessoa pessoa) {
        this.acao = "cadastrar";
        this.pessoa = pessoa;
        thread = new Thread(this);
        thread.start();
        return this.retornoString;
    }

    /**
     * ALTERA UM REGISTRO JÁ CADASTRADO ATRAVÉS DA OPERAÇÃO alterar(MÉTODO HTTP:PUT)
     */
    public String AlterarPessoa(Pessoa pessoa) {
        this.acao = "alterar";
        this.pessoa = pessoa;
        thread = new Thread(this);
        thread.start();
        return this.retornoString;
    }

    /**
     * CONSULTA TODAS AS PESSOAS CADASTRADAS NO SERVIÇO ATRAVÉS DA OPERAÇÃO todasPessoas(MÉTODO HTTP:GET)
     */
    public Pessoas ConsultarTodasPessoas() {
        try {
            Pessoas pessoas = this.clientRest.getForObject(URL_SERVICE + "todasPessoas", Pessoas.class);
            return pessoas;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * CONSULTA UMA PESSOA PELO CÓDIGO ATRAVÉS DA OPERAÇÃO getPessoa(MÉTODO HTTP: GET)
     */
    public Pessoa ConsultarPessoaPorCodigo(int codigo) {
      try {
            Pessoa pessoa = this.clientRest.getForObject(URL_SERVICE + "getPessoa/"+codigo, Pessoa.class);
            return pessoa;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * EXCLUI UM REGISTRO CADASTRADO PELO CÓDIGO ATRAVÉS DA OPERAÇÃO excluir(MÉTODO HTTP:delete)
     */
    public String ExcluirPessoaPorCodigo(int codigo) {
        try {
            this.clientRest.delete(URL_SERVICE + "excluir/"+codigo);
        } catch (Exception e) {
            return null;
        }
        return "Excluido com sucesso!";
    }


}
