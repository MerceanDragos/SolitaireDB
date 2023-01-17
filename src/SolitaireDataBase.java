import org.postgresql.PGResultSetMetaData;
import org.postgresql.util.PGInterval;
import org.postgresql.util.PGTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;

public class SolitaireDataBase {

    public class UsernameAlreadyTakenException extends Exception {

    }

    static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "3243";

    static Connection connection;
    static Statement statement;

    static {
        try {
            connection = DriverManager.getConnection ( URL, USER, PASS );
            statement = connection.createStatement ( );
        }
        catch ( SQLException e ) {
            System.out.println ( "Failed to establish a connection" );

            /*e.printStackTrace ( );
            System.err.println ( e.getClass ( ).getName ( ) + ": " + e.getMessage ( ) );*/
        }
    }

    static private String inQuotes ( String str ) {
        return "'" + str + "'";
    }

    static public int signUp ( String username, String password ) throws UsernameAlreadyTakenException {
        int i;
        try {
            statement.executeUpdate ( "INSERT INTO player VALUES (DEFAULT, " + username + ", " + password + ", DEFAULT, DEFAULT)" );
            ResultSet rs = ( statement.executeQuery ( "SELECT playerid FROM player WHERE username = " + username + " AND password = " + password ) );
            rs.next ( );
            i = rs.getInt ( 1 );
        }
        catch ( SQLException e ) {
            i = -1;

            if ( e.getMessage ( ).contains ( "sex" ) )
                System.out.println ( "sex" );

            e.printStackTrace ( );
            System.err.println ( e.getClass ( ).getName ( ) + ": " + e.getMessage ( ) );
        }

        return i;
    }

    static public int logIn ( String username, String password ) {

        username = inQuotes ( username );
        password = inQuotes ( password );


        try {
            ResultSet rs = statement.executeQuery ( "SELECT * FROM player WHERE username = " + username + " AND password = " + password );

            if ( rs.next ( ) ) {
                return rs.getInt ( 1 );
            }
            else {
                try {
                    int i;

                    if ( JOptionPane.showConfirmDialog ( null, "No account with these credentials has been found. Create new account?", "", JOptionPane.YES_NO_OPTION ) == 0 ) {
                        i = signUp ( username, password );
                        if ( i > 0 ) {
                            JOptionPane.showMessageDialog ( null, "New account created" );
                            return i;
                        }
                    }
                    else
                        return 0;
                }
                catch ( UsernameAlreadyTakenException e ) {
                    JOptionPane.showMessageDialog ( null, "Username already taken" );
                }
            }
        }
        catch ( SQLException e ) {
            e.printStackTrace ( );
            System.err.println ( e.getClass ( ).getName ( ) + ": " + e.getMessage ( ) );

            try {
                statement.executeQuery ( "SELECT setval('player_playerid_seq', (SELECT max(playerid) FROM player))" );
            }
            catch ( SQLException e2 ) {
                e2.printStackTrace ( );
                System.err.println ( e2.getClass ( ).getName ( ) + ": " + e2.getMessage ( ) );
            }
        }
        return -1;
    }

    static public void updatePerformance ( int playerID, boolean win, double time, int moves ) {

        try {
            ResultSet rs = statement.executeQuery ( "SELECT * FROM performance WHERE playerID = " + playerID );

            long nrRuns = 1;
            int nrWins = ( win ) ? 1 : 0;
            PGInterval totalTime = new PGInterval ( 0, 0, 0, 0, ( int ) ( time / 1000 ) / 60, ( time / 1000 ) % 60 );
            long totalMoves = moves;

            if ( rs.next ( ) ) {
                rs.next ( );
                nrRuns += rs.getInt ( 2 );
                nrWins += rs.getInt ( 3 );
                totalTime.add ( ( PGInterval ) rs.getObject ( 4 ) );
                totalMoves += rs.getLong ( 5 );
            }

            statement.executeUpdate ( "INSERT INTO performance (playerid, nrRuns, nrWins, totalTime, totalMoves) " +
                    "VALUES (" + playerID + ", " + nrRuns + ", " + nrWins + ", 'P0000-00-" + totalTime.getDays ( ) + "T" + totalTime.getHours ( ) + ":" + totalTime.getMinutes ( ) + ":" + totalTime.getSeconds ( ) + "', " + totalMoves + " ) " +
                    "ON CONFLICT (playerID) " +
                    "DO UPDATE SET nrRuns = " + nrRuns + ", nrWins = " + nrWins + ", totalTime = 'P0000-00-" + totalTime.getDays ( ) + "T" + totalTime.getHours ( ) + ":" + totalTime.getMinutes ( ) + ":" + totalTime.getSeconds ( ) + "', totalMoves = " + totalMoves );

        }
        catch ( SQLException e ) {
            e.printStackTrace ( );
            System.err.println ( e.getClass ( ).getName ( ) + ": " + e.getMessage ( ) );
        }

    }

    static public Object[] queryPerformance ( int playerID ) {

        DefaultTableModel table = new DefaultTableModel ( );
        ResultSet rs;
        String username = "";
        BigDecimal nrRuns = null;
        BigDecimal nrWins = null;
        PGInterval totalTime = null;
        BigDecimal totalMoves = null;
        try {
            rs = statement.executeQuery ( "SELECT username, nrRuns, nrWins, totalTime,totalMoves  FROM performance JOIN player ON (performance.playerID = player.playerID) WHERE performance.playerID = " + playerID );
            rs.next ( );
            username = rs.getString ( 1 );
            nrRuns = rs.getBigDecimal ( 2 );
            nrWins = rs.getBigDecimal ( 3 );
            totalTime = ( PGInterval ) rs.getObject ( 4 );
            totalMoves = rs.getBigDecimal ( 5 );
        }
        catch ( SQLException e ) {
            System.out.println ( "damn" );
        }

        Float winRate = nrWins.divide ( nrRuns, RoundingMode.DOWN ).floatValue ( );


        double seconds = totalTime.getHours ( ) * 3600 + totalTime.getMinutes ( ) * 60 + totalTime.getSeconds ( );
        seconds /= nrRuns.doubleValue ( );
        PGInterval avgTime = new PGInterval ( 0, 0, 0, 0, ( int ) seconds / 60, seconds % 60 );

        BigDecimal avgMoves = totalMoves.divide ( nrRuns );


        return new Object[]{ username, winRate, avgTime, avgMoves };
    }
}
