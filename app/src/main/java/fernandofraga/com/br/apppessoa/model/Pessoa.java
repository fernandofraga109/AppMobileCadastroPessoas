package fernandofraga.com.br.apppessoa.model;

/**
 * Created by fernandofraga on 04/08/16.
 */
public class Pessoa {

    private int codigo;
    private String nome;
    private String sexo;

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSexo(){
        return sexo;
    }
    public void setSexo(String sexo){
        this.sexo =  sexo;
    }
}