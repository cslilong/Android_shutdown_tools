Android_shutdown_tools
======================

This project develop a tool for android device to shutdown your PC.

每天晚上都用手机连笔记本wifi上网，天气冷不想起床关电脑，于是就想写个android关机工具，用手机控制电脑关机。
大体想法：


Socket 网络连接模型 

代码实现：

【服务端】
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {

       /**
       * @param args
       * @throws IOException
       * @throws InterruptedException
       */
       public static void main(String[] args) throws IOException, InterruptedException {
             // TODO Auto-generated method stub
            ServerSocket ss = new ServerSocket(8888);
            System. out.println("Listening..." );
             while(true ) {
                  Socket socket = ss.accept();
                  System. out.println("client Connected..." );
                  DataInputStream dInputStream = new DataInputStream(socket.getInputStream());
            String msg = dInputStream.readUTF();
            System. out.println(msg);
             if(msg.equals("shutdown" )) {
                  Runtime. getRuntime().exec("cmd /c shutdown -s -t 30");
            }
            
            DataOutputStream dOutputStream = new DataOutputStream(socket.getOutputStream());
                  dOutputStream.writeUTF( "已经执行关机命令！" );
            
            Thread. sleep(200);
            dInputStream.close();
            dOutputStream.close();
            socket.close();
            }
      }
}
【客户端】
在android中对网络进行操作需要添加相应权限，在AndroidManifest.xml添加
<uses-permission android:name="android.permission.INTERNET" ></uses-permission>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" ></uses-permission>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" ></uses-permission>
<uses-permission android:name="android.permission.WAKE_LOCK" ></uses-permission>

客户端主要做两件事情：
1，获得本机Ip地址，推算服务器ip地址
2，通过Socket通信发送关机命令给服务端

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
       private Button sendBtn ;
       private Button getipBtn ;
       private EditText et ;
       private String serverIp ;
      
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. main);
       
        et = (EditText)findViewById(R.id. editText1);
        sendBtn = (Button)findViewById(R.id.send);
        sendBtn.setOnClickListener(new Sendlistener());
       
        getipBtn = (Button)findViewById(R.id.getip);
        getipBtn.setOnClickListener(new Sendlistener1());
       
    }
   
    private String intToIp( int i) {    
        return (i & 0xFF ) + "." +
      ((i >> 8 ) & 0xFF) + "." +
      ((i >> 16 ) & 0xFF) + "." +
      ( i >> 24 & 0xFF) ;
      }
   
    private String getServerIp( int i) {
      return (i & 0xFF ) + "." +
      ((i >> 8 ) & 0xFF) + "." +
      ((i >> 16 ) & 0xFF) + ".1";
    }
   
    class Sendlistener1 implements OnClickListener{
        public void onClick(View v) {
             //获取wifi服务
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE );
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                  wifiManager.setWifiEnabled( true);
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
                        Socket socket = new Socket(serverIp , 8888);
                        DataOutputStream dOutputStream = new DataOutputStream(socket.getOutputStream());
                        dOutputStream.writeUTF( "shutdown");
                        
                        
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
}

