package com.youlongnet.lulu.ui.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public final class BusProvider {

    private static final BusWrapper BUS = new BusWrapper(ThreadEnforcer.MAIN);

    private BusProvider() {
    }

    public static BusWrapper getInstance() {
        return BUS;
    }

    public static class BusWrapper extends Bus {
        public BusWrapper(ThreadEnforcer main) {
            super(main);
        }

        final public void registers(Object... objs) {
            for (int i = 0; i < objs.length; i++)
                super.register(objs[i]);
        }

        ;

        final public void unregisters(Object... objs) {
            for (int i = 0; i < objs.length; i++)
                super.unregister(objs[i]);
        }

        ;
    }
}
