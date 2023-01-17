import org.postgresql.util.PGInterval;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class PerformanceFrame extends JFrame {

    PerformanceFrame ( int playerID ) {
        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );

        int initialFrameWidth = Math.round ( screenSize.width / 3f );
        int initialFrameHeight = Math.round ( screenSize.height / 3f );
        int initialFrameXCoordinate = Math.round ( ( screenSize.width - initialFrameWidth ) / 2f );
        int initialFrameYCoordinate = Math.round ( ( screenSize.height - initialFrameHeight ) / 2f );

        setMinimumSize ( new Dimension ( initialFrameWidth, initialFrameHeight ) );
        setLocation ( new Point ( initialFrameXCoordinate, initialFrameYCoordinate ) );
        setTitle ( "Performance" );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setLayout ( new FlowLayout ( ) );
        setBackground ( new Color ( 0x2b7b3b ) );

        Object[] numbers = SolitaireDataBase.queryPerformance ( playerID );

        String username = ( String ) numbers[0];
        Float winRate = ( Float ) numbers[1];
        PGInterval avgTime = ( PGInterval ) numbers[2];
        BigDecimal avgMoves = ( BigDecimal ) numbers[3];

        JLabel usernameLabel = new JLabel ( username );
        JLabel winRateLabel = new JLabel ( winRate.toString ( ) );
        JLabel avgTimeLabel = new JLabel ( /*avgTime.toString ( )*/ ( ( Integer ) avgTime.getMinutes ( ) ).toString ( ) + ":" + ( ( avgTime.getSeconds ( ) < 10 ) ? "0" : "" ) + ( ( ( Double ) avgTime.getSeconds ( ) ).toString ( ).trim() ) );
        JLabel avgMovesLabel = new JLabel ( avgMoves.toString ( ) );

        add ( usernameLabel );
        add ( winRateLabel );
        add ( avgTimeLabel );
        add ( avgMovesLabel );

        setVisible ( true );
    }
}
