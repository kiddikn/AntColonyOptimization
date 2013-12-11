
public class Analyse {

	private int[] distanceData;
	private double average = 0;
	private int max = 0,min,length,valueData = 0;
	private double SD = 0;
	/**
	 * コンストラクタ
	 * @param data
	 * @param len
	 */
	Analyse(int[] data,int len){
		this.distanceData = data;
		this.length = len;
	}
	/**
	 * 最大値、最小値、平均値、標準偏差を求めて表示する
	 */
	public void disp(){
		int sum = 0;
		min = distanceData[0];//初期値の設定
		for(int i = 0;i < length;i++){
			valueData = distanceData[i];
			sum += valueData;
			if(max < valueData)max = valueData;
			if(min > valueData)min = valueData;
		}
		average = sum/length;
		sum = 0;
		//標準偏差を求める
		for(int i = 0;i < length;i++){
			sum += (distanceData[i]-average)*(distanceData[i]-average);
		}
		SD = Math.sqrt(sum/(length-1));
		System.out.println("Max:"+max+",Min:"+min+",average:"+average+",SD:"+SD);
	}
	
	
}
