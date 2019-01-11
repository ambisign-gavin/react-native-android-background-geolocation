// @flow
import { Impl } from './src/geolocationServiceImpl';
import type { Option, Unsubscribe } from './src/type';
import { PriorityModeEnum, ErrorCodeEnum } from './src/enum';

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
