/**
 * Created by Shannon on 2015-03-29.
 */
public class TranslateMove {
    /*
     * Method to translate move to sever
     */

    public String translateOut(move m){

        //move=’a3-g3’ syntax

        int oldRow = -1;
        String oldCol = "";
        int newRow = -1;
        String newCol = "";
        String move="";
        int y = m.oldRowPos;
        int x = m.oldColPos;
        int y2 = m.newRowPos;
        int x2 = m.newColPos;

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
        switch(x2){
            case 0: newCol = "a";
                break;
            case 1: newCol = "b";
                break;
            case 2: newCol = "c";
                break;
            case 3: newCol = "d";
                break;
            case 4: newCol = "e";
                break;
            case 5: newCol = "f";
                break;
            case 6: newCol = "g";
                break;
            case 7: newCol = "h";
                break;
            case 8: newCol = "i";
                break;
            case 9: newCol = "j";
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y2){
            case 0: newRow = 9;
                break;
            case 1: newRow = 8;
                break;
            case 2: newRow = 7;
                break;
            case 3: newRow = 6;
                break;
            case 4: newRow = 5;
                break;
            case 5: newRow = 4;
                break;
            case 6: newRow = 3;
                break;
            case 7: newRow = 2;
                break;
            case 8: newRow = 1;
                break;
            case 9: newRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }

        move = move.concat(oldCol+oldRow+"-"+newCol+newRow);
        return move;

    }
    /*
     * Method to translate move from server
     */

    public move translateIn(String move){

        char x = move.charAt(0);
        char y = move.charAt(1);
        char x2 = move.charAt(3);
        char y2 = move.charAt(4);

        int oldCol = -1;
        int oldRow = -1;
        int newCol = -1;
        int newRow = -1;

        switch(x){
            case 'a': oldCol = 0;
                break;
            case 'b': oldCol = 1;
                break;
            case 'c': oldCol = 2;
                break;
            case 'd': oldCol = 3;
                break;
            case 'e': oldCol = 4;
                break;
            case 'f': oldCol = 5;
                break;
            case 'g': oldCol = 6;
                break;
            case 'h': oldCol = 7;
                break;
            case 'i': oldCol = 8;
                break;
            case 'j': oldCol = 9;
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y){
            case '0': oldRow = 9;
                break;
            case '1': oldRow = 8;
                break;
            case '2': oldRow = 7;
                break;
            case '3': oldRow = 6;
                break;
            case '4': oldRow = 5;
                break;
            case '5': oldRow = 4;
                break;
            case '6': oldRow = 3;
                break;
            case '7': oldRow = 2;
                break;
            case '8': oldRow = 1;
                break;
            case '9': oldRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }
        switch(x2){
            case 'a': newCol = 0;
                break;
            case 'b': newCol = 1;
                break;
            case 'c': newCol = 2;
                break;
            case 'd': newCol = 3;
                break;
            case 'e': newCol = 4;
                break;
            case 'f': newCol = 5;
                break;
            case 'g': newCol = 6;
                break;
            case 'h': newCol = 7;
                break;
            case 'i': newCol = 8;
                break;
            case 'j': newCol = 9;
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y2){
            case '0': newRow = 9;
                break;
            case '1': newRow = 8;
                break;
            case '2': newRow = 7;
                break;
            case '3': newRow = 6;
                break;
            case '4': newRow = 5;
                break;
            case '5': newRow = 4;
                break;
            case '6': newRow = 3;
                break;
            case '7': newRow = 2;
                break;
            case '8': newRow = 1;
                break;
            case '9': newRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }
        move moveMessage = new move(newRow, newCol, new Queen(oldRow,oldCol, true));

        System.out.println(moveMessage.toString());
        return moveMessage;
    }



    public String translateArrowOut(Arrow a) {

        String oldCol = "";
        int oldRow = 0;
        int x = a.colPos;
        int y = a.rowPos;
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
                System.out.println("Invalid col position");
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
                System.out.println("Invalid row position");
                break;
        }
        return oldCol + oldRow;
    }


    public Arrow translateArrowIn(String move) {

        char x = move.charAt(0);
        char y = move.charAt(1);

        int oldCol = -1;
        int oldRow = -1;


        switch(x){
            case 'a': oldCol = 0;
                break;
            case 'b': oldCol = 1;
                break;
            case 'c': oldCol = 2;
                break;
            case 'd': oldCol = 3;
                break;
            case 'e': oldCol = 4;
                break;
            case 'f': oldCol = 5;
                break;
            case 'g': oldCol = 6;
                break;
            case 'h': oldCol = 7;
                break;
            case 'i': oldCol = 8;
                break;
            case 'j': oldCol = 9;
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y){
            case '0': oldRow = 9;
                break;
            case '1': oldRow = 8;
                break;
            case '2': oldRow = 7;
                break;
            case '3': oldRow = 6;
                break;
            case '4': oldRow = 5;
                break;
            case '5': oldRow = 4;
                break;
            case '6': oldRow = 3;
                break;
            case '7': oldRow = 2;
                break;
            case '8': oldRow = 1;
                break;
            case '9': oldRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;

        }
        Arrow a = new Arrow(oldRow, oldCol);
        return a;
    }

}
