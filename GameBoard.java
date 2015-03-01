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
    public GameBoard(Queen[] friendly, Queen[]enemies) {
    	Board = new JPanel();
    	Board.setLayout(new GridLayout(10,10));
        Board.setPreferredSize(boardSize);
        Board.setBounds(0, 0, boardSize.width, boardSize.height);
        
//        Draw Board squares
        for (int i = 0; i < 100; i++) {
            JPanel square = new JPanel(new BorderLayout());
            Board.add(square);
            int row = (i / 10) % 2;
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
        JLabel QueenW1 = new JLabel(new ImageIcon("./images/nicSmall.png"));
        JLabel QueenW2 = new JLabel(new ImageIcon("./images/nicSmall.png"));
        JLabel QueenW3 = new JLabel(new ImageIcon("./images/nicSmall.png"));
        JLabel QueenW4 = new JLabel(new ImageIcon("./images/nicSmall.png"));
     
        JLabel QueenB1 = new JLabel(new ImageIcon("./images/nicSmallBlue.png"));
        JLabel QueenB2 = new JLabel(new ImageIcon("./images/nicSmallBlue.png"));
        JLabel QueenB3 = new JLabel(new ImageIcon("./images/nicSmallBlue.png"));
        JLabel QueenB4 = new JLabel(new ImageIcon("./images/nicSmallBlue.png"));

        JPanel panel1 = (JPanel)Board.getComponent(friendly[0].getConcatPos());
        panel1.add(QueenW1);
        panel1.setVisible(true);
        panel1.setEnabled(true);
        JPanel panel2 = (JPanel)Board.getComponent(friendly[1].getConcatPos());
        panel2.add(QueenW2);
        JPanel panel3 = (JPanel)Board.getComponent(friendly[2].getConcatPos());
        panel3.add(QueenW3);
        JPanel panel4 = (JPanel)Board.getComponent(friendly[3].getConcatPos());
        panel4.add(QueenW4);

        JPanel panel5 = (JPanel)Board.getComponent(enemies[0].getConcatPos());
        panel5.add(QueenB1);
        JPanel panel6 = (JPanel)Board.getComponent(enemies[1].getConcatPos());
        panel6.add(QueenB2);
        JPanel panel7 = (JPanel)Board.getComponent(enemies[2].getConcatPos());
        panel7.add(QueenB3);
        JPanel panel8 = (JPanel)Board.getComponent(enemies[3].getConcatPos());
        panel8.add(QueenB4);

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
