import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Kevin
 */
public class Board extends JPanel
{

    private ImageIcon blackQueen;
    private ImageIcon whiteQueen;
    private ImageIcon arrowMark;
//    private AmazonMove move;
    private String title = "Game of the Amazons";
    private JFrame frame = new JFrame(title);
    private JButton board[][] = new JButton[10][10];
    private JPanel panel = new JPanel();


    private boolean arrowShot = false;

    /**
     * Constructor, sets up the Game Board.
     */
    public Board()
    {
        makeBoard();
        makePieces();
    }

    private void makeBoard ()
    {
        panel.setBorder(BorderFactory.createTitledBorder("The Game of Amazons"));

        panel.setLayout(new GridLayout(10, 10));
        Graphics g = null;
        panel.paintComponents(g);
        // Creates the GameBoard Representation With out any pieces
        for (int row = 0; row < 10; row++)
        {
            for (int col = 0; col < 10; col++)
            {
                board[row][col] = new JButton();
                panel.add(board[row][col]);
                board[row][col].setActionCommand(Integer.toString(row) + Integer.toString(col));
                board[row][col].setToolTipText(String.valueOf((char)(col + 'a')) + String.valueOf(9 - row));
                board[row][col].setOpaque(true);
                board[row][col].setRolloverEnabled(true);
                if ((row % 2) == (col % 2))
                {
                    board[row][col].setBackground(Color.gray);
                }
                else
                {
                    board[row][col].setBackground(Color.white);
                }
            }
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(650, 650));
        frame.setLayout(new BorderLayout());
        frame.setLayout(new BorderLayout());
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates the Pieces that are being used, the Queens and the arrows.
     */
    private void makePieces ()
    {
        try
        {
            File whiteQueenFile = new File("./images/nicSmallBlue.png");
            whiteQueen = new ImageIcon(ImageIO.read(whiteQueenFile));
            File blackQueenFile = new File("./images/nicSmall.png");
            blackQueen = new ImageIcon(ImageIO.read(blackQueenFile));
            File arrowFile = new File("./images/arrow.png");
            arrowMark = new ImageIcon(ImageIO.read(arrowFile));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        board[0][3].setIcon(blackQueen);
        board[0][6].setIcon(blackQueen);
        board[3][0].setIcon(blackQueen);
        board[3][9].setIcon(blackQueen);
        board[6][0].setIcon(whiteQueen);
        board[6][9].setIcon(whiteQueen);
        board[9][6].setIcon(whiteQueen);
        board[9][3].setIcon(whiteQueen);
    }

    /**
     * Updates the board's display based on a move action.
     *
     */
    public void update (move queenMove, Arrow a, boolean isBlack)
    {
        ImageIcon queen = null;
        if(isBlack) {
            queen = blackQueen;
        }
        else {
            queen = whiteQueen;
        }
        board[queenMove.oldRowPos][queenMove.oldColPos].setIcon(null);
        board[queenMove.newRowPos][queenMove.newColPos].setIcon(queen);
        board[a.rowPos][a.colPos].setIcon(arrowMark);
    }
}