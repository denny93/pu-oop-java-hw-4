import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Random;

public  class Start extends JPanel {
    public static Shema[][] shemaArray = new Shema[8][8];
    static JFrame frame;
    public static Graphics gs;
    Image img1 , img2;
    public  void paint(Graphics g){
        gs = g;
        addElements(g , false );
    }

    /**
     *
     * @param g grafic model we use for paint
     * @param modes use to allow repaint form
     */
    public  static void addElements(Graphics g, boolean modes) {
        if (modes) {
            frame.repaint();
        }
        int x = 0 ;
        int y = 0;
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(shemaArray[i][j].baseModel.state == 1 ){
                    addXElement(g, x,  y);
                }
                else if(shemaArray[i][j].baseModel.finalStatus == 1 ) {
                    addFinalPosition(g, x,  y);
                }
                else{
                    addBaseColor(g, x,  y, i , j );
                }
                addPositionParameters(x , y , i , j);
                x+=70;
            }
            y+= 70;
            x = 0 ;
        }
    }

    /**
     *
     * @param x start left top position (px)
     * @param y start left top position (px)
     * @param i position of the cell
     * @param j position of the cell
     */
    private static void addPositionParameters(int x, int y , int i , int j) {
        Shema mdoel = shemaArray[i][j];
        mdoel.startY =y;
        mdoel.startX =x;
        mdoel.endX =x + 70;
        mdoel.endY = y+70;
        shemaArray[i][j] = mdoel;
    }

    /**
     * used for add base color of cell
     * @param g grafic model we use for paint
     * @param x start left top position (px)
     * @param y start left top position (px)
     * @param i position of the cell
     * @param j posiition of cell
     */
    private static void addBaseColor(Graphics g, int x, int y , int i , int j) {
        g.setColor(shemaArray[i][j].baseModel.color);
        g.fillRect(x, y, 70, 70);
        g.setColor(Color.black);
        g.drawRect(x, y, 70, 70);
    }

    /**
     * use this mothod for add "?" in cells
     * @param g grafic model we use for paint
     * @param x start left top position (px)
     * @param y start left top position (px)
     */
    private static void addFinalPosition(Graphics g, int x, int y) {
        g.setColor(Color.yellow);
        g.fillRect(x, y, 70 , 70 );;
        g.setColor(Color.black);
        g.drawRect(x, y, 70, 70);
        g.setColor(Color.red);
        g.drawString("?" ,x+ 35 , y + 35);
    }

    /**
     * use this mothod for add "X" in cells
     * @param g grafic model we use for paint
     * @param x start left top position (px)
     * @param y start left top position (px)
     */
    private static void addXElement( Graphics g , int x , int y) {
        g.setColor(Color.yellow);
        g.fillRect(x, y, 70, 70);
        g.setColor(Color.black);
        g.drawRect(x, y, 70, 70);
        g.setColor(Color.BLUE);
        g.drawString("X" , x+ 35 , y + 35);
    }

    /**
     * use this function to end a game if we found "baba qga " house
     */
    public static void finishWinner()
    {
        int answer=JOptionPane.showConfirmDialog(null,"You Win. Anotehr game?");
        if(answer==JOptionPane.YES_OPTION){
            refreshPage();
        }
    }

    /**
     * refresh form after accepting the new game
     */
    private static void refreshPage() {
        shemaArray= new Shema[8][8];
        importInfomation();
        enterBomb();
        enterBabaQgaHouse();
        addElements(gs, true);

    }

    /**
     * use this function to end a game if we haven't any available move
     */
    public static void finishLoser()
    {
        int answer=JOptionPane.showConfirmDialog(null,"You Lose. Anotehr game?");
        if(answer==JOptionPane.YES_OPTION){
            refreshPage();
        }

    }

    /**
     * use this function to end a game if we haven't any available move
     */
    private static void enterBabaQgaHouse() {
        Random rand = new Random();
        for (int i = 0 ; i < 8 ; i++)
        {
            int gen = rand.nextInt(50);
            for (int j = 0 ;j  < 8 ; j++)
            {
                if (shemaArray[i][j].baseModel.getClass().getName() == "GPSCordinates") {
                    if (gen < 25) {
                        shemaArray[i][j].baseModel.baba = 1;
                        return;
                    }
                }
            }
        }
    }

    /**
     * enter inaccessible territory on the map
     */
    private static void enterBomb() {
        Random rand = new Random();
        int maxGEn = 0;
        for (int i = 0 ; i < 8 ; i++)
        {
            int gen = rand.nextInt(50);
            for (int j = 0 ;j  < 8 ; j++)
            {
                if (shemaArray[i][j].baseModel.getClass().getName() == "UnknowTeritory") {
                    if (gen < 25){
                        shemaArray[i][j].bomb = 1;
                        maxGEn++;
                    }
                }
                if (maxGEn == 5)
                {return;}
            }
        }
    }

    /**
     * start program
     * @param args
     */

    public  static void main(String[] args)
    {
        createJframe();
        refreshPage();
    }

    /**
     * create jframe form
     */
    private static void createJframe() {
        frame = new JFrame();
        frame.setSize(600,600);
        frame.getContentPane().add(new Start());
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.black);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Component mouseClick = new MouseController()  ;
        frame.addMouseListener((MouseListener) mouseClick);
    }

    /**
     * add start importtant infomaption in  array
     */
    private static void importInfomation() {
        createStart();
        createNOMOVE();
        createGPSFREE();
        createUnknowPlace();
    }

    /**
     * add unknown territory in array
     */
    private static void createUnknowPlace() {
        for(int i = 0 ; i < 8 ; i++)
        {
            for(int j = 0 ; j < 8 ; j++) {
                if (shemaArray[i][j] == null) {
                    shemaArray[i][j] = new Shema(0, 0, 0, 0, new UnknowTeritory(Color.orange));
                }
            }
        }
    }

    /**
     * add avaable GPS cordinates in array
     */
    private static void createGPSFREE() {
        Random rand = new Random();
        int max = 8;
        for (int i = 0 ; i < max ; i++)
        {
            int x = rand.nextInt(8);
            int y = rand.nextInt(8);
            if (shemaArray[x][y] == null)
            {
                shemaArray[x][y] = new Shema(0,0,0,0, new GPSCordinates(Color.green));
            }
            else
            {
                max++;
            }
        }
    }

    /**
     * add inaccessible territory in array
     */
    private static void createNOMOVE() {
        Random rand = new Random();
        int max = 8;
        for (int i = 0 ; i < max ; i++)
        {
            int x = rand.nextInt(8);
            int y = rand.nextInt(8);
            if (shemaArray[x][y] == null)
            {
                shemaArray[x][y] = new Shema(0,0,0,0, new NoPlace(Color.gray));
            }
            else
            {
                max++;
            }
        }
    }

    private static void createStart() {
        shemaArray[0][7] = new Shema(0,0,0,0,new StartCordinates(Color.yellow));
    }
}
