package sjutil;

/** 
 * long currentTimeMillis = System.currentTimeMillis();<br>
 * <br>
 * MillsTranslator millsTranslator = new MillsTranslator(long currentTimeMillis, int gmt);<br>
 * <br>
 *  년/월/일 출력<br>
	public String getDate()<br>
	<br>
	시:분:초 출력<br>
	public String getTime()<br>
	<br>
	요일 출력<br>
	public String getDayOfWeek()<br>
	<br>
	주차 출력<br>
	public String getWeekLine()<br>
	
 */

public class MillsTranslator {
	private long current = 0;
	
	private int[] normalYear = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; // 평년 월별 일수
	private int[] leapYear = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; // 윤년 월별 일수
	private String[] dayOfWeek = {"목요일", "금요일", "토요일", "일요일", "월요일", "화요일", "수요일"};
			
	private int year = 1970; // 1970년부터 시간의 흐름에 따라 1년씩 증가
	private int month = 0; // 월
	private int day = 0; // 일
	private int yy = 0; // 평년이면 0, 윤년이면 1
	
	private int hour = 0; // 시
	private int minute = 0; // 분
	private int second = 0; // 초
	
	// 생성자
	// 1. 밀리초 -> 초로 변환
	// 2. GMT+i 를 위해 (60*60*i)초 추가
	public MillsTranslator(long currentTimeMillis, int gmt) {
		
		long current = currentTimeMillis/1000+(60*60*gmt);
		
		long currentDays = current/60/60/24; // 1970년 1월 1일부터 현재까지 흐른 일수. 연/월/일 계산에서 사용
		
		int year = 1970; // 1970년부터 시간의 흐름에 따라 1년씩 증가
		int month = 0; // 월
		int day = 0; // 일
		int yy = 0; // 평년이면 0, 윤년이면 1
		
		Outter: while(true) { // 1970년 이후로 평년인지 윤년인지 구분하면서 currentDay에서 월 단위로 배열에 저장된 일수를 빼서 현 시점까지 오는 계산
			if((year%4)==0 && (year%100)!=0 || (year%400)==0) { // 윤년에 해당하는 경우
				for(int i=0; i<leapYear.length; i++) {
					currentDays -= leapYear[i]; // currentDay에서 366일 빼줌
					if(currentDays < 0) {
						month = i+1;
						day = (int) currentDays + leapYear[i] + 1;
						yy = 1; // 윤년
						break Outter;
					} else if(currentDays == 0) { // 다음달로 넘어간 경우
						month = i+2;
						if(month == 13) {
							month = 1;
						}
						day = 1;
						yy = 1; // 윤년
						break Outter;
					} else {
						continue;
					}
				}
				year++; 
			} else { // 평년에 해당하는 경우
				for(int i=0; i<normalYear.length; i++) {
					currentDays -= normalYear[i]; // currentDay에서 365일 빼줌
					if(currentDays < 0) {
						month = i+1;
						day = (int) currentDays + normalYear[i] + 1;
						break Outter;
					} else if(currentDays == 0) { // 다음달로 넘어간 경우
						month = i+2;
						if(month == 13) {
							month = 1;
						}
						day = 1;
						break Outter;
					} else {
						continue;
					}
				}
				year++; 
			}
		}
		
		int hour = (int) current/60/60%24;
		int minute = (int) current/60%60;
		int second = (int) current%60;
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.yy = yy;
		
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		
		this.current = current;
		
	}
	
	// 년/월/일 출력
	public String getDate() {
		return String.format("%04d/%02d/%02d", year, month, day);
	}
		
	// 시:분:초 출력
	public String getTime() { 
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
	
	// 요일 출력
	public String getDayOfWeek() {
		long currentDays = current/60/60/24; // 1970년 1월 1일부터 현재까지 흐른 일수
		int dw = 0; // 요일 계산을 위한 인덱스 값
		String week = null; // 요일
		
		dw = (int) (currentDays % 7); // 요일 계산을 위한 인덱스 값
		week = dayOfWeek[dw]; // 배열을 이용하여 요일 계산
		
		return String.format("%s", week);
	}
	
	// 주차 출력
	public String getWeekLine() {
		String weekLine = null;
		long currentDays = current/60/60/24; // 1970년 1월 1일부터 현재까지 흐른 일수
		int lastOfMonth = 0; // 월의 마지막일
		int firstDayOfMonth = 0; // 당월 1일에 해당하는 요일(인덱스 값) 
		
		// 월의 마지막일 계산
		if(yy == 0) { // 평년
			lastOfMonth = normalYear[month-1];		
		} else { // 윤년
			lastOfMonth = leapYear[month-1];
		}			
			
		firstDayOfMonth = (int) (currentDays - day + 1) % 7; // 월의 첫날 요일 계산
		
		switch(firstDayOfMonth) {
			case 1: // 월의 첫날이 금요일
				if(day <= 3) {
					if((month-1) == 0) {
						weekLine = String.format("%02d월의 마지막주", 12);
					} else {
						weekLine = String.format("%02d월의 마지막주", month-1);
					}
				} else if(4<=day && day<=10){
					weekLine = String.format("%02d월의 1째주", month);
				} else if(11<=day && day<=17){
					weekLine = String.format("%02d월의 2째주", month);
				} else if(18<=day && day<=24){
					weekLine = String.format("%02d월의 3째주", month);
				} else {
					weekLine = String.format("%02d월의 4째주", month);
				}
				break;
				
			case 2: // 월의 첫날이 토요일
				if(day <= 2) {
					if((month-1) == 0) {
						weekLine = String.format("%02d월의 마지막주", 12);
					} else {
						weekLine = String.format("%02d월의 마지막주", month-1);
					}
				} else if(3<=day && day<=9){
					weekLine = String.format("%02d월의 1째주", month);
				} else if(10<=day && day<=16){
					weekLine = String.format("%02d월의 2째주", month);
				} else if(17<=day && day<=23){
					weekLine = String.format("%02d월의 3째주", month);
				} else if(24<=day && day<=30){
					weekLine = String.format("%02d월의 4째주", month);
				} else { // 31일이 있는 경우
					if((month+1) == 13) {
					weekLine = String.format("%02d월의 1째주", 1);
					} else {
						weekLine = String.format("%02d월의 1째주", month+1);
					}
				}
				break;
			case 3: // 월의 첫날이 일요일
				if(day <= 1) {
					if((month-1) == 0) {
						weekLine = String.format("%02d월의 마지막주", 12);
					} else {
						weekLine = String.format("%02d월의 마지막주", month-1);
					}
				} else if(2<=day && day<=8){
					weekLine = String.format("%02d월의 1째주", month);
				} else if(9<=day && day<=15){
					weekLine = String.format("%02d월의 2째주", month);
				} else if(16<=day && day<=22){
					weekLine = String.format("%02d월의 3째주", month);
				} else if(23<=day && day<=29){
					weekLine = String.format("%02d월의 4째주", month);
				} else { // 30, 31일이 있는 경우
					if((month+1) == 13) {
						weekLine = String.format("%02d월의 1째주", 1);
					} else {
						weekLine = String.format("%02d월의 1째주", month+1);
					}
				}
				break;
			
			case 4: // 월의 첫날이 월요일
				if(1<=day && day<=7) {
					weekLine = String.format("%02d월의 1째주", month);
				} else if(8<=day && day<=14){
					weekLine = String.format("%02d월의 2째주", month);
				} else if(15<=day && day<=21){
					weekLine = String.format("%02d월의 3째주", month);
				} else if(22<=day && day<=28){
					weekLine = String.format("%02d월의 4째주", month);
				} else { // 29, 30, 31일이 있는 경우
					if((month+1) == 13) {
						weekLine = String.format("%02d월의 1째주", 1);
					} else {
						weekLine = String.format("%02d월의 1째주", month+1);
					}
				}
				break;
				
			case 5: // 월의 첫날이 화요일
				if(1<=day && day<=6) {
					weekLine = String.format("%02d월의 1째주", month);
				} else if(7<=day && day<=13){
					weekLine = String.format("%02d월의 2째주", month);
				} else if(14<=day && day<=20){
					weekLine = String.format("%02d월의 3째주", month);
				} else if(21<=day && day<=27){
					weekLine = String.format("%02d월의 4째주", month);
				} else if(28<=day && day<=30){
					if(lastOfMonth == 31) {
						weekLine = String.format("%02d월의 5째주", month);
					} else {
						if((month+1) == 13) {
							weekLine = String.format("%02d월의 1째주", 1);
						} else {
							weekLine = String.format("%02d월의 1째주", month+1);
						}
					}
				} else {
					weekLine = String.format("%02d월의 5째주", month);
				}
				break;
				
			case 6: // 월의 첫날이 수요일
				if(1<=day && day<=5) {
					weekLine = String.format("%02d월의 1째주", month);
				} else if(6<=day && day<=12){
					weekLine = String.format("%02d월의 2째주", month);
				} else if(13<=day && day<=19){
					weekLine = String.format("%02d월의 3째주", month);
				} else if(20<=day && day<=26){
					weekLine = String.format("%02d월의 4째주", month);
				} else if(27<=day && day<=29){
					if(30 <= lastOfMonth) {
						weekLine = String.format("%02d월의 5째주", month);
					} else {
						if((month+1) == 13) {
							weekLine = String.format("%02d월의 1째주", 1);
						} else {
							weekLine = String.format("%02d월의 1째주", month+1);
						}
					}
				} else {
					weekLine = String.format("%02d월의 5째주", month);
				}
				break;
				
			default: // 월의 첫날이 목요일
				if(1<=day && day<=4) {
					weekLine = String.format("%02d월의 1째주", month);
				} else if(5<=day && day<=11){
					weekLine = String.format("%02d월의 2째주", month);
				} else if(12<=day && day<=18){
					weekLine = String.format("%02d월의 3째주", month);
				} else if(19<=day && day<=25){
					weekLine = String.format("%02d월의 4째주", month);
				} else if(26<=day && day<=28){
					if(29 <= lastOfMonth) {
						weekLine = String.format("%02d월의 5째주", month);
					} else {
						if((month+1) == 13) {
							weekLine = String.format("%02d월의 1째주", 1);
						} else {
							weekLine = String.format("%02d월의 1째주", month+1);
						}
					}
				} else {
					weekLine = String.format("%02d월의 5째주", month);
				}
				break;
		}
		
		return String.format("%s", weekLine);
	}
	
//	public static void main(String[] args) {
//		long currentTimeMillis = System.currentTimeMillis();
//		
//		for(int i = 1; i < 366; i++) {
//			currentTimeMillis += (1000*60*60*24);
//			
//			MillsTranslator millsTranslator = new MillsTranslator(currentTimeMillis, 9);
//		
//			String date = millsTranslator.getDate();
//			String time = millsTranslator.getTime();
//			String dayOfWeek = millsTranslator.getDayOfWeek();
//			String weekLine = millsTranslator.getWeekLine();
//		
//			System.out.printf("%10s%12s%10s%4s\n", weekLine, date, time, dayOfWeek);
//		}
//		
//		System.out.println("END");
//	}
	
}
