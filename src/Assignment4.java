import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;


public class Assignment4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph gr = new Graph();
		try {
			Scanner sc = new Scanner(new File(args[0]));
			while(sc.hasNextLine())
			{
				String line = sc.nextLine();
				line = line.replace(":", ",");
				line = line.replace(">", ",");
				String[] line1 = line.split(",");
				
				ArrayList<Intersection> inList= new ArrayList<Intersection>();
				for(int i =0; i<line1.length;i++)
				{
					Intersection is = new Intersection(line1[i]);
					inList.add(is);
					gr.addIntersection2(is);
				}
				for(int i =1; i<inList.size();i++)
				{
					if(i==inList.size()-1)
					{
						Rail rl1 = gr.findRailS(inList.get(0).getLabe(), inList.get(i).getLabe());
						rl1.setSwtc(true);
						
					}
					gr.addRail(gr.getInterNode(inList.get(0)), gr.getInterNode(inList.get(i)),1);
					
				}
			}
			sc.close();
			
			Scanner sc2 = new Scanner(new File(args[1]));
			while(sc2.hasNextLine())
			{
				String line = sc2.nextLine();
				line = line.replace("-", " ");
				String[] line1 = line.split(" ");
				
				Rail rl = gr.findRailS(line1[0], line1[1]);
				Rail rl1 = gr.findRailS(line1[1], line1[0]);
				rl.setWeight(Double.parseDouble(line1[2]));	
				rl1.setWeight(Double.parseDouble(line1[2]));
				
			}
			sc2.close();		
			
			Dijsktra dij  = new Dijsktra(gr);
						
			HashSet<Intersection> nodes1= (HashSet<Intersection>) gr.getInter();
			ArrayList<Intersection> nodes = new ArrayList<Intersection>(nodes1);
			
			HashSet<Rail> edges1 = (HashSet<Rail>) gr.getRails();
			ArrayList<Rail> edges = new ArrayList<Rail>(edges1);
			
			Scanner sc3 = new Scanner(new File(args[2]));
			while(sc3.hasNextLine())
			{
				String line = sc3.nextLine();
				String[] line3 = line.split(" ");
				line = line.replace(">", " ");
				line = line.replace(":", " ");
				line=line.replace("-", " ");
				line = line.replace(",", " ");
				String line1[]= line.split(" ");
				if(line1[0].equals("ROUTE"))
				{
					System.out.println("COMMAND IN PROCESS >> ROUTE "+line1[1]+">"+line1[2]+" "+line1[3]);
					
					dij.execute(gr.getIntersection(line1[1]), Double.parseDouble(line1[3]), Double.parseDouble(args[3]));
					LinkedList<Intersection> path = dij.getPath(gr.getIntersection(line1[2]));
					if(path==null)
					{
						System.out.println("	No route from "+line1[1]+" to "+line1[2]+" found currently!");
						continue;
					}
					dij.totalTime(gr.getIntersection(line1[2]), Double.parseDouble(line1[3]));
					
					//System.out.print("	Route from "+line1[1]+">"+line1[2]+": ");
					
					gr=dij.switchControl(gr, path);
					System.out.print("	Route from "+line1[1]+" to "+line1[2]+": ");
					dij.PrintPath(gr,path);
					System.out.println("	Command "+"\""+line1[0]+" "+line1[1]+">"+line1[2]+" "+line1[3]+"\" has been executed successfully!");
					
				}
				else if(line1[0].equals("BREAK"))
				{
					System.out.println("COMMAND IN PROCESS >> BREAK "+line1[1]+">"+line1[2]);
					gr.breakRail(line1[1], line1[2]);
					System.out.println("	Command "+"\""+line1[0]+"\""+" "+line1[1]+">"+line1[2]+" has been executed successfully!");
					
				}
				else if(line1[0].equals("REPAIR"))
				{
					System.out.println("COMMAND IN PROCESS >> REPAIR "+line1[1]+">"+line1[2]);
					gr.repair(line1[1], line1[2]);
					System.out.println("\tCommand \"RAPIR\" "+line1[1]+">"+line1[2]+" has been executed successfully!");
				}
				else if(line1[0].equals("MAINTAIN"))
				{
					System.out.println("COMMAND IN PROCESS >> MAINTAIN "+line1[1]);
					gr.maintain(line1[1]);
					System.out.println("	Command \"MAINTAIN "+line1[1]+"\" has been executed successfully!");
				}
				else if(line1[0].equals("SERVICE"))
				{
					System.out.println("COMMAND IN PROCESS >> SERVICE "+line1[1]);
					gr.service(line1[1]);
					System.out.println("\tCommand \"SERVICE "+line1[1]+"\" has been executed successfully!");
				}
				else if(line1[0].equals("ADD"))
				{
					System.out.println("COMMAND IN PROCESS >> ADD "+line1[1]);
					gr.addInterCommand(line1[1]);
					System.out.println("\tCommand \"ADD "+line1[1]+"\" has been executed successfully!");
				}
				else if(line1[0].equals("LINK"))
				{
					HashMap<String,Double> links = new HashMap<String,Double>();
					System.out.println("COMMAND IN PROCESS >> LINK "+line3[1]);
					
					//System.out.print("\t");
					for(int i=2;i<line1.length-1;i+=2)
					{
						//System.out.print(line1[i]+" "+line1[i+1]+" ");//
						links.put(line1[i], Double.parseDouble(line1[i+1]));
					}
					gr.Link(line1[1], links, line1[line1.length-1]);
					dij = new Dijsktra(gr);
					//System.out.println();
					System.out.println("\tCommand \"LINK "+line3[1]+"\"  has been executed successfully!");
				}
				else if(line1[0].equals("LISTROUTESFROM"))
				{
					System.out.println("COMMAND IN PROCESS >> LISTROUTESFROM "+line1[1]);
					gr.ListRoute(line1[1]);
					System.out.println("	Command \"LISTROUTESFROM\" "+line1[1]+"  has been executed successfully!");
				}
				else if(line1[0].equals("LISTMAINTAINS"))
				{
					System.out.println("COMMAND IN PROCESS >> LISTMAINTAINS");
					gr.ListMaintance();
					System.out.println("	Command \"LISTMAINTAINS\" has been executed successfully!");
				}
				else if(line1[0].equals("LISTACTIVERAILS"))
				{
					System.out.println("COMMAND IN PROCESS >> LISTACTIVERAILS");
					gr.ListActiveRails();
					System.out.println("	Command \"LISTACTIVERAILS\"  has been executed successfully!");
				}
				else if(line1[0].equals("LISTBROKENRAILS"))
				{
					System.out.println("COMMAND IN PROCESS >> LISTBROKENRAILS");
					gr.ListBrokenRails();
					System.out.println("\tCommand \"LISTBROKENRAILS\"  has been executed successfully!");
				}
				else if(line1[0].equals("LISTCROSSTIMES"))
				{
					System.out.println("COMMAND IN PROCESS >> LISTCROSSTIMES");
					gr.ListCrossTime();
					System.out.println("\tCommand \"LISTCROSSTIMES\"  has been executed successfully!");
				}
				else if(line1[0].equals("TOTALNUMBEROFJUNCTIONS"))
				{
					System.out.println("COMMAND IN PROCESS >> TOTALNUMBEROFJUNCTIONS");
					System.out.println("\tTotal # of junctions: "+gr.getInter().size());
					System.out.println("\tCommand \"TOTALNUMBEROFJUNCTIONS\"  has been executed successfully!");
				}
				else if(line1[0].equals("TOTALNUMBEROFRAILS"))
				{
					System.out.println("COMMAND IN PROCESS >> TOTALNUMBEROFRAILS");
					System.out.println("\tTotal # of rails: "+gr.getRails().size());
					System.out.println("\tCommand \"TOTALNUMBEROFRAILS\"  has been executed successfully!");
				}
				else
				{
					System.out.println("COMMAND IN PROCESS >> "+line1[0]);
					System.out.println("\tUnrecognized command "+"\""+line1[0]+"\"!");
				}
			}
			sc3.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
