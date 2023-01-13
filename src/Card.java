public class Card {
    final CardNumber number;
    final CardSuite suite;
    final CardColor color;
    boolean faceUp;

    Solitaire.CardPanel cardPanel;

    public Card ( CardNumber number, CardSuite suite ) {
        this.suite = suite;
        this.number = number;
        if ( this.suite == CardSuite.HEARTS || this.suite == CardSuite.DIAMONDS )
            this.color = CardColor.RED;
        else
            this.color = CardColor.BLACK;
    }

    public boolean faceUp ( ) {
        boolean prev = this.faceUp;
        this.faceUp = true;
        return !prev;
    }

    public void faceDown ( ) {
        this.faceUp = false;
    }
}