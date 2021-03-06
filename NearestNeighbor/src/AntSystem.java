import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntSystem extends NearestNeighbor{
	public final int Lnn,antSize,alpha,beta,seed; //antSizeが問題サイズ
	private double[][] pheromone; //フェロモン情報
	public final double ro;
	private int firstStep = 0;
	public int min;
	List<Ant> AntList = new ArrayList<Ant>();
	List<City> candidate = new ArrayList<City>();
	
	/**
	 * コンストラクタ
	 * @param al
	 * @param be
	 * @param ro
	 * @param n
	 * @param Lnn
	 */
	AntSystem(int al,int be,double ro,int seed,String s){
		super(s,1);
		antSize = super.getLength();
		for(City c : cityList){
			City c2 = new City(c.getNumber(),c.getValueX(),c.getValueY());
			candidate.add(c2);
		}
		Lnn = tsp();
		alpha = al;
		beta = be;
		this.ro = ro;
		this.seed = seed;
		rnd.setSeed(seed);
		pheromone = new double[antSize][antSize];
		double init = (double)antSize / (double)Lnn;
		for(int i = 0;i < antSize;i++){
			for(int j = 0;j < antSize;j++){
				pheromone[i][j] = init;
			}
		AntList.add(new Ant(i,antSize));
		}
	}
	
	/*List<City> cityList = new ArrayList<City>();//変更しないもとの都市情報
	List<City> solution = new ArrayList<City>();*/
	int distanceOfRoot;
	
	/**
	 * 全ての蟻の巡回路長を更新する
	 * 最小値も保存しておく
	 */
	public void renewAntRoot(){
		for(Ant ant : AntList){
			int first = ant.getAntNumber();
			searchRoot(first);
			getDistance();
			ant.copyAntRoot(solution, distanceOfRoot);
			if(firstStep == 0){
				min = distanceOfRoot;
				firstStep = 1;
			}
			if(min > distanceOfRoot){
				min = distanceOfRoot;
			}
		}
	}
	
	/**
	 * 蟻一匹に対して巡回路を作成する
	 * 今までの巡回路をclearしてやり直す
	 * 引数は初期都市の番号。蟻ごとに初期都市が異なる
	 * @param first
	 */
	private void searchRoot(int first){
		cityList.clear();
		cityList.addAll(candidate);
		solution.clear();
		solution.add(cityList.get(first));
		cityList.remove(first);
		for(int i = 0;i < antSize - 1;i++){
			//candidateの中の何番目か
			int nextCity = selectNextCity();
			solution.add(cityList.get(nextCity));
			cityList.remove(nextCity);
		}
	}
	
	Random rnd = new Random();
	/**
	 * 次に選択する都市を選択する
	 * 返り値はcityList(未訪問都市リスト)の何番目かを指定
	 * 乱数を利用する
	 * 次の都市はルーレット方式で確率を求める
	 * @return　nextCity
	 */
	private int selectNextCity(){
		City solutionLastCity = solution.get(solution.size() - 1);
		int i = solutionLastCity.getNumber();
		double total = 0;
		for(City c : cityList){	//未訪問都市のループ
			int j = c.getNumber();
			double tau = Math.pow(pheromone[i][j],alpha);
			double eta = Math.pow(1/(double)Calculation.Euc2d(solutionLastCity, c),beta);
			total += tau*eta;
		}
		double target = total * rnd.nextDouble(),darts = 0;
		int nextCity = 0;
		for(City c : cityList){	//未訪問都市のループ
			int j = c.getNumber();
			double tau = Math.pow(pheromone[i][j],alpha);
			double eta = Math.pow(1/(double)Calculation.Euc2d(solutionLastCity,c),beta);
			darts += tau*eta;
			if(target <= darts){
				break;
			}
			nextCity++;
		}
		return nextCity; //nextCityはcityListの何番目の都市を表す。(都市番号ではない)
	}
	
	/**
	 * フェロモン情報を更新する
	 */
	public void renewPheromone(){
		for(int i = 0;i < antSize;i++){
			for(int j = 0;j < antSize;j++){
				pheromone[i][j] *= 1 - ro;
			}
		}
		
		for(Ant ant : AntList){
			for(int i = 0;i < antSize;i++){
				int len = ant.getLength();
				double phero = 1/(double)len;
				int first = ant.solution.get(i).getNumber();
				int next = ant.solution.get((i+1)%antSize).getNumber();
				pheromone[first][next] += phero;
				pheromone[next][first] += phero;
			}
		}
		
	}
	
	/**
	 * 現時点の巡回路solutionの全長を返す
	 * @return distance
	 */
	private int getDistance(){
		distanceOfRoot = 0;
		for(int i = 0;i < antSize;i++){
			distanceOfRoot += Calculation.Euc2d(solution.get(i%antSize), solution.get((i+1)%antSize));
		}
		return distanceOfRoot;
	}
	
	/**
	 * すべての蟻が巡回路を作成した後の最小値を返す
	 * 終了条件チェック用のゲッター
	 * @return minDistance
	 */
	public int getMinDistance(){
		return min;
	}
	
	public int getAntSize(){
		return antSize;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String StringInput = "../JikkenTsp/prob/pr76.tsp";
		int beta = 3;//beta = 2,3,4,5
		double ro = 0.5;
		AntSystem[] ants = new AntSystem[10];
		int[] seeds = {113,127,131,139,151,157,163,251,257,271};
		//各乱数ごとに最小値を格納するリスト
		ArrayList<Integer> minCollectData = new ArrayList<Integer>();
		for(int i = 0;i < seeds.length;i++){//iはseeds
			ants[i] = new AntSystem(1,beta,ro,seeds[i],StringInput); //Antとcandidateを作成
			int count = 0,beforeLength = 110000000,isFirst = 1;
			int minDistance;
			//分析用にフェロモン更新時にそのときの最良解を格納
			ArrayList<Integer> analyseData = new ArrayList<Integer>();
			analyseData.clear();
			while(count < 1000){
				ants[i].renewAntRoot();
				ants[i].renewPheromone();
				minDistance = ants[i].getMinDistance();
				if(isFirst == 1){
					beforeLength = minDistance;
					isFirst = 0;
				}
				analyseData.add(minDistance);
				if(beforeLength <= minDistance){ //巡回路長の最小値が変化しない
					count++;
				}else{
					count = 0;
					beforeLength = minDistance;
				}
			}
			//各seedごとに分析結果を表示
			Analyse ana = new Analyse(analyseData);
			ana.disp();
			minCollectData.add(ana.getMin());
			//Analyse.exportGraph("ch130");
		}
		//seedを変えて10回実験を行ったあとの全体分析
		Analyse analyse = new Analyse(minCollectData);
		System.out.println("average and SD of 10times average:");
		analyse.disp();
	}

}
