package card;

import java.util.*;

public class pile {
	public LinkedList<card> cardpile;
    public pile(){
    	cardpile=new LinkedList<card>();
    	for(int i=1;i<=13;i++){
    		for(int j=0;j<4;j++){
    			String point;
    			char color;
    			if(i==1){
    				point="A";
    			}
    			else if(i==11){
    				point="J";
    			}
    			else if(i==12){
    				point="Q";
    			}
    			else if(i==13){
    				point="K";
    			}
    			else{
    				point=String.valueOf(i);
    			}
    			
    			if(j==0)
    			{
    				color='S';
    			}
    			else if(j==1)
    			{
    				color='C';
    			}
    			else if(j==2)
    			{
    				color='D';
    			}
    			else 
    			{
    				color='H';
    			}
    			cardpile.add(new card(point,color));
    			Collections.shuffle(cardpile);
    		}
    	}
    }
    public card getonecard(){
    	card res=cardpile.get(0);
    	cardpile.remove(0);
     	while(res.Point.equals("8")){
     		cardpile.add(res);
     		res=cardpile.get(0);
        	cardpile.remove(0);
    	}
    	return res;
    }
    public card playergetonecard(){
    	card res=cardpile.remove(0);
    	
    	return res;
    }
    public LinkedList<card> gethandcard(){
    	LinkedList<card> res=new LinkedList();
    	for(int i=0;i<5;i++){
    		res.add(cardpile.remove(0));
    	}
    	return res;
    }
}
