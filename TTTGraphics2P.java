import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial") //check meaning ???????
public class TTTGraphics2P extends JFrame{

public static final int ROWS = 3, COLS = 3;
public static final int CELL_SIZE = 100, CELL_SIZE_HALF = 50;
public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
public static final int GRID_WIDTH = 8;
public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;

public static final int CELL_PADDING = CELL_SIZE / 6;
public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING *2;
public static final int SYMBOL_STROKE_WIDTH = 8;

private static int currentRow, currentCol;

public enum GameState{
PLAYING, DRAW, CROSS_WON, NOUGHT_WON
}
private GameState currentState;

public enum Seed{
EMPTY, CROSS, NOUGHT
}
private Seed currentPlayer;

private Seed[][] board;
private DrawCanvas canvas; //canvas ???????
private JLabel statusBar;

private int gamewoncase;
public TTTGraphics2P(){
canvas = new DrawCanvas();
canvas.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
canvas.addMouseListener(new MouseAdapter(){
@Override
public void mouseClicked(MouseEvent e){
int mouseX = e.getX();
int mouseY = e.getY();
currentRow = mouseY / CELL_SIZE;
currentCol = mouseX / CELL_SIZE;

if(currentState == GameState.PLAYING){
if(currentRow < 3 && currentRow >= 0 && currentCol < 3 && currentCol >= 0 && board[currentRow][currentCol] == Seed.EMPTY){
board[currentRow][currentCol] = currentPlayer;
updateGame(currentPlayer, currentRow, currentCol);
currentPlayer = (currentPlayer == Seed.NOUGHT) ? Seed.CROSS : Seed.NOUGHT;
}
}
else{
initGame();
}
repaint();
}
}); ///???????????????????????????????????

statusBar = new JLabel("   ");
statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

Container cp = getContentPane();
cp.setLayout(new BorderLayout());
cp.add(canvas, BorderLayout.CENTER);
cp.add(statusBar,BorderLayout.PAGE_END);

setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
pack();
setTitle("TicTacToe");
setVisible(true);
board = new Seed[ROWS][COLS];
initGame();
}

public void initGame(){
for(int i=0;i<ROWS;i++){
for(int j=0;j<COLS;j++){
board[i][j] = Seed.EMPTY;
}
}
currentState = GameState.PLAYING;
currentPlayer = Seed.CROSS;
}

public void updateGame(Seed seed, int currentRow, int currentCol){
gamewoncase = hasWon(seed, currentRow, currentCol);
if(gamewoncase != 0){
currentState = (seed == Seed.CROSS) ? GameState.CROSS_WON : GameState.NOUGHT_WON;
}
else if(isDraw()){
currentState = GameState.DRAW;
}
}

public boolean isDraw(){
for(int i=0;i<ROWS;i++){
for(int j=0;j<COLS;j++){
if(board[i][j] == Seed.EMPTY) return false;
}
}
return true;
}

public int hasWon(Seed seed, int currentRow,int currentCol){
return  (board[currentRow][0] == seed && board[currentRow][1] == seed && board[currentRow][2] == seed) ? 1 :
	(board[0][currentCol] == seed && board[1][currentCol] == seed && board[2][currentCol] == seed) ? 2 :
	(currentRow == currentCol && board[0][0] == seed && board[1][1] == seed && board[2][2] == seed) ? 3 :
	(currentRow + currentCol == 2 && board[0][2] == seed && board[1][1] == seed && board[2][0] == seed) ? 4 : 0;
}

class DrawCanvas extends JPanel{
@Override
public void paintComponent(Graphics g){
super.paintComponent(g);
setBackground(Color.WHITE);

g.setColor(Color.LIGHT_GRAY);
for(int i=1;i<ROWS;i++){
g.fillRoundRect(0, CELL_SIZE * i - GRID_WIDTH_HALF, CANVAS_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH); /////??????????????????
}
for(int j=1;j<COLS;j++){
g.fillRoundRect(CELL_SIZE * j - GRID_WIDTH_HALF, 0, GRID_WIDTH, CANVAS_HEIGHT-1, GRID_WIDTH, GRID_WIDTH);
}

Graphics2D g2d = (Graphics2D)g;
g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); ////???????????????
for(int i=0;i<ROWS;i++){
for(int j=0;j<COLS;j++){
int x1 = j * CELL_SIZE + CELL_PADDING;
int y1 = i * CELL_SIZE + CELL_PADDING;
if(board[i][j] == Seed.CROSS){
g2d.setColor(Color.RED);
int x2 = (j+1) * CELL_SIZE - CELL_PADDING;
int y2 = (i+1) * CELL_SIZE - CELL_PADDING;
g2d.drawLine(x1, y1, x2, y2);
g2d.drawLine(x2, y1, x1, y2);
}
else if(board[i][j] == Seed.NOUGHT){
g2d.setColor(Color.BLUE);
g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
}
}
}

if(currentState == GameState.PLAYING){
statusBar.setForeground(Color.BLACK);
if(currentPlayer == Seed.CROSS) statusBar.setText("X's Turn");
else if(currentPlayer == Seed.NOUGHT) statusBar.setText("O's Turn");
}
else if(currentState == GameState.DRAW){
statusBar.setForeground(Color.RED);
statusBar.setText("Its a DRAW! Click to play again.");
}
else if(currentState == GameState.CROSS_WON){
statusBar.setForeground(Color.RED);
statusBar.setText("X WON! Click to play again.");
g2d.setColor(Color.GREEN);
switch(gamewoncase){
case 0: break;
case 1: g2d.drawLine(1, ((1+currentRow) * CELL_SIZE) - CELL_SIZE_HALF, CANVAS_WIDTH, (1+currentRow) * CELL_SIZE - CELL_SIZE_HALF); break;
case 2: g2d.drawLine((1+currentCol) * CELL_SIZE - CELL_SIZE_HALF, 0, (1+currentCol) * CELL_SIZE - CELL_SIZE_HALF, CANVAS_HEIGHT); break;
case 4: g2d.drawLine(0, CANVAS_HEIGHT, CANVAS_WIDTH, 0); break;
case 3: g2d.drawLine(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT); break;
}
}
else if(currentState == GameState.NOUGHT_WON){
statusBar.setForeground(Color.RED);
statusBar.setText("O WON! Click to play again.");
g2d.setColor(Color.GREEN);
switch(gamewoncase){
case 0: break;
case 1: g2d.drawLine(0, (1+currentRow) * CELL_SIZE - CELL_SIZE_HALF, CANVAS_WIDTH, (1+currentRow) * CELL_SIZE - CELL_SIZE_HALF); break;
case 2: g2d.drawLine((1+currentCol) * CELL_SIZE - CELL_SIZE_HALF, 0, (1+currentCol) * CELL_SIZE - CELL_SIZE_HALF, CANVAS_HEIGHT); break;
case 4: g2d.drawLine(0, CANVAS_HEIGHT, CANVAS_WIDTH, 0); break;
case 3: g2d.drawLine(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT); break;
}
}

}
}

public static void main(String[] args){
SwingUtilities.invokeLater(new Runnable(){
@Override
public void run(){
new TTTGraphics2P();
}
});
}
}
