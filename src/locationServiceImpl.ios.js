// @flow
import { type Option, type Unsubscribe } from './type';

const notSupported = () => console.warn('GeolocationService does not support for iOS.');

export const Impl = {
    start(option?: Option) {
        notSupported();
    },
    stop() {
        notSupported();
    },
    onLocationChanged(listener: ({ coords: { latitude: number, longitude: number } }) => void): Unsubscribe {
        notSupported();
        return () => {
            notSupported();
        };
    },
    onError(listener: ({ code: number, message: string }) => void): Unsubscribe {
        notSupported();
        return () => {
            notSupported();
        };
    },
    removeAllListeners() {
        notSupported();
    }
};