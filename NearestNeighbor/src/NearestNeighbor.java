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
		//2-opt�̏I�������̂��߂ɍ쐬
		return maxDiff;
	}
	/**
	 * ���t�@�C������s�s�������
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
			  //(num x y)�𕪊�
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
	 * NearestNeighbor�@��K��
	 * @return�@�ŒZ������Ԃ�
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
		//��������邽�߂ɍŏ��̓s�s�ƍŌ�̓s�s�̋��������Z����
		distance += Calculation.Euc2d
				(solution.get(0),solution.get(length-1));
		return distance;
	}
		
	/**
	 * �Ǐ��T��2-opt��Improve(S)�BBest-Improvement���̗p�B
	 * @return �ύX���distance
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
	 * �o�H���ω��������ǂ����̔��f���o�H��maxDiff�ɂ�蔻��
	 * maxDiff���ω����Ă��Ȃ�������(0�Ȃ�)�I��
	 * opt_2���J��Ԃ�����
	 * @return minDistance�B�o�H���ω����Ȃ��Ȃ�܂ŒT����������
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
	 * arrayList�̎w��̗v�f�����ւ��郁�\�b�h�B2-opt�Ŏg�p
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
		//�������L�����Ă����z��
		int[] analyseData = new int[n];
		int[] analyseDataOpt = new int[n];
		NearestNeighbor[] sol = new NearestNeighbor[n];
		//�o���s�s��0...n�Ƃ��Ă��ꂼ����o���_�Ƃ����Ƃ��̋��������߂�
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
