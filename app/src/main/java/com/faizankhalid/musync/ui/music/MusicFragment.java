package com.faizankhalid.musync.ui.music;

import java.util.ArrayList;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faizankhalid.musync.MediaPlayerService;
import com.faizankhalid.musync.R;
import com.faizankhalid.musync.StorageUtil;

public class MusicFragment extends Fragment {

    ArrayList<Audio> audioList;
    private RecyclerView songView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_music, container, false);

        songView = (RecyclerView)root.findViewById(R.id.song_list);


        audioList = new ArrayList<Audio>();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        69);
            }
        else
            LetsDoIt();

        return root;
    }

    void LetsDoIt()
    {
        loadAudio();
        //play the first audio in the ArrayList
        //playAudio(audioList.get(0).getData());

        AudioAdapter songAdt = new AudioAdapter(this.getContext(), audioList);
        songView.setAdapter(songAdt);
        songView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }


    private void loadAudio() {
        ContentResolver contentResolver = getContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to audioList
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        cursor.close();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 69: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {LetsDoIt(); }
                return;
            }
        }
    }











    /*
     *
     *          Connection to Service
     *
     *
    
    
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

            //Toast.makeText(getContext(), "Service Bound", Toast.LENGTH_SHORT).show();
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
            StorageUtil storage = new StorageUtil(getContext().getApplicationContext());
            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);
            
            Intent playerIntent = new Intent(getContext(), MediaPlayerService.class);
            getContext().startService(playerIntent);
            getContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getContext().getApplicationContext());
            storage.storeAudioIndex(audioIndex);
            
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            getContext().sendBroadcast(broadcastIntent);
        }
    }
    
    //*/


}