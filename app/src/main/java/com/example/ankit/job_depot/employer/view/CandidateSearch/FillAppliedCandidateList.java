package com.example.ankit.job_depot.employer.view.CandidateSearch;

import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.employer.model.DAO.EmployerHistory;
import com.parse.ParseObject;

import java.util.List;
import java.util.Map;

/**
 * Created by anjali on 8/2/15.
 */
public class FillAppliedCandidateList extends BaseAdapter {
    private List<ParseObject> elements;
    Activity mContext;
    public FillAppliedCandidateList(Activity mContext, List<ParseObject> elements) {
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

        EmployerHistory o = new EmployerHistory();
        ParseObject candidateDetails = o.getSingleCandidate(getItem(position).getString("studentCandidateID"));
        Log.d("candidateDetails", candidateDetails.toString());
        ParseObject jobDetails = o.getSingleJobDetail(getItem(position).getString("jobID"));
        Log.d("jobdetails", "Hello "+jobDetails);

        Log.d("FillCandi", candidateDetails.getString("username"));
        title.setText(candidateDetails.getString("username"));
        title.setMovementMethod(LinkMovementMethod.getInstance());
        Log.d("FillCandi", jobDetails.getString("jobName"));
        detail.setText("Applied for opening in \"" + jobDetails.getString("jobName") + "\" position");
        return (row);
    }
}