/*
 * 
 */
public class ReservationSystem {
	
	public static void main(String[] args) {
		String ext = ".txt";
		String fileName = null;

		try{
			fileName= args[0];
			fileName = fileName.concat(ext);
		}catch (Exception e) {
			System.out.println("No flight has been detected. Please enter flight name.");
		}
			
		Flight flight1 = new Flight(fileName);			
		flight1.selectOptions();

	}//main

}// ReservationSystem
