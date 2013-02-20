package com.bosicc.cluedo.pojo;

import java.io.Serializable;

public class PMovePOJO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8507993276456290414L;
	
	private int mPlayerXodit = -1;
	private int mPlayerPodtverdil =-1;
	
	private int mSlyx[] = {-1, -1, -1};
	
	public PMovePOJO(){
	}
	
	public PMovePOJO(int xodit) {
		setPlayerXodit(xodit);
	}

	public void setPlayerXodit(int num){
		this.mPlayerXodit = num;
	}
	
	public int getPlayerXodit(){
		return this.mPlayerXodit;
	}
	
	public void setPlayerPodtverdil(int num){
		this.mPlayerPodtverdil = num;
	}
	
	public int getPlayerPodtverdil(){
		return this.mPlayerPodtverdil;
	}
	
	public void setSlyxPerson(int person){
		mSlyx[0] = person;
	}
	
	public int getSlyxPerson(){
		return mSlyx[0];
	}
	
	public void setSlyxPlace(int place){
		mSlyx[1] = place;
	}
	
	public int getSlyxPlace(){
		return mSlyx[1];
	}
	
	public void setSlyxWeapon(int weapon){
		mSlyx[2] = weapon;
	}
	
	public int getSlyxWeapon(){
		return mSlyx[2];
	}
	
	public void setSlyx(int person, int place, int weapon){
		mSlyx[0] = person;
		mSlyx[1] = place;
		mSlyx[2] = weapon;
	}
	
	public int[] getSlyx(){
		return this.mSlyx;
	}
	
}
