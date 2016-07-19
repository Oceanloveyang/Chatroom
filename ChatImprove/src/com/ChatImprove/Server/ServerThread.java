package com.ChatImprove.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread implements Runnable{
	Socket s;
    String clientname;
    BufferedReader BRflow;
    PrintWriter PW;
    private volatile boolean CloseThread=false;
    
    public ServerThread(Socket s){
    	this.s=s;
    	try {
			BRflow = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

  @Override
  public void run(){
	while(!CloseThread){
		try {
			PW=new PrintWriter(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			PW.println("Please login with username");
			while(!CloseThread){
				String message=null;
				while((message=ReadFromClient())!=null){
					PW.println(message);
					break;
				}
			}
	}
  }
  	
	private String ReadFromClient(){
		try {
			return BRflow.readLine();
		} catch (IOException e) {
			// TODO: handle exception
			ChatServer.ServerThreadHashMap.remove(s);
		}
		return null;
	}
}

