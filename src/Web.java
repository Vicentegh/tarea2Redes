import java.net.ServerSocket;
import java.net.Socket;


public class Web{
		int puerto = 80;	//PUERTO MODIFICABLE, por defecto por lo que lei en una pagina use el 80
	
		public Web(String[] array) {
			// TODO Auto-generated constructor stub
		}

		public static void main(String[] array){
				Web web = new Web(array);
				web.iniciar();
		}
		
		boolean iniciar(){
			try{
				ServerSocket s = new ServerSocket(puerto);			
				while(true){ 
						Socket entrante = s.accept();		
						Iniciar empezar = new Iniciar(entrante);
						empezar.start();
				}
			}
			catch(Exception e){
					System.out.println("Error en server\n" + e.toString());
			}
			return true;
		}
}
	
	
	
	
