// @flow
import { PriorityModeEnum, ErrorCodeEnum } from '../../src/enum';

describe('PriorityModeEnum', () => {
    it('should PRIORITY_BALANCED_POWER_ACCURACY equals priority code', () => {
        expect(PriorityModeEnum.PRIORITY_BALANCED_POWER_ACCURACY).toEqual(10001);
    });

    it('should PRIORITY_HIGH_ACCURACY equals priority code', () => {
        expect(PriorityModeEnum.PRIORITY_HIGH_ACCURACY).toEqual(10002);
    });

    it('should PRIORITY_LOW_POWER equals priority code', () => {
        expect(PriorityModeEnum.PRIORITY_LOW_POWER).toEqual(10003);
    });

    it('should PRIORITY_NO_POWER equals priority code', () => {
        expect(PriorityModeEnum.PRIORITY_NO_POWER).toEqual(10004);
    });
});

describe('ErrorCodeEnum', () => {
    it('should PERMISSIONS_DENIED equals error code', () => {
        expect(ErrorCodeEnum.PERMISSIONS_DENIED).toEqual(40001);
    });

    it('should PRIORITY_NOT_SUPPORTED equals error code', () => {
        expect(ErrorCodeEnum.PRIORITY_NOT_SUPPORTED).toEqual(40002);
    });

    it('should GOOGLE_PLAY_SERVICE_UPDATING equals error code', () => {
        expect(ErrorCodeEnum.GOOGLE_PLAY_SERVICE_UPDATING).toEqual(40003);
    });

    it('should GOOGLE_PLAY_SERVICE_NEED_UPDATE equals error code', () => {
        expect(ErrorCodeEnum.GOOGLE_PLAY_SERVICE_NEED_UPDATE).toEqual(40004);
    });

    it('should GOOGLE_PLAY_SERVICE_ERROR equals error code', () => {
        expect(ErrorCodeEnum.GOOGLE_PLAY_SERVICE_ERROR).toEqual(40005);
    });
});