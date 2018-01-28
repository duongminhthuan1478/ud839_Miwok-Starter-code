package com.example.android.miwok;

/**
 * Created by Dell on 8/20/2017.
 */

public class Word {
    /** Default translation for the word*/
    private String mDefaultTranslation;

    /**Miwok translation for the word*/
    private String mMiWorkTranslation;

    //Image resource ID for the word
    private int mImageResourceId =  NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    //Button radio resource ID for the word
    private int mAudioResourceID;

    // Constructor for these rest Activity
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int audioResourceID) {
        mDefaultTranslation = defaultTranslation;
        mMiWorkTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceID = audioResourceID;
    }

    // Constructor for Phrases Activity , No image
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceID) {
        mDefaultTranslation = defaultTranslation;
        mMiWorkTranslation = miwokTranslation;
        mAudioResourceID = audioResourceID;
    }
    /**
     * Get the default translation of the word
     */
    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the Miwok translation of the word
     */
    public String getmMiwokTranslation() {
        return mMiWorkTranslation;
    }


    /**
     * Get the image from drawable resource ID
     */
    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public int getmAudioResourceID() { return mAudioResourceID; }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }


}
