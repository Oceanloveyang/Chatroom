package com.ChatImprove.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread implements Runnable{
	Socket s;
    String clientname;
    BufferedReader BRflow;
    PrintWriter PW;
    boolean LoginStatus=false;
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
			PW=new PrintWriter(s.getOutputStream(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			PW.println("Please login with username");
		    try{
		    	String message=null;
		    while((message=ReadFromClient())!=null){
		    	if(LoginStatus){
		    		if(message.startsWith("/")){
		    			if(message.startsWith("//")){
		    				if(message.trim().equals("//smile")){
		    					//System.out.println("hello");
		    					Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
		    					for(String key:keys){
		    						if(this==ChatServer.ServerThreadHashMap.get(key)){
		    							PW.println("#You smile to others!");
		    						}else{	
		    							ServerThread t=ChatServer.ServerThreadHashMap.get(key);
				    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
				    					pw.println("#"+this.clientname +" smiles to you!");
		    						}
		    					}
		    				}
		    				
		    				else if(message.startsWith("//hi")){
		    					String[] Words=message.split("\\s",2);
		    					//System.out.println(Words.length);
		    					Words[1]=Words[1].trim();
		    					if(Words.length>1&&Words[1]!=null){
		    						if(!Words[1].isEmpty()){
		    						int flag=0;
		    						Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
			    					for(String key:keys){
			    						if(key!=this.clientname&&key.equals(Words[1])){
			    							flag=1;break;
			    						}
			    					}
			    					if(flag==1){
			    						for(String key:keys){
			    							if(key!=this.clientname){
			    								if(key.equals(Words[1])){
			    									ServerThread t=ChatServer.ServerThreadHashMap.get(key);
							    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
							    					pw.println("#"+this.clientname +" greets to you: Hello, nice to meet you!");
							    					PW.println("#I greet to "+ key+" : Hello, nice to meet you!");
			    								}else{
			    									ServerThread t=ChatServer.ServerThreadHashMap.get(key);
							    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
							    					pw.println("#"+this.clientname +" greets to "+key+": Hello, nice to meet you!");
			    								}
			    							}
			    						}
			    					}else{
			    						PW.println("The person you greet to is not online!");
			    					}
		    					}
		    				}
		    					else{
		    						Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
		    						for(String key:keys){
		    							if(key!=this.clientname){
		    								ServerThread t=ChatServer.ServerThreadHashMap.get(key);
					    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
					    					pw.println("#"+this.clientname +" greets to all: Hello, nice to meet you!");
		    							}else{
		    								PW.println("#You greet to others: Hello, nice to meet you!");
		    							}
		    						}
		    					}
		    				}
		    				
		    			}else if(message.startsWith("/to")){
		    				String[] words=message.split("\\s",3);
		    				if(words.length>1&&words[1]!=null){
		    					words[1]=words[1].trim();
		    					if(!words[1].isEmpty()){
		    						if(this.clientname.equals(words[1])){
		    							PW.println("Stop talking to yourself!");
		    						}else{
		    							int flag=0;
		    							Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
			    						for(String key:keys){
			    							if(key!=this.clientname&&key.equals(words[1])){
			    								flag=1;
			    								PW.println("#I say to "+key+": "+words[2]);
			    								ServerThread t=ChatServer.ServerThreadHashMap.get(key);
						    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
						    					pw.println("#"+this.clientname +" says to you: "+words[2]);
			    								break;
			    							}
			    						}if(flag==0){
			    							PW.println(words[1]+" is not online!");
			    						}
		    						}
		    					}
		    				}
		    			}
		    			
		    			else if(message.startsWith("/who")){
		    				int count=1;
		    				PW.println("Current Online User:");
		    				Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
    						for(String key:keys){
    							PW.println(count+"."+key);
    							count++;
    						}
    						//PW.println("Now the number of users online is: "+ ChatServer.ServerThreadHashMap.size());
    						PW.println("Now the number of users online is: "+ count);
		    			}
		    			else if(message.startsWith("/history")){
		    				PW.println(message);
		    			}
		    			else if(message.startsWith("/quit")){
		    				LoginStatus=false;
		    				PW.println("/quit");
		    				Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
    						for(String key:keys){
    							if(key!=this.clientname){
    								ServerThread t=ChatServer.ServerThreadHashMap.get(key);
			    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
			    					pw.println("#"+this.clientname+" has left our chatroom!");
    							}
    						}
    						ChatServer.ServerThreadHashMap.remove(this.clientname,this);
    						s.close();
    						this.stop();
    						CloseThread=true;
		    			}
		    			else{
		    				PW.println("Not the default message!");
		    			}
		    			
		    		}else{
		    			Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
						for(String key:keys){
							if(key!=this.clientname){
								ServerThread t=ChatServer.ServerThreadHashMap.get(key);
		    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
		    					pw.println("#"+this.clientname+" says to you: "+ message);
		    					}
		    			PW.println("#You say to others: "+message);
		    		}
		    	}
		    	}else{
		    		if(message.startsWith("/login")){
		    			String[] names=message.split("\\s",2);
		    			//names[1]=names[1].trim();
		    			if(names.length==1||(names.length>1&&names[1].trim().isEmpty())){
		    				PW.println("Name can not be empty!");
		    			}else{
		    			if(ChatServer.ServerThreadHashMap.containsKey(names[1])){
		    				PW.println("Name exist, please choose another name.");
		    			}else{
		    				ChatServer.ServerThreadHashMap.put(names[1], this);
		    				PW.println("Welcome to our chatroom, "+names[1]);
		    				this.clientname=names[1];
		    				LoginStatus=true;
		    				Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
    						for(String key:keys){
    							if(key!=this.clientname){
    								ServerThread t=ChatServer.ServerThreadHashMap.get(key);
			    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
			    					pw.println(this.clientname+" has joined our chatroom!");
    							}
    						}
		    			}
		    		}
		    	}else if(message.startsWith("/quit")){
		    		PW.println("/quit with out login!");
		    		}else{
		    			PW.println("Invaild Command!");
		    		}
		    	}
		    }
			}catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("The Server has encounted some problems!");
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

