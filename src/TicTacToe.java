import java.util.InputMismatchException;
import java.util.LinkedList;

import static java.lang.Math.abs;

/**
 * This class implements and evaluates game situations of a TicTacToe game.
 */
public class TicTacToe {

    /**
     * Returns an evaluation for player at the current board state.
     * Arbeitet nach dem Prinzip der Alphabeta-Suche. Works with the principle of Alpha-Beta-Pruning.
     *
     * @param board     current Board object for game situation
     * @param player    player who has a turn
     * @return          rating of game situation from player's point of view
    **/
    public static int alphaBeta(Board board, int player)
    {
        return playerMax(board,-111,111,player, board.getN()*board.getN()-board.nFreeFields());
    }

    private static int playerMax(Board game, int alpha, int beta, int player, int depth) {
        //is game won
        if(game.isGameWon()) return -1*(game.nFreeFields()+1);
        else if(depth>=game.getN()*game.getN()) return 0;
        //get valid moves
        LinkedList<Position> validMoves = (LinkedList<Position>) game.validMoves();
        //for each valid move
        //recurse do move valid move
        //get score alphabeta other player
        //if score higher than alpha, set alpha = score
        //if alpha bigger equal beta, break BETA CUTOFF

        for (Position move : validMoves) {
            game.doMove(move, player);
            int score = playerMin(game,alpha,beta,-player, depth+1);
            game.undoMove(move);
            if (score > alpha) {
                alpha = score;
                if (alpha >= beta) break; //BETA CUTOFF
            }
        }

        //return alpha
        return alpha;
    }

    private static int playerMin(Board game, int alpha, int beta, int player, int depth) {
        //is game won
        if(game.isGameWon()) return game.nFreeFields()+1;
        else if(depth>=game.getN()*game.getN()) return 0;
        //get valid moves
        LinkedList<Position> validMoves = (LinkedList<Position>) game.validMoves();
        //for each valid move
        for (Position move : validMoves) {
            game.doMove(move, player);
            int score = playerMax(game,alpha,beta,-player,depth+1);
            game.undoMove(move);
            if (score < beta) {
                beta = score;
                if (beta <= alpha) break; //ALPHA CUTOFF
            }
        }

        //return beta
        return beta;
    }

    /**
     * Vividly prints a rating for each currently possible move out at System.out.
     * (from player's point of view)
     * Uses Alpha-Beta-Pruning to rate the possible moves.
     * formatting: See "Beispiel 1: Bewertung aller Zugmöglichkeiten" (Aufgabenblatt 4).
     *
     * @param board     current Board object for game situation
     * @param player    player who has a turn
    **/
    public static void evaluatePossibleMoves(Board board, int player) throws InputMismatchException
    {
        //headline
        //frame line #---------#     # + (3*n)- + #
        //            11 11 11
        //for y n-1 to 0
        // |
        //for x 0 to n-1
        //if getfield==0
        //    do move at that position,
        //    alphabeta,
        //    format, if result >= 0 put space before it
        //else
        //    lookuptable[getfield+1]
        //format space behind it
        // |
        // x end
        //println
        //y end
        //frame line #---------#     # + (3*n)- + #

        if(abs(player)!=1) throw new InputMismatchException("invalid player in evaluatePossibleMoves");
        String[] lookUpTable = {" O","  "," X"};
        System.out.println("Evaluation of player: '" + lookUpTable[player+1] + " '");
        String frameline = "#";
        for (int i = 1; i<= board.getN(); i++) {
            frameline += "---";
        }
        frameline += "#";
        System.out.println(frameline);

        String line;
        Position pos = new Position(0,0);
        for (int y = board.getN()-1; y>=0; y--) {
            line = "|";
            pos.y = y;
            for (int x = 0; x<board.getN(); x++) {
                pos.x = x;
                if(board.getField(pos)==0) {
                    board.doMove(pos,player);
                    int score = -alphaBeta(board,-player);
                    board.undoMove(pos);
                    if(score >= 0) line += " ";
                    line += Integer.toString(score);
                } else {
                    line += lookUpTable[board.getField(pos)+1];
                }
                line += " ";
            }
            line += "|";
            System.out.println(line);
        }

        System.out.println(frameline);
        System.out.println("");
    }

    public static void main(String[] args)
    {
        Board game = new Board(3);
        game.doMove(new Position(2,0),-1);
        game.doMove(new Position(1,2),1);
        game.print();
        System.out.println("");
        evaluatePossibleMoves(game,-1);
    }
}

