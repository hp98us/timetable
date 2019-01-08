package schedule;

import java.sql.SQLException;
import java.util.*;
import database.LhDB;

public class LectureHall {
	private int lhId;
	private int capacity;
	private boolean[][] assigned_Lh = new boolean [GlobalVar.DAYS][10];  	// array[day][timeSlot index] , 0: FREE , 1: OCCUPIED

	//Void Constructor
	public LectureHall() {

	}

	//main Constructor
	public LectureHall(int id,int capacity){
		this.lhId = id;
		this.capacity = capacity;
		this.intializeLh();
	}

	// Initialize all the LH with false entry
	public void intializeLh() {
		for(int i=0;i<GlobalVar.DAYS;i++) {
			//for odd days
			if(i%2==1) {
				this.assigned_Lh[i] = new boolean[GlobalVar.ODD];
			}
			//for even days
			else {
				this.assigned_Lh[i] = new boolean[GlobalVar.EVEN];
			}
		}
	}

	public Vector<LectureHall> getLH() throws SQLException {
		//connecting to database

		LhDB l = new LhDB();
		return l.getAllLH();

	}

	//Strength of the LH


	public int getlhId() {
		return this.lhId;
	}
	public void setLhId(int lhId) {
		this.lhId = lhId;
	}


	public int getCapacity() {
		return this.capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	//Assign lecture hall to a particular day at particular time
	public void allocateLH(int day,int timeIndex) {
		this.assigned_Lh[day][timeIndex] = true;
	}
	// check whether lh is assigned or not
	public boolean isAlloc(int day ,int time) {
		return this.assigned_Lh[day][time];
	}
	// Deallocating lecture hall
	public void DeallocateLh(int day , int timeIndex) {
		this.assigned_Lh[day][timeIndex] = false;
	}
}
