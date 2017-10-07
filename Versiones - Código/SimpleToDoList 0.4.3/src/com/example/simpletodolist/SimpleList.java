package com.example.simpletodolist;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.database.SQLitehelper;
import com.example.row.RowType;

public class SimpleList {

	Context context;

	private Element currentElement;
	ArrayList<Element> elements;
	ArrayList<String> numbers;

	SimpleList(Context context) {
		this.context = context;
		elements = generateElements();
		numbers = generateNumbers(elements);
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public ArrayList<String> getNumbers() {
		return numbers;
	}

	// Works
	public void createTest() {
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		// Inserts maindb -- example
		db.execSQL("INSERT INTO maindb VALUES (1,0,0,0,'Task 1',2,0)");
		db.execSQL("INSERT INTO maindb VALUES (2,1,1,0,'Grupo de 2',3,1)");
		db.execSQL("INSERT INTO maindb VALUES (3,0,0,0,'Task 2',3,0)");
		db.execSQL("INSERT INTO maindb VALUES (4,1,1,0,'Grupo de 1',4,2)");

		db.execSQL("INSERT INTO subdb VALUES (1,1,2,1,0,'SubTask de 2',1,0)");
		db.execSQL("INSERT INTO subdb VALUES (2,1,2,0,0,'SubTask 2 de 2',2,0)");
		db.execSQL("INSERT INTO subdb VALUES (3,2,2,1,0,'SubTask de 1',1,0)");

		db.close();

		elements = generateElements();
		numbers = generateNumbers(elements);
	}

	// Works
	public void deleteDB() {
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();
		db.execSQL("DELETE FROM maindb");
		db.execSQL("DELETE FROM subdb");
		db.close();
		elements = generateElements();
		numbers = generateNumbers(elements);
	}

	// Works
	public int test() {
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		Cursor m = db.rawQuery("SELECT id FROM maindb", null);
		int num = m.getCount();
		db.close();

		return num;
	}

	void deleteSubTaskInDB(Element SubTask) {
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		Cursor m = db.rawQuery(
				"SELECT id, position FROM subdb WHERE position > "
						+ SubTask.getPos()
						+ " AND idref = (SELECT idref FROM subdb WHERE id = "
						+ SubTask.getId() + ")", null);
		m.moveToFirst();
		int n;

		for (int i = 0; i < m.getCount(); i++) {
			n = m.getInt(1) + 1;
			db.execSQL("UPDATE subdb SET position = " + n + " WHERE id = "
					+ m.getInt(0));
			m.moveToNext();
		}

		db.execSQL("DELETE FROM subdb WHERE id = " + SubTask.getId());
		db.close();
	}

	void deleteTaskInDB(Element Task) {

		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		db.execSQL("DELETE FROM maindb WHERE id = " + Task.getId());

		Cursor m = db.rawQuery(
				"SELECT id, position FROM maindb WHERE position > "
						+ Task.getPos(), null);
		m.moveToFirst();
		int n;

		for (int i = 0; i < m.getCount(); i++) {
			n = m.getInt(1) + 1;
			db.execSQL("UPDATE maindb SET position = " + n + " WHERE id = "
					+ m.getInt(0));
			m.moveToNext();
		}

		db.close();
	}

	void deleteTask(Element Task) {

		// TODO
		int position = 0;
		for (int i = 0; i < elements.size(); ++i) {
			if (Task.getId() == elements.get(i).getId()
					&& Task.getType() == RowType.TASK_ROW.ordinal()) {
				position = Task.getPos();
				System.out.println(elements.size());
				elements.remove(i);
				System.out.println(elements.size());
				break;
			}
		}

		for (int i = position - 1; i < elements.size(); ++i) {
			elements.get(i).setPos(i + 1);
		}

		// reloadListAndNumbers();

		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		db.execSQL("DELETE FROM maindb WHERE id = " + Task.getId());

		Cursor m = db.rawQuery(
				"SELECT id, position FROM maindb WHERE position > " + position,
				null);

		for (int i = 0; i < m.getCount(); i++) {
			db.execSQL("UPDATE maindb SET position = " + (m.getInt(1) + 1)
					+ " WHERE id = " + m.getInt(0));
			m.moveToNext();
		}

		db.close();
	}

	// Works
	public void checkAll() {

		Element ele;
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getType() == RowType.TASK_ROW.ordinal()) {
				ele = elements.get(i);
				ele.setChecked(true);
				elements.set(i, ele);
			}

			if (elements.get(i).getType() == RowType.SUBTASK_ROW.ordinal()) {
				ele = elements.get(i);
				ele.setChecked(true);
				elements.set(i, ele);
			}
		}

		// reloadListAndNumbers();

		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();
		db.execSQL("UPDATE maindb SET checked = 1 WHERE type = "
				+ RowType.TASK_ROW.ordinal());
		db.execSQL("UPDATE subdb SET checked = 1");
		db.close();

	}

	// Works
	public void changeState(Element element) {
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		if (element.getType() == RowType.TASK_ROW.ordinal()) {
			for (int i = 0; i < elements.size(); i++) {
				if (element.getId() == elements.get(i).getId()
						&& elements.get(i).getType() == RowType.TASK_ROW
								.ordinal()) {
					Element ele = elements.get(i);
					ele.swapCheck();
					elements.set(i, ele);
				}
			}
			// reloadListAndNumbers();
			db.execSQL("UPDATE maindb SET checked = "
					+ (element.isChecked() ? 1 : 0) + ", priority = "
					+ element.getPriority() + ", task = '"
					+ element.getTask().replace("'", "''") + "' WHERE id = "
					+ element.getId());
		}
		if (element.getType() == RowType.GROUP_ROW.ordinal()) {
			for (int i = 0; i < elements.size(); i++) {
				if (element.getId() == elements.get(i).getId()
						&& elements.get(i).getType() == RowType.GROUP_ROW
								.ordinal()) {
					Element ele = elements.get(i);
					ele.swapCheck();
					elements.set(i, ele);
				}
			}
			// reloadList();
			db.execSQL("UPDATE maindb SET checked = "
					+ (element.isChecked() ? 1 : 0) + ", priority = "
					+ element.getPriority() + ", task = '"
					+ element.getTask().replace("'", "''") + "' WHERE id = "
					+ element.getId());
		}
		if (element.getType() == RowType.SUBTASK_ROW.ordinal()) {
			int father = 0;
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).getType() == RowType.GROUP_ROW.ordinal()) {
					father = elements.get(i).getChild();
				}
				if (element.getId() == father
						&& element.getId() == elements.get(i).getId()
						&& elements.get(i).getType() == RowType.SUBTASK_ROW
								.ordinal()) {
					Element ele = elements.get(i);
					ele.swapCheck();
					elements.set(i, ele);
				}
			}
			// reloadListAndNumbers();

			Cursor m = db
					.rawQuery(
							"SELECT subdb.id FROM subdb, maindb WHERE maindb.child = subdb.idref AND subdb.id = "
									+ element.getId(), null);
			m.moveToFirst();

			db.execSQL("UPDATE subdb SET checked = "
					+ (element.isChecked() ? 1 : 0) + " WHERE id = "
					+ m.getInt(0));

		}
		db.close();
		
		elements = generateElements();
		numbers = generateNumbers(elements);

	}

	public void deleteChecked() {
		// TODO
		ArrayList<Element> toDelete = new ArrayList<Element>();
		int n = 0;
		boolean check;
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getType() != RowType.GROUP_ROW.ordinal()
					&& elements.get(i).isChecked()) {
				toDelete.add(elements.get(i));
			}
			if (elements.get(i).getType() == RowType.GROUP_ROW.ordinal()) {
				n = i + 1;
				check = true;
				while (n < elements.size()
						&& elements.get(n).getType() == RowType.SUBTASK_ROW
								.ordinal()) {
					if (!elements.get(n).isChecked()) {
						check = false;
					}
					n++;
				}
				if (check)
					toDelete.add(elements.get(i));
			}
		}

		for (int i = 0; i < toDelete.size(); i++) {
			if (toDelete.get(i).getType() == RowType.TASK_ROW.ordinal()) {
				deleteTaskInDB(toDelete.get(i));
			}
			if (toDelete.get(i).getType() == RowType.GROUP_ROW.ordinal()) {
				deleteTaskInDB(toDelete.get(i));
			}
			if (toDelete.get(i).getType() == RowType.SUBTASK_ROW.ordinal()) {
				deleteSubTaskInDB(toDelete.get(i));
			}
		}

		elements = generateElements();
		numbers = generateNumbers(elements);
		// reloadFullList();
	}

	public void NewTask(Element ele) {
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		Cursor n = db.rawQuery("SELECT id FROM maindb ORDER BY id", null);
		Cursor m = db.rawQuery("SELECT position FROM maindb ORDER BY position",
				null);
		Element element = ele;

		if (n.getCount() == 0) {
			element.setId(1);
			element.setPos(1);
		} else {

			n.moveToLast();
			m.moveToLast();
			element.setId(n.getInt(0) + 1);
			element.setPos(m.getInt(0) + 1);
		}

		String test = "INSERT INTO maindb VALUES (" + element.getId() + " , "
				+ element.getType() + " , " + (element.isChecked() ? 1 : 0)
				+ " , " + element.getPriority() + " , '" + element.getTask()
				+ "', " + element.getPos() + " , 0)";
		db.execSQL(test);

		// reloadFullList();
		elements = generateElements();
		numbers = generateNumbers(elements);

		String priotity = "";
		if (ele.getPriority() == 2) {
			priotity = "high";
		}
		if (ele.getPriority() == 1) {
			priotity = "medium";
		}
		if (ele.getPriority() == 0) {
			priotity = "low";
		}

		Toast.makeText(
				context,
				"Created task: " + ele.getTask() + ", with " + priotity
						+ " priority", Toast.LENGTH_SHORT).show();

		db.close();

	}

	public void NewGroup(Element ele) {
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		Cursor n = db.rawQuery("SELECT id FROM maindb ORDER BY id", null);
		Cursor m = db.rawQuery("SELECT position FROM maindb ORDER BY position",
				null);
		Element element = ele;

		if (n.getCount() == 0) {
			element.setId(1);
			element.setPos(1);
			element.setChild(1);
		} else {
			int max = 0;
			for (int i = 0; i < elements.size(); i++) {
				max = (elements.get(i).getChild() > max) ? elements.get(i)
						.getChild() : max;
			}
			n.moveToLast();
			m.moveToLast();
			element.setId(n.getInt(0) + 1);
			element.setPos(m.getInt(0) + 1);
			element.setChild(max + 1);
		}

		String test = "INSERT INTO maindb VALUES (" + element.getId() + " , "
				+ element.getType() + " , " + (element.isChecked() ? 1 : 0)
				+ " , " + element.getPriority() + " , '" + element.getTask()
				+ "', " + element.getPos() + " , " + element.getChild() + ")";
		db.execSQL(test);

		// reloadFullList();
		elements = generateElements();
		numbers = generateNumbers(elements);

		String priotity = "";
		if (ele.getPriority() == 2) {
			priotity = "high";
		}
		if (ele.getPriority() == 1) {
			priotity = "medium";
		}
		if (ele.getPriority() == 0) {
			priotity = "low";
		}

		Toast.makeText(
				context,
				"Created group: " + ele.getTask() + ", with " + priotity
						+ " priority", Toast.LENGTH_SHORT).show();

		db.close();
	}
	
	public void NewSubTask(Element ele){
		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		if (ele.getTask() != "") {
			Cursor n = db.rawQuery("SELECT id FROM subdb ORDER BY id", null);
			Cursor m = db.rawQuery("SELECT position FROM subdb WHERE "
					+ currentElement.getChild() + " = idref ORDER BY position",
					null);

			Element element = ele;

			if (n.getCount() == 0) {
				element.setId(1);
				element.setPos(1);
			} else {
				n.moveToLast();
				element.setId(n.getInt(0) + 1);

				if (m.getCount() == 0) {
					element.setPos(1);
				} else {
					m.moveToLast();
					element.setPos(m.getInt(0) + 1);
				}
			}
			String query = "INSERT INTO subdb VALUES(" + element.getId() + ", "
					+ currentElement.getChild() + ", " + element.getType()
					+ ", " + (element.isChecked() ? 1 : 0) + ", "
					+ element.getPriority() + ", '" + element.getTask() + "', "
					+ element.getPos() + ", " + element.getChild() + ")";
			db.execSQL(query);
			// reloadFullList();
			elements = generateElements();
			numbers = generateNumbers(elements);

		} else {
			Toast.makeText(context, "The task can't be empty",
					Toast.LENGTH_SHORT).show();
		}

		db.close();
	}
	
	public void setCurrentEle(Element ele){
		currentElement = ele;
	}
	
	private ArrayList<String> generateNumbers(ArrayList<Element> elements) {
		ArrayList<String> num = new ArrayList<String>();
		int i = 0;
		int check = 0;
		int task = 0;
		int total = 0;
		int global = 0;
		boolean exit;

		while (i < elements.size()) {
			if (elements.get(i).getType() == RowType.GROUP_ROW.ordinal()) {
				global++;
				task = check = 0;
				i++;
				exit = true;
				do {
					if (i < elements.size()) {
						if (elements.get(i).getType() == RowType.SUBTASK_ROW
								.ordinal()) {
							task++;
							if (elements.get(i).isChecked() == true)
								check++;
							i++;
						} else {
							num.add("(" + check + "/" + task + ")");
							if (check == task)
								total++;
							exit = false;
						}
					} else {
						num.add("(" + check + "/" + task + ")");
						if (check == task)
							total++;
						exit = false;
					}
				} while (exit);

			} else {
				global++;
				if (elements.get(i).isChecked())
					total++;
				i++;
			}
		}
		// Toast.makeText(getApplicationContext(), global,
		// Toast.LENGTH_SHORT).show();

		num.add("(" + total + "/" + global + ")");
		return num;
	}

	private ArrayList<Element> generateElements() {
		ArrayList<Element> items = new ArrayList<Element>();

		SQLitehelper usdbh = new SQLitehelper(context);
		SQLiteDatabase db = usdbh.getWritableDatabase();

		Cursor n = db.rawQuery("SELECT * FROM maindb ORDER BY position", null);
		n.moveToFirst();

		for (int i = 0; i < n.getCount(); i++) {
			// If it is a Task, just add it
			if (n.getInt(1) == RowType.TASK_ROW.ordinal()) {
				items.add(new Element(n.getInt(0), RowType.TASK_ROW.ordinal(),
						n.getInt(2), n.getInt(3), n.getString(4), n.getInt(5),
						n.getInt(6)));
			}

			// If it is a Group, add it and his child
			if (n.getInt(1) == RowType.GROUP_ROW.ordinal()) {
				items.add(new Element(n.getInt(0), RowType.GROUP_ROW.ordinal(),
						n.getInt(2), n.getInt(3), n.getString(4), n.getInt(5),
						n.getInt(6)));
				Cursor m = db.rawQuery(
						"SELECT * FROM subdb WHERE idref = " + n.getInt(6)
								+ " ORDER BY position", null);
				m.moveToFirst();
				for (int x = 0; x < m.getCount(); x++) {
					items.add(new Element(m.getInt(0), RowType.SUBTASK_ROW
							.ordinal(), m.getInt(3), m.getInt(4), m
							.getString(5), m.getInt(6), m.getInt(7)));
					m.moveToNext();
				}

			}
			n.moveToNext();
		}
		db.close();
		return items;
	}

}
