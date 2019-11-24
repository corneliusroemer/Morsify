## Inspiration
Voice input is becoming increasingly important in the era of smaller and smaller devices. However, those without   voice are left behind. Even for those with vision, keyboards on smartwatches are notoriously tricky to use due to small screen real estate - meaning voice is often the only usable input method.

Many IoT devices have no speakers, meaning blind people cannot use acoustic screen readers.

We propose a general purpose input/output method using morse code for input via a single button and output via vibration - being usable even on small devices and by people who have neither vision nor hearing.

Trained morse operators can routinely reach speeds of 50 words per minute - faster than the average person using a smart phone keyboard (40 wpm).

## What it does
Our smartwatch app translates single button touch morse code into text. After entry, each character is repeated back to the user through watch vibrations in morse. This way, even blind deaf can check what they've entered is correct and proceed.

## How we built it
With Kotlin and Android studio on a Fossil smartwatch running Wear OS.

## Challenges we ran into
Couldn't get Android Studio to emulate virtual devices on a Windows Computer - getting nonsense error messages (enable setting X in BIOS, despite it being already enabled). It worked straightaway on MacOS.

Android doesn't provide a standard library for identifying swipes, only short and long presses are easy to detect without rewriting gesture recognizers.

## Accomplishments that we're proud of
Proof of concept of a working morse keyboard with vibration feedback on a smartwatch

## What we learned
Morse code!
Android development, in particular for WearOS. 

## What's next for Morsify: General Purpose I/O for blind-deaf people


## Special Thanks
This project has been implemented using part of the code and ideas from [Wear-Offline-Keyboard](https://github.com/GabrielMorenoIbarra/Wear-Offline-Notes).

## License

```
  Copyright (C) 2019 Cornelius Roemer
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```
