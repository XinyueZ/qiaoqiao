# QiaoQiao, qiaoqiao

[![Build Status](https://travis-ci.org/XinyueZ/qiaoqiao.svg?branch=master)](https://travis-ci.org/XinyueZ/qiaoqiao)

- An Android-app that gives detected information about live shooting, based on Google AI.

- Based on Google's Vision API.

# CI and Checkout     
    
[![Build Status](https://travis-ci.org/XinyueZ/qiaoqiao.svg?branch=master)](https://travis-ci.org/XinyueZ/qiaoqiao)
 


- Use ```./gradlew clean assembleDebug -x :app:processLiveDebugGoogleServices -x :app:processDevDebugGoogleServices```

- Ignore config of Play Service, use own one if checkout this project.
    - ```app/src/dev/google-services.json```
    - ```app/src/live/google-services.json```
    - Take care in ```strings.xml```, when use own ```google-services.json```
        -     <!--<string name="web_client_id">@string/default_web_client_id</string>-->
              <string name="web_client_id">[FIXME: Here use @string/default_web_client_id instead]</string>
            

