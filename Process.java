package cpuScheduler;


import java.util.ArrayList;
import java.util.Scanner;

import java.awt.Color;
import java.util.Iterator;
import java.util.PriorityQueue;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



/*
 * Sample
p3 yellow 4 10 3
p2 black 3 12 9
p4 pink 29 14 8
p5 gray 15 10 2
p1 red 0 30 4
p6 magenta 40 15 10
p7 green 0 7 1
p8 white 1 2 5

 */

public class Process implements Comparable<Process>{
	private static Priority sc = new Priority();
	private int priorityFactor;
	private int arrivalTime;
	private int burst;
	private String color, name; 
	int id;
	
	
	private static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws InterruptedException {
		
		

		
		
		System.out.println("how many threads to add?");
		int n = scan.nextInt();
		System.out.print("Enter the context switch:");
		int context_switch = scan.nextInt();
		sc.setContextSwitch(context_switch);
		scan.nextLine();
		ArrayList<Process> p = createP(n);
		
		p = arrange(p);
		
		
		for(Process x : p) {x.connect();};
		sc.run();
	

	}
	
	public static ArrayList<Process> arrange(ArrayList<Process> p) {
		
		ArrayList<Process> j = new ArrayList<Process>();
		Process temp = p.get(0);
		int index=0;
		while(true) {
			for(Process x : p) {
				if(temp.getArrivalTime()>x.getArrivalTime()) {
					temp=x;
				}
			}
			temp.setId(index);
			index++;
			j.add(temp);
			int k = p.indexOf(temp);
			
			p.remove(k);
			if(p.size()<=0) {break;}
			temp = p.get(0);
		}
		
		
		return j;
		
	}
	
	
	public Process(Priority s,String name, int priority, int arr,int burst, String color) {
		sc = s;
		priorityFactor = priority;
		arrivalTime = arr;
		this.burst=burst;
		this.color = color;
		this.name = name;
		
	}
	
	public void connect() {
		
		
		sc.prepareProcess(this);

	}

	
	
	/**
2 1 15 red
1 1 21 black
6 1 10 yellow
7 9 9 aqua
8 2 15 purple



hp1 Dark_Goldenrod 0 80 1
hp2 Navy 0 88 9 
hp3 Ivory 24 71 7
hp4 Dim_Gray 26 92 6
mp1 Indigo 2 5 10
mp2 Olive_Drab 15 31 6
mp3 Medium_Turquoise 14 100 6
LP1 Light_Grey 8 10 6
LP2 Lemon_Chiffon 2 34 7
LP3 Violet 25 72 4
	 */
	

	@Override
	public int compareTo(Process o) {
	//	System.out.println(this.getColor()+" "+ o.getColor());
		if(this.getFactor()>o.getFactor()) {
			
			return 1;
		}else if(this.getFactor()<o.getFactor()) {
			return -1;
		}
		else {
			if(this.getArrivalTime()>o.getArrivalTime()) {return 1;}
			else if(this.getArrivalTime() < o.getArrivalTime()) {return -1;}
		}
		return 0;
	}
	public static ArrayList<Process> createP(int n) {
		
		ArrayList<Process> pr = new ArrayList<Process>();
		
		System.out.println("Enter Name - color - arrival - burst time - priority:");
		
		int fact=0,arrival=0,burst=0;
		String color,name,command[];
		while(n>0) {
			n--;
			
			name = scan.nextLine();
			command = name.split(" ");
			name=command[0];
			
			color = command[1];
			arrival = Integer.parseInt(command[2]);
			burst = Integer.parseInt(command[3]);
			fact = Integer.parseInt(command[4]);
			
			
			
			Process p = new Process(sc, name, fact, arrival,burst, color);
			
			pr.add(p);
			
			
		}
		return pr;

	}
	public int getFactor() {
		return priorityFactor;
	}
	public int getArrivalTime() {
		return arrivalTime;
	}
	public void doJob() {
		
			
			try {
				Thread.sleep(100 * burst);
			} catch (Exception e) {
			
			}	
		
	}
	
	public int getBurst() {
		return burst;
	}
	public String getColor() {
		
		return color;
	}
	public void updateFactor() {
		if(priorityFactor>0) {priorityFactor/=2;}
	}
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}








class Priority {
	
	private static PriorityQueue<Process> waiting  = new PriorityQueue<Process>();
	
	private Vector<Process> arrival = new Vector<Process>();
	private Vector<String>	order = new Vector<String>();
	private static Process current=null;
	private static int timer=0, check=1,count=0;
	private static double avgWaiting=0.0, avgRoundTrip=0.0;
	private static int contextSwitch=1;
	// the frame and apnel to show the gui
	private static JFrame frame;
	private static JPanel panel;
	// the 2D array to save the labels that represent the progress
	private static JLabel[][] progress;
	
		
	/*
	 * Sample
	p3 yellow 4 10 3
	p2 black 3 12 9
	p4 pink 29 14 8
	p5 gray 15 10 2
	p1 red 0 30 4
	p6 magenta 40 15 10
	p7 green 0 7 1
	p8 white 1 2 5

	 */
	
	public void setContextSwitch(int c) {
		contextSwitch=c;
	}
	public void addProcess() {
		Process e = arrival.get(0);
		waiting.add(e);
		//
		arrival.remove(0);
		
	}
	
	public void prepareProcess(Process e) {
		arrival.add(e);
		
	}
	
	public void run() {
		GUI();
		Process e =arrival.get(0);
		while(!arrival.isEmpty()&& e.getArrivalTime()==arrival.get(0).getArrivalTime()) {addProcess();}
		
		acitvate();
		
	}
	// the function to construct the gui
	private void GUI() {
		// k determines the height of the frame if you have the array of the processes to come
		// before the program start running.  maximum in a frame is 12 process
		int k;
		if(arrival.size()>12) {k=12;}else {k=arrival.size();}
		// set the frame's title and size
		frame = new JFrame("Priority Scheduler");
		frame.setSize(1080, 40 * k);
		 panel = new JPanel();
		 
		panel.setLayout(null);
		
		
		// add the panel to the frame
		frame.add( panel);
		
		// make the frame appear in the center of the screen
		frame.setLocationRelativeTo(null);
		// make the frame always on the top of any application
		frame.setAlwaysOnTop(true);
		 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// calculate the total seconds the processes will take and add one for context switching
		k=0;
		for(Process x : arrival) {
			k+=x.getBurst()+contextSwitch;
			
		}
		// check if any process' arrival time is bigger than or equal the total time of processes burst
		for(Process x : arrival) {
			if(x.getArrivalTime() >= k) {
				
				k += (x.getArrivalTime()-k)+ x.getBurst()+contextSwitch;
			}
			
		}
		
		
		//calculate the size of each label py pexels
		int s = Math.round(900/(k));
		
		// initialise the 2D array of labels, number of rows equal to number of processes
		// number of columns equal number of seconds + 1 for the process name
		progress = new JLabel[arrival.size()][k+1];
		// initialise every label and if the j=0 then it is a new row and we add the process name
		for(int i=0; i< arrival.size();i++) {
			for(int j=0;j<(k+1);j++) {
				JLabel label = new JLabel();
				if(j==0) {
					
					label.setText(arrival.get(i).getName());
					label.setBounds(20, 30*i +20, 400, 20);
					
				}else {
					
					label.setBounds(s*j+120, 30*i +20, s, 20);
					
					label.setOpaque(true);
					//
				}
				panel.add(label);
				progress[i][j]=label;
				
			}
		}
		
		
		
		
		// set visible to show the frame
		frame.setVisible(true);
		
	}

	private void acitvate() {
		
		
		while(! waiting.isEmpty()) {
			if(timer/20>=check) {
				check=timer/20 +1;
				
			Iterator<Process> iter = waiting.iterator();
			Process x = iter.next();
			while(iter.hasNext()) {
				Process j = iter.next();
				if(x.getArrivalTime()>j.getArrivalTime()) {
					x = j;
				}
				
				
			}
			if(timer-x.getArrivalTime()>=20) {
			x.updateFactor();
			waiting.remove(x);
			waiting.add(x);
			}
		}
			
			
		current = waiting.poll();
		order.add(current.getName());
		
		
		int wait = (timer-current.getArrivalTime());
		int roundTrip = wait+current.getBurst();
		avgWaiting = avgWaiting + wait;
		avgRoundTrip = avgRoundTrip + roundTrip;
		
		System.out.println("Allocated: "+current.getName()+"  color: "+current.getColor()+"	waiting time="+wait+" roundtrip="+roundTrip);
		int x = current.getBurst();
	// loop to simulate the change of time and color the labels to show the progress
		for(int i=1; i<=x;i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			
			timer++;
			//change the color according to the time and the current working process
			color();
			update();
			
		}
		for(int i=0; i<contextSwitch;i++)
		{
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				
			}	
			
			
		timer++;
		update();
		color();}
		exit();
		if(waiting.isEmpty() && !arrival.isEmpty()) {
			while(waiting.isEmpty()) {timer++;update();System.out.println(timer);}
		}
			
		}
		System.out.println("Execution Order:");
		for(String x : order) { System.out.print(x+" "); }
		System.out.println(" ");
		avgWaiting = (avgWaiting / count)*100;
		avgRoundTrip = (avgRoundTrip / count)*100;
		
		
		avgRoundTrip = Math.round(avgRoundTrip) / 100.0;
		
		
		avgWaiting = Math.round(avgWaiting) / 100.0;
		
		System.out.println("Average waiting= "+avgWaiting+"  Average roundtrip= "+avgRoundTrip);
		
		
	}
	public void addGui() {
		progress[count][0].setText(current.getName());
		frame.repaint();
	}
	
	public void exit() {
		
		
	
		count++;
		
		System.out.println("exiting... "+current.getName());
		current=null;
	}
	
	public void update() {
		if(arrival.size()>0) {
		Process e = arrival.get(0);
		
		while( e.getArrivalTime()<=timer) {
			
			addProcess();
			if(arrival.size()<=0) {break;}
			e = arrival.get(0);
		}
	}
	
}
	// change the color of the label according to the id of the process and the time
public void color() {
		progress[current.getId()][timer].setBackground(picker());
		// update the frame
		frame.repaint();
	}

// choose the color based on the name
public Color picker() {
	Color c =null;
	
	switch (current.getColor().toLowerCase()) {
	case "black": {
		
		c = Color.BLACK;
		break;
	}
	case "blue": {
		
		c = Color.BLUE;
		break;
	}
	case "green": {
		
		c = Color.GREEN;
		break;
	}
	case "yellow": {
		
		c = Color.YELLOW;
		break;
	}
	case "orange": {
		
		c = Color.ORANGE;
		break;
	}
	case "gray": {
		
		c = Color.GRAY;
		break;
	}
	case "dark_gray": {
		
		c = Color.DARK_GRAY;
		break;
	}
	case "magenta": {
		
		c = Color.MAGENTA;
		break;
	}
	case "red": {
		
		c = Color.RED;
		break;
	}
	case "white": {
		
		c = Color.WHITE;
		break;
	}
	case "cyan": {
		
		c = Color.CYAN;
		break;
	}
	case "pink": {
		
		c = Color.PINK;
		break;
	}
	case "light_gray": {
		
		c = Color.lightGray;
		break;
	}
	
	
	default:
		c = Color.BLACK;
	}
	
	return c;
}


}



/*
 * Sample
p3 yellow 4 10 3
p2 black 3 12 9
p4 pink 29 14 8
p5 gray 15 10 2
p1 red 0 30 4
p6 magenta 40 15 10
p7 green 0 7 1
p8 white 1 2 5

 */




