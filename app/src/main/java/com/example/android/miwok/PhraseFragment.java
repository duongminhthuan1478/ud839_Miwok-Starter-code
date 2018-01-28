package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;


public class PhraseFragment extends Fragment {

    private MediaPlayer mediaPlayer;

    private AudioManager mAudioManager;

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompleListioner = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //Stop the media & release resource
                        releaseMediaPlayer();
                    } else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        mediaPlayer.start();
                    }
                }
            };


    public PhraseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.work_list, container, false);

        //Created and setup the AudioMManager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Array of Family
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?", "Bạn đang ở đi đâu vậy", R.raw.where_are_you_going));
        words.add(new Word("What is your name?", "Tên của bạn là gì", R.raw.what_is_your_name));
        words.add(new Word("My name is...", "Tên tôi là...", R.raw.my_name_is));
        words.add(new Word("How are you feeling?", "Bạn cảm thấy thế nào?", R.raw.how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "Tôi cảm thấy tốt", R.raw.im_feeling_good));
        words.add(new Word("Are you coming?", "Bạn đang đến?", R.raw.are_you_coming));
        words.add(new Word("Yes, I’m coming", "Vâng, tôi đang đến", R.raw.yes_im_coming));
        words.add(new Word("I’m coming.", "Tôi đang đến.", R.raw.im_coming));
        words.add(new Word("Let’s go.   ", "Đi nào.", R.raw.lets_go));
        words.add(new Word("Come here!", "Đến đây!", R.raw.come_here));

        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        // find id list of work_list.xml
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**parameter i : the position of the view in the adapter
             parameter l :  The row id of the item that was clicked. */
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Release the media player immediatly if user tapping multiple item diffrenent
                releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Word wordMedia = words.get(i);


                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mediaPlayer = MediaPlayer.create(getActivity(), wordMedia.getmAudioResourceID());
                    mediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mediaPlayer.setOnCompletionListener(mCompleListioner);
                }

            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resource because we won't\
        // be playing any more sounds
        releaseMediaPlayer();
        Log.v("Activity", "-- ON STOP --");
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
