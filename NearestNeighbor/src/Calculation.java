
public class Calculation {
	/**
	 * ssa,bÌ[Nbh£ðÔ·
	 * @param City a
	 * @param City b
	 * @return
	 */
	public static int Euc2d(City a,City b){
		double x1 = a.getValueX(),x2 = b.getValueX();
		double y1 = a.getValueY(),y2 = b.getValueY();
		//lÌÜüµ½£ðÔ·
		int d = (int)(Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))+0.5);
		return d;
	}
}
