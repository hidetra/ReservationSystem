import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Flight {
	public static final int MAXFIRST = 8;
	public static final int MAXECON = 120;
	SeatingSystem sChart;
	
	//This is used to read input redirection!
	BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	
	public String flightService = "First 1-2, Left: A-B, Right: C-D; Economy 10-29, Left: A-C, Right: D-F";
	File flightFile;
	String flightName;
		
	public Flight(String fileName) {
		sChart = new SeatingSystem();
		flightFile = new File(fileName);
		this.flightName = fileName;
		
		//pArrayFirst Initialization
		for(int rowF = 0; rowF < SeatingSystem.ROW_FIRST; rowF++){
			for(int colF = 0; colF < SeatingSystem.COL_FIRST; colF++){
				sChart.pArrayFirst[rowF][colF] = new Passenger();
				sChart.pArrayFirst[rowF][colF].setName(null);
				sChart.pArrayFirst[rowF][colF].setGroupName(null);
				sChart.pArrayFirst[rowF][colF].setSeat(null);
			}
		}		
		//pArrayEcon Initialization
		for(int rowE = 0; rowE < SeatingSystem.ROW_ECON; rowE++){
			for(int colE = 0; colE < SeatingSystem.COL_ECON; colE++){
				sChart.pArrayEcon[rowE][colE] = new Passenger();
				sChart.pArrayEcon[rowE][colE].setName(null);
				sChart.pArrayEcon[rowE][colE].setGroupName(null);
				sChart.pArrayEcon[rowE][colE].setSeat(null);
			}
		}		
		//Read in flight information if one is already existed.
		if(flightFile.exists()){
			readFlightData(fileName);
		}else{
			return;				
		}		
	}// Flight Constructor
	
	/*========================= readFlightData ==========================
	** If an existing flight is found, read the reservation data
	**/
	public void readFlightData(String fileName){
		try{
			FileReader input = new FileReader(fileName);
			//FileReader input = new FileReader("CL34.txt");
			BufferedReader buffer = new BufferedReader(input);
			
			boolean eof = false;
			if(!eof){
					flightService = buffer.readLine();
			}
			
			while(!eof){
				String lineStr = buffer.readLine();
				if(lineStr == null){
					eof = true;
				}
				else{
					String[] tokenStr = lineStr.split(", ");
					
					String tempSeat = tokenStr[0];
					String tempRow = tempSeat.substring(0, tempSeat.length() - 1);
					int rowNum = Integer.parseInt(tempRow);
					
					String tempCol = tempSeat.replace(tempSeat.substring(0, tempSeat.length() - 1),"");
					tempCol = tempCol.toUpperCase();
					char colLetter = tempCol.charAt(0);
					int colNum = (int)colLetter - 64;
					char passType = tokenStr[1].charAt(0);
					
					if(passType == 'I'){		
						sChart.setExistingPassenger(rowNum, colNum, tokenStr[2]);	
					}else{
						sChart.setExistingGroup(rowNum, colNum, tokenStr[3], tokenStr[2]);	
					}
				}				
			}
			buffer.close();
		}catch(IOException e){
			System.out.println("Error reading the existing flight data " + e.getMessage());
		}
		
	}//readFlightData

	/*========================= selectOptions ========================
	** Present all the available options regarding the flight 
	** for users to choose
	**/
	public void selectOptions(){
		String userInput = null;
		char choice;
		int validChoice;	
		do{
			System.out.println("\nAdd [P]assenger, Add [G]roup, "
					+ "[C]ancel Reservations, Print Seating [A]vailability Chart, "
					+ "Print [M]anifest, [Q]uit");
			System.out.printf("Choose an option: ");
			
			userInput = getUserInput(inputReader);
			choice = userInput.charAt(0);
			choice = Character.toUpperCase(choice);		
			validChoice = 1;
			
			switch (choice) {
			case 'P': 	addPassenger();
				break;				
			case 'G':	addGroup();
				break;				
			case 'C':	cancelReservation();	
				break;				
			case 'A':	printAvailability();	
				break;			
			case 'M': 	printManifest();
				break;
				
			case 'Q': 	saveFlight();	
				break;	
			default: 	System.out.println("Invalid Choice Selection");
						validChoice = 0;
				break;
			}//switch
				
		}while(choice != 'Q' || validChoice == 0);
	
	}//selectOptions

	/*========================= getUserInput ==========================
	** Read user's input (Input Redirection) and return that input
	** @param inputReader: BufferedReader variable type to read user's
	** input line by line
	** @return lineStr - user's input
	**/
	public String getUserInput(BufferedReader inputReader){
		String lineStr;
		
		while(true){			
			try{
				lineStr = inputReader.readLine();
				break;
			}catch(Exception e){
				System.out.println("Error reading line " + e.getMessage());
			}	
		}//loop		
		System.out.println();
		return lineStr.trim();
	}//getUserInput

	/*========================= addPassenger ==========================
	** This will prompt the user to enter necessary information prior
	** to reserve a seat.
	**/
	public void addPassenger(){
		//Local Variables
		String name;
		String serviceClass;
		String seatPreference;
		String seatFound = null;	
		
		System.out.printf("Name: ");
		name = getUserInput(inputReader);
		System.out.printf("Service Class (First or Economy): ");
		serviceClass = getUserInput(inputReader);

		if(serviceClass.toUpperCase().equals("FIRST")){
			System.out.printf("Seat Preference ([W]indow, [A]isle): ");
			seatPreference = getUserInput(inputReader);

			do{
				if(sChart.getNumPassFirst() == MAXFIRST){
					System.out.println("No more available seats in First Class.");
					return;
				}
				seatFound = sChart.searchAvailabilityFirst(seatPreference, name);
				
				if(seatFound == null){
					System.out.println("No available seats. Please choose a different seat preference: ");
					seatPreference = getUserInput(inputReader);
				}else{
					System.out.println("Your reserved seat is: " + seatFound);
				}
			}while(seatFound == null);
			
		}else if(serviceClass.toUpperCase().equals("ECONOMY")){
			System.out.printf("Seat Preference ([W]indow, [C]enter, [A]isle): ");
			seatPreference = getUserInput(inputReader);

			if(sChart.getNumPassEcon() == MAXECON){
				System.out.println("No more available seats in Economy Class.");
				return;
			}
			do{
				seatFound = sChart.searchAvailabilityEcon(seatPreference, name);
				if(seatFound == null){
					System.out.println("No available seats .");
					System.out.printf("Please choose a different seat preference: ");
					seatPreference = getUserInput(inputReader);
				}else{
					System.out.println("Your reserved seat is: " + seatFound);
				}
			}while(seatFound == null);		
		}else{
			System.out.println("Invalid choice => Request failed.");
			return;
		}
	}//addPassenger
	
	/*========================= addGroup ==========================
	** This will prompt the user to enter necessary information prior
	** to reserve a seat for a group of passengers.
	**/
	public void addGroup(){
		
		String groupName;
		String[] passNames;
		String serviceClass;
		String[] seats;
		System.out.printf("Group Name: ");
		groupName = getUserInput(inputReader);		 
		System.out.printf("Names: ");
		passNames = getUserInput(inputReader).split(",");
		System.out.printf("Service Class (First or Economy): ");
		serviceClass = getUserInput(inputReader);
		
		seats = new String[passNames.length];
		//First Class
		if(serviceClass.toUpperCase().equals("FIRST")){
			if(sChart.getNumPassFirst() == MAXFIRST){
				System.out.println("No more available seats in First Class.");
				return;
			}else if(sChart.getNumPassFirst() + passNames.length > MAXFIRST){
				System.out.println("Not enough seats to accomodate all passengers.");
				return;
			}else{
				seats = sChart.searchGroupAvaiFirst(groupName, passNames,seats);
				if(seats != null){
					System.out.println("Your reserved seats are:");
					for(int count = 0; count < passNames.length; count++){
						System.out.println(passNames[count].trim() + ": " + seats[count]);
					}
				}else{
					System.out.println("Invalid choice => Request failed.");
					return;
				}
			}
		//Economy Class
		}else if(serviceClass.toUpperCase().equals("ECONOMY")){
			if(sChart.getNumPassEcon() == MAXECON){
				System.out.println("No more available seats in Economy Class.");
				return;
			}else if(sChart.getNumPassEcon() + passNames.length > MAXECON){
				System.out.println("Not enough seats to accomodate all passengers.");
				return;
			}else{
				seats = sChart.searchGroupAvaiEcon(groupName, passNames, seats);
				if(seats != null){
					System.out.println("Your reserved seats are:");
					for(int count = 0; count < passNames.length; count++){
						System.out.println(passNames[count].trim() + ": " + seats[count]);
					}
				}else{
					System.out.println("Invalid choice => Request failed.");
					return;
				}
			}
		}	
	}//addGroup
	
	/*========================= cancelReservation ==========================
	** This will prompt the user to enter necessary information to cancel 
	** reservation, individual or group, in First or Economy Class
	**/
	public void cancelReservation(){
		String passengerType;
		String passName;
		String groupName;
		int success = 0;
		
		System.out.printf("Cancel [I]ndividual or [G]roup? ");
		passengerType = getUserInput(inputReader);		
		if(passengerType.toUpperCase().equals("I")){
			System.out.println("Name: ");
			passName = getUserInput(inputReader);
			success = sChart.searchToCancelIndi(passName);
	
		} else if(passengerType.toUpperCase().equals("G")){
			System.out.println("Group Name: ");
			groupName = getUserInput(inputReader);
			success = sChart.searchToCancelGroup(groupName);					
		}else{
			System.out.println("Invalid choice => Request failed.");
			return;
		}		
		if(success == 1){
			System.out.println("Successfully cancel your reservation.");
			return;
		}else{
			System.out.println("Invalid name. Please try again.");
			return;
		}
		
	}
		
	/*========================= printAvailability ==========================
	** This will prompt the user to enter necessary information to print 
	** availability chart in First or Economy Class
	**/
	public void printAvailability(){
		String serviceClass;
		
		System.out.printf("Service Class (First or Economy): ");
		serviceClass = getUserInput(inputReader);
		if(serviceClass.toUpperCase().equals("FIRST")){
			sChart.printAvailabilityFirst();
		} else if(serviceClass.toUpperCase().equals("ECONOMY")){
			sChart.printAvailabilityEcon();
		}else{
			System.out.println("Invalid choice => Request failed.");
			return;
		}
	}//printAvailability
	
	/*========================= printManifest ==========================
	** This will prompt the user to enter necessary information to print 
	** manifest chart in First or Economy Class
	**/
	public void printManifest(){
		String serviceClass;
		
		System.out.printf("Service Class (First or Economy): ");
		serviceClass = getUserInput(inputReader);
		if(serviceClass.toUpperCase().equals("FIRST")){
			sChart.printManifestFirst();
		} else if(serviceClass.toUpperCase().equals("ECONOMY")){
			sChart.printManifestEcon();
		}else{
			System.out.println("Invalid choice => Request failed.");
			return;
		}
	}//printManifest
	
	/*========================= saveFlight ==========================
	** Save all the final data to a file
	**/
	public void saveFlight(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(flightFile, false));
			writer.write("First 1-2, Left: A-B, Right: C-D; Economy 10-29, Left: A-C, Right: D-F");
			writer.newLine();
			//pArrayFirst Print
			for(int rowF = 0; rowF < SeatingSystem.ROW_FIRST; rowF++){
				for(int colF = 0; colF < SeatingSystem.COL_FIRST; colF++){		
					if(sChart.pArrayFirst[rowF][colF].getName() != null){	
						char column = (char)(colF + 1 + 64);						
						String s1 = Integer.toString(rowF + 1);
						String s2 = Character.toString(column);
						String s3 = s1.concat(s2);
						writer.write(s3);
						writer.write(", ");
						if(sChart.pArrayFirst[rowF][colF].getGroupName() != null){
							writer.write("G, ");
							writer.write(sChart.pArrayFirst[rowF][colF].getGroupName());
							writer.write(", ");
							writer.write(sChart.pArrayFirst[rowF][colF].getName());
						}else{
							writer.write("I, ");
							writer.write(sChart.pArrayFirst[rowF][colF].getName());
						}
						writer.newLine();
					}
				}
			}		
			//pArrayFirst Print
			for(int rowE = 0; rowE < SeatingSystem.ROW_ECON; rowE++){
				for(int colE = 0; colE < SeatingSystem.COL_ECON; colE++){		
					if(sChart.pArrayEcon[rowE][colE].getName() != null){	
						char column = (char)(colE + 1 + 64);						
						String s1 = Integer.toString(rowE + 10);
						String s2 = Character.toString(column);
						String s3 = s1.concat(s2);
						writer.write(s3);
						writer.write(", ");
						if(sChart.pArrayEcon[rowE][colE].getGroupName() != null){
							writer.write("G, ");
							writer.write(sChart.pArrayEcon[rowE][colE].getGroupName());
							writer.write(", ");
							writer.write(sChart.pArrayEcon[rowE][colE].getName());
						}else{
							writer.write("I, ");
							writer.write(sChart.pArrayEcon[rowE][colE].getName());
						}
						writer.newLine();
					}
				}
			}
			writer.close();		
		} catch (IOException e) {
			System.out.println("Error reading the output file to write");
			e.printStackTrace();
		}	
	}
	
}//Flight
