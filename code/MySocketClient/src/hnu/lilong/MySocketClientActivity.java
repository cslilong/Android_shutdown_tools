package hnu.lilong;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MySocketClientActivity extends Activity {
    /** Called when the activity is first created. */
	private Button sendBtn;
	private Button getipBtn;
	private EditText et;
	private String serverIp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //connectToServer();
        
        et = (EditText)findViewById(R.id.editText1);
        sendBtn = (Button)findViewById(R.id.send);
        sendBtn.setOnClickListener(new Sendlistener());
        
        getipBtn = (Button)findViewById(R.id.getip);
        getipBtn.setOnClickListener(new Sendlistener1());
        
    }
    
    private String intToIp(int i) {     
        return (i & 0xFF ) + "." +
    	((i >> 8 ) & 0xFF) + "." +
    	((i >> 16 ) & 0xFF) + "." +
    	( i >> 24 & 0xFF) ;
	}
    
    private String getServerIp(int i) {
    	return (i & 0xFF ) + "." +
    	((i >> 8 ) & 0xFF) + "." +
    	((i >> 16 ) & 0xFF) + ".1";
    }
    
    class Sendlistener1 implements OnClickListener{
        public void onClick(View v) {
        	//获取wifi服务
        	WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
            	wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            et.setText(ip);
            serverIp = getServerIp(ipAddress);
        }
    }
    
    class Sendlistener implements OnClickListener{  
        public void onClick(View v) { 
        	try {
				Socket socket = new Socket(serverIp, 8888);
				DataOutputStream dOutputStream = new DataOutputStream(socket.getOutputStream());
				dOutputStream.writeUTF("shutdown");
				
				
				DataInputStream dInputStream = new DataInputStream(socket.getInputStream());
	    		String msg = dInputStream.readUTF();
	    		et.setText(msg);
	    		
				dOutputStream.close();
				dInputStream.close();
				socket.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    }
    
    
    
    public void connectToServer(){
    	try {
    		Socket socket = new Socket(serverIp, 8888);
    		DataInputStream dInputStream = new DataInputStream(socket.getInputStream());
    		String msg = dInputStream.readUTF();
    		
    		EditText et = (EditText)findViewById(R.id.editText1);
    		et.setText(msg);
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    }
}