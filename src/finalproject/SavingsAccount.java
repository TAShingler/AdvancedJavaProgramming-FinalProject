/**
 *  Program:    SavingsAccount.java
 *  Date:       April 27, 2020
 * 
 *  SavingsAccount object definition. 
 *  
 *  SavingsAccount contains overloaded constructors: one that creates a basic 
 *  account and one that creates an account that calculates interest rate.
 */

package finalproject;

import java.text.DecimalFormat;

/**
 * @author Tyler
 */
public class SavingsAccount extends Account {
    // Used to format account number based on String
    private DecimalFormat df = new DecimalFormat("000");
    
    /**
     *  String value for what type of savings account was created.
     */
    String savingsType;
    
    /**
     *  String value for how often interest is calculated.
     */
    String termLengthName;
    
    /**
     *  Integer value representing how often interest is calculated.
     */
    int termLength;
    
    /**
     *  Double value for interest rate.
     */
    double interestRate;
    
    /**
     *  Double value for minimum account balance.
     */
    double minimumBalance;
    
    /**
     *  Integer value for amount of projections in 30-day period.
     */
    int projectionsCounter = 0;
    
    /**
     *  Double array to store 30-day interest earning projections.
     */
    double[] projections;

/*----- Constructors ---------------------------------------------------------*/
    /**
     *  Constructor for SavingsAccount. Constructor calls super class. 
     * 
     *  Constructor accepts 3 Strings, an int, and double as arguments.
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
    public SavingsAccount(int num, String fName, String lName, double bal) {
        super("Savings", num, fName, lName, bal);
    }

    /**
     *  Constructor for SavingsAccount. Constructor calls super class.
     * 
     *  Constructor accepts 2 Strings, an int, a double, a SavingsType item, and
     *  a TermLength item as arguments.
     * 
     * @param num{@code Integer} value representing the number associated 
     *              with his Account.
     * @param fName {@code String} value representing the Account holder's first
     *              name.
     * @param lName{@code String} value representing the Account holder's last
     *              name.
     * @param bal   {@code Double} value representing the balance associated 
     *              with this Account.
     * @param type  {@code SavingsType} object that represents the type of 
     *              SavingsAccount created.
     * @param length    {@code TermLengths} object representing the length of 
     *                  time between interest earnings calculations.
     */
    public SavingsAccount(int num, String fName, String lName, double bal, SavingsType type, TermLengths length) {
        super("Savings", num, fName, lName, bal);
        
        // Set SavingsType item name length to 15 characters
        StringBuilder sb = new StringBuilder(type.name());
        sb.setLength(15);
        savingsType = sb.toString();
        
        // Set TermLengths item name length to 7 characters
        sb = new StringBuilder(length.name());
        sb.setLength(7);
        termLengthName = sb.toString();
        
        // Call calcInterestRate() and setTermLength() to set interest rate and 
        // numeric value of term.
        calcInterestRate(type);
        setTermLength(length);
    }
    
/*----- Set Methods ----------------------------------------------------------*/
    /**
     *  Pre-Condition:  SavingsAccount object exists.
     * <br>
     *  Post-Condition: Type of savings account and interest rate set.
     * <p>
     *  Method sets the savings account type and calls calcInterestRate() to set 
     *  interest rate.
     * 
     * @param type  {@code SavingsType} object that represents the type of 
     *              SavingsAccount created.
     */
    public void setSavingsType(SavingsType type) {
        savingsType = type.name();
        
        calcInterestRate(type);
    }
    
    /**
     *  Pre-Condition:  SavingsAccount object exists.
     * <br>
     *  Post-Condition: Numeric value of term length set.
     * <p>
     *  Method sets the numeric value associated with the chosen term length.
     * 
     * @param t {@code TermLengths} object representing the length of time 
     *          between interest earnings calculations.
     */
    public void setTermLength(TermLengths t) {
        switch(t) {
            case DAILY:     
                termLength = 365;   
                break;
                
            case WEEKLY:    
                termLength = 52;    
                break;
                
            case MONTHLY:   
                termLength = 12;    
                break;
                
            case YEARLY:   
                termLength = 1;   
                break;
        }
    }

/*----- Get Methods ----------------------------------------------------------*/
    
    
/*----- Other Methods --------------------------------------------------------*/
    /**
     *  Pre-Condition:  SavingsAccount object exists.
     * <br>
     *  Post-Condition: String representation of SavingsAccount returned to 
     *                  calling class.
     * <p>
     *  Method returns a string representation of the created SavingsAccount to 
     *  the calling class. All fields are separated by a comma.
     * 
     * @return String representation of the created SavingsAccount.
     */
    @Override
    public String toString() {
        String s = super.toString() + delimiter
                + savingsType + delimiter 
                + termLengthName;
        return(s);
    }

    /**
     *  Pre-Condition:  SavingsAccount object exists.
     * <br>
     *  Post-Condition: Interest rate calculated based on SavingsAccount type.
     * <p>
     *  Method sets the interest rate of the savings account based on type of 
     *  SavingsAccount created. Method sets minimum balance needed to open 
     *  SavingsAccount.
     * 
     * @param type  {@code SavingsType} object that represents the type of 
     *              SavingsAccount created.
     */
    @Override
    public void calcInterestRate(SavingsType type) {
        switch(type) {
            case HIGH_INTEREST:
                interestRate = 0.015;
                minimumBalance = 100000.0;
                break;
                
            case MEDIUM_INTEREST:
                interestRate = 0.012;
                minimumBalance = 10000.0;
                break;
                
            case LOW_INTEREST:
                interestRate = 0.009;
                minimumBalance = 1000.0;
                break;
                
            default:
                break;
        }
    }

    /**
     *  Pre-Condition:  SavingsAccount object exists.
     * <br>
     *  Post-Condition: Array of double values returned to calling class.
     * <p>
     *  Method calculates 30-day projection of interest earnings (account 
     *  balance + interest) based on current account balance. Projected values 
     *  are stored in an array that is passed to the calling class.
     * 
     * @return  array of double values representing projected interest earnings.
     */
    @Override
    public double[] projectedInterest30Days() {
        double principal = accountBalance;
        double newBal = principal;
        projections = new double[30];
        projectionsCounter = 0;

        for (int i=1;i<projections.length+1;i++) {
            switch(termLength) {
                case 365:
                    newBal += (principal * (interestRate / termLength));
                    projectionsCounter++;
                    projections[i-1] = newBal;
                    break;

                case 52:
                    if (i % 7 == 0) {
                        newBal += (principal * (interestRate / termLength));
                        projectionsCounter++;
                        projections[projectionsCounter-1] = newBal;
                    }
                    break;

                case 12:
                    if (i % 30 == 0) {
                        newBal += (principal * (interestRate / termLength));
                        projectionsCounter++;
                        projections[projectionsCounter-1] = newBal;
                    }
                    break;

                case 1:
                    if (i % 365 == 0) {
                        newBal = (principal * (interestRate / termLength));
                        projectionsCounter++;
                        projections[projectionsCounter-1] = newBal;
                    }
                    break;
            }
            System.out.println("Day " + (i) + ": " + newBal);
        }
        
        return(projections);
    }
}