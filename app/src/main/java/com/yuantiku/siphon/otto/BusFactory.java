package com.yuantiku.siphon.otto;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by wanghb on 15/8/16.
 */
public class BusFactory {
    private static Bus bus;

    public static synchronized Bus getBus() {
        if (bus == null) {
            bus = new Bus(ThreadEnforcer.ANY);
        }
        return bus;
    }

}
