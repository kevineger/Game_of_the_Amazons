/**
 * 
 */

/**
 * @author Kevin
 *
 */

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JFrame {
	JSplitPane Pane;
    JPanel Board;

    JTextArea console = new JTextArea();
    private int xShift;
    private int yShift;
    final Dimension paneSize = new Dimension(800, 800);
    final Dimension boardSize = new Dimension(600,600);
    final Dimension textAreaSize = new Dimension(200,200);
    
//    GameBoard constructor
    public GameBoard() {
    	Board = new JPanel();
    	Board.setLayout(new GridLayout(8, 8));
        Board.setPreferredSize(boardSize);
        Board.setBounds(0, 0, boardSize.width, boardSize.height);
        
//        Draw Board sqaures
        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel(new BorderLayout());
            Board.add(square);
            int row = (i / 8) % 2;
            if (row == 0) {
            	square.setBackground( i % 2 == 0 ? Color.white : Color.gray);
            }               
            else {
            	square.setBackground( i % 2 == 0 ? Color.gray: Color.white);
            }                
        }

        // Add Text Area
        console.setBounds(0,0, textAreaSize.width, textAreaSize.height);

        //Create split pane
        Pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, Board, console);
        Pane.setPreferredSize(paneSize);

        //Pieces
        JLabel Piece_WHITE = new JLabel(new ImageIcon("./images/nic.png"));
        JLabel Piece_BLACK = new JLabel(new ImageIcon("./images/nic2.png"));

        /*
        Test Display Pieces
         */

        JPanel panel2 = (JPanel)Board.getComponent(0);
        panel2.add(Piece_WHITE);

        JPanel panel = (JPanel)Board.getComponent(3);
        panel.add(Piece_BLACK);

        //add to display
        getContentPane().add(Pane);
    	
    }


    public void write(String message){
        console.append(message);
    }

    public String read(){
        return console.getText();
    }
}
