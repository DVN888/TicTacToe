import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.abs;
/**
 * This class represents a generic TicTacToe game board.
 */
public class Board {
    private int n;
    private int[][] Fields;
    private int countFree;
    
    /**
     *  Creates Board object, am game board of size n * n with 1<=n<=10.
     */
    public Board(int n) throws InputMismatchException
    {
        if(n>10 || n<1) throw new InputMismatchException("facking n bounds you dum");
        this.n = n;
        this.Fields = new int[this.n][this.n];
        this.countFree = this.n * this.n;

        for(int i=0;i<this.n;i++) {
            for(int j=0;j<this.n;j++) {
                this.Fields[i][j] = 0;
            }
        }
    }
    
    /**
     *  @return     length/width of the Board object
     */
    public int getN() { return n; }
    
    /**
     *  @return     number of currently free fields
     */
    public int nFreeFields() {
        return countFree;
    }
    
    /**
     *  @return     token at position pos
     */
    public int getField(Position pos) throws InputMismatchException
    {
        if(pos.x < 0 || pos.x > getN()-1 || pos.y < 0 || pos.y > getN()-1) throw new InputMismatchException("Invalid Position Input in func getField(pos)");
        return Fields[pos.x][pos.y];
    }

    /**
     *  Sets the specified token at Position pos.
     */    
    public void setField(Position pos, int token) throws InputMismatchException
    {
        if(!(token==-1 || token==1 || token==0)) throw new InputMismatchException("Invalid Token in func setField()");
        if(pos.x < 0 || pos.x > getN()-1 || pos.y < 0 || pos.y > getN()-1) throw new InputMismatchException("Invalid Position Input in func setField()");
        if(abs(getField(pos)) != abs(token)) this.countFree += abs(getField(pos)) - abs(token);
        Fields[pos.x][pos.y]=token;
    }
    
    /**
     *  Places the token of a player at Position pos.
     */
    public void doMove(Position pos, int player)
    {
        if(!(player==-1 || player==1)) throw new InputMismatchException("Invalid Player in func doMove()");
        if(pos.x < 0 || pos.x > getN()-1 || pos.y < 0 || pos.y > getN()-1) throw new InputMismatchException("Invalid Position Input in func doMove()");
        if(getField(pos)!=0) throw new InputMismatchException("Field is taken func doMove()");
        setField(pos,player);
    }

    /**
     *  Clears board at Position pos.
     */
    public void undoMove(Position pos)
    {
        if(pos.x < 0 || pos.x > getN()-1 || pos.y < 0 || pos.y > getN()-1) throw new InputMismatchException("Invalid Position Input in func undoMove()");
        setField(pos,0);
    }
    
    /**
     *  @return     true if game is won, false if not
     */
    public boolean isGameWon() {
        int checkToken;
        Position checkPos = new Position(0,0);
        boolean checker = true;
        for(int x = 0 ; x<getN() ; x++){
            checkPos.x = x;
            checkPos.y = 0;
            checkToken = getField(checkPos);
            checker = true;
            for (int y = 0; y < getN(); y++) {
                checkPos.y = y;
                if (getField(checkPos) != checkToken || checkToken==0) {
                    checker = false;
                    break;
                }
            }
            if(checker) return true;
        }

        for(int y = 0 ; y<getN() ; y++){
            checkPos.y = y;
            checkPos.x = 0;
            checkToken = getField(checkPos);
            checker = true;
            for (int x = 0; x < getN(); x++) {
                checkPos.x = x;
                if (getField(checkPos) != checkToken || checkToken==0) {
                    checker = false;
                    break;
                }
            }
            if(checker) return true;
        }

        //block for diagonal 00 to NN
        checker = true;
        checkToken = getField(new Position(0,0));
        for (int i = 0 ; i < getN() ; i++) {
            checkPos.x = i;
            checkPos.y = i;
            if(getField(checkPos)!= checkToken || checkToken==0) {
                checker = false;
                break;
            }
        }
        if(checker) return true;

        //block for diagonal 0N to N0
        checker = true;
        checkToken = getField(new Position(0,getN()-1));
        for (int i = 0 ; i < getN() ; i++) {
            checkPos.x = i;
            checkPos.y = getN()-1-i;
            if(getField(checkPos)!= checkToken || checkToken==0) {
                checker = false;
                break;
            }
        }
        return checker;
    }

    /**
     *  @return     set of all free fields as some Iterable object
     */
    public Iterable<Position> validMoves()
    {
        LinkedList<Position> resultList = new LinkedList<>();
        Position checker = new Position(0,0);

        for (int i = 0 ; i < getN() ; i++) {
            for (int j = 0 ; j < getN() ; j++) {
                checker.x = j;
                checker.y = i;
                if(getField(checker)==0) {
                    Position pos = new Position(j,i);
                    resultList.add(pos);
                };
            }
        }

        return resultList;
    }

    /**
     *  Outputs current state representation of the Board object.
     *  Practical for debugging.
     */
    public void print()
    {
        char[] lookupTable = {'O',' ','X'};
        String line = "#";
        for (int i = 0 ; i < getN() ; i++) {
            line = line + "---";
        }
        line = line + "#";
        System.out.println(line);

        Position checker = new Position(0,0);
        for (int y= getN()-1 ; y >= 0 ; y--) {
            line = "|";
            checker.y = y;
            for (int x = 0; x < getN() ; x++) {
                checker.x = x;
                line = line + " " + lookupTable[getField(checker)+1] + " ";
            }
            line = line + "|";
            System.out.println(line);
        }

        line = "#";
        for (int i = 0 ; i < getN() ; i++) {
            line = line + "---";
        }
        line = line + "#";
        System.out.println(line);
    }

    public static void main(String[] args) {
        Board DihCrackToe = new Board(3);
        int playa = 1;
        Random rnd = new Random();
        LinkedList<Position> list;
        while(DihCrackToe.nFreeFields()>0 && !DihCrackToe.isGameWon()) {
            list = (LinkedList<Position>) DihCrackToe.validMoves();
            DihCrackToe.doMove(list.get(rnd.nextInt(list.size())), playa);
            System.out.println("game is won: " + DihCrackToe.isGameWon());
            System.out.println("free spaces: " + DihCrackToe.nFreeFields());
            DihCrackToe.print();
            System.out.println("");
            playa = -playa;
        }
        System.out.println("====================================================");
        if(DihCrackToe.isGameWon()) {
            System.out.print("Winner: ");
            if(-playa==1) System.out.print("X");
            else System.out.print("O");
        } else {
            System.out.print("There were no winners this time.");
        }
        System.out.println("");
        System.out.println("Final Board Position:");
        DihCrackToe.print();
        System.out.println("====================================================");
    }
}

