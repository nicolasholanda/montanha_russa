import java.util.concurrent.Semaphore;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Pane;
public class Parque {
    int qtd_passageiros = 0;
    Passageiro passageiros[] = new Passageiro[10];
    Semaphore Mutex = new Semaphore(1,true);
    Semaphore SemPassageiro;
    Semaphore SemVagao = new Semaphore(0,true);
    Semaphore SemLog = new Semaphore(1);
    Vagao vagao;
    
    
    public void cria_vagao(int cap_max, int tempo_viagem, Pane ancVagao, TextArea txtLog){
        this.vagao = new Vagao(cap_max, tempo_viagem, SemVagao, Mutex, ancVagao, SemLog, txtLog);
        this.SemPassageiro = new Semaphore(cap_max,true);
        vagao.start();
    }
    
    public void cria_passageiro(String nome, int tempo_embarque, int tempo_desembarque, TextArea txtLog, ImageView imgPassageiro, Pane ancVagao, Pane ancMapa){
        Passageiro p = new Passageiro(nome, tempo_embarque,tempo_desembarque, vagao, SemPassageiro, Mutex, SemVagao, SemLog, txtLog, imgPassageiro, ancVagao, ancMapa);
        this.passageiros[qtd_passageiros] = p;
        this.passageiros[qtd_passageiros].start();
    }
}
