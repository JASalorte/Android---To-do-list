package com.example.adapter;

import java.util.ArrayList;
import java.util.List;


import com.example.row.RowType;
import com.example.row.SubTaskRow;
import com.example.row.TaskRow;
import com.example.row.GroupRow;
import com.example.row.Row;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapter extends BaseAdapter{

	final List<Row> rows;

	
	public ListAdapter(Context context, List<Element> elementsList, List<String> numbers) {
		rows = new ArrayList<Row>();
		int pos = 0;
		boolean show_child = false;
		for(Element element : elementsList){
			if(element.getType() == 0){
				rows.add(new TaskRow(context, element));
			}
			if(element.getType() == 1){
				rows.add(new GroupRow(context, element, numbers.get(pos)));
				//rows.add(new GroupRow(context, element, "(0/0)"));
				pos++;
				if(element.isChecked())	show_child = true; else show_child = false;
			}
			if(element.getType() == 2){
				if(show_child)
				rows.add(new SubTaskRow(context, element));
			}
			
		}
	}
	
	 @Override
	    public int getViewTypeCount() {
	        return RowType.values().length;
	    }

	    @Override
	    public int getItemViewType(int position) {
	        return rows.get(position).getViewType();
	    }

	    public int getCount() {
	        return rows.size();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        return rows.get(position).getView(convertView);
	    }
	
}
