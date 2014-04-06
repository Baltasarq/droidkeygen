// KeyInfo.java

package droidkeygen.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Represents the information for the keystore
 * @author baltasarq
 */
public class KeyInfo {

    public KeyInfo( String alias, String name,
                     String orgUnit, String company,
                     String city, String state,
                     String countryCode, String password
    )
    {
         this.countryISOCodes = new ArrayList( Arrays.asList( Locale.getISOCountries() ) );

        this.setAlias( alias );
        this.setName( name );
        this.setOrgUnit( orgUnit );
        this.setCompany( company );
        this.setCity( city );
        this.setState( state );
        this.setCountryCode( countryCode );
        this.setPassword( password );
    }

    /**
     * @return the alias
     */
    public String getAlias()
    {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias)
    {
        alias = alias.trim();

        if ( alias.isEmpty() ) {
            throw new RuntimeException( "'alias' should not be empty" );
        }

        this.alias = alias;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        name = name.trim();

        if ( name.isEmpty() ) {
            throw new RuntimeException( "'name' should not be empty" );
        }

        this.name = name;
    }

    /**
     * @return the orgUnit
     */
    public String getOrgUnit()
    {
        return orgUnit;
    }

    /**
     * @param orgUnit the orgUnit to set
     */
    public void setOrgUnit(String orgUnit)
    {
        orgUnit = orgUnit.trim();

        if ( orgUnit.isEmpty() ) {
            throw new RuntimeException( "'organizational' unit should not be empty" );
        }

        this.orgUnit = orgUnit;
    }

    /**
     * @return the company
     */
    public String getCompany()
    {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company)
    {
        company = company.trim();

        if ( company.isEmpty() ) {
            throw new RuntimeException( "'company' should not be empty" );
        }

        this.company = company;
    }

    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city)
    {
        city = city.trim();

        if ( city.isEmpty() ) {
            throw new RuntimeException( "'city' should not be empty" );
        }

        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state)
    {
        state = state.trim();

        if ( state.isEmpty() ) {
            throw new RuntimeException( "'state' should not be empty" );
        }

        this.state = state;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode()
    {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode)
    {
        countryCode = countryCode.trim().toUpperCase();

        if ( countryCode.isEmpty() ) {
            throw new RuntimeException( "'country code' should not be empty" );
        }

        if ( this.countryISOCodes.indexOf( countryCode ) < 0 ) {
            throw new RuntimeException( "'country code' should be one of ISO-3166-2" );
        }

        this.countryCode = countryCode;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        password = password.trim();

        if ( password.isEmpty() ) {
            throw new RuntimeException( "'password' should not be empty" );
        }

        if ( password.length() < 6 ) {
            throw new RuntimeException( "'password' cannot be of less than 6 characters" );
        }

        this.password = password;
    }

    public static String[] getCountryCodes()
    {
        return Locale.getISOCountries();
    }

    private String alias;
    private String name;
    private String orgUnit;
    private String company;
    private String city;
    private String state;
    private String countryCode;
    private String password;
    private ArrayList countryISOCodes;
}
