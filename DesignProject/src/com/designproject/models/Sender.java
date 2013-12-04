package com.designproject.models;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class Sender {
	Socket RTSPsocket;
	InetAddress ServerIPAdd = null;
	InetAddress ClientIPAdd = null;
	int RTSP_PORT;
	boolean returned = false;
	boolean result = false;

	/**
	 * 
	 * @param IP String IP address
	 * @param ServerPort int Port Number
	 * @throws IOException
	 */
	public Sender(String IP, int ServerPort) throws IOException, Exception {
		ServerIPAdd = InetAddress.getByName(IP);
		RTSPsocket = new Socket();
		RTSPsocket.connect(new InetSocketAddress(ServerIPAdd, ServerPort), 1000);
	}

	//send data to server
	public void RTSPSend(String data) {
		new sendAsync().execute(data);
	}

	public void close() {
		try {
			RTSPsocket.close();
		} catch (IOException e) {
			Log.d("Socket Error", e.toString());
		}
	}
	
	class sendAsync extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				final PrintWriter sendData = new PrintWriter(new BufferedWriter(new OutputStreamWriter(RTSPsocket.getOutputStream())), true);
				sendData.println(params[0]);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		
		protected void onPostExcecute(Boolean res){
			returned = true;
			result = res;
		}		
	}
}
