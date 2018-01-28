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


public class FamilyFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                        // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                        // our app is allowed to continue playing sound but at a lower volume. We'll treat
                        // both cases the same way because our app is playing short sound files.

                        // Pause playback and reset player to the start of the file. That way, we can
                        // play the word from the beginning when we resume playback.
                        mediaPlayer.pause();
                        //SeekTo , start mediaPlayer at position 0 ,
                        mediaPlayer.seekTo(0);
                    }
                    else  if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                        // Stop playback and clean up resources
                        releaseMediaPlayer();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // The AUDIOFOCUS_GAIN cases means we have regained focus and can
                        // resume playback
                        mediaPlayer.start();
                    }
                }
            };


    public FamilyFragment() {
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
        words.add(new Word("Father", "Bố", R.drawable.family_father, R.raw.father));
        words.add(new Word("Mother", "Mẹ", R.drawable.family_mother, R.raw.mother));
        words.add(new Word("Son", "Con trai", R.drawable.family_son, R.raw.son));
        words.add(new Word("Daughter", "Con gái", R.drawable.family_daughter, R.raw.daughter));
        words.add(new Word("Older brother", "Anh trai", R.drawable.family_older_brother, R.raw.older_brother));
        words.add(new Word("Younger brother", "Em trai", R.drawable.family_younger_brother, R.raw.younger_brother));
        words.add(new Word("Older sister", "Chị gái", R.drawable.family_older_sister, R.raw.older_sister));
        words.add(new Word("Younger sister", "Em gái", R.drawable.family_younger_sister, R.raw.younger_sister));
        words.add(new Word("Grandmother", "Bà", R.drawable.family_grandmother, R.raw.grandmother));
        words.add(new Word("Grandfather", "Ông", R.drawable.family_grandfather, R.raw.grandfather));

        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_family);

        // find id list of work_list.xml
        ListView listView =(ListView) rootView.findViewById(R.id.list);

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

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have a audio focus now

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mediaPlayer = MediaPlayer.create(getActivity(), wordMedia.getmAudioResourceID());
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
                // Log.v("FamilyActivity", "Infor Word current" + wordMedia.toString());
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
