package com.example.dialog;

import com.example.adapter.Element;
import com.example.row.RowType;
import com.example.simpletodolist.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class NewSubTask extends DialogFragment{
	
	LayoutInflater inflater = null;
	View myLayout = null;
	EditText SubTaskContent;
	
	public interface NewSubTaskListener {
		public void onDialogNewSubTask(Element ele);
	}
	
	// Use this instance of the interface to deliver action events
	NewSubTaskListener mListener;

		// Override the Fragment.onAttach() method to instantiate the
		// NoticeDialogListener
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			// Verify that the host activity implements the callback interface
			try {
				// Instantiate the NoticeDialogListener so we can send events to the
				// host
				mListener = (NewSubTaskListener) activity;
			} catch (ClassCastException e) {
				// The activity doesn't implement the interface, throw exception
				throw new ClassCastException(activity.toString()
						+ " must implement NoticeDialogListener");
			}
		}
		
public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			inflater = getActivity().getLayoutInflater();
			myLayout = inflater.inflate(R.layout.dialog_new_subtask, null);
			SubTaskContent = (EditText) myLayout.findViewById(R.id.AddSubTaskEdit);			
			

			builder.setView(myLayout)
					.setTitle(R.string.DialogNewSubTaskTitle)
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Add",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Element ele = new Element(0, RowType.SUBTASK_ROW.ordinal(), 0, 0,
											SubTaskContent.getText().toString().replace("'", "''"), 0, 0);
									mListener.onDialogNewSubTask(ele);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									NewSubTask.this.getDialog().cancel();
								}
							});

			return builder.create();
		}
}
