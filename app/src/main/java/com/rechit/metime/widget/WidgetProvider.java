package com.rechit.metime.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.rechit.metime.R;
import com.rechit.metime.view.ui.MainActivity;
import com.rechit.metime.view.ui.TimeFragment;


public class WidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Intent intent = new Intent(context, WidgetService.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
        views.setRemoteAdapter(R.id.item_list, intent);

        // widget cick
        Intent intentOne = new Intent(context, MainActivity.class);
        intentOne.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentOne = PendingIntent.getActivity(context, 0, intentOne,0);
        views.setPendingIntentTemplate(R.id.appwidget_logo, pendingIntentOne);


        //widget single item click
        Intent intentTwo = new Intent(context, MainActivity.class);
        PendingIntent pendingIntentTwo = TaskStackBuilder.create(context).addNextIntentWithParentStack(intentTwo)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.item_list, pendingIntentTwo);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    //not complete
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        if(action !=null){
            if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                ComponentName cn = new ComponentName(context, WidgetProvider.class);
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(cn), R.id.item_list);
            }
        }
    }

    //for uopdate widget
    public static void sendBroadcastRefresh(Context context){
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, WidgetProvider.class ));
        context.sendBroadcast(intent);
    }

}