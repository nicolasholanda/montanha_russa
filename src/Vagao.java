import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
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
        boolean vivo=true;
        Pane ancMapa;
	
	public Vagao(int Capacidade, int TempoViagem, Semaphore Vagao, Semaphore Mutex, Pane ancVagao, Semaphore SemLog, TextArea txtLog, Pane ancMapa) 
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
                this.ancMapa = ancMapa;
	}
        
        private void logMensagem(String msg){
            Platform.runLater(() -> {
            txtLog.appendText("\n["+LocalDateTime.now().getHour()+":" + LocalDateTime.now().getMinute()+":"
                    + LocalDateTime.now().getSecond()+"] - " + msg);
            });
        }
        
	private static void mySleep(int tempo){
            long tempoInicial = System.currentTimeMillis();
            long tempofinal=tempoInicial+tempo;
            while(System.currentTimeMillis()<tempofinal){
                
            }
        }
        
	private void ExecutaViagem() throws InterruptedException
	{   
            ancVagao.setLayoutX(xInicial);
            long tempoFinal=System.currentTimeMillis()+(TempoViagem/2)*1000;
            EmViagem = 1;
            while(System.currentTimeMillis()<tempoFinal){
                Platform.runLater(()->{
                    ancVagao.setLayoutX( ancVagao.getLayoutX() + 1 );
                });
                mySleep(TempoViagem);
            }
            
            ancVagao.setLayoutX(xInicial-500);
            tempoFinal=System.currentTimeMillis()+(TempoViagem/2)*1000;
            while(System.currentTimeMillis()<tempoFinal){
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
		while(vivo)
		{
			try
			{
				SemLog.acquire();
                                logMensagem("Vagão está dormindo.");
                                SemLog.release();
                                
				this.SemaforoVagao.acquire();
                                if(!vivo){
                                    break;
                                }
                                SemLog.acquire();
                                logMensagem("Vagão partindo.");
                                SemLog.release();
                                EmViagem=1;
                                this.ExecutaViagem();
                                ancVagao.setLayoutX(xInicial);
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
                Platform.runLater(()->{
                    Node img = ancVagao.getChildren().get(0);
                    img.setVisible(false);
                    ancMapa.getChildren().add(img);
                    ancVagao.getChildren().clear();
                });
	}

}
