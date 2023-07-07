/**
 *  Program:    CheckingAccount.java
 *  Date:       April 27, 2020
 * 
 *  CheckingAccount object definition.
 */

package finalproject;

/**
 * @author Tyler
 */
public class CheckingAccount extends Account {
/*----- Constructors ---------------------------------------------------------*/
    /**
     *  Constructor for CheckingAccount. Constructor calls super class. 
     * 
     *  Constructor accepts 2 Strings, an int, and double as arguments.
     * 
     * @param num   {@code Integer} value representing the number associated 
     *              with his Account.
     * @param fName {@code String} value representing the Account holder's first
     *              name.
     * @param lName{@code String} value representing the Account holder's last
     *              name.
     * @param bal   {@code Double} value representing the balance associated 
     *              with this Account.
     */
    public CheckingAccount(int num, String fName, String lName, double bal) {
        super("Checking", num, fName, lName, bal);
    }

/*----- Set Methods ----------------------------------------------------------*/
    
/*----- Get Methods ----------------------------------------------------------*/
    
/*----- Other Methods --------------------------------------------------------*/
    /**
     *  Currently, only used in SavingsAccount.java. Might implement later.
     * 
     * @param type
     */
    @Override
    public void calcInterestRate(SavingsType type) {
        
    }

    /**
     *  Currently, only used in SavingsAccount.java. Might implement later.
     * 
     * @return
     */
    @Override
    public double[] projectedInterest30Days() {
        return(new double[0]);
    }

}