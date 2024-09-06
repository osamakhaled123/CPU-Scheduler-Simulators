import java.awt.Color;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultEditorKit.PasteAction;


public class AGAT_Schedule {

    public String process;
    public int burst_time;
    public int arrival_time;
    public int priority;
    public int quantum;
    public double v1;
    public int ceil;
    public int index;
    public boolean Continue;
    public String color;
    private static JFrame frame;

    private static JPanel panel;
    private static JLabel[][] progress;
    
    private static void GUI(int x,Vector<AGAT_Schedule> n) {
		int k;
		if(x>12) {k=12;}else {k=x;}
		
		frame = new JFrame("Priority Scheduler");
		frame.setSize(1080, 50 * k);
		 panel = new JPanel();
		 
		panel.setLayout(null);
		
		
		
		frame.add( panel);
		
		
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		k=0;
		for(AGAT_Schedule m : n) {
			k+=m.burst_time+1;
			
		}
		for(AGAT_Schedule m : n) {
			if(m.arrival_time >= k) {
				
				k += (m.arrival_time-k)+ m.burst_time+1;
			}
			
		}
		
		
		
		int s = Math.round(900/(k));
		
		
		progress = new JLabel[x][k+1];
		
		for(int i=0; i< x;i++) {
			for(int j=0;j<(k+1);j++) {
				JLabel label = new JLabel();
				if(j==0) {
					
					label.setText(n.get(i).process);
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
		
		
		
		
		
		frame.setVisible(true);
		
	}


	public static void color(int x,int t,String c) {
		progress[x][t].setBackground(picker(c));
		
		frame.repaint();
	}
	
	public static Color picker(String x) {
		Color c =null;
		
		switch (x.toLowerCase()) {
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
	
    
    AGAT_Schedule(String process, int burst_time, int arrival_time, int priority, int quantum, String c){
        this.process = process;
        this.burst_time = burst_time;
        this.arrival_time = arrival_time;
        this.priority = priority;
        this.quantum = quantum;
        v1 = 0;
        ceil = 0;
        this.index = 0;
        Continue = true;
        color = c; 
    }

    public int AGAT_factor(double v2){
        return (10-this.priority) + this.ceil +  this.ciel((double) this.burst_time / v2);
    }

    public int round(double f){
        double test = f - (int)f;

        if( test >= 0.5){
            return (int)f + 1;
        }

        else {
            return (int)f;
        }

    }

    public int ciel(double c){
        double test = c - (int)c;

        if(test > 0){
            return (int)c + 1;
        }
        else{
            return (int) c;
        }
    }

    public void sort(Vector<AGAT_Schedule> processes,int num){
        int k,min = 100000;
        for(int i=0;i<num-1;i++){
            k = i;
            min = processes.get(i).arrival_time;
            for(int j=i+1;j<num;j++){
                if(min > processes.get(j).arrival_time){
                    k = j;
                    min = processes.get(j).arrival_time;
                }
            }
            if(k!=i){
                AGAT_Schedule temp = processes.get(i);
                //processes.get(i) = processes.get(k);
                processes.set(i,processes.get(k));
                processes.set(k,temp);
            }
        }
    }

    public void print(){
        System.out.println("process :"+ process +" \n"+ "burst_time :" + burst_time +"\n"+ "arrival_time :"
                + arrival_time + "\n" + "priority :"
                + priority + "\n" + "Quantum :" + quantum + "\n" + "V1: " +v1 + "\n"+ "ceil: " + ceil);
        System.out.println("-------------------------------------------------------------------------");
    }


    public static void main(String[] args){

        Scanner input = new Scanner(System.in);
        String name, color;
        int i, Quantum;
        int processesNumber = input.nextInt();
        int Burst_time, arrival_time, priority;
        Vector<AGAT_Schedule> processes = new Vector<AGAT_Schedule>(processesNumber);
        
        System.out.println("Enter in order: name - burst time - arrival time - priority - quantum - color");
        for(i=0;i<processesNumber;i++){

            name = input.next();
            Burst_time = input.nextInt();
            arrival_time = input.nextInt();
            priority = input.nextInt();
            Quantum = input.nextInt();
            color = input.next();
            processes.add(new AGAT_Schedule(name,Burst_time, arrival_time, priority, Quantum, color));

        }

        
        
        processes.get(0).sort(processes,processesNumber);

        GUI(processesNumber, processes);
        


        for(int q=0;q<processesNumber;q++){
            processes.get(q).index = q;
        }

        double Avg = 0.0,V1 = processes.get(processesNumber-1).arrival_time;
        if(V1 > 10){
            V1 /= 10;
        }
        else {
            V1 = 1;
        }

        for(i=0;i<processesNumber;i++){

            processes.get(i).v1 = V1;
            processes.get(i).ceil = (int) Math.ceil(processes.get(i).arrival_time / V1);

        }

        Vector<AGAT_Schedule> queue = new Vector<AGAT_Schedule>();
        Vector<String> Processes_execution_order = new Vector<String>();
        Vector<Integer> Waiting_Time = new Vector<Integer>();
        Vector<Integer> Turnaround_Time = new Vector<Integer>();
        Vector<Integer> Service_Time = new Vector<Integer>();
        Vector<Integer> quantum = new Vector<Integer>();

        double V2 = 0;

        for(i=0;i < processesNumber;i++){
            Waiting_Time.add(0);
            Turnaround_Time.add(0);
            Service_Time.add(0);
            quantum.add(processes.get(i).quantum);
            if(V2 < processes.get(i).burst_time)
                V2 = processes.get(i).burst_time;
        }

        if(V2 > 10)
            V2 /= 10.0;
        else
            V2 = 1.0;

        int Time = processes.get(0).arrival_time;
        int index = 1;
        int j;
        int k;
        int pointer = 0;
        queue.add(processes.get(0));

        int agat_factor = 10000;
        AGAT_Schedule replace;
        AGAT_Schedule another;
        int temp_quantum = 0;


        while (queue.size() > 0 || index < processesNumber){
        	int pastTime = Time;

            if(queue.size() == 0){
                Time = Time + (processes.get(index).arrival_time - Time);
                queue.add(processes.get(index));
                index = index + 1;
            }
            if(queue.get(0).Continue){

                System.out.println(queue.get(0).process + " running:");
                System.out.print("Quantums = ("+quantum.get(0));
                for(int h=1;h<processesNumber;h++){
                    System.out.print(", "+quantum.get(h));
                }
                System.out.println(")\n");

                temp_quantum = queue.get(0).quantum;
                temp_quantum = temp_quantum - queue.get(0).round(0.4 * (double)queue.get(0).quantum);
                pastTime = Time;
                Time = Time + queue.get(0).round(0.4 * (double)queue.get(0).quantum);
                
                for (int l = pastTime+1; l < Time+1; l++) {
    				try {
    					Thread.sleep(50);
    				} catch (InterruptedException e) {
    					
    				}
    				color(queue.get(0).index, l, queue.get(0).color);
    			}
                
                
                
                Service_Time.set(queue.get(0).index,Service_Time.get(queue.get(0).index) + queue.get(0).round(0.4 * (double)queue.get(0).quantum));

                if(index < processes.size() && Time >= processes.get(index).arrival_time){
                    queue.add(processes.get(index));
                    index = index + 1;
                }


                for (j = 0; j < queue.size(); j++) {

                    if (V2 < queue.get(j).burst_time) {
                        V2 = queue.get(j).burst_time;
                    }
                }


                if (V2 > 10)
                    V2 /= 10.0;
                else
                    V2 = 1.0;

                Processes_execution_order.add(queue.get(0).process);

                if(processes.get(0).burst_time <= 0){
                    System.out.print("AGATs = (__");
                }
                else {
                    System.out.print("AGATs = (" + processes.get(0).AGAT_factor(V2));
                }
                for(int g=1;g<processesNumber;g++){
                    if(processes.get(g).burst_time <= 0){
                        System.out.print(", __");
                    }
                    else {
                        System.out.print(", " + processes.get(g).AGAT_factor(V2));
                    }
                }
                System.out.println(")\n");

                if(queue.get(0).burst_time - queue.get(0).round(0.4 * (double)queue.get(0).quantum) <= 0){
                    Time = Time - Math.abs(queue.get(0).burst_time - queue.get(0).round(0.4 * (double)queue.get(0).quantum));
                    queue.get(0).burst_time = 0;
                    queue.get(0).quantum = 0;
                    quantum.set(queue.get(0).index,0);
                    Turnaround_Time.set(queue.get(0).index, Time - queue.get(0).arrival_time);
                    queue.remove(queue.get(0));

                    continue;
                }
            }

            else{
                temp_quantum = temp_quantum - 1;
                pastTime = Time;
                Time = Time + 1;
                for (int l = pastTime+1; l < Time+1; l++) {
    				try {
    					Thread.sleep(50);
    				} catch (InterruptedException e) {
    					
    				}
    				color(queue.get(0).index, l, queue.get(0).color);
    			}
                
                Service_Time.set(queue.get(0).index,Service_Time.get(queue.get(0).index) + 1);

                if(index < processes.size() && Time >= processes.get(index).arrival_time){
                    queue.add(processes.get(index));
                    index = index + 1;
                }

                if(queue.get(0).burst_time - (queue.get(0).quantum - temp_quantum) <= 0){
                    Time = Time - Math.abs(queue.get(0).burst_time - (queue.get(0).quantum - temp_quantum));
                    queue.get(0).burst_time = 0;
                    queue.get(0).quantum = 0;
                    quantum.set(queue.get(0).index,0);
                    Turnaround_Time.set(queue.get(0).index, Time - queue.get(0).arrival_time);
                    queue.remove(queue.get(0));

                    continue;
                }
            }

            if(temp_quantum == 0 && queue.get(0).burst_time > 0){
                queue.get(0).Continue = true;
                queue.get(0).burst_time = queue.get(0).burst_time - queue.get(0).quantum;
                queue.get(0).quantum = queue.get(0).quantum + 2;
                quantum.set(queue.get(0).index, queue.get(0).quantum);
                processes.get(queue.get(0).index).burst_time = queue.get(0).burst_time;

                replace = queue.get(0);
                queue.remove(queue.get(0));
                queue.add(replace);
            }

            else if(temp_quantum > 0 && queue.get(0).burst_time > 0){

                for(k=0;k < queue.size();k++){
                    if(agat_factor > queue.get(k).AGAT_factor(V2)){
                        agat_factor = queue.get(k).AGAT_factor(V2);
                        pointer = k;
                    }
                }
                agat_factor = 10000;
                if(pointer == 0){
                    queue.get(0).Continue = false;
                }
                else{
                    queue.get(0).Continue = true;
                    queue.get(0).burst_time = queue.get(0).burst_time - (queue.get(0).quantum - temp_quantum);
                    queue.get(0).quantum = queue.get(0).quantum + temp_quantum;
                    quantum.set(queue.get(0).index, queue.get(0).quantum);
                    processes.get(queue.get(0).index).burst_time = queue.get(0).burst_time;
                    replace = queue.get(0);
                    another = queue.get(pointer);
                    queue.remove(queue.get(pointer));
                    queue.remove(queue.get(0));
                    queue.insertElementAt(another,0);
                    queue.add(replace);
                }
            }
            
            
            
            
            
        }

        System.out.println("\nProcesses execution order: ");

        for(i=0;i<Processes_execution_order.size();i++){
            System.out.print(Processes_execution_order.get(i)+" ");
        }
        System.out.println("\n");
        System.out.println("Turnaround Time for each Process: ");

        for(i=0;i<processesNumber;i++){
            Waiting_Time.set(i,Turnaround_Time.get(i) - Service_Time.get(i));
            System.out.println(processes.get(i).process+"'s Turnaround Time: "+Turnaround_Time.get(i));
        }

        System.out.println("\nWaiting Time for each Process: ");

        for(i=0;i<processesNumber;i++){
            System.out.println(processes.get(i).process+"'s Waiting Time: "+Waiting_Time.get(i));
            Avg = Avg + Waiting_Time.get(i);
        }

        System.out.println("\nAverage Waiting Time: " + Avg / (double) processesNumber);

        Avg = 0.0;

        for(i=0;i<processesNumber;i++){
            Avg = Avg + Turnaround_Time.get(i);
        }

        System.out.println("\nAverage Turnaround Time: " + Avg / (double) processesNumber);
        
    }
}

/*

6
p1 20 3 1 4 red
p2 45 29 6 3 blue
p3 11 1 10 1 orange
p4 3 11 3 3 yellow
p5 40 1 10 1 pink
p6 23 40 2 4 gray

 */




