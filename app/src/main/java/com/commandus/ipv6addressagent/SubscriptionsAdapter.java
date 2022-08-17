package com.commandus.ipv6addressagent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.commandus.utilipv6.Subscription;
import com.commandus.utilipv6.Subscriptions;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubscriptionsAdapter extends ArrayAdapter implements Filterable {
    private static final String TAG = SubscriptionsAdapter.class.getSimpleName();
    private final DateFormat dateFormat;
    private final DateFormat timeFormat;
    private final NumberFormat mNumberFormat;

    public SubscriptionsAdapter(Context context, int layout, Subscriptions subscriptions) {
        super(context, layout, subscriptions.toArray());
        dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm");
        // round to 10m
        mNumberFormat = NumberFormat.getInstance();
        mNumberFormat.setMaximumFractionDigits(2);
        mNumberFormat.setMinimumFractionDigits(2);
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subscription c = (Subscription) getItem(position);

        Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.listitem_subscription, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.item_subscription_name);
        tvName.setText(c.name);
        return convertView;
    }

    private List<Subscription> resultList = new ArrayList<Subscription>();

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Subscription> subscriptionList = find(constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = subscriptionList;
                    filterResults.count = subscriptionList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Subscription>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    // TODO
    private List<Subscription> find(String constraint) {
        List<Subscription> r = new ArrayList<Subscription>();
        String uconstraint = constraint.toUpperCase();
        return r;
    }
}
