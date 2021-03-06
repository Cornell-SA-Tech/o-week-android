package com.cornellsatech.o_week;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cornellsatech.o_week.models.Event;

/**
 * Holds data and reference pointers to {@link View}s for an {@link Event}. Its physical representation
 * is in {@link com.cornellsatech.o_week.R.layout#cell_feed}.
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
	private final TextView locationText;
	private final TextView requiredLabel;
	private final Context context;
	private Event event;
	private static final String	TAG = FeedCell.class.getSimpleName();

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
		locationText = itemView.findViewById(R.id.locationText);
		requiredLabel = itemView.findViewById(R.id.requiredLabel);
		itemView.setOnClickListener(this);
		context = itemView.getContext();
	}
	/**
	 * Sets the current event to display. Time display formats are specified here: {@link Event#DISPLAY_TIME_FORMAT}.
	 *
	 * @param event The {@link Event} this cell will represent as long as it is visible.
	 */
	public void configure(Event event)
	{
		this.event = event;
		startTimeText.setText(event.getStartTime().toString(Event.DISPLAY_PADDED_TIME_FORMAT));
		endTimeText.setText(event.getEndTime().toString(Event.DISPLAY_PADDED_TIME_FORMAT));
		titleText.setText(event.getName());
		locationText.setText(event.getLocation());
		setVisible(UserData.requiredForUser(event), requiredLabel);
	}
	/**
	 * This object has been clicked. Open the details page.
	 * @param v Clicked view.
	 */
	@Override
	public void onClick(View v)
	{
		DetailsActivity.startWithEvent(event, context);
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
