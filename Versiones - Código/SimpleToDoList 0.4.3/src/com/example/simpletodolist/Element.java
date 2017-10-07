package com.example.simpletodolist;

public class Element {

	private int id;
	private int pos;
	private int type;
	private boolean checked;
	private int priority;
	private String task;
	private int child;

	public int getChild() {
		return child;
	}

	public void setChild(int child) {
		this.child = child;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Element(int id, int type, int checked, int priority, String task, int pos, int child) {
		this.child = child;
		this.pos = pos;
		this.id = id;
		this.type = type;
		this.priority = priority;
		this.task = task;
		if (checked == 1) {
			this.checked = true;
		} else {
			this.checked = false;
		}
	}
	
	
	public void swapCheck(){
		checked = (checked ? true : false);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

}