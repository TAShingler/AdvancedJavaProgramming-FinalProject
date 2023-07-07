/**
 *  Program:    Account.java
 *  Date:       April 27, 2020
 * 
 *  Abstract class defining a bank account. Account.java implements Interest.java
 *  and has 2 subclasses: SavingsAccount.java and CheckingAccount.java.
 */

package finalproject;

import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * @author Tyler
 */
public abstract class Account implements Interest {
    // Format for balance to write to Account files
    private final String BALANCE_FORMAT = "0000000000.00";
    // Used to format account balance based on formatted String
    private DecimalFormat df = new DecimalFormat(BALANCE_FORMAT);
    
    /**
     *  String value to separate fields in String representation of Account object.
     */
    final String delimiter = ",";
    
    /**
     *  Integer value representing Account number.
     */
    int accountNumber;
    
    /**
     *  Double value representing Account balance.
     */
    double accountBalance;
    
    /**
     *  String value representing first name of Account holder.
     */
    String firstName;
    
    /**
     *  String value representing last name of Account holder.
     */
    String lastName;
    
    /**
     *  String value representing they type of Account created: savings or checking.
     */
    String accountType;
    
/*----- Constructors ---------------------------------------------------------*/
    /**
     *  Constructor for Account and all children. 
     * 
     *  Constructor accepts 3 Strings, an int, and double as arguments.
     * 
     * @param type  {@code String} value representing the tye of Account 
     *              created: savings or checking.
     * @param num   {@code Integer} value representing the number associated 
     *              with his Account.
     * @param fName {@code String} value representing the Account holder's first
     *              name.
     * @param lName{@code String} value representing the Account holder's last
     *              name.
     * @param bal   {@code Double} value representing the balance associated 
     *              with this Account.
     */
    public Account(String type, int num, String fName, String lName, double bal) {
        // Set Account type length to 8 characters
        StringBuilder sb = new StringBuilder(type);
        sb.setLength(8);
        
        accountType = sb.toString();
        accountNumber = num;
        firstName = fName;
        lastName = lName;
        accountBalance = bal;
    }
    
/*----- Set Methods ----------------------------------------------------------*/
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account number set.
     * <p>
     *  Method sets Account number based on int value passed to method.
     * 
     * @param num   {@code Integer} value representing Account number.
     */
    public void setAccountNumber(int num) {
        accountNumber = num;
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account holder first name set.
     * <p>
     *  Method sets the first name of the Account holder based on the String 
     *  value passed to the method.
     * 
     * @param fName {@code String} value representing the first name of the 
     *              account holder.
     */
    public void setFirstName(String fName) {
        firstName = fName;
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account holder last name set.
     * <p>
     *  Method sets the last name of the Account holder based on the String 
     *  value passed to the method.
     * 
     * @param lName {@code String} value representing the last name of the 
     *              account holder.
     */
    public void setLastName(String lName) {
        lastName = lName;
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account balance set.
     * <p>
     *  Method sets Account balance based on double value passed to method.
     * 
     * @param bal   {@code Double} value representing the Account balance.
     */
    public void setAccountBalance(double bal) {
        accountBalance = bal;
    }
    
/*----- Get Methods ----------------------------------------------------------*/
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account number passed to calling class.
     * <p>
     *  Method returns Account number to calling class.
     * 
     * @return int value.
     */
    public int getAccountNumber() {
        return accountNumber;
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account holder first name passed to calling class.
     * <p>
     *  Method returns Account holder's first name to calling class.
     * 
     * @return String value.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account holder last name passed to calling class.
     * <p>
     *  Method returns Account holder's last name to calling class.
     * 
     * @return String value.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account balance passed to calling class.
     * <p>
     *  Method returns Account balance to calling class.
     * 
     * @return double value.
     */
    public double getAccountBalance() {
        return accountBalance;
    }
    
/*----- Other Methods --------------------------------------------------------*/
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: String representation of Account returned to calling 
     *                  class.
     * <p>
     *  Method returns a string representation of the created Account to the 
     *  calling class. All fields are separated by a comma.
     * 
     * @return String representation of the created Account.
     */
    @Override
    public String toString() {
        String s = accountType + delimiter 
                + String.format("%03d", accountNumber) + delimiter 
                + firstName + delimiter 
                + lastName + delimiter 
                + df.format(accountBalance);
        
        return(s);
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account balance increased by deposit amount.
     * <p>
     *  Method adds the double value passed to the method to the existing 
     *  Account balance.
     * 
     * @param deposit   {@code Double} value representing the amount to deposit.
     */
    public void deposit(double deposit) {
        accountBalance += deposit;
    }
    
    /**
     *  Pre-Condition:  Account object exists.
     * <br>
     *  Post-Condition: Account balance decreased by deposit amount.
     * <p>
     *  Method subtracts the double value passed to the method from the existing 
     *  Account balance.
     * 
     * @param withdrawal    {@code Double} value representing the amount to 
     *                      withdraw.
     */
    public void withdraw(double withdrawal) {
        accountBalance -= withdrawal;
    }
}