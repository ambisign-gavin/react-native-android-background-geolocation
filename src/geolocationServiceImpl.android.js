// @flow
import { NativeModules, DeviceEventEmitter } from 'react-native';
import { PriorityModeEnum } from './enum';
import { type Option, type Unsubscribe } from './type';
let geolocationService = NativeModules.GeolocationService;

export const Impl = {
    start(option?: Option) {
        if (option == null) {
            option = {
                priority: PriorityModeEnum.PRIORITY_BALANCED_POWER_ACCURACY,
                stopOnTerminate: false,
                interval: 25 * 1000,
                fastestInterval: 20 * 1000,
                distanceFilter: 100,
            };
        }
        geolocationService.start(option);
    },
    stop() {
        geolocationService.stop();
    },
    onLocationChanged(listener: ({ coords: { latitude: number, longitude: number } }) => void): Unsubscribe {
        DeviceEventEmitter.addListener('onLocationChanged', listener);
        return () => {
            DeviceEventEmitter.removeListener('onLocationChanged', listener);
        };
    },
    onError(listener: ({ code: number, message: string }) => void): Unsubscribe {
        DeviceEventEmitter.addListener('onError', listener);
        return () => {
            DeviceEventEmitter.removeListener('onError', listener);
        };
    },
    removeAllListeners() {
        DeviceEventEmitter.removeAllListeners('onLocationChanged');
        DeviceEventEmitter.removeAllListeners('onError');
    }
};