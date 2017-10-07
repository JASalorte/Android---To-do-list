package com.example.row;

import com.example.simpletodolist.Element;
import com.example.simpletodolist.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simpletodolist.R;

public class GroupRow implements Row {

	private final Element element;
	private final LayoutInflater inflater;
	private final Main activity;
	private final String Text;

	public GroupRow(Context context, Element element, String Text) {
		this.activity = (Main) context;
		this.Text = Text;
		this.element = element;
		this.inflater = LayoutInflater.from(context);
	}

	public View getView(View convertView) {
		ViewHolder holder;
		View view;
		// we have a don't have a converView so we'll have to create a new one
		if (convertView == null) {
			ViewGroup viewGroup = (ViewGroup) inflater.inflate(
					R.layout.group_row, null);

			// use the view holder pattern to save of already looked up subviews
			holder = new ViewHolder(
					(ImageView) viewGroup.findViewById(R.id.icon2),
					(TextView) viewGroup.findViewById(R.id.taskGroup),
					(TextView) viewGroup.findViewById(R.id.countGroup));
			viewGroup.setTag(holder);

			view = viewGroup;
		} else {
			// get the holder back out
			holder = (ViewHolder) convertView.getTag();

			view = convertView;
		}

		holder.chekedView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				element.setChecked((element.isChecked() ? false : true));
				activity.changeState(element);
			}
		});
		
		holder.taskView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				activity.LongClickGroupDialog(element);
				//activity.sendMSN("Soy: " + element.getTask() + " y has hecho una pulsación larga sobre mi.");
				return false;
			}
		});

		// actually setup the view
		if (element.isChecked()) {
			holder.chekedView.setImageResource(R.drawable.see_less_small);
		} else {
			holder.chekedView.setImageResource(R.drawable.see_more_small);
		}
		holder.pointView.setText(Text);
		holder.taskView.setText(element.getTask());

		return view;
	}

	public int getViewType() {
		return RowType.GROUP_ROW.ordinal();
	}

	private static class ViewHolder {
		final ImageView chekedView;
		final TextView taskView;
		final TextView pointView;

		private ViewHolder(ImageView chekedView, TextView taskView,
				TextView pointView) {
			this.pointView = pointView;
			this.chekedView = chekedView;
			this.taskView = taskView;
		}
	}

}
