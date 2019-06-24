LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ConversionLib
LOCAL_SRC_FILES := tempConvert.cpp
include $(BUILD_SHARED_LIBRARY)
