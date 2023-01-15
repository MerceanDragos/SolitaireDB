import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Solitaire extends JFrame implements MouseListener {

    public static class CardPanel extends JPanel {

        private static final int cardWidth;
        private static final int cardHeight;
        private static final int partiallyCoveredCardHeight;

        private final Card card;

        private final JPanel upperPanel;
        private final JPanel lowerPanel;

        static {
            Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
            cardWidth = Math.round ( screenSize.width * 0.0296875f );
            cardHeight = Math.round ( screenSize.height * 0.0722222f );
            partiallyCoveredCardHeight = Math.round ( screenSize.height * 0.0722222f / 4f );
        }

        CardPanel ( Card correspondingCard, MouseListener newMouseListener ) {
            super ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            card = correspondingCard;
            upperPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            lowerPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            correspondingCard.setCardPanel ( this );

            setVisible ( false );

            upperPanel.setPreferredSize ( new Dimension ( cardWidth - 2, partiallyCoveredCardHeight - 1 ) );
            lowerPanel.setPreferredSize ( new Dimension ( cardWidth - 2, partiallyCoveredCardHeight * 3 - 1 ) );

            upperPanel.setBackground ( Color.WHITE );
            lowerPanel.setBackground ( Color.WHITE );

            JLabel smallSuite = new JLabel ( );
            JLabel bigSuite = new JLabel ( );
            JLabel number = new JLabel ( );

            smallSuite.setHorizontalAlignment ( JLabel.CENTER );
            smallSuite.setVerticalAlignment ( JLabel.CENTER );
            bigSuite.setHorizontalAlignment ( JLabel.CENTER );
            bigSuite.setVerticalAlignment ( JLabel.BOTTOM );
            number.setHorizontalAlignment ( JLabel.CENTER );
            number.setVerticalAlignment ( JLabel.CENTER );

            smallSuite.setFont ( new Font ( null, Font.BOLD, 25 ) );
            bigSuite.setFont ( new Font ( null, Font.BOLD, 70 ) );
            number.setFont ( new Font ( null, Font.BOLD, 20 ) );

            smallSuite.setPreferredSize ( new Dimension ( cardWidth / 2 - 2, partiallyCoveredCardHeight - 1 ) );
            number.setPreferredSize ( new Dimension ( cardWidth / 2 - 2, partiallyCoveredCardHeight - 1 ) );
            bigSuite.setPreferredSize ( new Dimension ( cardWidth - 2, partiallyCoveredCardHeight * 3 - 1 ) );

            switch ( card.getNumber ( ) ) {
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

            switch ( card.getSuite ( ) ) {
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

            if ( card.getColor ( ) == CardColor.RED ) {
                smallSuite.setForeground ( Color.RED );
                bigSuite.setForeground ( Color.RED );
                number.setForeground ( Color.RED );
            }

            upperPanel.add ( number );
            upperPanel.add ( smallSuite );
            lowerPanel.add ( bigSuite );

            addMouseListener ( newMouseListener );
            add ( upperPanel );
            add ( lowerPanel );
        }

        public void updateVisual ( ) {
            if ( card.isFaceUp ( ) ) {
                if ( card.getState ( ) == CardCoverState.COVERED )
                    setVisible ( false );
                else if ( card.getState ( ) == CardCoverState.PARTIALLY_COVERED ) {
                    setVisible ( true );
                    setBackground ( Color.WHITE );
                    setBorder ( BorderFactory.createMatteBorder ( 1, 1, 0, 1, Color.LIGHT_GRAY ) );
                    setPreferredSize ( new Dimension ( cardWidth, partiallyCoveredCardHeight ) );
                    upperPanel.setVisible ( true );
                    lowerPanel.setVisible ( false );
                }
                else {
                    setVisible ( true );
                    setBackground ( Color.WHITE );
                    setBorder ( BorderFactory.createLineBorder ( Color.LIGHT_GRAY, 1 ) );
                    setPreferredSize ( new Dimension ( cardWidth, cardHeight ) );
                    upperPanel.setVisible ( true );
                    lowerPanel.setVisible ( true );
                }
            }
            else {
                if ( card.getState ( ) == CardCoverState.COVERED ) {
                    setVisible ( false );
                }
                else if ( card.getState ( ) == CardCoverState.PARTIALLY_COVERED ) {
                    setVisible ( true );
                    setBackground ( Color.BLUE );
                    setBorder ( BorderFactory.createMatteBorder ( 3, 3, 0, 3, Color.WHITE ) );
                    setPreferredSize ( new Dimension ( cardWidth, partiallyCoveredCardHeight ) );
                    upperPanel.setVisible ( false );
                    lowerPanel.setVisible ( false );
                }
                else {
                    setVisible ( true );
                    setBackground ( Color.BLUE );
                    setBorder ( BorderFactory.createLineBorder ( Color.WHITE, 3 ) );
                    setPreferredSize ( new Dimension ( cardWidth, cardHeight ) );
                    upperPanel.setVisible ( false );
                    lowerPanel.setVisible ( false );
                }
            }
        }
    }

    public class VerticalPilePanel extends JPanel {
        final Board.Pile pile;
        final MouseListener mouseListener;

        VerticalPilePanel ( Board.Pile correspondingPile, MouseListener newMouseListener ) {
            super ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );

            pile = correspondingPile;
            correspondingPile.pilePanel = this;

            setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
            setBackground ( new Color ( 0x2b7b3b ) );

            mouseListener = newMouseListener;
            addMouseListener ( mouseListener );

            addAllCards ( correspondingPile );
        }

        private void removeAllCards ( ) {
            while ( getComponentCount ( ) > 1 )
                remove ( getComponentCount ( ) - 1 );
        }

        private void addAllCards ( Board.Pile pile ) {
            for ( int i = pile.size ( ) - 1; i >= 0; i-- )
                add ( pile.CardAt ( i ).getCardPanel ( ) );
        }

        public void updatePanel ( ) {
            removeAllCards ( );
            if ( this == foundationPanels[0] || this == foundationPanels[1] || this == foundationPanels[2] || this == foundationPanels[3] )
                getComponent ( getComponentCount ( ) - 1 ).setVisible ( pile.size ( ) == 0 );
            addAllCards ( pile );
            if ( getComponentCount ( ) == 1 )
                setBorder ( BorderFactory.createLineBorder ( new Color ( 0x34a249 ), 3 ) );
            else
                setBorder ( null );
            revalidate ( );
            repaint ( );
        }
    }

    public class HorizontalPilePanel extends JPanel {
        final Board.Pile pile;
        final MouseListener mouseListener;

        HorizontalPilePanel ( Board.Pile correspondingPile, MouseListener newMouseListener ) {
            super ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
            pile = correspondingPile;
            mouseListener = newMouseListener;
            addMouseListener ( newMouseListener );
            correspondingPile.pilePanel = this;
        }

        public void pack ( ) {
            setPreferredSize ( new Dimension ( CardPanel.cardWidth, ( Math.max ( getComponentCount ( ) - 1, 0 ) ) * CardPanel.partiallyCoveredCardHeight + CardPanel.cardHeight ) );
        }

        private void addAllCards ( Board.Pile pile ) {
            for ( int i = pile.size ( ) - 1; i >= 0; i-- )
                add ( pile.CardAt ( i ).getCardPanel ( ) );
        }

        public void updatePanel ( ) {
            removeAll ( );
            addAllCards ( pile );
            pack ( );
            if ( getComponentCount ( ) == 0 )
                setBorder ( BorderFactory.createLineBorder ( new Color ( 0x2b7b3b ), 3 ) );
            else
                setBorder ( null );
            revalidate ( );
            repaint ( );
        }

    }

    @Override
    public void mouseClicked ( MouseEvent event ) {

        if ( event.getSource ( ) == controlPanels[0] ) {
            board.undo ( );

            stockPanel.updatePanel ( );
            wastePanel.updatePanel ( );

            for ( int i = 0; i < 7; i++ )
                pilePanels[i].updatePanel ( );

            for ( int i = 0; i < 4; i++ )
                foundationPanels[i].updatePanel ( );
        }
        else if ( event.getSource ( ) == controlPanels[1] ) {
            board.newGame ( );

            stockPanel.updatePanel ( );
            wastePanel.updatePanel ( );

            for ( int i = 0; i < 7; i++ )
                pilePanels[i].updatePanel ( );

            for ( int i = 0; i < 4; i++ )
                foundationPanels[i].updatePanel ( );
        }
        else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == stockPanel || event.getSource ( ) == stockPanel ) {
            board.browse ( );
            stockPanel.updatePanel ( );
            wastePanel.updatePanel ( );
        }
        else if ( source != null ) {
            if ( event.getSource ( ) == middlePanel || event.getSource ( ) == leftPanel || event.getSource ( ) == rightPanel || event.getSource ( ) == topPanel )
                source = null;
            else {
                try {
                    Board.Pile destination;
                    if ( event.getSource ( ).getClass ( ).getName ( ).equals ( "Solitaire$CardPanel" ) ) {
                        if ( ( ( CardPanel ) event.getSource ( ) ).getParent ( ).getParent ( ) == middlePanel || ( ( CardPanel ) event.getSource ( ) ).getParent ( ).getParent ( ) == leftPanel )
                            destination = ( ( HorizontalPilePanel ) ( ( CardPanel ) event.getSource ( ) ).getParent ( ) ).pile;
                        else
                            destination = ( ( VerticalPilePanel ) ( ( CardPanel ) event.getSource ( ) ).getParent ( ) ).pile;
                    }
                    else {
                        if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) != rightPanel )
                            destination = ( ( HorizontalPilePanel ) event.getSource ( ) ).pile;
                        else
                            destination = ( ( VerticalPilePanel ) event.getSource ( ) ).pile;
                    }

                    if ( source == destination )
                        source = null;
                    else {
                        board.move ( source, destination, quantity );
                        if ( source.pilePanel == foundationPanels[0] || source.pilePanel == foundationPanels[1] || source.pilePanel == foundationPanels[2] || source.pilePanel == foundationPanels[3] )
                            ( ( VerticalPilePanel ) source.pilePanel ).updatePanel ( );
                        else
                            ( ( HorizontalPilePanel ) source.pilePanel ).updatePanel ( );
                        if ( destination.pilePanel == foundationPanels[0] || destination.pilePanel == foundationPanels[1] || destination.pilePanel == foundationPanels[2] || destination.pilePanel == foundationPanels[3] )
                            ( ( VerticalPilePanel ) destination.pilePanel ).updatePanel ( );
                        else
                            ( ( HorizontalPilePanel ) destination.pilePanel ).updatePanel ( );
                    }
                }
                catch ( Board.InvalidMoveException exception ) {
                    System.out.println ( "Invalid move" );
                }
                finally {
                    source = null;
                }
            }

            //if ( board.checkForWin ( ) )

        }
        else if ( event.getSource ( ) != middlePanel && event.getSource ( ) != leftPanel && event.getSource ( ) != rightPanel && event.getSource ( ) != topPanel ) {
            if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == wastePanel )
                source = wastePanel.pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == pilePanels[0] )
                source = pilePanels[0].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == pilePanels[1] )
                source = pilePanels[1].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == pilePanels[2] )
                source = pilePanels[2].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == pilePanels[3] )
                source = pilePanels[3].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == pilePanels[4] )
                source = pilePanels[4].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == pilePanels[5] )
                source = pilePanels[5].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == pilePanels[6] )
                source = pilePanels[6].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == foundationPanels[0] )
                source = foundationPanels[0].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == foundationPanels[1] )
                source = foundationPanels[1].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == foundationPanels[2] )
                source = foundationPanels[2].pile;
            else if ( ( ( JPanel ) event.getSource ( ) ).getParent ( ) == foundationPanels[3] )
                source = foundationPanels[3].pile;

            if ( source != null )
                quantity = source.indexOf ( ( ( CardPanel ) event.getSource ( ) ).card ) + 1;
        }
    }

    @Override
    public void mouseEntered ( MouseEvent e ) {
        assert true;
    }

    @Override
    public void mouseExited ( MouseEvent e ) {
        assert true;
    }

    @Override
    public void mousePressed ( MouseEvent e ) {
        assert true;
    }

    @Override
    public void mouseReleased ( MouseEvent e ) {
        assert true;
    }

    final private Board board;

    private final JPanel topLevelPanel;
    private final JPanel topPanel;
    private final JPanel leftPanel;
    private final JPanel middlePanel;
    private final JPanel rightPanel;
    private final CardPanel[] cardPanels;
    private final HorizontalPilePanel[] pilePanels;
    private final VerticalPilePanel stockPanel;
    private final HorizontalPilePanel wastePanel;
    private final VerticalPilePanel[] foundationPanels;
    private final JPanel[] controlPanels;

    private Board.Pile source = null;
    private int quantity;

    private CardPanel[] cardPanelsBuilder ( ) {
        CardPanel[] deck = new CardPanel[52];

        for ( int i = 0; i < 52; i++ )
            deck[i] = new CardPanel ( board.Stock.CardAt ( i ), this );

        return deck;
    }

    private HorizontalPilePanel wastePanelBuilder ( ) {
        HorizontalPilePanel wastePanel = new HorizontalPilePanel ( board.Waste, null );

        wastePanel.setBackground ( new Color ( 0x2b7b3b ) );
        wastePanel.setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
        leftPanel.add ( wastePanel );

        return wastePanel;
    }

    private VerticalPilePanel stockPanelBuilder ( ) {
        VerticalPilePanel newStockPanel = new VerticalPilePanel ( this.board.Stock, this );

        leftPanel.add ( newStockPanel );

        return newStockPanel;
    }

    private HorizontalPilePanel[] pilePanelsBuilder ( ) {
        HorizontalPilePanel[] pilePanels = new HorizontalPilePanel[7];

        pilePanels[0] = new HorizontalPilePanel ( board.Pile1, this );
        pilePanels[1] = new HorizontalPilePanel ( board.Pile2, this );
        pilePanels[2] = new HorizontalPilePanel ( board.Pile3, this );
        pilePanels[3] = new HorizontalPilePanel ( board.Pile4, this );
        pilePanels[4] = new HorizontalPilePanel ( board.Pile5, this );
        pilePanels[5] = new HorizontalPilePanel ( board.Pile6, this );
        pilePanels[6] = new HorizontalPilePanel ( board.Pile7, this );

        for ( int i = 0; i < 7; i++ )
            pilePanels[i].setBackground ( new Color ( 0x34a249 ) );

        SpringLayout layout = ( SpringLayout ) middlePanel.getLayout ( );

        for ( int i = 0; i < 7; i++ )
            middlePanel.add ( pilePanels[i] );

        middlePanel.add ( pilePanels[0] );
        middlePanel.add ( pilePanels[1] );

        for ( int i = 0; i < 7; i++ )
            layout.putConstraint ( SpringLayout.NORTH, pilePanels[i], 40, SpringLayout.NORTH, middlePanel );

        for ( int i = 0; i < 7; i++ )
            layout.putConstraint ( SpringLayout.WEST, pilePanels[i], ( CardPanel.cardWidth + 30 ) * i + 30, SpringLayout.WEST, middlePanel );

        pilePanels[0].addAllCards ( board.Pile1 );
        pilePanels[1].addAllCards ( board.Pile2 );
        pilePanels[2].addAllCards ( board.Pile3 );
        pilePanels[3].addAllCards ( board.Pile4 );
        pilePanels[4].addAllCards ( board.Pile5 );
        pilePanels[5].addAllCards ( board.Pile6 );
        pilePanels[6].addAllCards ( board.Pile7 );

        for ( int i = 0; i < 7; i++ )
            pilePanels[i].pack ( );

        return pilePanels;
    }

    private VerticalPilePanel[] foundationPanelsBuilder ( ) {
        VerticalPilePanel[] foundationPanels = new VerticalPilePanel[4];

        foundationPanels[0] = new VerticalPilePanel ( board.SpadesFoundation, this );
        foundationPanels[1] = new VerticalPilePanel ( board.DiamondsFoundation, this );
        foundationPanels[2] = new VerticalPilePanel ( board.ClubsFoundation, this );
        foundationPanels[3] = new VerticalPilePanel ( board.HeartsFoundation, this );

        JLabel spadesLabel = new JLabel ( "♠" );
        JLabel diamondsLabel = new JLabel ( "♦" );
        JLabel clubsLabel = new JLabel ( "♣" );
        JLabel heartsLabel = new JLabel ( "♥" );

        spadesLabel.setForeground ( new Color ( 0x34a249 ) );
        diamondsLabel.setForeground ( new Color ( 0x34a249 ) );
        clubsLabel.setForeground ( new Color ( 0x34a249 ) );
        heartsLabel.setForeground ( new Color ( 0x34a249 ) );

        spadesLabel.setBackground ( new Color ( 0x2b7b3b ) );
        diamondsLabel.setBackground ( new Color ( 0x2b7b3b ) );
        clubsLabel.setBackground ( new Color ( 0x2b7b3b ) );
        heartsLabel.setBackground ( new Color ( 0x2b7b3b ) );

        spadesLabel.setPreferredSize ( new Dimension ( CardPanel.cardWidth - 6, CardPanel.cardHeight - 6 ) );
        diamondsLabel.setPreferredSize ( new Dimension ( CardPanel.cardWidth - 6, CardPanel.cardHeight - 6 ) );
        clubsLabel.setPreferredSize ( new Dimension ( CardPanel.cardWidth - 6, CardPanel.cardHeight - 6 ) );
        heartsLabel.setPreferredSize ( new Dimension ( CardPanel.cardWidth - 6, CardPanel.cardHeight - 6 ) );

        spadesLabel.setHorizontalAlignment ( JLabel.CENTER );
        diamondsLabel.setHorizontalAlignment ( JLabel.CENTER );
        clubsLabel.setHorizontalAlignment ( JLabel.CENTER );
        heartsLabel.setHorizontalAlignment ( JLabel.CENTER );

        spadesLabel.setVerticalAlignment ( JLabel.CENTER );
        diamondsLabel.setVerticalAlignment ( JLabel.CENTER );
        clubsLabel.setVerticalAlignment ( JLabel.CENTER );
        heartsLabel.setVerticalAlignment ( JLabel.CENTER );

        spadesLabel.setFont ( new Font ( null, Font.BOLD, 70 ) );
        diamondsLabel.setFont ( new Font ( null, Font.BOLD, 70 ) );
        clubsLabel.setFont ( new Font ( null, Font.BOLD, 70 ) );
        heartsLabel.setFont ( new Font ( null, Font.BOLD, 70 ) );

        foundationPanels[0].add ( spadesLabel );
        foundationPanels[1].add ( diamondsLabel );
        foundationPanels[2].add ( clubsLabel );
        foundationPanels[3].add ( heartsLabel );

        for ( int i = 0; i < 4; i++ ) {
            foundationPanels[i].setBackground ( new Color ( 0x2b7b3b ) );
            foundationPanels[i].setPreferredSize ( new Dimension ( CardPanel.cardWidth, CardPanel.cardHeight ) );
            foundationPanels[i].addMouseListener ( this );
            foundationPanels[i].setBorder ( BorderFactory.createLineBorder ( new Color ( 0x34a249 ), 3 ) );
            rightPanel.add ( foundationPanels[i] );
        }

        return foundationPanels;
    }

    private JPanel[] mainPanelsBuilder ( ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int initialFrameWidth = Math.round ( screenSize.width * 2f / 5f );
        int initialFrameHeight = Math.round ( screenSize.height * 2f / 5f );

        JPanel topPanel = new JPanel ( );
        JPanel leftPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 20, 30 ) );
        JPanel middlePanel = new JPanel ( new SpringLayout ( ) );
        JPanel rightPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 20, 10 ) );

        topPanel.setBackground ( new Color ( 0x313131 ) );
        leftPanel.setBackground ( new Color ( 0x2b7b3b ) );
        middlePanel.setBackground ( new Color ( 0x34a249 ) );
        rightPanel.setBackground ( new Color ( 0x2b7b3b ) );

        topPanel.addMouseListener ( this );
        middlePanel.addMouseListener ( this );
        leftPanel.addMouseListener ( this );
        rightPanel.addMouseListener ( this );

        topPanel.setPreferredSize ( new Dimension ( initialFrameWidth, Math.round ( initialFrameHeight * 10f / 100f + 20 ) ) );
        leftPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameWidth * 10f / 100f ), Math.round ( initialFrameHeight * 90f / 100f ) ) );
        middlePanel.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 10f / 100f ) * 2 ), Math.round ( initialFrameHeight * 90f / 100f ) ) );
        rightPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameWidth * 10f / 100f ), Math.round ( initialFrameHeight * 90f / 100f ) ) );

        topLevelPanel.add ( topPanel, BorderLayout.NORTH );
        topLevelPanel.add ( leftPanel, BorderLayout.WEST );
        topLevelPanel.add ( middlePanel, BorderLayout.CENTER );
        topLevelPanel.add ( rightPanel, BorderLayout.EAST );

        return new JPanel[]{ topPanel, leftPanel, middlePanel, rightPanel };
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

    private JPanel[] controlFieldsBuilder ( JPanel middlePanel ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int initialFrameWidth = Math.round ( screenSize.width * 2f / 5f );
        int initialFrameHeight = Math.round ( screenSize.height * 2f / 5f );

        JPanel controlsField = new JPanel ( new FlowLayout ( FlowLayout.LEFT, 0, 0 ) );
        controlsField.setBackground ( Color.BLUE );
        controlsField.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f ) + 1 ), Math.round ( initialFrameHeight * 90f / 600f ) ) );

        JLabel undoArrow = new JLabel ( "↶" );
        JLabel undoText = new JLabel ( "UNDO" );

        undoArrow.setForeground ( new Color ( 0x2b7b3b ) );
        undoText.setForeground ( new Color ( 0x2b7b3b ) );

        undoArrow.setFont ( new Font ( null, Font.BOLD, 20 ) );
        undoText.setFont ( new Font ( null, Font.BOLD, 20 ) );

        undoArrow.setHorizontalAlignment ( JLabel.RIGHT );
        undoText.setHorizontalAlignment ( JLabel.LEFT );
        undoArrow.setVerticalAlignment ( JLabel.CENTER );
        undoText.setVerticalAlignment ( JLabel.CENTER );

        undoArrow.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f + 1 ) / 4 - 22 ), Math.round ( initialFrameHeight * 90f / 600f ) ) );
        undoText.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f + 1 ) / 4 + 22 ), Math.round ( initialFrameHeight * 90f / 600f ) ) );

        JPanel undoPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );

        undoPanel.addMouseListener ( this );
        undoPanel.setBackground ( new Color ( 0x34a249 ) );
        undoPanel.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f + 1 ) / 2 ), Math.round ( initialFrameHeight * 90f / 600f ) ) );

        undoPanel.add ( undoArrow );
        undoPanel.add ( undoText );

        /***************************************************************************************************************/

        JLabel starLabel = new JLabel ( "✪" );
        JLabel newGameLabel = new JLabel ( "NEW GAME" );

        starLabel.setForeground ( new Color ( 0x2b7b3b ) );
        newGameLabel.setForeground ( new Color ( 0x2b7b3b ) );

        starLabel.setFont ( new Font ( null, Font.BOLD, 20 ) );
        newGameLabel.setFont ( new Font ( null, Font.BOLD, 20 ) );

        starLabel.setHorizontalAlignment ( JLabel.RIGHT );
        newGameLabel.setHorizontalAlignment ( JLabel.LEFT );
        starLabel.setVerticalAlignment ( JLabel.CENTER );
        newGameLabel.setVerticalAlignment ( JLabel.CENTER );

        starLabel.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f + 1 ) / 4 - 60 ), Math.round ( initialFrameHeight * 90f / 600f ) ) );
        newGameLabel.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f + 1 ) / 4 + 60 ), Math.round ( initialFrameHeight * 90f / 600f ) ) );

        JPanel newGamePanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );

        newGamePanel.addMouseListener ( this );
        newGamePanel.setBackground ( new Color ( 0x34a249 ) );
        newGamePanel.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f + 1 ) / 2 ), Math.round ( initialFrameHeight * 90f / 600f ) ) );

        newGamePanel.add ( starLabel );
        newGamePanel.add ( newGameLabel );

        /***************************************************************************************************************/

        controlsField.add ( undoPanel );
        controlsField.add ( newGamePanel );

        middlePanel.add ( controlsField );
        ( ( SpringLayout ) middlePanel.getLayout ( ) ).putConstraint ( SpringLayout.WEST, controlsField, 0, SpringLayout.WEST, this );
        ( ( SpringLayout ) middlePanel.getLayout ( ) ).putConstraint ( SpringLayout.SOUTH, controlsField, -117, SpringLayout.SOUTH, this );

        /***************************************************************************************************************/

        return new JPanel[]{ undoPanel, newGamePanel };
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

        topPanel = mainPanels[0];
        leftPanel = mainPanels[1];
        middlePanel = mainPanels[2];
        rightPanel = mainPanels[3];

        stockPanel = stockPanelBuilder ( );

        wastePanel = wastePanelBuilder ( );

        pilePanels = pilePanelsBuilder ( );

        foundationPanels = foundationPanelsBuilder ( );

        controlPanels = controlFieldsBuilder ( middlePanel );

        pack ( );
        setVisible ( true );
    }
}
