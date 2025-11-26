package com.github.abrarshakhi.pascalinecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.abrarshakhi.pascalinecalculator.database.HistoryEntity;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryEntity> {
    Context context;
    List<HistoryEntity> list;
    LayoutInflater inflater;

    public HistoryAdapter(Context context, List<HistoryEntity> arrayList) {
        super(context, R.layout.row_history, arrayList);
        this.context = context;
        this.list = arrayList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.row_history, parent, false);

        TextView ts = rowView.findViewById(R.id.tvTimestamp);
        TextView eq = rowView.findViewById(R.id.tvEquation);

        HistoryEntity hist = this.list.get(position);
        ts.setText(String.valueOf(hist.getId()));
        eq.setText(hist.getExpression() + " = " + hist.getAns());

        return rowView;
    }
}
