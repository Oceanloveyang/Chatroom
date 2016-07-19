package com.ChatImprove.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    public static ServerSocket ss=null;
	public static ConcurrentHashMap<String, ServerThread> ServerThreadHashMap= new ConcurrentHashMap<String, ServerThread>();
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			ServerSocket ss=new ServerSocket(12345);
			System.out.println("Server is running, waiting for clients connections!");
			while(true){
				new Thread(new ServerThread(ss.accept())).start();
				System.out.println("Client"+ss.getInetAddress()+"connected to the server!");
			}
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("The port is occupied!");
		}finally{
			ss.close();
		}

	}

}
