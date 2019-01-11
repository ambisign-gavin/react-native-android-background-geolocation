// @flow
export type Priority = 10001 | 10002 | 10003 | 10004;

export type Option = {|
    priority: Priority,
    stopOnTerminate?: boolean,
    interval: number,
    fastestInterval: number,
    distanceFilter: number,
|}

export type Unsubscribe = () => void;