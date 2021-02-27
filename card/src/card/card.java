package card;

public class card {
    public String Point;
    public char Color;
    public card(String Point,char Color){
    	this.Color=Color;
    	this.Point=Point;
    }
    public card(String mycard){
    	this.Color=mycard.charAt(0);
    	String string="";
    	for(int i=1;i<mycard.length();i++)
			string+=mycard.charAt(i);
    	this.Point=string;
    }
    public int getPoint(){
    	if(Point.equals("8"))
    		return 50;
    	else if(Point.equals("J") || Point.equals("Q") || Point.equals("K"))
    		return 10;
    	else if(Point.equals("A"))
    		return 1;
    	else if(Point.equals("10"))
    		return 10;
    	else
    		return Integer.parseInt(Point);
    }
	@Override
	public String toString() {
		return Color+""+Point;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Color;
		result = prime * result + ((Point == null) ? 0 : Point.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		card other = (card) obj;
		if (Color != other.Color)
			return false;
		if (Point == null) {
			if (other.Point != null)
				return false;
		} else if (!Point.equals(other.Point))
			return false;
		return true;
	}
	
    
    
}
