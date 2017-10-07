package com.example.row;

import com.example.adapter.Element;
import com.example.simpletodolist.Main;
import com.example.simpletodolist.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class TaskRow implements Row {
	private final Element element;
	private final LayoutInflater inflater;
	private final Main activity;

	public TaskRow(Context context, Element element) {
		this.activity = (Main) context;
		this.element = element;
		this.inflater = LayoutInflater.from(context);
	}
	
	public View getView(View convertView) {
        ViewHolder holder;
        View view;
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null) {
            ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.task_row, null);

            //use the view holder pattern to save of already looked up subviews
            holder = new ViewHolder((ImageView)viewGroup.findViewById(R.id.icon),
                    (TextView)viewGroup.findViewById(R.id.task));
            viewGroup.setTag(holder);

            view = viewGroup;
        } else {
            //get the holder back out
            holder = (ViewHolder)convertView.getTag();

            view = convertView;
        }
        
        
        holder.chekedView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				element.setChecked((element.isChecked() ? false : true));
				activity.changeState(element);
			}
		});

        //actually setup the view
        if(element.isChecked()){
        	 holder.chekedView.setImageResource(R.drawable.checked_yes);
        }else{
        	 holder.chekedView.setImageResource(R.drawable.checked_no);
        }
 
        holder.taskView.setText(element.getTask());

        return view;
    }

	public int getViewType() {
		return RowType.TASK_ROW.ordinal();
	}

	private static class ViewHolder {
		final ImageView chekedView;
		final TextView taskView;

		private ViewHolder(ImageView chekedView, TextView taskView) {
			this.chekedView = chekedView;
			this.taskView = taskView;
		}
	}
}
