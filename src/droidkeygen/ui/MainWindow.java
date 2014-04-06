// MainWindow.java

package droidkeygen.ui;

import droidkeygen.core.AppInfo;
import droidkeygen.core.KeyInfo;
import droidkeygen.core.KeyManager;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * The main window of the app.
 * @author baltasarq
 */
public class MainWindow extends Frame {

    public MainWindow()
    {
        this.build();
    }
    
    /**
     * Exits the app
     */
    protected void onExit()
    {
        System.exit( 0 );
    }
    
    /**
     * Generation of the key store
     */
    protected void onGenerate()
    {
        this.pnlPswLoad.setVisible( false );
        this.pnlData.setVisible( true );
        this.lblStatus.setText( "Ready" );
        this.pack();
        this.edAlias.requestFocusInWindow();
    }
    
    /**
     * Checks an existing key
     */
    protected void onCheckKey()
    {
        this.pnlData.setVisible( false );
        this.pnlPswLoad.setVisible( true );
        this.lblStatus.setText( "Ready" );
        this.pack();
        this.edPswLoad.requestFocusInWindow();
    }
    
    /**
     * Loads an existing key
     */
    protected void loadKey()
    {
        File[] fileNames;
        FileDialog dlgLoad = new FileDialog( this, "Load keystore...", FileDialog.LOAD );
        dlgLoad.setFile( "*.ks" );
        String psw = this.edPswLoad.getText().trim();
        
        try {
            this.pnlData.setVisible( false );
            this.pnlPswLoad.setVisible( false );
            this.lblStatus.setText( "Choose file name..." );
            this.pack();
            dlgLoad.setVisible( true );
            fileNames = dlgLoad.getFiles();

            if ( fileNames.length > 0 ) {
                File file = fileNames[ 0 ].getCanonicalFile();

                this.lblStatus.setText( "Loading keystore..." );
                this.keyInfo = new KeyManager( file ).load( psw );
                
                if ( this.keyInfo != null ) {
                    this.writeKeyInfoToData();
                    this.onGenerate();
                } else {
                    this.lblStatus.setText( "Error loading: empty keystore?" );
                }
            } else {
                this.lblStatus.setText( "Ready" );
            }
        }
        catch(IOException
            | KeyStoreException
            | NoSuchAlgorithmException
            | RuntimeException
            | CertificateException ex)
        {
            this.lblStatus.setText( "Error: " + ex.getLocalizedMessage() );
        }
        finally {
            this.requestFocusInWindow();
        }
        
        return;
    }
    
    /**
     * Copies the information on the keyinfo object to the data on the form
     */
    protected void writeKeyInfoToData()
    {
        this.edAlias.setText( this.keyInfo.getAlias() );
        this.edName.setText( this.keyInfo.getName() );
        this.edOu.setText( this.keyInfo.getOrgUnit() );
        this.edFirm.setText( this.keyInfo.getCompany() );
        this.edCity.setText( this.keyInfo.getCity() );
        this.edState.setText( this.keyInfo.getState() );
        this.cmbCountryCode.select( this.keyInfo.getCountryCode() );
        
        this.edPsw1.setText( this.keyInfo.getPassword() );
        this.edPsw2.setText( this.keyInfo.getPassword() );
    }   
    
    /**
     * Checks all the info, by storing it in the keyInfo member
     */
    protected void readDataToKeyInfo() throws RuntimeException
    {
        String psw1 = this.edPsw1.getText().trim();
        String psw2 = this.edPsw2.getText().trim();

        if ( !( psw1.equals( psw2 ) ) ) {
            throw new RuntimeException( "passwords do not match" );
        }

        this.keyInfo =  new KeyInfo(
                this.edAlias.getText(), this.edName.getText(),
                this.edOu.getText(), this.edFirm.getText(),
                this.edCity.getText(), this.edState.getText(),
                this.cmbCountryCode.getSelectedItem(), psw1
        );
        
        return;
    }
    
    /**
     * Generate the key store
     */
    protected void onSaveKey()
    {
        File[] fileNames;
        FileDialog dlgSave = new FileDialog( this, "Save keystore...", FileDialog.SAVE );
        dlgSave.setFile( "*.ks" );
        
        try {
            this.readDataToKeyInfo();
            this.lblStatus.setText( "Choose file name..." );
            dlgSave.setVisible( true );
            fileNames = dlgSave.getFiles();

            if ( fileNames.length > 0 ) {
                File file = fileNames[ 0 ].getCanonicalFile();

                KeyManager kg = new KeyManager( file, this.keyInfo );
                this.lblStatus.setText( "Generating KeyStore..." );
                kg.generate();
                this.lblStatus.setText( "KeyStore file generated" );
                this.pnlData.setVisible( false );
            } else {
                this.lblStatus.setText( "Ready" );
            }
        } catch (RuntimeException
                | IOException
                | KeyStoreException
                | NoSuchAlgorithmException
                | CertificateException
                | InvalidKeyException
                | SignatureException
                | NoSuchProviderException ex)
        {
            this.lblStatus.setText( "Error: " + ex.getLocalizedMessage() );
        }
        finally {
            this.requestFocusInWindow();
        }
        
        return;
    }
    
    /**
     * About action
     */
    protected void onAbout()
    {
        this.pnlAbout.setVisible( true );
        this.lblStatus.setText( "Ready" );        
        this.pack();
    }
    
    private void build()
    {
        // Icon
        this.icon = Toolkit.getDefaultToolkit().getImage(
                DroidKeyGen.class.getResource( "/res/key.png" )
        );
        
        // Actions
        this.onExit = new AbstractAction( "onExit" ) {
            @Override
                public void actionPerformed(ActionEvent e)
                {
                   onExit();
                }
        };
        
        this.onGenerate = new AbstractAction( "onGenerate" ) {
            @Override
                public void actionPerformed(ActionEvent e)
                {
                   onGenerate();
                }
        };
        
        this.onCheckKey = new AbstractAction( "onCheckKey" ) {
            @Override
                public void actionPerformed(ActionEvent e)
                {
                   onCheckKey();
                }
        };
        
        this.onAbout = new AbstractAction( "onAbout" ) {
            @Override
                public void actionPerformed(ActionEvent e)
                {
                   onAbout();
                }
        };
        
        this.onSaveKey = new AbstractAction( "onSaveKey" ) {
            @Override
                public void actionPerformed(ActionEvent e)
                {
                   onSaveKey();
                }
        };
        
        // Create the name panel
        this.pnlName = new Panel();
        Label lblName = new Label( "Complete name: " );
        this.edName = new TextField( 40 );
        Label lblAlias = new Label( "Alias: " );
        this.edAlias = new TextField( 40 );
        this.pnlName.setLayout( new GridLayout( 2,2 ) );
        this.pnlName.add( lblAlias );
        this.pnlName.add( this.edAlias );
        this.pnlName.add( lblName );
        this.pnlName.add( this.edName );
        
        // Create firm panel
        this.pnlFirm = new Panel();
        Label lblFirm = new Label( "Company name: " );
        this.edFirm = new TextField( 40 );
        Label lblOu = new Label( "Organizational Unit: " );
        this.edOu = new TextField( 40 );
        this.pnlFirm.setLayout( new GridLayout( 2,2 ) );
        this.pnlFirm.add( lblFirm );
        this.pnlFirm.add( this.edFirm );
        this.pnlFirm.add( lblOu );
        this.pnlFirm.add( this.edOu );
        
        // Create address panel
        this.pnlAddress = new Panel();
        Label lblCity = new Label( "City: " );
        this.edCity = new TextField( 40 );
        Label lblState = new Label( "State: " );
        this.edState = new TextField( 40 );
        Label lblCountryCode = new Label( "Country code: " );
        this.cmbCountryCode = new Choice();
        for(String code: KeyInfo.getCountryCodes())  {
            this.cmbCountryCode.add( code );
        }

        this.pnlAddress.setLayout( new GridLayout( 3,2 ) );
        this.pnlAddress.add( lblCity );
        this.pnlAddress.add( this.edCity );
        this.pnlAddress.add( lblState );
        this.pnlAddress.add( this.edState );
        this.pnlAddress.add( lblCountryCode );
        this.pnlAddress.add( this.cmbCountryCode );
        
        // Create password panel
        this.pnlPsw = new Panel();
        Label lblPsw1 = new Label( "Password: " );
        this.edPsw1 = new TextField( 20 );
        Label lblPsw2 = new Label( "Repeat password: " );
        this.edPsw2 = new TextField( 20 );
        this.edPsw1.setEchoChar( '*' );
        this.edPsw2.setEchoChar( '*' );
        this.pnlPsw.setLayout( new GridLayout( 3,2 ) );
        this.pnlPsw.add( lblPsw1 );
        this.pnlPsw.add( this.edPsw1 );
        this.pnlPsw.add( lblPsw2 );
        this.pnlPsw.add( this.edPsw2 );
        
        // Create the password only panel
        this.pnlPswLoad = new Panel();
        this.pnlPswLoad.setVisible( false );
        Label lblPswLoad = new Label( "Password: " );
        this.edPswLoad = new TextField( 20 );
        this.edPswLoad.setEchoChar( '*' );
        Button btLoadKey = new Button( "..." );
        this.pnlPswLoad.setLayout( new BoxLayout( this.pnlPswLoad, BoxLayout.LINE_AXIS ) );
        this.pnlPswLoad.add( lblPswLoad );
        this.pnlPswLoad.add( this.edPswLoad );
        this.pnlPswLoad.add( btLoadKey );
        btLoadKey.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                pnlPswLoad.setVisible( false );
                pack();
                loadKey();
            }
        });
        
        // Create action panel
        this.pnlAction = new Panel();
        this.pnlAction.setLayout( new BoxLayout( this.pnlAction, BoxLayout.LINE_AXIS ) );
        this.btSaveAs = new Button( "Save as..." );
        this.btSaveAs.addActionListener( onSaveKey );
        this.pnlAction.add( Box.createHorizontalGlue() );
        this.pnlAction.add( Box.createHorizontalGlue() );
        this.pnlAction.add( this.btSaveAs );
        
        // Panel about
        Label lblInfo = new Label();
        Color paleYellow = new Color( 0xFFFFFFE0 );
        Button btCloseAbout = new Button( "X" );
        btCloseAbout.setBackground( paleYellow );
        lblInfo.setText( AppInfo.Name + ' ' + AppInfo.Version + ". " + AppInfo.Author );
        btCloseAbout.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                pnlAbout.setVisible( false );
                pack();
            }
        });

        Panel pnlInnerAbout = new Panel();
        pnlInnerAbout.setLayout( new BoxLayout( pnlInnerAbout, BoxLayout.LINE_AXIS ) );
        pnlInnerAbout.add( lblInfo );
        pnlInnerAbout.add( Box.createHorizontalGlue() );
        pnlInnerAbout.add( btCloseAbout );
        
        this.pnlAbout = new Panel();
        this.pnlAbout.setVisible( false );
        this.pnlAbout.setBackground( paleYellow );
        this.pnlAbout.setLayout( new BoxLayout( this.pnlAbout, BoxLayout.PAGE_AXIS ) );
        this.pnlAbout.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
        this.pnlAbout.add( pnlInnerAbout );
        this.pnlAbout.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
        
        // Status panel
        this.pnlStatus = new Panel();
        this.pnlStatus.setLayout( new BoxLayout( this.pnlStatus, BoxLayout.PAGE_AXIS) );
        this.pnlStatus.add( this.pnlAbout );
        this.lblStatus = new Label( "Ready" );
        this.pnlStatus.add( this.lblStatus  );
        this.pnlStatus.setBackground( Color.lightGray );
        
        // Main panel
        this.pnlData = new Panel();
        this.pnlData.setVisible( false );
        this.pnlData.setLayout( new BoxLayout( this.pnlData, BoxLayout.PAGE_AXIS ) );
        this.pnlData.add( this.pnlName );
        this.pnlData.add( Box.createRigidArea( new Dimension( 0,5 ) ) );
        this.pnlData.add( this.pnlFirm );
        this.pnlData.add( Box.createRigidArea( new Dimension( 0,5 ) ) );
        this.pnlData.add( this.pnlAddress );
        this.pnlData.add( Box.createRigidArea( new Dimension( 0,5 ) ) );
        this.pnlData.add( this.pnlPsw );
        this.pnlData.add( Box.createRigidArea( new Dimension( 0,5 ) ) );
        this.pnlData.add( this.pnlAction );
        
        // Main menu
        MenuBar mMainMenu = new MenuBar();
        Menu mFile = new Menu( "File" );
        Menu mHelp = new Menu( "?" );
        MenuItem opGenerate = new MenuItem( "Generate..." );
        opGenerate.addActionListener( onGenerate );
        mFile.add( opGenerate );
        MenuItem opCheck = new MenuItem( "Check key..." );
        opCheck.addActionListener( onCheckKey );
        mFile.add( opCheck );
        MenuItem opExit = new MenuItem( "Exit", new MenuShortcut( KeyEvent.VK_Q, false ) );
        opExit.addActionListener( onExit );
        MenuItem opAbout = new MenuItem( "About" );
        opAbout.addActionListener( onAbout );
        mHelp.add( opAbout );
        mFile.add( opExit );
        mMainMenu.add( mFile );
        mMainMenu.add( mHelp );
                
        // Finally
        this.setIconImage( this.icon );
        this.setMenuBar( mMainMenu );
        this.setLayout( new BorderLayout() );
        this.add( this.pnlPswLoad, BorderLayout.PAGE_START );
        this.add( this.pnlData, BorderLayout.CENTER );
        this.add( this.pnlStatus, BorderLayout.PAGE_END );
        this.setSize( 500, 400 );
        this.setTitle( AppInfo.Name + ' ' + AppInfo.Version );
        this.setResizable( false );
        this.pack();
        
        int neededHeight = this.getGraphics().getFontMetrics( this.getFont() ).getHeight() +5;
        this.pnlAbout.setMaximumSize( new Dimension( Integer.MAX_VALUE, neededHeight + 20 ) );
        this.pnlStatus.setMaximumSize( new Dimension( Integer.MAX_VALUE, neededHeight ) );

        Dimension preferredSize = new Dimension( 420, 470 );
        this.setMinimumSize( preferredSize );
        this.setMaximumSize( preferredSize );
        this.setPreferredSize( preferredSize );
        
        // Events
        this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
  
        return;
    }
    
    private AbstractAction onExit;
    private AbstractAction onGenerate;
    private AbstractAction onAbout;
    private AbstractAction onSaveKey;
    private AbstractAction onCheckKey;
    private Panel pnlAction;
    private Panel pnlData;
    private Panel pnlName;
    private Panel pnlFirm;
    private Panel pnlAddress;
    private Panel pnlPsw;
    private Panel pnlAbout;
    private Panel pnlStatus;
    private Panel pnlPswLoad;
    private TextField edName;
    private TextField edAlias;
    private TextField edFirm;
    private TextField edOu;
    private TextField edCity;
    private TextField edState;
    private TextField edPswLoad;
    private Choice cmbCountryCode;
    private TextField edPsw1;
    private TextField edPsw2;
    private Label lblStatus;
    private Image icon;
    private Button btSaveAs;
    private KeyInfo keyInfo;
}
