public class Run {
    public static void main(String[] args){
    	GamePlayer gp = new GamePlayer("","");
    	System.out.println("List of Rooms:"+gp.getRooms());
    	gp.joinRoom();
    }
}