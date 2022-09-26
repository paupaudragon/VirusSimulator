/**
 * 
 */
package virusSimulator;

import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Simulates virus transmission that people within the board will experience
 * from HEALTHY to INFECTED to RECOVERED to IMMUNED to Healthy or Dead.
 * 
 * @author tzhou & Penny
 *
 */
public class Simulation {
	private static final int SIMULATION_HEIGHT = 70;
	private static final int SIMULATION_WIDTH = 100;
	private static final int WIDTH_ADJUSTMENT = 50;
	private static final int HEIGHT_ADJUSTMENT = 15;
	private static Person[] starter_patients = new Person[4];
	private static ArrayList<Person> healthy_people = new ArrayList<Person>();
	private static ArrayList<Person> recovering = new ArrayList<Person>();// red to orange;
	private static ArrayList<Person> getting_immunity = new ArrayList<Person>();// orange to green
	private static ArrayList<Person> ready_to_healthy = new ArrayList<Person>();// back to blue
	private static Person temp = new Person(25, 3, State.HEALTHY);

	private static int newInfected = 0;
	private static int totalInfected = 0;
	private static int totalDeath = 0;
	private static int deathRate = 0;
	private static Person[][] grids;

	static FileWriter output_file;
	static File log = new File("C:\\Users\\owner\\Desktop\\2420 Algorithm\\output_2.txt");
	static PrintWriter writer;
	static String path = "C:\\Users\\owner\\Desktop\\2420 Algorithm\\output-2.txt";

	/**
	 * Test Client to 1. Create four random virus carriers. 2. Trigger Spread method
	 * that start virus transmission.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		StdDraw.setCanvasSize(1600, 800);
		StdDraw.setXscale(0, 200);
		StdDraw.setYscale(0, 100);
		Font font1 = new Font("arial",Font.BOLD,40);
		Font font2 = new Font("arial",Font.BOLD,15);
		StdDraw.setFont(font1);
		StdDraw.text(100, 90, "SIMULATION");
		StdDraw.setFont(font2);
		StdDraw.text(100, 10, "Population: 4000"+"     " 
				+"Recovery rate: 3"+"     " +"Immunity rate: 2"+"     " +"Death rate: 0");

		grids = new Person[SIMULATION_HEIGHT][SIMULATION_WIDTH];
		StdDraw.enableDoubleBuffering();
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.setPenRadius(0.01);
		for (int i = 0; i < grids.length; i++) {
			for (int j = 0; j < grids[0].length; j++) {
				healthy_people.add(new Person(j + WIDTH_ADJUSTMENT, i + HEIGHT_ADJUSTMENT, State.HEALTHY));
				StdDraw.point(j + WIDTH_ADJUSTMENT, i + HEIGHT_ADJUSTMENT);
				grids[i][j] = new Person(i, j, State.HEALTHY);
			}
		}
		// Changes population to 3000.
/************************************************************************/
//		int index;
//		Random rand_population = new Random ();
//		for (int r = 0; r<3000;r++) {
//			index = rand_population.nextInt(3000);
//			Person p = healthy_people.get(index);
//			StdDraw.setPenColor(StdDraw.WHITE);
//			StdDraw.point(p.getLocation_x(), p.getLocation_y());
//			healthy_people.remove(index);
//		}
/************************************************************************/
		try {
			if (log.exists() == false) {
				log.createNewFile();
			}
			writer = new PrintWriter(new FileWriter(log, true));
		} catch (IOException except) {
			except.printStackTrace();
		}

		writer.printf("New infected: %d\t\t Total infected: %d\t\t Total death: %d\t\t", newInfected, totalInfected,
				totalDeath);
		writer.println();
		starter_patients = start_random_patients();
		start_reaction_chain(starter_patients);
		writer.close();
	}

	/**
	 * Starts the four virus carriers.
	 * 
	 * @return a Person array Contains the starter patients.
	 */
	private static Person[] start_random_patients() {

		StdDraw.pause(1000);
		StdDraw.show();

		StdDraw.enableDoubleBuffering();
		StdDraw.setPenColor(StdDraw.BLUE);
		Random rand = new Random();
		Person[] starter = new Person[4];
		for (int i = 0; i < starter.length; i++) {
			int x = rand.nextInt(SIMULATION_WIDTH) + WIDTH_ADJUSTMENT;
			int y = rand.nextInt(SIMULATION_HEIGHT) + HEIGHT_ADJUSTMENT;
			starter[i] = new Person(x, y, State.HEALTHY);
			StdDraw.point(x, y);
			healthy_people.remove(new Person(x, y, State.HEALTHY));
			recovering.add(new Person(x, y, State.RECOVER));
			newInfected++;
			totalInfected++;
		}
		writer.append("New Infected:  " + String.valueOf(newInfected) + "\t\t");
		writer.append("Total Infected: " + String.valueOf(totalInfected) + "\t\t");
		writer.append("Total death: " + String.valueOf(totalDeath) + "\n");
		StdDraw.show();
		return starter;
	}

	/**
	 * Spreads the virus and attains heard immunity.
	 * 
	 * @param Person array Represents starter patients
	 */
	private static void start_reaction_chain(Person[] p) throws IOException {
		int[] start_size = { 5, 1, 1, 5 };
		int[] cycle = { 1, 1, 1, 1 };

		for (int b = 0; b < 500; b++) {
			StdDraw.enableDoubleBuffering();
			getSick(p, start_size, cycle);
			StdDraw.pause(100);
			StdDraw.show();

			StdDraw.enableDoubleBuffering();
			getRecover(p, start_size, cycle);
			StdDraw.pause(100);
			StdDraw.show();

			StdDraw.enableDoubleBuffering();
			getImmune(p, start_size, cycle);
			StdDraw.pause(100);
			StdDraw.show();

			if (isOver()) {
				break;
			}
			;
		}
	}

	public static boolean isOver() {
		if (ready_to_healthy.size() == 7000) {
			return true;
		} else
			return false;
	}

	/**
	 * Infects healthy neighboring people with the virus.
	 * 
	 * @param Person array containing the four first virus carriers
	 * @param an     integer array representing the size of the first transmission
	 * @param an     integer array representing the cycle of this transmission is at
	 */
	public static void getSick(Person[] p, int[] start_size, int[] cycle) {
		newInfected = 0;
		for (int z = 0; z < p.length; z++) {
			for (int i = p[z].getLocation_x() - 2 * cycle[0]; i < start_size[0] + p[z].getLocation_x()
					- 2 * cycle[0]; i++) {
				for (int j = p[z].getLocation_y() - 2 * cycle[0]; j < start_size[0] + p[z].getLocation_y()
						- 2 * cycle[0]; j++) {
					for (int w = 0; w < healthy_people.size(); w++) {
						if (healthy_people.get(w).equals(new Person(i, j, State.HEALTHY))) {
							StdDraw.setPenColor(StdDraw.RED);
							StdDraw.point(i, j);
							recovering.add(new Person(i, j, State.RECOVER));
							healthy_people.remove(w);
							newInfected++;
							totalInfected++;
						}
					}
				}
			}
		}
		if (deathRate == 5) {
			int counter = (int) Math.round(newInfected * 0.05);
			Random rand = new Random();
			if (counter != 0) {
				for (int e = 0; e < counter; e++) {
					StdDraw.setPenColor(StdDraw.BLACK);
					int a = rand.nextInt(recovering.size());
					Person dead = recovering.get(a);
					StdDraw.point(dead.getLocation_x(), dead.getLocation_y());
					recovering.remove(a);
					totalDeath++;
			}
				}
		}
		write_record();
		start_size[0] += 4;
		cycle[0]++;
	}

	/**
	 * Recovers people who got infected based on how long they have been infected
	 * 
	 * @param Person array containing the four first virus carriers
	 * @param an     integer array representing the sizes of virus expansion and
	 *               immunity expansion
	 * @param an     integer array representing the cycles of virus expansion and
	 *               immunity expansion
	 */
	public static void getRecover(Person[] p, int[] start_size, int[] cycle) {
		if (cycle[0] == 3) {
			for (int q = 0; q < p.length; q++) {
				StdDraw.setPenColor(StdDraw.DARK_GRAY);
				StdDraw.point(p[q].getLocation_x(), p[q].getLocation_y());
			}
			write_record();
			start_size[1] += 4;
			cycle[1]++;
		} else if (cycle[0] % 3 == 0 && cycle[0] != 3) {
			cycle[1]--;
			for (int m = 0; m < p.length; m++) {
				for (int i = p[m].getLocation_x() - 2 * cycle[1]; i < start_size[1] + p[m].getLocation_x()
						- 2 * cycle[1]; i++) {
					for (int j = p[m].getLocation_y() - 2 * cycle[1]; j < start_size[1] + p[m].getLocation_y()
							- 2 * cycle[1]; j++) {
						temp = new Person(i, j, State.RECOVER);
						for (int w = 0; w < recovering.size(); w++) {
							if (recovering.get(w).equals(temp)) {
								StdDraw.setPenColor(StdDraw.DARK_GRAY);
								StdDraw.point(i, j);
								getting_immunity.add(new Person(i, j, State.IMMUNE));
								recovering.remove(temp);
							}
						}
					}
				}
			}
			write_record();
			start_size[1] += 4;
			cycle[1] += 2;
		}
	}

	/**
	 * Writes record update to an output file.
	 */
	public static void write_record() {
		writer.append("New Infected:  " + String.valueOf(newInfected) + "\t\t");
		writer.append("Total Infected: " + String.valueOf(totalInfected) + "\t\t");
		writer.append("Total death: " + String.valueOf(totalDeath) + "\n");
	}

	/**
	 * Simulates people gain immunity after three cycles of recovery.
	 * 
	 * @param Person array containing the four first virus carriers
	 * @param an     integer array representing the sizes of virus expansion and
	 *               immunity expansion
	 * @param an     integer array representing the cycles of virus expansion and
	 *               immunity expansion
	 */
	public static void getImmune(Person[] p, int[] start_size, int[] cycle) {
		if (cycle[0] == 7) {
			for (int q = 0; q < p.length; q++) {
				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
				StdDraw.point(p[q].getLocation_x(), p[q].getLocation_y());
			}
			write_record();
			start_size[2] += 4;
			cycle[2]++;
		} else if (cycle[0] % 7 == 0 && cycle[0] != 7) {
			cycle[2]--;
			for (int m = 0; m < p.length; m++) {
				for (int i = p[m].getLocation_x() - 2 * cycle[2]; i < start_size[2] + p[m].getLocation_x()
						- 2 * cycle[2]; i++) {
					for (int j = p[m].getLocation_y() - 2 * cycle[2]; j < start_size[2] + p[m].getLocation_y()
							- 2 * cycle[2]; j++) {

						temp = new Person(i, j, State.IMMUNE);
						for (int w = 0; w < getting_immunity.size(); w++) {
							if (getting_immunity.get(w).equals(temp)) {
								StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
								StdDraw.point(i, j);
								ready_to_healthy.add(new Person(i, j, State.EMPTY));
								getting_immunity.remove(temp);
							}
						}
					}
				}
			}
			start_size[2] += 4;
			cycle[2] += 2;
		}
	}
}
