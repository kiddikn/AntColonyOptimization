import java.util.ArrayList;
import java.util.List;



public class Ant {
	List<City> solution = new ArrayList<City>();
	int antNumber,size,lengthOfRoot;
	
	public int getAntNumber(){
		return antNumber;
	}
	
	public int getLength(){
		return lengthOfRoot;
	}
	
	Ant(int num,int len){
		antNumber = num;
		size = len;
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
			City c2 = new City(c.getNumber(),c.getValueX(),c.getValueY());
			solution.add(c2);
		}
		lengthOfRoot = length;
	}

}
