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
	 * �X�V�p�̃t�F�������}�g���b�N�X��������
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
	 * �ak�̒ʂ�������H�ɂ���ӂɉ��Z����悤�̍s��
	 * @return pheromonMatrix
	 */
	public double[][] getPheromone(){
		double delta = 1 / lengthOfRoot;
		for(int i = 0;i < size;i++){
			int first = i,next = (i + 1)%size;
			//getCity_number
			City c1 = solution.get(first);
			City c2 = solution.get(next);
			//i < j�ɂ���
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
	 * �a�̒ʂ�������H�Ƃ��̒�����ۑ�
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
