import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class LogInScreen extends JFrame implements MouseListener {

    @Override
    public void mouseReleased ( MouseEvent event ) {
        if ( event.getSource ( ) == playOfflinePanel ) {
            dispose ( );
            new Solitaire ( -1 );
        }
        else {
            String username = usernameTextField.getText ( );
            String password = passwordTextField.getText ( );

            if ( username.equals ( "" ) || password.equals ( "" ) ) {
                JOptionPane.showMessageDialog ( null, "Both fields must be completed" );
                usernameTextField.setText ( "" );
                passwordTextField.setText ( "" );
            }
            else {
                int playerID = SolitaireDataBase.logIn ( username, password );
                if ( playerID > 0 ) {
                    dispose ( );
                    new Solitaire ( playerID );
                }
                else {
                    usernameTextField.setText ( "" );
                    passwordTextField.setText ( "" );
                }
            }
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
    public void mouseClicked ( MouseEvent e ) {
        assert true;
    }


    private JTextField usernameTextField;
    private JTextField passwordTextField;

    private JPanel signUpPanel;
    private JPanel playOfflinePanel;

    public LogInScreen ( ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );

        setTitle ( "LogIn" );
        setSize ( 320, 180 );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setLayout ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
        setResizable ( false );
        setLocation ( ( screenSize.width - 320 ) / 2, ( screenSize.height - 180 ) / 2 );

        JPanel usernamePanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 20 ) );
        usernamePanel.setPreferredSize ( new Dimension ( 320, 60 ) );
        usernamePanel.setBackground ( new Color ( 0x2b7b3b ) );


        JPanel passwordPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
        passwordPanel.setPreferredSize ( new Dimension ( 320, 35 ) );
        passwordPanel.setBackground ( new Color ( 0x2b7b3b ) );

        JPanel containerPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 20, 0 ) );
        containerPanel.setPreferredSize ( new Dimension ( 320, 85 ) );
        containerPanel.setBackground ( new Color ( 0x2b7b3b ) );

        signUpPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER ) );
        signUpPanel.setPreferredSize ( new Dimension ( 100, 30 ) );
        signUpPanel.setBackground ( new Color ( 0x2b7b3b ) );
        signUpPanel.setBorder ( BorderFactory.createLineBorder ( new Color ( 0x34a249 ) ) );
        signUpPanel.addMouseListener ( this );
        JLabel signUpText = new JLabel ( "Log In" );
        signUpText.setForeground ( Color.WHITE );
        signUpPanel.add ( signUpText );

        playOfflinePanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER ) );
        playOfflinePanel.setPreferredSize ( new Dimension ( 100, 30 ) );
        playOfflinePanel.setBackground ( new Color ( 0x2b7b3b ) );
        playOfflinePanel.setBorder ( BorderFactory.createLineBorder ( new Color ( 0x34a249 ) ) );
        playOfflinePanel.addMouseListener ( this );
        JLabel playOfflineText = new JLabel ( "Play Offline" );
        playOfflineText.setForeground ( Color.WHITE );
        playOfflinePanel.add ( playOfflineText );

        JLabel usernameLabel = new JLabel ( "Username: " );
        usernameLabel.setForeground ( Color.WHITE );
        usernamePanel.add ( usernameLabel );

        usernameTextField = new JTextField ( 20 );
        usernamePanel.add ( usernameTextField );

        JLabel passwordLabel = new JLabel ( "Password: " );
        passwordLabel.setForeground ( Color.WHITE );
        passwordPanel.add ( passwordLabel );

        passwordTextField = new JPasswordField ( 20 );
        passwordPanel.add ( passwordTextField );
        containerPanel.add ( signUpPanel );
        containerPanel.add ( playOfflinePanel );

        add ( usernamePanel );
        add ( passwordPanel );
        add ( containerPanel );

        setVisible ( true );
    }

    public LogInScreen ( boolean stop ) {

        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );

        setTitle ( "Log In" );
        setSize ( 320, 180 );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setLayout ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
        setResizable ( false );
        setLocation ( ( screenSize.width - 320 ) / 2, ( screenSize.height - 180 ) / 2 );

        JPanel usernamePanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 20 ) );
        usernamePanel.setPreferredSize ( new Dimension ( 320, 60 ) );
        usernamePanel.setBackground ( new Color ( 0x2b7b3b ) );


        JPanel passwordPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 0, 0 ) );
        passwordPanel.setPreferredSize ( new Dimension ( 320, 35 ) );
        passwordPanel.setBackground ( new Color ( 0x2b7b3b ) );

        JPanel containerPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 20, 0 ) );
        containerPanel.setPreferredSize ( new Dimension ( 320, 85 ) );
        containerPanel.setBackground ( new Color ( 0x2b7b3b ) );

        signUpPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER ) );
        signUpPanel.setPreferredSize ( new Dimension ( 100, 30 ) );
        signUpPanel.setBackground ( new Color ( 0x2b7b3b ) );
        signUpPanel.setBorder ( BorderFactory.createLineBorder ( new Color ( 0x34a249 ) ) );
        signUpPanel.addMouseListener ( this );
        JLabel signUpText = new JLabel ( "Log In" );
        signUpText.setForeground ( Color.WHITE );
        signUpPanel.add ( signUpText );

        JLabel usernameLabel = new JLabel ( "Username: " );
        usernameLabel.setForeground ( Color.WHITE );
        usernamePanel.add ( usernameLabel );

        usernameTextField = new JTextField ( 20 );
        usernamePanel.add ( usernameTextField );

        JLabel passwordLabel = new JLabel ( "Password: " );
        passwordLabel.setForeground ( Color.WHITE );
        passwordPanel.add ( passwordLabel );

        passwordTextField = new JPasswordField ( 20 );
        passwordPanel.add ( passwordTextField );
        containerPanel.add ( signUpPanel );

        add ( usernamePanel );
        add ( passwordPanel );
        add ( containerPanel );

        setVisible ( true );
    }
}
