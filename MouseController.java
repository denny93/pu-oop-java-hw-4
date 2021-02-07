import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *This is class for control all game by clicking over one of the graphics buckets
 * in front end
 * we use mouse press to handle the event from clicking over jframe
 */

public class MouseController extends JComponent implements MouseListener {
    int mode = 1;
    int x = 0;
    int y = 0;
    int isOK = 0;
    int firstI = 0;
    int firstJ = 0;
    int secondI = 0;
    int secondJ = 0;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     *
     * @param iSecond is used for the current cell of the matrix
     * @param jSecond is used for the current cell of the matrix
     * @return count of available position for a move
     */

    private boolean gardColorMode(int iSecond, int jSecond) {
        int max = 0 ;
        if (iSecond + 1 < 8 && changeColor(iSecond + 1, jSecond)) {
            max++;
        }
        if (iSecond - 1 > -1 && changeColor(iSecond - 1, jSecond)) {
            max++;
        }
        if (jSecond + 1 < 8 && changeColor(iSecond, jSecond + 1)) {
            max++;
        }
        if (jSecond - 1 > -1 && changeColor(iSecond, jSecond - 1)) {
            max++;
        }
        Start.addElements(Start.gs, true);
        if (max == 0) {
            Start.finishLoser();
            return false;
        }
        return true;
    }

    /**
     *
     * @param iSecond is used for the current cell of the matrix
     * @param jSecond is used for the current cell of the matrix
     * @return true if change cell that we can move and false when the sell is unavailable for move.
     */
    private boolean changeColor(int iSecond, int jSecond) {
        if (Start.shemaArray[iSecond][jSecond].baseModel.getClass().getName() == "NoPlace") {
            return false;
        } else {
            if (Start.shemaArray[iSecond][jSecond].baseModel.finalStatus == 1) {
                return false;
            }
            Start.shemaArray[iSecond][jSecond].baseModel.state = 1;
            return true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mode == 1) {
            x = e.getX() - 10;
            y = e.getY() - 10;
            checkFirsstElemnts(x , y );

        } else if (mode == 2) {
            int xs = e.getX() - 10;
            int ys = e.getY() - 10;
            checkSecondElement(xs , ys);

        }
    }

    /**
     *
     * @param xs is used for the current cell of the matrix
     * @param ys is used for the current cell of the matrix
     *  the result - check cell that we over if is baba a house or we in dead road
     */
    private void checkSecondElement(int xs, int ys) {
        checkEl(xs, ys);
        if (Start.shemaArray[secondI][secondJ].baseModel.state != 1) {
            isOK--;
            mode = 2;
            Start.shemaArray[firstI][firstJ].baseModel.state = 0;
            return;
        } else {
            Start.shemaArray[secondI][secondJ].baseModel.finalStatus = 1;
            restartArray();
            mode = 1;
            Start.addElements(Start.gs, true);
            if (checkBoxSituation(secondI, secondJ)) {
                Start.finishLoser();
            } else {
                if (checkWinner(secondI, secondJ)) {
                    Start.finishWinner();
                }
            }
        }
    }

    /**
     *
     * @param xs is used for the current cell of the matrix
     * @param ys is used for the current cell of the matrix
     */
    private void checkEl(int xs, int ys) {
        for (int i = 0; i < 8; i++) {
            if (isOK > 1) {
                break;
            }
            for (int j = 0; j < 8; j++) {
                if ((Start.shemaArray[i][j].startX <= xs && Start.shemaArray[i][j].endX >= xs) &&
                        (Start.shemaArray[i][j].startY <= ys && Start.shemaArray[i][j].endY >= ys)) {
                    secondI = i;
                    secondJ = j;
                    isOK++;
                    return;
                }
            }
        }
    }

    /**
     *  restart array cell state
     */
    private void restartArray() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Start.shemaArray[i][j].baseModel.state = 0;
            }
        }
    }

    /***
     *
     * @param x is used for the current cell of the matrix (get position of pointer)
     * @param y is used for the current cell of the matrix (get position of pointer)
     */
    private void checkFirsstElemnts(int x, int y) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((Start.shemaArray[i][j].startX <= x && Start.shemaArray[i][j].endX >= x) &&
                        (Start.shemaArray[i][j].startY <= y && Start.shemaArray[i][j].endY >= y)) {
                    if (Start.shemaArray[i][j].baseModel.state == 0) {

                        if(!gardColorMode(i, j))
                        {
                            return;
                        }
                        mode = 2;
                        firstI = i;
                        firstJ = j;
                        isOK++;
                        return;
                    }
                }
            }
        }
    }

    /*
    chek if we found "baba qga hause"
     */
    private boolean checkWinner(int secondI, int secondJ) {
        if (Start.shemaArray[secondI][secondJ].baseModel.baba == 1) {
            return true;
        }
        return false;
    }

    /*
        check if the current position hasn't possible move
     */
    private boolean checkBoxSituation(int secondI, int secondJ) {
        int max = 0;
        if (secondI + 1 < 8) {
            if (checkBlueBox(secondI + 1, secondJ)) {
                max++;
            }
        }
        if (secondI - 1 > -1) {
            if (checkBlueBox(secondI - 1, secondJ)) {
                max++;
            }
        }
        if (secondJ + 1 < 8) {
            if (checkBlueBox(secondI, secondJ + 1)) {
                max++;
            }
        }
        if (secondJ - 1 > -1) {
            if (checkBlueBox(secondI, secondJ - 1)) {
                max++;
            }
        }
        if (max >= 3) {
            return true;
        }
        return false;
    }


    /**
     * check is unknow territory is blue space
     * @return
     */
    private boolean checkBlueBox(int i , int j) {
        if ( Start.shemaArray[i][j].bomb == 1)
        {
            return  true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
