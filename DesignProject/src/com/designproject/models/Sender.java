package com.designproject.models;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.StrictMode;
import android.util.Log;

public class Sender {
	Socket RTSPsocket;
	InetAddress ServerIPAdd = null;
	InetAddress ClientIPAdd = null;
	int RTSP_PORT;

	/**
	 * 
	 * @param IP String IP address
	 * @param ServerPort int Port Number
	 * @throws IOException
	 */
	public Sender(String IP, int ServerPort) throws IOException, Exception {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		ServerIPAdd = InetAddress.getByName(IP);
		RTSPsocket = new Socket();
		RTSPsocket.connect(new InetSocketAddress(ServerIPAdd, ServerPort), 1000);
	}

	//send data to server
	public void RTSPSend(String data) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		try {
			final PrintWriter sendData = new PrintWriter(new BufferedWriter(new OutputStreamWriter(RTSPsocket.getOutputStream())), true);
			sendData.println(data);
		} catch (Exception e) {
			Log.d("Socket Send", e.toString());
		}
	}

	public void close() {
		try {
			RTSPsocket.close();
		} catch (IOException e) {
			Log.d("Socket Error", e.toString());
		}
	}
}
