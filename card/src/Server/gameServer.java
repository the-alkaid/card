package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import card.*;



public class gameServer {
	public static List<Socket> list=Collections.synchronizedList(new ArrayList());
	public static List<serverThread> serverThreadlist=Collections.synchronizedList(new ArrayList());
	public static int playerCount=1;
	public static pile p;
	public static discardPile dp;
	public static int handindex=0;
	public static boolean dirction;
	public static int round;
	public static HashMap<Integer,int[]> goal;
	public static ArrayList<LinkedList<card>> handcard; 
	public static card lastcard;
	public static String lastplayer;
	public static String miss=null;
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
        p=new pile();
        dp=new discardPile();
        round=1;
        dirction=true;
        goal=new HashMap();
        handcard=new ArrayList();
      
		ServerSocket ss=new ServerSocket(30000);
        while(list.size()<4){
        	Socket socket=ss.accept();
        	list.add(socket);
        	serverThreadlist.add(new serverThread(socket,playerCount));
        	serverThreadlist.get(playerCount-1).run();  	
        	playerCount++;
        }
        
        while(!isEnd(goal)){
        	lastplayer="System";
        	 for(int i=1;i<5;i++){
         	    serverThread st=serverThreadlist.get(i-1);
         	    st.handcard=gameServer.p.gethandcard();
         	    handcard.add(st.handcard);
 				String content="your handcards are: ";
 				for(card c: st.handcard)
 					content+=c.toString()+" ";
 				    st.ps.println(content);
        	 }
        	 int[] goalcurrent=new int[4];
        	 lastcard=p.getonecard();
        	 dp.addcard(lastcard);
        	 while(true){
        		if(turnisend()){
        			changeHandindex();
        			if(endround()){
        				for(int i=0;i<4;i++){
        					int sum=0;
        					for(card c:handcard.get(i)){
        						sum+=c.getPoint();
        					}
        					goalcurrent[i]=sum;
        				}
        				goal.put(round, goalcurrent);
        				int g1=0,g2=0,g3=0,g4=0;
        				for(int round:goal.keySet()){
        					g1+=goal.get(round)[0];
        					g2+=goal.get(round)[1];
        					g3+=goal.get(round)[2];
        					g4+=goal.get(round)[3];
        				}
        				System.out.println("round"+round);
        				String  goalsum="round"+round+" goals: player1 goals "+g1+" ;player2 goals "+g2+" ;player3 goals "+g3+" ;player4 goals "+g4;
        				
        				for(int i=1;i<5;i++){
        					serverThread st=serverThreadlist.get(i-1);
        	 				st.ps.println(goalsum);
        	        	 }
        				
    	 			
    	 				System.out.println(goalsum);
        				
        				round++;
        				 handcard=new ArrayList();
        	        	 handindex=0;
        	        	 p=new pile();
        	             dp=new discardPile();
        	             dirction=true;
        				for(int i=1;i<5;i++){
        					serverThread st=serverThreadlist.get(i-1);
        	         	  
        	 				String content="New round begin: ";
        	 				st.ps.println(content);
        	        	 }
        				break;
        				
        			} 	 
        		}
        		
        	 }
        }
        int winindex=whowin();
        serverThread st=serverThreadlist.get(winindex);
        st.ps.println("you win!");

		for(int i=1;i<5;i++){
     	    serverThread st1=serverThreadlist.get(i-1);
				String content="the winner is player"+(winindex+1);
				st1.ps.println(content);
    	 }
		while(true){
			
		}
        
	}
	private static int whowin() {
		// TODO Auto-generated method stub
		int g1=0,g2=0,g3=0,g4=0;
		for(int round:goal.keySet()){
			g1+=goal.get(round)[0];
			g2+=goal.get(round)[1];
			g3+=goal.get(round)[2];
			g4+=goal.get(round)[3];
		}
		if(g1<g2 && g1<g3 && g1<g4)
			return 0;
		else if(g2<g1 && g2<g3 && g2<g4)
			return 1;
		else if(g3<g1 && g3<g2 && g3<g4)
			return 2;
		else 
			return 3;
		
	}
	private static boolean endround() {
		// TODO Auto-generated method stub
		if(handcard.get(0).size()==0 || handcard.get(1).size()==0|| handcard.get(2).size()==0||handcard.get(3).size()==0)
			return true;
		return false;
	}
	private static void changeHandindex() {
		// TODO Auto-generated method stub
		if(dirction)
			handindex=(handindex+1)%4;
		else{
			if(handindex==0){
				handindex=3;
			}
			else
				handindex--;
		}
	}
	private static int getnextPeople() {
		// TODO Auto-generated method stub
		if(dirction)
			return (handindex+1)%4;
		else{
			if(handindex==0){
				return 3;
			}
			else
				return handindex-1;
		}
	}
	private static boolean turnisend() {
		// TODO Auto-generated method stub
		 serverThread st=serverThreadlist.get(handindex);
		 String content="your handcards are: ";
		     for(card c: st.handcard)
				content+=c.toString()+" ";
			 st.ps.println(content);
			 String str="";
				 if(dirction)
					 str="left";
				 else
					 str="right";
				 st.ps.println("round"+round+":the last card is "+lastcard.toString()+" ,last player is "+lastplayer+" , dirction is from "+str+" ,is your turn");
				 
		 String palyercard =st.readFromSocket();
		 if(palyercard.equals("NOT")){
			 for(int i=0;i<3;i++){
				 if(p.cardpile.size()==0)
					  return true;
				 card newcard=p.playergetonecard();
				 st.handcard.add(newcard);
				
				 content="your handcards are: ";
	 			     for(card c: st.handcard)
	 					content+=c.toString()+" ";
	 				 st.ps.println(content);
	 				  str="";
	 				 if(dirction)
	 					 str="left";
	 				 else
	 					 str="right";
	 				 st.ps.println("round"+round+":the last card is "+lastcard.toString()+" ,last player is "+lastplayer+" , dirction is from "+str+" ,is your turn");
	 				 
	 				 palyercard =st.readFromSocket();
	 				if(!palyercard.equals("NOT")){
	 					lastplayer=(handindex+1)+"";
	 					 card cardplayer=new card(palyercard);
	 					 if(cardplayer.Point.equals("A")){
	 						 dirction=!dirction; 
	 						st.handcard.remove(cardplayer);
		 					 dp.addcard(cardplayer);
		 					 lastcard=cardplayer;
		 					miss=null;
		 					break;
		 					}
	 					 else if(cardplayer.Point.equals("Q")){
	 						miss=(handindex+1)+"";
	 						changeHandindex();
	 						st.handcard.remove(cardplayer);
		 					 dp.addcard(cardplayer);
		 					 lastcard=cardplayer;
		 					
		 					serverThread st1=serverThreadlist.get(handindex);
	        	         	  
        	 				
        	 				st1.ps.println("you loss your turn because player "+miss);
		 					break;
	 						 }
	 					 else if(cardplayer.Point.equals("2")){
	 						  int nextindex=getnextPeople();
	 						 miss=null;
	 						  serverThread st1=serverThreadlist.get(nextindex);
	 						  if(dp.getFist().Point.equals("2")){
	 							 if(p.cardpile.size()==0)
	 								  return true;
	 							  card new1=p.playergetonecard();
	 							 if(p.cardpile.size()==0)
	 								  return true;
	 							  card new2=p.playergetonecard();
	 							  if(p.cardpile.size()==0)
	 							  st1.handcard.add(new1);
	 							  st1.handcard.add(new2);
	 						  }
	 						 if(p.cardpile.size()==0)
								  return true;
	 						  card new1=p.playergetonecard();
	 						 if(p.cardpile.size()==0)
								  return true;
	 						  card new2=p.playergetonecard();
	 						  st1.handcard.add(new1);
	 						  st1.handcard.add(new2);
	 						 
	 			 				content="your handcards are: ";
	 			 				for(card c: st1.handcard)
	 			 					content+=c.toString()+" ";
	 			 				    st1.ps.println(content);
	 			 				 st.handcard.remove(cardplayer);
	 		 					 dp.addcard(cardplayer);
	 		 					 lastcard=cardplayer;
	 		 				 
	 		 					break;
	 					  }
	 					 else if(cardplayer.Point.equals("8")){
	 						miss=null;
	 						 st.ps.println("choose the new suit");
	 						 palyercard =st.readFromSocket();
	 						 st.handcard.remove(cardplayer);
	 						 dp.addcard(cardplayer);
	 						 cardplayer=new card(palyercard+"R");
	 						 lastcard=cardplayer;
		 				 
		 					break;
	 						 
	 					 }
	 					 else{
	 						miss=null;
	 						 st.handcard.remove(cardplayer);
 		 					 dp.addcard(cardplayer);
 		 					 lastcard=cardplayer;
 		 					break;
	 					 }
	 					 
	 				}
			 }
		 }
		 else{
			 lastplayer=(handindex+1)+"";
			 card cardplayer=new card(palyercard);
			 if(cardplayer.Point.equals("A")){
				 miss=null;
				 dirction=!dirction; 
				 st.handcard.remove(cardplayer);
					 dp.addcard(cardplayer);
					 lastcard=cardplayer;
				 
					}
			 else if(cardplayer.Point.equals("Q")){
				 miss=(handindex+1)+"";  
				 changeHandindex();
				   st.handcard.remove(cardplayer);
					 dp.addcard(cardplayer);
					 lastcard=cardplayer;
					
					 serverThread st1=serverThreadlist.get(handindex);
 	 				
 	 				st1.ps.println("you loss your turn because player "+miss);
					}
			 else if(cardplayer.Point.equals("2")){
				 miss=null; 
				 int nextindex=getnextPeople();
				 
				  serverThread st1=serverThreadlist.get(nextindex);
				  if(dp.getFist().Point.equals("2")){
					  if(p.cardpile.size()==0)
							  return true;
					  card new1=p.playergetonecard();
					  if(p.cardpile.size()==0)
							  return true;
					  card new2=p.playergetonecard();
					  st1.handcard.add(new1);
					  st1.handcard.add(new2);
				  }
				  if(p.cardpile.size()==0)
						  return true;
				  card new1=p.playergetonecard();
				  if(p.cardpile.size()==0)
						  return true;
				  card new2=p.playergetonecard();
				  st1.handcard.add(new1);
				  st1.handcard.add(new2);
				 
	 				content="your handcards are: ";
	 				for(card c: st1.handcard)
	 					content+=c.toString()+" ";
	 				    st1.ps.println(content);
	 				   st.handcard.remove(cardplayer);
	 					 dp.addcard(cardplayer);
	 					 lastcard=cardplayer;
	 				 
	 				
			  }
			 else if(cardplayer.Point.equals("8")){
				 miss=null;
				 st.ps.println("choose the new suit");
				 palyercard =st.readFromSocket();
				 st.handcard.remove(cardplayer);
				 dp.addcard(cardplayer);
				 
				 cardplayer=new card(palyercard+"R");
				 lastcard=cardplayer;
						 
			 }
			 else{
				 miss=null;
				 st.handcard.remove(cardplayer);
					 dp.addcard(cardplayer);
					 lastcard=cardplayer;
				 
					
			 }
			 
		 }
		return true;
	}
	private static boolean isEnd(HashMap<Integer, int[]> goal2) {
		// TODO Auto-generated method stub
		int g1=0,g2=0,g3=0,g4=0;
		for(int round:goal.keySet()){
			g1+=goal.get(round)[0];
			g2+=goal.get(round)[1];
			g3+=goal.get(round)[2];
			g4+=goal.get(round)[3];
		}
		if(g1>=100||g2>=100||g3>=100||g4>=100)
			return true;
		return false;
	}
}
