package debjyoti.example.com.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import debjyoti.example.com.bakingtime.R;
import debjyoti.example.com.bakingtime.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Intent intent = new Intent(context, MyWidgetRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_list, intent);


        Intent mainIntent = new Intent(context, RecipesActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_title, mainPendingIntent);

//        Intent intent = new Intent(context, MyWidgetRemoteViewsService.class);
//        views.setRemoteAdapter(R.id.widget_list, intent);

        views.setEmptyView(R.id.widget_list, R.id.widget_empty_view);
//
//       Intent mIntent = new Intent(context, RecipeDetailFragment.class);
//        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//       views.setPendingIntentTemplate(R.id.widget_list, mPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName mComponentName = new ComponentName(context, BakingAppWidgetProvider.class);
            mAppWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.widget_list);
        }
        super.onReceive(context, intent);
    }
}

