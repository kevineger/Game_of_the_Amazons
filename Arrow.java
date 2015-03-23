/**
 * arrow object to shoot after a queen is moved
 *
 * Created by TCulos on 2015-02-26.
 */
public class Arrow extends GamePiece {

    public Arrow(int y, int x){
        super(y,x);
    }

    protected Arrow clone() {
        Arrow aNew = new Arrow(colPos, rowPos);
        return aNew;
    }

    public String toString(){
        return "Arrow is at Row: " + rowPos + ", Col: " + colPos;
    }

    public String translate() {

        String oldCol = "";
        int oldRow = 0;
        int x = colPos;
        int y = rowPos;
        switch(x){
            case 0: oldCol = "a";
                break;
            case 1: oldCol = "b";
                break;
            case 2: oldCol = "c";
                break;
            case 3: oldCol = "d";
                break;
            case 4: oldCol = "e";
                break;
            case 5: oldCol = "f";
                break;
            case 6: oldCol = "g";
                break;
            case 7: oldCol = "h";
                break;
            case 8: oldCol = "i";
                break;
            case 9: oldCol = "j";
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y){
            case 0: oldRow = 9;
                break;
            case 1: oldRow = 8;
                break;
            case 2: oldRow = 7;
                break;
            case 3: oldRow = 6;
                break;
            case 4: oldRow = 5;
                break;
            case 5: oldRow = 4;
                break;
            case 6: oldRow = 3;
                break;
            case 7: oldRow = 2;
                break;
            case 8: oldRow = 1;
                break;
            case 9: oldRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }
        return oldCol + oldRow;
    }

}
