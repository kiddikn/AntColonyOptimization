
public class City {
	private int number;
	private double x,y;
	/**
	 * �R���X�g���N�^
	 * @param number
	 * @param x
	 * @param y
	 */
	City(int number,int x,int y){
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
	int getNumber(){
		return this.number;
	}
}
