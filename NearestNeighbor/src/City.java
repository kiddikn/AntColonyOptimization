
public class City {
	private int number;
	private double x,y;
	/**
	 * �R���X�g���N�^
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
	 * �t�B�[���h�l�̃Q�b�^�[
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
