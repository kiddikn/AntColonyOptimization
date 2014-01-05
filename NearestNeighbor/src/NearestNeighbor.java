import java.io.*;
import java.util.*;

public class NearestNeighbor {
	
	List<City> cityList = new ArrayList<City>();
	List<City> solution = new ArrayList<City>();
	private int firstCity; //1to...
	private int length = 0,distance = 0,maxDiff;
	
	public int getLength(){
		return length;
	}
	public int getMaxDiff(){
		//2-opt‚ÌI—¹ğŒ‚Ì‚½‚ß‚Éì¬
		return maxDiff;
	}
	/**
	 * —á‘èƒtƒ@ƒCƒ‹‚©‚ç“ssî•ñ‚ğæ‚é
	 * @param fileInput
	 * @param fCity
	 */
	NearestNeighbor(String fileInput,int fCity){
	try{
		  File file = new File(fileInput);
		  BufferedReader br = new BufferedReader(new FileReader(file));
		  String str = br.readLine();
		  while(!(str.equals("NODE_COORD_SECTION"))){
		    //System.out.println(str);
			if(str.contains("DIMENSION")){
				String[] str1Ary = str.split(" ");
				length = Integer.valueOf(str1Ary[2]).intValue();
			}
		    str = br.readLine();
		  }
		  //System.out.println(str);
		  str = br.readLine();
		  while(!(str.equals("EOF"))){
			  //(num x y)‚ğ•ªŠ„
			    String[] str1Ary = str.split("[\\s]+");
			    //System.out.println(str1Ary[0]+str1Ary[1]+str1Ary[2]);
			    cityList.add(new City(Double.valueOf(str1Ary[0]).intValue()-1,
			    		Double.valueOf(str1Ary[1]).intValue(),
			    		Double.valueOf(str1Ary[2]).intValue()));
			    str = br.readLine();
			  }
		  br.close();
		  this.firstCity = fCity-1;//0to..
		}catch(FileNotFoundException e){
		  System.out.println(e);
		}catch(IOException e){
		  System.out.println(e);
		}
	}
	
	/**
	 * NearestNeighbor–@‚ğ“K‰
	 * @return@Å’Z‹——£‚ğ•Ô‚·
	 */
	public int tsp(){
		solution.add(cityList.get(firstCity));
		cityList.remove(firstCity);
		int solutionNum = 0;
		distance = 0;
		while(!cityList.isEmpty()){
			//Å¬’l‚ğ‰ğ‚ÌÅ‰‚Ì“ss‚ÆŒó•â“ss‚ÌÅ‰‚Ì“ss‚Æ‚Ì‹——£‚Æ‚·‚é
			int min = Calculation.Euc2d
					(solution.get(solutionNum),cityList.get(0));
			int d,nextCity=0,candidate=0;
			for(City c : cityList){
				d = Calculation.Euc2d
						(solution.get(solutionNum),c);
				if(min > d){
					min = d;
					nextCity = candidate;
				}
				candidate++;
			}
			distance += min;
			solution.add(cityList.get(nextCity));
			cityList.remove(nextCity);
			solutionNum++;
		}
		//ˆêü‚³‚¹‚é‚½‚ß‚ÉÅ‰‚Ì“ss‚ÆÅŒã‚Ì“ss‚Ì‹——£‚ğ‰ÁZ‚·‚é
		distance += Calculation.Euc2d
				(solution.get(0),solution.get(length-1));
		return distance;
	}
		
	/**
	 * ‹ÇŠ’Tõ2-opt‚ÌImprove(S)BBest-Improvement‚ğÌ—pB
	 * @return •ÏXŒã‚Ìdistance
	 */
	public int opt_2(){
		int diff,baseCity = 0,changeCity = 0;
		maxDiff = 0;
		for(int i = 0;i < length - 1;i++){
			for(int j = i + 2;j <= i + length - 2;j++){
				diff = (Calculation.Euc2d(solution.get(mod(i,length)),solution.get(mod(i+1,length)))
					   +Calculation.Euc2d(solution.get(mod(j,length)),solution.get(mod(j+1,length)))
					  -(Calculation.Euc2d(solution.get(mod(i,length)),solution.get(mod(j,length)))
					   +Calculation.Euc2d(solution.get(mod(i+1,length)),solution.get(mod(j+1,length)))));
				if(diff > 0 && maxDiff < diff){
					maxDiff = diff;
					baseCity = i;
					changeCity = j;
				}
			}
		}
		//baseCity‚ÆchangeCity‚ÌŠÔ‚à•ÏX‚·‚é
		if(!(baseCity==changeCity)){
			reverseRangeOfList(solution,mod(baseCity+1,length),mod(changeCity,length));
			distance -=  maxDiff;
		}
		return distance;
	}

	/**
	 * Œo˜H‚ª•Ï‰»‚µ‚½‚©‚Ç‚¤‚©‚Ì”»’f‚ğŒo˜H·maxDiff‚É‚æ‚è”»’è
	 * maxDiff‚ª•Ï‰»‚µ‚Ä‚¢‚È‚©‚Á‚½‚ç(0‚È‚ç)I—¹
	 * opt_2‚ğŒJ‚è•Ô‚µÀŒ±
	 * @return minDistanceBŒo˜H‚ª•Ï‰»‚µ‚È‚­‚È‚é‚Ü‚Å’Tõ‚µ‚½Œ‹‰Ê
	 */
	public int DoOpt_2(){
		int minDistance = opt_2();
		while(maxDiff > 0)
			minDistance = opt_2();
		return minDistance;
	}
	
	/**
	 * ƒŠƒXƒg“à‚Ìw’è‚Ì”ÍˆÍ‚Ì—v‘f‚ğ‹t‡‚É‚·‚é
	 * from‚©‚çto‚Ü‚Å‚Ì—v‘f‚ğ‹t‡‚É
	 * @param list
	 * @param from
	 * @param to
	 */
	static <T> void reverseRangeOfList(List<T> list,int from,int to){
		if(from < to){
			int middle = (int) ((to-from)/2+0.5);
			for(int i = 0;i <= middle;i++)
				swap(list,from+i,to-i);
		}else if(to < from){
			reverseRangeOfList(list,to,from);
		}
	}
	
	/**
	 * arrayList‚Ìw’è‚Ì—v‘f‚ğ“ü‚ê‘Ö‚¦‚éƒƒ\ƒbƒhB2-opt‚Åg—p
	 * @param list
	 * @param index1
	 * @param index2
	 */
	static <T> void swap(List<T> list,int index1,int index2){
	      T tmp=list.get(index1);
	      list.set(index1,list.get(index2));
	      list.set(index2, tmp);
	  }
	
	static int mod(int a,int n){
		return (a % n);
	}
	
	public static void main(String[] args){
		String StringInput = "../JikkenTsp/prob/ch130.tsp";
		NearestNeighbor nn = new NearestNeighbor(StringInput,1);
		int n = nn.getLength();
		//‹——£‚ğ‹L‰¯‚µ‚Ä‚¨‚­”z—ñ
		int[] analyseData = new int[n];
		int[] analyseDataOpt = new int[n];
		NearestNeighbor[] sol = new NearestNeighbor[n];
		//o”­“ss‚ğ0...n‚Æ‚µ‚Ä‚»‚ê‚¼‚ê‚ğo”­“_‚Æ‚µ‚½‚Æ‚«‚Ì‹——£‚ğ‹‚ß‚é
		for(int i = 0;i < n;i++){
			sol[i] = new NearestNeighbor(StringInput,i+1);
			analyseData[i] = sol[i].tsp();
			analyseDataOpt[i] = sol[i].DoOpt_2();
		}
		Analyse ana = new Analyse(analyseData,n);
		ana.disp();
		Analyse anaOpt = new Analyse(analyseDataOpt,n);
		anaOpt.disp();
	}
}
