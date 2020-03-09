/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.ipupdater.internal.handler;

import org.eclipse.jdt.annotation.NonNull;

/**
 * @author Miika Jukka - Initial contribution
 *
 */
public interface BridgeListener {

    boolean registerIpStatusListener(@NonNull IpStatusListener ipStatusListener);

    boolean unregisterIpStatusListener(@NonNull IpStatusListener ipStatusListener);

    public String getAddressFromBridge(@NonNull Integer ipv4OrIpv6);
}
