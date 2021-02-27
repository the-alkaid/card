package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import card.card;

public class MyClient {
    static ArrayList<card> handcard=new ArrayList(); 
    static card currentlast;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
       Socket socket=new Socket();
       socket.connect(new InetSocketAddress("127.0.0.1",30000),10000);
       new Thread(new ClientThread(socket)).start();//接受服务器的
       //向服务器写数据
       PrintStream ps=new PrintStream(socket.getOutputStream());
       BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
       String line=null;
       while((line=br.readLine())!=null){
    	  
    		   if(line.equals("NOT")){
    			   ps.println(line);
    			   
    		   }
    		   else if(line.equals("S") || line.equals("H") || line.equals("C") || line.equals("D")){
    			   ps.println(line);
    			   
    		   }
    		   else{	
    			   card putout=new card(line);
    			   if(putout.Point.equals("8") || putout.Point.equals(currentlast.Point)){
    				   ps.println(line);
    				   handcard.remove(putout);
    				   
    			   }
    			   else if((currentlast.Color=='S'||currentlast.Color=='H')&&(putout.Color=='S'||putout.Color=='H')){
    				   ps.println(line);
    				   handcard.remove(putout);
    				   
    			   }
    			   else if((currentlast.Color=='C'||currentlast.Color=='D')&&(putout.Color=='C'||putout.Color=='D')){
    				   ps.println(line);
    				   handcard.remove(putout);
    				  
    			   }
    			   else{
    				   System.out.println("you must follow the rule");
    			   }
        		  
    	   
    	   }
    	   
    	   			
       }
	}

}

class ClientThread implements Runnable {
	   private Socket socket;
	   private BufferedReader br=null;
	   public ClientThread(Socket socket) throws IOException{
		   this.socket=socket;
		   
		   br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		   
	   }
	   @Override
		public void run() {
			// TODO Auto-generated method stub
		   String content=null;
		   try{
		   while((content=br.readLine())!=null){
			   System.out.println(content);
			   if(content.startsWith("round")){
				   String[] str=content.split(" ");
				 
				   MyClient.currentlast=new card(str[4]);
			   }
		   }
		   }
		   catch(Exception e){
			   e.printStackTrace();
		   }
			
		}

	}