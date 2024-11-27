package Chat;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea; 

public class Servidor  {

	public static void main(String[] args) {
		MarcoServidor mimarco=new MarcoServidor();
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}	
}

class MarcoServidor extends JFrame implements Runnable{
	private	JTextArea areatexto;
	
	public MarcoServidor(){
		setBounds(1200,300,280,350);				
		JPanel milamina= new JPanel();	
		milamina.setLayout(new BorderLayout());
		areatexto=new JTextArea();
		milamina.add(areatexto,BorderLayout.CENTER);
		add(milamina);
		setVisible(true);
		Thread mihilo = new Thread(this);
		mihilo.start();
	}

	@Override
	public void run() {
		try (ServerSocket servidor = new ServerSocket(123)) {
			
			String nick,ip,msj;
			int puerto;
			PaqueteEnvio paquete_recibido;
			
			while(true) {
				
				Socket misocket = servidor.accept();
				/*DataInputStream flujo_entrada = new DataInputStream(misocket.getInputStream());
				String msj = flujo_entrada.readUTF();
				areatexto.append("\n"+msj);
				misocket.close();*/
				ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
				paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();
				nick = paquete_recibido.getNick();
				//ip =  paquete_recibido.getIp();
				puerto = Integer.parseInt(paquete_recibido.getPuerto());
				msj =  paquete_recibido.getMensaje();
				areatexto.append("\n"+ nick + ": " + msj + " para " + puerto);
				
				
				Socket enviaDestinatario = new Socket("localhost",puerto);
				ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
				paqueteReenvio.writeObject(paquete_recibido);
				paqueteReenvio.close();
				enviaDestinatario.close();
				misocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}