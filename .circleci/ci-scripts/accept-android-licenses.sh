#!/usr/bin/env bash

export LICENSES_PATH="$ANDROID_HOME/licenses"
export ANDROID_SDK_LICENSE_PATH="$LICENSES_PATH/android-sdk-license"
export ANDROID_SDK_LICENSE_CONTENTS=$'\n8933bad161af4178b1185d1a37fbf41ea5269c55'

if [ ! -e ${ANDROID_SDK_LICENSE_PATH} ]; then
    echo "Android SDK license acceptance not found in '$LICENSES_PATH', creating it..."

    mkdir "$LICENSES_PATH" || true
    echo -e "$ANDROID_SDK_LICENSE_CONTENTS" > "$ANDROID_SDK_LICENSE_PATH"

    echo "Done."
else
    echo "No need to create license acceptance file, already found in: $LICENSES_PATH/"
fi
