package com.faizankhalid.musync.ui.music;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faizankhalid.musync.MainActivity;
import com.faizankhalid.musync.MediaPlayerService;
import com.faizankhalid.musync.R;
import com.faizankhalid.musync.StorageUtil;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
    private ArrayList<Audio> songs;
    private Context context;
    public class AudioViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView artist;

        AudioViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.song_title);
            artist = (TextView) itemView.findViewById(R.id.song_artist);
        }
        
    }

    public AudioAdapter(Context c, ArrayList<Audio> theSongs){
        songs=theSongs;
        context=c;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song, parent, false);
        return new AudioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AudioViewHolder holder, final int position) {
        holder.title.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,holder.title.getText(),Toast.LENGTH_SHORT).show();
                //playAudio(audioList.get(audioList.getLayoutPosition()).getData());
                playAudio(position);
            }
        });
    }
    

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /*
    public void insert(int position, Audio data) {
        songs.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(Audio data) {
        int position = songs.indexOf(data);
        songs.remove(position);
        notifyItemRemoved(position);
    }
    //*/
    
    
    
    
    /*
     *
     *          Connection to Service
     *
     */
    
    
    public static final String Broadcast_PLAY_NEW_AUDIO
            = "com.faizankhalid.musync.PlayNewAudio";
    
    private MediaPlayerService player;
    boolean serviceBound = false;
    
    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
            
            //Toast.makeText(context, "Service Bound", Toast.LENGTH_SHORT).show();
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
    
    
    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(context.getApplicationContext());
            storage.storeAudio(songs);
            storage.storeAudioIndex(audioIndex);
            
            Intent playerIntent = new Intent(context, MediaPlayerService.class);
            context.startService(playerIntent);
            context.bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(context.getApplicationContext());
            storage.storeAudioIndex(audioIndex);
            
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            context.sendBroadcast(broadcastIntent);
        }
    }
    
    //*/
    
    
    
}
