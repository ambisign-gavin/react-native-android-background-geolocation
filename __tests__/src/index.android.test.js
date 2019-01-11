// @flow
import { NativeModules, DeviceEventEmitter } from 'react-native';
import LocationService from '../../src';
import { PriorityModeEnum } from '../../src/enum';
import type { Option } from '../../src/type';

describe('LocationService on Android', () => {
    it('should start with undefined options when not pass any options', () => {
        LocationService.start();
        expect(NativeModules.GeolocationService.start.mock.calls.length).toEqual(1);
        expect(NativeModules.GeolocationService.start.mock.calls[0][0]).toBeUndefined();
    });

    it('should start with custom options', () => {
        let option: Option = {
            priority: PriorityModeEnum.PRIORITY_HIGH_ACCURACY,
            stopOnTerminate: true,
            interval: 60 * 1000,
            fastestInterval: 50 * 1000,
            distanceFilter: 20,
        };
        LocationService.start(option);
        expect(NativeModules.GeolocationService.start.mock.calls.length).toEqual(1);
        expect(NativeModules.GeolocationService.start.mock.calls[0][0]).toEqual(option);
    });

    it('should stop success', () => {
        LocationService.stop();
        expect(NativeModules.GeolocationService.stop.mock.calls.length).toEqual(1);
    });

    it('should add listener by onLocationChanged', () => {
        LocationService.onLocationChanged(jest.fn());
        expect(DeviceEventEmitter.addListener.mock.calls.length).toEqual(1);
        expect(DeviceEventEmitter.addListener.mock.calls[0][0]).toEqual('onLocationChanged');
    });

    it('should unsubscribe listener by the function that onLocationChanged returned', () => {
        const unsubscribe = LocationService.onLocationChanged(jest.fn());
        unsubscribe();
        expect(DeviceEventEmitter.removeListener.mock.calls.length).toEqual(1);
        expect(DeviceEventEmitter.removeListener.mock.calls[0][0]).toEqual('onLocationChanged');
    });

    it('should add listener by onError', () => {
        LocationService.onError(jest.fn());
        expect(DeviceEventEmitter.addListener.mock.calls.length).toEqual(1);
        expect(DeviceEventEmitter.addListener.mock.calls[0][0]).toEqual('onError');
    });

    it('should unsubscribe listener by the function that onError returned', () => {
        const unsubscribe = LocationService.onError(jest.fn());
        unsubscribe();
        expect(DeviceEventEmitter.removeListener.mock.calls.length).toEqual(1);
        expect(DeviceEventEmitter.removeListener.mock.calls[0][0]).toEqual('onError');
    });

    it('should removeAllListener', () => {
        LocationService.removeAllListeners();
        expect(DeviceEventEmitter.removeAllListeners.mock.calls.length).toEqual(2);
        expect(DeviceEventEmitter.removeAllListeners.mock.calls[0][0]).toEqual('onLocationChanged');
        expect(DeviceEventEmitter.removeAllListeners.mock.calls[1][0]).toEqual('onError');
    });

});