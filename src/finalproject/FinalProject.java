/**
 *  Program:    FinalProject.java
 *  Date:       April 27, 2020
 * 
 *  Program allows user to create Savings accounts and checking accounts, saving
 *  them to separate files, which can be edited and deleted. The saved account 
 *  data can be displayed to the user in a JTable object.
 */

package finalproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *  @author Tyler
 */
public class FinalProject extends JFrame implements ActionListener {
    final String accNumLabelText = "Account Number:";
    final String fNameLabelText = "First Name:";
    final String lNameLabelText = "Lst Name:";
    final String accBalLabelText = "Account Balance:";
    final String savingsAccountTypeLabelText = "Savings Account Type:";
    final String termLengthLabelText = "Term Length:";
    final String sortFieldLabelText = "Field to Sort By";
    final String sortingLabelText = "Field Sort Option";
    final String[] frameNames = {
        "DEFAULT",
        "CREATE_SAVINGS",
        "EDIT_SAVINGS",
        "DELETE_SAVINGS",
        "CREATE_CHECKING",
        "EDIT_CHECKING",
        "DELETE_CHECKING"
    };
    int savingsAccountsInArray = 0, checkingAccountsInArray = 0;
    
/*----- GENERAL ACCOUNT data -------------------------------------------------*/
    static final int MAX_ACCOUNTS_AMOUNT = 1000;
    static final int MAX_NAME_SIZE = 24;
    static final String BALANCE_FORMAT = "0000000000.00";
    static final double MAX_BALANCE = 2000000000.00;
    static final String RATE_FORMAT = "0.000";
    static final String ACC_NUM_FORMAT = "000";
    static final String NAME_FORMAT = "                        ";
    static final String ACC_TYPE_FORMAT = "        ";
    static final String SAVINGS_TYPE_FORMAT = "               ";
    static final String TERM_LENGTH_FORMAT = "       ";
    static final String DELIMITER = ",";
    
/*----- SAVINGS_ACCOUNT data -------------------------------------------------*/
    static final int SAV_ACC_TYPE_NAME_MAX_LENGTH = 15;
    static final int SAV_TERM_NAME_MAX_LENGTH = 7;
    static final double SAV_MAX_RATE = 0.015;
    static final double SAV_MIN_RATE = 0.009;
    static final String SAV_MIN_BALANCE_NEEDED_FORMAT = "000000.00";
    static final int MAX_SAVINGS_FIELDS = 6;
    static final String SAVINGS_ACCOUNT_DATA_FORMAT = 
            ACC_TYPE_FORMAT + DELIMITER + ACC_NUM_FORMAT + DELIMITER + NAME_FORMAT + DELIMITER + NAME_FORMAT + DELIMITER + 
            BALANCE_FORMAT + DELIMITER + SAVINGS_TYPE_FORMAT + DELIMITER + TERM_LENGTH_FORMAT + System.getProperty("line.separator");    
    static final int SAVINGS_ACCOUNT_DATA_LENGTH = SAVINGS_ACCOUNT_DATA_FORMAT.length();
    
/*----- CHECKING_ACCOUNT data ------------------------------------------------*/
    static final int MAX_CHECKING_FIELDS = 4;
    static final String CHECKING_ACCOUNT_DATA_FORMAT = 
            ACC_TYPE_FORMAT + DELIMITER + ACC_NUM_FORMAT + DELIMITER + NAME_FORMAT 
            + DELIMITER + NAME_FORMAT + DELIMITER + BALANCE_FORMAT + System.getProperty("line.separator");
    static final int CHECKING_ACCOUNT_DATA_LENGTH = CHECKING_ACCOUNT_DATA_FORMAT.length();
    
    static String[][] savingsAccountsData; // for table
    final String[] savingsTableHeaders = {
        "Acc. Number", 
        "First Name", 
        "Last Name", 
        "Acc. Balance",
        "Savings Type",
        "Term Length",
    };
    
    static String[][] checkingAccountsData;    // for table
    final String[] checkingTableHeaders = {
        "Acc. Number", 
        "First Name", 
        "Last Name", 
        "Acc. Balance"
    };
    
    static final Path savingsFp = Paths.get(
            "C:\\COMT\\Java\\IT-36311\\FinalProject\\ExtraFiles\\SAVINGS_ACCOUNTS.dat");
    static final Path checkingFp = Paths.get(
            "C:\\COMT\\Java\\IT-36311\\FinalProject\\ExtraFiles\\CHECKING_ACCOUNTS.dat");
    
/*----- JFrame Compopnents ---------------------------------------------------*/
    JMenuBar menuBar = new JMenuBar();
    
    JMenu savingsMenu = new JMenu("Savings");
    JMenuItem newSavingsRecord = new JMenuItem("New Account");
    JMenuItem editSavingsRecord = new JMenuItem("Edit Account");
    JMenuItem deleteSavingsRecord = new JMenuItem("Delete Account");
    
    JMenu checkingMenu = new JMenu("Checking");
    JMenuItem newCheckingRecord = new JMenuItem("New Account");
    JMenuItem editCheckingRecord = new JMenuItem("Edit Account");
    JMenuItem deleteCheckingRecord = new JMenuItem("Delete Account");
    
    JMenu tableMenu = new JMenu("Display Accounts");
    JMenuItem displaySavingsAccounts = new JMenuItem("Savings");
    JMenuItem displayCheckingAccounts = new JMenuItem("Checking");
    
    JLabel accountNumberLabel;
    JLabel firstNameLabel;
    JLabel lastNameLabel;
    JLabel accountBalanceLabel;
    JLabel savingsAccountTypeLabel;
    JLabel termLengthLabel;
    JLabel sortFieldLabel;
    JLabel sortingLabel;
    final JLabel defaultFrameInstructions = new JLabel("Choose an option from the menu bar at the top of the window to begin.", SwingConstants.CENTER);
    
    JTextField accountNumberField;
    JTextField firstNameField;
    JTextField lastNameField;
    JTextField accountBalanceField;
    
    JComboBox savingsAccountTypeCombo = new JComboBox(SavingsType.values());
    JComboBox termLengthCombo = new JComboBox(TermLengths.values());
    
    JPanel accountNumberPanel;
    JPanel firstNamePanel;
    JPanel lastNamePanel;
    JPanel accountBalancePanel;
    JPanel savingsAccountTypePanel;
    JPanel termLengthPanel;
    JPanel savingsCreatePanel;
    JPanel checkingCreatePanel;
    JPanel savingsEditPanel;
    JPanel checkingEditPanel;
    JPanel savingsDeletePanel;
    JPanel checkingDeletePanel;
    JPanel frameButtonsPanel;
    
    JButton addButton;
    JButton editButton;
    JButton deleteButton;
    JButton clearButton;
    
    JTable accountRecords;
    
    JScrollPane tableScrollPane;
    
    static JFrame frame;
    JDialog tableWindow;
    
    // Used to create SAVINGS_ACCOUNT.txt and CHECKING_ACCOUNT.txt
    static {
        OutputStream fileOut;
        BufferedWriter writer;
        
        // SAVINGS_ACCOUNTS.dat
        if (!Files.exists(savingsFp)) {
            try {
                // Create file if it does not exist, then append data to the end of the file
                fileOut = new BufferedOutputStream(Files.newOutputStream(savingsFp, CREATE, APPEND));

                // Instantiate writer object to write to file
                writer = new BufferedWriter(new OutputStreamWriter(fileOut));

                // Write blank savings account to SAVINGS_ACCOUNTS.dat
                for (int i=0;i<MAX_ACCOUNTS_AMOUNT;i++) {
                    writer.write(SAVINGS_ACCOUNT_DATA_FORMAT);
                }

                // Close writer
                writer.close();

            } catch(IOException e) {
                JOptionPane.showMessageDialog(null, (savingsFp.getFileName() + " could not be created."), 
                        "I/O Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        
        // CHECKING_ACCOUNTS.dat
        if (!Files.exists(checkingFp)) {
            try {
                    // Create file if it does not exist, then append data to the end of the file
                    fileOut = new BufferedOutputStream(Files.newOutputStream(checkingFp, CREATE, APPEND));

                    // Instantiate writer object to write to file
                    writer = new BufferedWriter(new OutputStreamWriter(fileOut));

                    // Write blank checking accounts to CHECKING_ACCOUNTS.dat
                    for (int i=0;i<MAX_ACCOUNTS_AMOUNT;i++) {
                        writer.write(CHECKING_ACCOUNT_DATA_FORMAT);
                    }

                    // Close writer
                    writer.close();

            } catch(IOException e) {
                JOptionPane.showMessageDialog(null, (checkingFp.getFileName() + " could not be created."), 
                        "I/O Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
/*----- Constructors ---------------------------------------------------------*/
    /**
     *  JFrame constructor for FinalProject. Constructor accepts a FrameType and
     *  ActionEvent object as arguments.
     * <p>
     *  Constructor creates JFrame for creating, editing, or deleting savings
     *  account or checking account.
     *  
     * @param fT    {@code FrameType} object that determines which JFrame gets
     *              created.
     * @param act   {@code AccountAction} object that determines which JFrame
     *              gets created.
     */
    public FinalProject(FrameType fT, AccountAction act) {
        super("IT-36311 Final Project: Faux Monetary Cache");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        savingsMenu.add(newSavingsRecord);
        savingsMenu.add(editSavingsRecord);
        savingsMenu.add(deleteSavingsRecord);
        
        checkingMenu.add(newCheckingRecord);
        checkingMenu.add(editCheckingRecord);
        checkingMenu.add(deleteCheckingRecord);
        
        tableMenu.add(displaySavingsAccounts);
        tableMenu.add(displayCheckingAccounts);
        
        menuBar.add(savingsMenu);
        menuBar.add(checkingMenu);
        menuBar.add(tableMenu);
        add(menuBar, BorderLayout.NORTH);
        
        accountNumberLabel = new JLabel(accNumLabelText);
        firstNameLabel = new JLabel(fNameLabelText);
        lastNameLabel = new JLabel(lNameLabelText);
        accountBalanceLabel = new JLabel(accBalLabelText);
        savingsAccountTypeLabel = new JLabel(savingsAccountTypeLabelText);
        termLengthLabel = new JLabel(termLengthLabelText);
        sortFieldLabel = new JLabel(sortFieldLabelText);
        sortingLabel = new JLabel(sortingLabelText);
        
        accountNumberField = new JTextField();
        accountNumberField.setToolTipText("Max account number: " + (MAX_ACCOUNTS_AMOUNT - 1));
        accountNumberField.setEditable(true);
        firstNameField = new JTextField();
        firstNameField.setToolTipText("Maximum characters: " + MAX_NAME_SIZE);
        lastNameField = new JTextField();
        lastNameField.setToolTipText("Maximum characters: " + MAX_NAME_SIZE);
        accountBalanceField = new JTextField();
        accountBalanceField.setToolTipText("Maximum account balance: $2 billion");
        
        accountNumberPanel = new JPanel(new GridLayout(1, 2));
        firstNamePanel = new JPanel(new GridLayout(1, 2));
        lastNamePanel = new JPanel(new GridLayout(1, 2));
        accountBalancePanel = new JPanel(new GridLayout(1, 2));
        savingsAccountTypePanel = new JPanel(new GridLayout(1, 2));
        termLengthPanel = new JPanel(new GridLayout(1, 2));
        frameButtonsPanel = new JPanel(new GridLayout(1, 2, 50, 0));
        
        addButton = new JButton("Create Account");
        editButton = new JButton("Update Account");
        deleteButton = new JButton("Delete Account");
        clearButton = new JButton("Clear All Fields");
        
        accountNumberPanel.add(accountNumberLabel);
        accountNumberPanel.add(accountNumberField);
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameField);
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameField);
        accountBalancePanel.add(accountBalanceLabel);
        accountBalancePanel.add(accountBalanceField);
        savingsAccountTypePanel.add(savingsAccountTypeLabel);
        savingsAccountTypePanel.add(savingsAccountTypeCombo);
        termLengthPanel.add(termLengthLabel);
        termLengthPanel.add(termLengthCombo);
        
        // Create UI based on type of frame needed: default, create savings, 
        // edit savings, delete savings, create checking, edit checking, or 
        // delete checking
        switch(fT) {
            case DEFAULT: {
                setName(frameNames[0]);
                setSize(500, 200);
                defaultFrameInstructions.setPreferredSize(new Dimension(100, 50));
                
                add(defaultFrameInstructions, BorderLayout.CENTER);
                
                break;
            }
            
            case SAVINGS: {
                setSize(500, 500);
                
                switch(act) {
                    case CREATE: {
                        setName(frameNames[1]);
                        savingsCreatePanel = new JPanel(new GridLayout(6, 1, 0, 20));
                        
                        frameButtonsPanel.add(addButton);
                        frameButtonsPanel.add(clearButton);
                        
                        savingsCreatePanel.add(accountNumberPanel);
                        savingsCreatePanel.add(firstNamePanel);
                        savingsCreatePanel.add(lastNamePanel);
                        savingsCreatePanel.add(accountBalancePanel);
                        savingsCreatePanel.add(savingsAccountTypePanel);
                        savingsCreatePanel.add(termLengthPanel);

                        savingsCreatePanel.setBorder(new EmptyBorder(30, 25, 30, 25));
                        frameButtonsPanel.setPreferredSize(new Dimension(WIDTH, 100));
                        frameButtonsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
                        add(savingsCreatePanel, BorderLayout.CENTER);
                        add(frameButtonsPanel, BorderLayout.SOUTH);
                        
                        addButton.addActionListener(this);
                        clearButton.addActionListener(this);
                    }   break;
                        
                    case EDIT: {
                        setName(frameNames[2]);
                        savingsEditPanel = new JPanel(new GridLayout(6, 1, 0, 20));
                        
                        frameButtonsPanel.add(editButton);
                        frameButtonsPanel.add(clearButton);
                        
                        savingsEditPanel.add(accountNumberPanel);
                        savingsEditPanel.add(firstNamePanel);
                        savingsEditPanel.add(lastNamePanel);
                        savingsEditPanel.add(accountBalancePanel);
                        savingsEditPanel.add(savingsAccountTypePanel);
                        savingsEditPanel.add(termLengthPanel);
                        
                        savingsEditPanel.setBorder(new EmptyBorder(30, 25, 30, 25));
                        frameButtonsPanel.setPreferredSize(new Dimension(WIDTH, 100));
                        frameButtonsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
                        add(savingsEditPanel, BorderLayout.CENTER);
                        add(frameButtonsPanel, BorderLayout.SOUTH);
                        
                        editButton.addActionListener(this);
                        clearButton.addActionListener(this);
                    }   break;
                        
                    case DELETE: {
                        setName(frameNames[3]);
                        setSize(400, 300);
                        
                        savingsDeletePanel = new JPanel(new GridLayout(1, 1));
                        
                        frameButtonsPanel = new JPanel(new GridLayout(1, 1));
                        frameButtonsPanel.add(deleteButton);
                        
                        savingsDeletePanel.add(accountNumberPanel);

                        savingsDeletePanel.setBorder(new EmptyBorder(54, 25, 54, 25));
                        frameButtonsPanel.setPreferredSize(new Dimension(WIDTH, 100));
                        frameButtonsPanel.setBorder(new EmptyBorder(25, 75, 25, 75));
                        add(savingsDeletePanel, BorderLayout.CENTER);
                        add(frameButtonsPanel, BorderLayout.SOUTH);
                        
                        deleteButton.addActionListener(this);
                    }   break;
                }
            }   break;
                
            case CHECKING: {
                setSize(500, 400);
                
                switch(act) {
                    case CREATE: {
                        setName(frameNames[4]);
                        checkingCreatePanel = new JPanel(new GridLayout(4, 1, 0, 15));
                        
                        frameButtonsPanel.add(addButton);
                        frameButtonsPanel.add(clearButton);

                        checkingCreatePanel.add(accountNumberPanel);
                        checkingCreatePanel.add(firstNamePanel);
                        checkingCreatePanel.add(lastNamePanel);
                        checkingCreatePanel.add(accountBalancePanel);

                        checkingCreatePanel.setBorder(new EmptyBorder(40, 25, 40, 25));
                        frameButtonsPanel.setPreferredSize(new Dimension(WIDTH, 100));
                        frameButtonsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
                        add(checkingCreatePanel, BorderLayout.CENTER);
                        add(frameButtonsPanel, BorderLayout.SOUTH);
                        
                        addButton.addActionListener(this);
                        clearButton.addActionListener(this);
                    }   break;
                        
                    case EDIT: {
                        setName(frameNames[5]);
                        checkingEditPanel = new JPanel(new GridLayout(4, 1, 0, 15));
                        
                        frameButtonsPanel.add(editButton);
                        frameButtonsPanel.add(clearButton);

                        checkingEditPanel.add(accountNumberPanel);
                        checkingEditPanel.add(firstNamePanel);
                        checkingEditPanel.add(lastNamePanel);
                        checkingEditPanel.add(accountBalancePanel);

                        checkingEditPanel.setBorder(new EmptyBorder(40, 25, 40, 25));
                        frameButtonsPanel.setPreferredSize(new Dimension(WIDTH, 100));
                        frameButtonsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
                        add(checkingEditPanel, BorderLayout.CENTER);
                        add(frameButtonsPanel, BorderLayout.SOUTH);
                        
                        editButton.addActionListener(this);
                        clearButton.addActionListener(this);
                    }   break;
                        
                    case DELETE: {
                        setName(frameNames[6]);
                        setSize(400, 300);
                        
                        checkingDeletePanel = new JPanel(new GridLayout(1, 1));
                        
                        frameButtonsPanel = new JPanel(new GridLayout(1, 1));
                        frameButtonsPanel.add(deleteButton);
                        
                        checkingDeletePanel.add(accountNumberPanel);

                        checkingDeletePanel.setBorder(new EmptyBorder(54, 25, 54, 25));
                        frameButtonsPanel.setPreferredSize(new Dimension(WIDTH, 100));
                        frameButtonsPanel.setBorder(new EmptyBorder(25, 75, 25, 75));
                        add(checkingDeletePanel, BorderLayout.CENTER);
                        add(frameButtonsPanel, BorderLayout.SOUTH);
                        
                        deleteButton.addActionListener(this);
                    }   break;
                }
            }   break;
        }
        
        // Add listeners to JMenuItems
        newSavingsRecord.addActionListener(this);
        editSavingsRecord.addActionListener(this);
        deleteSavingsRecord.addActionListener(this);
        
        newCheckingRecord.addActionListener(this);
        editCheckingRecord.addActionListener(this);
        deleteCheckingRecord.addActionListener(this);
        
        displaySavingsAccounts.addActionListener(this);
        displayCheckingAccounts.addActionListener(this);
    }
    
    public static void main(String[] args) {
        frame = new FinalProject(FrameType.DEFAULT, AccountAction.CREATE);
        frame.setVisible(true);
    }
    
/*----- Method: DisplayAccounts() --------------------------------------------*/
    /**
     *  Pre-Condition:  JFrame exists.
     * <br>
     *  Post-Condition: JDialog containing JTable of savings accounts data or 
     *                  checking accounts data.
     * <p>
     *  Method creates a JDialog window that displays saved data for savings 
     *  accounts or checking accounts to the user in a JTable.
     * 
     * @param tableType {@code String} value that represents which type of table 
     *                  to create: savings or checking.
     */
    public void DisplayAccounts(String tableType) {
        // Create JDialog and JTable based on String passed to method
        switch(tableType) {
            case "Savings": {
                tableWindow = new JDialog(frame, tableType, true);
                tableWindow.setName("savingsTable");
                tableWindow.setSize(((MAX_SAVINGS_FIELDS+1) * 100), 300);
                tableWindow.setLayout(new BorderLayout());
                
                // Populate savingsAccountsData array with SavingsAccount data,
                // then create JTable based on data
                ReadFromFile(tableType);
                accountRecords = new JTable(savingsAccountsData, savingsTableHeaders);
                
                tableScrollPane = new JScrollPane(accountRecords);
            } break;
            
            case "Checking": {
                tableWindow = new JDialog(frame, tableType, true);
                tableWindow.setName("checkingTable");
                tableWindow.setSize(((MAX_CHECKING_FIELDS+1) * 100), 300);
                
                // Populate checkingAccountsData array with ChackingAccount data,
                // then create JTable based on data
                ReadFromFile(tableType);
                accountRecords = new JTable(checkingAccountsData, checkingTableHeaders);
                
                tableScrollPane = new JScrollPane(accountRecords);
            } break;
        }
        
        // Make each column in table sortable by either ascending or descending order
        accountRecords.setAutoCreateRowSorter(true);
        accountRecords.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        tableWindow.add(tableScrollPane, BorderLayout.CENTER);
        tableWindow.setLocationRelativeTo(frame);
        tableWindow.setVisible(true);
    }
    
/*----- Method: CreateAccount() ----------------------------------------------*/
    /**
     *  Pre-Condition:  JFrame exists and all fields in frame properly filled.
     * <br>
     *  Post-Condition: SavingsAccount or CheckingAccount data added to 
     *                  appropriate file.
     * <p>
     *  Method creates a SavingsAccount or CheckingAccount based on which JFrame 
     *  exists, then saves the data to the appropriate file.
     * 
     * @param frameName {@code String} value that determines which type of 
     *                  Account to create.
     */
    public void CreateAccount(String frameName) {
        if (frameName.equals(frameNames[1])) {
            // Get data from all fields in Create Savings JFrame
            String  accNum = accountNumberField.getText(),
                    accBal = accountBalanceField.getText();
            StringBuilder fName = new StringBuilder(firstNameField.getText());
            StringBuilder lName = new StringBuilder(lastNameField.getText());
            Object  sType= (SavingsType)(savingsAccountTypeCombo.getSelectedItem()); 
            Object  term = (TermLengths)(termLengthCombo.getSelectedItem());
            double  parsedBal = 0;  // Holds parsed account balance
            int parsedAccNum = 0;   // Holds parsed account number
            SavingsAccount acc;     // Used to create SavingsAccount object to save to SAVINGS_ACCOUNTS.dat

            // Set name lengths to 24 characters
            fName.setLength(MAX_NAME_SIZE);
            lName.setLength(MAX_NAME_SIZE);
            
            // Try to parse Account number to int data type
            try {
                parsedAccNum = Integer.parseInt(accNum);
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, 
                        "Failed to parse " + accNum + " to integer value. Enter a valid account number.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Try to parse Account balance and write SavingsAccount data to SAVINGS_ACCOUNT.dat
            try {
                parsedBal = Double.parseDouble(accBal);
                
                if (parsedBal > MAX_BALANCE) {
                    JOptionPane.showMessageDialog(frame, 
                            "Balance entered is greater than the maximum value: " + MAX_BALANCE + ". Enter a valid account balance.", 
                            "Balance Too Large", JOptionPane.ERROR_MESSAGE);
                } else {
                    acc = new SavingsAccount(parsedAccNum, fName.toString(), lName.toString(), parsedBal, 
                            SavingsType.valueOf(sType.toString()), TermLengths.valueOf(term.toString()));
                    
                    WriteToFile(acc, parsedAccNum);
                    JOptionPane.showMessageDialog(frame, "Account successfully saved to file.");
                    return;
                }
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, 
                        "Failed to parse " + accBal + " to decimal value. Enter a valid floating-point value.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
                return;
            } catch(IOException e) {
                JOptionPane.showMessageDialog(frame, "Unable to create account.", "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if (frameName.equals(frameNames[4])) {
            // Get data from all fields in Create Checking JFrame
            String  accNum = accountNumberField.getText(),
                    accBal = accountBalanceField.getText();
            StringBuilder fName = new StringBuilder(firstNameField.getText());
            StringBuilder lName = new StringBuilder(lastNameField.getText());
            double  parsedBal = 0;  // Holds parsed account balance
            int parsedAccNum = 0;   // Holds parsed account number
            CheckingAccount acc;    // Used to create CheckingsAccount object to save to CHECKING_ACCOUNTS.dat

            // Set name lengths to 24 characters
            fName.setLength(MAX_NAME_SIZE);
            lName.setLength(MAX_NAME_SIZE);
            
            // Try to parse Account number to int data type
            try {
                parsedAccNum = Integer.parseInt(accNum);
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, 
                        "Failed to parse " + accNum + " to integer value. Enter a valid account number.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Try to parse Account balance and write CheckingAccount data to CHECKING_ACCOUNT.dat
            try {
                parsedBal = Double.parseDouble(accBal);
                
                if (parsedBal > MAX_BALANCE) {
                    JOptionPane.showMessageDialog(frame, 
                            "Balance entered is greater than the maximum value: $2 Billion. Enter a valid account balance.", 
                            "Balance Too Large", JOptionPane.ERROR_MESSAGE);
                } else {
                    acc = new CheckingAccount(parsedAccNum, fName.toString(), lName.toString(), parsedBal);
                    
                    WriteToFile(acc, parsedAccNum);
                    JOptionPane.showMessageDialog(frame, "Account successfully saved to file.");
                }
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, 
                        "Failed to parse " + accountBalanceField.getText() + " to floating-point value. Enter a valid floating-point value.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
            } catch(IOException e) {
                JOptionPane.showMessageDialog(frame, "Unable to create account.", "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
/*----- Method: DeleteAccount() ----------------------------------------------*/
    /**
     *  Pre-Condition:  JFrame exists and all fields in frame properly filled.
     * <br>
     *  Post-Condition: Account associated with entered account number deleted 
     *                  from appropriate file.
     * <p>
     *  Method deletes account data saved in SAVINGS_ACCOUNT.dat or 
     *  CHECKING_ACCOUNTS.dat based on entered account number.
     * 
     * @param accNum    {@code String} value representing account number of 
     *                  Account to delete;
     */
    public void DeleteAccount(String accNum) {
        int lineInFile;

        try {
            // Get line in file (based on entered account number) to set position to
            lineInFile = Integer.parseInt(accNum);
            
            // Savings
            if (frame.getName().equals(frameNames[3])) {
                // Declare variables for FileChannel object, ByteBuffer object
                FileChannel fc;
                ByteBuffer buffer;

                // byte array for bytes written to file
                byte[] dataToFile = SAVINGS_ACCOUNT_DATA_FORMAT.getBytes();
                
                // Instantiate FileChannel object to read from and write to file 
                fc = (FileChannel)Files.newByteChannel(savingsFp, READ, WRITE);

                // Instantiate ByteBuffer object to write bytes from String 
                // SAVINGS_ACCOUNT_DATA_FORMAT in byte array dataToFile to SAVINGS_ACCOUNT.dat
                buffer = ByteBuffer.wrap(dataToFile);

                // Set position in file to write to
                fc.position(lineInFile * SAVINGS_ACCOUNT_DATA_LENGTH);

                // Write blank account data to file
                fc.write(buffer);

                // Close FileChannel
                fc.close();
                
                JOptionPane.showMessageDialog(frame, "Account succesfully deleted.");
                
            // Checking
            } else if (frame.getName().equals(frameNames[6])) {
                // Declare variables for FileChannel object, ByteBuffer object
                FileChannel fc;
                ByteBuffer buffer;

                // byte array for bytes written to file
                byte[] dataToFile = CHECKING_ACCOUNT_DATA_FORMAT.getBytes();
        
                // Instantiate FileChannel object to read from and write to file 
                fc = (FileChannel)Files.newByteChannel(checkingFp, READ, WRITE);
                
                // Instantiate ByteBuffer object to write bytes from String 
                // CHECKING_ACCOUNT_DATA_FORMAT in byte array dataToFile to CHECKING_ACCOUNT.dat
                buffer = ByteBuffer.wrap(dataToFile);

                // Set position in file to write to
                fc.position(lineInFile * CHECKING_ACCOUNT_DATA_LENGTH);

                // Write blank account data to file
                fc.write(buffer);

                // Close FileChannel
                fc.close();
                
                JOptionPane.showMessageDialog(frame, "Account succesfully deleted.");
            }
            
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, 
                    ("Unable to parsed account number to an integer value. Enter a valid account number following format: " 
                            + ACC_NUM_FORMAT), 
                    "Fail to Parse", JOptionPane.ERROR_MESSAGE);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(frame, 
                    ("Unable to parsed account number to delete account from file." 
                            + ACC_NUM_FORMAT), 
                    "IO Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
/*----- Method: EditAccount() ------------------------------------------------*/
    /**
     *  Pre-Condition:  JFrame exists and all fields in frame properly filled.
     * <br>
     *  Post-Condition: Account associated with entered account number edited 
     *                  in appropriate file.
     * <p>
     *  Method edits account data saved in SAVINGS_ACCOUNT.dat or 
     *  CHECKING_ACCOUNTS.dat based on entered account number using entered data
     *  in JFrame fields.
     * 
     * @param accNum    {@code String} value representing account number of 
     *                  Account to edit;
     */
    public void EditAccount(String accNum) {
        // Savings
        if (frame.getName().equals(frameNames[2])) {
            // Get data from all fields in Edit Savings JFrame
            String accBal = accountBalanceField.getText();
            StringBuilder fName = new StringBuilder(firstNameField.getText());
            StringBuilder lName = new StringBuilder(lastNameField.getText());
            Object  sType= (SavingsType)(savingsAccountTypeCombo.getSelectedItem()); 
            Object  term = (TermLengths)(termLengthCombo.getSelectedItem());
            double  parsedBal = 0;  // Holds parsed account balance
            int parsedAccNum = 0;   // Holds parsed account number
            SavingsAccount acc;     // Used to create SavingsAccount object to save to SAVINGS_ACCOUNTS.dat
            
            // Set name lengths to 24 characters
            fName.setLength(MAX_NAME_SIZE);
            lName.setLength(MAX_NAME_SIZE);
            
            // Try to parse Account number to int data type
            try {
                parsedAccNum = Integer.parseInt(accNum);
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, 
                        "Failed to parse " + accNum + " to integer value. Enter a valid account number.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Try to parse Account balance and write SavingsAccount data to SAVINGS_ACCOUNT.dat
            try {
                parsedBal = Double.parseDouble(accBal);
                
                if (parsedBal > MAX_BALANCE) {
                    JOptionPane.showMessageDialog(frame, 
                            "Balance entered is greater than the maximum value: $2 Billion. Enter a valid account balance.", 
                            "Balance Too Large", JOptionPane.ERROR_MESSAGE);
                } else {
                    acc = new SavingsAccount(parsedAccNum, fName.toString(), lName.toString(), parsedBal, 
                                SavingsType.valueOf(sType.toString()), TermLengths.valueOf(term.toString()));

                    WriteEditToFile(acc, parsedAccNum);

                    JOptionPane.showMessageDialog(frame, "Account successfully edited.");
                }
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Failed to parse " + accNum + 
                        " to integer value. Enter a valid account number.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
            } catch(IOException e) {
                JOptionPane.showMessageDialog(frame, "Unable to edit account. Account does not exist or names were entered wrong.", "IO Error", JOptionPane.ERROR_MESSAGE);
            } catch(NullPointerException e) {
                JOptionPane.showMessageDialog(frame, "Account with number " + accNum + " does not exist. Enter a valid account number.", 
                        "Account does not Exist", JOptionPane.ERROR_MESSAGE);
            }
            
        // Checking
        } else if (frame.getName().equals(frameNames[5])) {
            // Get data from all fields in Edit Checking JFrame
            String accBal = accountBalanceField.getText();
            StringBuilder fName = new StringBuilder(firstNameField.getText());
            StringBuilder lName = new StringBuilder(lastNameField.getText());
            double  parsedBal = 0;  // Holds parsed account balance
            int parsedAccNum = 0;   // Holds parsed account number
            CheckingAccount acc;    // Used to create Checking Account object to save to CHECKING_ACCOUNTS.dat

            // Set name lengths to 24 characters
            fName.setLength(MAX_NAME_SIZE);
            lName.setLength(MAX_NAME_SIZE);
            
            // Try to parse Account number to int data type
            try {
                parsedAccNum = Integer.parseInt(accNum);
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, 
                        "Failed to parse " + accNum + " to integer value. Enter a valid account number.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Try to parse Account balance and write CheckingAccount data to CHECKING_ACCOUNT.dat
            try {
                parsedBal = Double.parseDouble(accBal);
                
                if (parsedBal > MAX_BALANCE) {
                    JOptionPane.showMessageDialog(frame, 
                            "Balance entered is greater than the maximum value: $2 Billion. Enter a valid account balance.", 
                            "Balance Too Large", JOptionPane.ERROR_MESSAGE);
                } else {
                    acc = new CheckingAccount(parsedAccNum, fName.toString(), lName.toString(), parsedBal);

                    WriteEditToFile(acc, parsedAccNum);

                    JOptionPane.showMessageDialog(frame, "Account successfully edited.");
                }
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Failed to parse " + accNum + 
                        " to integer value. Enter a valid account number.", 
                        "Fail to Parse", JOptionPane.ERROR_MESSAGE);
            } catch(IOException e) {
                JOptionPane.showMessageDialog(frame, "Unable to edit account. Account does not exist or names were entered wrong.", "IO Error", JOptionPane.ERROR_MESSAGE);
            } catch(NullPointerException e) {
                JOptionPane.showMessageDialog(frame, "Account with number " + accNum + " does not exist. Enter a valid account number.", 
                        "Account does not Exist", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
/*----- Method: WriteToFile() ------------------------------------------------*/
    /**
     *  Pre-Condition:  JFrame exists and all fields in CREATE SAVINGS frame 
     *                  filled with valid data.
     * <br> 
     *  Post-Condition: Data entered saved to SAVINGS_ACCOUNT.dat.
     * <p>
     *  Method writes data entered by user for SavingsAccount to  
     *  SAVINGS_ACCOUNT.dat.
     * 
     * @param a             {@code SavingsAccount} object to be written to file.
     * @param lineInFile    {@code Integer} value representing where to start 
     *                      writing data into file
     * @throws IOException  
     */
    public void WriteToFile(SavingsAccount a, int lineInFile) throws IOException {
        // Declare variables for FileChannel object, ByteBuffer object
        FileChannel fc;
        ByteBuffer buffer;
        
        // Formatted String of data to write to file
        String s = a.toString() + System.getProperty("line.separator");
        
        // byte arrays for bytes read from file, bytes written to file
        byte[] dataFromFile = new byte[SAVINGS_ACCOUNT_DATA_LENGTH];
        byte[] dataToFile = s.getBytes();
        
        // String to hold record read from file
        String r;
        // String array to fields separated by delimiter (',') in String r
        String[] record;
        
        // Instantiate FileChannel object to read from, and write to, file SAVINGS_ACCOUNTS.txt
        fc = (FileChannel)Files.newByteChannel(savingsFp, READ, WRITE);
        // Instantitae ByteBuffer object to store bytes from file SAVINGS_ACCOUNTS.txt 
        // in byte array dataFromFile
        buffer = ByteBuffer.wrap(dataFromFile);
        
        // Set position in file to read from
        fc.position(lineInFile * SAVINGS_ACCOUNT_DATA_LENGTH);
        
        // Read line from file
        fc.read(buffer);
        
        // Instantite String r from bytes from dataFromFile
        r = new String(dataFromFile);
        
        // Populate byte array record using fields separated by delimiter (',') in String r
        record = r.split(DELIMITER);
        
        // Writes bytes in byte array dataToFile to file SAVINGS_ACCOUNTS.txt if record 
        // in file at specified position is empty
        if (record[2].equals(NAME_FORMAT)) {
            if (record[3].equals(NAME_FORMAT)) {
                buffer = ByteBuffer.wrap(dataToFile);

                fc.position(lineInFile * SAVINGS_ACCOUNT_DATA_LENGTH);

                fc.write(buffer);
                
                fc.close();
            } else {
                System.out.println("inner if");
                throw new IOException();
            }
        } else {
            System.out.println("outer if");
            throw new IOException();
        }
    }
    
/*----- Method: WriteToFile() ------------------------------------------------*/
    // CheckingAccount
    /**
     *  Pre-Condition:  JFrame exists and all fields in CREATE CHECKING frame 
     *                  filled with valid data.
     * <br> 
     *  Post-Condition: Data entered saved to CHECKING_ACCOUNTS.dat.
     * <p>
     *  Method writes data entered by user for CheckingAccount to  
     *  CHECKING_ACCOUNTS.dat.
     * 
     * @param a             {@code CheckingAccount} object to be written to file.
     * @param lineInFile    {@code Integer} value representing where to start 
     *                      writing data into file
     * @throws IOException 
     */
    public void WriteToFile(CheckingAccount a, int lineInFile) throws IOException {
        // Declare variables for FileChannel object, ByteBuffer object
        FileChannel fc;
        ByteBuffer buffer;
        
        // Formatted String of data to write to file
        String s = a.toString() + System.getProperty("line.separator");
        
        // byte arrays for bytes read from file, bytes written to file
        byte[] dataFromFile = new byte[CHECKING_ACCOUNT_DATA_LENGTH];
        byte[] dataToFile = s.getBytes();
        
        // String to hold record read from file
        String r;
        // String array to fields separated by delimiter (',') in String r
        String[] record = new String[MAX_CHECKING_FIELDS];
        
        // Instantiate FileChannel object to read from, and write to, file CHECKING_ACCOUNTS.txt
        fc = (FileChannel)Files.newByteChannel(checkingFp, READ, WRITE);
        // Instantitae ByteBuffer object to store bytes from file CHECKING_ACCOUNTS.txt 
        // in byte array dataFromFile
        buffer = ByteBuffer.wrap(dataFromFile);
        
        // Set position in file to read from
        fc.position(lineInFile * CHECKING_ACCOUNT_DATA_LENGTH);
        
        // Read line from file
        fc.read(buffer);
        
        // Instantite String r from bytes from dataFromFile
        r = new String(dataFromFile);
        
        // Populate byte array record using fields separated by delimiter (',') in String r
        record = r.split(DELIMITER);
        
        // Writes bytes in byte array dataToFile to file CHECKING_ACCOUNTS.txt if record 
        // in file at specified position is empty
        if (record[2].equals(NAME_FORMAT)) {
            if (record[3].equals(NAME_FORMAT)) {
                buffer = ByteBuffer.wrap(dataToFile);

                fc.position(lineInFile * CHECKING_ACCOUNT_DATA_LENGTH);

                fc.write(buffer);
                
                fc.close();
            } else {
                throw new IOException();
            }
        } else {
            throw new IOException();
        }
    }
    
/*----- Method: WriteEditToFile() --------------------------------------------*/
    /**
     * Pre-Condition:  JFrame exists and all fields in EDIT SAVINGS frame 
     *                  filled with valid data.
     * <br> 
     *  Post-Condition: Data entered saved to SAVINGS_ACCOUNTS.dat.
     * <p>
     *  Method writes data entered by user for SavingsAccount to  
     *  SAVINGS_ACCOUNTS.dat.
     * 
     * @param a             {@code SavingsAccount} object to be written to file.
     * @param lineInFile    {@code Integer} value representing where to start 
     *                      writing data into file
     * @throws IOException 
     */
    public void WriteEditToFile(SavingsAccount a, int lineInFile) throws IOException {
        // Declare variables for FileChannel object, ByteBuffer object
        FileChannel fc;
        ByteBuffer buffer;
        
        // Formatted String of data to write to file
        String s = a.toString() + System.getProperty("line.separator");
        
        // byte array for bytes to write to file
        byte[] dataToFile = s.getBytes();
        
        // Instantiate FileChannel object to read from, and write to, file SAVINGS_ACCOUNTS.txt
        fc = (FileChannel)Files.newByteChannel(savingsFp, READ, WRITE);
        
        // Instantitae ByteBuffer object to Writes bytes in byte array dataToFile 
        // to file SAVINGS_ACCOUNTS.txt at specified position in file
        buffer = ByteBuffer.wrap(dataToFile);

        // Set position in file to write to
        fc.position(lineInFile * SAVINGS_ACCOUNT_DATA_LENGTH);

        // Write to file
        fc.write(buffer);

        // Close FileChannel
        fc.close();
    }
    
/*----- Method: WriteEditToFile() --------------------------------------------*/
    /**
     * Pre-Condition:  JFrame exists and all fields in EDIT CHECKING frame 
     *                  filled with valid data.
     * <br> 
     *  Post-Condition: Data entered saved to CHECKING_ACCOUNTS.dat.
     * <p>
     *  Method writes data entered by user for CheckingAccount to  
     *  CHECKING_ACCOUNTS.dat.
     * 
     * @param a             {@code CheckingAccount} object to be written to file.
     * @param lineInFile    {@code Integer} value representing where to start 
     *                      writing data into file
     * @throws IOException 
     */
    public void WriteEditToFile(CheckingAccount a, int lineInFile) throws IOException {
        // Declare variables for FileChannel object, ByteBuffer object
        FileChannel fc;
        ByteBuffer buffer;
        
        // Formatted String of data to write to file
        String s = a.toString() + System.getProperty("line.separator");
        
        // byte array for bytes to write to file
        byte[] dataToFile = s.getBytes();
        
        // Instantiate FileChannel object to read from, and write to, file CHECKING_ACCOUNTS.txt
        fc = (FileChannel)Files.newByteChannel(checkingFp, READ, WRITE);
        
        // Instantitae ByteBuffer object to Writes bytes in byte array dataToFile 
        // to file CHECKING_ACCOUNTS.txt at specified position in file
        buffer = ByteBuffer.wrap(dataToFile);

        // Set position in file to write to
        fc.position(lineInFile * CHECKING_ACCOUNT_DATA_LENGTH);

        // Write to file
        fc.write(buffer);

        // Close FileChannel
        fc.close();
    }
    
/*----- Method: ReadFromFile() -----------------------------------------------*/
    // reset savingsAccountData[MAX_ACCOUNTS_AMOUNT][MAX_SAVINGS_FIELDS] and checkingAccountsData[][] each call
    // returns location in file to write to
    /**
     *  Pre-Condition:  JDialog tableWindow exists.
     * <br>
     *  Post-Condition: String[][] for SavingsAccount data or CheckingAccount 
     *                  data populated from appropriate files based on which 
     *                  table of data needs created.
     * <p>
     *  Method reads valid data from SAVINGS_ACCOUNTS.dat or 
     *  CHECKING_ACCOUNTS.dat and stores the data in the appropriate String[][].
     * 
     * @param file  {@code String} value that represents which file to read from 
     *              and which multi-dimensional array needs populated: savings 
     *              or checking.
     */
    public void ReadFromFile(String file) {
        // Declare variables for InputStream object, BufferedReader object
        InputStream fileIn;
        BufferedReader reader;
        String s;           // Stores data read from line in file
        String [] record;   // Stores fields separated by delimiter (",") read from line in file
        
        try {
            switch(file) {
                case "Savings": {
                    // Create a multi-dimensional array to hold the max possible amount of accounts from SAVINGS_ACCOUNTS.dat
                    String[][] tempTable = new String[MAX_ACCOUNTS_AMOUNT][MAX_SAVINGS_FIELDS];
                    savingsAccountsInArray = 0; // Counter for amount of valid accounts read from SAVINGS_ACCOUNTS.dat
                    
                    // Instantiate InputStream object for sequential reading
                    fileIn = Files.newInputStream(savingsFp);

                    // Instantiate BufferedReader object
                    reader = new BufferedReader(new InputStreamReader(fileIn));
                    
                    // Read through every line of SAVINGS_ACCOUNTS.dat
                    for (int i=0;i<MAX_ACCOUNTS_AMOUNT;i++) {
                        s = reader.readLine();
                        
                        // Store data fields contained in Strinf s, separated by delimiter (",")
                        record = s.split(DELIMITER);
                        
                        // If the name fields contain characters other than only whitespace, add Account data to tempTable array
                        if (!(record[2].equals(NAME_FORMAT))) {
                            if (!(record[3].equals(NAME_FORMAT))) {
                                for (int j=0;j<MAX_SAVINGS_FIELDS;j++) {
                                    // Parse account number to to double to get rid of leading 0s for display in JTable
                                    if (j == 3) {
                                        double parsedBal = Double.parseDouble(record[j+1]);
                                        String formatBal = String.format("$%.2f", parsedBal);
                                        
                                        tempTable[savingsAccountsInArray][j] = formatBal;
                                    } else {
                                        tempTable[savingsAccountsInArray][j] = record[j+1];
                                    }
                                }
                                savingsAccountsInArray++;
                            }
                        }
                    }
                    
                    // Instantiate savingsAccountsData with enough rows to store all valid accounts to allow for proper sorting of JTable
                    savingsAccountsData = new String[savingsAccountsInArray][MAX_SAVINGS_FIELDS];
                    
                    // Copy data from tempTable arry to savingsAccountData array
                    System.arraycopy(tempTable, 0, savingsAccountsData, 0, savingsAccountsInArray);
                } break;

                case "Checking": {
                    // Create a multi-dimensional array to hold the max possible amount of accounts from CHECKING_ACCOUNTS.dat
                    String[][] tempTable = new String[MAX_ACCOUNTS_AMOUNT][MAX_CHECKING_FIELDS];
                    checkingAccountsInArray = 0;    // Counter for amount of valid accounts read from CHECKING_ACCOUNTS.dat
                    
                    // Instantiate InputStream object for sequential reading
                    fileIn = Files.newInputStream(checkingFp);

                    // Instantiate BufferedReader object
                    reader = new BufferedReader(new InputStreamReader(fileIn));
                    
                    // Read through every line of CHECKING_ACCOUNTS.dat
                    for (int i=0;i<MAX_ACCOUNTS_AMOUNT;i++) {
                        s = reader.readLine();
                        
                        // Store data fields contained in Strinf s, separated by delimiter (",")
                        record = s.split(DELIMITER);
                        
                        // If the name fields contain characters other than only whitespace, add Account data to tempTable array
                        if (!(record[2].equals(NAME_FORMAT))) {
                            if (!(record[3].equals(NAME_FORMAT))) {
                                for (int j=0;j<MAX_CHECKING_FIELDS;j++) {
                                    // Parse account number to to double to get rid of leading 0s for display in JTable
                                    if (j == 3) {
                                        double parsedBal = Double.parseDouble(record[j+1]);
                                        String formatBal = String.format("$%.2f", parsedBal);
                                        
                                        tempTable[checkingAccountsInArray][j] = formatBal;
                                    } else {
                                        tempTable[checkingAccountsInArray][j] = record[j+1];
                                    }
                                }
                                checkingAccountsInArray++;
                            }
                        }
                    }
                    
                    checkingAccountsData = new String[checkingAccountsInArray][MAX_CHECKING_FIELDS];
                    
                    System.arraycopy(tempTable, 0, checkingAccountsData, 0, checkingAccountsInArray);
                } break;
            }
        } catch(IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to read accounts from file.", "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }

/*----- Method: actionPerformed() --------------------------------------------*/
    /**
     *  Pre-Condition:  JFrame exists.
     * <br>
     *  Post-Condition: Action performed based on button pressed or JMenuItem 
     *                  selected.
     * <p>
     *  Method handles ActionEvents originating from JButtons or JMenuItems 
     *  added to JFrame.
     * 
     * @param e {@code ActionEvent} originating from JButtons or JMenuItems 
     *          added to JFrame.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        int emptyFields = 0;
     
    /*----- JButtons ---------------------------------------------------------*/
        // Calls createAccount() if all fields contain valid data
        if (source == addButton) {
            String s = accountNumberField.getText();
            if (accountNumberField.getText().isEmpty()) {
                emptyFields++;
            } else {
                if (!(s.matches("\\d{" + ACC_NUM_FORMAT.length() + "}"))) {
                    JOptionPane.showMessageDialog(frame, 
                            "Account number is not in the correct format: " + ACC_NUM_FORMAT + ". Enter a valid account number.", 
                            "Account Number Format", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            if (firstNameField.getText().isEmpty()) {
                emptyFields++;
            }
            
            if (lastNameField.getText().isEmpty()) {
                emptyFields++;
            }
            
            if (accountBalanceField.getText().isEmpty()) {
                emptyFields++;
            }
            
            if (frame.getName().equals(frameNames[1]) && emptyFields == 0) {
                CreateAccount(frameNames[1]);
            } else if (frame.getName().equals(frameNames[4]) && emptyFields == 0) {
                CreateAccount(frameNames[4]);
            } else {
                JOptionPane.showMessageDialog(frame, 
                        "Not all fields are populated with valid data. Please fill all fields to create an account", 
                        "Too Many Empty Fields", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Calls EditAccount() if account number entered is in a valid format
        if (source == editButton) {
            String s = accountNumberField.getText();
            
            if (frame.getName().equals(frameNames[2])) {
                if (s.matches("\\d{" + ACC_NUM_FORMAT.length() + "}")) {
                    EditAccount(s);
                } else {
                    JOptionPane.showMessageDialog(frame, 
                            "Account number " + s + " is not a valid account number. "
                                    + "Enter a valid account number following format: " 
                                    + ACC_NUM_FORMAT + ".", 
                            "Invalid Format", JOptionPane.ERROR_MESSAGE);
                }
            } else if (frame.getName().equals(frameNames[5])) {
                if (accountNumberField.getText().matches("\\d{" + ACC_NUM_FORMAT.length() + "}")) {
                    EditAccount(s);
                } else {
                    JOptionPane.showMessageDialog(frame, 
                            "Account number " + accountNumberField.getText() 
                                    + " is not a valid account number. Enter a valid account number following format: " 
                                    + ACC_NUM_FORMAT + ".", 
                            "Invalid Format", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        // Calls DeleteAccount() if account number entered is in valid format and user 
        // confirms their choice to delete the account associated with the entered account number
        if (source == deleteButton) {
            String s = accountNumberField.getText();
            int confirmInput;
            
            if (frame.getName().equals(frameNames[3])) {
                if (s.matches("\\d{" + ACC_NUM_FORMAT.length() + "}")) {
                    confirmInput = JOptionPane.showConfirmDialog(frame, ("Are you sure you want to delete account with number: " + s), 
                            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (confirmInput == 0) {
                        DeleteAccount(s);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, 
                            "Account number " + s + " is not a valid account number. "
                                    + "Enter a valid account number following format: " 
                                    + ACC_NUM_FORMAT + ".", 
                            "Invalid Format", JOptionPane.ERROR_MESSAGE);
                }
            } else if (frame.getName().equals(frameNames[6])) {
                if (s.matches("\\d{" + ACC_NUM_FORMAT.length() + "}")) {
                    confirmInput = JOptionPane.showConfirmDialog(frame, ("Are you sure you want to delete account with number: " + s), 
                            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (confirmInput == 0) {
                        DeleteAccount(s);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, 
                            "Account number " + s + " is not a valid account number. "
                                    + "Enter a valid account number following format: " 
                                    + ACC_NUM_FORMAT + ".", 
                            "Invalid Format", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        // Clears all JTextFields text and resets JComboBoxes back to their first option
        if (source == clearButton) {
                accountNumberField.setText(null);
                firstNameField.setText(null);
                lastNameField.setText(null);
                accountBalanceField.setText(null);
                savingsAccountTypeCombo.setSelectedIndex(0);
                termLengthCombo.setSelectedIndex(0);
        }
        
    /*----- JMenuItems -------------------------------------------------------*/
        // All dispose of JFrame and create a new JFrame based on the MenuItem selected, 
        // except last two if statements, which call DisplayAccounts()
        if (source == newSavingsRecord) {
            frame.dispose();
            frame = new FinalProject(FrameType.SAVINGS, AccountAction.CREATE);
            frame.setVisible(true);
        }
        
        if (source == editSavingsRecord) {
            frame.dispose();
            frame = new FinalProject(FrameType.SAVINGS, AccountAction.EDIT);
            frame.setVisible(true);
        }
        
        if (source == deleteSavingsRecord) {
            frame.dispose();
            frame = new FinalProject(FrameType.SAVINGS, AccountAction.DELETE);
            frame.setVisible(true);
        }
        
        if (source == newCheckingRecord) {
            frame.dispose();
            frame = new FinalProject(FrameType.CHECKING, AccountAction.CREATE);
            frame.setVisible(true);
        }
        
        if (source == editCheckingRecord) {
            frame.dispose();
            frame = new FinalProject(FrameType.CHECKING, AccountAction.EDIT);
            frame.setVisible(true);
        }
        
        if (source == deleteCheckingRecord) {
            frame.dispose();
            frame = new FinalProject(FrameType.CHECKING, AccountAction.DELETE);
            frame.setVisible(true);
        }
        
        if (source == displaySavingsAccounts) {
            DisplayAccounts("Savings");
        }
        
        if (source == displayCheckingAccounts) {
            DisplayAccounts("Checking");
        }
    }
    
/*----- Enumerations ---------------------------------------------------------*/
    /**
     *  Enumeration for type of JFrame to create.
     */
    enum FrameType {
        DEFAULT,
        SAVINGS,
        CHECKING
    }
    
    /**
     *  Enumeration for which action is being performed on the chosen account type.
     */
    enum AccountAction {
        CREATE,
        EDIT,
        DELETE
    }
}