/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,constantProperty,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import { Directive, ElementRef, Input } from '@angular/core';
import { NSTableRowMover } from 'nstablerowmover';
/**
 * @record
 */
export function INSTableRowMoverSetting() { }
if (false) {
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.context;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.table;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.isSameTableMove;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.droppableTable;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.handler;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.dragStartHandler;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.dragHandler;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.dragEndHandler;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.customClass;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.enableAnimation;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.enableMovingText;
    /** @type {?|undefined} */
    INSTableRowMoverSetting.prototype.movingTextCallback;
}
var NSTableRowMoverAngularDirective = /** @class */ (function () {
    function NSTableRowMoverAngularDirective(elementRef) {
        this.elementRef = elementRef;
        this.__element = elementRef.nativeElement;
    }
    Object.defineProperty(NSTableRowMoverAngularDirective.prototype, "setting", {
        get: /**
         * @return {?}
         */
        function () {
            return this.__setting;
        },
        set: /**
         * @param {?} setting
         * @return {?}
         */
        function (setting) {
            this.__setting = setting;
            if (this.__setting) {
                this.__create();
            }
        },
        enumerable: true,
        configurable: true
    });
    ;
    /**
     * @return {?}
     */
    NSTableRowMoverAngularDirective.prototype.ngOnInit = /**
     * @return {?}
     */
    function () {
    };
    ;
    /**
     * @return {?}
     */
    NSTableRowMoverAngularDirective.prototype.ngOnDestroy = /**
     * @return {?}
     */
    function () {
        this.remove();
    };
    ;
    /**
     * @return {?}
     */
    NSTableRowMoverAngularDirective.prototype.processRows = /**
     * @return {?}
     */
    function () {
        this.__objNSTableRowMover.processRows();
    };
    ;
    /**
     * @return {?}
     */
    NSTableRowMoverAngularDirective.prototype.remove = /**
     * @return {?}
     */
    function () {
        this.__objNSTableRowMover.remove();
    };
    ;
    /**
     * @private
     * @return {?}
     */
    NSTableRowMoverAngularDirective.prototype.__create = /**
     * @private
     * @return {?}
     */
    function () {
        if (!this.__objNSTableRowMover && this.setting) {
            this.setting.table = this.__element;
            this.__objNSTableRowMover = new NSTableRowMover(this.setting);
        }
    };
    ;
    NSTableRowMoverAngularDirective.decorators = [
        { type: Directive, args: [{
                    selector: '[nsTableRowMoverAngular]'
                },] }
    ];
    /** @nocollapse */
    NSTableRowMoverAngularDirective.ctorParameters = function () { return [
        { type: ElementRef }
    ]; };
    NSTableRowMoverAngularDirective.propDecorators = {
        setting: [{ type: Input }]
    };
    return NSTableRowMoverAngularDirective;
}());
export { NSTableRowMoverAngularDirective };
if (false) {
    /**
     * @type {?}
     * @private
     */
    NSTableRowMoverAngularDirective.prototype.__element;
    /**
     * @type {?}
     * @private
     */
    NSTableRowMoverAngularDirective.prototype.__objNSTableRowMover;
    /**
     * @type {?}
     * @private
     */
    NSTableRowMoverAngularDirective.prototype.__setting;
    /**
     * @type {?}
     * @private
     */
    NSTableRowMoverAngularDirective.prototype.elementRef;
    /* Skipping unhandled member: ;*/
    /* Skipping unhandled member: ;*/
    /* Skipping unhandled member: ;*/
    /* Skipping unhandled member: ;*/
    /* Skipping unhandled member: ;*/
    /* Skipping unhandled member: ;*/
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibnNUYWJsZVJvd01vdmVyQW5ndWxhci5kaXJlY3RpdmUuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9ucy10YWJsZXJvd21vdmVyLWFuZ3VsYXIvIiwic291cmNlcyI6WyJsaWIvbnNUYWJsZVJvd01vdmVyQW5ndWxhci5kaXJlY3RpdmUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7OztBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsVUFBVSxFQUFFLEtBQUssRUFBcUIsTUFBTSxlQUFlLENBQUM7QUFFaEYsT0FBTyxFQUFDLGVBQWUsRUFBQyxNQUFNLGlCQUFpQixDQUFDOzs7O0FBRWhELDZDQWFDOzs7SUFaRywwQ0FBYzs7SUFDZCx3Q0FBVzs7SUFDWCxrREFBMEI7O0lBQzFCLGlEQUFvQjs7SUFDcEIsMENBQWE7O0lBQ2IsbURBQXNCOztJQUN0Qiw4Q0FBaUI7O0lBQ2pCLGlEQUFvQjs7SUFDcEIsOENBQWlCOztJQUNqQixrREFBMEI7O0lBQzFCLG1EQUEyQjs7SUFDM0IscURBQXVCOztBQUczQjtJQXVCSSx5Q0FBb0IsVUFBcUI7UUFBckIsZUFBVSxHQUFWLFVBQVUsQ0FBVztRQUVyQyxJQUFJLENBQUMsU0FBUyxHQUFHLFVBQVUsQ0FBQyxhQUFhLENBQUM7SUFDOUMsQ0FBQztJQXBCRCxzQkFBYSxvREFBTzs7OztRQVFwQjtZQUVJLE9BQU8sSUFBSSxDQUFDLFNBQVMsQ0FBQztRQUMxQixDQUFDOzs7OztRQVhELFVBQXFCLE9BQStCO1lBRWhELElBQUksQ0FBQyxTQUFTLEdBQUcsT0FBTyxDQUFDO1lBQ3pCLElBQUcsSUFBSSxDQUFDLFNBQVMsRUFDakI7Z0JBQ0ksSUFBSSxDQUFDLFFBQVEsRUFBRSxDQUFDO2FBQ25CO1FBQ0wsQ0FBQzs7O09BQUE7SUFhQSxDQUFDOzs7O0lBRUYsa0RBQVE7OztJQUFSO0lBR0EsQ0FBQztJQUFBLENBQUM7Ozs7SUFFRixxREFBVzs7O0lBQVg7UUFFSSxJQUFJLENBQUMsTUFBTSxFQUFFLENBQUM7SUFDbEIsQ0FBQztJQUFBLENBQUM7Ozs7SUFFRixxREFBVzs7O0lBQVg7UUFFSSxJQUFJLENBQUMsb0JBQW9CLENBQUMsV0FBVyxFQUFFLENBQUM7SUFDNUMsQ0FBQztJQUFBLENBQUM7Ozs7SUFFRixnREFBTTs7O0lBQU47UUFFSSxJQUFJLENBQUMsb0JBQW9CLENBQUMsTUFBTSxFQUFFLENBQUM7SUFDdkMsQ0FBQztJQUFBLENBQUM7Ozs7O0lBRU0sa0RBQVE7Ozs7SUFBaEI7UUFFSSxJQUFHLENBQUMsSUFBSSxDQUFDLG9CQUFvQixJQUFJLElBQUksQ0FBQyxPQUFPLEVBQzdDO1lBQ0ksSUFBSSxDQUFDLE9BQU8sQ0FBQyxLQUFLLEdBQUcsSUFBSSxDQUFDLFNBQVMsQ0FBQztZQUNwQyxJQUFJLENBQUMsb0JBQW9CLEdBQUcsSUFBSSxlQUFlLENBQUMsSUFBSSxDQUFDLE9BQU8sQ0FBQyxDQUFDO1NBQ2pFO0lBQ0wsQ0FBQztJQUFBLENBQUM7O2dCQXZETCxTQUFTLFNBQUM7b0JBQ1AsUUFBUSxFQUFFLDBCQUEwQjtpQkFDdkM7Ozs7Z0JBckJtQixVQUFVOzs7MEJBeUJ6QixLQUFLOztJQWtEVixzQ0FBQztDQUFBLEFBeERELElBd0RDO1NBcERZLCtCQUErQjs7Ozs7O0lBZXhDLG9EQUErQjs7Ozs7SUFDL0IsK0RBQWtDOzs7OztJQUNsQyxvREFBMkM7Ozs7O0lBRS9CLHFEQUE2QiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IERpcmVjdGl2ZSwgRWxlbWVudFJlZiwgSW5wdXQsIE9uRGVzdHJveSwgT25Jbml0IH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XHJcblxyXG5pbXBvcnQge05TVGFibGVSb3dNb3Zlcn0gZnJvbSAnbnN0YWJsZXJvd21vdmVyJztcclxuXHJcbmV4cG9ydCBpbnRlcmZhY2UgSU5TVGFibGVSb3dNb3ZlclNldHRpbmcge1xyXG4gICAgY29udGV4dD86IGFueSxcclxuICAgIHRhYmxlPzphbnksXHJcbiAgICBpc1NhbWVUYWJsZU1vdmU/OiBib29sZWFuLFxyXG4gICAgZHJvcHBhYmxlVGFibGU/OmFueSxcclxuICAgIGhhbmRsZXI/OmFueSxcclxuICAgIGRyYWdTdGFydEhhbmRsZXI/OmFueSxcclxuICAgIGRyYWdIYW5kbGVyPzphbnksXHJcbiAgICBkcmFnRW5kSGFuZGxlcj86YW55LFxyXG4gICAgY3VzdG9tQ2xhc3M/OmFueSxcclxuICAgIGVuYWJsZUFuaW1hdGlvbj86IGJvb2xlYW4sXHJcbiAgICBlbmFibGVNb3ZpbmdUZXh0PzogYm9vbGVhbixcclxuICAgIG1vdmluZ1RleHRDYWxsYmFjaz86YW55XHJcbn1cclxuXHJcbkBEaXJlY3RpdmUoe1xyXG4gICAgc2VsZWN0b3I6ICdbbnNUYWJsZVJvd01vdmVyQW5ndWxhcl0nXHJcbn0pXHJcblxyXG5leHBvcnQgY2xhc3MgTlNUYWJsZVJvd01vdmVyQW5ndWxhckRpcmVjdGl2ZSBpbXBsZW1lbnRzIE9uSW5pdCwgT25EZXN0cm95ICBcclxue1xyXG4gICAgQElucHV0KCkgc2V0IHNldHRpbmcoc2V0dGluZzpJTlNUYWJsZVJvd01vdmVyU2V0dGluZylcclxuICAgIHtcclxuICAgICAgICB0aGlzLl9fc2V0dGluZyA9IHNldHRpbmc7XHJcbiAgICAgICAgaWYodGhpcy5fX3NldHRpbmcpXHJcbiAgICAgICAge1xyXG4gICAgICAgICAgICB0aGlzLl9fY3JlYXRlKCk7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgZ2V0IHNldHRpbmcoKTogSU5TVGFibGVSb3dNb3ZlclNldHRpbmdcclxuICAgIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5fX3NldHRpbmc7XHJcbiAgICB9XHJcbiAgICBcclxuICAgIHByaXZhdGUgX19lbGVtZW50OiBIVE1MRWxlbWVudDtcclxuICAgIHByaXZhdGUgX19vYmpOU1RhYmxlUm93TW92ZXI6IGFueTtcclxuICAgIHByaXZhdGUgX19zZXR0aW5nOiBJTlNUYWJsZVJvd01vdmVyU2V0dGluZztcclxuICAgIFxyXG4gICAgY29uc3RydWN0b3IocHJpdmF0ZSBlbGVtZW50UmVmOkVsZW1lbnRSZWYpIFxyXG4gICAge1xyXG4gICAgICAgIHRoaXMuX19lbGVtZW50ID0gZWxlbWVudFJlZi5uYXRpdmVFbGVtZW50O1xyXG4gICAgfTtcclxuICAgIFxyXG4gICAgbmdPbkluaXQoKSA6IHZvaWRcclxuICAgIHtcclxuICAgICAgIFxyXG4gICAgfTtcclxuICAgIFxyXG4gICAgbmdPbkRlc3Ryb3koKTogdm9pZCBcclxuICAgIHtcclxuICAgICAgICB0aGlzLnJlbW92ZSgpO1xyXG4gICAgfTtcclxuICAgIFxyXG4gICAgcHJvY2Vzc1Jvd3MoKTp2b2lkXHJcbiAgICB7XHJcbiAgICAgICAgdGhpcy5fX29iak5TVGFibGVSb3dNb3Zlci5wcm9jZXNzUm93cygpO1xyXG4gICAgfTtcclxuICAgIFxyXG4gICAgcmVtb3ZlKCk6dm9pZFxyXG4gICAge1xyXG4gICAgICAgIHRoaXMuX19vYmpOU1RhYmxlUm93TW92ZXIucmVtb3ZlKCk7XHJcbiAgICB9O1xyXG4gICAgXHJcbiAgICBwcml2YXRlIF9fY3JlYXRlKCk6IHZvaWRcclxuICAgIHtcclxuICAgICAgICBpZighdGhpcy5fX29iak5TVGFibGVSb3dNb3ZlciAmJiB0aGlzLnNldHRpbmcpXHJcbiAgICAgICAge1xyXG4gICAgICAgICAgICB0aGlzLnNldHRpbmcudGFibGUgPSB0aGlzLl9fZWxlbWVudDtcclxuICAgICAgICAgICAgdGhpcy5fX29iak5TVGFibGVSb3dNb3ZlciA9IG5ldyBOU1RhYmxlUm93TW92ZXIodGhpcy5zZXR0aW5nKTtcclxuICAgICAgICB9XHJcbiAgICB9O1xyXG59XHJcbiJdfQ==



import { ElementRef, OnDestroy, OnInit } from '@angular/core';
export interface INSTableRowMoverSetting {
    context?: any;
    table?: any;
    isSameTableMove?: boolean;
    droppableTable?: any;
    handler?: any;
    dragStartHandler?: any;
    dragHandler?: any;
    dragEndHandler?: any;
    customClass?: any;
    enableAnimation?: boolean;
    enableMovingText?: boolean;
    movingTextCallback?: any;
}
export declare class NSTableRowMoverAngularDirective implements OnInit, OnDestroy {
    private elementRef;
    setting: INSTableRowMoverSetting;
    private __element;
    private __objNSTableRowMover;
    private __setting;
    constructor(elementRef: ElementRef);
    ngOnInit(): void;
    ngOnDestroy(): void;
    processRows(): void;
    remove(): void;
    private __create;
}
