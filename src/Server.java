import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server implements Runnable{
	static int aux;
	static int socket = 6789;	//MODIFICABLE por si se necesita modificar (recomendaciones de una tutorial D: )
	
	private static String obtenerH() throws IOException {
		String historiales = "", historial = "";
		File arch = new File("HistorialM.txt");
		FileReader leer = new FileReader(arch);
		BufferedReader buffer = new BufferedReader(leer);
		while(historial!= null){
			historial = buffer.readLine();
			if(historial!=null){	
				historiales = historiales.concat(historial + "$");
			}
		}		
		return historiales;
	}
	
	private static void actualizar(String query) throws IOException {
		String ip, mensaje, ipOrigen;
		String[] linea, aux;
		linea = query.split("=");
		aux = linea[1].split("\\+");
		ip = aux[1];
		mensaje = aux[2];
		ipOrigen = aux[3];
		File archivo = new File("HistorialM.txt");
		if(archivo.exists()){
			FileWriter chat = new FileWriter(archivo,true);
			BufferedWriter chat1 = new BufferedWriter(chat);
			PrintWriter impr = new PrintWriter(chat1);		
			impr.append(ip + "!" + "Envio: " +ipOrigen+ "\nMensaje: " +mensaje+ "\nEnviado el: " +obtenerF()+ "\nA las: " +obtenerHora()+")\n");
			impr.close();
			chat1.close();
		}	
	}	

	private static void CorrerS() throws IOException {
		String linea, linea2, operacion, operacion1, historiales;	
		String[] lineas, operacions;
		ServerSocket sS = new ServerSocket(socket);
		while(true){
			Socket conexion = sS.accept();
			BufferedReader entrada;
			DataOutputStream salida;
			entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			salida = new DataOutputStream(conexion.getOutputStream());
			linea = entrada.readLine();
			lineas = linea.split(":");
			operacion = lineas[1];
			operacions = lineas[0].split("=");
			operacion1 = operacions[0];
			historiales = obtenerH();
				if(operacion.equals("1"))
					salida.writeBytes(lineas[0]+ "#" + historiales);
				else 
					if(operacion.equals("0") && (operacion1.equals("message")))
						actualizar(lineas[0]);
			System.out.println("Received: " + lineas[1]);	
			linea2 = linea.toUpperCase() + '\n';    
			salida.writeBytes(linea2);
		}
	}

	private static void CorrerS1() {
		int cont;
		String arch;
		byte[] recivido;
		Socket conecto;
		ServerSocket sS;
		BufferedInputStream bIS;
		BufferedOutputStream bOS;
		try{		 
			sS = new ServerSocket(socket);
			while(true){
				 conecto = sS.accept();
				 recivido = new byte[1024];
				 bIS = new BufferedInputStream(conecto.getInputStream());
				 DataInputStream dis=new DataInputStream(conecto.getInputStream());		 
				 arch = dis.readUTF();
				 arch = arch.substring(arch.indexOf('\\')+1,arch.length());
				 bOS = new BufferedOutputStream(new FileOutputStream(arch));
				 while ((cont = bIS.read(recivido)) != -1)
					 bOS.write(recivido,0,cont);
				 bOS.close();
				 dis.close();
			 }
		}
		catch (Exception e ) {
			 System.err.println(e);
		}
	}

	public static String obtenerF() {
		String fecha;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date date= new Date();
		fecha = formato.format(date);	
	    return fecha;
	}
	
	private static String obtenerHora() {
		String hora;
		SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
		Date date= new Date();
		hora = formato.format(date);	
	    return hora;
	}
	
	public static void main(String argv[]) throws Exception{		
		aux = 0;
		(new Thread(new Server())).start();
		Thread.sleep(2000);
		aux = 1;
		(new Thread(new Server())).start();
	}

	public void run(){
		if(aux==0)
			try {
				CorrerS();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(aux==1)
			CorrerS1();
	}

}