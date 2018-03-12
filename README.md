# ViApp with Tensorflow-Lite

- Use Tensorflow-Lite to predicate camera frames.
- This is a standalone branch, it won't be merged in to [https://github.com/XinyueZ/qiaoqiao/tree/feature/dev](dev).
- An Android-app that gives detected information about live shooting, based on Google AI.
- Based on Google's Vision API.

# Task of this branch

- Using and learning how to integrate Tensorflow into mobile project.
- Learning and training models, converting into Tensorflow-Lite mobile.
- Evaluating ```offline``` predication for the ViApp.   
- Evaluating a solution that the ViApp supports image-predication with(out) [Vision API](https://cloud.google.com/vision/). 

# CI and Checkout     
    
[![Build Status](https://travis-ci.org/XinyueZ/qiaoqiao.svg?branch=master)](https://travis-ci.org/XinyueZ/qiaoqiao)
 
- Compile ```./gradlew clean assembleDebug```

- Build APK: Because I have ignored config of Play Service, you must use own one if checkout this project.
    - Dev version ```app/src/dev/google-services.json```
    - Live version ```app/src/live/google-services.json```
    - Complete ```buildtools/release.json``` for a release version.
    - Take care in ```strings.xml```, when use own ```google-services.json```
        -     <!--<string name="web_client_id">@string/default_web_client_id</string>-->
              <string name="web_client_id">[FIXME: Here use @string/default_web_client_id instead]</string>
        - Comment out ```gradle.startParameter.excludedTaskNames``` in ```build.gradle```
            

