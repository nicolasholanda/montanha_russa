import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
public class Passageiro extends Thread {
	Vagao vagao;
	int TempoEmbarque;
	int TempoDesembarque;
        int emAnimacao=0;
	Semaphore SemaforoPassageiro;
	Semaphore SemaforoMutex;
	Semaphore SemaforoVagao;
        Semaphore SemLog;
	String nome;
        TextArea txtLog;
        ImageView img;
        Pane ancVagao;
        Pane ancMapa;
        int banco;
        boolean podeApagar = true;
        boolean vivo=true;
        
	
	public Passageiro(String nome, int TempoEmbarque, int TempoDesembarque,
                Vagao vagao, Semaphore SemPassageiro, Semaphore Mutex, 
                Semaphore SemVagao, Semaphore SemLog, TextArea txtLog, ImageView imgPassageiro, 
                Pane ancVagao, Pane ancMapa) 
	{
		this.vagao=vagao;
		this.TempoEmbarque=TempoEmbarque;
		this.TempoDesembarque=TempoDesembarque;
		this.SemaforoPassageiro=SemPassageiro;
		this.SemaforoMutex=Mutex;
		this.SemaforoVagao=SemVagao;
		this.nome=nome;
                this.txtLog = txtLog;
                this.SemLog = SemLog;
                this.img = imgPassageiro;
                this.ancVagao = ancVagao;
                this.ancMapa = ancMapa;
                this.banco = banco;
	}
        
        private void logMensagem(String msg){
            Platform.runLater(() -> {
            txtLog.appendText("\n["+LocalDateTime.now().getHour()+":" + LocalDateTime.now().getMinute()+":"
                + LocalDateTime.now().getSecond()+"] - " + msg);
            });
        }
        
        private void fila_para_embarque(){
            int dif, aux, dif2;
            
            if(Integer.parseInt(nome) <= 4 ){
                aux = Integer.parseInt(nome);
                dif = -40;
                dif2 = 5 - Integer.parseInt(nome);
            }
            
            else{
                aux = Integer.parseInt(nome)-4; 
                dif = 75;
                dif2 = 9-Integer.parseInt(nome);
            }
            
            PathTransition entra_fila = new PathTransition();
            entra_fila.setNode(img);
            entra_fila.setDuration( Duration.seconds( 2 ) );
            
            Polyline line = new Polyline(
                60+42*aux, -210+dif,
                60+42*aux, -210,
                60+42*(aux+dif2), -210,
                60+42*(aux+dif2), -210-42*3
            );
            entra_fila.setPath(line);
            entra_fila.play();
            while(entra_fila.getStatus() == Animation.Status.RUNNING){
                System.out.println(entra_fila.getStatus());
            }
        }
        
        private void embarque_para_vagao(){
            int dif, aux, dif2;
            this.banco = vagao.CadeirasOcupadas;
            if(Integer.parseInt(nome) <= 4 ){
                aux = Integer.parseInt(nome);
                dif = -40;
                dif2 = 5 - Integer.parseInt(nome);
            }
            
            else{
                aux = Integer.parseInt(nome)-4; 
                dif = 75;
                dif2 = 9-Integer.parseInt(nome);
            }
            
            PathTransition entra_fila = new PathTransition();
            entra_fila.setNode(img);
            entra_fila.setDuration( Duration.seconds( TempoEmbarque ) );
            
            Polyline line = new Polyline(
                60+42*(aux+dif2), -210-42*3,
                60+42*(aux+dif2-banco)-banco*3, -210-42*3,
                60+42*(aux+dif2-banco)-banco*3, -210-42*4
            );
            entra_fila.setPath(line);
            entra_fila.play();
            while(entra_fila.getStatus() == Animation.Status.RUNNING){
                System.out.println(entra_fila.getStatus());
            }
            
            Platform.runLater(() -> {
                ancVagao.getChildren().add(img);
                img.setX(-188);
                img.setY(-70);
            });
        }
        
        private void entrar_na_fila(){
            int dif, aux;
            
            if(Integer.parseInt(nome) <= 4 ){
                aux = Integer.parseInt(nome);
                dif = -40;
            }
            
            else{
                aux = Integer.parseInt(nome)-4; 
                dif = 75;
            }
            
            PathTransition entra_fila = new PathTransition();
            entra_fila.setNode(img);
            entra_fila.setDuration( Duration.seconds( 1 ) );
            entra_fila.setCycleCount(0);
            Polyline line = new Polyline(
                60, -210,
                60+42*aux, -210,
                60+42*aux, -210+dif
            );
            entra_fila.setPath(line);
            entra_fila.play();
            while(entra_fila.getStatus() == Animation.Status.RUNNING){
                System.out.println(entra_fila.getStatus());
            }
        }
        
        private void entrada_p_fila(){
            double xInicial = img.getX()+10;
            double yInicial = img.getY()+10;
            PathTransition fila_inicio = new PathTransition();
            fila_inicio.setNode(img);
            fila_inicio.setDuration( Duration.seconds( 1 ) );
            Polyline line = new Polyline(
                20, 0,
                20, -210,
                60, -210
            );
            fila_inicio.setPath(line);
            fila_inicio.play();
            while(fila_inicio.getStatus() == Animation.Status.RUNNING){
                System.out.println(fila_inicio.getStatus());
            }
            entrar_na_fila();
        }
	
	private void Embarca()
	{
            fila_para_embarque();
            embarque_para_vagao();
            Platform.runLater(()->{
                ancMapa.getChildren().remove(img);
            });
	}
	
	private void Desembarque()
	{
            ancVagao.setLayoutX(186);
            Platform.runLater(()->{
                ancVagao.getChildren().remove(img);
                ancMapa.getChildren().add(img);
            });
            int dif, aux, dif2;
            
            if(Integer.parseInt(nome) <= 4 ){
                aux = Integer.parseInt(nome);
                dif = -40;
                dif2 = 5 - Integer.parseInt(nome);
            }
            
            else{
                aux = Integer.parseInt(nome)-4; 
                dif = 75;
                dif2 = 9-Integer.parseInt(nome);
            }
            
            PathTransition entra_fila = new PathTransition();
            entra_fila.setNode(img);
            entra_fila.setDuration( Duration.seconds( TempoDesembarque ) );
            
            Polyline line = new Polyline(
                60+42*(aux+dif2-banco)-banco*3, -210-42*4,
                60+42*(aux+dif2-banco)-banco*3, -210-42*3,
                20, -210-42*3,
                20, -210,
                60, -210
            );
            entra_fila.setPath(line);
            entra_fila.play();
            while(entra_fila.getStatus() == Animation.Status.RUNNING){
                System.out.println(entra_fila.getStatus());
            }
            img.setX(0);
            img.setY(0);
            img.setScaleX(1);
            img.setScaleY(1);
            entrar_na_fila();
	}

	private void CurtirViagem() throws InterruptedException
	{
            while( vagao.EmViagem == 1){ 
                Platform.runLater(()->{
                    img.setScaleX(0.8);
                    img.setScaleY(0.8);
                });
                Parque.mySleep(100);
                Platform.runLater(()->{
                    img.setScaleX(1);
                    img.setScaleY(1);
                });
                Parque.mySleep(100);
            }
	}
	
	
	@Override
	public void run()
	{       
                podeApagar=true;
                img.setVisible(true);
		entrada_p_fila();
                System.out.println("pog");

		while(this.vivo)
		{
			try
			{
                                SemLog.acquire();
                                logMensagem(this.nome+" está na fila");
                                SemLog.release();
				this.SemaforoPassageiro.acquire();
				this.SemaforoMutex.acquire();
                                if(!vivo){
                                    this.SemaforoPassageiro.release();
                                    this.SemaforoMutex.release();
                                    break;
                                }
                                podeApagar=false;
				this.Embarca();
                                this.vagao.CadeirasOcupadas++;
                                SemLog.acquire();
                                logMensagem(this.nome+" embarcou.");
                                SemLog.release();
                                
				if(this.vagao.CadeirasOcupadas<this.vagao.Capacidade)
				{
					this.SemaforoMutex.release();
					SemLog.acquire();
                                        logMensagem(this.nome+" aguarda no vagão.");
                                        SemLog.release();
					this.SemaforoVagao.acquire();
				}
				else
				{
					this.SemaforoMutex.release();
                                        SemLog.acquire();
                                        logMensagem(this.nome+" acordou o vagão.");
                                        SemLog.release();
					this.SemaforoVagao.release(this.vagao.Capacidade);
				}
                                SemLog.acquire();
                                logMensagem(this.nome+" curtindo a viagem");
                                SemLog.release();
                                vagao.EmViagem=1;
                                this.CurtirViagem();
                                vagao.EmViagem=0;
				this.SemaforoMutex.acquire();
				this.vagao.CadeirasOcupadas--;
                                podeApagar=true;
				this.Desembarque();
                                SemLog.acquire();
                                logMensagem(this.nome+" desembarcou.");
                                SemLog.release();
				if(this.vagao.CadeirasOcupadas==0)
				{
					this.SemaforoPassageiro.release(this.vagao.Capacidade);
				}
				this.SemaforoMutex.release();
			}
			catch(InterruptedException exc)
			{
				System.out.println(exc);
			}
			
		}
                Platform.runLater(()->{
                    img.setVisible(false);
                    img.setLayoutX(167);
                    img.setLayoutY(471);
                });
                logMensagem(nome+" foi excluída.");
	}
}
