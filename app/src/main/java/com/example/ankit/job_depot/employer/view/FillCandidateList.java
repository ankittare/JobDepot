package com.example.ankit.job_depot.employer.view;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ankit.job_depot.R;
import com.parse.ParseObject;

import java.util.List;

public class FillCandidateList extends BaseAdapter {
    private List<ParseObject> elements;
    Activity mContext;
    public FillCandidateList(Activity mContext, List<ParseObject> elements) {
        this.elements = elements;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public ParseObject getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View row;
        row = inflater.inflate(R.layout.emp_candidate_list, parent, false);
        TextView title, detail;
        title = (TextView) row.findViewById(R.id.title);
        detail = (TextView) row.findViewById(R.id.detail);
        title.setText(getItem(position).get("username").toString());
        title.setMovementMethod(LinkMovementMethod.getInstance());
        detail.setText(getItem(position).get("skills").toString());
        return (row);
    }
}