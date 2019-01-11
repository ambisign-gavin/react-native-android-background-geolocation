// @flow
import { Impl } from './locationServiceImpl';
import type { Option, Unsubscribe } from './type';
import { PriorityModeEnum, ErrorCodeEnum } from './enum';

const LocationService = {
    start(option?: Option) {
        Impl.start(option);
    },
    stop() {
        Impl.stop();
    },
    onLocationChanged(listener: ({ coords: { latitude: number, longitude: number } }) => void): Unsubscribe {
        return Impl.onLocationChanged(listener);
    },
    onError(listener: ({ code: number, message: string }) => void): Unsubscribe {
        return Impl.onError(listener);
    },
    removeAllListeners() {
        Impl.removeAllListeners();
    }
};

export default LocationService;

export {
    PriorityModeEnum,
    ErrorCodeEnum
};
