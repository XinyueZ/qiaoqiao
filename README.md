# ViApp with [Vision API](https://cloud.google.com/vision/)

- Use [Vision API](https://cloud.google.com/vision/) to classify camera frames.
- An Android-app that gives detected information about live shooting, based on Google AI.
- Based on Google's [Vision API](https://cloud.google.com/vision/).

[![Showcase](https://lh3.googleusercontent.com/n7Mmjue0dMFD6v6km_2vQWf6BOznblfdo4V2ZpyI5Cy0iqV8v5IiTcgjc6QxaTgmfOtHzn8D_GS9MUzjSqgX=w1920-h1080-n-k-rw)](https://drive.google.com/file/d/1ll_fCFRAAcsZ9Ja9yDb2MguMjne0vz_O/view)

# CI and Checkout     
    
[![Build Status](https://travis-ci.org/XinyueZ/qiaoqiao.svg?branch=feature%2Ftensorflow-lite)](https://travis-ci.org/XinyueZ/qiaoqiao)
 
- Compile ```./gradlew clean assembleDebug```

- Build APK: Because I have ignored config of Play Service, you must use own one if checkout this project.
    - Dev version ```app/src/dev/google-services.json```
    - Live version ```app/src/live/google-services.json```
    - Complete ```buildtools/release.json``` for a release version.
    - Take care in ```strings.xml```, when use own ```google-services.json```
        -     <!--<string name="web_client_id">@string/default_web_client_id</string>-->
              <string name="web_client_id">[FIXME: Here use @string/default_web_client_id instead]</string>
        - Comment out ```gradle.startParameter.excludedTaskNames``` in ```build.gradle```
            
        

# License

``` 
                            MIT License

                Copyright (c) 2018 Chris Xinyue 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
