package schedule;

import java.util.*;

public class TimeTableScheduling {

	//tuple { CourseId , FacultyId , LH , day , time }

	// 3d vector to maintain time table
	//public static Vector<Vector<Vector<Tuple>>> tt = new Vector<Vector<Vector<Tuple>>>(GlobalVar.DAYS);
	public static Tuple [][][] t;
	public static Vector<LectureHall> lh ;
	public static Map<String,Faculty>fac;
	public static Vector<Course>crs;


	public  void schedule() {
		t = new Tuple[5][10][13];


		//test cases
		// String []cid = {"CSE1-101","CSE2-108","CSE3-104","CSE4-131","ECE1-104","PHY1-105","MTH2-202"};
		// String []fid = {"CSE-101","CSE-108","CSE-108","CSE-131","ECE-104","PHY-105","MTH-202"};
		// String []name= {"CP","IDBMS","CN","AI","BE","P1","M2"};
		// String []fname= {"Mukesh_J","Rajbir_K","Rajbir_K","Khitiz_V","Abhishek_S","Amit_N","AjitPatel"};
		//

		// int []lid = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		// int []lstr= {250,250,150,150,150,150,150,150,150,150,150,250,250};

		//
		// lh = new Vector<LectureHall>(50);
		// for(int i=0;i<lid.length;i++)
		// {
		// 	lh.add(new LectureHall(lid[i],lstr[i]));
		// }
		//
		// crs= new Vector<Course>(50);
		// for(int i=0;i<cid.length;i++)
		// {
		// 	crs.add(new Course(cid[i],name[i],fid[i],lstr[i]));
		// }
		//
		// fac = new HashMap<String,Faculty>(50);
		// for(int i=0;i<fid.length;i++)
		// {
		// 	fac.put(fid[i],new Faculty(fname[i], fid[i]));
		// }



		LectureHall l = new LectureHall();
		Vector<LectureHall> lh = l.getLH();

		Course c = new Course();
		Vector<Course> crs = c.getCourses();

		Faculty f = new Faculty();
		Map<String,Faculty> fac = f.getFaculty();

		// getting morning batches and evening batches
		Vector<Course> mrgCrs =  new Vector<Course>(50);
		Vector<Course> evgCrs = new Vector<Course>(50);

		for(Course cr : crs) {
			String s = cr.getCrsId();
			//if course if from cse 'C' and mme , math 'M'  and physics 'P'
			if(s.charAt(0) == 'C'|| s.charAt(0)=='M' ) {
				// 1st year and 3rd year clubbed together in morning batch
				if(s.charAt(3)=='1'||s.charAt(3)=='3') {
					mrgCrs.add((Course)cr);

				}
				else{  //goes to evening batch  ECE
					evgCrs.add(cr);
				}
			}
			else {	//for course ECE
				// 2st year and 4rd year clubbed together in morning batch
				if(s.charAt(3)=='2'||s.charAt(3)=='4') {
					mrgCrs.add((Course)cr);

				}
				else{  //goes to evening batch
					evgCrs.add(cr);
				}
			}
		}


		//filing time table for morning batch
		int i=0,j=0,flag=1; // flag is for alternate allocation of courses on Monday and Tuesday
		for(Course x: mrgCrs) {


			//flag=1 for Monday
			if(flag == 1) {
				LectureHall lechall = decideLH(0,i,lh,x.getCrsStrength());
				//EVERY ALLOTMENT OF LECTURE INCREMENT FACULTY 'S LECTURE PER DAY
				fac.get(x.getfId()).inrClassPerDay(0);
				t[0][i][lechall.getlhId()]= new Tuple(x,fac.get(x.getfId()),lechall.getlhId());
				i = (i+1)%(GlobalVar.EVEN/2);
				flag=0;
			}
			else {  //for Tuesday
				LectureHall lechall = decideLH(0,j,lh,x.getCrsStrength());
				//EVERY ALLOTMENT OF LECTURE INCREMENT FACULTY 'S LECTURE PER DAY
				fac.get(x.getfId()).inrClassPerDay(1);
				t[1][j][lechall.getlhId()]=new Tuple(x,fac.get(x.getfId()),lechall.getlhId());
				j = (j+1)%(GlobalVar.ODD/2);
				flag=1;
			}
		}

		//for evening batch

		//Reinitializing variables
		i=0;j=0;flag=1;
		for(Course x: evgCrs) {
		//flag=1 for Monday
			if(flag == 1) {
				LectureHall lechall = decideLH(0,i+GlobalVar.EVEN/2,lh,x.getCrsStrength());
				t[0][i+GlobalVar.EVEN/2][lechall.getlhId()]=new Tuple(x,fac.get(x.getfId()),lechall.getlhId());
				i = (i+1)%(GlobalVar.EVEN/2);
				flag=0;
			}
			else {  //for Tuesday
				LectureHall lechall = decideLH(0,j+GlobalVar.ODD/2,lh,x.getCrsStrength());
				t[1][j+GlobalVar.ODD/2][lechall.getlhId()]= new Tuple(x,fac.get(x.getfId()),lechall.getlhId());
				j = (j+1)%(GlobalVar.ODD/2);
				flag=1;
			}
		}

		// now copy the same Monday's and Tuesday's time table in other days
		generateFulltimetable();

	}

	public LectureHall decideLH(int day , int time ,Vector<LectureHall>l ,int cstrength) {
		int min = Integer.MAX_VALUE;
		LectureHall LH = new LectureHall();
		for(LectureHall lh:l) {
			if(lh.getCapacity()>=cstrength && min<lh.getCapacity() && lh.isAlloc(day,time)) {
				min = lh.getCapacity();
				LH = lh;
			}
		}
		//allocating lecture hall for given day and time
		LH.allocateLH(day,time);
		return LH;
	}

	public void generateFulltimetable() {
		for(int d =2;d< GlobalVar.DAYS;d++) {
			if(d%2==0){
				for(int time=0;time<GlobalVar.EVEN;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++)
					t[d][time][o] = t[0][time][o];  //follows the Monday's time table
				}
			}
			else{
				for(int time=0;time<GlobalVar.ODD;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++)
					t[d][time][o] = t[1][time][o];  //follows the Tuesday's time table
				}
			}
		}
	}

	//giving user interface the tuple array of modified / custom time table
	public Tuple[] viewAsFaculty(String id) {
		Tuple[]tt = new Tuple[1000];
		int i=0;
		for(int d=0;d<GlobalVar.DAYS;d++)
		{

			if(d%2==0){
				for(int time=0;time<GlobalVar.EVEN;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if( t[d][time][o].fac.getFacId() == id ) {
							tt[i++]=t[d][time][o];
						}
						else
							tt[i++]=null;
					}
				}
			}
			else{
				for(int time=0;time<GlobalVar.ODD;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if( t[d][time][o].fac.getFacId() == id ) {
							tt[i++]=t[d][time][o];
						}
						else
							tt[i++]=null;
					}
				}
			}
		}
		return tt;
	}

	public Tuple[] viewAsStudent(String branch , String year) {
		// this clubbed id is to be checked with the course id
		String id =branch+year;
		Tuple[]tt = new Tuple[1000];
		int i=0;
		for(int d=0;d<GlobalVar.DAYS;d++)
		{

			if(d%2==0){
				for(int time=0;time<GlobalVar.EVEN;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if( t[d][time][o].crs.getCrsId().substring(0, 4) == id ) {
							tt[i++]=t[d][time][o];
						}
						else
							tt[i++]=null;
					}
				}
			}
			else{
				for(int time=0;time<GlobalVar.ODD;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if( t[d][time][o].crs.getCrsId().substring(0, 4) == id ) {
							tt[i++]=t[d][time][o];
						}
						else
							tt[i++]=null;

					}
				}
			}
		}
		return tt;
	}

	public Tuple[] viewTimeTable() {
		Tuple[]tt = new Tuple[1000];
		int i=0;
		for(int d=0;d<GlobalVar.DAYS;d++)
		{

			if(d%2==0){
				for(int time=0;time<GlobalVar.EVEN;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if( t[d][time][o].crs.getCrsId().substring(0, 4) == null ) {
							tt[i++]=null;
						}
						else
							tt[i++]=t[d][time][o];
					}
				}
			}
			else{
				for(int time=0;time<GlobalVar.ODD;time++) {
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if( t[d][time][o].crs.getCrsId().substring(0, 4) == null ) {
							tt[i++]=null;
						}
						else
							tt[i++]=t[d][time][o];
					}
				}
			}
		}
		return tt;
	}



	//displaying custom timetables
	public void displayTimeTable() {
		System.out.println("-----------WHOLE TIME TABLE------");
		System.out.println();
		for(int d=0;d<GlobalVar.DAYS;d++)
		{
			System.out.println();
			System.out.println();

			System.out.println(GlobalVar.DayName[d]);
			System.out.println("-------------------------");

			if(d%2==0){
				for(int time=0;time<GlobalVar.EVEN;time++) {
					System.out.println();
					System.out.print(GlobalVar.EVEN_DAY_TIMING[time]+":   ");
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if(t[d][time][o]!=null)
							System.out.print("   {"+t[d][time][o].crs.getCrsName() +" ,"+ t[d][time][o].fac.getFacName() + ", LH-" + t[d][time][o].lhid+"}");
						//else
							//System.out.print("   0LH-"+o);
					}
				}
			}
			else{
				for(int time=0;time<GlobalVar.ODD;time++) {
					System.out.println();
					System.out.print(GlobalVar.ODD_DAY_TIMING[time]+":   ");
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if(t[d][time][o]!=null)
							System.out.print("   {"+t[d][time][o].crs.getCrsName() +" ,"+ t[d][time][o].fac.getFacName() + ",  LH-" + t[d][time][o].lhid+"}");
						//else
							//System.out.print("   0LH-"+o);
					}
				}
			}
		}
	}

	public void displayAsStudent(char year) {

		if(year == '1'||year =='2'||year =='3'||year =='4')
		{


		System.out.println("-----------Student TIME TABLE----------");
		System.out.println();

		for(int d=0;d<GlobalVar.DAYS;d++)
		{
			System.out.println();
			System.out.println();

			System.out.println(GlobalVar.DayName[d]);
			System.out.println("-------------------------");

			if(d%2==0){
				for(int time=0;time<GlobalVar.EVEN;time++) {
					System.out.println();
					System.out.print(GlobalVar.EVEN_DAY_TIMING[time]+":   ");
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if(t[d][time][o]!=null && t[d][time][o].crs.getCrsId().charAt(3) == year)
							System.out.print("   {"+t[d][time][o].crs.getCrsName() +" ,"+ t[d][time][o].fac.getFacName() + ", LH-" + t[d][time][o].lhid+"}");
						//else
							//System.out.print("   0LH-"+o);
					}
				}
			}
			else{
				for(int time=0;time<GlobalVar.ODD;time++) {
					System.out.println();
					System.out.print(GlobalVar.ODD_DAY_TIMING[time]+":   ");
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if(t[d][time][o]!=null && t[d][time][o].crs.getCrsId().charAt(3) == year)
							System.out.print("   {"+t[d][time][o].crs.getCrsName() +" ,"+ t[d][time][o].fac.getFacName() + ",  LH-" + t[d][time][o].lhid+"}");
						//else
							//System.out.print("   0LH-"+o);
					}
				}
			}
		}
		}
		else
		{
			System.out.println("invalid year");
			return;
		}
	}

	public void displayAsFaculty(String facid) {
		//check if valid faculty id
		if(!fac.containsKey(facid)) {
			System.out.println("not a valid faculty id ");
			return ;
		}
		System.out.println("\n----------- TIME TABLE for Prof. "+fac.get(facid).getFacName()+"------");
		System.out.println();


		for(int d=0;d<GlobalVar.DAYS;d++)
		{
			System.out.println();
			System.out.println();

			System.out.println(GlobalVar.DayName[d]);
			System.out.println("-------------------------");

			if(d%2==0){
				for(int time=0;time<GlobalVar.EVEN;time++) {
					System.out.println();
					System.out.print(GlobalVar.EVEN_DAY_TIMING[time]+":   ");
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if(t[d][time][o]!=null && t[d][time][o].fac.getFacId() == facid )
							System.out.print("   {"+t[d][time][o].crs.getCrsName() +" ,"+ t[d][time][o].fac.getFacName() + ", LH-" + t[d][time][o].lhid+"}");
						//else
							//System.out.print("   0LH-"+o);
					}
				}
			}
			else{
				for(int time=0;time<GlobalVar.ODD;time++) {
					System.out.println();
					System.out.print(GlobalVar.ODD_DAY_TIMING[time]+":   ");
					for(int o=0;o<GlobalVar.NUMBER_LH;o++) {
						if(t[d][time][o]!=null && t[d][time][o].fac.getFacId() == facid)
							System.out.print("   {"+t[d][time][o].crs.getCrsName() +" ,"+ t[d][time][o].fac.getFacName() + ",  LH-" + t[d][time][o].lhid+"}");
						//else
							//System.out.print("   0LH-"+o);
					}
				}
			}
		}
	}
}
