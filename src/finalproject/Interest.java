/**
 *  Program:    Interest.java
 *  Date:       April 27, 2020
 * 
 *  Interface with methods for calculating interest rate and 30-day projected 
 *  interest earnings. 
 * 
 *  Implemented as interface because Checking accounts can have interest on them 
 *  as well as Savings accounts.
 */

package finalproject;

/**
 *
 * @author Tyler
 */
public interface Interest {
    /**
     *  Abstract method to calculate interest rate.
     * 
     * @param type  {@code SavingsType} object representing which type of savings 
     *              account to create.
     */
    public void calcInterestRate(SavingsType type);
    
    /**
     *  Abstract method to calculate account balance after 30 days of interest 
     *  earnings.
     * 
     * @return array of double values.
     */
    public double[] projectedInterest30Days();
}