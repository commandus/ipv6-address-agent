package com.commandus.ipv6addressagent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.commandus.utilipv6.Subscription;

public class SubscriptionsActivity extends AppCompatActivity {
    private static final String TAG = SubscriptionsActivity.class.getSimpleName();
    private static final int RET_SEL_ACTION = 1;
    private ListView mSubscriptionsList;
    private SubscriptionsAdapter mAdapter;
    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSubscriptionsList = (ListView) findViewById(R.id.listview_subcriptions);

        Intent intent = getIntent();
        if (intent != null) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                // TODO
            }
        }

        mSettings = Settings.getInstance(this);

        mAdapter = new SubscriptionsAdapter(this, R.layout.listitem_subscription,
                mSettings.subscriptions);
        mSubscriptionsList.setAdapter(mAdapter);
        mSubscriptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subscription c = (Subscription) mAdapter.getItem(position);
                String address = c.address;
                String name = c.name;
                String endpoint = c.endpoint;
                String publicKey = c.publicKey;
                String token = c.getToken();
            }
        });
        // getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RET_SEL_ACTION:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        doAction(data);
                    }
                }
                break;
        }
    }

    private void doAction(Intent intent) {
        Log.d(TAG, "doAction");
    }

}
