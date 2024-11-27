package Chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Cliente {

	public static void main(String[] args) {
		MarcoCliente mimarco=new MarcoCliente();
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		setBounds(600,300,280,350);		
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		add(milamina);
		setVisible(true);
	}	
}

class LaminaMarcoCliente extends JPanel implements Runnable{
	private JTextField campo1,nick,puerto;
	private JButton miboton;
	private JTextArea campochat;
	
	public LaminaMarcoCliente(){
		nick = new JTextField(5);
		add(nick);
		JLabel texto=new JLabel("CHAT");
		add(texto);
		puerto = new JTextField(8);
		add(puerto);
		campochat = new JTextArea(12,20);
		add(campochat);
		campo1=new JTextField(20);
		add(campo1);		
		miboton=new JButton("Enviar");
		EnviarTexto mievento = new EnviarTexto();
		miboton.addActionListener(mievento);
		add(miboton);	
		Thread mihilo = new Thread(this);
		//mihilo.start();
	}
	
	private class EnviarTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try (Socket misocket = new Socket("localhost",123)) {
				/*DataOutputStream flujo_salida = new DataOutputStream(misocket.getOutputStream());
				flujo_salida.writeUTF(campo1.getText());
				flujo_salida.close();*/
				PaqueteEnvio datos = new PaqueteEnvio();
				datos.setNick(nick.getText());
				//datos.setIp(ip.getText());
				datos.setPuerto(puerto.getText());
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
				paquete_datos.writeObject(datos);
				misocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage());
			}
		}
	}

	@Override
	public void run() {
		
		try {
			ServerSocket servidor_cliente = new ServerSocket(321);
			Socket cliente;
			PaqueteEnvio paqueteRecibido;
			
			while(true) {
				cliente = servidor_cliente.accept();
				ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();
				campochat.append("\n"+paqueteRecibido.getNick()+": "+paqueteRecibido.getMensaje());
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}


class PaqueteEnvio implements Serializable{
	private String nick,ip,mensaje,puerto;

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
}
