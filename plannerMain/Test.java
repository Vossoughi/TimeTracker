package plannerMain;

public class Test {

	public static void main(String[] args) {
		CustomDate a = new CustomDate(0);
		CustomDate b = new CustomDate(86400001 * 2);
		System.out.println(CustomDate.dayDifference(a, b));
	}
}
