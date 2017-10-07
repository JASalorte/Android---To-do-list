package com.example.dialog;

import com.example.row.RowType;
import com.example.simpletodolist.Element;
import com.example.simpletodolist.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;

public class NewGroup extends DialogFragment{

	LayoutInflater inflater = null;
	View myLayout = null;
	EditText TaskContent;
	RadioButton b1, b2, b3;
	
	public interface NewGroupListener {
		public void onDialogNewGroup(Element ele);
	}
	
	// Use this instance of the interface to deliver action events
		NewGroupListener mListener;

		// Override the Fragment.onAttach() method to instantiate the
		// NoticeDialogListener
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			// Verify that the host activity implements the callback interface
			try {
				// Instantiate the NoticeDialogListener so we can send events to the
				// host
				mListener = (NewGroupListener) activity;
			} catch (ClassCastException e) {
				// The activity doesn't implement the interface, throw exception
				throw new ClassCastException(activity.toString()
						+ " must implement NoticeDialogListener");
			}
		}
		
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			inflater = getActivity().getLayoutInflater();
			myLayout = inflater.inflate(R.layout.dialog_new_task, null);
			TaskContent = (EditText) myLayout.findViewById(R.id.AddTaskEdit);
			b1 = (RadioButton) myLayout.findViewById(R.id.radioButton1);
			b2 = (RadioButton) myLayout.findViewById(R.id.radioButton2);
			b3 = (RadioButton) myLayout.findViewById(R.id.radioButton3);
			
			b2.setChecked(true);
			
			b1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					b2.setChecked(false);
					b3.setChecked(false);
				}
			});

			b2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					b1.setChecked(false);
					b3.setChecked(false);
				}
			});

			b3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					b1.setChecked(false);
					b2.setChecked(false);
				}
			});
			
			

			builder.setView(myLayout)
					.setTitle(R.string.DialogNewGroupTitle)
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Add",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									
									int priotity = 0;
									if(b1.isChecked())
										priotity = 2;
									if(b2.isChecked())
										priotity = 1;
									if(b3.isChecked())
										priotity = 0;
									
									Element ele = new Element(0, RowType.GROUP_ROW.ordinal(), 0, priotity,
											TaskContent.getText().toString().replace("'", "''"), 0 , 0);

									mListener.onDialogNewGroup(ele);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									NewGroup.this.getDialog().cancel();
								}
							});

			return builder.create();
		}
}
