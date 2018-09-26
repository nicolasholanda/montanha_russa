import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class TelaEntradaController implements Initializable {
    Parque parque = new Parque();
    
    @FXML
    private Pane ancPrincipal;
    
    @FXML
    private Pane ancMapa;
    
    @FXML
    public Pane ancVagao;
    
    @FXML
    private TextArea txtLog;
    
    @FXML
    private TextField txtTempoEmbarque;
    
    @FXML
    private TextField txtTempoDesembarque;
    
    @FXML
    private TextField txtTempoViagem;
    
    @FXML
    private TextField txtCapacidade;
    
    @FXML
    private ImageView imgParque;
    
    @FXML
    private ImageView imgLeao1;
    
    @FXML
    private ImageView imgLeao2;

    @FXML
    private ImageView imgLeao3;

    @FXML
    private ImageView imgLeao4;

    @FXML
    private ImageView imgLeao5;
    
    @FXML
    private ImageView imgLeao6;
    
    @FXML
    private ImageView imgLeao7;
    
    @FXML
    private ImageView imgLeao8;
    
    @FXML
    private ImageView imgLeao9;
    
    @FXML
    private ImageView imgLeao10;
    
    @FXML
    private ImageView imgVagao1;
    
    @FXML
    private ImageView imgVagao2;
    
    @FXML
    private ImageView imgVagao3;
    
    @FXML
    private ImageView imgVagao4;
    
    @FXML
    private ImageView imgVagao5;
    
    @FXML
    private Button btAddPassageiro;
    
    @FXML
    private Button btAddVagao;
    
    @FXML
    private Button btMatarVagao;
    
    @FXML
    private Button btMatarPassageiro;
    
    @FXML
    private TextField txtId;
    
    private final ImageView imgs_vagao[] = new ImageView[5];
    private final ImageView imgs_passageiros[] = new ImageView[10];
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgs_vagao[0] = imgVagao1;
        imgs_vagao[1] = imgVagao2;
        imgs_vagao[2] = imgVagao3;
        imgs_vagao[3] = imgVagao4;
        imgs_vagao[4] = imgVagao5;
        
        imgs_passageiros[0]=imgLeao1;
        imgs_passageiros[1]=imgLeao2;
        imgs_passageiros[2]=imgLeao3;
        imgs_passageiros[3]=imgLeao4;
        imgs_passageiros[4]=imgLeao5;
        imgs_passageiros[5]=imgLeao6;
        imgs_passageiros[6]=imgLeao7;
        imgs_passageiros[7]=imgLeao8;
        imgs_passageiros[8]=imgLeao9;
        imgs_passageiros[9]=imgLeao10;
        
        for(int i=0; i<10; i++){
            parque.passageiros[i]=null;
        }
    }
    
    
    @FXML
    private void matarPassageiro(){
        int id = Integer.parseInt(txtId.getText());
        if( parque.passageiros[id] != null){
            if(!parque.passageiros[id].podeApagar){
                logMensagem("Não pode apagar agora.");
            }
            else{
                parque.qtd_passageiros--;
                parque.passageiros[id].vivo = false;
                parque.passageiros[id] = null;
            }
        }
        else{
            logMensagem("Insira um ID válido");
        }
    }
    
    @FXML
    private void matarVagao(){
        if(parque.vagao!=null){
            if(parque.vagao.EmViagem==0 && parque.qtd_passageiros==0){
                parque.vagao.vivo=false;
                parque.SemVagao.release();
                parque.vagao = null;
                
                btAddVagao.setDisable(false);
            }
            else{
                logMensagem("Todas as pessoas têm que ser excluídas");
            }
        }
        else{
            logMensagem("Não existe vagão");
        }
    }
    
    @FXML
    private void addPassageiro(){
        if( parque.vagao != null && parque.SemPassageiro!=null ){
            try{
                parque.qtd_passageiros++;
                //String nome = Integer.toString(parque.qtd_passageiros);
                int i=1;
                while(parque.passageiros[i]!=null){
                    i++;
                }
                String nome = Integer.toString(i);
                int tempo_embarque = Integer.parseInt( txtTempoEmbarque.getText() );
                int tempo_desembarque = Integer.parseInt( txtTempoDesembarque.getText() );
                if( tempo_desembarque<=0 || tempo_embarque<=0 ){
                    throw new NumberFormatException();
                }
                parque.cria_passageiro(nome, tempo_embarque, tempo_desembarque, txtLog, imgs_passageiros[i-1], ancVagao, ancMapa);
                if(parque.qtd_passageiros == 9){
                    btAddPassageiro.setDisable(true);
                }
            }
            catch(NumberFormatException e){
                logMensagem("Valores inválidos para passageiro");
            }
        }
        else{
            logMensagem("Instancie um vagão primeiro.");
        }
    }
    
    
    @FXML
    private void addVagao(){
        try{
            int cap_max = Integer.parseInt(txtCapacidade.getText());
            int tempo = Integer.parseInt(txtTempoViagem.getText());
            if( !(cap_max>=1 && cap_max<=5) || tempo<=0 ){
                throw new NumberFormatException();
            }
            logMensagem("O vagão foi instanciado.");
            ancVagao.getChildren().add( imgs_vagao[cap_max-1] );
            imgs_vagao[cap_max-1].setLayoutX(220.0 - (cap_max-1)*48);
            imgs_vagao[cap_max-1].setLayoutY(10.0);
            imgs_vagao[cap_max-1].setVisible(true);
            parque.cria_vagao(cap_max, tempo, ancVagao, txtLog, ancMapa);
            btAddPassageiro.setDisable(false);
            txtTempoEmbarque.setDisable(false);
            txtTempoDesembarque.setDisable(false);
            btAddVagao.setDisable(true);
        }
        catch(NumberFormatException e){
            logMensagem("Valores inválidos para vagão.");
        }
    }
    
    public void logMensagem(String msg){
        Platform.runLater(() -> {
            txtLog.appendText("\n["+LocalDateTime.now().getHour()+":" + LocalDateTime.now().getMinute()+":"
                    + LocalDateTime.now().getSecond()+"] - " + msg);
        });
    }    
    
}
