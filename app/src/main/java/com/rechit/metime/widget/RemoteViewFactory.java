package com.rechit.metime.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.rechit.metime.R;
import com.rechit.metime.helper.DatabaseHelper;
import com.rechit.metime.model.Time;

import java.util.ArrayList;


public class RemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {


    private ArrayList<Time> timeList = new ArrayList<>();
    private Context context;

    private DatabaseHelper databaseHelper;

    public RemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        timeList = databaseHelper.getFromDatabase();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Time time = timeList.get(position);

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_provider_list);
        rv.setTextViewText(R.id.tv_activity, time.getTitleTime());
        rv.setTextViewText(R.id.tv_time, time.getTrackingTime());

        Intent fillIntent = new Intent();
        fillIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        rv.setOnClickFillInIntent(R.id.widget_item_list, fillIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
