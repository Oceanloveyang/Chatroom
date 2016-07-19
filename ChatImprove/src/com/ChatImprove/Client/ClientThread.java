package com.ChatImprove.Client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread extends Thread {
	//create a Socket 
    Socket s;
		//
	String username=null;
    BufferedReader BRflow=null;
	public ClientThread (Socket s) throws IOException{
		this.s=s;
		BRflow =new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
    @Override
	public void run(){
    	try {
			String message=null;
			while((message=BRflow.readLine())!=null){
				System.out.println(message);
			}
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
}
