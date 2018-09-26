import java.util.concurrent.Semaphore;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
public class Parque {
    int qtd_passageiros = 0;
    Passageiro passageiros[] = new Passageiro[10];
    Semaphore Mutex = new Semaphore(1,true);
    Semaphore SemPassageiro;
    Semaphore SemVagao = new Semaphore(0,true);
    Semaphore SemLog = new Semaphore(1);
    Vagao vagao;
    
    
    
    public static void mySleep(int tempo){
        long tempoInicial = System.currentTimeMillis(), tempoMedido=0, tempoAtual=0;
        while(tempoAtual < tempo){
            while(tempoAtual == tempoMedido){
                tempoMedido=(System.currentTimeMillis()-tempoInicial);
            }
            tempoAtual = tempoMedido;
        }
    }
    
    public void cria_vagao(int cap_max, int tempo_viagem, Pane ancVagao, TextArea txtLog, Pane ancMapa){
        this.vagao = new Vagao(cap_max, tempo_viagem, SemVagao, Mutex, ancVagao, SemLog, txtLog, ancMapa);
        this.SemPassageiro = new Semaphore(cap_max,true);
        vagao.start();
    }
    
    public void cria_passageiro(String nome, int tempo_embarque, int tempo_desembarque, TextArea txtLog, ImageView imgPassageiro, Pane ancVagao, Pane ancMapa){
        Passageiro p = new Passageiro(nome, tempo_embarque,tempo_desembarque, vagao, SemPassageiro, Mutex, SemVagao, SemLog, txtLog, imgPassageiro, ancVagao, ancMapa);
        this.passageiros[Integer.parseInt(nome)] = p;
        this.passageiros[Integer.parseInt(nome)].start();
    }
    
    
}
