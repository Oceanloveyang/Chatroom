package com.ChatImprove.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    public static ServerSocket ss=null;
    public static int OnlineUserNum=0;
	public static ConcurrentHashMap<String, ServerThread> ServerThreadHashMap= new ConcurrentHashMap<String, ServerThread>();
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			@SuppressWarnings("resource")
			ServerSocket ss=new ServerSocket(12345);
			System.out.println("Server is running, waiting for clients connections!");
			while(true){
				Socket s=ss.accept();
				new Thread(new ServerThread(s)).start();
				System.out.println("Client "+s.getRemoteSocketAddress()+" connected to the server!");
			}
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("The port is occupied!");
		}finally{
			if(ss!=null)
			ss.close();
		}

	}

}
