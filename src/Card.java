public class Card {
    final CardNumber number;
    final CardSuite suite;
    final CardColor color;
    boolean faceUp;

    CardCoverState state;

    Solitaire.CardPanel cardPanel;

    public Card ( CardNumber number, CardSuite suite ) {
        this.suite = suite;
        this.number = number;
        if ( this.suite == CardSuite.HEARTS || this.suite == CardSuite.DIAMONDS )
            this.color = CardColor.RED;
        else
            this.color = CardColor.BLACK;
        state = CardCoverState.COVERED;
    }

    public boolean faceUp ( ) {
        boolean prev = this.faceUp;
        this.faceUp = true;
        return !prev;
    }

    public void faceDown ( ) {
        this.faceUp = false;
    }

    public void setState ( CardCoverState newState ) {
        state = newState;
    }
}