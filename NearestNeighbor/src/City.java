
public class City {
	private int number;
	private double x,y;
	/**
	 * コンストラクタ
	 * @param number
	 * @param x
	 * @param y
	 */
	City(int number,double x,double y){
		this.number = number;
		this.x = x;
		this.y = y;
	}
	/**
	 * フィールド値のゲッター
	 * @return
	 */
	public double getValueX(){
		return this.x;
	}
	public double getValueY(){
		return this.y;
	}
	public int getNumber(){
		return this.number;
	}
}
