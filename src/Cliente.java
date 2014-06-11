import java.io.*;
import java.net.*;


public class Cliente{
	static String ipAux="192.168.1.152"; //IP MODIFICABLE, esa es la ip que segun un tutorial debia usar (la de mi notebook)
	
	private static void enviarA(String linea){		 
		int cont;
		byte[] arregloB;
		BufferedInputStream bIS;
		BufferedOutputStream bOS;
		String[] direccionAux = linea.split("\\+");
		String direccion = direccionAux[0];		
		direccion = direccion.replace("%3A",":");
		direccion = direccion.replace("%5C","\\");		
		final String nombreD = direccion;		 
		try{
			 final File archivo = new File(nombreD);
			 InetAddress ip = InetAddress.getByName(ipAux);
			 Socket client = new Socket(ip,6788);
			 bIS = new BufferedInputStream(new FileInputStream(archivo));
			 bOS = new BufferedOutputStream(client.getOutputStream());
			 DataOutputStream dos=new DataOutputStream(client.getOutputStream());
			 dos.writeUTF(archivo.getName());
			 arregloB = new byte[8192];
			 while ((cont = bIS.read(arregloB)) != -1)
				 bOS.write(arregloB,0,cont);
		bIS.close();
		bOS.close();		 
		}
		catch ( Exception e ){
			System.err.println(e);
		}
	 }
	 
	 private static void actualizar(String string) throws IOException {
		File html = new File("Chat.html");			
		String[] aux = string.split("#");
		String ipOrigen = aux[0];
		String historial = aux[1];	
		String[] historiales = historial.split("\\$");
		int cont, largo = historiales.length - 1;
		if(html.exists()){	
			FileWriter chat = new FileWriter(html);
			BufferedWriter chat2 = new BufferedWriter(chat);
			PrintWriter chat3 = new PrintWriter(chat2);				
			chat3.append("<html>\n " +
						"head>\n " +
						"<title>Avioncito de Papel</title>\n" +
						"<meta  http-equiv=\"refresh\" content=\"10\" charset=\"UTF-8\">" +
						"body {\n" +
						"</head>\n" +
						"<body>\n" +
						"<h3>Chat</h3>\n" +
						"<form class=\"form-horizontal\" action =\"Chat.html\" method=\"post\">\n" +
						"Nombre:" +
						"<input name=\"message\" type=\"text\"><br>\n" +
						"<button id=\"singlebutton\" type=\"submit\" value=\"Submit\">Enviar</button>\n" +
						"</form>\n" +
						"<form class=\"form-horizontal\" action =\"Chat.html\" method=\"post\">\n" +
						"Direccion del archivo:" +
						"<input name=\"file\" type=\"text\"><br>" +
						"<button id=\"singlebutton\" type=\"submit\" value=\"Submit\">Enviar Archivo</button>" +
						"</form>"); 				
			for(cont=0; cont<=largo-1; cont++){
				String[] mensajes = historiales[cont].split("!");
				String ip = mensajes[0];					
				if(ipOrigen.equals(ip))
					chat3.append("<h3>"+ mensajes[1] +"</h3>\n");				
			}	
			chat3.append("</body>" +
						"</html>");
			chat3.close();						
		}				
	 }
	
	 static void envio(String mensaje,int b) throws Exception{
			String linea, aux;		
			String[] lineaArray;
			InetAddress ip = InetAddress.getByName(ipAux);
			Socket clientSocket = new Socket(ip,6789);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			linea = mensaje.concat(":" + b);
			if(linea.substring(0, 4).equals("file"))
				enviarA(linea.substring(5));
			outToServer.writeBytes(linea + '\n');
			aux = inFromServer.readLine();
			lineaArray = aux.split(":");		
			if(lineaArray[1].equals("1"))
				actualizar(lineaArray[0]);
			clientSocket.close();
	}

}
