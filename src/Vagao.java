import java.time.LocalDateTime;
import javafx.util.Duration;
import java.util.concurrent.Semaphore;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
public class Vagao extends Thread
{
	public Semaphore SemaforoVagao;
	public Semaphore SemaforoMutex;
        public Semaphore SemLog;
	public int Capacidade;
	public int TempoViagem;
	public int EmViagem;
	public int CadeirasOcupadas;
        public Pane ancVagao;
        public TextArea txtLog;
        PathTransition move_trem1;
        PathTransition move_trem2;
        double xInicial;
        double yInicial;
	
	public Vagao(int Capacidade, int TempoViagem, Semaphore Vagao, Semaphore Mutex, Pane ancVagao, Semaphore SemLog, TextArea txtLog) 
	{
		// TODO Auto-generated constructor stub
		this.Capacidade=Capacidade;
		this.CadeirasOcupadas=0;
		this.TempoViagem=TempoViagem;
		this.SemaforoVagao=Vagao;
		this.SemaforoMutex=Mutex;
		this.EmViagem=0;
                this.ancVagao = ancVagao;
                this.xInicial = ancVagao.getLayoutX();
                this.yInicial = ancVagao.getLayoutY();
                this.SemLog = SemLog;
                this.txtLog = txtLog;
	}
        
        private void logMensagem(String msg){
            Platform.runLater(() -> {
            txtLog.appendText("\n["+LocalDateTime.now().getHour()+":" + LocalDateTime.now().getMinute()+":"
                    + LocalDateTime.now().getSecond()+"] - " + msg);
            });
        }
        
	private static void mySleep(int tempo){
            long tempoInicial = System.currentTimeMillis(), tempoMedido=0, tempoAtual=0;
            while(tempoAtual < tempo){
                while(tempoAtual == tempoMedido){
                    tempoMedido=(System.currentTimeMillis()-tempoInicial);
                }
                tempoAtual = tempoMedido;
            }
        }
        
	private void ExecutaViagem() throws InterruptedException
	{   
            EmViagem = 1;
            while(ancVagao.getLayoutX() != xInicial+500 && EmViagem==1){
                Platform.runLater(()->{
                    ancVagao.setLayoutX( ancVagao.getLayoutX() + 1 );
                });
                mySleep(TempoViagem);
            }

            ancVagao.setLayoutX(xInicial-500);
            
            while(ancVagao.getLayoutX()!=xInicial){
                Platform.runLater(()->{
                    ancVagao.setLayoutX( ancVagao.getLayoutX() + 1 );
                });
                mySleep(TempoViagem);
            }
            ancVagao.setLayoutX(xInicial);
            EmViagem=0;
        }
	
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				SemLog.acquire();
                                logMensagem("Vagão está dormindo.");
                                SemLog.release();
                                
				this.SemaforoVagao.acquire();
                                
                                SemLog.acquire();
                                logMensagem("Vagão partindo.");
                                SemLog.release();
                                EmViagem=1;
                                this.ExecutaViagem();
                                EmViagem=0;
                                SemLog.acquire();
                                logMensagem("Vagão chegou.");
                                SemLog.release();
				
			}
			catch(InterruptedException exc)
			{
				System.out.println(exc);
			}
		}
	}

}
