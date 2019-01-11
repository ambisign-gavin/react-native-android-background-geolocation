// @flow
import { NativeModules } from 'react-native';
import LocationService from '../../src';

global.console = {
    warn: jest.fn()
};

describe('LocationService on iOS', () => {
    it('should show warning messages when call start', () => {
        LocationService.start();
        expect(console.warn.mock.calls.length).toEqual(1);
        expect(NativeModules.GeolocationService.start.mock.calls.length).toEqual(0);
    });

    it('should show warning messages when call stop', () => {
        LocationService.stop();
        expect(console.warn.mock.calls.length).toEqual(1);
        expect(NativeModules.GeolocationService.start.mock.calls.length).toEqual(0);
    });

    it('should show warning messages when add listener by onLocationChanged', () => {
        LocationService.onLocationChanged(jest.fn());
        expect(console.warn.mock.calls.length).toEqual(1);
        expect(NativeModules.GeolocationService.start.mock.calls.length).toEqual(0);
    });

    it('should show warning messages when add listener by onError', () => {
        LocationService.onError(jest.fn());
        expect(console.warn.mock.calls.length).toEqual(1);
        expect(NativeModules.GeolocationService.start.mock.calls.length).toEqual(0);
    });

    it('should show warning messages when call removeAllListener', () => {
        LocationService.removeAllListeners();
        expect(console.warn.mock.calls.length).toEqual(1);
        expect(NativeModules.GeolocationService.start.mock.calls.length).toEqual(0);
    });

});