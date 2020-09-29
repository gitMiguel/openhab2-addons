/**
 * Copyright 2017-2018 Gregory Moyer and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openhab.binding.lametrictime.api;

import javax.ws.rs.client.ClientBuilder;

import org.openhab.binding.lametrictime.api.cloud.CloudConfiguration;
import org.openhab.binding.lametrictime.api.cloud.LaMetricTimeCloud;
import org.openhab.binding.lametrictime.api.impl.LaMetricTimeImpl;
import org.openhab.binding.lametrictime.api.local.ApplicationActionException;
import org.openhab.binding.lametrictime.api.local.ApplicationActivationException;
import org.openhab.binding.lametrictime.api.local.ApplicationNotFoundException;
import org.openhab.binding.lametrictime.api.local.LaMetricTimeLocal;
import org.openhab.binding.lametrictime.api.local.LocalConfiguration;
import org.openhab.binding.lametrictime.api.local.NotificationCreationException;
import org.openhab.binding.lametrictime.api.local.UpdateException;
import org.openhab.binding.lametrictime.api.local.model.Application;
import org.openhab.binding.lametrictime.api.local.model.Audio;
import org.openhab.binding.lametrictime.api.local.model.Bluetooth;
import org.openhab.binding.lametrictime.api.local.model.Display;
import org.openhab.binding.lametrictime.api.local.model.UpdateAction;
import org.openhab.binding.lametrictime.api.local.model.Widget;
import org.openhab.binding.lametrictime.api.model.CoreAction;
import org.openhab.binding.lametrictime.api.model.CoreApplication;
import org.openhab.binding.lametrictime.api.model.CoreApps;
import org.openhab.binding.lametrictime.api.model.Icon;
import org.openhab.binding.lametrictime.api.model.enums.BrightnessMode;
import org.openhab.binding.lametrictime.api.model.enums.Priority;
import org.openhab.binding.lametrictime.api.model.enums.Sound;

public interface LaMetricTime
{
    /**
     * Get the version identifier reported by the device.
     *
     * @return the version
     */
    public String getVersion();

    /**
     * Send a low priority message to the device.
     *
     * @param message
     *            the text to display
     * @return the identifier of the newly created notification
     * @throws NotificationCreationException
     *             if there is a communication error or malformed data
     */
    public String notifyInfo(String message) throws NotificationCreationException;

    /**
     * Send a medium priority message to the device.
     *
     * @param message
     *            the text to display
     * @return the identifier of the newly created notification
     * @throws NotificationCreationException
     *             if there is a communication error or malformed data
     */
    public String notifyWarning(String message) throws NotificationCreationException;

    /**
     * Send an urgent message to the device. The notification will not be
     * automatically removed. The user will be required to dismiss this
     * notification or it must be deleted through he API.
     *
     * @param message
     *            the text to display
     * @return the identifier of the newly created notification
     * @throws NotificationCreationException
     *             if there is a communication error or malformed data
     */
    public String notifyCritical(String message) throws NotificationCreationException;

    /**
     * Send a notification to the device.
     *
     * Priority is important. It defines the urgency of this notification as
     * related to others in the queue and the current state of the device.
     * <ol>
     * <li>{@link Priority#INFO}: lowest priority; not shown when the
     * screensaver is active; waits for its turn in the queue
     * <li>{@link Priority#WARNING}: middle priority; not shown when the
     * screensaver is active; preempts {@link Priority#INFO}
     * <li>{@link Priority#CRITICAL}: highest priority; shown even when the
     * screensaver is active; preempts all other notifications (to be used
     * sparingly)
     * </ol>
     *
     * @param message
     *            the text to display
     * @param priority
     *            the urgency of this notification; defaults to
     *            {@link Priority#INFO}
     * @param icon
     *            the icon to display next to the message; can be
     *            <code>null</code>
     * @param sound
     *            the sound to play when the notification is displayed; can be
     *            <code>null</code>
     * @param messageRepeat
     *            the number of times the message should be displayed before
     *            being removed (use <code>0</code> to leave the notification on
     *            the device until it is dismissed by the user or deleted
     *            through the API)
     * @param soundRepeat
     *            the number of times to repeat the sound (use <code>0</code> to
     *            keep the sound looping until the notification is dismissed by
     *            the user or deleted through the API)
     * @return the identifier of the newly created notification
     * @throws NotificationCreationException
     *             if there is a communication error or malformed data
     */
    public String notify(String message,
                         Priority priority,
                         Icon icon,
                         Sound sound,
                         int messageRepeat,
                         int soundRepeat) throws NotificationCreationException;

    /**
     * Get the built-in clock application. This applications displays the time
     * and date. It also provides an alarm feature.
     *
     * @return the clock app
     */
    public Application getClock();

    /**
     * Get the built-in countdown timer application. This application counts
     * time down to zero when it sets off a beeper until it is reset. The
     * countdown can also be paused.
     *
     * @return the countdown app
     */
    public Application getCountdown();

    /**
     * Get the built-in radio application. The radio can play streams from the
     * Internet. The streams are set up in a list and can be navigated using
     * 'next' and 'previous' actions. The music can be started and stopped.
     *
     * @return the radio app
     */
    public Application getRadio();

    /**
     * Get the built-in stopwatch application. The stopwatch counts time
     * forwards and can be started, paused, and reset.
     *
     * @return the stopwatch app
     */
    public Application getStopwatch();

    /**
     * Get the built-in weather application. This application displays the
     * current weather conditions. It can also display the forecast for today
     * and tomorrow.
     *
     * @return the weather app
     */
    public Application getWeather();

    /**
     * Get any of the built-in applications.
     *
     * @param coreApp
     *            the app to retrieve
     * @return the requested app
     */
    public Application getApplication(CoreApplication coreApp);

    /**
     * Get any application installed on the device.
     *
     * @param name
     *            the name of the app to retrieve
     * @return the requested app
     * @throws ApplicationNotFoundException
     *             if the requested app is not found on the device
     */
    public Application getApplication(String name) throws ApplicationNotFoundException;

    /**
     * Display the given built-in application on the device.
     *
     * @param coreApp
     *            the app to activate
     */
    public void activateApplication(CoreApplication coreApp);

    /**
     * Display the first instance (widget) of the given application on the
     * device.
     *
     * @param app
     *            the app to activate
     * @throws ApplicationActivationException
     *             if the app fails to activate
     */
    public void activateApplication(Application app) throws ApplicationActivationException;

    /**
     * Display the given widget on the device. A widget is simply an instance of
     * an application. Some applications can be installed more than once (e.g.
     * the {@link CoreApps#weather() weather} app) and each instance is assigned
     * a widget.
     *
     * @param widget
     *            the application instance (widget) to activate
     * @throws ApplicationActivationException
     *             if the app fails to activate
     */
    public void activateWidget(Widget widget) throws ApplicationActivationException;

    /**
     * Perform the given action on the first instance (widget) of the
     * corresponding built-in application. The widget will activate
     * automatically before carrying out the action.
     *
     * @param coreAction
     *            the action to perform
     */
    public void doAction(CoreAction coreAction);

    /**
     * Perform the given action on the first instance (widget) of the given
     * application. The widget will activate automatically before carrying out
     * the action.
     *
     * @param app
     *            the app which understands the requested action
     * @param action
     *            the action to perform
     * @throws ApplicationActionException
     *             if the action cannot be performed
     */
    public void doAction(Application app, UpdateAction action) throws ApplicationActionException;

    /**
     * Perform the given core action on the given widget. A widget is simply an
     * instance of an application. Some applications can be installed more than
     * once (e.g. the {@link CoreApps#weather() weather} app) and each instance
     * is assigned a widget. The widget will activate automatically before
     * carrying out the action.
     *
     * @param widget
     *            the widget which understands the requested core action
     * @param action
     *            the action to perform
     * @throws ApplicationActionException
     *             if the action cannot be performed
     */
    public void doAction(Widget widget, CoreAction action) throws ApplicationActionException;

    /**
     * Perform the given action on the given widget. A widget is simply an
     * instance of an application. Some applications can be installed more than
     * once (e.g. the {@link CoreApps#weather() weather} app) and each instance
     * is assigned a widget. The widget will activate automatically before
     * carrying out the action.
     *
     * @param widget
     *            the widget which understands the requested action
     * @param action
     *            the action to perform
     * @throws ApplicationActionException
     *             if the action cannot be performed
     */
    public void doAction(Widget widget, UpdateAction action) throws ApplicationActionException;

    /**
     * Set the display brightness. The {@link #setBrightnessMode(BrightnessMode)
     * brightness mode} will also be set to {@link BrightnessMode#MANUAL}.
     *
     * @param brightness
     *            the brightness value to set (must be between 0 and 100,
     *            inclusive)
     * @return the updated state of the display
     * @throws UpdateException
     *             if the update failed
     */
    public Display setBrightness(int brightness) throws UpdateException;

    /**
     * Set the brightness mode on the display. {@link BrightnessMode#MANUAL}
     * will immediately respect the current brightness value while
     * {@link BrightnessMode#AUTO} will ignore the brightness value and set the
     * brightness based on ambient light intensity.
     *
     * @param mode
     *            the mode to set
     * @return the updated state of the display
     * @throws UpdateException
     *             if the update failed
     */
    public Display setBrightnessMode(BrightnessMode mode) throws UpdateException;

    /**
     * Set the speaker volume on the device.
     *
     * @param volume
     *            the volume to set (must be between 0 and 100, inclusive)
     * @return the update audio state
     * @throws UpdateException
     *             if the update failed
     */
    public Audio setVolume(int volume) throws UpdateException;

    /**
     * Mute the device's speakers. The current volume will be stored so that
     * {@link #unmute()} will restored it. If the volume is currently at zero,
     * no action will be taken.
     *
     * @return the update audio state
     * @throws UpdateException
     *             if the update failed
     */
    public Audio mute() throws UpdateException;

    /**
     * Restore the volume prior to {@link #mute()}. If the volume has not been
     * muted previously and the volume is currently zero, it will be set to 50%.
     *
     * @return the update audio state
     * @throws UpdateException
     *             if the update failed
     */
    public Audio unmute() throws UpdateException;

    /**
     * Set the active state of the Bluetooth radio on the device.
     *
     * @param active
     *            <code>true</code> to activate Bluetooth; <code>false</code> to
     *            deactive it
     * @return the updated state of Bluetooth on the device
     * @throws UpdateException
     *             if the update failed
     */
    public Bluetooth setBluetoothActive(boolean active) throws UpdateException;

    /**
     * Set the device name as seen via Bluetooth connectivity.
     *
     * @param name
     *            the name to display on other devices
     * @return the updated state of Bluetooth on the device
     * @throws UpdateException
     *             if the update failed
     */
    public Bluetooth setBluetoothName(String name) throws UpdateException;

    /**
     * Get the local API for more advanced interactions as well device inquiry.
     *
     * @return the local API
     */
    public LaMetricTimeLocal getLocalApi();

    /**
     * Get the cloud API for interacting with LaMetric's services.
     *
     * @return the cloud API
     */
    public LaMetricTimeCloud getCloudApi();

    /**
     * Create an instance of this API. For greater control over the
     * configuration, see {@link #create(Configuration, ClientBuilder)},
     * {@link #create(LocalConfiguration, CloudConfiguration)}, and
     * {@link #create(LocalConfiguration, CloudConfiguration, ClientBuilder)}.
     *
     * @param config
     *            the configuration parameters that the new instance will use
     * @return the API instance
     */
    public static LaMetricTime create(Configuration config)
    {
        return new LaMetricTimeImpl(config);
    }

    /**
     * Create an instance of this API. For greater control over the
     * configuration, see
     * {@link #create(LocalConfiguration, CloudConfiguration, ClientBuilder)}.
     *
     * @param config
     *            the configuration parameters that the new instance will use
     * @param clientBuilder
     *            the builder that will be used to create clients for
     *            communicating with the device and cloud services
     * @return the API instance
     */
    public static LaMetricTime create(Configuration config, ClientBuilder clientBuilder)
    {
        return new LaMetricTimeImpl(config, clientBuilder);
    }

    /**
     * Create an instance of this API specifying detailed configuration for both
     * the local and cloud APIs. See also
     * {@link #create(LocalConfiguration, CloudConfiguration, ClientBuilder)}.
     *
     * @param localConfig
     *            the local API configuration for the new instance
     * @param cloudConfig
     *            the cloud API configuration for the new instance
     * @return the API instance
     */
    public static LaMetricTime create(LocalConfiguration localConfig,
                                      CloudConfiguration cloudConfig)
    {
        return new LaMetricTimeImpl(localConfig, cloudConfig);
    }

    /**
     * Create an instance of this API specifying detailed configuration for both
     * the local and cloud APIs as well as the generic client.
     *
     * @param localConfig
     *            the local API configuration for the new instance
     * @param cloudConfig
     *            the cloud API configuration for the new instance
     * @param clientBuilder
     *            the builder that will be used to create clients for
     *            communicating with the device and cloud services
     * @return the API instance
     */
    public static LaMetricTime create(LocalConfiguration localConfig,
                                      CloudConfiguration cloudConfig,
                                      ClientBuilder clientBuilder)
    {
        return new LaMetricTimeImpl(localConfig, cloudConfig, clientBuilder);
    }
}