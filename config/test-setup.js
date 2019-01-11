// @flow
jest.mock('react-native', () => {
    let DeviceEventEmitter = {
        addListener: jest.fn(),
        removeListener: jest.fn(),
        removeAllListeners: jest.fn(),
    };
    let NativeModules = {
        GeolocationService: {
            start: jest.fn(),
            stop: jest.fn(),
        }
    }
    return {
        NativeModules,
        DeviceEventEmitter,
    }
})
