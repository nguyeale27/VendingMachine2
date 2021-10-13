package com.vendingmachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * VendingMachine represents the main program that simulates the functions of a vending machine.
 * 
 */
class VendingMachine{

     HashMap<Character,Integer> hm = new HashMap<Character, Integer>(); //used to convert row letters to numbers
    Scanner s = new Scanner(System.in); //used for input
    VendingSnack[][] inventory; //keeps track of items in VendingMachine
    File record; //Used to record transactions
    File i;//Used to represent the input
    FileWriter fw;
    FileReader fr;
    StringBuffer sb;
    JSONObject jo;
    JSONParser jp;
    int totalRows, totalColumns;
    int currentRow, currentColumn;
    /**
     * Constructor for VendingMachine
     * @throws IOException
     * @throws ParseException
     */
    public VendingMachine() throws IOException, ParseException{
        record = new File("Transactions.txt");
        readInput();
    }
public static void main (String[] args) throws IOException, ParseException{
    VendingMachine vm = new VendingMachine();
    //vm.mainMenu();
}

private void mainMenu() throws IOException{
    try {
        fw = new FileWriter(record);
    } catch (IOException e) {
        
        e.printStackTrace();
    }
    boolean done = false;
    while (done == false){ //Allows the user to enter combinations until they are done
        System.out.println("Enter 'a' to add a snack. Enter 'p' to purchase a snack. Enter 0 to exit.");
        String choice = s.nextLine();
            if(choice.equals("p")){
            selectSnack();
            }
            else if(choice.equals("a")){
                if(currentColumn > totalColumns || currentRow > totalRows){
                    System.out.println("Vending Machine is full.");
                }
                else{
                    addSnack();
                    }
                                        }
            else if(choice.equals("0")){
            done = true;
            }
            else{
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
private void readInput() throws IOException, ParseException{
    fr = new FileReader("src/Input/input.json");
    JSONObject config;
    int rows;
    int columns;
        jp = new JSONParser();
        jo = (JSONObject) jp.parse(fr);
        if(jo.containsKey("config") == true){
            config = (JSONObject) jo.get("config");
            rows = (int) (config.get("rows"));
            columns = (int) (config.get("columns"));
        }
        
}


 /**
     * setMachine creates VendingSnack objects from the given input
     */
private void setMachine(){
    boolean b = false;

    if(sb.toString().contains("items") == true){
        sb.delete(0, sb.indexOf("items"));
        currentRow = 0; 
        currentColumn = 0;

    while(b == false){
        if(sb.toString().contains("name") == true && sb.toString().contains("amount") == true 
        && sb.toString().contains("price") == true){
            String[] properties = new String[3];
            properties[0] = sb.substring(sb.indexOf("name"), sb.indexOf("amount")); //represents the name
            properties[1] = sb.substring(sb.indexOf("amount"), sb.indexOf("price")); //represents the amount
            properties[2] = sb.substring(sb.indexOf("price"), sb.indexOf("}")); //represents the price
            setSnack(properties,currentRow,currentColumn);

            sb.delete(sb.indexOf("name"), sb.indexOf("}")+1); //allows the program to move on to the next snack
            if(currentColumn >= totalColumns-1) //Program goes through columns first before moving on to the next row
            {
                currentColumn = 0;
                currentRow++;
            }
            else{
                currentColumn++;
            }
        }
        else{
            b = true;
        }
    }
}
}
/**
     * setRowsAndColumns Takes the specified number of rows and columns and creates a 2D array
     * carrying the VendingSnack objects with them.
     */
private void setRowsAndColumns(){
    String s = sb.toString();

    if (s.contains("rows") == true && s.contains("columns") == true){
        String r = s.substring(s.indexOf("rows"), s.indexOf("columns")); 
        r = r.replaceAll("rows", ""); r = r.replace(':', ' '); //Used to trim the string to get only the int value
        r = r.replace(',', ' '); r = r.trim();

        String c = s.substring(s.indexOf("columns"), s.indexOf("}"));
        c = c.replaceAll("columns", "");
        c = c.replace(':', ' '); c = c.replace(',', ' '); c = c.trim();

        totalRows = Integer.parseInt(r);
        totalColumns = Integer.parseInt(c);

        inventory = new VendingSnack[totalRows][totalColumns];
        setRowLetters(Integer.parseInt(r));
    }
}
/**
     * setMachine creates VendingSnack objects from the given input
     */
private void setRowLetters(int numRows){
    char c = 'A'; 
    for(int i = 0; i<numRows;i++){
        hm.put(c, i);
        c += 1;
    }
}
/**
     * calculatePayment calculates the transaction for the selected snack.
     * 
     * @param properties carries the name, amount, and price information needed to create the VendingSnack object.
     * @param r the selected row of the vending machine
     * @param c the selected column of the vending machine
     */
private void setSnack(String[] properties, int r, int c){
    String name = "";
    int amount = 0;
    double price = 0;
    for (String s : properties) {
        
        if(s.contains("name") == true){
            s = s.replaceAll("name", ""); //Trimming the string to get only the needed value for name
            s = s.replace(':', ' '); s = s.replace(',', ' ');
            s = s.replace('"', ' '); s = s.trim();

            name = s;
        }
        else if(s.contains("amount") == true){
            s = s.replaceAll("amount", "");
            s = s.replace(':', ' '); s = s.replace(',', ' ');
            s = s.replace('"', ' '); s = s.trim();

            amount = Integer.parseInt(s);
        }
        else if(s.contains("price") == true){
            s = s.replaceAll("price", "");
            s = s.replace(':', ' '); s = s.replace(',', ' ');
            s = s.replace('"', ' ');s = s.replace('$', ' '); s = s.trim();

            price = Double.parseDouble(s);
        }
    }
    inventory[r][c] = new VendingSnack(name, amount, price);
}
/**
 * SelectSnack Gives the user a prompt to add a snack
 * 
 */
private void addSnack(){
    char c = 'A';
    c += currentRow;
    String[] snack = new String[3];
    System.out.println("Enter the name of the snack.");
    String n = "name " + s.nextLine();
    snack[0] = n;
    System.out.println("Enter the amount of the snack.");
    String a = "amount " + s.nextLine();
    snack[1] = a;
    System.out.println("Enter the price of the snack.");
    String p = "price " + s.nextLine();
    snack[2] = p;
    setSnack(snack, currentRow, currentColumn);
    System.out.printf("Snack added at row %c, column %d.\n", c, currentColumn + 1);
    if(currentColumn >= totalColumns-1) //Program goes through columns first before moving on to the next row
    {
        currentColumn = 0;
        currentRow++;
    }
    else{
        currentColumn++;
    }
}
/**
 * SelectSnack Gives the user a prompt to purchase a snack
 * 
 */
private void selectSnack(){
    boolean done = false;
    while(done == false){
    System.out.println("Enter combination to select a snack. Example: A1 for the snack in the first row and first column. Enter 0 to cancel and go back to the menu.");
    String select = s.nextLine();
    if(select.length() == 2){
        try{
            
        int r = hm.get(select.charAt(0));
         int c = Character.getNumericValue(select.charAt(1)) - 1;
         VendingSnack snack = inventory[r][c];
         calculatePayment(snack);
         done = true;
    }
    catch(NullPointerException e){
        System.out.printf("No snack found. Please try again.");
    }
    
}
    else if(select.equals("0")){ //Exits the method
        done = true;
    }
    else{
        System.out.println("Invalid combination.");
    }
}
}


 /**
     * calculatePayment calculates the transaction for the selected snack.
     * 
     * @param row the selected row of the vending machine
     * @param column the selected column of the vending machine
     */
public void calculatePayment(VendingSnack vs){
        
        if(vs.getAmount() > 0){//Checks to make sure that there is still a snack left to purchase
            double price = vs.getPrice();
   
            System.out.printf("Selected %s. \n", vs.getName());
            System.out.printf("Total price is: $%.2f.\n", price);

            boolean hasPaid = false;
            while(hasPaid == false){

                System.out.println("Please enter payment amount.");
                double payment = Double.parseDouble(s.nextLine());
                if(payment >= price){ //If the entered payment covers the price
                    double change = payment - price;
                    System.out.printf("Thank you. Your change is: %.2f.\n", change);
                    hasPaid = true;
                    recordTransaction(vs, price, payment, change);
                    vs.subtractAmount();
                                    }
        else{
                System.out.println("Payment is less than price.");
            }
    }
}
else{ //if the snack has run out
    System.out.println("This snack has run out.");
}
}
 /**
     * recordTransaction records the current transaction into the txt file
     * 
     * @param row the selected row of the vending machine
     * @param column the selected column of the vending machine
     */
private void recordTransaction(VendingSnack vs, double price, double payment, double change){
    try {
        String name = vs.getName();
        String s = String.format("Transaction: %s purchased. Payment: $%.2f. Total Change: $%.2f. \n", name, payment, change);
        fw.append(s);
    } catch (IOException e) {
        System.out.println("Error with FileWriter.");
    }
}
}