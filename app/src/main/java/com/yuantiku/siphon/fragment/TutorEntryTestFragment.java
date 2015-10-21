package com.yuantiku.siphon.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuantiku.siphon.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bwzz.fragment.BaseFragment;

/**
 * @author wanghb
 * @date 15/10/21.
 */
public class TutorEntryTestFragment extends BaseFragment {

    @Bind(R.id.teacher_id)
    TextView teacherIdTextView;

    @Bind(R.id.lecture_id)
    TextView lectureIdTextView;

    @Bind(R.id.season_id)
    TextView seasonIdTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_entry_test, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.launch_teacher)
    public void launchTeacher() {
        String type = "Teacher";
        String id = teacherIdTextView.getText().toString();
        launch(type, id);
    }

    @OnClick(R.id.launch_lecture)
    public void launchLecture() {
        String type = "Lecture";
        String id = lectureIdTextView.getText().toString();
        launch(type, id);
    }

    @OnClick(R.id.launch_season)
    public void launchSeason() {
        String type = "Season";
        String id = seasonIdTextView.getText().toString();
        launch(type, id);
    }

    private void launch(String type, String id) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format("tutor1f6f42334e1709a4://openRecommended%s?id=%s",
                type, id)));
        startActivity(intent);
    }
}
