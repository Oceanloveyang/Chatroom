package com.ChatImprove.Client;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientThread extends Thread implements Runnable{
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
				if(message.contains("Welcome to our chatroom")){
					String[] name=message.split(" ",2);
					username=name[0];
					WriteHistory(username,"");
					//System.out.println(username);				
				}
				if(message.startsWith("/quit")){
					if(message.equals("/quit with out login!")){
						System.out.println("You are disconnecting with server and exiting the client!");
						System.out.println("You will exit in 5 seconds!");
						Thread.sleep(5000);
						System.exit(0);
					}
					System.out.println("You are logging out!!!");
					Thread.sleep(5000);
					System.exit(0);
				}
				else if(message.startsWith("#"))
				{
					message=message.replaceAll("#", "");
					System.out.println(message);
					Date date=new Date();
					DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time=format.format(date);
					message=time+":"+message;
					WriteHistory(username, message);
				}
				else if(message.startsWith("/history")){
					File filename=new File("F:\\Chat\\"+username+".txt");
					if(message.equals("/history")){
					String str=ReadHistory(filename);
					System.out.print(str);
					}else{
						String[] words=message.split(" ",3);
						String str=ReadHistory(filename,Integer.parseInt(words[1]),Integer.parseInt(words[2]));
						System.out.print(str);
					}
				}
				else{
					System.out.println(message);
				}
				
			}
		} catch (IOException e) {
			// TODO: handle exception
			//e.printStackTrace();
			System.out.println("Server is boomming and disconnected!");
			try {
				s.close();
				Thread.sleep(5000);
			} catch (InterruptedException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Server is boomming and disconnected!");
		}
    }
    
    private void WriteHistory(String username,String content) throws IOException{
		String filepath="F:\\Chat\\"+username+".txt";
		File filename = new File(filepath);
		if(!filename.exists()){
			filename.createNewFile();
		}
		FileWriter wr=new FileWriter(filepath,true);
		if(content.equals("")){
			wr.close();
		}else{
			wr.write((content+"\r\n"));//write column with \n
			wr.close();
		}
    }
    
    private String ReadHistory(File Filename) throws IOException
	{
		 String read,readStr=""; 
	        FileReader fileread=null; 
	        BufferedReader bufread=null;
	        try { 
	            fileread = new FileReader(Filename); 
	            bufread = new BufferedReader(fileread);
	            int LineColNum=GetTotalLines(Filename);
	            //System.out.println(LineColNum);
	            try { 
	            	if(LineColNum<50){
	                System.out.println("You message history is less than 50 !");
	            		while ((read = bufread.readLine()) != null) { 
	                    readStr = readStr + read+ "\n"; 
	                } 
	            		readStr.substring(0,((LineColNum==0)?readStr.length():readStr.length()-2));
	                }else{
	                	readStr=ReadHistory(Filename, LineColNum, 50);
	                }
	            } catch (IOException e) { 
	                // TODO Auto-generated catch block 
	                e.printStackTrace(); 
	            } 
	        } catch (FileNotFoundException e) { 
	            // TODO Auto-generated catch block 
	            e.printStackTrace(); 
	        } finally{
	        	if(bufread!=null){
	        		try {
						bufread.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}if(fileread!=null){
	        		try {
						fileread.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		}
	        }
	        return readStr; 
	    }
	
	private String ReadHistory(File Filename,int Start_index,int max_count) throws IOException{
		String read,readStr="";
		FileReader fileread = new FileReader(Filename); 
        @SuppressWarnings("resource")
		BufferedReader bufread = new BufferedReader(fileread);
			int LineColNum=GetTotalLines(Filename);
		if(LineColNum<Start_index){
			System.out.println("You don't have so much conversation history!");
		}else{
			int flag=1;
			if((LineColNum-Start_index)>=max_count)
			while((read=bufread.readLine())!=null){
				if((flag>=(Start_index-max_count+1))&&(flag<=Start_index)){
				 readStr = readStr + read+ "\n";
				 }
				flag++;
			}else{
				System.out.println("You only have "+(LineColNum-Start_index)+" message history!");
				while((read=bufread.readLine())!=null){
					if(flag<=Start_index){
					readStr = readStr + read+ "\n";
					}
					flag++;
				}
			}
			readStr.substring(0,readStr.length()-2);
		}
		return readStr;
	}
	
	private int GetTotalLines(File Filename) throws IOException{
		FileReader fileread=new FileReader(Filename);
		@SuppressWarnings("resource")
		BufferedReader LineNumReader=new BufferedReader(fileread);
		int LineNum=0;
		while((LineNumReader.readLine())!=null){
			LineNum++;	
		}
		return LineNum;
	}
	
    
}
