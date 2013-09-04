import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.omg.SendingContext.RunTime;


public class MySocketServer {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		ServerSocket ss = new ServerSocket(8888);
		System.out.println("Listening...");
		while(true) {
			Socket socket = ss.accept();
			System.out.println("client Connected...");
			DataInputStream dInputStream = new DataInputStream(socket.getInputStream());
    		String msg = dInputStream.readUTF();
    		System.out.println(msg);
    		if(msg.equals("shutdown"))
    		Runtime.getRuntime().exec("cmd /c shutdown -s -t 30");
    		
    		
    		DataOutputStream dOutputStream = new DataOutputStream(socket.getOutputStream());
			dOutputStream.writeUTF("已经执行关机命令！");
    		
    		Thread.sleep(200);
    		dInputStream.close();
    		dOutputStream.close();
    		socket.close();
			
		}
		
	}

}
