package org.horn.mommons.dao.json;

public class Student{
	private int id;
	private String name;
	private String sex;
	private int age;
	private String[] hobby;
	
	public Student(int id,String name,String sex,int age,String[] hobbys){
		this.id=id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.hobby = hobbys;
	}
	
	public Student(){
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String[] getHobby() {
		return hobby;
	}
	public void setHobby(String[] hobby) {
		this.hobby = hobby;
	}
	
}