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
package org.openhab.binding.vallox.internal.mapper;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.vallox.internal.telegram.Converter;

/**
 * Class for temperature channels.
 *
 * @author Miika Jukka - Initial contribution
 */
@NonNullByDefault
public class TemperatureChannel extends ValloxChannel {

    public TemperatureChannel(byte variable) {
        super(variable);
    }

    @Override
    public State convertToState(Byte value) {
        return new DecimalType(Converter.temperatureToInt(value));
    }

    @Override
    public byte convertFromState(Byte value) {
        return Converter.temperatureToByte(value);
    }
}
