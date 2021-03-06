package com.vendingmachine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * VendingMachine represents the main program that simulates the functions of a vending machine.
 * 
 */
class VendingMachine
{

    HashMap<Character,Integer> hm = new HashMap<Character, Integer>(); //used to convert row letters to numbers
    Scanner s = new Scanner(System.in); //used for input
    VendingSnack[][] inventory; //keeps track of items in VendingMachine
    File record; //Used to record transactions
    File i;//Used to represent the input
    FileWriter fw;
    FileReader fr;
    int totalRows, totalColumns;
    int currentRow, currentColumn;
    /**
     * Constructor for VendingMachine
     * @throws IOException
     * @throws ParseException
     */
    public VendingMachine() throws IOException, ParseException
    {
        record = new File("src/Records/Transactions.txt");
        readInput();
        mainMenu();
    }
/**
    *mainMenu Acts as the main menu where the user can add or purchase a snack in VendingMachine 
    * @throws IOException
*/
private void mainMenu() throws IOException
{

    fw = new FileWriter(record);
    boolean done = false;
    while (done == false)//Allows the user to enter combinations until they are done
    { 
        System.out.println("Enter 'a' to add a snack. Enter 'p' to purchase a snack. Enter 0 to exit.");
        String choice = s.nextLine();
            if(choice.equals("p"))
            {
            selectSnack();
            }

            else if(choice.equals("a"))
            {
                if(currentColumn > totalColumns || currentRow > totalRows)
                {
                    System.out.println("Vending Machine is full.");
                }

                else
                {
                    addSnack();
                }

            }

            else if(choice.equals("0"))
            {
            done = true;
            }

            else
            {
                System.out.println("Invalid input. Please try again.");
            }

    }
        
    fw.close();
}
 /**
     * readInput Takes input file and sends it into a StringBuffer for creating VendingSnacks.
     * @throws ParseException
     * @throws IOException
 */
private void readInput() throws IOException, ParseException
{
    fr = new FileReader("src/Input/input.json");
    JSONObject config;
    JSONArray items;
    JSONObject inputList;
    JSONParser jp;
    
    jp = new JSONParser();
    inputList = (JSONObject) jp.parse(fr);
    if(inputList.containsKey("config") == true)
    {
            config = (JSONObject) inputList.get("config");
            totalRows = Integer.parseInt(config.get("rows").toString());
            totalColumns = Integer.parseInt(config.get("columns").toString());
            setRowsAndColumns(totalRows, totalColumns);
    }

    if(inputList.containsKey("items") == true)
    {
            items = (JSONArray) inputList.get("items");
            setMachine(items);
    }
}


 /**
     * setMachine creates VendingSnack objects from the given input
 */
private void setMachine(JSONArray ja)
{
    String name;
    double price;
    int amount;
    for(int i = 0;i<ja.size();i++)
    {
        JSONObject jso = (JSONObject) ja.get(i);
        name = jso.get("name").toString();
        price = Double.parseDouble(jso.get("price").toString().replace("$", ""));
        amount = Integer.parseInt(jso.get("amount").toString());
        setSnack(name, price, amount, currentRow, currentColumn);
    }
    
}
/**
     * setRowsAndColumns Takes the specified number of rows and columns and creates a 2D array
     * carrying the VendingSnack objects as elements.
     * 
     * @param r the total number of rows for the VendingMachine
     * @param c the total number of columns for the VendingMachine
*/
private void setRowsAndColumns(int r, int c)
{
        inventory = new VendingSnack[totalRows][totalColumns];
        setRowLetters(r);
}
/**
     * setMachine creates VendingSnack objects from the given input
*/
private void setRowLetters(int numRows)
{
    char c = 'A'; 
    for(int i = 0; i<numRows;i++)
    {
        hm.put(c, i);
        c += 1;
    }
}
/**
     * setSnack Creates a new VendingSnack to add to the VendingMachine instance
     * 
     * @param n the name of the snack
     * @param p the price of the snack
     * @param a the amount of the snack
     * @param r the selected row of the vending machine
     * @param c the selected column of the vending machine
*/
private void setSnack(String n, double p, int a, int r, int c)
{
    inventory[r][c] = new VendingSnack(n, a, p);
    if(currentColumn >= totalColumns-1) //Program goes through columns first before moving on to the next row
    {
        currentColumn = 0;
        currentRow++;
    }

    else
    {
        currentColumn++;
    }
}
/**
    * addSnack Gives the user a prompt to add a snack
    * 
*/
private void addSnack()
{
    char c = 'A';
    c += currentRow;
    String newName;
    double newPrice;
    int newAmount;

    System.out.println("Enter the name of the snack.");
    newName = s.nextLine();
    System.out.println("Enter the amount of the snack.");
    newAmount = Integer.parseInt(s.nextLine());
    System.out.println("Enter the price of the snack.");
    newPrice = Double.parseDouble(s.nextLine());
    setSnack(newName, newPrice, newAmount, currentRow, currentColumn);
    System.out.printf("Snack added at row %c, column %d.\n", c, currentColumn);
    
}
/**
    * SelectSnack Gives the user a prompt to purchase a snack
    * @throws IOException
    * 
*/
private void selectSnack() throws IOException
{
    boolean done = false;
    while(done == false)
    {
    System.out.println("Enter combination to select a snack. Example: A1 for the snack in the first row and first column. Enter 0 to cancel and go back to the menu.");
    String select = s.nextLine();
        if(select.length() == 2)
        {
            try{
            int r = hm.get(select.charAt(0));
            int c = Character.getNumericValue(select.charAt(1)) - 1;
            VendingSnack snack = inventory[r][c];
            calculatePayment(snack);
            done = true;
            }
            catch (Exception ex)
            {
                System.out.println("Invalid combination.");
            }
        }

        else if(select.equals("0"))//Exits the method
        { 
        done = true;
        }

        else
        {
        System.out.println("Invalid combination.");
        }

    }
}


/**
     * calculatePayment calculates the transaction for the selected snack.
     * 
     * @param row the selected row of the vending machine
     * @param column the selected column of the vending machine
     * @throws IOException
*/
public void calculatePayment(VendingSnack vs) throws IOException
{
        
        if(vs.getAmount() > 0)//Checks to make sure that there is still a snack left to purchase
        {
            double price = vs.getPrice();
   
            System.out.printf("Selected %s. \n", vs.getName());
            System.out.printf("Total price is: $%.2f.\n", price);

            boolean hasPaid = false;
            while(hasPaid == false)
            {

                System.out.println("Please enter payment amount.");
                double payment = Double.parseDouble(s.nextLine());
                if(payment >= price)//If the entered payment covers the price
                { 
                    double change = payment - price;
                    System.out.printf("Thank you. Your change is: $%.2f.\n", change);
                    hasPaid = true;
                    recordTransaction(vs, price, payment, change);
                    vs.subtractAmount();
                }

                else
                {
                System.out.println("Payment is less than price.");
                }

            }
        }

        else//if the snack's amount value equals 0
        { 
    System.out.println("This snack has run out.");
        }
}
 /**
     * recordTransaction records the current transaction into the txt file
     * 
     * @param row the selected row of the vending machine
     * @param column the selected column of the vending machine
 */
private void recordTransaction(VendingSnack vs, double price, double payment, double change) throws IOException
{
        String name = vs.getName();
        String s = String.format("Transaction: %s purchased. Payment: $%.2f. Total Change: $%.2f. \n", name, payment, change);
        fw.append(s);
}
}