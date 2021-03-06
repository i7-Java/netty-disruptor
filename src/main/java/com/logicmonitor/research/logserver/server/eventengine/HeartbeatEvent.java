package com.logicmonitor.research.logserver.server.eventengine;

import com.logicmonitor.research.logserver.server.state.State;

/**
 * Created by jsong on 7/27/15.
 *
 * HeartbeatEvent is generated by Netty channel handler when it
 * receives a heartbeat message from a client.
 */
public class HeartbeatEvent implements IEvent {
    private final State _state;
    private final int _cliId;
    /**
     * The member list version reported by the client via the heartbeat message
     */
    private final long _clientVer;

    /**
     * @param clientVer the member list version reported by the client
     */
    public HeartbeatEvent(final State state, final int cliId, final long clientVer) {
        _state = state;
        _cliId = cliId;
        _clientVer = clientVer;
    }

    @Override
    public void run() {
        _state.lock();
        try {
            boolean toNotify = _state.heartbeat(_cliId, _clientVer);
            if (toNotify)
                _state.conditionSignal();
        }
        finally {
            _state.unlock();
        }
    }
}
