package debjyoti.example.com.bakingtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import debjyoti.example.com.bakingtime.extras.Extras;
import debjyoti.example.com.bakingtime.extras.Step;


public class StepDetailFragment extends android.support.v4.app.Fragment implements ExoPlayer.EventListener {

    public static final String PLAY_WHEN_READY = "Play When Ready";
    public static final String PLAY_BACK_POSITION = "Play Back Position";
    public static final String CURRENT_WINDOW = "Current Window";
    private static MediaSessionCompat mMediaSession;
    //save Exo Player State Variables
    long playBackPosition;
    boolean playWhenReady;
    int currentWindow;
    private TextView descriptionTitle;
    private TextView descriptionStep;
    private String videoUrl;
    private String description;
    private String thumbnailUrl;
    private Step step;
    private int stepIndex;
    private ArrayList<Step> steps;
    private ImageView thumbnail;
    //Exo player variables
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private PlaybackStateCompat.Builder mStateBuilder;


    public StepDetailFragment() {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());


    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        descriptionTitle = rootView.findViewById(R.id.tv_title_description_step);
        descriptionStep = rootView.findViewById(R.id.tv_description_step);
        mPlayerView = rootView.findViewById(R.id.video_step_view);
        initializeMediaSession();

        Button previous_step = rootView.findViewById(R.id.previousStep);
        Button next_step = rootView.findViewById(R.id.nextStep);
        thumbnail = rootView.findViewById(R.id.iv_thumbnail_description_step);

        Bundle bundle = getArguments();
        if (bundle != null) {
            videoUrl = bundle.getString(Extras.VIDEO_URL);
            description = bundle.getString(Extras.STEP_DESCRIPTION);
            thumbnailUrl = bundle.getString(Extras.THUMBNAIL);
            stepIndex = bundle.getInt(Extras.STEP_INDEX);
            steps = bundle.getParcelableArrayList(Extras.STEPS);


        } else {
            Log.d("Bundle error", "Step detail bundle is empty");
        }


        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            playBackPosition = savedInstanceState.getLong(PLAY_BACK_POSITION);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
        }

        if (description != null && !description.isEmpty()) {

            descriptionStep.setText(description);
        } else {
            descriptionTitle.setVisibility(View.GONE);
        }


        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {

            Picasso.with(getContext()).load(thumbnailUrl).into(thumbnail);
        } else {
            thumbnail.setVisibility(View.GONE);
        }
        if (videoUrl != null && !videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl));
        } else {
            mPlayerView.setVisibility(View.GONE);

        }


        previous_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExoPlayer != null) {
                    mExoPlayer.stop();
                }
                if (stepIndex > 0) {
                    stepIndex--;
                    playVideo(rootView);

                } else {
                    Toast.makeText(getContext(), "You are in the first step", Toast.LENGTH_LONG).show();
                }
            }

        });

        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExoPlayer != null) {
                    mExoPlayer.stop();
                }

                if (stepIndex < steps.size() - 1) {
                    stepIndex++;
                    playVideo(rootView);

                } else {
                    Toast.makeText(getContext(), "You are in the last step", Toast.LENGTH_LONG).show();
                }
            }

        });


        return rootView;
    }


    private void playVideo(View rootView) {
        releasePlayer();
        reset();
        step = steps.get(stepIndex);
        videoUrl = step.getVideoUrl();
        description = step.getDescription();
        thumbnailUrl = step.getThumbnailUrl();

        if (description != null && !description.isEmpty()) {
            descriptionStep.setText(description);
        } else {
            descriptionTitle.setVisibility(View.GONE);
        }

        if (thumbnail != null && !thumbnailUrl.isEmpty()) {
            ImageView thumbnail = rootView.findViewById(R.id.iv_thumbnail_description_step);
            Picasso.with(getContext()).load(thumbnailUrl).into(thumbnail);
        }
        if (videoUrl != null && !videoUrl.isEmpty()) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoUrl));
            mMediaSession.setActive(true);
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    void reset() {
        playBackPosition = 0;
        currentWindow = 0;
        playWhenReady = false;
    }


    private void initializeMediaSession() {


        mMediaSession = new MediaSessionCompat(getContext(), "Media Session");


        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);


        mMediaSession.setMediaButtonReceiver(null);


        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        mMediaSession.setCallback(new MySessionCallback());


        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playBackPosition);
            mExoPlayer.addListener(this);


            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory
                    (getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {

            playBackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(videoUrl)) {
            initializePlayer(Uri.parse(videoUrl));
            mMediaSession.setActive(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_WINDOW, currentWindow);
        outState.putLong(PLAY_BACK_POSITION, playBackPosition);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);


    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}


