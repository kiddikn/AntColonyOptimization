import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Hybrid extends NearestNeighbor{
	public final int antSize,alpha,beta,seed,citySize;
	public int bestLength,iterationBestLength;
	public final double ro,pBest;
	public double tauMax,tauMin;
	private double[][] pheromone; //フェロモン情報
	private int firstStep = 1,maxDiff;

	List<City> candidate = new ArrayList<City>();
	List<City> bestRoot = new ArrayList<City>();
	List<City> iterationBest = new ArrayList<City>();

	/**
	 * コンストラクタ
	 * すべての変数を設定し、フェロモン行列を初期化
	 * @param fileInputName
	 * @param al
	 * @param be
	 * @param ro
	 * @param pb
	 * @param seeds
	 * @param antSize
	 */
		Hybrid(String fileInputName,int al,int be,double ro,double pb,int seeds,int antSize) {
		super(fileInputName, 1);
		citySize = super.getLength();
		this.antSize = antSize;
		copyRoot(cityList,candidate);
		bestLength = tsp();
		alpha = al;
		beta = be;
		this.ro = ro;
		seed = seeds;
		pBest = pb;
		pheromone = new double[citySize][citySize];
		double init = 1 / (ro*(double)bestLength);
		for(int i = 0;i < citySize;i++){
			for(int j = 0;j < citySize;j++){
				pheromone[i][j] = init;
			}
		}
		renewTau();
	}

	/**
	 * tauを更新
	 * bestなルートが変更したら最大値と最小値を求める
	 */
	private void renewTau(){
		tauMax = 1/(ro*bestLength);
		double npBest = Math.pow(pBest, 1/citySize);
		double numerator = 1 - npBest;
		double denominator = (citySize/2 - 1) * npBest;
		tauMin = (numerator/ denominator)*tauMax;
	}

	//現時点での経路長
	private int distanceOfRoot;
	public void renewAntRoot(){
		for(int i = 0;i < antSize;i++){//蟻ループ
			searchRoot(i);//蟻ごとにスタート都市を変えsolutionに結果を入れる
			getDistance();//solutionの巡回路長をもとめる
			if(firstStep == 1){
				iterationBestLength = distanceOfRoot;
				copyRoot(solution,iterationBest);
				firstStep = 0;
			}
			if(iterationBestLength > distanceOfRoot){
				iterationBestLength = distanceOfRoot;
				copyRoot(solution,iterationBest);
			}
			if(bestLength > distanceOfRoot){
				bestLength = distanceOfRoot;
				copyRoot(solution,bestRoot);
			}
		}
		firstStep = 1;
	}

	private void searchRoot(int first){
		distanceOfRoot = 0;
		cityList.clear();
		cityList.addAll(candidate);
		solution.clear();
		solution.add(cityList.get(first));
		cityList.remove(first);
		for(int i = 0;i < citySize - 1;i++){
			//candidateの中の何番目かを取得
			int nextCity = selectNextCity();
			solution.add(cityList.get(nextCity));
			cityList.remove(nextCity);
		}
		DoOptim_2();
	}
	
	/**
	 * 局所探索2-optのImprove(S)。Best-Improvementを採用。
	 * @return 変更後のdistance
	 */
	public void optim_2(){
		int diff,baseCity = 0,changeCity = 0;
		maxDiff = 0;
		for(int i = 0;i < citySize - 1;i++){
			for(int j = i + 2;j <= i + citySize - 2;j++){
				diff = (Calculation.Euc2d(solution.get(mod(i,citySize)),solution.get(mod(i+1,citySize)))
					   +Calculation.Euc2d(solution.get(mod(j,citySize)),solution.get(mod(j+1,citySize)))
					  -(Calculation.Euc2d(solution.get(mod(i,citySize)),solution.get(mod(j,citySize)))
					   +Calculation.Euc2d(solution.get(mod(i+1,citySize)),solution.get(mod(j+1,citySize)))));
				if(diff > 0 && maxDiff < diff){
					maxDiff = diff;
					baseCity = i;
					changeCity = j;
				}
			}
		}
		//baseCityとchangeCityの間も変更する
		if(!(baseCity==changeCity)){
			reverseRangeOfList(solution,mod(baseCity+1,citySize),mod(changeCity,citySize));
		}
	}

	/**
	 * 経路が変化したかどうかの判断を経路差maxDiffにより判定
	 * maxDiffが変化していなかったら(0なら)終了
	 * opt_2を繰り返し実験
	 * @return minDistance。経路が変化しなくなるまで探索した結果
	 */
	public void DoOptim_2(){
		optim_2();
		while(maxDiff > 0){
			optim_2();
		}
	}
	
	Random rnd = new Random();
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

	private int getDistance(){
		distanceOfRoot = 0;
		for(int i = 0;i < citySize;i++){
			distanceOfRoot += Calculation.Euc2d(solution.get(i%citySize), solution.get((i+1)%citySize));
		}
		return distanceOfRoot;
	}

	private void copyRoot(List<City> from, List<City> to) {
		to.clear();
		for(City c : from){
			City c1 = new City(c.getNumber(),c.getValueX(),c.getValueY());
			to.add(c1);
		}
	}

	public void renewPheromone(){
		renewTau();
		for(int i = 0;i < citySize;i++){
			for(int j = 0;j < citySize;j++){
				pheromone[i][j] *= 1 - ro;
			}
		}

		for(int i = 0;i < citySize;i++){
			double phero = 1/(double)iterationBestLength;
			int first = iterationBest.get(i).getNumber();
			int next = iterationBest.get((i+1)%citySize).getNumber();
			pheromone[first][next] += phero;
			pheromone[next][first] += phero;
		}

		//checkPheromone
		for(int i = 0;i < citySize;i++){
			for(int j = 0;j < citySize;j++){
				if(pheromone[i][j] < tauMin){
					pheromone[i][j] = tauMin;
				}else if(pheromone[i][j] > tauMax){
					pheromone[i][j] = tauMax;
				}
			}
		}

	}

	public int getMinDistance(){
		return bestLength;
	}

	public static void main(String[] args) {
		String StringInput = "../JikkenTsp/prob/KroA100.tsp";
		int beta = 2;//beta = 2,3,4,5
		double ro = 0.02,pBest = 0.05;
		Hybrid[] ants = new Hybrid[10];
		int[] seeds = {113,127,131,139,151,157,163,251,257,271};
		//各乱数ごとに最小値を格納するリスト
		ArrayList<Integer> minCollectData = new ArrayList<Integer>();
		for(int i = 0;i < seeds.length;i++){//iはseeds
			//MaxMinAntSystem(String fileInputName,int al,int be,double ro,double pb,int seeds)
			ants[i] = new Hybrid(StringInput,1,beta,ro,pBest,seeds[i],25);
			int count = 0,beforeLength = 0,isFirst = 1;
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
			//Analyse.exportGraph("eil51");
		}
		//seedを変えて10回実験を行ったあとの全体分析
		Analyse analyse = new Analyse(minCollectData);
		System.out.println("average and SD of 10times average:");
		analyse.disp();
	}

}
