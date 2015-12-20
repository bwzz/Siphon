package com.yuantiku.siphon.mvp.context;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.yuantiku.siphon.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghb on 15/10/21.
 */
public class TutorEntryTestContext extends BaseContext {
    @Bind(R.id.teacher_id)
    TextView teacherIdTextView;

    @Bind(R.id.lecture_id)
    TextView lectureIdTextView;

    @Bind(R.id.season_id)
    TextView seasonIdTextView;

    @Bind(R.id.teacher_id_for_serial)
    TextView teacherIdForSerialTextView;

    @Bind(R.id.serial_id)
    TextView serialIdTextView;

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

    @OnClick(R.id.launch_serial)
    public void launchSerial() {
        String type = "Serial";
        Map<String, String> map = new HashMap<>();
        map.put("id", serialIdTextView.getText().toString());
        map.put("teacherId", teacherIdForSerialTextView.getText().toString());
        launch(type, map);
    }

    private void launch(String type, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        launch(type, map);
    }

    private void launch(String type, Map<String, String> query) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : query.keySet()) {
            stringBuilder.append(key)
                    .append("=")
                    .append(query.get(key))
                    .append("&");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.format("tutor1f6f42334e1709a4://openRecommended%s?%s",
                    type, stringBuilder.toString())));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "手机上没有安装猿辅导或者猿辅导没有提供支持", Toast.LENGTH_LONG).show();
        }
    }
}
