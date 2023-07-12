import sjutil.MillsTranslator;

public class MillisTranslatorTest {
	
	public static void main(String[] args) {
		long currentTimeMillis = System.currentTimeMillis();
		
		for(int i = 1; i < 366; i++) {
			currentTimeMillis += (1000*60*60*24);
			
			MillsTranslator millsTranslator = new MillsTranslator(currentTimeMillis, 9);
		
			String date = millsTranslator.getDate();
			String time = millsTranslator.getTime();
			String dayOfWeek = millsTranslator.getDayOfWeek();
			String weekLine = millsTranslator.getWeekLine();
		
			System.out.printf("%10s%12s%10s%4s\n", weekLine, date, time, dayOfWeek);
		}
		
		System.out.println("END");
	}
	
}
