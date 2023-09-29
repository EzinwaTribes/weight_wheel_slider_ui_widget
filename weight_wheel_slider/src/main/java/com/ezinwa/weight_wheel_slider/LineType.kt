package com.ezinwa.weight_wheel_slider

sealed class LineType {
    object Normal : LineType()
    object FiveStep : LineType()
    object TenStep : LineType()
}