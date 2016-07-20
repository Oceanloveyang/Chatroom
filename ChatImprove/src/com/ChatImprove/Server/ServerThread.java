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
		    					System.out.println("hello");
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
		    					if(Words.length>1&&Words[1]!=null){
		    						Words[1]=Words[1].trim();
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
		    				else{
		    					PW.println("Not the default message! You can see the manual with /help");
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
		    				int count=0;
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
    						System.out.println(this.clientname+" has left our chatroom!");
    						ChatServer.OnlineUserNum--;
    				        System.out.println("Current the number of online user is: "+ChatServer.OnlineUserNum);
    						ChatServer.ServerThreadHashMap.remove(this.clientname,this);
    						//System.out.println(ChatServer.ServerThreadHashMap.size());
    						s.close();
    						this.stop();
    						CloseThread=true;
		    			}
		    			else if(message.startsWith("/help")){
		    				PW.println("----------ChatRoom Manual---------");
		    				PW.println("1.broadcast message startswith everything except \"/\"");
		    				PW.println("2.command message startswith \"/\":");
		    				PW.println("(1).\"/login + username\" to log in chatroom.");
		    				PW.println("(2).\"/to + username +message\" to start a private chat.");
		    				PW.println("(3).\"/who\" to query current online users.");
		    				PW.println("(4).\"/history + [parameter] + [parameter]\" to query chat history.");
		    				PW.println("(5).\"/quit\" to log out.");
		    				PW.println("3.default message startswith\"//\":");
		    				PW.println("(1).\"//smile\" to smile to other users.");
		    				PW.println("(2).\"//hi + [username]\" to greet to whether a person or other users.");
		    				PW.println("-----------------end--------------");
		    			}
		    			else{
		    				PW.println("Not the default message!You can see the manual with /help");
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
		    				PW.println(names[1]+" , Welcome to our chatroom!");
		    				this.clientname=names[1];
		    				LoginStatus=true;
		    				ChatServer.ServerThreadHashMap.put(names[1], this);
		    				ChatServer.OnlineUserNum++;
		    				System.out.println(names[1]+" has joined our chatroom!");
		    				System.out.println("Current the number of online user is: "+ChatServer.OnlineUserNum);
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
		    		this.stop();
		    		CloseThread=true;
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

  	
	private String ReadFromClient() throws IOException{
		try {
			return BRflow.readLine();
		} catch (IOException e) {
			// TODO: handle exception
			//this.stop();
			if(!CloseThread){
			if(LoginStatus)
			{
				Collection<String> keys = ChatServer.ServerThreadHashMap.keySet();
				for(String key:keys){
					if(key!=this.clientname){
						ServerThread t=ChatServer.ServerThreadHashMap.get(key);
    					PrintWriter pw=new PrintWriter(t.s.getOutputStream(),true);
    					pw.println(this.clientname+" has escaped from our chatroom!");
					}
				}
				this.stop();
				ChatServer.ServerThreadHashMap.remove(this.clientname,this);
				ChatServer.OnlineUserNum--;
				System.out.println(this.clientname+ " closed the console without logging out!");
			}
			else{
				this.stop();
				System.out.println("One client closed the console without logging in!");
			}
			}
			CloseThread=true;
		}
		return null;
	}
}

