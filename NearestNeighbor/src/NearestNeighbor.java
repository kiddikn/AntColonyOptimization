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
		//2-optの終了条件のために作成
		return maxDiff;
	}
	/**
	 * 例題ファイルから都市情報を取る
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
			  //(num x y)を分割
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
	 * NearestNeighbor法を適応
	 * @return　最短距離を返す
	 */
	public int tsp(){
		solution.add(cityList.get(firstCity));
		cityList.remove(firstCity);
		int solutionNum = 0;
		distance = 0;
		while(!cityList.isEmpty()){
			int min=1000,d,nextCity=0,candidate=0;
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
		//一周させるために最初の都市と最後の都市の距離を加算する
		distance += Calculation.Euc2d
				(solution.get(0),solution.get(length-1));
		return distance;
	}
		
	/**
	 * 局所探索2-optのImprove(S)。Best-Improvementを採用。
	 * @return 変更後のdistance
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
		swap(solution,mod(baseCity+1,length),mod(changeCity,length));
		return distance - maxDiff;
	}

	/**
	 * 経路が変化したかどうかの判断を経路差maxDiffにより判定
	 * maxDiffが変化していなかったら(0なら)終了
	 * opt_2を繰り返し実験
	 * @return minDistance。経路が変化しなくなるまで探索した結果
	 */
	public int DoOpt_2(){
		int minDistance = opt_2();
		while(maxDiff > 0){
			int nextDist = opt_2();
			if(nextDist < minDistance)
				minDistance = nextDist;
		}
		return minDistance;
	}
	
	/**
	 * arrayListの指定の要素を入れ替えるメソッド。2-optで使用
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
		return a % n;
	}
	
	public static void main(String[] args){
		String StringInput = "../JikkenTsp/prob/eil51.tsp";
		NearestNeighbor nn = new NearestNeighbor(StringInput,1);
		int n = nn.getLength();
		//距離を記憶しておく配列
		int[] analyseData = new int[n];
		int[] analyseDataOpt = new int[n];
		NearestNeighbor[] sol = new NearestNeighbor[n];
		//出発都市を0...nとしてそれぞれを出発点としたときの距離を求める
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
