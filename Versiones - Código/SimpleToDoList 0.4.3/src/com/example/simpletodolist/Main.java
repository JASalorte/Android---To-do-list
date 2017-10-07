package com.example.simpletodolist;


import com.example.adapter.ListAdapter;
import com.example.dialog.GroupMenu;
import com.example.dialog.GroupMenu.GroupMenuListener;
import com.example.dialog.NewGroup;
import com.example.dialog.NewGroup.NewGroupListener;
import com.example.dialog.NewSubTask;
import com.example.dialog.NewSubTask.NewSubTaskListener;
import com.example.dialog.NewTask;
import com.example.dialog.NewTask.NewTaskListener;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends FragmentActivity implements OnClickListener,
		OnLongClickListener, NewTaskListener, NewGroupListener,
		GroupMenuListener, NewSubTaskListener {

	private ImageView new_element, new_group, check_all, remove, config, logo;
	private SimpleList MainList;
	private ListAdapter adapter;
	ListView list;
	TextView main_num;
	MediaPlayer mp;

	DialogFragment dialogGroupMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mp = MediaPlayer.create(this, R.raw.engineer);

		main_num = (TextView) findViewById(R.id.main_num);

		new_element = (ImageView) findViewById(R.id.new_element);
		new_group = (ImageView) findViewById(R.id.new_group);
		check_all = (ImageView) findViewById(R.id.check_all);
		remove = (ImageView) findViewById(R.id.remove);
		config = (ImageView) findViewById(R.id.config);
		logo = (ImageView) findViewById(R.id.logo);

		new_element.setOnClickListener(this);
		new_group.setOnClickListener(this);
		check_all.setOnClickListener(this);
		remove.setOnClickListener(this);
		config.setOnClickListener(this);
		logo.setOnLongClickListener(this);

		MainList = new SimpleList(this);
		//MainList.deleteDB();
		//MainList.createTest();

		reloadList();
	}

	public void changeState(Element element) {
		MainList.changeState(element);
		reloadList();
	}

	public void sendMSN(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}

	public void reloadList() {

		adapter = new ListAdapter(this, MainList.getElements(),	MainList.getNumbers());

		int index = (list == null) ? 0 : list.getFirstVisiblePosition();
		View v = (list == null) ? null : list.getChildAt(0);
		int top = (v == null) ? 0 : v.getTop();

		list = (ListView) findViewById(R.id.listviewShare);
		list.setAdapter(adapter);

		list.setSelectionFromTop(index, top);
		
		main_num.setText(MainList.getNumbers().get(MainList.getNumbers().size() - 1));
	}

	/*
	 * public void reloadListAndNumbers() { numbers = generateNumbers(elements);
	 * adapter = new ListAdapter(this, elements, numbers); int index =
	 * list.getFirstVisiblePosition(); View v = list.getChildAt(0); int top = (v
	 * == null) ? 0 : v.getTop();
	 * 
	 * list = (ListView) findViewById(R.id.listviewShare);
	 * list.setAdapter(adapter);
	 * 
	 * list.setSelectionFromTop(index, top);
	 * main_num.setText(numbers.get(numbers.size() - 1)); }
	 */

	/*
	 * private void reloadFullList() { elements = generateElements(); numbers =
	 * generateNumbers(elements); adapter = new ListAdapter(this, elements,
	 * numbers);
	 * 
	 * int index = (list == null) ? 0 : list.getFirstVisiblePosition(); View v =
	 * (list == null) ? null : list.getChildAt(0); int top = (v == null) ? 0 :
	 * v.getTop();
	 * 
	 * list = (ListView) findViewById(R.id.listviewShare);
	 * list.setAdapter(adapter);
	 * 
	 * list.setSelectionFromTop(index, top);
	 * 
	 * main_num.setText(numbers.get(numbers.size() - 1)); }
	 */

	@Override
	public void onDialogNewTask(Element ele) {
		MainList.NewTask(ele);
		reloadList();
	}

	@Override
	public void onDialogNewGroup(Element ele) {
		MainList.NewGroup(ele);
		reloadList();
	}

	public void LongClickGroupDialog(Element ele) {
		MainList.setCurrentEle(ele);
		dialogGroupMenu = new GroupMenu();
		dialogGroupMenu.show(getSupportFragmentManager(), "tagGroupMenu");
	}

	@Override
	public void onDialogGroupMenuModify() {
		dialogGroupMenu.dismiss();
		/*Toast.makeText(
				getApplicationContext(),
				"Modify the task: " + currentElement.getTask()
						+ " - Not implemented", Toast.LENGTH_SHORT).show();*/
	}

	@Override
	public void onDialogGroupMenuPosition() {
		dialogGroupMenu.dismiss();
		Toast.makeText(getApplicationContext(),
				"Change position - Not implemented", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDialogGroupMenuAddSubTask() {
		dialogGroupMenu.dismiss();
		DialogFragment dialog = new NewSubTask();
		dialog.show(getSupportFragmentManager(), "tagNewSubTask");

	}

	@Override
	public void onDialogNewSubTask(Element ele) {
		MainList.NewSubTask(ele);
		reloadList();
	}

	@Override
	public void onClick(View v) {
		if (v == new_element) {
			DialogFragment dialogo = new NewTask();
			dialogo.show(getSupportFragmentManager(), "tagNewTask");
		}
		if (v == new_group) {
			DialogFragment dialogo = new NewGroup();
			dialogo.show(getSupportFragmentManager(), "tagNewGroup");
		}
		if (v == check_all) {
			MainList.checkAll();
			reloadList();
			Toast.makeText(getApplicationContext(), "Check all tasks",
					Toast.LENGTH_LONG).show();
		}
		if (v == remove) {
			 MainList.deleteChecked();
			 reloadList();
			// deleteDB();
			Toast.makeText(getApplicationContext(),
					"Deleted all checked tasks", Toast.LENGTH_SHORT).show();

		}
		if (v == config) {
			reloadList();
			Toast.makeText(
					getApplicationContext(),
					"Debug option - Reload List \nOriginal function not implemented",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onLongClick(View v) {
		if (v == logo) {
			mp.start();
		}
		return false;
	}

}
