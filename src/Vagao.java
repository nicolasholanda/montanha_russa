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
	public long TempoViagem;
	public int EmViagem;
	public int CadeirasOcupadas;
        public Pane ancVagao;
        public TextArea txtLog;
        PathTransition move_trem1;
        PathTransition move_trem2;
        double xInicial;
        double yInicial;
	
	public Vagao(int Capacidade, long TempoViagem, Semaphore Vagao, Semaphore Mutex, Pane ancVagao, Semaphore SemLog, TextArea txtLog) 
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
                    tempoMedido=(System.currentTimeMillis()-tempoInicial)/10;
                }
                tempoAtual = tempoMedido;
            }
        }
        
	private void ExecutaViagem() throws InterruptedException
	{   
            
            int tempo=1500;
            SemLog.acquire();
            logMensagem("Vagão partindo.");
            SemLog.release();
            EmViagem=1;
            while(tempo !=0){
                if(ancVagao.getLayoutX() != xInicial+500){
                    ancVagao.setLayoutX( ancVagao.getLayoutX() + 1);
                }
                mySleep(1);
                tempo--;
            }
            tempo=1500;
            ancVagao.setLayoutX(xInicial-500);
            
            while(tempo !=0){
                if(ancVagao.getLayoutX() != xInicial){
                    ancVagao.setLayoutX( ancVagao.getLayoutX() + 1 );
                }
                mySleep(1);
                tempo--;
            }
            EmViagem = 0;
            ancVagao.setLayoutX(xInicial);
            SemLog.acquire();
            logMensagem("Vagão chegou.");
            SemLog.release();
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
                                logMensagem("Vagão acordou.");
                                SemLog.release();
                                EmViagem = 1;
                                this.ExecutaViagem();
                                EmViagem = 0;
				
			}
			catch(InterruptedException exc)
			{
				System.out.println(exc);
			}
		}
	}

}
