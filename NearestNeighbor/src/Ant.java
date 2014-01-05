import java.util.ArrayList;
import java.util.List;


public class Ant {
	List<City> solution = new ArrayList<City>();
	int antNumber,size,lengthOfRoot;
	double[][] pheroMatrix;
	
	Ant(int num,int len){
		antNumber = num;
		size = len;
		resetMatrix();
	}
	
	/**
	 * 更新用のフェロモンマトリックスを初期化
	 */
	private void resetMatrix(){
		pheroMatrix = new double[size][size];
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				pheroMatrix[i][j] = 0;
			}
		}
	}

	/**
	 * 蟻kの通った巡回路にある辺に加算するようの行列
	 * @return pheromonMatrix
	 */
	public double[][] getPheromone(){
		double delta = 1 / lengthOfRoot;
		for(int i = 0;i < size;i++){
			int first = i,next = (i + 1)%size;
			//getCity_number
			City c1 = solution.get(first);
			City c2 = solution.get(next);
			//i < jにする
			int num1 = c1.getNumber(),num2 = c2.getNumber();
			if(num1 > num2){
				int tmp = num1;
				num1 = num2;
				num2 = tmp;
			}
			pheroMatrix[num1][num2] = delta;
		}
		return pheroMatrix;
	}
	
	/**
	 * 蟻の通った巡回路とその長さを保存
	 * @param solution
	 * @param length
	 */
	public void copyAntRoot(List<City> solutionData,int length){
		solution.clear();
		/*for(City c : solutionData){
			City c2 = new City(c.getNumber(),c.getValueX(),c.getValueY());
			solution.add(c2);
		}*/
		for(City c : solutionData){
			solution.add(c);
		}
		lengthOfRoot = length;
	}

}
