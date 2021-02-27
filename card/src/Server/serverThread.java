package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

import card.card;

public class serverThread {
public Socket socket;
public BufferedReader br=null;
public PrintStream ps=null;
public LinkedList<card> handcard;
public int index;
public serverThread( Socket socket,int index) throws IOException{
	this.socket=socket;
	this.index=index;
	br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	handcard=new LinkedList();
}

public void run() {
	// TODO Auto-generated method stub
	
	try {
		ps=new PrintStream(socket.getOutputStream());
		ps.println("your id is:"+index);
		
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
}
public String readFromSocket(){
	try{
		String line=br.readLine();
		System.out.println("client"+index+": "+line);
		return line;
	}
	catch(Exception e){
		gameServer.list.remove(this.socket);
	}
	return null;
}

}
