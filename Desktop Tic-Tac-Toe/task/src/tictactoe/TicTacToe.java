package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class TicTacToe extends JFrame {
    Thread th = new Thread();
    static int moves = 0;
    static boolean turn = true;
    Board board = new Board();
    BottomPanel bottomPanel;
    UpperPanel upperPanel;
    boolean gamesStart = false;

    public TicTacToe() {

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(400, 400);
        JMenuBar myMenu = new MenuPlease();
        setJMenuBar(myMenu);
        bottomPanel = new BottomPanel();
        upperPanel= new UpperPanel();
        add(upperPanel);
        add(board);
        add(bottomPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Tic Tac Toe");


    }

    //check for a winner or a draw and updates status accordingly
    String gameStatus(){
        String player1type = ((PlayerButton)upperPanel.getComponent(0)).getText();
        String player2type = ((PlayerButton)upperPanel.getComponent(2)).getText();
        String winnerString = "";
        for (int i = 0; i < 8; i++) {
            //check rows for winner
            if(i % 3 == 0) {
                if(!((Button)board.getComponent(i+0)).getText().equals(" ")  &&
                        ((Button)board.getComponent(i+0)).getText().equals(((Button)board.getComponent(i+1)).getText()) &&
                        ((Button)board.getComponent(i+1)).getText().equals(((Button)board.getComponent(i+2)).getText())) {
//                    winnerString = " (" + ((Button) board.getComponent(i+0)).getText() + ") wins";
                    winnerString = ((Button) board.getComponent(i+0)).getText();
                    gamesStart = false;
                    break;
                }
            }
            //check columns for winner
            if(i < 3 ) {
                if(!((Button)board.getComponent(i+0)).getText().equals(" ") &&
                        ((Button)board.getComponent(i+0)).getText().equals(((Button)board.getComponent(i+3)).getText()) &&
                        ((Button)board.getComponent(i+3)).getText().equals(((Button)board.getComponent(i+6)).getText())) {
//                    winnerString = " (" + ((Button) board.getComponent(i+0)).getText() + ") wins";
                    winnerString = ((Button) board.getComponent(i+0)).getText();

                    gamesStart = false;
                    break;
                }
            }
        }
        //check diagonals for winner
        if(!((Button)board.getComponent(4)).getText().equals(" ") &&
                (((Button)board.getComponent(0)).getText().equals(((Button)board.getComponent(4)).getText()) &&
                ((Button)board.getComponent(4)).getText().equals(((Button)board.getComponent(8)).getText())
                || ((Button)board.getComponent(2)).getText().equals(((Button)board.getComponent(4)).getText()) &&
                ((Button)board.getComponent(4)).getText().equals(((Button)board.getComponent(6)).getText()))) {
//            winnerString = " (" + ((Button) board.getComponent(4)).getText() + ") wins";
            winnerString = ((Button) board.getComponent(4)).getText();
            gamesStart = false;
        }
        //if no winner is appointed after all possible moves set status to "draw"
        String gameStatusMessageFormat = MessageFormat.format("The turn of {0} Player ({1})",
                moves % 2 == 0 ? player1type : player2type,
                moves % 2 == 0 ? "X" : "O");
        String gameWinnerMessageFormat = MessageFormat.format("The {0} Player ({1}) wins",
                winnerString.equals("X") ? player1type : player2type,
                winnerString);


        return  moves == 9 && winnerString.isEmpty() ? "Draw" : winnerString.isEmpty() ? gameStatusMessageFormat : gameWinnerMessageFormat;

    }


    //create board helper class to display the gaiming grid and host the cells(Buttons)
    class Board extends JPanel {

        public Board() {

            setLayout(new GridLayout(3, 3));
            setSize(300, 300);
            int buttons = 0;
            char nameY = 'A';
            char nameX = '3';

            while (buttons < 9) {
                add(new Button("Button" + nameY + nameX));

                nameY++;
                buttons++;
                if (nameY == 'D') {
                    nameX--;
                    nameY = 'A';
                }
            }
        }

    }

    //button helper class, to represent each game cell
    class Button extends JButton {

        public  Button(String name) {
            setName(name);
            setFocusPainted(false);
            setEnabled(false);
            setText(" ");
            setFont(new Font("Aerial", Font.BOLD, 40));

            addActionListener(e -> {
                if (getText().equals(" ")) {
                    setText(TicTacToe.moves % 2 == 0 ? "X" : "O");

                    TicTacToe.moves++;
                    String gameStatus = gameStatus();

                    bottomPanel.labelText.setText(gameStatus);
                    if (gameStatus.contains("wins") || gameStatus.contains("Draw")){
                        for (Component c: board.getComponents()) {
                           c.setEnabled(false);
                        }
                    }


                    removeActionListener(this.actionListener);
                }
            });
        }
    }

    //create bottom ribon panel for containing a status label and reset button to clear the board
    class BottomPanel extends JPanel {
        JLabel labelText;

        BottomPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setMaximumSize(new Dimension(500, 40));

            labelText = new JLabel("Game is not started");
            labelText.setName("LabelStatus");
            add(labelText);
        }
    }

    class UpperPanel extends JPanel {

        UpperPanel() {
            setLayout(new GridLayout(1, 3, 0, 5));
            setMaximumSize(new Dimension(500, 40));
            JButton reset = new JButton("Start");
            JButton playerSelect1 = new PlayerButton();
            JButton playerSelect2 = new PlayerButton();
            playerSelect1.setName("ButtonPlayer1");
            playerSelect2.setName("ButtonPlayer2");


            reset.setName("ButtonStartReset");
            reset.setFocusPainted(false);   //remove border around selected button
            reset.addActionListener(e -> {
                moves = 0; // set the static move counter to 0 as for a new game
                gamesStart = (reset.getText().equals("Start") ? true : false);
                for (Component comp : board.getComponents()) {
                    ((Button)comp).setText(" "); //resetting all cell texts
                    comp.setEnabled(gamesStart); //enable all components of the board when pressing reset
                }

//                boolean test = reset.getText().equals("Start");
                reset.setText(gamesStart ? "Reset" : "Start");
                ((JLabel)(bottomPanel.getComponent(0))).setText(gamesStart ? gameStatus() : "Game is not started");

                playerSelect1.setEnabled(!gamesStart);
                playerSelect2.setEnabled(!gamesStart);



            });


            add(playerSelect1);
            add(reset);
            add(playerSelect2);

        }
    }
    class PlayerButton extends JButton{

        public PlayerButton(){
            super();
            setFocusPainted(false);
            setText("Human");
            addActionListener(e -> {
                setText(getText().equals("Human") ? "Robot" : "Human");
            });
        }
    }

    class TicTacRobot extends Thread {
        boolean turn = true;
        @Override
        public void run() {
//            while (turn && gamesStart){
//                try{
//                    join(1000);
//
//                }catch(InterruptedException ex){
//
//                };
            synchronized (this){
                List<Button> b = Arrays.stream(board.getComponents())
                        .map(comp->(Button)comp)
                        .filter(x -> x.getText().equals(" "))
                        .collect(Collectors.toList());
                b.get(new Random().nextInt(b.size())).doClick();
                System.out.println(moves);
            }

//                setTurn(false);




//               turn = false;
//               pickMove.doClick();
//                          .collect(Collectors.toList());
                System.out.println("Name: " + getName());
                System.out.println("ID: " + getId());
                System.out.println("Alive: " + isAlive());
                System.out.println("Priority: " + getPriority());
                System.out.println("Daemon: " + isDaemon());

//            }
        }

        public void setTurn(boolean turn) {
            this.turn = turn;
        }
    }

    class MenuPlease extends JMenuBar{
        MenuPlease(){

            JMenu menuGame = new JMenu("Game");
            menuGame.setName("MenuGame");


            StartMenuItem menuHumanHuman = new StartMenuItem("Human","Human");
            StartMenuItem menuHumanRobot = new StartMenuItem("Human", "Robot");
            StartMenuItem menuRobotHuman = new StartMenuItem("Robot", "Human");
            StartMenuItem menuRobotRobot = new StartMenuItem("Robot" , "Robot");


            JMenuItem menuExit = new JMenuItem("Exit");
            menuExit.setName("MenuExit");
            menuExit.addActionListener(e -> System.exit(0));

            add(menuGame);

            menuGame.add(menuHumanHuman);
            menuGame.add(menuHumanRobot);
            menuGame.add(menuRobotHuman);
            menuGame.add(menuRobotRobot);
            menuGame.addSeparator();
            menuGame.add(menuExit);

        }

        class StartMenuItem extends JMenuItem{
            String player1;
            String player2;
            StartMenuItem(String player1,String player2){
                this.player1=player1;
                this.player2 = player2;
                setName("Menu" + player1 + player2);
                setText(player1 + " vs " + player2);
                addActionListener(e -> {
                ((PlayerButton)upperPanel.getComponent(0)).setText(this.player1);
                ((PlayerButton)upperPanel.getComponent(2)).setText(this.player2);
                if (((JButton)upperPanel.getComponent(1)).getText().equals("Start")){
                    ((JButton)upperPanel.getComponent(1)).doClick();
                } else {
                    ((JButton)upperPanel.getComponent(1)).doClick();
                    ((JButton)upperPanel.getComponent(1)).doClick();
                }
            });
            }
        }
    }
}

