package com.rievo.library;

/**
 * Created by kwang on 2017-09-15.
 */

/**
 * Implement to receive onBack commands for a specific view group
 */
public interface Reversible {
    /**
     * Called when a back event deals with this specific ViewGroup. This will be called before the
     * back event is handled internally. Return true if back event was handled.
     * @return Return true if back event was handled
     */
    boolean onBack();
}
