import java.awt.Color;
import java.util.ArrayList; 
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



import java.util.Collections;


public class SRTF implements Comparable<SRTF>{
	
	int complete, burst_time, wait_time, id, arrival_time ;
	String name, color;
	int remain_burst_time, turnAround_time ; 
	// the frame and panel to show the gui
	private static JFrame frame;
	private static JPanel panel;
	static int time;
	// the 2D array to save the labels that represent the progress
	private static JLabel[][] progress;
	
	
	public SRTF(String name,int burst_time, int arrival_time, String color) {
		this.name = name;
		this.color = color;
		this.arrival_time = arrival_time ; 
		this.burst_time = burst_time ; 
		this.wait_time = 0 ; 
		this.complete = arrival_time ; 
		this.remain_burst_time = burst_time ; 
	}
	
	public int compareTo(SRTF process) {
		return this.arrival_time - process.arrival_time ; 
	}

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in) ;
		System.out.print("Enter number of processes : ");
		int n = input.nextInt() ; 
		System.out.print("Enter context switch : ");
		int contextSwitch = input.nextInt() ; 
		
		ArrayList<SRTF> arr = new ArrayList<SRTF>() ;
		
		for(int i=0 ; i<n ; i++) {
			System.out.print("Enter Name, burst time, arrival time : ");
			String name=input.next();
			int burst=input.nextInt(), arrival=input.nextInt() ; 
			String color = input.next() ;
			arr.add(new SRTF(name, burst, arrival, color)) ; 
		}

		

		
		int current_time=0, numOf_process=0, idxOfSwitch = -1 ;
		ArrayList<SRTF> order=new ArrayList<SRTF>();
		ArrayList<Integer> timeOrder = new ArrayList<Integer>();
		Collections.sort(arr);
		int q=0;
		for(SRTF x: arr) {x.id=q;q++;}
		
		
		while(numOf_process!=n) {
			int flag=-1;
			ArrayList<Integer> indices = new ArrayList<Integer>() ; 
			SRTF current=null;
			for( int i=0 ; i<n ; i++) {
				
				if( arr.get(i).arrival_time<=current_time && arr.get(i).remain_burst_time>0 ) {
					indices.add(i) ; 
					arr.get(i).complete++ ; 
				}
				
			}
			if(indices.size() != 0) {
				int minBurst=arr.get(indices.get(0)).burst_time, idxMinBurst=indices.get(0) ; 
				int queueInd=0 ; 
				for(int i=0 ; i<indices.size() ; i++) {
					
					if(arr.get(indices.get(i)).burst_time < minBurst ) {
						idxMinBurst = indices.get(i) ;
						minBurst = arr.get(indices.get(i)).burst_time ;
						
					}
				}
				arr.get(idxMinBurst).remain_burst_time-- ;
				
				
				if( idxOfSwitch == -1 ) {
					idxOfSwitch = idxMinBurst ;
					order.add(arr.get(idxOfSwitch));
					timeOrder.add(current_time);
				}
				current = arr.get(idxOfSwitch);
				System.out.println(current.name+" - "+current_time);
				if( idxOfSwitch != idxMinBurst ) {
					idxOfSwitch = idxMinBurst ; 
					order.add(arr.get(idxOfSwitch));
					
					
					for(int i=0 ; i<indices.size() ; i++) {
						arr.get(indices.get(i)).complete += contextSwitch ; 
						
					}
					
					current_time+=contextSwitch;
					timeOrder.add(current_time);
					
					
				}
				
				
			}
			
			
			
			current_time++ ;
			
			
			for(int i=0 ; i<indices.size() ; i++) {
				if( arr.get(indices.get(i)).remain_burst_time == 0 ) {
					numOf_process++ ; 
				}
			}
		}
		
		GUI(n, arr, contextSwitch, timeOrder.size());
		for(int i=0;i<order.size()-1;i++) {
			
			for(int j=timeOrder.get(i);j<timeOrder.get(i+1);j++) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					
				}
				color(order.get(i),j);
			}
		}
		int index = order.size()-1;
		for(int j=timeOrder.get(timeOrder.size()-1);j<time;j++){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				
			}
			color(order.get(index),j);
		}
		
		
		
		// To handle turn-around time of each process
		// As known that turn-around time = complete-arrival & wait=turn around - burst time
		float wait_avg=0, turnAround_avg=0 ; 
		for(int i=0 ; i<n ; i++) {
			arr.get(i).turnAround_time = arr.get(i).complete - arr.get(i).arrival_time ;
			arr.get(i).wait_time = arr.get(i).turnAround_time - arr.get(i).burst_time ;
			wait_avg += arr.get(i).wait_time ; 
			turnAround_avg += arr.get(i).turnAround_time ; 
			//arr.get(i).complete -= arr.get(i).arrival_time ; 
		}
		
		wait_avg /= n ; 
		turnAround_avg /= n ; 
		
		for(SRTF process : arr) {
			System.out.println();
			System.out.println("*********************");
			System.out.println("name: "+process.name+",  burst: "+process.burst_time+",  arrival time: "+process.arrival_time);
			System.out.println(",  complete time: "+process.complete+",  waiting time: "+process.wait_time+ ",  turn-around time: "+process.turnAround_time) ; 
		}
		
		System.out.println("*********************");
		System.out.println("The average wait is : " + wait_avg) ; 
		System.out.println("The average turn-around is : " + turnAround_avg);
	}
	
	private static void GUI(int n, ArrayList<SRTF> arr, int con, int si) {
		// k determines the height of the frame if you have the array of the processes to come
		// before the program start running.  maximum in a frame is 12 process
		int k;
		if(n>12) {k=12;}else {k=n;}
		// set the frame's title and size
		frame = new JFrame("Priority Scheduler");
		frame.setSize(1080, 50 * k);
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
		for(SRTF x : arr) {
			k+=x.burst_time;
			
		}
		// check if any process' arrival time is bigger than or equal the total time of processes burst
		for(SRTF x : arr) {
			if(x.arrival_time >= k) {
				
				k += (x.arrival_time-k)+ x.burst_time;
			}
			
		}
		k = k+(con*si);
		time=k;
		
		//calculate the size of each label py pexels
		int s = Math.round(900/(k));
		
		// initialise the 2D array of labels, number of rows equal to number of processes
		// number of columns equal number of seconds + 1 for the process name
		progress = new JLabel[n][k+2];
		// initialise every label and if the j=0 then it is a new row and we add the process name
		for(int i=0; i<n;i++) {
			for(int j=0;j<(k+1);j++) {
				JLabel label = new JLabel();
				if(j==0) {
					
					label.setText(arr.get(i).name);
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
	
	public static void color(SRTF current,int timer) {
		int t;
		
		
		progress[current.id][timer].setBackground(picker(current.color));
		
		frame.repaint();
	}

// choose the color based on the name
public static Color picker(String color) {
	Color c =null;
	
	switch (color.toLowerCase()) {
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


	/*

p1 10 2 red
p2 5 5 orange
p4 12 0 black
p3 3 8 pink

	 */
	
	
	
}
