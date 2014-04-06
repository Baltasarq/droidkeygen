// KeyManager.java

package droidkeygen.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;

/**
 * The Key generator, wrapping the KeyStore class
 * @author baltasarq
 */
public class KeyManager {

    public KeyManager(File f)
    {
        this.file = f;
        this.keyInfo = null;
    }
    
    public KeyManager(File f, KeyInfo k)
    {
        this( f );
        this.keyInfo = k;
    }
    
    public void generate()
            throws KeyStoreException, IOException, NoSuchAlgorithmException,
                   CertificateException, NoSuchProviderException,
                   InvalidKeyException, SignatureException
    {
        int keySize = 2048;
        char[] psw = this.keyInfo.getPassword().toCharArray();
        OutputStream fout = null;
                 
        try {
            fout = new java.io.FileOutputStream( this.file );
            
            // Create KeyStore
            this.keyStore = KeyStore.getInstance( "JKS" );
            this.keyStore.load( null, psw );
            
            // Create key
            CertAndKeyGen keypair = new CertAndKeyGen( "RSA", "SHA1WithRSA", null ); 
            X500Name x500Name = new X500Name(
                    this.keyInfo.getName(),
                    this.keyInfo.getOrgUnit(),
                    this.keyInfo.getCompany(),
                    this.keyInfo.getCity(),
                    this.keyInfo.getState(),
                    this.keyInfo.getCountryCode()
            );
            
            keypair.generate( keySize );
            PrivateKey privateKey = keypair.getPrivateKey();
            X509Certificate[] chain = new X509Certificate[ 1 ];
            chain[ 0 ] = keypair.getSelfCertificate(
                    x500Name,
                    new Date(),
                    35000 * 24L * 60L * 60L
            );
    
            // save key
            this.keyStore.setKeyEntry( this.keyInfo.getAlias(),
                    privateKey,
                    this.keyInfo.getPassword().toCharArray(),
                    chain
            );                    

            // store the keystore   
            this.keyStore.store( fout, psw );
        }
        finally {
            if ( fout != null ) {
                fout.close();
            }
        }
        
        return;
    }
    
    public KeyInfo load(String password) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException
    {
        FileInputStream fin = null;
        
        try {
            char[] psw = password.toCharArray();
            String alias = "";
            this.keyInfo = null;
            fin = new FileInputStream( file );

            // Load key store
            this.keyStore = KeyStore.getInstance( "JKS" );
            this.keyStore.load( fin, psw );
            
            // Get the certificate from the keystore
            if ( this.keyStore.size() > 0 ) {
                alias = this.keyStore.aliases().nextElement();
                X509Certificate cert = (X509Certificate) this.keyStore.getCertificate( alias );
                X500Principal principal = cert.getSubjectX500Principal();
                X500Name x500Info = X500Name.asX500Name( principal );
                // Now, lets decode it

                // Dump the info from the guts of the cert library
                this.keyInfo = new KeyInfo(
                                    alias,
                                    x500Info.getCommonName(),
                                    x500Info.getOrganizationalUnit(),
                                    x500Info.getOrganization(),
                                    x500Info.getLocality(),
                                    x500Info.getState(),
                                    x500Info.getCountry(),
                                    password
                );
            }
        }
        finally {
            if ( fin != null ) {
                fin.close();
            }
        }
        
        return this.keyInfo;
    }
    
    private File file;
    private KeyInfo keyInfo;
    private KeyStore keyStore;
}
