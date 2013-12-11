
public class Analyse {

	private int[] distanceData;
	private double average = 0;
	private int max = 0,min,length,valueData = 0;
	private double SD = 0;
	/**
	 * �R���X�g���N�^
	 * @param data
	 * @param len
	 */
	Analyse(int[] data,int len){
		this.distanceData = data;
		this.length = len;
	}
	/**
	 * �ő�l�A�ŏ��l�A���ϒl�A�W���΍������߂ĕ\������
	 */
	public void disp(){
		int sum = 0;
		min = distanceData[0];//�����l�̐ݒ�
		for(int i = 0;i < length;i++){
			valueData = distanceData[i];
			sum += valueData;
			if(max < valueData)max = valueData;
			if(min > valueData)min = valueData;
		}
		average = sum/length;
		sum = 0;
		//�W���΍������߂�
		for(int i = 0;i < length;i++){
			sum += (distanceData[i]-average)*(distanceData[i]-average);
		}
		SD = Math.sqrt(sum/(length-1));
		System.out.println("Max:"+max+",Min:"+min+",average:"+average+",SD:"+SD);
	}
	
	
}
