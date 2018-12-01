package tech.joeyck.livefootball.ui.matches;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.ui.competitions.MainActivity;
import tech.joeyck.livefootball.utilities.DateUtils;

class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesAdapterViewHolder> {

    // The context we use to utility methods, app resources and layout inflaters
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our CompetitionAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onItemClick method whenever
     * an item is clicked in the list.
     */
    private final MatchesAdapter.MatchesAdapterOnItemClickHandler mClickHandler;

    private List<MatchEntity> mMatches;

    /**
     * Creates a CompetitionAdapter.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    MatchesAdapter(@NonNull Context context, MatchesAdapter.MatchesAdapterOnItemClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new CompetitionAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MatchesAdapter.MatchesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId = R.layout.match_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new MatchesAdapter.MatchesAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MatchesAdapter.MatchesAdapterViewHolder viewHolder, int position) {
        MatchEntity currentMatch = mMatches.get(position);
        viewHolder.homeTeamNameText.setText(currentMatch.getHomeTeam().get("name"));
        viewHolder.awayTeamNameText.setText(currentMatch.getAwayTeam().get("name"));
        viewHolder.homeTeamScoreText.setText(currentMatch.getScore().getHomeTeamScore());
        viewHolder.awayTeamScoreText.setText(currentMatch.getScore().getAwayTeamScore());
        viewHolder.dateText.setText(DateUtils.getFormattedMatchDate(mContext,currentMatch.getLocalDateTime()));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mMatches) return 0;
        return mMatches.size();
    }

    /**
     * Swaps the list used by the CompetitionAdapter for its weather data. This method is called by
     * {@link MainActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param matches the new list of forecasts to use as CompetitionAdapter's data source
     */
    void swapMatches(final List<MatchEntity> matches) {
        mMatches = matches;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface MatchesAdapterOnItemClickHandler {
        void onItemClick(MatchEntity match);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class MatchesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView homeTeamNameText;
        final TextView awayTeamNameText;
        final TextView homeTeamScoreText;
        final TextView awayTeamScoreText;
        final TextView dateText;

        MatchesAdapterViewHolder(View view) {
            super(view);

            homeTeamNameText = view.findViewById(R.id.home_name_text);
            awayTeamNameText = view.findViewById(R.id.away_name_text);
            homeTeamScoreText = view.findViewById(R.id.home_score_text);
            awayTeamScoreText = view.findViewById(R.id.away_score_tex);
            dateText = view.findViewById(R.id.date_text);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onItemClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MatchEntity matchEntity = mMatches.get(adapterPosition);
            mClickHandler.onItemClick(matchEntity);
        }
    }


}
