package com.example.dialog;

import com.example.simpletodolist.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class GroupMenu extends DialogFragment{

	LayoutInflater inflater = null;
	View myLayout = null;
	Button Mod, Pos, Add;

	
	public interface GroupMenuListener {
		public void onDialogGroupMenuModify();
		public void onDialogGroupMenuPosition();
		public void onDialogGroupMenuAddSubTask();
	}
	
	// Use this instance of the interface to deliver action events
	GroupMenuListener mListener;

		// Override the Fragment.onAttach() method to instantiate the
		// NoticeDialogListener
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			// Verify that the host activity implements the callback interface
			try {
				// Instantiate the NoticeDialogListener so we can send events to the
				// host
				mListener = (GroupMenuListener) activity;
			} catch (ClassCastException e) {
				// The activity doesn't implement the interface, throw exception
				throw new ClassCastException(activity.toString()
						+ " must implement NoticeDialogListener");
			}
		}
		
	public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		inflater = getActivity().getLayoutInflater();
		myLayout = inflater.inflate(R.layout.dialog_group_menu, null);

		builder.setView(myLayout).setTitle("Group Menu")
				.setIcon(R.drawable.ic_launcher);

		Mod = (Button) myLayout.findViewById(R.id.ModGroup);
		Pos = (Button) myLayout.findViewById(R.id.ChangePos);
		Add = (Button) myLayout.findViewById(R.id.AddSubTask);
		Mod.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onDialogGroupMenuModify();
			}
		});
		
		Pos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onDialogGroupMenuPosition();
			}
		});
		
		Add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onDialogGroupMenuAddSubTask();
			}
		});

		return builder.create();
		}
	
}
