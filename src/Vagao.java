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
        
	private void ExecutaViagem() throws InterruptedException
	{   
            move_trem1 = new PathTransition();
            move_trem1.setNode(ancVagao);
            move_trem1.setDuration( Duration.seconds( TempoViagem/2 ) );
            
            move_trem2 = new PathTransition();
            move_trem2.setNode(ancVagao);
            move_trem2.setDuration( Duration.seconds( TempoViagem/2 ) );
            
            Polyline line = new Polyline(
                xInicial-30, yInicial-30,
                xInicial + 500, yInicial-30
            );
            Polyline line2 = new Polyline(
                xInicial-500, yInicial-30,
                xInicial-30, yInicial-30
            );
            this.move_trem1.setPath(line);
            this.move_trem2.setPath(line2);
            SemLog.acquire();
            logMensagem("Vagão partindo.");
            SemLog.release();
            

            this.move_trem1.play();
            
            while(move_trem1.getStatus() == Animation.Status.RUNNING){
                //código qualquer cpu bound
                System.out.println(move_trem1.getStatus());
            }
            
            this.move_trem2.play();
            
            while(move_trem2.getStatus() == Animation.Status.RUNNING){
                //código qualquer cpu bound
                System.out.println(move_trem2.getStatus());
            }
            
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
