import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class Board {

    public static class Pile extends Stack< Card > {

        public Card top ( ) {
            try {
                return this.elementAt ( this.size ( ) - 1 );
            }
            catch ( ArrayIndexOutOfBoundsException e ) {
                throw new EmptyStackException ( );
            }
        }

        public Card CardAt ( int index ) {
            return this.elementAt ( this.size ( ) - 1 - index );
        }

        private void turnPile ( ) {
            int i = 0;
            while ( true ) {
                try {
                    this.elementAt ( i ).faceDown ( );
                    i++;
                }
                catch ( ArrayIndexOutOfBoundsException e ) {
                    break;
                }
            }
        }

        private void unTurnPile ( ) {
            int i = 0;
            while ( true ) {
                try {
                    this.elementAt ( i ).faceUp ( );
                    i++;
                }
                catch ( ArrayIndexOutOfBoundsException e ) {
                    break;
                }
            }
        }

        private void printPile ( int nrOfCards ) {
            if ( nrOfCards == 0 )
                nrOfCards = this.size ( );

            for ( int i = Math.max ( this.size ( ) - nrOfCards, 0 ); i < this.size ( ); i++ ) {
                if ( !this.get ( i ).faceUp ) {
                    System.out.print ( TextColor.GREEN + "▮" + TextColor.RESET );
                    continue;
                }

                String name = "";

                switch ( this.get ( i ).number ) {
                    case ACE -> name += "A";
                    case TWO -> name += "2";
                    case THREE -> name += "3";
                    case FOUR -> name += "4";
                    case FIVE -> name += "5";
                    case SIX -> name += "6";
                    case SEVEN -> name += "7";
                    case EIGHT -> name += "8";
                    case NINE -> name += "9";
                    case TEN -> name += "10";
                    case JACK -> name += "J";
                    case QUEEN -> name += "Q";
                    case KING -> name += "K";
                }

                switch ( this.get ( i ).suite ) {
                    case SPADES -> name += "♠";
                    case DIAMONDS -> name += "♦";
                    case CLUBS -> name += "♣";
                    case HEARTS -> name += "♥";


                }

                switch ( this.get ( i ).color ) {
                    case RED -> System.out.print ( TextColor.RED );
                    case BLACK -> System.out.print ( TextColor.BLUE );
                }

                System.out.print ( name + TextColor.RESET + " " );
            }

            System.out.println ( );
        }

    }

    private static class Move {
        boolean browseMove;

        boolean revealedCard;
        Pile source;
        Pile destination;
        int nrOfCards;

        private Move ( int nrOfCards ) {
            this.browseMove = true;
            this.nrOfCards = nrOfCards;
        }

        private Move ( boolean revealedCard, Pile source, Pile destination, int nrOfCards ) {
            this.revealedCard = revealedCard;
            this.source = source;
            this.destination = destination;
            this.nrOfCards = nrOfCards;
        }
    }

    public static class InvalidMoveException extends Exception {

    }

    public static class UnknownPileException extends Exception {
    }

    Stack< Move > Moves = new Stack< Move > ( );

    Pile Stock = new Pile ( );
    Pile Waste = new Pile ( );

    Pile Pile1 = new Pile ( );
    Pile Pile2 = new Pile ( );
    Pile Pile3 = new Pile ( );
    Pile Pile4 = new Pile ( );
    Pile Pile5 = new Pile ( );
    Pile Pile6 = new Pile ( );
    Pile Pile7 = new Pile ( );

    Pile SpadesFoundation = new Pile ( );
    Pile DiamondsFoundation = new Pile ( );
    Pile ClubsFoundation = new Pile ( );
    Pile HeartsFoundation = new Pile ( );

    private boolean reveal ( ) {
        boolean flag = false;
        try {
            flag = this.Pile1.top ( ).faceUp ( );
        }
        catch ( EmptyStackException e ) {
            assert true;
        }
        try {
            flag |= this.Pile2.top ( ).faceUp ( );
        }
        catch ( EmptyStackException e ) {
            assert true;
        }
        try {
            flag |= this.Pile3.top ( ).faceUp ( );
        }
        catch ( EmptyStackException e ) {
            assert true;
        }
        try {
            flag |= this.Pile4.top ( ).faceUp ( );
        }
        catch ( EmptyStackException e ) {
            assert true;
        }
        try {
            flag |= this.Pile5.top ( ).faceUp ( );
        }
        catch ( EmptyStackException e ) {
            assert true;
        }
        try {
            flag |= this.Pile6.top ( ).faceUp ( );
        }
        catch ( EmptyStackException e ) {
            assert true;
        }
        try {
            flag |= this.Pile7.top ( ).faceUp ( );
        }
        catch ( EmptyStackException e ) {
            assert true;
        }

        return flag;
    }

    private Pile PileNr ( int n ) throws UnknownPileException {
        if ( n < 0 || n > 11 )
            throw new UnknownPileException ( );

        switch ( n ) {
            case 0 -> {
                return this.Waste;
            }
            case 1 -> {
                return this.Pile1;
            }
            case 2 -> {
                return this.Pile2;
            }
            case 3 -> {
                return this.Pile3;
            }
            case 4 -> {
                return this.Pile4;
            }
            case 5 -> {
                return this.Pile5;
            }
            case 6 -> {
                return this.Pile6;
            }
            case 7 -> {
                return this.Pile7;
            }
            case 8 -> {
                return this.SpadesFoundation;
            }
            case 9 -> {
                return this.DiamondsFoundation;
            }
            case 10 -> {
                return this.ClubsFoundation;
            }
            default -> {
                return this.HeartsFoundation;
            }
        }
    }

    private boolean validMove ( Pile source, Pile destination, int quantity ) {
        if ( destination == this.SpadesFoundation )
            return quantity == 1 && source.top ( ).suite == CardSuite.SPADES && ( ( destination.empty ( ) && source.top ( ).number == CardNumber.ACE ) || source.top ( ).number.ordinal ( ) == destination.top ( ).number.ordinal ( ) + 1 );
        else if ( destination == this.DiamondsFoundation )
            return quantity == 1 && source.top ( ).suite == CardSuite.DIAMONDS && ( ( destination.empty ( ) && source.top ( ).number == CardNumber.ACE ) || source.top ( ).number.ordinal ( ) == destination.top ( ).number.ordinal ( ) + 1 );
        else if ( destination == this.ClubsFoundation )
            return quantity == 1 && source.top ( ).suite == CardSuite.CLUBS && ( ( destination.empty ( ) && source.top ( ).number == CardNumber.ACE ) || source.top ( ).number.ordinal ( ) == destination.top ( ).number.ordinal ( ) + 1 );
        else if ( destination == this.HeartsFoundation )
            return quantity == 1 && source.top ( ).suite == CardSuite.HEARTS && ( ( destination.empty ( ) && source.top ( ).number == CardNumber.ACE ) || source.top ( ).number.ordinal ( ) == destination.top ( ).number.ordinal ( ) + 1 );
        else if ( destination.empty ( ) )
            return quantity <= source.size ( ) && source.CardAt ( quantity - 1 ).faceUp && source.CardAt ( quantity - 1 ).number == CardNumber.KING;
        else
            return quantity <= source.size ( ) && source.CardAt ( quantity - 1 ).faceUp && source.CardAt ( quantity - 1 ).color != destination.top ( ).color && source.CardAt ( quantity - 1 ).number.ordinal ( ) + 1 == destination.top ( ).number.ordinal ( );
    }

    private void rememberMove ( Move move ) {
        this.Moves.push ( move );
    }

    private void undoMove ( Move move ) {
        if ( move.revealedCard )
            move.source.top ( ).faceDown ( );

        Pile aux = new Pile ( );

        for ( int i = 0; i < move.nrOfCards; i++ )
            aux.push ( move.destination.pop ( ) );

        for ( int i = 0; i < move.nrOfCards; i++ )
            move.source.push ( aux.pop ( ) );

    }

    private void undoBrowse ( Move move ) {
        if ( move.nrOfCards == 0 ) {
            Pile aux;
            aux = this.Stock;
            this.Stock = this.Waste;
            this.Waste = aux;
            this.Waste.unTurnPile ( );
        }
        else {
            for ( int i = 0; i < move.nrOfCards; i++ ) {
                this.Waste.top ( ).faceDown ( );
                this.Stock.push ( this.Waste.pop ( ) );
            }
        }
    }

    public Board ( ) {

        for ( CardSuite cardSuit : CardSuite.values ( ) )
            for ( CardNumber cardNumber : CardNumber.values ( ) )
                this.Stock.push ( new Card ( cardNumber, cardSuit ) );

    }

    private void gatherCards ( ) {

        while ( !Waste.empty ( ) )
            Stock.push ( Waste.pop ( ) );

        while ( !Pile1.empty ( ) )
            Stock.push ( Pile1.pop ( ) );

        while ( !Pile2.empty ( ) )
            Stock.push ( Pile2.pop ( ) );

        while ( !Pile3.empty ( ) )
            Stock.push ( Pile3.pop ( ) );

        while ( !Pile4.empty ( ) )
            Stock.push ( Pile4.pop ( ) );

        while ( !Pile5.empty ( ) )
            Stock.push ( Pile5.pop ( ) );

        while ( !Pile6.empty ( ) )
            Stock.push ( Pile6.pop ( ) );

        while ( !Pile7.empty ( ) )
            Stock.push ( Pile7.pop ( ) );

        while ( !SpadesFoundation.empty ( ) )
            Stock.push ( SpadesFoundation.pop ( ) );

        while ( !DiamondsFoundation.empty ( ) )
            Stock.push ( DiamondsFoundation.pop ( ) );

        while ( !ClubsFoundation.empty ( ) )
            Stock.push ( ClubsFoundation.pop ( ) );

        while ( !HeartsFoundation.empty ( ) )
            Stock.push ( HeartsFoundation.pop ( ) );

        for ( int i = 0; i < 52; i++ )
            Stock.CardAt ( 0 ).faceDown ( );
    }

    public void newGame ( ) {
        if ( Stock.size ( ) != 52 )
            gatherCards ( );

        Collections.shuffle ( this.Stock );

        this.Pile1.push ( this.Stock.pop ( ) );

        for ( int i = 0; i < 2; i++ )
            this.Pile2.push ( this.Stock.pop ( ) );

        for ( int i = 0; i < 3; i++ )
            this.Pile3.push ( this.Stock.pop ( ) );

        for ( int i = 0; i < 4; i++ )
            this.Pile4.push ( this.Stock.pop ( ) );

        for ( int i = 0; i < 5; i++ )
            this.Pile5.push ( this.Stock.pop ( ) );

        for ( int i = 0; i < 6; i++ )
            this.Pile6.push ( this.Stock.pop ( ) );

        for ( int i = 0; i < 7; i++ )
            this.Pile7.push ( this.Stock.pop ( ) );

        this.reveal ( );
    }

    public void printBoard ( ) {

        System.out.println ( "Stock: " + this.Stock.size ( ) );
        System.out.print ( "0.Waste: " );
        this.Waste.printPile ( 3 );
        System.out.print ( "1.Pile: " );
        this.Pile1.printPile ( 0 );
        System.out.print ( "2.Pile: " );
        this.Pile2.printPile ( 0 );
        System.out.print ( "3.Pile: " );
        this.Pile3.printPile ( 0 );
        System.out.print ( "4.Pile: " );
        this.Pile4.printPile ( 0 );
        System.out.print ( "5.Pile: " );
        this.Pile5.printPile ( 0 );
        System.out.print ( "6.Pile: " );
        this.Pile6.printPile ( 0 );
        System.out.print ( "7.Pile: " );
        this.Pile7.printPile ( 0 );
        System.out.print ( "8.Spades: " );
        this.SpadesFoundation.printPile ( 0 );
        System.out.print ( "9.Diamonds: " );
        this.DiamondsFoundation.printPile ( 0 );
        System.out.print ( "10.Clubs: " );
        this.ClubsFoundation.printPile ( 0 );
        System.out.print ( "11.Hearts: " );
        this.HeartsFoundation.printPile ( 0 );
        System.out.println ( );

    }

    public void move ( int s, int d, int quantity ) throws InvalidMoveException {

        if ( d == 0 )
            throw new InvalidMoveException ( );

        Pile source;
        Pile destination;

        try {
            source = this.PileNr ( s );
            destination = this.PileNr ( d );
        }
        catch ( UnknownPileException e ) {
            throw new InvalidMoveException ( );
        }


        if ( this.validMove ( source, destination, quantity ) ) {

            Pile aux = new Pile ( );

            for ( int i = 0; i < quantity; i++ )
                aux.push ( source.pop ( ) );

            for ( int i = 0; i < quantity; i++ )
                destination.push ( aux.pop ( ) );

            Move move = new Move ( this.reveal ( ), source, destination, quantity );
            this.rememberMove ( move );
        }
        else {
            throw new InvalidMoveException ( );
        }

        printBoard ();
    }

    public int browse ( ) {
        if ( this.Stock.empty ( ) && this.Waste.empty ( ) ) {
            printBoard ();
            return -1;
        }
        else if ( this.Stock.empty ( ) ) {
            Pile aux;
            aux = this.Stock;
            this.Stock = this.Waste;
            this.Waste = aux;
            this.Stock.turnPile ( );
            Move move = new Move ( 0 );
            this.rememberMove ( move );

            printBoard ();
            return 0;
        }
        else {
            int i = 0;
            Card aux;
            while ( !this.Stock.empty ( ) && i < 3 ) {
                aux = this.Stock.pop ( );
                aux.faceUp ( );
                this.Waste.push ( aux );
                i++;
            }

            Move move = new Move ( i );
            this.rememberMove ( move );

            printBoard ();
            return 1;
        }
    }

    public void undo ( ) {
        if ( this.Moves.empty ( ) )
            return;

        Move lastMove = this.Moves.pop ( );

        if ( lastMove.browseMove )
            this.undoBrowse ( lastMove );
        else
            this.undoMove ( lastMove );
    }

    public boolean checkForWin ( ) {
        return this.ClubsFoundation.size ( ) == 13 &&
                this.SpadesFoundation.size ( ) == 13 &&
                this.DiamondsFoundation.size ( ) == 13 &&
                this.HeartsFoundation.size ( ) == 13;
    }

}
