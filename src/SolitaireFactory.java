import javax.swing.*;
import java.awt.*;

public abstract class SolitaireFactory {

    public static void frameSetUp ( Solitaire frame ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );

        int initialFrameWidth = Math.round ( screenSize.width / 2f );
        int initialFrameHeight = Math.round ( screenSize.height / 2f );
        int initialFrameXCoordinate = Math.round ( ( screenSize.width - initialFrameWidth ) / 2f );
        int initialFrameYCoordinate = Math.round ( ( screenSize.height - initialFrameHeight ) / 2f );

        frame.setMinimumSize ( new Dimension ( initialFrameWidth, initialFrameHeight ) );
        frame.setLocation ( new Point ( initialFrameXCoordinate, initialFrameYCoordinate ) );
        frame.setTitle ( "Solitaire" );
        frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

    }

    public static JPanel[] mainPanelsBuilder ( ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int initialFrameWidth = Math.round ( screenSize.width / 2f );
        int initialFrameHeight = Math.round ( screenSize.height / 2f );

        JPanel topPanel = new JPanel ( new FlowLayout ( FlowLayout.RIGHT, 0, 0 ) );
        JPanel leftPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 20, 20 ) );
        JPanel middlePanel = new JPanel ( new SpringLayout ( ) );
        JPanel rightPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 20, 20 ) );

        topPanel.setBackground ( new Color ( 0x313131 ) );
        leftPanel.setBackground ( new Color ( 0x2b7b3b ) );
        middlePanel.setBackground ( new Color ( 0x34a249 ) );
        rightPanel.setBackground ( new Color ( 0x2b7b3b ) );

        topPanel.setPreferredSize ( new Dimension ( initialFrameWidth, Math.round ( initialFrameHeight * 10f / 100f ) ) );
        leftPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameWidth * 10f / 100f ), Math.round ( initialFrameHeight * 90f / 100f ) ) );
        middlePanel.setPreferredSize ( new Dimension ( Math.round ( ( initialFrameWidth - initialFrameWidth * 20f / 100f ) ), Math.round ( initialFrameHeight * 90f / 100f ) ) );
        rightPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameWidth * 10f / 100f ), Math.round ( initialFrameHeight * 90f / 100f ) ) );

        return new JPanel[]{ topPanel, leftPanel, middlePanel, rightPanel };
    }

    public static JPanel[] controlPanelsBuilder ( JPanel topPanel ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        int initialFrameHeight = Math.round ( screenSize.height * 2f / 5f );

        JLabel undoArrow = new JLabel ( "↶" );

        undoArrow.setForeground ( new Color ( 0xF8F8FF ) );

        undoArrow.setFont ( new Font ( null, Font.BOLD, 20 ) );

        undoArrow.setHorizontalAlignment ( JLabel.CENTER );
        undoArrow.setVerticalAlignment ( JLabel.CENTER );

        undoArrow.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f ), Math.round ( initialFrameHeight * 10f / 100f ) ) );

        JPanel undoPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );

        undoPanel.setBackground ( new Color ( 0x313131 ) );
        undoPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f + 10 ), Math.round ( initialFrameHeight * 10f / 100f ) ) );

        undoPanel.add ( undoArrow );

        /***************************************************************************************************************/

        JLabel starLabel = new JLabel ( "✪" );

        starLabel.setForeground ( new Color ( 0xF8F8FF ) );

        starLabel.setFont ( new Font ( null, Font.BOLD, 20 ) );

        starLabel.setHorizontalAlignment ( JLabel.CENTER );
        starLabel.setVerticalAlignment ( JLabel.CENTER );

        starLabel.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f ), Math.round ( initialFrameHeight * 10f / 100f ) + 6 ) );

        JPanel newGamePanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );

        newGamePanel.setBackground ( new Color ( 0x313131 ) );
        newGamePanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f ), Math.round ( initialFrameHeight * 10f / 100f ) ) );

        newGamePanel.add ( starLabel );

        /***************************************************************************************************************/

        JLabel logInLabel = new JLabel ( "LogIn" );

        logInLabel.setForeground ( new Color ( 0xF8F8FF ) );

        logInLabel.setFont ( new Font ( null, Font.BOLD, 10 ) );

        logInLabel.setHorizontalAlignment ( JLabel.CENTER );
        logInLabel.setVerticalAlignment ( JLabel.CENTER );

        logInLabel.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f ), Math.round ( initialFrameHeight * 10f / 100f ) + 6 ) );

        JPanel logInPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );

        logInPanel.setBackground ( new Color ( 0x313131 ) );
        logInPanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f ), Math.round ( initialFrameHeight * 10f / 100f ) ) );

        logInPanel.add ( logInLabel );

        topPanel.add ( undoPanel );
        topPanel.add ( newGamePanel );
        topPanel.add ( logInPanel );

        /***************************************************************************************************************/

        JLabel performanceLabel = new JLabel ( "\uD83C\uDFC6" );

        performanceLabel.setForeground ( new Color ( 0xF8F8FF ) );

        performanceLabel.setFont ( new Font ( null, Font.BOLD, 26 ) );

        performanceLabel.setHorizontalAlignment ( JLabel.CENTER );
        performanceLabel.setVerticalAlignment ( JLabel.CENTER );

        performanceLabel.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f ), Math.round ( initialFrameHeight * 10f / 100f ) - 20  ) );

        JPanel performancePanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 14 ) );

        performancePanel.setBackground ( new Color ( 0x313131 ) );
        performancePanel.setPreferredSize ( new Dimension ( Math.round ( initialFrameHeight * 10f / 100f ), Math.round ( initialFrameHeight * 10f / 100f ) ) );

        performancePanel.add ( performanceLabel );

        topPanel.add ( undoPanel );
        topPanel.add ( newGamePanel );
        topPanel.add ( logInPanel );


        return new JPanel[]{ undoPanel, newGamePanel, logInPanel, performancePanel };
    }

    public static CardPanel[] cardPanelsBuilder ( Board board ) {
        CardPanel[] deck = new CardPanel[52];

        for ( int i = 0; i < 52; i++ )
            deck[i] = new CardPanel ( board.Stock.CardAt ( i ) );


        return deck;
    }

    public static void middlePanelLayoutSetUp ( JPanel middlePanel, PilePanel[] pilePanels ) {

        SpringLayout layout = ( SpringLayout ) middlePanel.getLayout ( );

        for ( int i = 0; i < 7; i++ )
            layout.putConstraint ( SpringLayout.NORTH, pilePanels[i], 20, SpringLayout.NORTH, middlePanel );

        layout.putConstraint ( SpringLayout.WEST, pilePanels[0], 63, SpringLayout.WEST, middlePanel );

        for ( int i = 0; i < 6; i++ )
            layout.putConstraint ( SpringLayout.WEST, pilePanels[i + 1], 61, SpringLayout.EAST, pilePanels[i] );

        layout.putConstraint ( SpringLayout.EAST, middlePanel, 63, SpringLayout.WEST, pilePanels[6] );
    }
}
