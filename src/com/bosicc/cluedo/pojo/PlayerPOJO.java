package com.bosicc.cluedo.pojo;

import java.io.Serializable;

import com.bosicc.cluedo.R;


public class PlayerPOJO implements Serializable{

	private static final long serialVersionUID = 3768556506242861054L;
	
	private int mPlayernum;
	private String mCardName;
	private String mName;
	private String mLabel;
	private int mResColor;
	private boolean inGame;
	
	public PlayerPOJO(){
		mPlayernum=-1;
		mCardName="";
		mName="";
		mLabel="";
		mResColor = R.color.bgMain; //TODO: Check about color may be need to get Color from res
		inGame = true;
	}
	
	public PlayerPOJO(String CartdName, int color){
		mPlayernum=-1;
		mCardName=CartdName;
		mName="";
		mLabel=CartdName;
		mResColor = color;
		inGame = true;
	}
	
	
	public String getLabel(){
		return mLabel;
	}
	
	public void setLabel(String name, String cardname){
		if (name.equals("")){
			mLabel = cardname;
		}else{
			mLabel = name + " (" + cardname + " )";
		}
	}
	
	// ===== Number ---------
	public void setNumber(int people_num){
		mPlayernum = people_num;
	}
	
	public int getNumber(){
		return mPlayernum;
	}
	
	// ===== Name ---------
	public void setName(String text){
		this.mName = text;
	}
	
	public String getName(){
		return this.mName;
	}
	
	// ===== CardName ---------
	public void setCardName(String text){
		this.mCardName = text;
	}
	
	public String getCardName(){
		return this.mCardName;
	}
	
	// ===== Color ---------
	public void setColor(int color){
		mResColor = color;
	}
	
	public int getColor(){
		return mResColor;
	}
	
	// ===== Color ---------
	public void inGame(boolean in){
		inGame = in;
	}
	
	public boolean inGame(){
		return inGame;
	}
	
	public void reset(){
		mPlayernum=-1;
		mCardName="";
		mName="";
		mLabel="";
		mResColor = R.color.bgMain;
		inGame = true;
	}
}
