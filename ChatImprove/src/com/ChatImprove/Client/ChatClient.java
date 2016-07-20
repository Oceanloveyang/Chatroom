package com.ChatImprove.Client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			Socket s=new Socket("localhost",12345);
			//start Client Thread
			new Thread(new ClientThread(s)).start();
			//get the flow of Socket
			PrintWriter PW=new PrintWriter(s.getOutputStream(),true);
			String message=null;
			//read the input from keyboard
			BufferedReader BRflow=new BufferedReader(new InputStreamReader(System.in));
			while((message=BRflow.readLine())!=null){
				if(message==""||(message.trim().equals(""))){
					System.out.println("message can't be send with blank!!!");
				}else{
					PW.println(message); 
					PW.flush();
				}
			}	
			}catch(Exception e){
				//e.printStackTrace();
				System.out.println("Server was closed!");
			}
		}

}
