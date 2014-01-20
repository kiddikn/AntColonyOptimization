import java.io.*;
import java.util.ArrayList;


public class Analyse {
	public static int[] distanceData;
	private double average = 0;
	private int max = 0,min,length,valueData = 0;
	private double SD = 0;
	/**
	 * コンストラクタ
	 * @param data
	 * @param len
	 */
	Analyse(int[] data,int len){
		distanceData = data;
		this.length = len;
	}
	/**
	 * ArrayList型のデータ配列を受け取りAnalyse型に変換
	 * @param data
	 */
	Analyse(ArrayList<Integer> data){
		length = data.size();
		distanceData = new int[length];
		for(int i = 0;i < length;i++){
			distanceData[i] = data.get(i);
		}
	}
	
	public static void exportGraph(String fileName){
		try{
			//FileWriterクラスのインスタンス作成
			PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter(fileName+".csv")));
			//ファイルに書き込み
			for(int i = 0;i < distanceData.length;i++){
				fw.print(i+","+distanceData[i]);                                                                                                                                
				fw.println();
			}
			System.out.println("ファイルに書き込みました");
			//ファイルをクローズ
			fw.close();
		}catch(IOException e){
			System.out.println(e + "例外が発生しました");
		}

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
		System.out.println("Min:"+min+",Max:"+max+",average:"+average+",SD:"+SD);
	}
	
	public int getMin(){
		return min;
	}
}
