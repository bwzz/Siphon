package com.yuantiku.siphon.otto;

import com.squareup.otto.Bus;

/**
 * Created by wanghb on 15/8/16.
 */
public class BusFactory {
    private static Bus bus;

    public static synchronized Bus createBus() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }

}
