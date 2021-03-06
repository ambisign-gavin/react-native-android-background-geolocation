# React Native Android Background Geolocation

A react-native geolocation module for Android, which uses [Fused Location Provider API](https://developers.google.com/location-context/fused-location-provider/) to get location, and use the Service so that can run in the foreground and background even the app is terminated.

## Example

See [react-native-android-background-geolocation-demo](https://github.com/ambisign-gavin/react-native-android-background-geolocation-demo)

## Installation

#### 1. Install with npm

```sh
npm i --save react-native-android-background-geolocation
```

#### 2. Link the native module

###### Automatic _(Recommend)_

Use `react-native link` to automatic install android module.

```sh
react-native link react-native-android-background-geolocation
```

###### Manually

* in `android/app/build.gradle`:

```
dependencies {
    + compile project(':react-native-android-background-geolocation')
    ...
}
```

* in `android/settings.gradle`:

```
...
include ':app'
+ include ':react-native-android-background-geolocation'
+ project(':react-native-android-background-geolocation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-background-geolocation/android')
```

* in `MainApplication.java`:

```java
+ import com.gavin.RNAndroidGeolocationService.RNAndroidGeolocationServicePackage;
...
    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            ...
            + new RNAndroidGeolocationServicePackage()
        );
    }
```

#### 3. Add the service

 Add `<service/>` to your `AndroidManifest.xml`.

```xml
<application>
    ...
    <service android:name="com.gavin.RNAndroidGeolocationService.LocationService" />
    ...
</application>
```

## Usage

### Run in foreground and background

You need to call `LocationService.start()` and then register a listener by `LocationService.onLocationChanged`.

```js
export default class App extends Component<{}> {

    async componentDidMount() {
        // 1. Request permissions
        await this._requestLocationPermission();

        // 2. Register a event listener by OnError and onLocationChanged
        LocationService.onError(({code, message}) => {
            console.log('error code', code);
            console.log('error message', message);
        });

        LocationService.onLocationChanged(({coords}) => console.log('coords', coords));
    }

    componentWillUnmount() {
        // 3. Remove All listeners
        LocationService.removeAllListeners();
    }
    
    _startGeolocationService() {
        LocationService.start({
            priority: PriorityModeEnum.PRIORITY_HIGH_ACCURACY,
            stopOnTerminate: false,
            interval: 25 * 1000,
            fastestInterval: 20 * 1000,
            distanceFilter: 0,
        });
    }

    _stopGeolocationService() {
        LocationService.stop();
    }

    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.welcome}>Welcome to React Native!</Text>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => this._startGeolocationService()}
                >
                    <Text style={styles.buttonText} >Start Location Service</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => this._stopGeolocationService()}
                >
                    <Text style={styles.buttonText} >Stop Location Service</Text>
                </TouchableOpacity>
            </View>
        );
    }
}
```

### Continuously run when the app is terminated

The `react-native-android-background-geolocation` will invoke your App in the background when the app is terminated. But that will not execute any react components, which means that only execute your entry file, usually is `index.js`.

**index.js**

```js
// @flow
import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import LocationService from 'react-native-android-background-geolocation';

LocationService.onLocationChanged(({coords}) => {
    //do something, like update data
    console.log(coords)
});

AppRegistry.registerComponent(appName, () => App);

```

### Methods

* [start](#start)
* [stop](#stop)
* [onLocationChanged](#onLocationChanged)
* [onError](#onError)

<a id="start">**`start(option?)`**</a>

```js
LocationService.start(option?);
```

Start tracking device location with specific options.

**Parameters**

| NAME | TYPE | REQUIRED | DESCRIPTION |
| --- | --- | --- | --- |
| option | [Option](#Option) | No | Options for the Fused Location Provider API.

---

<a id="stop">**`stop()`**</a>

```js
LocationService.stop();
```

Stop tracking device location.

---

<a id="onLocationChanged">**`onLocationChanged(listener)`**</a>

```js
let unsubscribe = LocationService.onLocationChanged(LocationInfo => console.log('LocationInfo', LocationInfo));
```

Register a location change listener, and return the unsubscribe function.

*see the [LocationInfo](#LocationInfo) type*

---

<a id="onError">**`onError(listener)`**</a>

```js
let unsubscribe = LocationService.onError(ErrorInfo => console.log('ErrorInfo', ErrorInfo));
```

Register an error listener, and return the unsubscribe function.

*see the [ErrorInfo](#ErrorInfo) type*

---

### Types

* [Option](#Option)
* [LocationInfo](#LocationInfo)
* [ErrorInfo](#ErrorInfo)

<a id="Option">**`Option`**</a>

```js
import LocationService, { PriorityModeEnum } from 'react-native-android-background-geolocation';
...
LocationService.start({
    priority: PriorityModeEnum.PRIORITY_HIGH_ACCURACY,
    stopOnTerminate: false,
    interval: 25 * 1000,
    fastestInterval: 20 * 1000,
    distanceFilter: 0,
});
...
```

Options for the location service.

**Parameters**

| NAME | TYPE | REQUIRED | DESCRIPTION | DEFAULT |
| --- | --- | --- | --- | --- |
| priority | [PriorityModeEnum](#PriorityModeEnum) | YES | Set the priority of the request. | `PRIORITY_BALANCED_POWER_ACCURACY` |
| stopOnTerminate | boolean | NO | Whether this service is stopped or not when the app is closed. | `true` |
| interval | number | YES | The interval that can receive location information, in milliseconds. | 25000 ms |
| fastestInterval | number | YES | The fastest interval that can receive location information, in milliseconds. | 20000 ms |
| distanceFilter | number | YES | Set the minimum displacement between location updates in meters. | 100 m |

---

<a id="LocationInfo">**`LocationInfo`**</a>

**Structure**
```js
{
    coords: {
        latitude: number,
        longitude: number,
    }
}
```

The location information is the argument passed to the event listener.

---

<a id="ErrorInfo">**`ErrorInfo`**</a>

**Structure**
```js
{
    code: number,
    message: string,
}
```

The error code and an error message are the arguments passed to the event listener.

*See more details of description at [errors section](#Errors).*

---

### Enums

* [PriorityModeEnum](#PriorityModeEnum)
* [ErrorCodeEnum](#ErrorCodeEnum)

<a id="PriorityModeEnum">**`PriorityModeEnum`**</a>

```js
import { PriorityModeEnum } from 'react-native-android-background-geolocation';
```

This enum offers the priority of the start option.

| VALUE | DESCRIPTION |
| --- | --- |
| PRIORITY_BALANCED_POWER_ACCURACY | This will mixed-use GPS or wifi and cell towers to get the location.
| PRIORITY_HIGH_ACCURACY | This will use GPS to get the finest location available but use more power.
| PRIORITY_LOW_POWER | This will use wifi or cell towers to get the location.
| PRIORITY_NO_POWER | Your app does not trigger any location updates, but receives locations triggered by other apps.

---

<a id="ErrorCodeEnum">**`ErrorCodeEnum`**</a>

```js
import { ErrorCodeEnum } from 'react-native-android-background-geolocation';
```
This enum offers error codes that can be used at `if` or `switch-case` to distinguish the error.

| VALUE |
| --- |
| PERMISSIONS_DENIED |
| PRIORITY_NOT_SUPPORTED |
| GOOGLE_PLAY_SERVICE_UPDATING |
| GOOGLE_PLAY_SERVICE_NEED_UPDATE |
| GOOGLE_PLAY_SERVICE_ERROR |

---

### Errors<a id="Errors"></a>

| CODE | ENUM VALUE | DESCRIPTION |
| --- | --- | --- |
| 40001 | PERMISSIONS_DENIED | No permissions to access a location service.
| 40002 | PRIORITY_NOT_SUPPORTED | Use the unsupported priority.
| 40003 | GOOGLE_PLAY_SERVICE_UPDATING | Google Play Services is updating. |
| 40004 | GOOGLE_PLAY_SERVICE_NEED_UPDATE | Google Play Services version is too old, needs to update. |
| 40005 | GOOGLE_PLAY_SERVICE_ERROR | Google Play Services is unavailable, please check is installed or not.

## License

MIT
