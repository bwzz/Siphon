/*
 * Copyright (c) 2016 John Paul Krause.
 * MainActivity.java is part of AndroidProxySetter.
 *
 * AndroidProxySetter is free software: you can redistribute it and/or modify
 * iit under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidProxySetter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidProxySetter.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package proxysetter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.yuantiku.siphon.R;
import com.yuantiku.siphon.app.ApplicationFactory;
import com.yuantiku.siphon.data.apkconfigs.ApkConfig;

import bwzz.fragment.BaseFragment;


public class ProxyFragment extends BaseFragment {

    private static final String TAG = "ProxySetterApp";

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_proxy, container, false);
        findViewById(R.id.set).setOnClickListener(v -> {
            Intent intent = getParamIntent();
            if (!validateIntent(intent)) {
                showPopup("Invalid parameters!");
            } else {
                new ProxyChangeAsync(getActivity()).execute(intent);
            }
        });
        findViewById(R.id.clear).setOnClickListener(v -> {
            Intent intent = getParamIntent();
            intent.putExtra(ProxyChangeParams.CLEAR, "true");
            if (!validateIntent(intent)) {
                showPopup("Invalid parameters!");
            } else {
                new ProxyChangeAsync(getActivity()).execute(intent);
            }
        });
        restore();
        return rootView;
    }

    private View findViewById(int id) {
        return rootView.findViewById(id);
    }

    private Intent getParamIntent() {
        Intent intent = new Intent();
        fillTextToIntent(R.id.ssid, intent, ProxyChangeParams.SSID);
        fillTextToIntent(R.id.password, intent, ProxyChangeParams.KEY);
        fillTextToIntent(R.id.host, intent, ProxyChangeParams.HOST);
        fillTextToIntent(R.id.port, intent, ProxyChangeParams.PORT);
        fillTextToIntent(R.id.bypass, intent, ProxyChangeParams.BYPASS);
        intent.putExtra(ProxyChangeParams.RESET_WIFI, "true");
        return intent;
    }

    private void fillTextToIntent(@IdRes int id, Intent intent, String key) {
        TextView t = (TextView) findViewById(id);
        if (!TextUtils.isEmpty(t.getText())) {
            intent.putExtra(key, t.getText().toString());
            Hawk.put(key, t.getText().toString());
        }
    }

    private boolean validateIntent(Intent intent) {
        if (!intent.hasExtra(ProxyChangeParams.HOST) && !intent.hasExtra(ProxyChangeParams.CLEAR)) {
            showPopup("Error: No HOST given or not clearing proxy");
            return false;
        }
        if (!intent.hasExtra(ProxyChangeParams.SSID)) {
            showPopup("Warning: No SSID given, setting on the fist one available");
        }
        return true;
    }

    /**
     * Shows a toast and logs to logcat
     *
     * @param msg Message to show/log
     */
    public void showPopup(final String msg) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            Log.d(TAG, msg);
        });
    }

    private void restore() {
        restoreString(R.id.ssid, ProxyChangeParams.SSID);
        restoreString(R.id.password, ProxyChangeParams.KEY);
        restoreString(R.id.host, ProxyChangeParams.HOST);
        restoreString(R.id.port, ProxyChangeParams.PORT);
        restoreString(R.id.bypass, ProxyChangeParams.BYPASS);
    }

    private void restoreString(@IdRes int id, String key) {
        String value = Hawk.get(key, "");
        TextView t = (TextView) findViewById(id);
        t.setText(value);
    }

}
