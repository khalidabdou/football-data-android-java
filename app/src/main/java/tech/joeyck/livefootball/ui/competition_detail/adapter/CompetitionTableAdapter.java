package tech.joeyck.livefootball.ui.competition_detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.TeamTableEntity;
import tech.joeyck.livefootball.ui.competitions.MainActivity;
import tech.joeyck.livefootball.utilities.NetworkUtils;

public class CompetitionTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The context we use to utility methods, app resources and layout inflaters
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our CompetitionAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onItemClick method whenever
     * an item is clicked in the list.
     */
    private final CompetitionAdapterOnItemClickHandler mClickHandler;

    private List<CompetitionTableItem> mTable;

    /**
     * Creates a CompetitionAdapter.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public CompetitionTableAdapter(@NonNull Context context, CompetitionAdapterOnItemClickHandler clickHandler) {
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
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new CompetitionAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == CompetitionTableItem.TYPE_HEADER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.table_header, viewGroup, false);
            return new CompetitionAdapterHeaderViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.table_item, viewGroup, false);
            view.setFocusable(true);
            return new CompetitionAdapterViewHolder(view);
        }
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        int type = getItemViewType(position);
        if (type == CompetitionTableItem.TYPE_HEADER) {
            HeaderItem header = (HeaderItem) mTable.get(position);
            CompetitionAdapterHeaderViewHolder holder = (CompetitionAdapterHeaderViewHolder) viewHolder;
            holder.headerTextView.setText(header.getTitle());
        } else {
            CompetitionAdapterViewHolder holder = (CompetitionAdapterViewHolder) viewHolder;
            TeamItem currentTeamTableEntity = (TeamItem)mTable.get(position);
            holder.teamNameTextView.setText(currentTeamTableEntity.getTeam().getTeam().getName());
            holder.pointsTextView.setText(""+currentTeamTableEntity.getTeam().getPoints());
            holder.positionTextView.setText(""+currentTeamTableEntity.getTeam().getPosition());
            String crestUrl = NetworkUtils.getPngUrl(currentTeamTableEntity.getTeam().getTeam().getCrestUrl());
            Glide.with(mContext).load(crestUrl).into(holder.crestView);
        }

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mTable) return 0;
        return mTable.size();
    }

    /**
     * Swaps the list used by the CompetitionAdapter for its weather data. This method is called by
     * {@link MainActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param table the new list of forecasts to use as CompetitionAdapter's data source
     */
    public void swapTable(final List<CompetitionTableItem> table) {
        mTable = table;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mTable.get(position).getType();
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface CompetitionAdapterOnItemClickHandler {
        void onItemClick(TeamTableEntity competition);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class CompetitionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView crestView;
        final TextView teamNameTextView;
        final TextView positionTextView;
        final TextView pointsTextView;

        CompetitionAdapterViewHolder(View view) {
            super(view);

            crestView = view.findViewById(R.id.team_crest);
            teamNameTextView = view.findViewById(R.id.team_name);
            pointsTextView = view.findViewById(R.id.team_points);
            positionTextView = view.findViewById(R.id.team_position);

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
            TeamTableEntity teamTableEntity = ((TeamItem)mTable.get(adapterPosition)).getTeam();
            mClickHandler.onItemClick(teamTableEntity);
        }
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class CompetitionAdapterHeaderViewHolder extends RecyclerView.ViewHolder {

        final TextView headerTextView;

        CompetitionAdapterHeaderViewHolder(View view) {
            super(view);
            headerTextView = view.findViewById(R.id.headerText);
        }

    }
}