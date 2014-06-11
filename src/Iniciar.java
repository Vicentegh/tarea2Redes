import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;


class Iniciar extends Thread{

	Iniciar(Socket socket){
		setPriority(NORM_PRIORITY - 1);
   	}
	
	public void run(){
		try	{
			String linea = "default", method;
			StringTokenizer token = new StringTokenizer(linea);
			if(token.countTokens()>=2)	{
				method = token.nextToken();						
				if(method.equals("POST"))
					goTCP(token.nextToken());
				else{
					System.out.println("Error");
				}
			}	
		}
		catch(Exception e){
			System.out.println("Error en el server\n" + e.toString());
		}
	}
	
	private void goTCP(String query) throws Exception {		
		InetAddress iA = InetAddress.getLocalHost();
		String addres = iA.getHostAddress();		
		query = query.concat("+" + addres);		
		Cliente.envio(query,0);
		actualizar();		
	}
	
	private void actualizar() throws Exception {
		InetAddress iA = InetAddress.getLocalHost();
		String addres = iA.getHostAddress();
		Cliente.envio(addres,1);
	}
}