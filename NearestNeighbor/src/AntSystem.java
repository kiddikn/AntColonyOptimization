import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntSystem extends NearestNeighbor{
	public final int Lnn,antSize,alpha,beta,seed; //antSizeが問題サイズ
	private double[][] pheromone;
	public final double ro;
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
		min = Lnn;
		alpha = al;
		beta = be;
		this.ro = ro;
		this.seed = seed;
		rnd.setSeed(seed);
		pheromone = new double[antSize][antSize];
		double init = antSize / Lnn;
		for(int i = 0;i < antSize;i++){
			for(int j = 0;j < antSize;j++){
				pheromone[i][j] = 0;
				if(i < j){
					pheromone[i][j] = init;//上三角行列
				}
			}
		AntList.add(new Ant(i,antSize));
		}
	}
	
	/*List<City> cityList = new ArrayList<City>();//変更しないもとの都市情報
	List<City> solution = new ArrayList<City>();*/
	int distance;
	
	/**
	 * 全ての蟻の巡回路長を更新する
	 * 最小値も保存しておく
	 */
	public void renewAntRoot(){
		for(Ant ant : AntList){
			serchRoot();
			int distance = getDistance();
			ant.copyAntRoot(solution, distance);
			if(min > distance){
				min = distance;
			}
		}
	}
	
	/**
	 * 蟻一匹に対して巡回路を作成する
	 * 今までの巡回路をclearしてやり直す
	 */
	private void serchRoot(){
		cityList.clear();
		cityList.addAll(candidate);
		solution.clear();
		solution.add(cityList.get(0));
		cityList.remove(0);
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
	 * 返り値はcandidateの何番目かを指定
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
			double eta = Math.pow(1/Calculation.Euc2d(solutionLastCity, c),beta);
			total += tau*eta;
		}
		double target = total * rnd.nextDouble(),darts = 0;
		int nextCity = 0,count = 0;
		for(City c : cityList){	//未訪問都市のループ
			int j = c.getNumber();
			double tau = Math.pow(pheromone[i][j],alpha);
			double eta = Math.pow(1/Calculation.Euc2d(solutionLastCity,c),beta);
			darts += tau*eta;
			if(target < darts){
				nextCity = count;
				break;
			}
			count++;
		}
		return nextCity;
	}
	
	public void renewPheromone(){
		for(Ant ant : AntList){
			double[][] antPheromone = ant.getPheromone();
			for(int i = 0;i < antSize;i++){
				for(int j = i + 1;j < antSize;j++){
					pheromone[i][j] += antPheromone[i][j];
				}
			}
		}
		for(int i = 0;i < antSize;i++){
			for(int j = i + 1;j < antSize;j++){
				pheromone[i][j] *= 1 - ro;
			}
		}
	}
	
	/**
	 * 現時点の巡回路solutionの全長を返す
	 * @return distance
	 */
	private int getDistance(){
		distance = 0;
		for(int i = 0;i < antSize;i++){
			distance += Calculation.Euc2d(solution.get(i%antSize), solution.get((i+1)%antSize));
		}
		return distance;
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
		String StringInput = "../JikkenTsp/prob/eil51.tsp";
		int beta = 2;//beta = 2,3,4,5
		double ro = 0.5;
		AntSystem ant = new AntSystem(1,beta,ro,141,StringInput);
		int count = 0,beforeLength = ant.getAntSize();
		int minDistance = beforeLength;
		while(count < 1000){
			ant.renewAntRoot();
			ant.renewPheromone();
			minDistance = ant.getMinDistance();
			if(beforeLength <= minDistance){ //巡回路長の最小値が変化しない
				count++;
			}else{
				count = 0;
				beforeLength = minDistance;
			}
		}
		System.out.println(minDistance);
	}

}
