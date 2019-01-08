package schedule;

public class Main {

	public static void  main(String[] args) {


		TimeTableScheduling timetable = new TimeTableScheduling();
		timetable.schedule();

		System.out.println();
		timetable.displayTimeTable();
		System.out.println();
		timetable.displayAsStudent('1');
		System.out.println();
		timetable.displayAsFaculty("CSE-108");
	}
}
