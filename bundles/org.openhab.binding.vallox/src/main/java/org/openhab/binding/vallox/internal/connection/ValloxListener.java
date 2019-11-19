/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.vallox.internal.connection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.vallox.internal.telegram.Telegram;

/**
 * This interface defines interface to receive data from heat pump.
 *
 * @author Miika Jukka - Initial contribution
 */
@NonNullByDefault
public interface ValloxListener {

    /**
     * Receive telegram from vallox.
     */
    void telegramReceived(Telegram telegram);

    /**
     * Receiving error.
     *
     */
    void errorOccurred(String error);

}