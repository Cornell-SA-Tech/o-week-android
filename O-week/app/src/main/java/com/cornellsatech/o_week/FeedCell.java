package com.cornellsatech.o_week;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cornellsatech.o_week.models.Event;
import com.cornellsatech.o_week.util.NotificationCenter;
import com.cornellsatech.o_week.util.Notifications;
import com.cornellsatech.o_week.util.Settings;

/**
 * Holds data and reference pointers to {@link View}s for an {@link Event}. Its physical representation
 * is in {@link R.layout#cell_feed}.
 *
 * {@link #event}: The event that this object currently represents.
 * {@link #context}: To be used in {@link #onClick(View)}
 *
 * @see FeedAdapter
 */
public class FeedCell extends RecyclerView.ViewHolder implements View.OnClickListener
{
	private final TextView startTimeText;
	private final TextView endTimeText;
	private final TextView titleText;
	private final TextView captionText;
	private final CheckBox checkBox;
	private final TextView requiredLabel;
	private final TextView categoryRequiredText;
	private final Context context;
	private Event event;
	private static final String TAG = FeedCell.class.getSimpleName();

	/**
	 * Stores pointers to all the subviews and sets up listeners.
	 * @param itemView {@inheritDoc}
	 */
	public FeedCell(View itemView)
	{
		super(itemView);
		startTimeText = itemView.findViewById(R.id.startTimeText);
		endTimeText = itemView.findViewById(R.id.endTimeText);
		titleText = itemView.findViewById(R.id.titleText);
		captionText = itemView.findViewById(R.id.captionText);
		checkBox = itemView.findViewById(R.id.checkbox);
		requiredLabel = itemView.findViewById(R.id.requiredLabel);
		categoryRequiredText = itemView.findViewById(R.id.categoryRequiredText);
		checkBox.setOnClickListener(this);
		itemView.setOnClickListener(this);
		context = itemView.getContext();
	}
	/**
	 * Sets the current event to display. Time display formats are specified here: {@link Event#DISPLAY_TIME_FORMAT}.
	 *
	 * @param event The {@link Event} this cell will represent as long as it is visible.
	 * @param selected Whether this {@link #event} is selected.
	 */
	public void configure(Event event, boolean selected)
	{
		this.event = event;
		startTimeText.setText(event.startTime.toString(Event.DISPLAY_TIME_FORMAT));
		endTimeText.setText(event.endTime.toString(Event.DISPLAY_TIME_FORMAT));
		titleText.setText(event.title);
		captionText.setText(event.caption);
		checkBox.setChecked(selected);
		setVisible(event.required || event.categoryRequired, requiredLabel);
		setVisible(event.categoryRequired, categoryRequiredText);
		if (event.categoryRequired)
			categoryRequiredText.setText(UserData.categoryForPk(event.category).name);
	}
	/**
	 * This object has been clicked. List listeners know.
	 * @param v Clicked view.
	 */
	@Override
	public void onClick(View v)
	{
		if (v.getId() == checkBox.getId())  //checkBox was clicked
		{
			if (checkBox.isChecked())
			{
				UserData.insertToSelectedEvents(event);
				if (Settings.getReceiveReminders(context))
					Notifications.scheduleForEvent(event, context);
			}
			else
			{
				UserData.removeFromSelectedEvents(event);
				Notifications.unscheduleForEvent(event, context);
			}

		}
		else    //entire view was clicked
			NotificationCenter.DEFAULT.post(new NotificationCenter.EventEventClicked(event));
	}

	/**
	 * Helper method to set the visibility of a view based on a boolean expression.
	 * @param condition If true, the view is visible. If false, the view is gone.
	 * @param view The view to set visibility of.
	 */
	private void setVisible(boolean condition, View view)
	{
		if (condition)
			view.setVisibility(View.VISIBLE);
		else
			view.setVisibility(View.GONE);
	}
}
