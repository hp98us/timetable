package schedule;
import database.CourseDB;

import java.sql.SQLException;
import java.util.*;

public class Course {
	//Faculty[] crsFaculty; //list of faculty teaching that course
	//Student[] crsStudent; //list of students enrolled in that course
	private String crsName;
	private String crsId;
	private String fId; //id of faculty teaching the course
	private int strength; // number of students enrolled in that course

	//	CONSTRUCTOR
	public Course() {

	}
	public Course(String crsId ,String name,String  facid,int strength){
		this.crsId=crsId;
		crsName = name;
		fId = facid;
		this.strength = strength;

	}



	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public void setCrsName(String crsName) {
		this.crsName = crsName;
	}

	public void setCrsId(String crsId) {
		this.crsId = crsId;
	}


	//	METHODS
	public Vector<Course> getCourses() throws SQLException{
		//connecting to database;
		CourseDB c = new CourseDB();
		return c.getALLCourse();

	}

	//get function for all the attributes of the class
	public String getCrsName() {
		return this.crsName;
	}

	public String getCrsId() {
		return this.crsId;
	}

	public String getfId() {
		return this.fId;
	}
	public void setfId(String f) {
		this.fId=f;
	}

	public int getCrsStrength() {
		return this.strength;
	}

	public void setCrsStrength(int int1) {
		// TODO Auto-generated method stub

	}
}
