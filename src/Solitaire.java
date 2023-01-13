import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Solitaire extends JFrame {

    public static class CardPanel extends JPanel {

        private static final int cardWidth;
        private static final int cardHeight;
        private static final int partiallyCoveredCardHeight;

        private final Card card;
        private CardCoverState covered;

        private final JLabel smallSuite;
        private final JLabel bigSuite;
        private final JLabel number;

        private final JPanel upperPanel;

        private final JPanel lowerPanel;

        static {
            Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
            cardWidth = Math.round ( screenSize.width * 0.0296875f );
            cardHeight = Math.round ( screenSize.height * 0.0722222f );
            partiallyCoveredCardHeight = Math.round ( screenSize.height * 0.0722222f / 5f );
        }

        CardPanel ( Card correspondingCard ) {
            super ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            card = correspondingCard;
            covered = CardCoverState.COVERED;
            upperPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            lowerPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            correspondingCard.cardPanel = this;

            setBorder ( new LineBorder ( Color.WHITE, 3 ) );
            setPreferredSize ( new Dimension ( cardWidth, cardHeight ) );
            setVisible ( false );

            upperPanel.setPreferredSize ( new Dimension ( cardWidth, partiallyCoveredCardHeight ) );
            lowerPanel.setPreferredSize ( new Dimension ( cardWidth, partiallyCoveredCardHeight * 4 ) );

            smallSuite = new JLabel ( );
            bigSuite = new JLabel ( );
            number = new JLabel ( );

            switch ( card.number ) {
                case ACE -> number.setText ( "A" );
                case TWO -> number.setText ( "2" );
                case THREE -> number.setText ( "3" );
                case FOUR -> number.setText ( "4" );
                case FIVE -> number.setText ( "5" );
                case SIX -> number.setText ( "6" );
                case SEVEN -> number.setText ( "7" );
                case EIGHT -> number.setText ( "8" );
                case NINE -> number.setText ( "9" );
                case TEN -> number.setText ( "10" );
                case JACK -> number.setText ( "J" );
                case QUEEN -> number.setText ( "Q" );
                case KING -> number.setText ( "K" );
            }

            switch ( card.suite ) {
                case SPADES -> {
                    smallSuite.setText ( "♠" );
                    bigSuite.setText ( "♠" );
                }
                case DIAMONDS -> {
                    smallSuite.setText ( "♦" );
                    bigSuite.setText ( "♦" );
                }
                case CLUBS -> {
                    smallSuite.setText ( "♣" );
                    bigSuite.setText ( "♣" );
                }
                case HEARTS -> {
                    smallSuite.setText ( "♥" );
                    bigSuite.setText ( "♥" );
                }
            }

            if ( card.color == CardColor.RED ) {
                smallSuite.setForeground ( Color.RED );
                bigSuite.setForeground ( Color.RED );
                number.setForeground ( Color.RED );
            }

            smallSuite.setPreferredSize ( new Dimension ( cardWidth / 2, partiallyCoveredCardHeight ) );
            number.setPreferredSize ( new Dimension ( cardWidth / 2, partiallyCoveredCardHeight ) );
            bigSuite.setPreferredSize ( new Dimension ( cardWidth, partiallyCoveredCardHeight * 4 ) );

            upperPanel.add ( number );
            upperPanel.add ( smallSuite );
            lowerPanel.add ( bigSuite );

            add ( upperPanel );
            add ( lowerPanel );
        }

        public void updateCardPanel ( ) {
            if ( card.faceUp ) {
                setBorder ( null );
                setBackground ( Color.BLACK );
                upperPanel.setVisible ( true );

                if ( covered == CardCoverState.COVERED ) {
                    setVisible ( false );
                }
                else if ( covered == CardCoverState.PARTIALLY_COVERED ) {
                    setVisible ( true );
                    lowerPanel.setVisible ( false );
                    setPreferredSize ( new Dimension ( cardWidth, partiallyCoveredCardHeight ) );
                    setBorder ( BorderFactory.createMatteBorder ( 1, 1, 0, 1, Color.GRAY ) );
                }
                else {
                    setVisible ( true );
                    lowerPanel.setVisible ( true );
                    setPreferredSize ( new Dimension ( cardWidth, cardHeight ) );
                    setBorder ( BorderFactory.createLineBorder ( Color.GRAY, 1 ) );
                }
            }
            else {
                setBackground ( Color.BLUE );
                upperPanel.setVisible ( false );
                lowerPanel.setVisible ( false );

                if ( covered == CardCoverState.COVERED ) {
                    setVisible ( false );
                }
                else if ( covered == CardCoverState.PARTIALLY_COVERED ) {
                    setVisible ( true );
                    setPreferredSize ( new Dimension ( cardWidth, partiallyCoveredCardHeight ) );
                    setBorder ( BorderFactory.createMatteBorder ( 3, 3, 0, 3, Color.WHITE ) );
                }
                else {
                    setVisible ( true );
                    setPreferredSize ( new Dimension ( cardWidth, cardHeight ) );
                    setBorder ( BorderFactory.createLineBorder ( Color.WHITE, 3 ) );
                }
            }
        }
    }

    private class StockPanel extends JPanel {
        int nrOfCards;
        JPanel cardPanel;

        StockPanel ( JButton browse ) {
            super ( new FlowLayout ( FlowLayout.CENTER, 0, -3 ) );
            nrOfCards = board.Stock.size ( );
            cardPanel = new JPanel ( );

            setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
            setBackground ( new Color ( 0x2b7b3b ) );
            setBorder ( BorderFactory.createLineBorder ( new Color ( 0x34a249 ), 3 ) );

            cardPanel.setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
            cardPanel.setBorder ( BorderFactory.createLineBorder ( Color.WHITE, 3 ) );
            cardPanel.setBackground ( Color.BLUE );
            cardPanel.setLayout ( new FlowLayout ( FlowLayout.CENTER, 0, -3 ) );

            browse.setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
            browse.setOpaque ( false );
            browse.setContentAreaFilled ( false );
            browse.setBorderPainted ( false );
            browse.setFocusable ( false );

            browse.addActionListener ( e -> visualBrowse ( ) );

            cardPanel.add ( browse );

            add ( cardPanel );
        }


    }

    private class PilePanel extends JPanel {

        int nrOfCards;
        final Board.Pile pile;

        CardPanel cardPanel1 = null;
        CardPanel cardPanel2 = null;
        CardPanel cardPanel3 = null;

        public void pack ( ) {
            if ( getComponentCount ( ) < 2 )
                setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
            else if ( getComponentCount ( ) == 2 )
                setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight + CardPanel.partiallyCoveredCardHeight ) );
            else
                setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight + CardPanel.partiallyCoveredCardHeight * 2 ) );
        }

        PilePanel ( Board.Pile correspondingPile ) {
            super ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            nrOfCards = correspondingPile.size ( );
            pile = correspondingPile;

            pack ( );
        }

    }

    private Board board;

    private final JPanel topLevelPanel;
    private final JPanel topPanel;
    private final JPanel leftPanel;
    private final JPanel middlePanel;
    private final JPanel rightPanel;
    private final CardPanel[] cardPanels;
    private final StockPanel stockPanel;
    private final PilePanel wastePanel;

    private final JButton browse = new JButton ( );

    private CardPanel[] cardPanelsBuilder ( ) {
        CardPanel[] deck = new CardPanel[ 52 ];

        for ( int i = 0; i < 52; i++ )
            deck[ i ] = new CardPanel ( board.Stock.CardAt ( i ) );

        return deck;
    }

    private PilePanel wastePanelBuilder ( ) {
        PilePanel wastePanel = new PilePanel ( board.Waste );

        wastePanel.setBackground ( new Color ( 0x2b7b3b ) );
        wastePanel.setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
        leftPanel.add ( wastePanel );

        return wastePanel;
    }

    private StockPanel stockPanelBuilder ( JButton browse ) {
        StockPanel newStockPanel = new StockPanel ( browse );

        leftPanel.add ( newStockPanel );

        return newStockPanel;
    }

    private JPanel[] mainPanelsBuilder ( ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int initialFrameWidth = Math.round ( screenSize.width * 2f / 5f );
        int initialFrameHeight = Math.round ( screenSize.height * 2f / 5f );

        JPanel topPanel = new JPanel ( );
        JPanel leftPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 20, 30 ) );
        JPanel middlePanel = new JPanel ( new SpringLayout ( ) );
        JPanel rightPanel = new JPanel ( new FlowLayout ( ) );

        topPanel.setBackground ( new Color ( 0x313131 ) );
        leftPanel.setBackground ( new Color ( 0x2b7b3b ) );
        middlePanel.setBackground ( new Color ( 0x34a249 ) );
        rightPanel.setBackground ( new Color ( 0x2b7b3b ) );

        topPanel.setPreferredSize ( new Dimension ( initialFrameWidth, Math.round ( initialFrameHeight * 10f / 100f + 20 ) ) );
        leftPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameWidth * 10f / 100f ), Math.round ( initialFrameHeight * 90f / 100f ) ) );
        middlePanel.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 10f / 100f ) * 2 ), Math.round ( initialFrameHeight * 90f / 100f ) ) );
        rightPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameWidth * 10f / 100f ), Math.round ( initialFrameHeight * 90f / 100f ) ) );

        topLevelPanel.add ( topPanel, BorderLayout.NORTH );
        topLevelPanel.add ( leftPanel, BorderLayout.WEST );
        topLevelPanel.add ( middlePanel, BorderLayout.CENTER );
        topLevelPanel.add ( rightPanel, BorderLayout.EAST );

        return new JPanel[]{topPanel, leftPanel, middlePanel, rightPanel};
    }

    private JPanel topLevelFieldBuilder ( ) {

        JPanel newTopLevelPanel = new JPanel ( new BorderLayout ( ) );

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int initialPanelWidth = Math.round ( screenSize.width * 2f / 5f );
        int initialPanelHeight = Math.round ( screenSize.height * 2f / 5f );

        newTopLevelPanel.setBackground ( Color.MAGENTA );
        newTopLevelPanel.setPreferredSize ( new Dimension ( initialPanelWidth, initialPanelHeight ) );
        newTopLevelPanel.setMinimumSize ( new Dimension ( initialPanelWidth / 2, initialPanelHeight / 2 ) );
        add ( newTopLevelPanel, BorderLayout.CENTER );

        return newTopLevelPanel;
    }

    private void frameSetUp ( ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );

        int initialFrameWidth = Math.round ( screenSize.width * 2f / 5f );
        int initialFrameHeight = Math.round ( screenSize.height * 2f / 5f );
        int initialFrameXCoordinate = Math.round ( ( screenSize.width - initialFrameWidth ) / 2f );
        int initialFrameYCoordinate = Math.round ( ( screenSize.height - initialFrameHeight ) / 2f );

        setMinimumSize ( new Dimension ( initialFrameWidth * 2 / 3, initialFrameHeight * 2 / 3 ) );
        setLocation ( new Point ( initialFrameXCoordinate, initialFrameYCoordinate ) );
        setTitle ( "Solitaire" );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

    }

    public Solitaire ( ) {
        board = new Board ( );

        cardPanels = cardPanelsBuilder ( );

        board.newGame ( );

        frameSetUp ( );

        topLevelPanel = topLevelFieldBuilder ( );

        JPanel[] mainPanels = mainPanelsBuilder ( );

        topPanel = mainPanels[ 0 ];
        leftPanel = mainPanels[ 1 ];
        middlePanel = mainPanels[ 2 ];
        rightPanel = mainPanels[ 3 ];

        stockPanel = stockPanelBuilder ( browse );

        wastePanel = wastePanelBuilder ( );

        pack ( );
        setVisible ( true );
    }

    void visualBrowse ( ) {

        wastePanel.removeAll ( );

        if ( board.browse ( ) < 1 ) {

            wastePanel.cardPanel1 = null;
            wastePanel.cardPanel2 = null;
            wastePanel.cardPanel3 = null;
        }
        else {
            switch ( board.Waste.size ( ) ) {
                case 1 -> {
                    wastePanel.cardPanel1 = board.Waste.top ( ).cardPanel;
                    wastePanel.cardPanel2 = null;
                    wastePanel.cardPanel3 = null;

                    wastePanel.cardPanel1.covered = CardCoverState.UNCOVERED;

                    wastePanel.cardPanel1.updateCardPanel ( );

                    wastePanel.add ( wastePanel.cardPanel1 );
                }
                case 2 -> {
                    wastePanel.cardPanel1 = board.Waste.CardAt ( 1 ).cardPanel;
                    wastePanel.cardPanel2 = board.Waste.top ( ).cardPanel;
                    wastePanel.cardPanel3 = null;

                    wastePanel.cardPanel1.covered = CardCoverState.PARTIALLY_COVERED;
                    wastePanel.cardPanel2.covered = CardCoverState.UNCOVERED;

                    wastePanel.cardPanel1.updateCardPanel ( );
                    wastePanel.cardPanel2.updateCardPanel ( );

                    wastePanel.add ( wastePanel.cardPanel1 );
                    wastePanel.add ( wastePanel.cardPanel2 );
                }
                default -> {
                    wastePanel.cardPanel1 = board.Waste.CardAt ( 2 ).cardPanel;
                    wastePanel.cardPanel2 = board.Waste.CardAt ( 1 ).cardPanel;
                    wastePanel.cardPanel3 = board.Waste.top ( ).cardPanel;

                    wastePanel.cardPanel1.covered = CardCoverState.PARTIALLY_COVERED;
                    wastePanel.cardPanel2.covered = CardCoverState.PARTIALLY_COVERED;
                    wastePanel.cardPanel3.covered = CardCoverState.UNCOVERED;

                    wastePanel.cardPanel1.updateCardPanel ( );
                    wastePanel.cardPanel2.updateCardPanel ( );
                    wastePanel.cardPanel3.updateCardPanel ( );

                    wastePanel.add ( wastePanel.cardPanel1 );
                    wastePanel.add ( wastePanel.cardPanel2 );
                    wastePanel.add ( wastePanel.cardPanel3 );
                }
            }
        }

        stockPanel.cardPanel.setVisible ( board.Waste.size ( ) != 0 );

        wastePanel.pack ( );

        wastePanel.repaint ( );
        wastePanel.revalidate ( );
    }
}
