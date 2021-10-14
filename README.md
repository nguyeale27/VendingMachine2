# VendingMachine2

Overview
VendingMachine2 is an updated program developed in Visual Studio Code that acts as a vending machine with snacks. 
It takes a JSON file as an input for the total number of rows and columns in the vending machine as well as the items that the machine will start with.
The user can also create snacks based on if the size of the vending machine allows it. 
Reuses code from previous version of VendingMachine. Uses json.simple dependency from Maven Repository.
Repository URL: https://github.com/nguyeale27/VendingMachine2
Previous Version Repository URL: https://github.com/nguyeale27/VendingMachine
Maven Repository URL: https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1

Approach
The program first takes the JSON file and reads it using JSONParser to find the number of rows and columns as well as the starting items and builds a 2D array, where
each element holds an object instance of VendingSnack to represent a snack. 
After this process, the user is given a prompt to choose to add another snack, buy one from the existing list, or exit the program.
The program will record transactions in a txt file called "Transactions" in the project folder under the "src/Records" file path. 
The transactions will not appear until the user exits the program.

User Interaction
When using the program, it is recommended to use Visual Studio Code to run the program and the JSON file should be named "input.json" and added to the "src/Input" file path.
After starting the program, the user can either add another snack, purchase an existing one, or exit the program in the prompt given in the terminal.
A user selects a particular item by giving a 2 character input, 
where the first character is a letter indicating the row where the item is located and the second character is a number indicating the column.
If the snack has an amount value greater than 0, the user is asked to enter the payment, where the difference is calculated and the transaction is recorded.
If it is less than the price, the program will not accept it and will ask to reenter another payment. After the transaction is recorded, the program then goes back to the original menu.
If the user adds another snack, they are given prompts to enter the name, amount, and price of the snack. Once given, it is added to the next available slot, which is
displayed for the user.
