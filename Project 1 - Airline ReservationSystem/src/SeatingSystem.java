
public class SeatingSystem {
	
	//Constants
	public static final int ROW_FIRST = 2;
	public static final int COL_FIRST = 4;
	public static final int ROW_ECON = 20;
	public static final int COL_ECON = 6;
	
	//Instance Variables
	public int numPassFirst = 0;
	public int numPassEcon = 0;
	
	public Passenger[][] pArrayFirst = new Passenger[ROW_FIRST][COL_FIRST];
	public Passenger[][] pArrayEcon = new Passenger[ROW_ECON][COL_ECON];
	
	//Getters
	public int getNumPassFirst() {
		return numPassFirst;
	}
	public int getNumPassEcon(){
		return numPassEcon;
	}

	/*======================= setExistingPassenger ========================
	** Reserve a seat for individual passenger from an existing flight
	** @param row - the row position on seating layout
	** @param col - the column position on seating layout
	** @param passName - passenger's name
	** @return res - the seating location
	**/
	public String setExistingPassenger(int row, int col, String passName){
		//First Class
		char column = (char)(col + 64);
		String s1 = Integer.toString(row);
		String s2 = Character.toString(column);
		String res = s1.concat(s2);

		if(row <= ROW_FIRST){
			pArrayFirst[row-1][col-1].setName(passName);
			pArrayFirst[row-1][col-1].setSeat(res);
			numPassFirst++;

		//Economy Class
		}else{
			pArrayEcon[row-10][col-1].setName(passName);
			pArrayEcon[row-10][col-1].setSeat(res);
			numPassEcon++;
		}			
		return res;
	}//setExistingPassenger
	
	/*========================= setExistingGroup ==========================
	** Reserve a seat for a group passenger from an existing flight
	** @param row - the row position on seating layout
	** @param col - the column position on seating layout
	** @param passName - passenger's name
	** @param groupname - passenger's group name
	** @return res - the seating location
	**/
	public String setExistingGroup(int row, int col, String passName, String groupName){
		
		char column = (char)(col + 64);
		String s1 = Integer.toString(row);
		String s2 = Character.toString(column);
		String res = s1.concat(s2);
		
		if(row <= ROW_FIRST){
			pArrayFirst[row-1][col-1].setName(passName);
			pArrayFirst[row-1][col-1].setGroupName(groupName);
			pArrayFirst[row-1][col-1].setSeat(res);
			numPassFirst++;
		}else{
			pArrayEcon[row-10][col-1].setName(passName);
			pArrayEcon[row-10][col-1].setGroupName(groupName);
			pArrayEcon[row-10][col-1].setSeat(res);
			numPassEcon++;
		}
		
		return res;
	}//setExistingGroup
		
	/*==================== searchAvailabilityFirst =====================
	** Search for available seats and then set a passenger on that seat.
	** @param seatPreference - the preferred seat from passenger
	** @param name - name of the passenger
	** @return seatLocation - the seat for added passengers
	**/
	public String searchAvailabilityFirst(String seatPreference, String name){
		String seatLocation = null;
		
		if(seatPreference.toUpperCase().equals("W")){
			for(int rowF = 0; rowF < ROW_FIRST; rowF++){
				//Window A
				if(pArrayFirst[rowF][0].getName() == null){	
					seatLocation = setExistingPassenger(rowF+1, 0+1, name);
					break;
				//Window D
				}else if(pArrayFirst[rowF][3].getName() == null){	
					seatLocation = setExistingPassenger(rowF+1, 3+1, name);
					break;
				}	
			}//loop
		}else if(seatPreference.toUpperCase().equals("A")){
			for(int rowF = 0; rowF < ROW_FIRST; rowF++){
				//Aisle B
				if(pArrayFirst[rowF][1].getName() == null){	
					seatLocation = setExistingPassenger(rowF+1, 1+1, name);
					break;
				//Aisle C
				}else if(pArrayFirst[rowF][2].getName() == null){	
					seatLocation = setExistingPassenger(rowF+1, 2+1, name);
					break;
				}	
			}
		}	
		return seatLocation;	
	}
	
	/*==================== searchAvailabilityEcon =====================
	** Search for available seating of group passengers in Economy Class
	** @param groupName - the name of the group
	** @param passNames - name of the passengers in this group
	** @return seatLocation - the seat for added passengers
	**/
	public String searchAvailabilityEcon(String seatPreference, String name){
		String seatLocation = null;
		if(seatPreference.toUpperCase().equals("W")){
			for(int rowE = 0; rowE < ROW_ECON; rowE++){
				if(pArrayEcon[rowE][0].getName() == null){	
					seatLocation = setExistingPassenger(rowE+10, 0+1, name);
					break;				
				}else if(pArrayEcon[rowE][5].getName() == null){	
					seatLocation = setExistingPassenger(rowE+10, 5+1, name);
					break;
				}	
			}
		}else if(seatPreference.toUpperCase().equals("C")){
			for(int rowE = 0; rowE < ROW_ECON; rowE++){
				if(pArrayEcon[rowE][1].getName() == null){	
					seatLocation = setExistingPassenger(rowE+10, 1+1, name);
					break;				
				}else if(pArrayEcon[rowE][4].getName() == null){	
					seatLocation = setExistingPassenger(rowE+10, 4+1, name);
					break;
				}	
			}
		}else if(seatPreference.toUpperCase().equals("A")){
			for(int rowE = 0; rowE < ROW_ECON; rowE++){
				if(pArrayEcon[rowE][2].getName() == null){	
					seatLocation = setExistingPassenger(rowE+10, 2+1, name);
					break;				
				}else if(pArrayEcon[rowE][3].getName() == null){	
					seatLocation = setExistingPassenger(rowE+1, 3+1, name);
					break;
				}	
			}
		}		
		return seatLocation;	
	}//searchAvailabilityEcon
	
	/*==================== searchGroupAvaiFirst =====================
	** Search for available seats and then set the group on seating.
	** @param groupName - name of the group
	** @param names - an array of all the members
	** @param seats -  an empty array containing seat locations 
	** @return seats - an array with seat locations
	**/
	public String[] searchGroupAvaiFirst(String groupName, String[] names, String[] seats){
		int numNeededSeats = names.length;
		int total;
		int largest;//largest number of available seats
		int rowLargest; //row with largest available seats
		int count = 0;		
		
		do{
			largest = 0;
			rowLargest = 0;
			for(int rowF = 0; rowF < ROW_FIRST; rowF++){
				total = 0;
				for(int colF = 0; colF < COL_FIRST; colF++){
					if(pArrayFirst[rowF][colF].getName() == null){
						total++;
					}	
				}
				if(total > largest){
					largest = total;
					rowLargest = rowF;
				}
			}
			//At the end you will have the rowLargest and largest.
			for(int colF2 = 0; colF2 < COL_FIRST; colF2++){
				if(pArrayFirst[rowLargest][colF2].getName() == null && count < names.length){
					seats[count] = setExistingGroup(rowLargest+1, colF2+1, names[count].trim(), groupName);
					count++;
				}
			}
			numNeededSeats -= largest;//remaining needed seats to be filled if any
		}while(numNeededSeats > 0);
	return seats;	
	}//searchGroupAvaiFirst
	
	/*==================== searchGroupAvaiEcon =====================
	** Search for available seats and then set the group on seating.
	** @param groupName - name of the group
	** @param names - an array containing passengers to be added
	** @param seats -  an empty array containing seat locations 
	** @return seats - an array with seat locations
	**/
	public String[] searchGroupAvaiEcon(String groupName, String[] names, String[] seats){
		
		int numNeededSeats = names.length;
		int total;
		int largest;//largest number of available seats
		int rowLargest; //row with largest available seats
		int count = 0;
		
		do{
			largest = 0;
			rowLargest = 0;
			for(int rowE = 0; rowE < ROW_ECON; rowE++){
				total = 0;
				for(int colE = 0; colE < COL_ECON; colE++){
					if(pArrayEcon[rowE][colE].getName() == null){
						total++;
					}	
				}
				if(total > largest){
					largest = total;
					rowLargest = rowE;
				}
			}
			//At the end you will have the rowLargest and largest.
			for(int colE2 = 0; colE2 < COL_ECON; colE2++){
				if(pArrayEcon[rowLargest][colE2].getName() == null && count < names.length){
					seats[count] = setExistingGroup(rowLargest+10, colE2+1, names[count].trim(), groupName);
					count++;
				}
			}
			numNeededSeats -= largest;//remaining needed seats to be filled if any
		}while(numNeededSeats > 0);

		return seats;	
	}//searchGroupAvaiEcon
	
	/*==================== searchToCancelIndi =====================
	** Search for an individual passenger and remove from flight
	** @param name - the name of passenger
	** @return success - 0 if successfully found and deleted
	**/
	public int searchToCancelIndi(String name){
		int success = 0;
		//Search in First Class
		for(int rowF = 0; rowF < ROW_FIRST; rowF++){
			for(int colF = 0; colF < COL_FIRST; colF++){
				if(pArrayFirst[rowF][colF].getName() != null){
					
					if(pArrayFirst[rowF][colF].getName().toUpperCase().equals(name.toUpperCase())){
						pArrayFirst[rowF][colF].setName(null);
						pArrayFirst[rowF][colF].setGroupName(null);
						pArrayFirst[rowF][colF].setSeat(null);
						numPassFirst--;
						return success = 1;
					}
				}	
			}
		}
		//Search in Economy Class
		for(int rowE = 0; rowE < ROW_ECON; rowE++){
			for(int colE = 0; colE < COL_ECON; colE++){	
				if(pArrayEcon[rowE][colE].getName() != null){
					
					if(pArrayEcon[rowE][colE].getName().toUpperCase().equals(name.toUpperCase())){
						pArrayEcon[rowE][colE].setName(null);
						pArrayEcon[rowE][colE].setGroupName(null);
						pArrayEcon[rowE][colE].setSeat(null);
						numPassEcon--;
						return success = 1;
					}
				}	
			}
		}	
		return success;
	}
	
	/*==================== searchToCancelGroup =====================
	** Search for all passengers in a group and remove from flight
	** @param groupName - the name of the group
	** @return success - 0 if successfully found and deleted
	**/
	public int searchToCancelGroup(String groupName){
		int success = 0;
		//Search in First Class
		for(int rowF = 0; rowF < ROW_FIRST; rowF++){
			for(int colF = 0; colF < COL_FIRST; colF++){
				if(pArrayFirst[rowF][colF].getGroupName() != null){
					
					if(pArrayFirst[rowF][colF].getGroupName().toUpperCase().equals(groupName.toUpperCase())){
						pArrayFirst[rowF][colF].setName(null);
						pArrayFirst[rowF][colF].setGroupName(null);
						pArrayFirst[rowF][colF].setSeat(null);
						numPassFirst--;
						success = 1;
					}
				}	
			}
		}
		//Search in Economy Class
		for(int rowE = 0; rowE < ROW_ECON; rowE++){
			for(int colE = 0; colE < COL_ECON; colE++){	
				if(pArrayEcon[rowE][colE].getGroupName() != null){
					
					if(pArrayEcon[rowE][colE].getGroupName().toUpperCase().equals(groupName.toUpperCase())){
						pArrayEcon[rowE][colE].setName(null);
						pArrayEcon[rowE][colE].setGroupName(null);
						pArrayEcon[rowE][colE].setSeat(null);
						numPassEcon--;
						success = 1;
					}
				}	
			}
		}	
		return success;
	}
	
	/*======================= printAvailabilityFirst ========================
	** Print the available seating in First Class
	**/
	public void printAvailabilityFirst(){
		int flag;
		//pArrayFirst Print
		System.out.println("First");
		for(int rowF = 0; rowF < ROW_FIRST; rowF++){
			flag = 1;
			for(int colF = 0; colF < COL_FIRST; colF++){				
				if(pArrayFirst[rowF][colF].getName() == null){
					if(flag == 1){
						System.out.printf("%d:", rowF + 1);
						flag = 0;
					}
					char column = (char)(colF + 1 + 64);
					System.out.printf(" %c,", column);
				}
			}
			System.out.println();
		}			
	}//printAvailabilityFirst
	
	/*======================= printAvailabilityEcon ========================
	** Print the available seating in Economy Class
	**/
	public void printAvailabilityEcon(){
		int flag;
		//pArrayEcon Print
		System.out.println("Economy");
		for(int rowE = 0; rowE < ROW_ECON; rowE++){
			flag = 1;
			for(int colE = 0; colE < COL_ECON; colE++){				
				if(pArrayEcon[rowE][colE].getName() == null){					
					if(flag == 1){
						System.out.printf("%d:", rowE + 10);
						flag = 0;
					}
					char column = (char)(colE + 1 + 64);								
					System.out.printf(" %c,", column);
				}
			}
			System.out.println();
		}		
	}//printAvailabilityEcon
	
	/*======================= printManifestFirst ========================
	** Print the occupied seating of passengers in First Class
	**/
	public void printManifestFirst(){
		System.out.println("First");
		//pArrayFirst Print
		for(int rowF = 0; rowF < ROW_FIRST; rowF++){
			for(int colF = 0; colF < COL_FIRST; colF++){		
				if(pArrayFirst[rowF][colF].getName() != null){	
					char column = (char)(colF + 1 + 64);			
					System.out.printf("%d%c: ", rowF + 1, column);
					System.out.printf("%s\n", pArrayFirst[rowF][colF].getName());
				}
			}
		}		
	}//printManifestFirst

	/*======================= printManifestEcon ========================
	** Print the occupied seating of passengers in Economy Class
	**/
	public void printManifestEcon(){
		System.out.println("Economy");
		//pArrayEcon Print
		for(int rowE = 0; rowE < ROW_ECON; rowE++){
			for(int colE = 0; colE < COL_ECON; colE++){				
				if(pArrayEcon[rowE][colE].getName() != null){					
					char column = (char)(colE + 1 + 64);
					System.out.printf("%2d%c: ", rowE + 10, column);
					System.out.printf("%s\n",pArrayEcon[rowE][colE].getName());	
				}
			}
		}	
	}//printManifestEcon

}//SeatingSystem
