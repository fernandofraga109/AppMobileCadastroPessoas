package fernandofraga.com.br.apppessoa.Uteis;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import fernandofraga.com.br.apppessoa.model.Greeting;
import fernandofraga.com.br.apppessoa.model.Pessoa;
import fernandofraga.com.br.apppessoa.model.Pessoas;


/**
 * Created by fernandofraga on 03/08/16.
 */
public class HttpRequestTask extends AsyncTask<Void, Void, Pessoas> {
  /*  @Override
    protected Greeting doInBackground(Void... params) {

        try {
            final String url = "http://rest-service.guides.spring.io/greeting";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Greeting greeting = restTemplate.getForObject(url, Greeting.class);
            System.out.println(greeting.getContent());
            return greeting;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }*/
    @Override
    protected Pessoas doInBackground(Void... params) {

        try {
            final String url = "http://192.168.1.126:8081/WebServiceRest/rest/service/todasPessoas";
            final String urlinsert = "http://192.168.1.126:8081/WebServiceRest/rest/service/cadastrar";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Pessoas pessoas = restTemplate.getForObject(url, Pessoas.class);
            Pessoa pessoa = new Pessoa();
            pessoa.setCodigo(9);
            pessoa.setNome("Testes Android");
            pessoa.setSexo("M");
            restTemplate.postForLocation(urlinsert, pessoa);
            System.out.println(pessoas.get(0).getNome());
            return pessoas;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Pessoas greeting) {
      /*  TextView greetingIdText = (TextView) findViewById(R.id.id_value);
        TextView greetingContentText = (TextView) findViewById(R.id.content_value);
        greetingIdText.setText(greeting.getId());
        greetingContentText.setText(greeting.getContent());
        */
    }

}